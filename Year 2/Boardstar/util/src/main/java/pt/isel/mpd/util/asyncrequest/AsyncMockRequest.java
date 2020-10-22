package pt.isel.mpd.util.asyncrequest;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

public class AsyncMockRequest implements AsyncRequest{

    @Override
    public CompletableFuture<Reader> getBody(String path) {
        path = path.replace('&', '_')
                .replace('/', '_')
                .replace(',', '_')
                .replace('=', '_')
                .replace('?', '_')
                .substring(35,path.length());
        path += ".json";
        InputStream in = ClassLoader.getSystemClassLoader().getResourceAsStream(path);
        InputStreamReader reader = new InputStreamReader(in);
        return CompletableFuture.completedFuture(reader);
    }

    @Override
    public void close() throws Exception {

    }
}
