package com.rtek.nrseasonpts;

import java.util.List;

public class GNSSeason extends Season {

    public GNSSeason(int year, String series) {
        super(year, series, 36);
    }

    @Override
    public void addRace(List<SingleRaceDriver> race) {
        // todo
    }
}
