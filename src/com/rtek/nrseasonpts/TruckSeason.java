package com.rtek.nrseasonpts;

import java.util.List;

public class TruckSeason extends Season {

    public TruckSeason(int year, String series) {
        super(year, series, 36);
    }

    @Override
    public void addRace(List<SingleRaceDriver> race) {
        // todo
    }
}
