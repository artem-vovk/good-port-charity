package com.charity.charity.services;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.translate.Translate;
import com.google.api.services.translate.model.TranslationsListResponse;
import com.google.api.services.translate.model.TranslationsResource;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Arrays;


@EnableAutoConfiguration
public class TranslateCustomTextService {

    public static String translateText(String text, String language, String GOOGLE_TRANSLATOR_KEY) throws GeneralSecurityException, IOException {

        Translate t = new Translate.Builder(
                GoogleNetHttpTransport.newTrustedTransport()
                , GsonFactory.getDefaultInstance(), null)
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
