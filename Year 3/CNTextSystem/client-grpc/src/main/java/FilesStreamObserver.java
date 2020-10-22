import io.grpc.stub.StreamObserver;
import textservice.HashFile;

import java.util.ArrayList;
import java.util.List;

public class FilesStreamObserver implements StreamObserver<HashFile> {
    List<String> fileNames = new ArrayList<>();
    boolean completed = false;
    @Override
    public void onNext(HashFile hashFile) {
        fileNames.add(hashFile.getFilename());
    }

    @Override
    public void onError(Throwable throwable) {
        System.err.println(throwable.getMessage());
    }

    @Override
    public void onCompleted() {
        completed = true;
    }

    public List<String> getFileNames() {
        return fileNames;
    }

    public boolean isCompleted() {
        return completed;
    }
}
