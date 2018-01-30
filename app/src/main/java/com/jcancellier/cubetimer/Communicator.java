package com.jcancellier.cubetimer;

import com.roughike.bottombar.BottomBar;

/**
 * Created by joshuacancellier on 12/28/17.
 */

public interface Communicator {
    public void respond(String time, String scramble);
    public void setCube(int id);
    public int getCube();
    public void resetChronometer();
}
