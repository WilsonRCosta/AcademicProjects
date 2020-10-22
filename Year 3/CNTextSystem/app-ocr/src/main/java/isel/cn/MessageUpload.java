package isel.cn;

import com.google.cloud.pubsub.v1.Publisher;
import com.google.protobuf.ByteString;
import com.google.pubsub.v1.PubsubMessage;
import com.google.pubsub.v1.TopicName;

import java.io.IOException;
import java.util.Map;

public class MessageUpload {
    private static final String PROJECT_ID = "g06-li62d-v1920";
    private static String topicName;
    private final String filename;

    public MessageUpload(String filename, String accountType) {
        this.filename = filename;
        setTopicName(accountType);
    }

    private void setTopicName(String accountType) {
        switch (accountType) {
            case "free" : topicName = "free-translation"; break;
            case "premium" : topicName = "premium-translation"; break;
            default: break;
        }
    }

    public void uploadMessage(String text, Map<String, String> attrs) throws IOException {
        TopicName tName = TopicName.ofProjectTopicName(PROJECT_ID, topicName);
        Publisher publisher = Publisher.newBuilder(tName).build();
        ByteString msgData = ByteString.copyFromUtf8(text);
        PubsubMessage pubsubMessage = PubsubMessage.newBuilder()
                .setData(msgData)
                .putAllAttributes(attrs)
                .build();
        publisher.publish(pubsubMessage);
        publisher.shutdown();
    }

}
