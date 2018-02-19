package com.jcancellier.cubetimer;

import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnMenuTabClickListener;


import com.jcancellier.cubetimer.fragments.SolvesFragment;
import com.jcancellier.cubetimer.fragments.StatsFragment;
import com.jcancellier.cubetimer.fragments.TimerFragment;

import uk.co.deanwild.materialshowcaseview.MaterialShowcaseView;

public class MainActivity extends AppCompatActivity implements Communicator{
    public final TimerFragment fragment1 = new TimerFragment();
    public final SolvesFragment fragment2 = new SolvesFragment();
    public final StatsFragment fragment3 = new StatsFragment();
    Fragment active;
    //BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener;
    public static TinyDB tinyDB;
    int currentPuzzle;
    BottomBar bottomBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(null);   //make null to prevent overlapping
        setContentView(R.layout.activity_main);

        tinyDB = new TinyDB(this);

        //if returning user: get last puzzle worked on by user
        //else (first time): set default puzzle
        if (tinyDB.getInt("CubeSelected") != -1)
            currentPuzzle = tinyDB.getInt("CubeSelected");
        else {
            currentPuzzle = CubeID.THREEXTHREE.ordinal();
            tinyDB.putInt("CubeSelected", currentPuzzle);
        }

        //Setting toolbar title attributes
        //myToolBar.setTitleTextAppearance(this, R.style.ToolbarTitleText);
        //myToolBar.setTitle("Cube Timer");
        //myToolBar.setTitleTextColor(getResources().getColor(R.color.textTimer));

        //Display Toast
        // TODO: make custom Toast with different position and background color
        //Toast.makeText(getApplicationContext(), "Press and hold to start timer!", Toast.LENGTH_LONG).show();

        //BottomBar initialization and settings
        bottomBar = BottomBar.attach(this, savedInstanceState);
        bottomBar.noTabletGoodness();
        bottomBar.setItems(R.menu.navigation);
        bottomBar.useDarkTheme();
        bottomBar.getBar().setBackgroundColor(getResources().getColor(R.color.transparent));
        bottomBar.setActiveTabColor(getResources().getColor(R.color.colorPrimaryDark));

        final FragmentManager fm = getSupportFragmentManager();

        //add fragments (check if fragments exist before adding them)
        //NOTE: fixes the case where there are overlapping fragments when activity is resumed
        //if (fm.getFragments().isEmpty()){
            fm.beginTransaction().add(R.id.mainActivity, fragment3, "3").hide(fragment3).commit();
            fm.beginTransaction().add(R.id.mainActivity, fragment2, "2").hide(fragment2).commit();
            fm.beginTransaction().add(R.id.mainActivity, fragment1, "1").hide(fragment1).commit();
        //}

        //show timer fragment by default
        fm.beginTransaction().show(fragment1).hide(fragment2).hide(fragment3).commit();
        active = fragment1;

        //switch fragments based on bottombar icon click
        bottomBar.setOnMenuTabClickListener(new OnMenuTabClickListener() {
            @Override
            public void onMenuTabSelected(@IdRes int menuItemId) {
                if(menuItemId == R.id.navigation_timer){
                        fm.beginTransaction().hide(active).show(fragment1).commit();
                    //else
                        //fm.beginTransaction().hide(active).show(fragment1).commit();
                    active = fragment1;
                    ///////////////////////////////////////////////////////////////
                    //fm.beginTransaction().hide(fragment2).hide(fragment3).commit();
                    ///////////////////////////////////////////////////////////////
                    // Set title bar
                    //fragment1.changeToolbar();
                }
                if(menuItemId == R.id.navigation_list){
                    fm.beginTransaction().hide(active).commit();
                    active = fragment2;
                    ///////////////////////////////////////////////////////////////
                    //fm.beginTransaction().hide(fragment1).hide(fragment3).commit();
                    ///////////////////////////////////////////////////////////////
                    Fragment frg = fm.findFragmentByTag("2");
                    final FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                    //ft.replace(R.id.mainActivity, fragment2).commit();
                    fm.beginTransaction().show(active).commit();
                    //ft.show(frg);
                    //ft.commit();
                    //change toolbar
                    fragment2.changeToolbar();
                }
                if(menuItemId == R.id.navigation_stats){
                    fm.beginTransaction().hide(active).commit();
                    active = fragment3;
                    ///////////////////////////////////////////////////////////////
                    //fm.beginTransaction().hide(fragment2).hide(fragment1).commit();
                    ///////////////////////////////////////////////////////////////
                    Fragment frg = fm.findFragmentByTag("3");
                    final FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                    ft.detach(frg);
                    ft.attach(frg);
                    ft.show(frg);
                    ft.commit();
                }
            }

            @Override
            public void onMenuTabReSelected(@IdRes int menuItemId) {
                if(menuItemId == R.id.navigation_list){
                    fragment2.scrollToListTop();
                }
            }
        });

    }

    @Override
    public void respond(String time, String scramble) {
        fragment2.addSolve(time, scramble);
    }

    @Override
    public void setCube(int id) {
        currentPuzzle = id;
        tinyDB.putInt("Cube Selected", currentPuzzle);
    }

    @Override
    public int getCube() {
        return currentPuzzle;
    }

    @Override
    public void resetChronometer(){
        fragment1.newScramble();
        fragment1.resetChronometer();
    }

    // Set the color for the active tab. Ignored on mobile when there are more than three tabs.
        //bottomBar.setActiveTabColor(R.color.flatRed);
        //fragment switcher
        /*mOnNavigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.navigation_timer) {
                    if (active != fragment1)
                        fm.beginTransaction().show(fragment1).commit();
                    else
                        fm.beginTransaction().hide(active).show(fragment1).commit();
                    active = fragment1;
                    fm.beginTransaction().hide(fragment2).hide(fragment3).commit();
                    return true;
                }
                if (item.getItemId() == R.id.navigation_list) {
                    Log.v("Hello 2", "Hello 2");
                    fm.beginTransaction().hide(active).show(fragment2).commit();
                    active = fragment2;
                    fm.beginTransaction().hide(fragment1).hide(fragment3).commit();
                    return true;
                }
                if (item.getItemId() == R.id.navigation_stats) {
                    fm.beginTransaction().hide(active).show(fragment3).commit();
                    active = fragment3;
                    fm.beginTransaction().hide(fragment2).hide(fragment1).commit();
                    return true;
                }
                return false;
            }
        };

        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        */
    /*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_items_main, menu);
        return true;
    }
    */
}
