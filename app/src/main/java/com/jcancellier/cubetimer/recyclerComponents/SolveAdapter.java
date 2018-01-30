package com.jcancellier.cubetimer.recyclerComponents;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jcancellier.cubetimer.R;
import com.jcancellier.cubetimer.SolveInfo;

import java.util.List;

/**
 * Created by joshuacancellier on 12/28/17.
 */

public class SolveAdapter extends RecyclerView.Adapter<SolveAdapter.SolveViewHolder> {

    private List<SolveInfo> solveList;

    public SolveAdapter(List<SolveInfo> solveList) {
        this.solveList = solveList;
    }

    @Override
    public int getItemCount() {
        return solveList.size();
    }

    @Override
    public void onBindViewHolder(SolveViewHolder holder, int position) {
        SolveInfo solveInfo = solveList.get(position);

        holder.vTime.setText(solveInfo.getTime());
        holder.vDate.setText(solveInfo.getDate());
        holder.vScramble.setText(solveInfo.getScramble());
    }

    @Override
    public SolveViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view_solve, parent, false);
        return new SolveViewHolder(itemView);
    }

    public static class SolveViewHolder extends RecyclerView.ViewHolder {
        //Card components
        protected TextView vTime;
        protected TextView vDate;
        protected TextView vScramble;

        public SolveViewHolder(View v) {
            super(v);

            //get reference to card components
            vTime = v.findViewById(R.id.time);
            vDate = v.findViewById(R.id.date);
            vScramble = v.findViewById(R.id.scramble);
        }
    }
}
