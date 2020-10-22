package isel.cn;

import com.google.api.core.ApiFuture;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class FirestoreUpload {
    private static final String COLLECTION_NAME = "pendingTranslations-cnText";
    public Firestore database;
    public CollectionReference colRef;

    public FirestoreUpload() throws IOException {
        database = FirestoreOptions
                .newBuilder()
                .setCredentials(GoogleCredentials.getApplicationDefault())
                .build()
                .getService();
        colRef = database.collection(COLLECTION_NAME);
    }

    public void insertTranslation(String filename, String srcTxt, String resTxt, String sessionId) throws ExecutionException, InterruptedException {
        DocumentReference docRef = colRef.document(filename);
        try {
            final ApiFuture<WriteResult> writeResultApiFuture = docRef.create(toMap(srcTxt, resTxt, sessionId));
            writeResultApiFuture.get();
        } catch (InterruptedException | ExecutionException e) {
            final ApiFuture<WriteResult> writeResultApiFuture = docRef.set(toMap(srcTxt, resTxt, sessionId));
            writeResultApiFuture.get();
        }

    }

    private Map<String, String> toMap(String srcTxt, String resTxt, String sessionId) {
        Map<String, String> fields = new HashMap<>();
        fields.put("text", srcTxt);
        fields.put("translated", resTxt);
        fields.put("sessionId", sessionId);
        return fields;
    }
}
