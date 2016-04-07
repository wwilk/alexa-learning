package com.mpw.translate;

import com.google.api.services.translate.Translate;
import com.google.api.services.translate.model.TranslationsListResponse;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;

//GOOGLE TRANSLATE LIBRARY [JAVA]: https://developers.google.com/resources/api-libraries/documentation/translate/v2/java/latest/

@RestController
@RequestMapping("/api/translate")
public class QueryController {
    private final String googleApiAppName;
    private final String googleApiAppKey;
    private String srcLang = "en";
    private String tgtLang = "pl";
    private Translate translator;

    @Autowired
    public QueryController(@Value("${google.app.name}") String name,
                           @Value("${google.app.key}")  String key){
        googleApiAppName = name;
        googleApiAppKey = key;

        try {
            translator = new Translate.Builder(
                    com.google.api.client.googleapis.javanet.GoogleNetHttpTransport.newTrustedTransport(),
                    com.google.api.client.json.gson.GsonFactory.getDefaultInstance(),
                    null).setApplicationName(googleApiAppName)
                         .build();
        } catch (Exception e) { e.printStackTrace(); }
    }

    //TODO: src and target language in content separated by sth
    @RequestMapping("/{content}")
    public String translateText(@PathVariable String content) {
        String result = "";
        try {
            Translate.Translations.List listToTranslate = translator.new Translations().list(
                    Arrays.asList(content),
                    tgtLang);
            listToTranslate.setSource(srcLang);
            listToTranslate.setKey(googleApiAppKey);
            TranslationsListResponse response = listToTranslate.execute();
            result = response.getTranslations().get(0).getTranslatedText();
        } catch (Exception e) { e.printStackTrace(); }
        return result;
    }
}
