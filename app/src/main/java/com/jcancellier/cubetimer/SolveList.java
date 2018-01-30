package com.jcancellier.cubetimer;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by joshuacancellier on 1/9/18.
 */

public class SolveList {
    private List<SolveInfo> allSolves;

    public SolveList(){
        allSolves = new ArrayList<>();
    }

    public List<SolveInfo> getAllSolves() {
        return allSolves;
    }

    public void addSolve(SolveInfo si){
        allSolves.add(si);
    }

    public String getBestTime(){
        if(allSolves.isEmpty())
            return "-----";
        int best = allSolves.get(0).toMilliseconds();
        String bestString = allSolves.get(0).getTime();
        for(int i = 1; i < allSolves.size(); i++){
            if(allSolves.get(i).toMilliseconds() < best){
                best = allSolves.get(i).toMilliseconds();
                bestString = allSolves.get(i).getTime();
            }
        }
        return bestString;
    }

    public String getWorstTime(){
        if(allSolves.isEmpty())
            return "-----";
        int worst = allSolves.get(0).toMilliseconds();
        String worstString = allSolves.get(0).getTime();
        for(int i = 1; i < allSolves.size(); i++){
            if(allSolves.get(i).toMilliseconds() > worst){
                worst = allSolves.get(i).toMilliseconds();
                worstString = allSolves.get(i).getTime();
            }
        }
        return worstString;
    }

    public int getSize(){
        return allSolves.size();
    }
}
