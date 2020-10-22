package pt.isel.mpd.util.request;

import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Objects;

public class MockRequest extends AbstractRequest {
    @Override
    public Reader getReader(String path) {
        path = path.replace('&', '_')
                .replace('/', '_')
                .replace(',', '_')
                .replace('=', '_')
                .replace('?', '_')
                .substring(35,path.length());
        path += ".json";
        return new InputStreamReader(
                Objects.requireNonNull(
                        ClassLoader.getSystemClassLoader().getResourceAsStream(path)
                )
        );
    }
}
