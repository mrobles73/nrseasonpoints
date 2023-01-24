package com.rtek.nrseasonpts.utils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.*;

public class FileUtils {

    public static Document readInHTMLToDocument(String file, String type) throws IOException {
        Document doc = null;
        if(type.equals("PATH")) {
            File input = new File(file);
            doc = Jsoup.parse(input, "UTF-8");
        } else if(type.equals("CONTENT")) {
            doc = Jsoup.parse(file);
        }
        return doc;
    }
}
