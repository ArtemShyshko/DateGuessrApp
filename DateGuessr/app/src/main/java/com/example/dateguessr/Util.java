package com.example.dateguessr;

import com.example.dateguessr.model.wiki_api.imageinfo_pojo.DateTimeOriginal;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Util {

    public static String extractYear(DateTimeOriginal dateTimeOriginal) {
        if (dateTimeOriginal == null)  return null;

        // Regular expression to match 4 subsequent digits (year)
        String input = dateTimeOriginal.getValue();
        Pattern pattern = Pattern.compile("\\b\\d{4}\\b");
        Matcher matcher = pattern.matcher(input);

        // Find the first match
        if (matcher.find()) {
            return matcher.group();
        } else {
            return null;
        }
    }

    public static String getFileNameFromUrl(String url) {
        String fileName = "File:" +
                url.split("/")[url.split("/").length - 1];
        String decodedFileName = null;
        try {
            decodedFileName = URLDecoder.decode(fileName, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            System.out.println("ERROR: " + e.getLocalizedMessage());
        }
        return decodedFileName;
    }

}
