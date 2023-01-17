package com.company.rtek.utils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.*;

public class FileUtils {

    public static Document readInHTMLToDocument(String filePath) throws IOException {
        File input = new File(filePath);
        Document doc = Jsoup.parse(input, "UTF-8");
        return doc;
    }
}
