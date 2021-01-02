package com.example.householdhelper;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatDialogFragment;

public class NewListDialog extends AppCompatDialogFragment {
    private EditText editTextListName;
    private NewListDialogListener listener;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.layout_dialog, null);

        builder.setView(view)
                .setTitle("Create a new list")
                .setNegativeButton("back", (dialog, which) -> {

                })
                .setPositiveButton("create", (dialog, which) -> {
                    String name = editTextListName.getText().toString();
                    listener.createNewList(name);
                });

        editTextListName = view.findViewById(R.id.edit_listname);

        return builder.create();
    }

    @Override
    public void onAttach(Context context){
        super.onAttach(context);
        try{
            listener = (NewListDialogListener) context;
        } catch (ClassCastException e){
            throw new ClassCastException(context.toString() + "must implement NewListDialogListener");
        }
    }

    public interface NewListDialogListener {
        void createNewList(String name);
    }

}
