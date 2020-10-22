package isel.cn;

import com.google.cloud.translate.v3.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static isel.cn.SubscriberBaseApp.PROJECT_ID;

public class TranslationUpload {

    public List<String> detectLanguage(String text) throws IOException {
        List<String> languages = new ArrayList<>();
        try (TranslationServiceClient client = TranslationServiceClient.create()) {
            LocationName parent = LocationName.of(PROJECT_ID, "global");
            DetectLanguageRequest request = DetectLanguageRequest.newBuilder()
                    .setParent(parent.toString())
                    .setMimeType("text/plain")
                    .setContent(text)
                    .build();
            DetectLanguageResponse response = client.detectLanguage(request);
            for (DetectedLanguage language : response.getLanguagesList()) {
                languages.add(language.getLanguageCode());
            }
        }
        return languages;
    }

    public String translateText(Translator translator) throws IOException {
        List<String> translatedText = new ArrayList<>();
        try (TranslationServiceClient client = TranslationServiceClient.create()) {
            LocationName parent = LocationName.of(PROJECT_ID, "global");
            TranslateTextRequest request = TranslateTextRequest.newBuilder()
                    .setParent(parent.toString())
                    .setMimeType("text/plain")
                    .setTargetLanguageCode(translator.getTargetLang())
                    .addContents(translator.getText())
                    .build();

            TranslateTextResponse response = client.translateText(request);
            for (Translation translation : response.getTranslationsList()) {
                translatedText.add(translation.getTranslatedText());
            }
        }
        return translatedText.get(0);
    }
}
