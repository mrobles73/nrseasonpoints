package com.rtek.nrseasonpts;

public class SeasonFactory {

    public static Season getSeason(int year, String series) {
        if(series.equals("Cup")) {
            return new CupSeason(year, series);
        } else if(series.equals("GNS")){
            return new GNSSeason(year, series);
        } else if(series.equals("Trucks")) {
            return new TruckSeason(year, series);
        } else {
            throw new IllegalArgumentException(series + " is not a valid series to create.");
        }
    }
}
