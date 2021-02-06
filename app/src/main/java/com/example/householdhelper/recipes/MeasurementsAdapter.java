package com.example.householdhelper.recipes;

import android.app.Activity;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.householdhelper.R;

import java.util.ArrayList;

/**
 * Extended RecyclerView Adapter for displaying ingredients
 *
 * @author Bryan Burdick
 * @version 1.0
 * @since 2021-02-06
 */
public class MeasurementsAdapter extends RecyclerView.Adapter<MeasurementsAdapter.MeasurementTypesAdapterViewHolder> {
    private final ArrayList<Measurement> list;
    public boolean editMode;
    private final ArrayList<MeasurementTypesAdapterViewHolder> viewHolders = new ArrayList<>();

    /**
     * Toggles whether text should be editable or not
     * @param editMode the new state for text editability
     */
    public void setEditMode(boolean editMode){
        this.editMode = editMode;
        MeasurementTypesAdapterViewHolder holder;
        for(int i = 0; i < viewHolders.size(); i++){
            holder = viewHolders.get(i);
            holder.editMode = editMode;
            if(editMode){
                holder.measurementTextView.setVisibility(View.GONE);
                holder.measurementAmountEditText.setVisibility(View.VISIBLE);
                holder.measurementsSpinner.setVisibility(View.VISIBLE);
            }else{
                holder.measurementTextView.setVisibility(View.VISIBLE);
                holder.measurementAmountEditText.setVisibility(View.GONE);
                holder.measurementsSpinner.setVisibility(View.GONE);
            }
        }
    }

    public static class MeasurementTypesAdapterViewHolder extends RecyclerView.ViewHolder{
        public TextView measurementTextView;
        public EditText measurementAmountEditText;
        public String measurementType;
        public String measurementAmount;
        public boolean editMode = false;
        private Context context;
        public Spinner measurementsSpinner;

        public MeasurementTypesAdapterViewHolder(View itemView){
            super(itemView);
            measurementTextView = itemView.findViewById(R.id.measurementTextView);
            measurementAmountEditText = itemView.findViewById(R.id.measurementAmountEditText);

            measurementsSpinner = itemView.findViewById(R.id.measurementsSpinner);
        }
    }

    /**
     * default constructor
     * @param inList an ArrayList of Measurements
     */
    public MeasurementsAdapter(ArrayList<Measurement> inList){
        list = inList;
    }

    @Override
    public MeasurementTypesAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.measurement_list_item, parent, false);
        MeasurementTypesAdapterViewHolder viewHolder = new MeasurementTypesAdapterViewHolder(v);
        viewHolder.context = parent.getContext();
        return viewHolder;
    }

    /**
     * sets values of holder's views to match items in the ArrayList
     * @param holder current ViewHolder
     * @param position ViewHolder's index in the ArrayList
     */
    @Override
    public void onBindViewHolder(@NonNull MeasurementTypesAdapterViewHolder holder, int position) {
        holder.editMode = editMode;
        Measurement currentItem = list.get(position);
        holder.measurementAmount = currentItem.getAmount();
        holder.measurementType = currentItem.getType();

        // TODO: get measurement types from database
        ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(holder.context, R.array.measurement_types, android.R.layout.simple_spinner_item);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        holder.measurementsSpinner.setAdapter(spinnerAdapter);
        holder.measurementsSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                holder.measurementType = holder.measurementsSpinner.getSelectedItem().toString();
                holder.measurementTextView.setText(holder.measurementAmount + " " + holder.measurementType);
                list.get(holder.getAdapterPosition()).setType(holder.measurementsSpinner.getSelectedItem().toString());

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        String[] measurements = holder.context.getResources().getStringArray(R.array.measurement_types);

        boolean found = false;
        for(int i = 0; !found && i < measurements.length; i++){

            if(measurements[i].equals(holder.measurementType)){

                holder.measurementsSpinner.setSelection(i);
                found = true;
            }
        }

        holder.measurementAmount = currentItem.getAmount();
        if(holder.measurementAmount.isEmpty() || holder.measurementAmount.equals("") || holder.measurementAmount.equals("0")){
            holder.measurementTextView.setText(holder.measurementAmount + " " + holder.measurementType);
        }else{
            holder.measurementTextView.setText("");
        }
        holder.measurementAmountEditText.setHint(String.valueOf(holder.measurementAmount));
        holder.measurementAmountEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(holder.editMode){
                    try{
                        holder.measurementAmount = String.valueOf(s);
                        list.get(holder.getAdapterPosition()).setAmount(holder.measurementAmount);

                    }catch(Exception e){
                    }

                    if(!holder.measurementAmount.isEmpty() || !holder.measurementAmount.equals("") || !holder.measurementAmount.equals("0")){
                        holder.measurementTextView.setText(holder.measurementAmount + " " + holder.measurementType);
                    }else{
                        holder.measurementTextView.setText("");
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        holder.measurementAmountEditText.setOnKeyListener((v, keyCode, event) -> {
            // If the event is a key-down event on the "enter" button
            if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                    (keyCode == KeyEvent.KEYCODE_ENTER)) {
                // Close keyboard
                InputMethodManager imm = (InputMethodManager) holder.context.getSystemService(Activity.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                return true;
            }
            return false;
        });

        if(editMode){
            holder.measurementTextView.setVisibility(View.GONE);
            holder.measurementAmountEditText.setVisibility(View.VISIBLE);
            holder.measurementsSpinner.setVisibility(View.VISIBLE);
        }else{
            if(!holder.measurementAmount.isEmpty() || !holder.measurementAmount.equals("") || !holder.measurementAmount.equals("0")){
                holder.measurementTextView.setText(holder.measurementAmount + " " + holder.measurementType);
            }else{
                holder.measurementTextView.setText("");
            }

            holder.measurementTextView.setVisibility(View.VISIBLE);
            holder.measurementAmountEditText.setVisibility(View.GONE);
            holder.measurementsSpinner.setVisibility(View.GONE);
        }

        viewHolders.add(holder);
    }

    /**
     * returns the size of the ArrayList
     * @return size of the ArrayList
     */
    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public String toString(){
        String ret = "[ ";

        MeasurementTypesAdapterViewHolder holder;

        for(int i = 0; i < viewHolders.size(); i++){
            holder = viewHolders.get(i);
            ret += holder.measurementAmount + " " + holder.measurementType;
            if(i < viewHolders.size()-1){
                ret += ", ";
            }
        }

        ret += " ]\n";

        return ret;
    }
}