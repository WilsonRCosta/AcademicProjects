package isel.cn;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;

import java.io.IOException;

public class StorageCleaner {
    private static final String BUCKET_NAME = "bucket-g06-cntext";
    public Storage storage;

    public StorageCleaner() throws IOException {
        storage = StorageOptions
                .newBuilder()
                .setCredentials(GoogleCredentials.getApplicationDefault())
                .build()
                .getService();
    }

    public void deleteBlob(String filename){
        storage.delete(BUCKET_NAME, filename);
    }
}
