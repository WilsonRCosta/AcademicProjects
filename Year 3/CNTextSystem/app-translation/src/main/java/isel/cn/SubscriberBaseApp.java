package isel.cn;

import com.google.cloud.pubsub.v1.MessageReceiver;
import com.google.cloud.pubsub.v1.Subscriber;
import com.google.pubsub.v1.ProjectSubscriptionName;

public abstract class SubscriberBaseApp {
    public static final String PROJECT_ID = "g06-li62d-v1920";
    private static String subName;
    private static MessageReceiver receiver;

    public static void setSubName(String subName) {
        SubscriberBaseApp.subName = subName;
    }

    public static void setReceiver(MessageReceiver receiver) {
        SubscriberBaseApp.receiver = receiver;
    }

    public static void getMessages() {
        ProjectSubscriptionName subscriptionName = ProjectSubscriptionName.of(PROJECT_ID, subName);
        Subscriber subscriber = null;
        try {
            subscriber = Subscriber.newBuilder(subscriptionName, receiver).build();
            subscriber.startAsync().awaitRunning();
            subscriber.awaitTerminated();
        } finally {
            if (subscriber != null) {
                subscriber.stopAsync();
            }
        }
    }
}
