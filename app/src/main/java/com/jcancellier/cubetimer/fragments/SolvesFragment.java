package com.jcancellier.cubetimer.fragments;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.jcancellier.cubetimer.Communicator;
import com.jcancellier.cubetimer.CubeID;
import com.jcancellier.cubetimer.R;
import com.jcancellier.cubetimer.SolveInfo;
import com.jcancellier.cubetimer.SolveList;
import com.jcancellier.cubetimer.TinyDB;
import com.jcancellier.cubetimer.recyclerComponents.SolveAdapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by joshuacancellier on 12/19/17.
 */

public class SolvesFragment extends Fragment {
    Toolbar myToolBar;
    SolveAdapter solveAdapter;
    ArrayList<SolveList> solveList;     //each solve list pertains to a specific puzzle (4 currently) (2x2, 3x3, 4x4, 5x5)
    int puzzleIndex;        // 0 = 2x2, 1 = 3x3, 2 = 4x4, 3 = 5x5
    TinyDB tinyDB;
    Communicator comm;
    RecyclerView recyclerView;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.solves_fragment, container, false);
        setHasOptionsMenu(true);

        tinyDB = new TinyDB(this.getContext());

        comm = (Communicator) getActivity();

        puzzleIndex = comm.getCube();
        Log.v("Current Cube ID set:", String.format("%d", comm.getCube()));
        Log.v("Current Cube ID:", String.format("%d", puzzleIndex));
        //set up solve list for each of the 4 current puzzles (2x2, 3x3, 4x4, 5x5)
        //solveList = new SolveList[CubeID.values().length];

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

        //set custom toolbar for solves tab
        myToolBar = rootView.findViewById(R.id.solvesToolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(myToolBar);

        //Setting toolbar title attributes
        myToolBar.setTitleTextAppearance(getActivity(), R.style.ToolbarTitleText);
        myToolBar.setTitle(R.string.title_solves);
        myToolBar.setTitleTextColor(getResources().getColor(R.color.textTimer));

        //Add RecyclerView
        recyclerView = rootView.findViewById(R.id.cardList);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);

        //create adapter and set recyclerView to that adapter
        solveAdapter = new SolveAdapter(solveList.get(puzzleIndex).getAllSolves());
        recyclerView.setAdapter(solveAdapter);

        if(recyclerView.getAdapter().getItemCount() > 0)
            recyclerView.smoothScrollToPosition(recyclerView.getAdapter().getItemCount()-1);
        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        Log.v("hello", "hello");
        super.onCreateOptionsMenu(menu,inflater);
        menu.clear();
        inflater.inflate(R.menu.toolbar_items_solves, menu);
    }

    public void changeToolbar(){
        ((AppCompatActivity) getActivity()).setSupportActionBar(myToolBar);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.delete_all){


            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle("Confirm");
            builder.setMessage("Permanently delete all solves?");

            builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    Toast.makeText(getActivity().getApplicationContext(), "Deleted all solves", Toast.LENGTH_SHORT).show();
                    solveList.get(puzzleIndex).getAllSolves().clear();
                    solveAdapter.notifyDataSetChanged();

                    ////////Put new solves to tinyDB/////////////////////////////////////////////////////
                    ArrayList<Object> solveObjects = new ArrayList<Object>();

                    for(SolveList a : solveList){
                        solveObjects.add((Object)a);
                    }

                    tinyDB.putListObject("AllCubeSolves", solveObjects);
                    /////////////////////////////////////////////////////////////////////////////////////
                }
            });
            builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }

            });
            AlertDialog alertDialog = builder.create();
            alertDialog.show();

        }
        if(item.getItemId() == R.id.add_solve){
            Toast.makeText(getActivity().getApplicationContext(), "Custom solve added", Toast.LENGTH_SHORT).show();
        }
        return false;
    }

    public void addSolve(String time, String scramble){
        puzzleIndex = comm.getCube();
        SolveInfo si = new SolveInfo();
        si.setTime(time);
        si.setScramble(scramble);

        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy, h:mm:ss a");
        Date now = new Date();
        si.setDate(sdf.format(now));
        solveList.get(puzzleIndex).addSolve(si);
        Log.v("Solve size: ", String.format("%d", solveList.get(puzzleIndex).getAllSolves().size()));

        ////////Put new solves to tinyDB/////////////////////////////////////////////////////
        ArrayList<Object> solveObjects = new ArrayList<Object>();

        for(SolveList a : solveList){
            solveObjects.add((Object)a);
        }

        tinyDB.putListObject("AllCubeSolves", solveObjects);
    }

    public void scrollToListTop(){
        recyclerView.smoothScrollToPosition(0);
    }
}
