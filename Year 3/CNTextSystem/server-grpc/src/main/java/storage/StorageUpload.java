package storage;

import com.google.cloud.WriteChannel;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import credentials.Credentials;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public class StorageUpload {
    private static final String BUCKET_NAME = "bucket-g06-cntext";
    public Storage storage;

    public StorageUpload() {
        storage = StorageOptions
                .newBuilder()
                .setCredentials(Credentials.credentials)
                .build()
                .getService();
    }

    public String uploadImage(Path imagePath, long sessionId) throws IOException {
        String blobName = sessionId + "-" + Math.abs(imagePath.hashCode());
        Map<String, String> propMap = new HashMap<>();
        propMap.put("userId", String.valueOf(sessionId));
        String contentType = Files.probeContentType(imagePath);
        BlobId blobId = BlobId.of(BUCKET_NAME, blobName);
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId).setContentType(contentType).setMetadata(propMap).build();
        if (Files.size(imagePath) > 1_000_000) {
            try (WriteChannel writer = storage.writer(blobInfo)) {
                byte[] buffer = new byte[1024];
                try (InputStream input = Files.newInputStream(imagePath)) {
                    int limit;
                    while ((limit = input.read(buffer)) >= 0) {
                        try {
                            writer.write(ByteBuffer.wrap(buffer, 0, limit));
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }
                }
            }
        } else {
            byte[] bytes = Files.readAllBytes(imagePath);
            storage.create(blobInfo, bytes);
        }
        return blobName;
    }

}
