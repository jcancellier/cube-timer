package com.jcancellier.cubetimer;

/**
 * Created by joshuacancellier on 12/28/17.
 */

public class SolveInfo {
    private String time;
    private String scramble;
    private String date;

    public SolveInfo(){
        this.time = "00:00:36";
        this.scramble = "F R B2 D L2 R' B R B R2 R' D2 F' R2";
    }

    public String getTime(){
        return time;
    }

    public String getDate(){ return date; }

    public String getScramble(){
        return scramble;
    }

    public void setTime(String time){
        this.time = time;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setScramble(String scramble){
        this.scramble = scramble;
    }

    public  int toMilliseconds(){
        int msCount = 0;
        int count = time.length();

        //in case string includes hours (future proof)
        if(count > 8){
            msCount += Integer.parseInt(time.substring(count-11, count-9)) * 3600000;
        }

        //Convert minutes to milliseconds
        msCount += Integer.parseInt(time.substring(count-8, count-6)) * 60000;

        //Convert seconds to milliseconds
        msCount += Integer.parseInt(time.substring(count-5, count-3)) * 1000;

        //append a zero to millisecond from string and add it to msCount
        msCount += Integer.parseInt(time.substring(count-2, count)) * 10;

        return msCount;
    }
}
