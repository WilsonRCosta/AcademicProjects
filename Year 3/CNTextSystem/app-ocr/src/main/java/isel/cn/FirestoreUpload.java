package isel.cn;

import com.google.api.core.ApiFuture;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.*;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

public class FirestoreUpload {
    private static final String COLLECTION_NAME = "pendingTasks-cnText";
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

    public void insertTask(Task pendingTask) throws InterruptedException, ExecutionException {
        String docId = pendingTask.getFilename();
        DocumentReference docRef = colRef.document(docId);
        try {
            final ApiFuture<WriteResult> writeResultApiFuture = docRef.create(pendingTask);
            writeResultApiFuture.get();
        } catch (ExecutionException e) {
            final ApiFuture<WriteResult> writeResultApiFuture = docRef.set(pendingTask);
            writeResultApiFuture.get();
        }
    }

}
