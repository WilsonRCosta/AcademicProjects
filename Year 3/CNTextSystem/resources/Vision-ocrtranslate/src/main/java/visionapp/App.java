package visionapp;

import com.google.cloud.translate.*;
import com.google.cloud.vision.v1.*;
import com.google.protobuf.ByteString;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class App {

    // dado um texto obtém a linguagem "pt", "en", ... em que está escrito
    public static String detectLanguage(Translate translate, String text) {
        String detectedLanguage="und";
        Detection detection = translate.detect(text);
        detectedLanguage = detection.getLanguage();
        System.out.println(text+":"+detectedLanguage+":"+detection.getConfidence());
        return detectedLanguage;
    }

    public static List<DetectedText>  detectText(Translate translate, String filePath) throws IOException {
        List<DetectedText> allText=null;

        ByteString imgBytes = ByteString.readFrom(new FileInputStream(filePath));
        Image img = Image.newBuilder().setContent(imgBytes).build();
        // https://cloud.google.com/vision/docs/features-list
        Feature feat = Feature.newBuilder().setType(Feature.Type.TEXT_DETECTION).build();

        List<AnnotateImageRequest> requests = new ArrayList<>();
        AnnotateImageRequest request = AnnotateImageRequest.newBuilder()
                                       .addFeatures(feat).setImage(img).build();
        requests.add(request);
        try (ImageAnnotatorClient client = ImageAnnotatorClient.create()) {
            BatchAnnotateImagesResponse response = client.batchAnnotateImages(requests);
            List<AnnotateImageResponse> responses = response.getResponsesList();
            for (AnnotateImageResponse res : responses) {
                if (res.hasError()) {
                    System.out.println("Error:"+res.getError().getMessage());
                    return null;
                }
                // For full list of available annotations, see http://g.co/cloud/vision/docs
                allText=new ArrayList<DetectedText>();
                for (EntityAnnotation annotation : res.getTextAnnotationsList()) {
                    DetectedText dt = new DetectedText();
                    dt.text = annotation.getDescription();
                    dt.language = detectLanguage(translate, dt.text);
                    dt.poly = annotation.getBoundingPoly();
                    allText.add(dt);
                }
            }
        }
        return allText;
    }

    public static String translateText(Translate translate, String text, String lsrc, String lout) {
        Translation translation = translate.translate(
                text,
                Translate.TranslateOption.sourceLanguage(lsrc),
                Translate.TranslateOption.targetLanguage(lout));
                return translation.getTranslatedText();
    }

    public static void printSupportedLanguages(Translate translate) {
        //List<Language> supLang=translate.listSupportedLanguages();
        for (Language lg :translate.listSupportedLanguages()) {
            System.out.println(lg.getCode()+":"+lg.getName());
        }
    }


    // args[0] - pathname da imagem a processar
    // A conta de serviço tem de ter Role para Cloud Translation API
    // O acesso à vision API pode ser feiro com qualquer conta de serviço do
    // projeto onde se ativou a Vision API
    public static void main(String[] args) throws Exception {
        String imagePathName = args[0];
        String textTranslated = null;

        Translate translateService = TranslateOptions.getDefaultInstance().getService();
        //printSupportedLanguages(translateService);
        List<DetectedText> allText = detectText(translateService, imagePathName);
        System.out.println("All Text found:\n" + allText.get(0).text);
        String lang2trans = ask("Qual a lingua para traduzir");

        for (DetectedText dt : allText) {
            System.out.println("\n##Text from Image:\n" + dt.text);
            if (!dt.language.equals(lang2trans) && !dt.language.equals("und")) {
                textTranslated = translateText(translateService, dt.text, dt.language, lang2trans);
            } else textTranslated = dt.text;
            System.out.println(">>Translated text:\n" + textTranslated);
            String aux = "";
            for (Vertex vt : dt.poly.getVerticesList())
                aux += " (X=" + vt.getX() + ",Y=" + vt.getY() + ")";
            System.out.println("Vertices:" + aux);
        }
    }


    private static Scanner in = new Scanner(System.in);

    private static String ask(String msg) {
        System.out.println(msg);
        return in.nextLine();
    }
}
