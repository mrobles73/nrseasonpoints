package com.company.rtek.nrseasonpts;

import com.company.rtek.utils.NRUtils;

import java.util.*;

public class CupSeason {
    private final int NUMBER_OF_RACES = 36;
    private final int YEAR;
    private int racesRun = 0;
    private List<FullSeasonDriver> seasonDriverList = new ArrayList<FullSeasonDriver>();

    private List<SingleRaceDriver>[] races = new List[NUMBER_OF_RACES];
    private List<FullSeasonDriver>[] prevStandings = new ArrayList[NUMBER_OF_RACES];

    private final int[] TOP_10_RESET_0406 = new int[]{5050, 5045, 5040, 5035, 5030, 5025, 5020, 5015, 5010, 5005};
    private final int[] TOP_10_POINTS_17 = new int[]{0, 15, 10, 9, 8, 7, 6, 5, 4, 3, 2};

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
            ArrayList<FullSeasonDriver> fsDriversArr = new ArrayList<FullSeasonDriver>();
            for(SingleRaceDriver driver : race) {
                if(seasonDriverList.contains(driver)) {
                    int i = seasonDriverList.indexOf(driver);
                    FullSeasonDriver fsDriver = seasonDriverList.get(i);

                    fsDriver.incrementRacesRun();
                    if(YEAR > 2013 && racesRun == 36 && fsDriver.isInPostSeason()) {
                        if(YEAR < 2016) {
                            driver.setPoints(NRUtils.POINTS_11_15[driver.getFinish()]);
                        } else if(YEAR == 2016) {
                            driver.setPoints(NRUtils.POINTS_16[driver.getFinish()]);
                        } else {
                            driver.setPoints(NRUtils.POINTS_17[driver.getFinish()]);
                        }
                    }
                    if(fsDriver.isInPostSeason()) {
                        fsDriver.incrementSeasonPlayoffPoints(driver.getPoints());
                    }
                    fsDriver.incrementPoints(driver.getPoints());
                    fsDriver.incrementLapsRun(driver.getLapsRun());
                    fsDriver.incrementLapsLed(driver.getLapsLed());
                    fsDriver.calcAvgFinish(driver.getFinish());
                    fsDriver.calcAvgStart(driver.getStart());
                    fsDriver.addPlayoffPoints(driver.getPlayoffPoints());

                    if(driver.getFinish() == 1){
                        fsDriver.incrementWins();
                        if(racesRun > 26 && fsDriver.isInPostSeason()) {
                            fsDriver.incrementPlayoffWins();
                        }
                    }
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
                        } else if(YEAR > 2010 && YEAR < 2014) {
                            if(fsDriver.getPointsPosition() < 11) {
                                fsDriver.putInPostSeason();
                                fsDriver.setPoints((fsDriver.getWinCount()*3) + 2000);
                            } else if(fsDriver.getPointsPosition() > 10 && fsDriver.getPointsPosition() < 21) {
                                fsDriversArr.add(fsDriver);
                            }
                        } else if(YEAR > 2013) {
                            if(fsDriver.getPointsPosition() < 17) { //&& fsDriver.getRacesRun() == 26){
                                fsDriversArr.add(fsDriver);
                            } else if(fsDriver.getPointsPosition() > 16 && fsDriver.getPointsPosition() < 31 && fsDriver.getRacesRun() == 26 && fsDriver.getWinCount() > 0) {
                                fsDriversArr.add(fsDriver);
                            }
                        }
                    } else if(YEAR > 2013 && (racesRun == 29 || racesRun == 32 || racesRun == 35)) {
                        int cutoff = 13, subtractPoints = 0;
                        if(racesRun == 32) {
                            cutoff = 9;
                            subtractPoints = 1000;
                        }
                        if(racesRun == 35) {
                            cutoff = 5;
                            subtractPoints = 2000;
                        }
                        if(fsDriver.getPointsPosition() < cutoff || (fsDriver.isInPostSeason() && fsDriver.getPlayoffWins() > 0)) {
                            fsDriversArr.add(fsDriver);
                        } else if(fsDriver.isInPostSeason()){
                            fsDriver.removeFromPostSeason();
                            fsDriver.setPoints(fsDriver.getSeasonPlayoffPoints()); //setting points back if they are out of playoffs
                            //fsDriver.subtractPoints(subtractPoints);
                        }
                    }

                } else {
                    //add the driver to full season driver list
                    seasonDriverList.add(new FullSeasonDriver(driver.getFirstName(), driver.getLastName(), driver.getNumber(), driver.getLapsRun(), driver.getLapsLed(), driver.getPoints(), driver.getStart(), driver.getFinish(), driver.getStatus()));
                }
            }

            if(YEAR > 2016 && racesRun == 26) {
                Collections.sort(seasonDriverList, new FullSeasonDriver.SortByPoints());
                for(int i = 0; i<10; i++)
                    seasonDriverList.get(i).setPointsPosition(i+1);
            }
            if(fsDriversArr.size() > 0) {
                setWildCardAndPlayoffs(fsDriversArr, racesRun);
            }

        }

        //loop through seasonDriverList and set driver's points position, maybe calculate next and leader points here
        Collections.sort(seasonDriverList, new FullSeasonDriver.SortByPoints());
        for(int i = 0; i<seasonDriverList.size(); i++) {
            seasonDriverList.get(i).setPointsPosition(i+1);
            seasonDriverList.get(i).setLeader(seasonDriverList.get(0).getPoints() - seasonDriverList.get(i).getPoints());
            if (i == 0)
                seasonDriverList.get(i).setNext(0);
            else
                seasonDriverList.get(i).setNext(seasonDriverList.get(i - 1).getPoints() - seasonDriverList.get(i).getPoints());
        }

        prevStandings[index] = makeDeepCopyOfStandings(seasonDriverList);
    }

    public List<FullSeasonDriver> getSeasonDriverList() {
        return this.seasonDriverList;
    }

    public void printOutSeasonDriverList() {
        Collections.sort(seasonDriverList, new FullSeasonDriver.SortByPoints());
        for (FullSeasonDriver driver : seasonDriverList) {
            System.out.println(driver);
        }
    }

    public void printOutFullSeasonRaces() {
        for(int i=0; i<racesRun; i++) {
            System.out.println("R" + i);
            for(FullSeasonDriver driver : prevStandings[i]) {
                System.out.println(driver);
            }
        }
    }

    public String getAllRacesJSON() {
        StringBuilder json = new StringBuilder("{");
        for(int i=0; i<racesRun; i++) {
            json.append("\"R").append(i + 1).append("\":").append(NRUtils.convertListToJSON(prevStandings[i])).append(",");
        }
        return json.deleteCharAt(json.lastIndexOf(",")).append("}").toString();
    }

    public String getAllRacesPrettyJSON() {
        StringBuilder json = new StringBuilder("{\n");
        for(int i=0; i<racesRun; i++) {
            json.append("\"R").append(i + 1).append("\": ").append(NRUtils.convertListToPrettyJSON(prevStandings[i])).append(",\n");
        }
        return json.deleteCharAt(json.lastIndexOf(",")).append("}").toString();
    }

    public void setWildCardAndPlayoffs(ArrayList<FullSeasonDriver> fsDriversArr, int raceNum) {
        int resetPoints = 3000;
        int cutoff = 12;
        int subtractPoints = 0;
        if(raceNum == 26) {
            Collections.sort(fsDriversArr, new FullSeasonDriver.SortByWinsThenPoints());
        } else if(raceNum == 29 || raceNum == 32 || raceNum == 35) {
            Collections.sort(fsDriversArr, new FullSeasonDriver.SortByPlayoffWinsThenPoints());
            if(raceNum == 32){
                resetPoints = 4000;
                cutoff = 8;
                subtractPoints = 1000;
            } else if(raceNum == 35) {
                resetPoints = 5000;
                cutoff = 4;
                subtractPoints = 2000;
            }
        }

        if(YEAR < 2014) {
            fsDriversArr.get(0).putInPostSeason();
            fsDriversArr.get(0).setPoints(2000);
            fsDriversArr.get(1).putInPostSeason();
            fsDriversArr.get(1).setPoints(2000);
        } else if(raceNum == 26) {
            for(int i=0; i < 16; i++) {
                fsDriversArr.get(i).putInPostSeason();
                if(YEAR < 2017){
                    fsDriversArr.get(i).setPoints((fsDriversArr.get(i).getWinCount()*3) + 2000);
                } else {
                    int pointsFinish = (fsDriversArr.get(i).getPointsPosition() < 11) ? fsDriversArr.get(i).getPointsPosition() : 0;
                    fsDriversArr.get(i).addPlayoffPoints(TOP_10_POINTS_17[pointsFinish]);
                    fsDriversArr.get(i).setPoints(fsDriversArr.get(i).getPlayoffPoints() + 2000);
                }
                fsDriversArr.get(i).setSeasonPlayoffPoints(fsDriversArr.get(i).getPoints());
            }
        } else if(raceNum != 36) {
            for(int i=0; i < cutoff; i++) {
                fsDriversArr.get(i).resetPlayoffWins();
                if(YEAR < 2017) {
                    fsDriversArr.get(i).setPoints(resetPoints);
                } else {
                    fsDriversArr.get(i).setPoints(resetPoints + fsDriversArr.get(i).getPlayoffPoints());
                }
            }
            for(int i = cutoff; i < fsDriversArr.size(); i++) {
                fsDriversArr.get(i).removeFromPostSeason();
                fsDriversArr.get(i).setPoints(fsDriversArr.get(i).getSeasonPlayoffPoints());
                //fsDriversArr.get(i).subtractPoints(subtractPoints); //this needs to be changed because points won't reset correctly
            }
        }
    }

    public List<FullSeasonDriver> makeDeepCopyOfStandings(List<FullSeasonDriver> list) {
        List<FullSeasonDriver> newList = new ArrayList<FullSeasonDriver>();
        for(FullSeasonDriver driver : list) {
            newList.add(new FullSeasonDriver(driver.getFirstName(), driver.getLastName(), driver.getNumber(), driver.getLapsRun(), driver.getLapsLed(), driver.getPoints(), driver.getPointsPosition(), driver.getRacesRun(), driver.getPoles(), driver.getWinCount(), driver.getT5(), driver.getT10(), driver.getDNFCount(), driver.getPlayoffPoints(), driver.getLeader(), driver.getNext(), driver.getAvgFinish(), driver.getAvgStart()));
        }
        return newList;
    }

}
