package com.charity.charity.services;

import com.charity.charity.models.CustomInfo;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.translate.Translate;
import com.google.api.services.translate.model.TranslationsListResponse;
import com.google.api.services.translate.model.TranslationsResource;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Arrays;
import java.util.List;


@EnableAutoConfiguration
public class TranslateService {

//    @Autowired
//    private ItemRepository itemRepository;

//    @Value("${google-translator-key}")
//    static
//    String GOOGLE_TRANSLATOR_KEY;


    //When need to add in App new languages for translation customers text
    //just add new language to this array
    public static String[] arrayLanguagesForTranslateCustomText(){
        return new String[] {"uk", "en", "pl", "de", "ru"};
    }


    //service method
    public static List<CustomInfo> translatCustomInfoToAllLanguages(
            String originLanguage,
            String text,
            String contentName,
            List<CustomInfo> customInfos,
            String GOOGLE_TRANSLATOR_KEY
    ) throws GeneralSecurityException, IOException {

        String[] languagesForTranslation = arrayLanguagesForTranslateCustomText(); //count languages for translate
//        List<CustomInfo> customInfos = new ArrayList<>();

        for (String lang : languagesForTranslation) {
            if (!lang.equals(originLanguage)){
                String translation = translateText(text, lang, GOOGLE_TRANSLATOR_KEY);
                customInfos.add(new CustomInfo(contentName, lang, translation));
            }
        }
        return customInfos;
    }


    public static String translateText(String text, String language, String GOOGLE_TRANSLATOR_KEY) throws GeneralSecurityException, IOException {

        Translate t = new Translate.Builder(
                GoogleNetHttpTransport.newTrustedTransport(),
                GsonFactory.getDefaultInstance(),
                null)
                // Set your application name
                .setApplicationName("Good Port")
                .build();
        Translate.Translations.List list = t.new Translations().list(
                Arrays.asList(
                        // Pass in list of strings to be translated
                        text),
                // Target language
                language);

        // TODO: Set your API-Key from https://console.developers.google.com/
        list.setKey(GOOGLE_TRANSLATOR_KEY);
        TranslationsListResponse response = list.execute();

        String textTranslated = "";
        for (TranslationsResource translationsResource : response.getTranslations()) {
            textTranslated = translationsResource.getTranslatedText();
        }

        return textTranslated;
    }
}
