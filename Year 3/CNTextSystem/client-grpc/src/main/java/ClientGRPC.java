import com.google.common.util.concurrent.ListenableFuture;
import com.google.protobuf.ByteString;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import textservice.*;
import textservice.TextServiceGrpc.TextServiceFutureStub;
import textservice.Void;
import textservice.TextServiceGrpc.TextServiceStub;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.ExecutionException;

public class ClientGRPC {

    private static final Scanner input = new Scanner(System.in);
    private static String username, password;

    private static long sessionId;
    private static String accountType;
    private static TextServiceFutureStub textFutureStub;
    private static TextServiceStub textStreamStub;

    public static void main(String[] args) {
        if (args.length != 1) {
            System.err.println("Usage: {ip}:{port}");
            System.exit(-1);
        }
        buildChannels(args[0].split(":"));
        System.out.print("Authenticate:\n\t1 - SignUp\n\t2 - Login\n- ");
        switch (input.nextLine()) {
            case "1": signUp(); break;
            case "2": login(); break;
            default: break;
        }
    }

    private static void menu() {
        boolean end = false;
        while (!end) {
            System.out.print("\nMENU:\n\t1 - Upload File\n\t2 - Show File Result\n\t3 - Show Translated Result\n\t4 - Logout\n- ");
            switch (input.nextLine()) {
                case "1": uploadFile(); break;
                case "2": showResult(); break;
                case "3": showTranslatedResult(); break;
                case "4": logout(); end = true; break;
            }
        }
    }

    private static void signUp() {
        setCredentials();
        System.out.print("Account Type [FREE/PREMIUM]: ");
        accountType = input.nextLine().toUpperCase();
        if (!accountType.equals("FREE") && !accountType.equals("PREMIUM")) {
            System.err.println("Error: Account type invalid!");
            return;
        }
        User user = User.newBuilder()
                .setUsername(username)
                .setPassword(password)
                .setAccountType(accountType)
                .build();
        try {
            ListenableFuture<Session> lf = textFutureStub.signUp(user);
            while (!lf.isDone()) {
                System.out.print(".");
                Thread.sleep(500);
            }
            sessionId = lf.get().getSessionId();
            System.out.println("\nSession ID: " + sessionId);
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        menu();
    }

    private static void login() {
        setCredentials();
        User user = User.newBuilder()
                .setUsername(username)
                .setPassword(password)
                .build();
        try {
            ListenableFuture<Session> lf = textFutureStub.login(user);
            while (!lf.isDone()) {
                System.out.print(".");
                Thread.sleep(500);
            }
            sessionId = lf.get().getSessionId();
            accountType = lf.get().getAccountType();
            System.out.println("\nSession ID: " + sessionId + "\nAccount Type: " + accountType);
            menu();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }

    private static void logout() {
        Session session = Session.newBuilder().setSessionId(sessionId).setAccountType(accountType).build();
        try {
            ListenableFuture<Void> lf = textFutureStub.logout(session);
            while (!lf.isDone()) {
                System.out.print(".");
                Thread.sleep(500);
            }
            System.out.println("\nUser logged out.");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static void buildChannels(String[] addr) {
        ManagedChannel channel = ManagedChannelBuilder
                .forAddress(addr[0], Integer.parseInt(addr[1])).usePlaintext().build();
        textFutureStub = TextServiceGrpc.newFutureStub(channel);
        textStreamStub = TextServiceGrpc.newStub(channel);
    }

    private static void setCredentials() {
        System.out.print("Username: ");
        username = input.nextLine();
        System.out.print("Password: ");
        password = input.nextLine();
    }

    private static void uploadFile() {
        System.out.print("Image to process: ");
        String path = input.nextLine();
        byte[] bytes = {};
        try {
            bytes = Files.readAllBytes(Paths.get(path));
        } catch (IOException e) {
            System.err.println("Error! File not found.");
        }
        System.out.print("Translate [Y/N] : ");
        boolean toTranslate = input.nextLine().equalsIgnoreCase("Y");
        File.Builder fileBuilder = File.newBuilder()
                .setFileBytes(ByteString.copyFrom(bytes))
                .setSessionId(sessionId)
                .setTranslate(toTranslate)
                .setAccountType(accountType);
        if (toTranslate) {
            System.out.print("To (pt, en, es, it): ");
            fileBuilder.setTargetLanguage(input.nextLine());
        }
        File file = fileBuilder.build();
        try {
            ListenableFuture<HashFile> lf = textFutureStub.uploadFile(file);
            while (!lf.isDone()) {
                System.out.print(".");
                Thread.sleep(500);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static void showResult() {
        List<String> hashFiles = getHashFiles();
        if (hashFiles.size() == 0) {
            System.err.println("No files to visualize.");
            return;
        }
        System.out.println("Files to visualize: ");
        hashFiles.forEach(System.out::println);
        System.out.print("Choose one: ");
        String file = input.nextLine();
        HashFile hashFile = HashFile.newBuilder()
                .setFilename(file)
                .setSessionId(sessionId)
                .build();
        try {
            ListenableFuture<Result> lf = textFutureStub.showResult(hashFile);
            while (!lf.isDone()) {
                System.out.print(".");
                Thread.sleep(500);
            }
            String text = lf.get().getText();
            System.out.println("\n" + text);
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }

    private static void showTranslatedResult() {
        List<String> translatedFiles = getTranslatedFiles();
        if (translatedFiles.size() == 0) {
            System.err.println("No files to visualize.");
            return;
        }
        System.out.println("Files to visualize: ");
        translatedFiles.forEach(System.out::println);
        System.out.print("Choose translated file: ");
        String file = input.nextLine();
        HashFile hashFile = HashFile.newBuilder()
                .setFilename(file)
                .setSessionId(sessionId)
                .build();
        try {
            ListenableFuture<Result> lf = textFutureStub.showTranslatedResult(hashFile);
            while (!lf.isDone()) {
                System.out.print(".");
                Thread.sleep(500);
            }
            String text = lf.get().getTranslated();
            System.out.println("\n" + text);
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }

    private static List<String> getHashFiles() {
        FilesStreamObserver observer = new FilesStreamObserver();
        Session session = Session.newBuilder().setSessionId(sessionId).build();
        textStreamStub.getHashFiles(session, observer);
        while (!observer.isCompleted()) {
            try {
                System.out.print(".");
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return observer.getFileNames();
    }

    private static List<String> getTranslatedFiles() {
        FilesStreamObserver observer = new FilesStreamObserver();
        Session session = Session.newBuilder().setSessionId(sessionId).build();
        textStreamStub.getTranslatedFiles(session, observer);
        while (!observer.isCompleted()) {
            try {
                System.out.print(".");
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return observer.getFileNames();
    }
}
