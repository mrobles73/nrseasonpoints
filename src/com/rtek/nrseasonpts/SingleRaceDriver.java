package com.rtek.nrseasonpts;

import com.rtek.nrseasonpts.utils.NRUtils;

public class SingleRaceDriver extends Driver{
    private String status;
    private int start;
    private int finish;
    private boolean mostLapsLed;

    public SingleRaceDriver(String firstName, String lastName, String status, int number, int lapsRun, int lapsLed, int points, int start, int finish, boolean mostLapsLed) {
        super(firstName, lastName, number, lapsRun, lapsLed, points);
        if(NRUtils.isValidString(status)) {
            this.status = status;
            this.start = start;
            this.finish = finish;
            this.mostLapsLed = mostLapsLed;
        } else {
            throw new IllegalArgumentException("Invalid String parameters passed to SingleRaceDriver constructor");
        }
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public int getFinish() {
        return finish;
    }

    public void setFinish(int finish) {
        this.finish = finish;
    }

    public boolean mostLapsLed() {
        return mostLapsLed;
    }

    public void setMostLapsLed(boolean mostLapsLed) {
        this.mostLapsLed = mostLapsLed;
    }

    @Override
    public String toString() {
        return "F: " + finish + " | S: " + start + " | #" + number + " | Driver: " + firstName + " " + lastName + " | Laps Run: " + lapsRun + " | Laps Led: " + lapsLed + " | " + mostLapsLed;
    }


}
