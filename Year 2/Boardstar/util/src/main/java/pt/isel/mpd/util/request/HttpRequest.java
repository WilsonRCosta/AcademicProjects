package pt.isel.mpd.util.request;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;

public class HttpRequest extends AbstractRequest {
    @Override
    public Reader getReader(String path) {
        try {
            return new InputStreamReader(new URL(path).openStream());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
