package com.example.householdhelper;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatDialogFragment;

public class DeleteItemDialog extends AppCompatDialogFragment {
    private DeleteItemDialogListener listener;
    public static final String TAG = "DeleteItemDialog";

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        Log.d(TAG, "onCreateDialog: started");
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        Log.d(TAG, "onCreateDialog: setting variables");
        String name = getArguments().getString("itemName");
        int position = getArguments().getInt("itemPosition");

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.yesno_dialog, null);

        builder.setView(view)
                .setTitle("Remove " + name + " from quick add?")
                .setNegativeButton("no", (dialog, which) -> {

                })
                .setPositiveButton("yes", (dialog, which) -> {
                    listener.deleteQuickAddItem(name, position);
                });
        return builder.create();
    }

    @Override
    public void onAttach(Context context){
        super.onAttach(context);
        try{
            listener = (DeleteItemDialogListener) context;
        } catch (ClassCastException e){
            throw new ClassCastException(context.toString() + "must implement DeleteItemDialogListener");
        }
    }

    public interface DeleteItemDialogListener {
    void deleteQuickAddItem(String name, int position);
    }

}
