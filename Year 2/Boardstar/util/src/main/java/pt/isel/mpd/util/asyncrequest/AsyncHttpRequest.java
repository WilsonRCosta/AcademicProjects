package pt.isel.mpd.util.asyncrequest;

import org.asynchttpclient.AsyncHttpClient;
import org.asynchttpclient.Dsl;
import org.asynchttpclient.Response;

import java.io.InputStreamReader;
import java.io.Reader;
import java.util.concurrent.CompletableFuture;

public class AsyncHttpRequest implements AsyncRequest{

    AsyncHttpClient ahc = Dsl.asyncHttpClient();

    @Override
    public CompletableFuture<Reader> getBody(String path) {
        return ahc
            .prepareGet(path)
            .execute()
            .toCompletableFuture()
            .thenApply(Response::getResponseBodyAsStream)
            .thenApply(InputStreamReader::new);
    }

    @Override
    public void close() throws Exception {
        if(ahc != null)
            ahc.close();
    }
}
