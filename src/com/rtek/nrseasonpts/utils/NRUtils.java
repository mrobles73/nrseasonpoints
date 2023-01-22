package com.rtek.nrseasonpts.utils;

import com.rtek.nrseasonpts.CupSeason;
import com.rtek.nrseasonpts.FullSeasonDriver;
import com.rtek.nrseasonpts.SingleRaceDriver;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class NRUtils {

    private static Map<Integer, Integer> pointsMap;

    private final static int WINNER_0406 = 180;
    private final static int WINNER_0710 = 185;
    private final static int[] POINTS_75_10 = new int[]{0, 175, 170, 165, 160, 155, 150, 146, 142, 138, 134, 130, 127, 124, 121, 118, 115, 112, 109, 106, 103, 100, 97, 94, 91, 88, 85, 82, 79, 76, 73, 70, 67, 64, 61, 58, 55, 52, 49, 46, 43, 40, 37, 34};
    public final static int[] POINTS_11_15 = new int[]{0, 43, 42, 41, 40, 39, 38, 37, 36, 35, 34, 33, 32, 31, 30, 29, 28, 27, 26, 25, 24, 23, 22, 21, 20, 19, 18, 17, 16, 15, 14, 13, 12, 11, 10, 9, 8, 7, 6, 5, 4, 3, 2, 1};
    public final static int[] POINTS_16 = new int[]{0, 40, 39, 38, 37, 36, 35, 34, 33, 32, 31, 30, 29, 28, 27, 26, 25, 24, 23, 22, 21, 20, 19, 18, 17, 16, 15, 14, 13, 12, 11, 10, 9, 8, 7, 6, 5, 4, 3, 2, 1, 0, 0, 0};
    public final static int[] POINTS_17 = new int[]{0, 40, 35, 34, 33, 32, 31, 30, 29, 28, 27, 26, 25, 24, 23, 22, 21, 20, 19, 18, 17, 16, 15, 14, 13, 12, 11, 10, 9, 8, 7, 6, 5, 4, 3, 2, 1, 1, 1, 1, 1, 0, 0, 0};

    public static List<SingleRaceDriver> parseHTMLRaceStandings(Document doc, int year) {
        Elements h3 = doc.getElementsByTag("h3");
        String track = h3.get(0).text();
        String date = h3.get(1).text();

        Elements tables = doc.getElementsByTag("tbody");
        Elements tableRows;
        if(tables.size() == 1) {
            tableRows = tables.get(0).getElementsByTag("tr");
        } else {
            tableRows = tables.get(1).getElementsByTag("tr");
        }
        List<SingleRaceDriver> drivers = new ArrayList<SingleRaceDriver>();
        for(Element tableRow : tableRows) {
            Elements tableData = tableRow.getElementsByTag("td");
            if(!tableData.get(0).text().equals("F")) {
                String [] driverName = tableData.get(3).text().trim().split(" ");
                String fName = driverName[0];
                String lName = driverName[1];
                if(driverName.length == 3) lName += " " + driverName[2];
                int finish = Integer.parseInt(tableData.get(0).text());
                int start = Integer.parseInt(tableData.get(1).text());
                int number = Integer.parseInt(tableData.get(2).text());
                int laps = Integer.parseInt(tableData.get(5).text());
                int led = 0;
                boolean mostLapsLed = false;
                if(tableData.get(6).text().contains("*")) {
                    mostLapsLed = true;
                    led = Integer.parseInt(tableData.get(6).text().replace("*", ""));
                } else {
                    led = Integer.parseInt(tableData.get(6).text());
                }
                int points = Integer.parseInt(tableData.get(7).text());

                //calculate race points
                if(finish == 1 && (year > 2003 && year < 2011)) {
                    points = calculate0410FirstPlacePoints(year, led, mostLapsLed);
                } else if(year > 2010) {
                    points = calculateDriverPoints(year, finish, led, mostLapsLed);
                }

                String status = tableData.get(8).text();
                SingleRaceDriver srDriver = new SingleRaceDriver(fName, lName, status, number, laps, led, points, start, finish, mostLapsLed);
                if(year > 2016 && finish == 1) {
                    srDriver.addPlayoffPoints(5);
                }
                drivers.add(srDriver);
            }
        }
        return drivers;
    }

    private static int calculate0410FirstPlacePoints(int year, int led, boolean mostLapsLed) {
        int points;
        int toadd = 0;
        if(led > 0) toadd = 5;
        if(mostLapsLed) toadd += 5;

        if(year < 2007) {
            points = 180;
        } else {
            points = 185;
        }
        points += toadd;
        return points;
    }

    private static int calculateDriverPoints(int year, int finish, int led, boolean mostLapsLed) {
        int points = 0;

        if(year > 2010 && year < 2017) {
            int win = 3;
            int lapLed = 1;
            int mLapsLed = 1;
            if(year < 2016) {
                points = POINTS_11_15[finish];
            } else {
                points = POINTS_16[finish];
            }
            if(finish == 1) points += win;
            if(led > 0) points += lapLed;
            if(mostLapsLed) points += mLapsLed;
        } else {
            points = POINTS_17[finish];
            if(finish == 1) {
                points += 5;
            }
        }
        return points;
    }

    public static boolean isValidString(String str) {
        return !(str == null || str.isBlank());
    }

    public static String convertListToJSON(List<FullSeasonDriver> list) {
        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gson = gsonBuilder.create();
        return gson.toJson(list);
    }

    public static String convertListToPrettyJSON(List<FullSeasonDriver> list) {
        Gson prettyGson = new GsonBuilder().setPrettyPrinting().create();
        return prettyGson.toJson(list);
    }

    public static String getSeasonJSONString(int year, String series, String... fileNames) {
        String toReturn = "";
        try {
            CupSeason season = new CupSeason(year, series);
            for(int i=0; i<fileNames.length; i++) {
                List<SingleRaceDriver> raceResultsList = NRUtils.parseHTMLRaceStandings(FileUtils.readInHTMLToDocument("C:\\Papyrus\\NASCAR Racing 2003 Season\\exports_imports\\"+fileNames[i]+".html", "PATH"), season.getYear());
                //season.addRace(raceResultsList, i);
                season.addRace(raceResultsList);
            }
            //toReturn = season.getAllRacesJSON();
            toReturn = season.getAllRacesPrettyJSON();
        } catch (Exception e) {
            toReturn = "{ \"error\":true,\"message\":\"" +e.getMessage() + "\" }";
        }
        return toReturn;
    }

    public static List<FullSeasonDriver>[] getSeasonStandingsArray(int year, String series, String... files) {
        try {
            CupSeason season = new CupSeason(year, series);
            for(int i=0; i<files.length; i++) {
                List<SingleRaceDriver> raceResultsList = NRUtils.parseHTMLRaceStandings(FileUtils.readInHTMLToDocument(files[i], "STRING"), season.getYear());
                //season.addRace(raceResultsList, i);
                season.addRace(raceResultsList);
            }
            return season.getAllStandings();
        } catch (Exception e) {
            return null;
        }
    }

    public static CupSeason getSeason(int year, String series, String... files) {
        try {
            CupSeason season = new CupSeason(year, series);
            for(int i=0; i<files.length; i++) {
                List<SingleRaceDriver> raceResultsList = NRUtils.parseHTMLRaceStandings(FileUtils.readInHTMLToDocument(files[i], "STRING"), season.getYear());
                //season.addRace(raceResultsList, i);
                season.addRace(raceResultsList);
            }
            return season;
        } catch (Exception e) {
            return null;
        }
    }

    public static List<List<FullSeasonDriver>> addRacesToSeason(CupSeason season, String[] files) {
        try {
            for(int i=0; i<files.length; i++) {
                List<SingleRaceDriver> raceResultsList = NRUtils.parseHTMLRaceStandings(FileUtils.readInHTMLToDocument(files[i], "STRING"), season.getYear());
                season.addRace(raceResultsList);
            }
            List<List<FullSeasonDriver>> list = new ArrayList<>();
            List<List<FullSeasonDriver>> curStandings = season.getAllStandingsAL();
            for(int i=(curStandings.size()-files.length); i<curStandings.size(); i++) {
                list.add(curStandings.get(i));
            }
            return list;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void printSeasonResults(int year, String series, String... fileNames) {
        try {
            CupSeason season = new CupSeason(year, series);
            for(int i=0; i<fileNames.length; i++) {
                List<SingleRaceDriver> raceResultsList = NRUtils.parseHTMLRaceStandings(FileUtils.readInHTMLToDocument("C:\\Papyrus\\NASCAR Racing 2003 Season\\exports_imports\\"+fileNames[i]+".html", "PATH"), season.getYear());
                //season.addRace(raceResultsList, i);
                season.addRace(raceResultsList);
            }
            season.printOutSeasonDriverList();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }
}
