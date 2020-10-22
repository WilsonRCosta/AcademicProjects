package isel.cn;

import java.io.IOException;

public class App extends SubscriberBaseApp {

    public static void main(String[] args) throws IOException {
        if(args.length != 0) {
            setSubName(args[0]);
            setReceiver(new TaskHandler(args[0].split("-")[0]));
            getMessages();
        }
        else System.out.println("Error! Subscription not indicated: \n\tfree-ocr-sub\n\tpremium-ocr-sub");
    }
}
