package com.charity.charity.services;

public class ReversoContext {

    public static String reversoContext(String str){
        if (str != null) {
            return str.replace("<", "&lt;")
                    .replace(">", "&gt;")
                    .replace("'", "&#39;")
                    .replace("\"", "&Prime;")
                    .replace("script", "")
                    .replace("style", "")
                    .replace("button", "");
        } else
        return "";
    }

    public static String backReversoContext(String str) {
        if (str != null) {
            return str.replace("&lt;", "<")
                    .replace("&gt;", ">")
                    .replace("&#39;", "'")
                    .replace("&Prime;", "\"")
                    .replace("&quot;", "\"");
        } else
        return "";

    }
}
