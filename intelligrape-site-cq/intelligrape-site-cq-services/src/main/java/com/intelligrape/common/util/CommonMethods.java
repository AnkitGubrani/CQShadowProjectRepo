package com.intelligrape.common.util;

public class CommonMethods {
    public static String parseHtmlToText(String html) {
        html = html.replaceAll("\\<.*?\\>", "").replaceAll("&nbsp;", "");
        html = html.replaceAll("\n", "").replaceAll("\r", "");
        return html;
    }

    public static String checkNull(String text){
        return ((text!=null)?text : "");
    }
}