package isel.cn;

import com.google.cloud.pubsub.v1.AckReplyConsumer;
import com.google.cloud.pubsub.v1.MessageReceiver;
import com.google.pubsub.v1.PubsubMessage;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TranslationHandler implements MessageReceiver {

    private final Logger logger = Logger.getLogger(TranslationHandler.class.getName());

    public TranslationHandler(String accountType) {
        logger.log(Level.INFO, "app-translation " + accountType.toUpperCase() + " listening...");
    }

    @Override
    public void receiveMessage(PubsubMessage pubsubMessage, AckReplyConsumer ackReplyConsumer) {
        String text = String.valueOf(pubsubMessage.getData().toStringUtf8());
        Map<String, String> attrs = pubsubMessage.getAttributesMap();
        ackReplyConsumer.ack();
        logger.log(Level.INFO,"Message received: " + attrs.get("filename"));
        tryTranslationUpload(text, attrs);
    }

    private void tryTranslationUpload(String srcText, Map<String, String> attrs) {
        String targetLang = attrs.get("dest_lang");
        String fileName = attrs.get("filename");
        String sessionId = attrs.get("sessionId");
        String resultTxt = null;
        try {
            TranslationUpload translationUpload = new TranslationUpload();
            List<String> strings = translationUpload.detectLanguage(srcText);
            logger.log(Level.INFO, "Detected languages: " + String.join(" " , strings));
            resultTxt = translationUpload.translateText(new Translator(srcText, targetLang));
            logger.log(Level.INFO, "Translation successful to " + targetLang);
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Error in message translation to " + targetLang);
        }
        try {
            new FirestoreUpload().insertTranslation(fileName, srcText, resultTxt, sessionId);
            logger.log(Level.INFO, "Data of " + fileName + " successfully sent to Firestore.");
        } catch (ExecutionException | InterruptedException | IOException e) {
            logger.log(Level.SEVERE, "Document " + fileName + " already exists.");
        }
    }
}
