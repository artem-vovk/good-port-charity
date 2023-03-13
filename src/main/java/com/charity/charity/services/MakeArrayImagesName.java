package com.charity.charity.services;

public class MakeArrayImagesName {

    //make array with images names from String
    public static String[] makeArrayImageName(String allFileNames) {
        if (allFileNames != null) {
            return allFileNames.split("\\,");
        } else {
            return new String[]{};
        }
    }
}
