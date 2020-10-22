package computeEngine;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.compute.Compute;
import com.google.api.services.compute.ComputeScopes;
import com.google.api.services.compute.model.Operation;
import com.google.auth.http.HttpCredentialsAdapter;
import com.google.auth.oauth2.GoogleCredentials;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class InstanceGroupManager {
    private static final Logger logger = Logger.getLogger(InstanceGroupManager.class.getName());

    private static final String PROJECT_ID = "g06-li62d-v1920";
    private static final String ZONE_NAME = "europe-west2-c";
    private static final String INSTANCE_GROUP_NAME = "premium-instance-group";
    private static final String INSTANCE_TRANS_GROUP_NAME = "premium-transinstance-group";
    private static final String APPLICATION_NAME = "ComputeEngineAPI";
    private static Compute computeService;
    private static int currentSize = 0;

    public InstanceGroupManager() throws IOException, GeneralSecurityException {
        computeService = initServiceAccess();
    }

    public void addInstance() throws IOException, InterruptedException {
        currentSize++;
        resizeInstanceGroup();
    }


    public void removeInstance() throws IOException, InterruptedException {
        currentSize--;
        resizeInstanceGroup();
    }

    public void resizeInstanceGroup() throws IOException, InterruptedException {
        executeOperation(
                computeService
                        .instanceGroupManagers()
                        .resize(PROJECT_ID, ZONE_NAME, INSTANCE_GROUP_NAME, currentSize)
                        .execute()
        );
        executeOperation(
                computeService
                        .instanceGroupManagers()
                        .resize(PROJECT_ID, ZONE_NAME, INSTANCE_TRANS_GROUP_NAME, currentSize)
                        .execute()
        );
    }

    private static Compute initServiceAccess() throws IOException, GeneralSecurityException {
        GoogleCredentials credential = GoogleCredentials.getApplicationDefault();
        List<String> scopes = new ArrayList<>();
        scopes.add(ComputeScopes.COMPUTE);
        credential = credential.createScoped(scopes);
        HttpTransport transport = GoogleNetHttpTransport.newTrustedTransport();
        JsonFactory jsonFactory = JacksonFactory.getDefaultInstance();
        HttpRequestInitializer requestInit = new HttpCredentialsAdapter(credential);
        return new Compute
                .Builder(transport, jsonFactory, requestInit)
                .setApplicationName(APPLICATION_NAME)
                .build();
    }

    private static void executeOperation(Operation operation) throws IOException, InterruptedException {
        Operation.Error error = waitOperation(computeService, operation);
        if (error != null)
            error.getErrors().forEach(System.out::println);
        else
            logger.log(Level.INFO, "Operation ended successfully.");
    }

    static Operation.Error waitOperation(Compute compute, Operation op) throws InterruptedException, IOException {
        String zone = op.getZone();
        if (zone != null) {
            String[] bits = zone.split("/");
            zone = bits[bits.length - 1];
        }
        while (!op.getStatus().equals("DONE")) {
            Thread.sleep(1000);
            op = compute
                    .zoneOperations()
                    .get(PROJECT_ID, zone, op.getName())
                    .execute();
        }
        return op.getError();
    }
}
