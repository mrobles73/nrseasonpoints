package com.company.rtek.utils;

import com.company.rtek.nrseasonpts.CupSeason;
import com.company.rtek.nrseasonpts.FullSeasonDriver;
import com.company.rtek.nrseasonpts.SingleRaceDriver;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NRUtils {

    private static Map<Integer, Integer> pointsMap;
    private int[] pointsArr = new int[]{185, 170, 165, 160, 155, 150, 146, 142, 138, 134, 130, 127, 124, 121, 118, 115, 112, };

    int winnerPoints0406 = 180;
    int winnerPoints0710 = 185;

    public static List<SingleRaceDriver> parseHTMLRaceStandings(Document doc, int year) {
        Elements tables = doc.getElementsByTag("tbody");
        Elements tableRows = tables.get(1).getElementsByTag("tr");
        List<SingleRaceDriver> drivers = new ArrayList<SingleRaceDriver>();
        for(Element tableRow : tableRows) {
            Elements tableData = tableRow.getElementsByTag("td");
            if(!tableData.get(0).text().equals("F")) {
                String [] driverName = tableData.get(3).text().trim().split(" ");
                String fName = driverName[0];
                String lName = driverName[1];
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
                //call util to calculate points?
                if(finish == 1 && (year > 2003 && year < 2011)) {
                    points = calculate0410FirstPlacePoints(year, led, mostLapsLed);
                } else if(year > 2010) {
                    points = calculateDriverPoints(year, finish, led, mostLapsLed);
                }
                String status = tableData.get(8).text();
                SingleRaceDriver srDriver = new SingleRaceDriver(fName, lName, status, number, laps, led, points, start, finish, mostLapsLed);
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
        if(year > 2010 && year < 2014) {

        } else if(year == 2014 || year == 2015) {

        } else if(year == 2016) {

        } else if(year >= 2017) {
            //current system
        }
        return points;
    }

    public static Map<Integer, Integer> getPointsMap(int year) {
        pointsMap = new HashMap<Integer, Integer>();
        if(year > 2003 && year < 2011) {
            if(year > 2003 && year < 2007)
                pointsMap.put(1, 180);
            else
                pointsMap.put(1, 185);

            pointsMap.put(2, 170);

        }
        return pointsMap;
    }

    public static boolean isValidString(String str) {
        return !(str == null || str.isBlank());
    }

    public static String convertListToJSON(List<FullSeasonDriver> list) {
        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gson = gsonBuilder.create();
        String jsonObject = gson.toJson(list);
        return jsonObject;
    }

    public static String convertListToPrettyJSON(List<FullSeasonDriver> list) {
        Gson prettyGson = new GsonBuilder().setPrettyPrinting().create();
        String prettyJson = prettyGson.toJson(list);
        return prettyJson;
    }
}
