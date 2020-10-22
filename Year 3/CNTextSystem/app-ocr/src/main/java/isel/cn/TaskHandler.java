package isel.cn;

import com.google.cloud.ReadChannel;
import com.google.cloud.pubsub.v1.AckReplyConsumer;
import com.google.cloud.pubsub.v1.MessageReceiver;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import com.google.pubsub.v1.PubsubMessage;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.WritableByteChannel;
import java.util.concurrent.ExecutionException;

public class TaskHandler implements MessageReceiver {

    private static final String BUCKET_NAME = "bucket-g06-cntext";
    private static final String ROOT_FOLDER = "/var/ocr/tmp/"; // VM
    //private static final String ROOT_FOLDER = "C:/Users/Tiago/Documents/ISEL/CN/tmp"; // LOCAL
    //private static final String ROOT_FOLDER = "D:/ISEL/_CN/temp"; // LOCAL

    private final Storage storage = StorageOptions.getDefaultInstance().getService();
    private final Logger logger = Logger.getLogger(TaskHandler.class.getName());

    private final StorageCleaner cleaner;
    private final FirestoreUpload firestoreUpload;
    private final String accountType;

    public TaskHandler(String accountType) throws IOException {
        this.accountType = accountType;
        logger.log(Level.INFO, "app-ocr listening...");
        cleaner = new StorageCleaner();
        firestoreUpload = new FirestoreUpload();
    }

    @Override
    public void receiveMessage(PubsubMessage pubsubMessage, AckReplyConsumer ackReplyConsumer) {
        String fileName = String.valueOf(pubsubMessage.getData().toStringUtf8());
        Map<String, String> attrs = pubsubMessage.getAttributesMap();
        String sessionId = fileName.split("-")[0];
        ackReplyConsumer.ack();
        logger.log(Level.INFO,"Message received: " + fileName);
        tryStorageDownload(fileName);
        String text = tryVisionUpload(fileName);
        if(attrs.containsKey("dest_lang"))
            tryTranslate(text, attrs.get("dest_lang"), sessionId);
    }

    private void tryStorageDownload(String fileName) {
        BlobId blobId = BlobId.of(BUCKET_NAME, fileName);
        Blob blob = storage.get(blobId);
        try {
            PrintStream writeTo = new PrintStream(new FileOutputStream(new File(ROOT_FOLDER.concat(fileName))));
            if (blob.getSize() < 1_000_000) {
                byte[] content = blob.getContent();
                writeTo.write(content);
            } else {
                try (ReadChannel reader = blob.reader()) {
                    WritableByteChannel channel = Channels.newChannel(writeTo);
                    ByteBuffer bytes = ByteBuffer.allocate(64 * 1024);
                    while (reader.read(bytes) > 0) {
                        bytes.flip();
                        channel.write(bytes);
                        bytes.clear();
                    }
                }
            }
            writeTo.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String tryVisionUpload(String fileName){
        VisionUpload visionUpload = new VisionUpload(ROOT_FOLDER.concat(fileName));
        String text = null;
        try {
            text = visionUpload.upload();
            cleaner.deleteBlob(fileName);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            firestoreUpload.insertTask( new Task(fileName, text) );
            logger.log(Level.INFO, "Data of " + fileName + " successfully sent to Firestore.");
        } catch (ExecutionException | InterruptedException e) {
            logger.log(Level.SEVERE,"Document " + fileName + " already exists.");
        }
        return text;
    }

    private void tryTranslate(String text, String destLang, String sessionId) {
        String filename = destLang + "-" + Math.abs(text.hashCode());
        MessageUpload upload = new MessageUpload(filename, accountType);
        try {
            Map<String, String> attrs = new HashMap<>();
            attrs.put("filename", filename);
            attrs.put("dest_lang", destLang);
            attrs.put("sessionId", sessionId);
            upload.uploadMessage(text, attrs);
            logger.log(Level.INFO,"Message published to translation");
        } catch (IOException e) {
            logger.log(Level.SEVERE,"Error on publishing message");
        }
    }
}
