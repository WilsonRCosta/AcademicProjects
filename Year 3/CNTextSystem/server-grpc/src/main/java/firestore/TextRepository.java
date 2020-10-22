package firestore;

import com.google.api.core.ApiFuture;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class TextRepository {

    private static Firestore database;
    private static final String TASK_COLLECTION = "pendingTasks-cnText";
    private static final String TRANSLATION_COLLECTION = "pendingTranslations-cnText";

    public TextRepository() {
        try {
            database = FirestoreOptions
                    .newBuilder()
                    .setCredentials(GoogleCredentials.getApplicationDefault())
                    .build()
                    .getService();
        } catch (IOException e) {
            System.err.print(e.getMessage());
        }
    }

    public Object showDocumentText(String filename, long currentId) throws ExecutionException, InterruptedException {
        DocumentReference docRef = database.collection(TASK_COLLECTION).document(filename);
        String sessionId = String.valueOf(docRef.get().get().get("sessionId"));
        if(currentId == Long.parseLong(sessionId))
            return docRef.get().get().get("text");
        return new Throwable("Error! User does not have permissions to view result of " + filename);
    }

    public Object showTranslatedDocumentText(String filename, long currentId) throws ExecutionException, InterruptedException {
        DocumentReference docRef = database.collection(TRANSLATION_COLLECTION).document(filename);
        String sessionId = String.valueOf(docRef.get().get().get("sessionId"));
        if(currentId == Long.parseLong(sessionId))
            return docRef.get().get().get("translated");
        return new Throwable("Error! User does not have permissions to view result of " + filename);
    }

    public List<String> getDocumentsBySessionId(long sessionId, String collection) throws ExecutionException, InterruptedException {
        String collectionName;
        if(collection.equals("task")) collectionName = TASK_COLLECTION;
        else collectionName = TRANSLATION_COLLECTION;
        Query query = database.collection(collectionName).whereEqualTo("sessionId", String.valueOf(sessionId));
        List<String> taskFiles = new ArrayList<>();
        ApiFuture<QuerySnapshot> querySnapshot = query.get();
        for (DocumentSnapshot doc: querySnapshot.get().getDocuments())
            taskFiles.add(doc.getId());
        return taskFiles;
    }
}
