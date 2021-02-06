package com.example.householdhelper.lists;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import androidx.appcompat.app.AppCompatDialogFragment;

import com.example.householdhelper.R;

/**
 * Yes or no dialog to remove an item from a list.
 *
 * @author Bryan Burdick
 * @version 1.0
 * @since 2021-02-06
 */
public class DeleteItemDialog extends AppCompatDialogFragment {
    private DeleteItemDialogListener listener;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());


        String name = getArguments().getString("itemName");
        int position = getArguments().getInt("itemPosition");

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.yesno_dialog, null);

        builder.setView(view)
                .setTitle("Remove " + name + "?")
                .setNegativeButton("no", (dialog, which) -> {

                })
                .setPositiveButton("yes", (dialog, which) -> {

                    if(listener == null){

                    }else{listener.deleteItem(position);}

                });
        return builder.create();
    }

    @Override
    public void onAttach(Context context){
        super.onAttach(context);
    }

    public interface DeleteItemDialogListener {
        void deleteItem(int position);
    }

    public void setListener(DeleteItemDialogListener listener){
        this.listener = listener;
    }

}
