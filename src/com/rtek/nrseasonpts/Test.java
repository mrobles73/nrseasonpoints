package com.rtek.nrseasonpts;

import com.rtek.nrseasonpts.utils.NRUtils;
import org.jsoup.Jsoup;

import java.io.*;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;

public class Test {

    private static String PATH;
    private static String SERIES;
    private static int YEAR;

    public static void main(String[] args) throws Exception {
        if(args.length == 6 && args[0].equals("-T")) {
            System.out.println("NR Season Points Program TEST Running...");

            YEAR = Integer.parseInt(args[1]);
            SERIES = args[2];
            PATH = args[4];
            int length = Integer.parseInt(args[3]);
            String [] fileNames = new String[length];
            String [] fileContent = new String[length];
            for(int i=0; i<length; i++) {
                fileNames[i] = PATH + "R" + (i+1) + ".html";
                fileContent[i] = Jsoup.parse(new File(fileNames[i]), "UTF-8").toString();
            }

            String fileName = args[5] +  LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS).toString().replaceAll(":", "-") +".log";
            try(PrintStream prtStream = new PrintStream(new File(fileName))) {
                PrintStream stdout = System.out;
                System.setOut(prtStream);

                testPrintContentVersion(YEAR, SERIES, fileContent);
                testPrintPathVersion(YEAR, SERIES, fileNames);
                testJSONContentVersion(YEAR, SERIES, fileContent);
                testJSONPathVersion(YEAR, SERIES, fileNames);
                testExceptions(fileNames, fileContent);

                System.setOut(stdout);
            }

            System.out.println("NR Season Points Program TEST Closing...");

        }
    }

    public static void testPrintContentVersion(int year, String series, String... files) throws Exception {
        System.out.println("STARTED Testing Season Creation And List Results From String Content:");
        Season season = NRUtils.getContentVersion().getSeason(year, series, files);
        season.printAllRaceResults();
        season.printAllStandings();
        System.out.println();
        season.printCurrentStandings();
        System.out.println("FINISHED Testing Season Creation And List Results From String Content.");
    }

    public static void testPrintPathVersion(int year, String series, String... files) throws Exception {
        System.out.println("STARTED Testing Season Creation And List Results From Files:");
        Season season = NRUtils.getPathVersion().getSeason(year, series, files);
        season.printAllRaceResults();
        season.printAllStandings();
        System.out.println();
        season.printCurrentStandings();
        System.out.println("FINISHED Testing Season Creation And List Results From Files.");
    }

    public static void testJSONContentVersion(int year, String series, String... files) throws Exception {
        System.out.println("STARTED Testing Season Creation And JSON Results From String Content:");
        Season season = NRUtils.getContentVersion().getSeason(year, series, files);
        System.out.println(season.getCurrentStandingsJSON());
        System.out.println(season.getAllRaceResultsJSON());
        System.out.println(season.getAllStandingsJSON());
        System.out.println("FINISHED Testing Season Creation And JSON Results From String Content.");
    }

    public static void testJSONPathVersion(int year, String series, String... files) throws Exception {
        System.out.println("STARTED Testing Season Creation And JSON Results From Files:");
        Season season = NRUtils.getPathVersion().getSeason(year, series, files);
        System.out.println(season.getCurrentStandingsJSON());
        System.out.println(season.getAllRaceResultsJSON());
        System.out.println(season.getAllStandingsJSON());
        System.out.println("FINISHED Testing Season Creation And JSON Results From Files.");
    }

    public static void testExceptions(String[] fileNames, String[] fileContent) throws Exception {
        System.out.println("STARTED Exception Testing:");
        int length = fileNames.length+1;
        String [] finalStandingsFileNames = new String[length];
        String [] finalStandingsFileContent = new String[length];
        String [] malformedFileNames = new String[length];
        String [] malformedFileContent = new String[length];
        String [] emptyFileNames = new String[length];
        String [] emptyFileContent = new String[length];
        String [] noHTMLFileNames = new String[length];
        String [] noHTMLFileContent = new String[length];
        for(int i=0; i<length-1; i++) {
            finalStandingsFileNames[i] = fileNames[i];
            finalStandingsFileContent[i] = fileContent[i];
            malformedFileNames[i] = fileNames[i];
            malformedFileContent[i] = fileContent[i];
            emptyFileNames[i] = fileNames[i];
            emptyFileContent[i] = fileContent[i];
            noHTMLFileNames[i] = fileNames[i];
            noHTMLFileContent[i] = fileContent[i];
        }
        finalStandingsFileNames[length-1] = PATH + "Final Standings.html";
        finalStandingsFileContent[length-1] = Jsoup.parse(new File(PATH + "Final Standings.html"), "UTF-8").toString();
        malformedFileNames[length-1] = PATH + "malformed.html";
        malformedFileContent[length-1] = Jsoup.parse(new File(PATH + "malformed.html"), "UTF-8").toString();
        emptyFileNames[length-1] = PATH + "empty.html";
        emptyFileContent[length-1] = Jsoup.parse(new File(PATH + "empty.html"), "UTF-8").toString();
        noHTMLFileNames[length-1] = PATH + "nohtml.html";
        noHTMLFileContent[length-1] = Jsoup.parse(new File(PATH + "nohtml.html"), "UTF-8").toString();

        System.out.println("STARTED Testing Unrelated HTML File:");
        tryMethodPath(finalStandingsFileNames);
        tryMethodContent(finalStandingsFileContent);
        System.out.println("FINISHED Testing Unrelated HTML File.");

        System.out.println("STARTED Testing Export File But Not Properly Formed:");
        tryMethodPath(malformedFileNames);
        tryMethodContent(malformedFileContent);
        System.out.println("FINISHED Testing Export File But Not Properly Formed.");

        System.out.println("STARTED Testing Empty HTML File:");
        tryMethodPath(emptyFileNames);
        tryMethodContent(emptyFileContent);
        System.out.println("FINISHED Testing Empty HTML File:");

        System.out.println("STARTED Testing HTML File With No HTML Content:");
        tryMethodPath(noHTMLFileNames);
        tryMethodContent(noHTMLFileContent);
        System.out.println("FINISHED Testing HTML File With No HTML Content:");

        System.out.println("FINISHED Exception Testing.");
    }

    public static void tryMethodPath(String [] files) {
        try {
            System.out.println("PRINTED Version With PATH:");
            testPrintPathVersion(YEAR, SERIES, files);
        } catch (Exception e){
            e.printStackTrace(System.out);
        }

        try {
            System.out.println("JSON Version With PATH:");
            testJSONPathVersion(YEAR, SERIES, files);
        } catch (Exception e){
            e.printStackTrace(System.out);
        }
    }

    public static void tryMethodContent(String [] files) {
        try {
            System.out.println("PRINTED Version With CONTENT:");
            testPrintContentVersion(YEAR, SERIES, files);
        } catch (Exception e){
            e.printStackTrace(System.out);
        }

        try {
            System.out.println("JSON Version With CONTENT:");
            testJSONContentVersion(YEAR, SERIES, files);
        } catch (Exception e){
            e.printStackTrace(System.out);
        }
    }

}