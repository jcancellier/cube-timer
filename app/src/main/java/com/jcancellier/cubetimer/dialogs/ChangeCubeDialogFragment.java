package com.jcancellier.cubetimer.dialogs;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;

import com.jcancellier.cubetimer.Communicator;
import com.jcancellier.cubetimer.TinyDB;

/**
 * DialogFragment that is displayed when user wishes to switch session to an alternate puzzle
 *
 * Created by Joshua Cancellier on 1/9/18.
 */

public class ChangeCubeDialogFragment extends DialogFragment {
    TinyDB tinyDB;
    int mCheckedItem;
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        tinyDB = new TinyDB(getContext());
        Log.v("TinyDB value", String.format("%d", tinyDB.getInt("CubeSelected")));
        if(tinyDB.getInt("CubeSelected") != -1)
            mCheckedItem = tinyDB.getInt("CubeSelected");
        else
            mCheckedItem = 1;
        Log.v("mCheckedItem", String.format("%d", mCheckedItem));
        //TODO: check null
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        CharSequence[] items = {"2x2x2 Cube", "3x3x3 Cube", "4x4x4 Cube", "5x5x5 Cube"};
        builder.setTitle("Change Puzzle")

        .setSingleChoiceItems(items, mCheckedItem, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        })
                // Set the action buttons
                .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        // User clicked OK, so save the mSelectedItems results somewhere
                        // or return them to the component that opened the dialog
                        mCheckedItem = ((AlertDialog)dialog).getListView().getCheckedItemPosition();

                        Communicator comm = (Communicator) getActivity();
                        comm.setCube(mCheckedItem);
                        comm.resetChronometer();
                        Log.v("item selected", String.format("%d", mCheckedItem));
                        tinyDB.putInt("CubeSelected", mCheckedItem);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });
        return builder.create();
    }

    public int getSelected(){
        return mCheckedItem;
    }

}
