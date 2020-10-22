package isel.cn;

import com.google.cloud.vision.v1.*;
import com.google.protobuf.ByteString;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class VisionUpload {
    private final String pathToFile;

    public VisionUpload(String pathToFile) {
        this.pathToFile = pathToFile;
    }

    public String upload() throws IOException {
        List<AnnotateImageRequest> request = detectText(pathToFile);
        return resolveRequest(request).get(0);
    }

    private List<AnnotateImageRequest> detectText(String pathToFile) throws IOException {
        ByteString imgBytes = ByteString.readFrom(new FileInputStream(pathToFile));
        Image img = Image.newBuilder().setContent(imgBytes).build();
        Feature feat = Feature.newBuilder().setType(Feature.Type.TEXT_DETECTION).build();
        List<AnnotateImageRequest> requests = new ArrayList<>();
        AnnotateImageRequest request = AnnotateImageRequest.newBuilder()
                .addFeatures(feat).setImage(img).build();
        requests.add(request);
        return requests;
    }

    private List<String> resolveRequest(List<AnnotateImageRequest> requests) throws IOException {
        List<String> imageText = new LinkedList<>();
        try (ImageAnnotatorClient client = ImageAnnotatorClient.create()) {
            BatchAnnotateImagesResponse response = client.batchAnnotateImages(requests);
            List<AnnotateImageResponse> responses = response.getResponsesList();

            for (AnnotateImageResponse res : responses) {
                if (res.hasError()) {
                    System.out.format("Error: %s%n", res.getError().getMessage());
                    throw new IOException("Error reading image!");
                }

                for (EntityAnnotation annotation : res.getTextAnnotationsList()) {
                    imageText.add(annotation.getDescription());
                }
            }
        }
        return imageText;
    }
}
