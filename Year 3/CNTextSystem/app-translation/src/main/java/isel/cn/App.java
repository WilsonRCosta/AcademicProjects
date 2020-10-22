package isel.cn;

public class App extends SubscriberBaseApp {

    public static void main(String[] args) {
        if(args.length != 0) {
            setSubName(args[0]);
            setReceiver(new TranslationHandler(args[0].split("-")[0]));
            getMessages();
        }
        else System.out.println("Error! Subscription not indicated: \n\tfree-translation-sub\n\tpremium-translation-sub");
    }
}
