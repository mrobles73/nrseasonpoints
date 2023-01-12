package com.company.rtek.nrseasonpts;

import java.util.*;

public class CupSeason {
    private final int NUMBER_OF_RACES = 36;
    private final int YEAR;
    private int racesRun = 0;
    private List<FullSeasonDriver> seasonDriverList = new ArrayList<FullSeasonDriver>();

    private List<SingleRaceDriver>[] races = new List[NUMBER_OF_RACES];
    private List<FullSeasonDriver>[] prevStandings = new ArrayList[NUMBER_OF_RACES];

    private final int[] TOP_10_RESET_0406 = new int[]{5050, 5045, 5040, 5035, 5030, 5025, 5020, 5015, 5010, 5005};

    public CupSeason(int year) {
        if(year > 1974) {
            this.YEAR = year;
        } else {
            throw new IllegalArgumentException(year + " not a valid year");
        }
    }

    public int getYear() {
        return YEAR;
    }

    private void incrementRacesRun() {
        racesRun++;
    }

    public void addRace(List<SingleRaceDriver> race, int index) {
        races[index] = race;
        incrementRacesRun();
        if(racesRun == 1) {
            //add all the drivers in the race to the list of full season drivers
            for(SingleRaceDriver driver : race) {
                seasonDriverList.add(new FullSeasonDriver(driver.getFirstName(), driver.getLastName(), driver.getNumber(), driver.getLapsRun(), driver.getLapsLed(), driver.getPoints(), driver.getStart(), driver.getFinish(), driver.getStatus()));
            }
        } else {
            //find the driver in season list of drivers and add this race's information to them, or if driver isn't found, add them to the list
            for(SingleRaceDriver driver : race) {
                if(seasonDriverList.contains(driver)) {
                    int i = seasonDriverList.indexOf(driver);
                    FullSeasonDriver fsDriver = seasonDriverList.get(i);

                    fsDriver.incrementRacesRun();
                    fsDriver.incrementPoints(driver.getPoints());
                    fsDriver.incrementLapsRun(driver.getLapsRun());
                    fsDriver.incrementLapsLed(driver.getLapsLed());
                    fsDriver.calcAvgFinish(driver.getFinish());
                    fsDriver.calcAvgStart(driver.getStart());

                    if(driver.getFinish() == 1)
                        fsDriver.incrementWins();
                    if(driver.getStart() == 1)
                        fsDriver.incrementPoles();
                    if(driver.getFinish() < 6)
                        fsDriver.incrementT5();
                    if(driver.getFinish() < 11)
                        fsDriver.incrementT10();
                    if(!driver.getStatus().equals("Running"))
                        fsDriver.incrementDNF();

                    //calculate playoffs
                    if(YEAR > 2003 && racesRun == 26) {
                        if(YEAR < 2007 && fsDriver.getPointsPosition() < 11) {
                            fsDriver.putInPostSeason();
                            fsDriver.setPoints(TOP_10_RESET_0406[fsDriver.getPointsPosition()-1]);
                        } else if(YEAR > 2006 && YEAR < 2011 && fsDriver.getPointsPosition() < 13) {
                            fsDriver.putInPostSeason();
                            fsDriver.setPoints((fsDriver.getWinCount()*5) + 5000);
                        }
                    }

                } else {
                    //add the driver to full season driver list
                    seasonDriverList.add(new FullSeasonDriver(driver.getFirstName(), driver.getLastName(), driver.getNumber(), driver.getLapsRun(), driver.getLapsLed(), driver.getPoints(), driver.getStart(), driver.getFinish(), driver.getStatus()));
                }
            }
        }

        //loop through seasonDriverList and set driver's points position
        Collections.sort(seasonDriverList);
        for(int i = 0; i<seasonDriverList.size(); i++) {
            seasonDriverList.get(i).setPointsPosition(i+1);
        }
        prevStandings[index] = new ArrayList<FullSeasonDriver>(seasonDriverList);
    }

    public void addDriver(FullSeasonDriver driver) {
        if(driver != null)
            seasonDriverList.add(driver);
        else
            throw new IllegalArgumentException("Driver passed in is null");
    }

    public List<FullSeasonDriver> getSeasonDriverList() {
        return this.seasonDriverList;
    }

    public void printOutSeasonDriverList() {
        Collections.sort(seasonDriverList);
        for (FullSeasonDriver driver : seasonDriverList) {
            System.out.println(driver);
        }
    }

}
