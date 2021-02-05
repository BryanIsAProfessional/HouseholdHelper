package com.example.householdhelper.schedule;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.TimePicker;
import android.widget.ToggleButton;

import androidx.appcompat.app.AppCompatDialogFragment;

import com.example.householdhelper.R;

public class NewMedDialog extends AppCompatDialogFragment {
    private EditText medicineEditText;
    private NewMedDialogListener listener;
    private NumberPicker frequencyNumberPicker, timePeriodNumberPicker, doseSizeNumberPicker, daysInAdvanceNumberPicker;
    private TimePicker timePicker;
    private ToggleButton automaticToggleButton;
    private final String[] timePeriods = {"day", "week"};

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.new_med_dialog, null);

        medicineEditText = view.findViewById(R.id.medicineEditText);
        timePicker = view.findViewById(R.id.reminderTimePicker);
        frequencyNumberPicker = view.findViewById(R.id.frequencyNumberPicker);
        timePeriodNumberPicker = view.findViewById(R.id.timePeriodNumberPicker);
        doseSizeNumberPicker = view.findViewById(R.id.doseSizeNumberPicker);
        automaticToggleButton = view.findViewById(R.id.automaticToggleButton);
        daysInAdvanceNumberPicker = view.findViewById(R.id.daysInAdvanceNumberPicker);

        daysInAdvanceNumberPicker.setMaxValue(14);
        daysInAdvanceNumberPicker.setMinValue(0);
        doseSizeNumberPicker.setMaxValue(100);
        doseSizeNumberPicker.setMinValue(1);
        frequencyNumberPicker.setMaxValue(20);
        frequencyNumberPicker.setMinValue(1);
        timePeriodNumberPicker.setDisplayedValues(timePeriods);

        builder.setView(view)
                .setTitle("Track a new med")
                .setNegativeButton("cancel", (dialog, which) -> {

                })
                .setPositiveButton("track", (dialog, which) -> {
                    String timePeriod = timePeriods[timePeriodNumberPicker.getValue()];
                    listener.trackNewMed(medicineEditText.getText().toString(), doseSizeNumberPicker.getValue(), doseSizeNumberPicker.getValue(), calculateHoursBetweenDoses(frequencyNumberPicker.getValue(), timePeriod), automaticToggleButton.isChecked(), timeToString(timePicker.getCurrentHour(), timePicker.getCurrentMinute()), daysInAdvanceNumberPicker.getValue());
                });

        return builder.create();
    }

    public void setListener(NewMedDialogListener listener){
        this.listener = listener;
    }

    public String timeToString(int hour, int minute){
        return "" + hour + ":" + minute;
    }

    public int calculateHoursBetweenDoses(int num, String period){
        switch(period){
            case "day":
                return (int)24.0/num;
            case "week":
                return (int)24.0*7/num;
            default:
                return -1;
        }

    }

    @Override
    public void onAttach(Context context){
        super.onAttach(context);
        try{
            listener = (NewMedDialogListener) context;
        } catch (ClassCastException e){
            throw new ClassCastException(context.toString() + "must implement NewMedDialogListener");
        }
    }

    public interface NewMedDialogListener {
        void trackNewMed(String name, int remaining, int total, int hoursBetween, boolean automatic, String notifyAt, int daysInAdvance);
    }
}
