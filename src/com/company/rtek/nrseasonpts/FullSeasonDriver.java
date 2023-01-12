package com.company.rtek.nrseasonpts;

import java.text.DecimalFormat;

public class FullSeasonDriver extends Driver{
    private int racesRun;
    private double avgStart;
    private double avgFinish;
    private boolean isInPostSeason = false;
    private int next;
    private int poles;
    private int wins;
    private int t5;
    private int t10;
    private int dnf;
    private int pointsPosition;

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

    public void incrementRacesRun() {
        racesRun++;
    }

    public void incrementPoints(int points) {
        this.points += points;
    }

    public void incrementLapsRun(int laps) {
        this.lapsRun += laps;
    }

    public void incrementLapsLed(int laps) {
        this.lapsLed += laps;
    }

    public void calculateNext() {

    }
    public void incrementPoles() {
        poles++;
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

    public void incrementT10() {
        t10++;
    }

    public void incrementDNF() {
        dnf++;
    }

    public void calcAvgStart(int start) {
        avgStart = ((avgStart * (racesRun-1)) + start) / racesRun;
    }

    public void calcAvgFinish(int finish) {
        avgFinish = ((avgFinish * (racesRun-1)) + finish) / racesRun;
    }

    @Override
    public String toString() {
        return pointsPosition + " | #" + number + " | Driver: " + getFullName() + " | Pts: " + points + " | Starts: " + racesRun + " | Poles: " + poles + " | Wins: " + wins + " | T5: " + t5 + " | T10: " + t10 +" | DNF: " + dnf + " | Laps Led: " + lapsLed + " | Laps Run: " + lapsRun + " | Avg Start: " + decFormat.format(avgStart) + " | Avg Finish: " + decFormat.format(avgFinish);
    }
}
