package com.jcancellier.cubetimer.recyclerComponents;

import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
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

        //Overflow menu for CardView
        final PopupMenu popup = new PopupMenu(holder.vScramble.getContext(), holder.vImgButton, Gravity.END, R.attr.actionOverflowMenuStyle, 0);
        popup.inflate(R.menu.cardview_overflow_menu);
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()){
                    case R.id.delete_solve:
                        return true;
                    case R.id.solve_details:
                        return true;
                    default:
                        return true;

                }
            }
        });
        holder.vImgButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popup.show();
            }
        });
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
        protected ImageButton vImgButton;


        public SolveViewHolder(View v) {
            super(v);

            //get reference to card components
            vTime = v.findViewById(R.id.time);
            vDate = v.findViewById(R.id.date);
            vScramble = v.findViewById(R.id.scramble);
            vImgButton = v.findViewById(R.id.cvOverflowMenu);
        }
    }
}
