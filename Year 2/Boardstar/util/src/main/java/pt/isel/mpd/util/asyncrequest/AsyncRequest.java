package pt.isel.mpd.util.asyncrequest;

import java.io.Reader;
import java.util.concurrent.CompletableFuture;

public interface AsyncRequest extends AutoCloseable {

    CompletableFuture<Reader> getBody(String path);
}
