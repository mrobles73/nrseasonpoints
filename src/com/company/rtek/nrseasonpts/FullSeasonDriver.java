package com.company.rtek.nrseasonpts;

import java.text.DecimalFormat;
import java.util.Comparator;

public class FullSeasonDriver extends Driver{
    private int racesRun;
    private double avgStart;
    private double avgFinish;
    private boolean isInPostSeason = false;
    private int leader = 0;
    private int next = 0;
    private int poles;
    private int wins;
    private int t5;
    private int t10;
    private int dnf;
    private int pointsPosition;
    private int playoffWins = 0;

    //if driver is put in post season, have points that track them all the way to round of 4
    private int seasonPlayoffPoints;

    private static final DecimalFormat decFormat = new DecimalFormat("0.00");

    public FullSeasonDriver(String firstName, String lastName, int number, int lapsRun, int lapsLed, int points, int start, int finish, String status) {
        super(firstName, lastName, number, lapsRun, lapsLed, points);
        this.racesRun = 1;
        this.avgStart = start;
        this.avgFinish = finish;

        if(start == 1)
            poles = 1;
        if(finish == 1)
            wins = 1;
        if(finish < 6)
            t5 = 1;
        if(finish < 11)
            t10 = 1;
        if(!status.equals("Running"))
            dnf = 1;
    }

    public FullSeasonDriver(String firstName, String lastName, int number, int lapsRun, int lapsLed, int points, int pointsPosition, int racesRun, int poles, int wins, int t5, int t10, int dnf, int playoffPoints, int leader, int next, double avgFinish, double avgStart) {
        super(firstName, lastName, number, lapsRun, lapsLed, points);

        this.pointsPosition = pointsPosition;
        this.racesRun = racesRun;
        this.poles = poles;
        this.wins = wins;
        this.t5 = t5;
        this.t10 = t10;
        this.dnf = dnf;
        this.playoffPoints = playoffPoints;
        this.leader = leader;
        this.next = next;
        this.avgFinish = avgFinish;
        this.avgStart = avgStart;
    }

    public void setSeasonPlayoffPoints(int points) {
        this.seasonPlayoffPoints = points;
    }

    public void incrementSeasonPlayoffPoints(int points) {
        this.seasonPlayoffPoints += points;
    }

    public int getSeasonPlayoffPoints() {
        return seasonPlayoffPoints;
    }

    public void putInPostSeason() {
        isInPostSeason = true;
    }

    public void removeFromPostSeason() {
        isInPostSeason = false;
    }

    public boolean isInPostSeason() {
        return isInPostSeason;
    }

    public int getPointsPosition() {
        return pointsPosition;
    }

    public void setPointsPosition(int position) {
        this.pointsPosition = position;
    }

    public int getRacesRun() {
        return racesRun;
    }

    public void incrementRacesRun() {
        racesRun++;
    }

    public void incrementPoints(int points) {
        this.points += points;
    }

    public void subtractPoints(int points) {
        this.points -= points;
    }

    public void incrementLapsRun(int laps) {
        this.lapsRun += laps;
    }

    public void incrementLapsLed(int laps) {
        this.lapsLed += laps;
    }

    public void setLeader(int leader) {
        this.leader = leader;
    }

    public int getLeader() {
        return leader;
    }

    public void setNext(int next) {
        this.next = next;
    }

    public int getNext() {
        return next;
    }

    public void incrementPoles() {
        poles++;
    }

    public int getPoles() {
        return poles;
    }

    public void incrementWins() {
        wins++;
    }

    public int getWinCount() {
        return wins;
    }

    public void incrementT5() {
        t5++;
    }

    public int getT5() {
        return t5;
    }

    public void incrementT10() {
        t10++;
    }

    public int getT10() {
        return t10;
    }

    public void incrementDNF() {
        dnf++;
    }

    public int getDNFCount() {
        return dnf;
    }

    public void incrementPlayoffWins() {
        playoffWins++;
    }

    public void resetPlayoffWins() {
        playoffWins = 0;
    }

    public int getPlayoffWins() {
        return playoffWins;
    }

    public void calcAvgStart(int start) {
        avgStart = ((avgStart * (racesRun-1)) + start) / racesRun;
    }

    public double getAvgStart() {
        return avgStart;
    }

    public void calcAvgFinish(int finish) {
        avgFinish = ((avgFinish * (racesRun-1)) + finish) / racesRun;
    }

    public double getAvgFinish() {
        return avgFinish;
    }


    @Override
    public String toString() {
        return pointsPosition + " | #" + number + " | Driver: " + getFullName() + " | Pts: " + points + " | Next: " + next + " | Leader: " + leader + " | Starts: " + racesRun + " | Poles: " + poles + " | Wins: " + wins + " | T5: " + t5 + " | T10: " + t10 +" | DNFs: " + dnf + " | Laps Led: " + lapsLed + " | Laps Run: " + lapsRun + " | Avg Start: " + decFormat.format(avgStart) + " | Avg Finish: " + decFormat.format(avgFinish);
    }

    //official tiebreakers is number of finishes from 1 to 40* but i'll just be using wins to avg finish
    public static class SortByWinsThenPoints implements Comparator<FullSeasonDriver> {
        //should sort by wins, then points, then t5, then t10
        public int compare(FullSeasonDriver driverOne, FullSeasonDriver driverTwo) {
            int ret = Integer.compare(driverTwo.getWinCount(), driverOne.getWinCount());
            if(ret == 0)
                ret = Integer.compare(driverTwo.getPoints(), driverOne.getPoints());
            if(ret == 0)
                ret = Double.compare(driverOne.getAvgFinish(), driverTwo.getAvgFinish());
            if(ret == 0)
                ret = (driverTwo.getT5() != driverOne.getT5()) ? Integer.compare(driverTwo.getT5(), driverOne.getT5()) : Integer.compare(driverTwo.getT10(), driverOne.getT10());

            return ret;

//            if(driverOne.getWinCount() != driverTwo.getWinCount()) {
//                return driverTwo.getWinCount() - driverOne.getWinCount();
//            } else {
//                return driverTwo.getPoints() - driverOne.getPoints();
//            }
        }
    }

    public static class SortByPlayoffWinsThenPoints implements Comparator<FullSeasonDriver> {
        //should sort by wins, then points, then t5, then t10
        public int compare(FullSeasonDriver driverOne, FullSeasonDriver driverTwo) {
            int ret = Integer.compare(driverTwo.getPlayoffWins(), driverOne.getPlayoffWins());
            if(ret == 0)
                ret = Integer.compare(driverTwo.getPoints(), driverOne.getPoints());
            if(ret == 0)
                ret = Double.compare(driverOne.getAvgFinish(), driverTwo.getAvgFinish());
            if(ret == 0)
                ret = (driverTwo.getT5() != driverOne.getT5()) ? Integer.compare(driverTwo.getT5(), driverOne.getT5()) : Integer.compare(driverTwo.getT10(), driverOne.getT10());

            return ret;
//            if(driverOne.getPlayoffWins() != driverTwo.getPlayoffWins()) {
//                return driverTwo.getPlayoffWins() - driverOne.getPlayoffWins();
//            } else {
//                return driverTwo.getPoints() - driverOne.getPoints();
//            }
        }
    }

    public static class SortByPoints implements Comparator<FullSeasonDriver> {
        public int compare(FullSeasonDriver driverOne, FullSeasonDriver driverTwo) {
            int ret = Integer.compare(driverTwo.getPoints(), driverOne.getPoints());
            if(ret == 0)
                ret = Integer.compare(driverTwo.getWinCount(), driverOne.getWinCount());
            if(ret == 0)
                ret = Double.compare(driverOne.getAvgFinish(), driverTwo.getAvgFinish());
            if(ret == 0)
                ret = (driverTwo.getT5() != driverOne.getT5()) ? Integer.compare(driverTwo.getT5(), driverOne.getT5()) : Integer.compare(driverTwo.getT10(), driverOne.getT10());

            return ret;
        }

    }

}
