package credentials;

import com.google.auth.oauth2.GoogleCredentials;
import java.io.IOException;

public class Credentials {
    public static GoogleCredentials credentials;

    public static void setCredentials() {
        try {
            credentials = GoogleCredentials.getApplicationDefault();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
