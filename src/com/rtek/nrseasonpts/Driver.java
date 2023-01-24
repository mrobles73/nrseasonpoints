package com.rtek.nrseasonpts;

import com.rtek.nrseasonpts.utils.NRUtils;

public class Driver implements Comparable<Driver> {
    protected String firstName;
    protected String lastName;
    protected int number;
    protected int lapsRun;
    protected int lapsLed;
    protected int points;
    protected int playoffPoints = 0;
    private String raceTrack;

    public Driver(String firstName, String lastName, int number, int lapsRun, int lapsLed, int points) {
        if(NRUtils.isValidString(firstName) && NRUtils.isValidString(lastName) && lapsRun >= 0 && lapsLed >= 0 && points >= 0) {
            this.firstName = firstName;
            this.lastName = lastName;
            this.number = number;
            this.lapsRun = lapsRun;
            this.lapsLed = lapsLed;
            this.points = points;
        } else {
            throw new IllegalArgumentException("Invalid parameters for Driver constructor");
        }
    }

    public String getFullName() {
        return firstName + " " + lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public int getLapsRun() {
        return lapsRun;
    }

    public void setLapsRun(int lapsRun) {
        this.lapsRun = lapsRun;
    }

    public int getLapsLed() {
        return lapsLed;
    }

    public void setLapsLed(int lapsLed) {
        this.lapsLed = lapsLed;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public int getPlayoffPoints() {
        return playoffPoints;
    }

    public void addPlayoffPoints(int points) {
        this.playoffPoints += points;
    }

    public void setRaceTrack(String raceTrack) {
        this.raceTrack = raceTrack;
    }

    public String getRaceTrack() {
        return this.raceTrack;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = getFullName().hashCode() * hash;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if(this == obj)
            return true;
        if(!(obj instanceof Driver))
            return false;

        Driver driver = (Driver)obj;
        return getFullName().equals(driver.getFullName());
    }

    public int compareTo(Driver driver) {
        return driver.getFullName().compareTo(this.getFullName());
    }
}
