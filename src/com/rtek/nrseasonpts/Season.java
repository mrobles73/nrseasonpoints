package com.rtek.nrseasonpts;

import com.rtek.nrseasonpts.utils.NRUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class Season {
    protected final int YEAR;
    protected final int RACE_COUNT;
    protected int racesRun;
    protected List<String> raceTracks = new ArrayList<String>();
    protected List<FullSeasonDriver> seasonDriverList = new ArrayList<FullSeasonDriver>();
    protected List<List<SingleRaceDriver>> races = new ArrayList<List<SingleRaceDriver>>(); /**/
    protected List<List<FullSeasonDriver>> prevStandings = new ArrayList<List<FullSeasonDriver>>(); /**/

    public Season(int year, String series, int raceCount) {
        if(year > 1974)
            this.YEAR = year;
        else
            throw new IllegalArgumentException(year + " not a valid year");
        if(raceCount < 1)
            throw new IllegalArgumentException("Race count must be greater than 1");
        else
            this.RACE_COUNT = raceCount;

        this.racesRun = 0;
    }

    public abstract void addRace(List<SingleRaceDriver> race);

    public int getYear() {
        return YEAR;
    }

    public int getRaceCount() {
        return RACE_COUNT;
    }

    public int getRacesRun() {
        return racesRun;
    }

    protected void incrementRacesRun() {
        racesRun++;
    }

    public List<FullSeasonDriver> getCurrentStandings() {
        return this.seasonDriverList;
    }

    public List<List<SingleRaceDriver>> getAllRaceResults() {
        return this.races;
    }

    public List<List<FullSeasonDriver>> getAllStandings() {
        return this.prevStandings;
    }

    public String getCurrentStandingsJSON() {
        return "{\"standings\":" + NRUtils.convertListToJSON(seasonDriverList) + "}";
    }

    public String getAllRaceResultsJSON() {
        StringBuilder json = new StringBuilder("{");
        int i = 1;
        for(List<SingleRaceDriver> race : races) {
            json.append("\"R").append(i++).append("\":").append(NRUtils.convertListToJSON(race)).append(",");
        }
        return json.deleteCharAt(json.lastIndexOf(",")).append("}").toString();
    }

    public String getAllStandingsJSON() {
        StringBuilder json = new StringBuilder("{");
        int i = 1;
        for(List<FullSeasonDriver> race : prevStandings) {
            json.append("\"R").append(i++).append("\":").append(NRUtils.convertListToJSON(race)).append(",");
        }
        return json.deleteCharAt(json.lastIndexOf(",")).append("}").toString();
    }

    public String getAllStandingsPrettyJSON() {
        StringBuilder json = new StringBuilder("{\n");
        int i = 1;
        for(List<FullSeasonDriver> race : prevStandings) {
            json.append("\"R").append(i++).append("\":").append(NRUtils.convertListToPrettyJSON(race)).append(",");
        }
        return json.deleteCharAt(json.lastIndexOf(",")).append("}").toString();
    }

    public void printCurrentStandings() {
        System.out.println("Standings:");
        seasonDriverList.forEach(System.out::println);
    }

    public void printAllRaceResults() {
        int i = 1;
        for(List<SingleRaceDriver> race : races) {
            System.out.println("\nR"+i++);
            race.forEach(System.out::println);
        }
    }

    public void printAllStandings() {
        int i = 1;
        for(List<FullSeasonDriver> race : prevStandings) {
            System.out.println("\nR"+i++);
            race.forEach(System.out::println);
        }
    }

    public List<FullSeasonDriver> makeDeepCopyOfStandings(List<FullSeasonDriver> list, String raceTrack) {
        List<FullSeasonDriver> newList = new ArrayList<FullSeasonDriver>();
        list.forEach((driver) -> newList.add(new FullSeasonDriver(driver.getFirstName(), driver.getLastName(), driver.getNumber(), driver.getLapsRun(), driver.getLapsLed(), driver.getPoints(), driver.getPointsPosition(), driver.getRacesRun(), driver.getPoles(), driver.getWinCount(), driver.getT5(), driver.getT10(), driver.getDNFCount(), driver.getPlayoffPoints(), driver.getLeader(), driver.getNext(), driver.getAvgFinish(), driver.getAvgStart())));
        newList.get(0).setRaceTrack(raceTrack);
        return newList;
    }

}
