package firestore;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import credentials.Credentials;
import model.User;

import java.util.Objects;
import java.util.concurrent.ExecutionException;

public class UserRepository {
    private static final String COLLECTION_NAME = "users-cnText";
    public Firestore database;
    public CollectionReference colRef;

    public UserRepository() {
        database = FirestoreOptions.newBuilder().setCredentials(Credentials.credentials).build().getService();
        colRef = database.collection(COLLECTION_NAME);
    }

    public long insertUser(User user) throws ExecutionException, InterruptedException {
        String docId = user.getUsername();
        DocumentReference docRef = colRef.document(docId);
        final ApiFuture<WriteResult> writeResultApiFuture = docRef.create(user);
        writeResultApiFuture.get();
        return user.getSessionId();
    }

    public User checkIfUserExists(String username) throws ExecutionException, InterruptedException {
        Query query = database.collection(COLLECTION_NAME).whereEqualTo("username", username);
        DocumentSnapshot doc = query.get().get().getDocuments().get(0);
        return new User(Objects.requireNonNull(doc.getData()));
    }


}
