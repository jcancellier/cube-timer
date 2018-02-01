package com.jcancellier.cubetimer.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jcancellier.cubetimer.Communicator;
import com.jcancellier.cubetimer.CubeID;
import com.jcancellier.cubetimer.R;
import com.jcancellier.cubetimer.SolveList;
import com.jcancellier.cubetimer.TinyDB;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GridLabelRenderer;

import java.util.ArrayList;

/**
 * Created by joshuacancellier on 12/19/17.
 */

public class StatsFragment extends Fragment {
    TextView bestTime, worstTime;
    ArrayList<SolveList> solveList;
    int puzzleIndex;        // 0 = 2x2, 1 = 3x3, 2 = 4x4, 3 = 5x5
    TinyDB tinyDB;
    Communicator comm;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.stats_fragment, container, false);

        final Toolbar myToolBar = rootView.findViewById(R.id.statsToolbar);
        bestTime = rootView.findViewById(R.id.statsBestTime);
        worstTime = rootView.findViewById(R.id.statsWorstTIme);
        tinyDB = new TinyDB(this.getContext());

        //Get current puzzle's solves
        comm = (Communicator) getActivity();
        puzzleIndex = comm.getCube();


        //restore previous solves from all cubes///////
        ArrayList<Object> playerObjects = tinyDB.getListObject("AllCubeSolves", SolveList.class);
        solveList = new ArrayList<SolveList>();

        for(Object objs : playerObjects){
            solveList.add((SolveList)objs);
        }

        for(int i = 0; i < CubeID.values().length; i++){
            solveList.add(new SolveList());
        }
        ///////////////////////////////////////////////

        //set various stats TODO: add more stats
        bestTime.setText(solveList.get(puzzleIndex).getBestTime());
        worstTime.setText(solveList.get(puzzleIndex).getWorstTime());



        //Set custom Stats Toolbar
        ((AppCompatActivity) getActivity()).setSupportActionBar(myToolBar);
        myToolBar.setTitleTextAppearance(getActivity(), R.style.ToolbarTitleText);
        myToolBar.setTitle(R.string.title_stats);
        myToolBar.setTitleTextColor(getResources().getColor(R.color.textTimer));

        //Set up GraphView
        GraphView gv = rootView.findViewById(R.id.graph);
        GridLabelRenderer glr = gv.getGridLabelRenderer();
        glr.setPadding(32);
        gv.getGridLabelRenderer().setLabelsSpace(10);
        gv.getGridLabelRenderer().setHorizontalLabelsVisible(false);

        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        Log.v("hello", "hello");
        super.onCreateOptionsMenu(menu,inflater);
        menu.clear();
        inflater.inflate(R.menu.toolbar_items_main, menu);
    }
}
