package pubsub;

import com.google.cloud.pubsub.v1.Publisher;
import com.google.protobuf.ByteString;
import com.google.pubsub.v1.PubsubMessage;
import com.google.pubsub.v1.TopicName;
import model.Account;

import java.io.IOException;
import java.util.Map;

public class MessageUpload {
    private static final String PROJECT_ID = "g06-li62d-v1920";
    private static String TOPIC_NAME;
    private final String filename;
    private final long sessionId;

    public MessageUpload(Account accountType, String blobName, long sessionId) {
        switch (accountType) {
            case FREE:
                TOPIC_NAME = "free-ocr"; break;
            case PREMIUM:
                TOPIC_NAME = "premium-ocr"; break;
            default: break;
        }
        this.filename = blobName;
        this.sessionId = sessionId;
    }

    public void uploadMessage(Map<String, String> attrs) throws IOException {
        TopicName tName = TopicName.ofProjectTopicName(PROJECT_ID, TOPIC_NAME);
        Publisher publisher = Publisher.newBuilder(tName).build();
        ByteString msgData = ByteString.copyFromUtf8(filename);
        PubsubMessage pubsubMessage;
        PubsubMessage.Builder messageBuilder = PubsubMessage.newBuilder()
                .setMessageId(String.valueOf(sessionId))
                .setData(msgData);
        if (!attrs.isEmpty()) {
            messageBuilder.putAllAttributes(attrs);
        }
        pubsubMessage = messageBuilder.build();
        publisher.publish(pubsubMessage);
        publisher.shutdown();
    }
}
