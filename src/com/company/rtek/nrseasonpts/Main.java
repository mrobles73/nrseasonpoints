package com.company.rtek.nrseasonpts;

import com.company.rtek.utils.FileUtils;
import com.company.rtek.utils.NRUtils;

import java.util.List;

public class Main {
    public static void main(String[] args) throws Exception{
        System.out.println("NR Season Points Program Running");
        String [] fileNames = {"R1", "R2", "R3", "R4", "R5", "R6", "R7", "R8", "R9", "R10", "R11", "R12", "R13", "R14", "R15", "R16", "R17", "R18", "R19", "R20", "R21", "R22", "R23", "R24", "R25", "R26", "R27", "R28", "R29", "R30", "R31", "R32", "R33", "R34", "R35", "R36"};
        String [] fileNames26 = {"R1", "R2", "R3", "R4", "R5", "R6", "R7", "R8", "R9", "R10", "R11", "R12", "R13", "R14", "R15", "R16", "R17", "R18", "R19", "R20", "R21", "R22", "R23", "R24", "R25", "R26"};
        String [] fileNames29 = {"R1", "R2", "R3", "R4", "R5", "R6", "R7", "R8", "R9", "R10", "R11", "R12", "R13", "R14", "R15", "R16", "R17", "R18", "R19", "R20", "R21", "R22", "R23", "R24", "R25", "R26", "R27", "R28", "R29"};
        String [] fileNames32 = {"R1", "R2", "R3", "R4", "R5", "R6", "R7", "R8", "R9", "R10", "R11", "R12", "R13", "R14", "R15", "R16", "R17", "R18", "R19", "R20", "R21", "R22", "R23", "R24", "R25", "R26", "R27", "R28", "R29", "R30", "R31", "R32"};
        String [] fileNames35 = {"R1", "R2", "R3", "R4", "R5", "R6", "R7", "R8", "R9", "R10", "R11", "R12", "R13", "R14", "R15", "R16", "R17", "R18", "R19", "R20", "R21", "R22", "R23", "R24", "R25", "R26", "R27", "R28", "R29", "R30", "R31", "R32", "R33", "R34", "R35"};
        String [] fileNames1 = {"R1"};
        //System.out.println(FileUtils.readInHTMLToString("C:\\Papyrus\\NASCAR Racing 2003 Season\\exports_imports\\test results.html"));
        CupSeason season = new CupSeason(2014);
        for(int i=0; i<fileNames.length; i++) {
            List<SingleRaceDriver> raceResultsList = NRUtils.parseHTMLRaceStandings(FileUtils.readInHTMLToDocument("C:\\Papyrus\\NASCAR Racing 2003 Season\\exports_imports\\"+fileNames[i]+".html"), season.getYear());
            season.addRace(raceResultsList, i);
        }

        //season.printOutSeasonDriverList();
        season.printOutFullSeasonRaces();

//        String json = NRUtils.convertListToJSON(season.getSeasonDriverList());
//        String prettyJSON = NRUtils.convertListToPrettyJSON(season.getSeasonDriverList());
//        System.out.println(prettyJSON);

        //List<SingleRaceDriver> raceResultsList = NRUtils.parseHTMLRaceStandings(FileUtils.readInHTMLToDocument("C:\\Papyrus\\NASCAR Racing 2003 Season\\exports_imports\\test results.html"));



    }
}