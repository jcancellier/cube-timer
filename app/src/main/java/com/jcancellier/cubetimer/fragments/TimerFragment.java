package com.jcancellier.cubetimer.fragments;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.shapes.OvalShape;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jcancellier.cubetimer.dialogs.ChangeCubeDialogFragment;
import com.jcancellier.cubetimer.Chronometer;
import com.jcancellier.cubetimer.Communicator;
import com.jcancellier.cubetimer.R;
import java.util.Random;

import co.mobiwise.materialintro.shape.Focus;
import co.mobiwise.materialintro.shape.FocusGravity;
import co.mobiwise.materialintro.shape.ShapeType;
import co.mobiwise.materialintro.view.MaterialIntroView;
import uk.co.deanwild.materialshowcaseview.MaterialShowcaseView;
import uk.co.deanwild.materialshowcaseview.shape.Shape;
import uk.co.deanwild.materialshowcaseview.target.Target;

/**
 * Created by joshuacancellier on 12/19/17.
 */

public class TimerFragment extends Fragment {
    static int SCRAMBLE_SIZE = 25;
    private long lastPause;
    public Chronometer chronometer;
    TextView txtScramble;
    String scramble;
    Boolean resume = false;
    Boolean startAlready = false;
    Boolean stopAlready = true;
    Boolean isSpeakButtonLongPressed = false;
    RelativeLayout relativeLayout;
    Toolbar myToolBar;
    Communicator comm;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.timer_fragment, container, false);
        setHasOptionsMenu(true);

        //set toolbar for timer fragment (references main activity)
        myToolBar = rootView.findViewById(R.id.timerToolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(myToolBar);
        //Setting toolbar title attributes
        myToolBar.setTitleTextAppearance(getActivity(), R.style.ToolbarTitleText);
        myToolBar.setTitle(R.string.app_name);
        myToolBar.setTitleTextColor(getResources().getColor(R.color.textTimer));

        //disable toolbar title
        //((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowTitleEnabled(false);    //disable title

        //////////////Imported//////////////////////
        txtScramble = rootView.findViewById(R.id.txtScramble);
        chronometer = rootView.findViewById(R.id.jChronometer);
        //mBannerAd = (AdView) rootView.findViewById(R.id.banner_AdView);
        relativeLayout = rootView.findViewById(R.id.relative_layout_timer);

        //showBannerAd();
        newScramble();

        relativeLayout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                chronometer.setTextColor(getResources().getColor(R.color.flatGreen));
                resetTimer();
                isSpeakButtonLongPressed = true;
                return true;
            }
        });

        relativeLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                if(startAlready)
                    scramble = getScramble();
                else if(!isSpeakButtonLongPressed)
                    chronometer.setTextColor(getResources().getColor(R.color.flatRed));
                stopTimer();
                txtScramble.setText(scramble);
                chronometer.animate().translationY(0);
                myToolBar.animate().alpha(1.0f);
                myToolBar.animate().translationY(0);
                Log.v("test", "animation activated");
                //((AppCompatActivity) getActivity()).getSupportActionBar().show();
                //myToolBar.setVisibility(View.VISIBLE);
                txtScramble.animate().alpha(1.0f);
                if(motionEvent.getAction() == MotionEvent.ACTION_UP){

                    if(isSpeakButtonLongPressed) {
                        isSpeakButtonLongPressed = false;
                        chronometer.setTextColor(getResources().getColor(R.color.textTimer));
                        startTimer();
                        myToolBar.animate().alpha(0.0f);
                        txtScramble.animate().alpha(0.0f);
                        //((AppCompatActivity) getActivity()).getSupportActionBar().hide();
                        //myToolBar.setVisibility(View.GONE);
                        myToolBar.animate().translationY(-myToolBar.getHeight());
                        chronometer.animate().translationY(-(chronometer.getHeight()/4.0f));
                        //myToolBar.setVisibility(View.GONE);
                    }
                    else{
                        chronometer.setTextColor(getResources().getColor(R.color.textTimer));
                    }
                }
                return false;
            }
        });

        //Show user tutorial for timer
        showTutorial1();

        return rootView;
    }

    public void newScramble() {
        scramble = getScramble();
        txtScramble.setText(scramble);
    }

    private String getScramble(){
        Random rand = new Random();
        String temp = "";
        String side = "";
        int already = -1;
        int perm1 = -1;
        int perm2 = -1;
        int howManyPerms = 0;

        for(int i = 0; i < SCRAMBLE_SIZE; i++) {
            int randomNumber;
            //Determine which face of the cube
            do {
                randomNumber = rand.nextInt(6);
            }while(randomNumber == already && already != -1);

            //The following if-statements fix the scramble bug that causes illogical permutations i.e. R L R2
            if(howManyPerms == 0) {
                perm1 = randomNumber;
                howManyPerms++;
            }
            else if(howManyPerms == 1) {
                perm2 = randomNumber;
                howManyPerms++;
            }
            else if(howManyPerms == 2){
                if( ((perm1 == 0 || perm2 ==0) && (perm1 == 1 || perm2 == 1)) ||
                        ((perm1 == 2 || perm2 == 2) && (perm1 == 3 || perm2 == 3)) ||
                        ((perm1 == 4 || perm2 == 4) && perm1 == 5 || perm2 == 5)  ){

                    while(randomNumber == perm1 || randomNumber == already){
                        randomNumber = rand.nextInt(6);
                    }
                }
                perm1 = perm2;
                perm2 = randomNumber;
                howManyPerms = 2;
            }
            already = randomNumber;
            switch(randomNumber){
                case 0:
                    side = "R";
                    break;
                case 1:
                    side = "L";
                    break;
                case 2:
                    side = "U";
                    break;
                case 3:
                    side = "D";
                    break;
                case 4:
                    side = "F";
                    break;
                case 5:
                    side = "B";
                    break;
            }
            //Determine whether two turns, prime, or neither
            randomNumber = rand.nextInt(3);
            switch(randomNumber){
                case 0:
                    side = side + "2";
                    break;
                case 1:
                    side = side + "'";
                default:
                    break;

            }
            temp = temp + " " + side;

        }
        Log.v("hello", temp);
        return temp.substring(1, temp.length());
    }

    public void resetTimer(){
        if(startAlready)
            return;
        chronometer.setBase(SystemClock.elapsedRealtime());
        lastPause = SystemClock.elapsedRealtime();
    }

    public void stopTimer(){
        if(stopAlready)
            return;
        lastPause = SystemClock.elapsedRealtime();
        startAlready = false;
        stopAlready = true;
        chronometer.stop();

        String pattern = "MMM dd, yyyy, hh:mm:ss a";

        comm = (Communicator) getActivity();
        comm.respond(chronometer.getText().toString(), txtScramble.getText().toString());
        //MainActivity.addSolve(chronometer.getText().toString(), txtScramble.getText().toString());
        //add solve to solves list with: time and date
        //MainActivity.fragment2.addSolve(chronometer.getText().toString(), new SimpleDateFormat("MMM dd, yyyy, hh:mm:ss a"));
    }


    public void startTimer(){
        if(startAlready)
            return;
        if(!resume) {
            chronometer.setBase(SystemClock.elapsedRealtime());
            resume = true;
        }
        else{
            chronometer.setBase(chronometer.getBase() + SystemClock.elapsedRealtime() - lastPause);
        }
        stopAlready = false;
        startAlready = true;
        chronometer.start();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        Log.v("hello", "hello");
        super.onCreateOptionsMenu(menu,inflater);
        menu.clear();
        inflater.inflate(R.menu.toolbar_items_main, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.change_cube){
            //Toast.makeText(getActivity().getApplicationContext(), "Cube changed", Toast.LENGTH_SHORT).show();
            ChangeCubeDialogFragment changeCubeDialogFragment = new ChangeCubeDialogFragment();
            changeCubeDialogFragment.show(getFragmentManager(), "Change Cube");
        }
        if(item.getItemId() == R.id.settings){
            Toast.makeText(getActivity().getApplicationContext(), "Settings open", Toast.LENGTH_SHORT).show();
        }
        return false;
    }

    public void resetChronometer(){
        chronometer.setText("00:00.00");
    }


    private void showTutorial1(){
        new MaterialShowcaseView.Builder(getActivity())
                .setTarget(chronometer)
                .setDismissText(getResources().getString(R.string.tutorial_1_confirm))
                .setContentText("Touch and hold to start timer")
                .setContentTextColor(getResources().getColor(R.color.textTimer))
                .setDelay(200) // optional but starting animations immediately in onCreate can make them choppy
                 // provide a unique ID used to ensure it is only shown once
                .setMaskColour(getResources().getColor(R.color.tutorial_color1))
                .withRectangleShape(true)
                .setShapePadding(0)
                .setTargetTouchable(true)
                .setFadeDuration(500)
                .show();
    }

}
