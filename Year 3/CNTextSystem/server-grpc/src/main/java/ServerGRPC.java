import com.google.protobuf.ByteString;
import computeEngine.InstanceGroupManager;
import credentials.Credentials;
import firestore.TextRepository;
import firestore.UserRepository;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.stub.StreamObserver;
import model.Account;
import model.User;
import pubsub.MessageUpload;
import storage.StorageUpload;
import textservice.HashFile;
import textservice.Result;
import textservice.Session;
import textservice.TextServiceGrpc.TextServiceImplBase;
import textservice.Void;

import java.io.*;
import java.nio.file.Paths;
import java.security.GeneralSecurityException;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ServerGRPC extends TextServiceImplBase {

    private static final String rootFolder = "/var/ocr/tmp/";
    //private static final String rootFolder = "D:/ISEL/_CN/temp/";

    private static List<Long> currentFreeUsers;
    private static List<Long> currentPremiumUsers;
        private static Server server;

    private static UserRepository userRepository;
    private static TextRepository textRepository;

    private static StorageUpload storageUpload;
    private static InstanceGroupManager instanceGroupManager;

    private static final Logger logger = Logger.getLogger(ServerGRPC.class.getName());

    public static void main(String[] args) {
        if (args.length != 1) {
            System.err.println("Usage: {port}");
            System.exit(-1);
        }

        startServer(Integer.parseInt(args[0]));
        new Scanner(System.in).nextLine();
        stopServer();
        System.exit(0);
    }

    public static void startServer(int port) {
        try {
            Credentials.setCredentials();
            server = ServerBuilder
                    .forPort(port)
                    .addService(new ServerGRPC())
                    .build()
                    .start();
            currentFreeUsers = new ArrayList<>();
            currentPremiumUsers = new ArrayList<>();
            textRepository = new TextRepository();
            userRepository = new UserRepository();
            storageUpload = new StorageUpload();
            instanceGroupManager = new InstanceGroupManager();
        } catch (IOException | GeneralSecurityException e) {
            logger.log(Level.SEVERE, e.getMessage());
        }
        logger.log(Level.INFO, "server listening...");
    }

    private static void stopServer() {
        logger.log(Level.INFO, "server shutting down...");
        server.shutdown();
    }

    @Override
    public void signUp(textservice.User req, StreamObserver<Session> response) {
        long sessionID = -1;
        Account type = Account.valueOf(req.getAccountType());
        try {
            sessionID = userRepository.insertUser(
                    new User(req.getUsername(), req.getPassword(), type));
        } catch (ExecutionException | InterruptedException e) {
            response.onError(new Throwable("Error: User already exists!"));
        }
        addUser(req, sessionID, type);

        setSessionResponse(response, sessionID, type);
    }

    @Override
    public void login(textservice.User req, StreamObserver<Session> response) {
        long sessionID = -1;
        Account type = null;
        try {
            User user = userRepository.checkIfUserExists(req.getUsername());
            if (req.getPassword().equals(user.getPassword())) {
                sessionID = user.getSessionId();
                type = user.getAccountType();
            } else {
                response.onError(new Throwable("Error: Wrong Password!"));
            }
        } catch (ExecutionException | InterruptedException e) {
            response.onError(new Throwable("Error: User does not exist!"));
        }
        addUser(req, sessionID, type);
        setSessionResponse(response, sessionID, type);
    }

    private void addUser(textservice.User user, long sessionID, Account type) {
        List<Long> list;
        if (type == Account.FREE) {
            list = currentFreeUsers;
        } else {
            list = currentPremiumUsers;
            try {
                instanceGroupManager.addInstance();
            } catch (IOException | InterruptedException e) {
                logger.log(Level.SEVERE, e.getMessage());
            }
        }

        list.add(sessionID);
        int totalUsers = currentFreeUsers.size() + currentPremiumUsers.size();
        logger.log(Level.INFO, "User '" + user.getUsername() + "' signed up. (TOTAL " + type.toString() + ":" + list.size() + ")");
        logger.log(Level.INFO, "TOTAL: " + totalUsers);
    }

    @Override
    public void logout(Session req, StreamObserver<Void> response) {
        removeUser(req);
        response.onNext(null);
        response.onCompleted();
    }

    private void removeUser(Session session) {
        Account type = Account.valueOf(session.getAccountType());
        Long id = session.getSessionId();
        List<Long> list;
        if (type == Account.FREE) {
            list = currentFreeUsers;
        } else {
            list = currentPremiumUsers;
            try {
                instanceGroupManager.removeInstance();
            } catch (IOException | InterruptedException e) {
                logger.log(Level.SEVERE, e.getMessage());
            }
        }
        list.remove(id);
        int totalUsers = currentFreeUsers.size() + currentPremiumUsers.size();
        logger.log(Level.INFO, "UserID '" + session.getSessionId() + "' logged out. (TOTAL " + type.toString() + ":" + list.size() + ")");
        logger.log(Level.INFO, "TOTAL: " + totalUsers);

    }

    public void setSessionResponse(StreamObserver<Session> response, long sessionID, Account type) {
        Session session = Session.newBuilder()
                .setSessionId(sessionID)
                .setAccountType(String.valueOf(type))
                .build();
        response.onNext(session);
        response.onCompleted();
    }

    @Override
    public void uploadFile(textservice.File req, StreamObserver<HashFile> response) {
        logger.log(Level.INFO, "uploading new file...");
        ByteString fileBytes = req.getFileBytes();
        String filename = rootFolder + req.getSessionId() + "-" + Math.abs(fileBytes.hashCode());

        try (OutputStream stream = new FileOutputStream(new File(filename))) {
            stream.write(fileBytes.toByteArray());
            String blobName = storageUpload.uploadImage(Paths.get(filename), req.getSessionId());
            Map<String, String> attrs = new HashMap<>();
            if (req.getTranslate())
                attrs.put("dest_lang", req.getTargetLanguage());
            MessageUpload messageUpload = new MessageUpload(
                    Account.valueOf(req.getAccountType()), blobName, req.getSessionId()
            );
            messageUpload.uploadMessage(attrs);
            HashFile hashFile = HashFile.newBuilder().setFilename(blobName).build();
            response.onNext(hashFile);
            response.onCompleted();
        } catch (IOException e) {
            response.onError(e.getCause());
        }
    }

    @Override
    public void getHashFiles(Session req, StreamObserver<HashFile> response) {
        logger.log(Level.INFO, "showing all files uploaded by user: " + req.getSessionId());
        try {
            textRepository
                    .getDocumentsBySessionId(req.getSessionId(), "task")
                    .forEach((fileName) -> {
                        HashFile hashFile = HashFile.newBuilder().setFilename(fileName).build();
                        response.onNext(hashFile);
                    });
            response.onCompleted();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void getTranslatedFiles(Session req, StreamObserver<HashFile> response) {
        logger.log(Level.INFO, "showing all translated files by user: " + req.getSessionId());
        try {
            textRepository
                    .getDocumentsBySessionId(req.getSessionId(), "translation")
                    .forEach((fileName) -> {
                        HashFile hashFile = HashFile.newBuilder().setFilename(fileName).build();
                        response.onNext(hashFile);
                    });
            response.onCompleted();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void showResult(HashFile req, StreamObserver<Result> response) {
        logger.log(Level.INFO, "showing file result of '" + req.getFilename() + "'");
        try {
            Object text = textRepository.showDocumentText(String.valueOf(req.getFilename()), req.getSessionId());
            Result result = Result.newBuilder().setText(text.toString()).build();
            response.onNext(result);
            response.onCompleted();
        } catch (ExecutionException | InterruptedException e) {
            response.onError(new Throwable("Error: Text not found for file " + req.getFilename()));
        }
    }

    @Override
    public void showTranslatedResult(HashFile req, StreamObserver<Result> response) {
        logger.log(Level.INFO, "showing translated file result of '" + req.getFilename() + "'");
        try {
            String text = String.valueOf(
                    textRepository.showTranslatedDocumentText(String.valueOf(req.getFilename()), req.getSessionId()));
            Result result = Result.newBuilder().setTranslated(text).build();
            response.onNext(result);
            response.onCompleted();
        } catch (ExecutionException | InterruptedException e) {
            response.onError(new Throwable("Error: Text not found for file " + req.getFilename()));
        }
    }
}