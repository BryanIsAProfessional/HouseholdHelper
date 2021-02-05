package com.example.householdhelper.recipes;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.database.DataSetObserver;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.householdhelper.R;
import com.example.householdhelper.helpers.DatabaseHelper;

import java.util.ArrayList;
import java.util.Collections;

public class IngredientsAdapter extends RecyclerView.Adapter<IngredientsAdapter.IngredientsAdapterViewHolder> {
    private final ArrayList<Ingredient> list;
    public boolean editMode;
    private final ArrayList<IngredientsAdapter.IngredientsAdapterViewHolder> viewHolders = new ArrayList<>();
    public static final String TAG = "IngredientsAdapter";

    public void setEditMode(boolean editMode){
        this.editMode = editMode;
        IngredientsAdapter.IngredientsAdapterViewHolder holder;
        for(int i = 0; i < viewHolders.size(); i++){
            holder = viewHolders.get(i);
            holder.editMode = editMode;
            ((MeasurementsAdapter)holder.measurementsRecyclerView.getAdapter()).setEditMode(editMode);
            if(editMode){
                holder.ingredientNameTextView.setVisibility(View.GONE);
                holder.ingredientNameEditText.setVisibility(View.VISIBLE);
                holder.addMeasurementButton.setVisibility(View.VISIBLE);
            }else{
                holder.ingredientNameTextView.setVisibility(View.VISIBLE);
                holder.ingredientNameEditText.setVisibility(View.GONE);
                holder.addMeasurementButton.setVisibility(View.GONE);
            }
        }
    }

    public static class IngredientsAdapterViewHolder extends RecyclerView.ViewHolder{
        public TextView ingredientNameTextView;
        public EditText ingredientNameEditText;
        public ImageButton addMeasurementButton;
        public String ingredientName;
        public boolean editMode = false;
        private Context context;
        public RecyclerView measurementsRecyclerView;
        public MeasurementsAdapter measurementsAdapter;
        public RecyclerView.LayoutManager measurementsLayoutManager;

        public IngredientsAdapterViewHolder(View itemView){
            super(itemView);
            ingredientNameTextView = itemView.findViewById(R.id.ingredientNameTextView);
            ingredientNameEditText = itemView.findViewById(R.id.ingredientNameEditText);

            measurementsRecyclerView = itemView.findViewById(R.id.measurementsRecyclerView);
            addMeasurementButton = itemView.findViewById(R.id.addMeasurementButton);

        }
    }

    public IngredientsAdapter(ArrayList<Ingredient> inList){
        list = inList;
    }

    @Override
    public IngredientsAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.ingredient_list_item, parent, false);
        IngredientsAdapterViewHolder viewHolder = new IngredientsAdapterViewHolder(v);
        viewHolder.context = parent.getContext();
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull IngredientsAdapterViewHolder holder, int position) {
        holder.editMode = editMode;
        Ingredient currentItem = list.get(position);

        //TODO: get measurements from database

        holder.measurementsLayoutManager = new LinearLayoutManager(holder.context) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        holder.measurementsAdapter = new MeasurementsAdapter(currentItem.getMeasurementsList());
        holder.measurementsRecyclerView.setLayoutManager(holder.measurementsLayoutManager);
        holder.measurementsRecyclerView.setAdapter(holder.measurementsAdapter);

        holder.ingredientName = currentItem.getName();
        holder.ingredientNameTextView.setText(holder.ingredientName);
        holder.ingredientNameEditText.setHint(holder.ingredientName);
        holder.ingredientNameEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(holder.editMode){
                    holder.ingredientName = String.valueOf(s);
                    holder.ingredientNameTextView.setText(holder.ingredientName);
                    list.get(holder.getAdapterPosition()).setName(holder.ingredientName);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        ItemTouchHelper helper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {

            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                if(holder.editMode){
                    int position = viewHolder.getAdapterPosition();
                    // TODO: remove measurement from database
                    DatabaseHelper db = new DatabaseHelper(holder.context);
                    db.deleteMeasurementById(list.get(holder.getAdapterPosition()).getMeasurementsList().get(position).getId());
                    if(position < list.size()){
                        list.get(holder.getAdapterPosition()).getMeasurementsList().remove(position);
                        holder.measurementsRecyclerView.getAdapter().notifyItemRemoved(position);
                    }else{
                        notifyDataSetChanged();
                    }
                }
            }

            @Override
            public boolean isLongPressDragEnabled() {
                return false;
            }
        });

        helper.attachToRecyclerView(holder.measurementsRecyclerView);

        holder.addMeasurementButton.setOnClickListener(v -> {
            list.get(position).getMeasurementsList().add(new Measurement());
            holder.measurementsAdapter.notifyItemInserted(list.get(position).getMeasurementsList().size()-1);
            ((MeasurementsAdapter)holder.measurementsRecyclerView.getAdapter()).setEditMode(editMode);
        });

        holder.ingredientNameEditText.setOnKeyListener((v, keyCode, event) -> {
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
            holder.ingredientNameTextView.setVisibility(View.GONE);
            holder.ingredientNameEditText.setVisibility(View.VISIBLE);
            holder.addMeasurementButton.setVisibility(View.VISIBLE);
        }else{
            holder.ingredientNameTextView.setVisibility(View.VISIBLE);
            holder.ingredientNameEditText.setVisibility(View.GONE);
            holder.addMeasurementButton.setVisibility(View.GONE);
        }

        viewHolders.add(holder);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public String toString(){
        String ret = "[ ";

        IngredientsAdapter.IngredientsAdapterViewHolder holder;

        for(int i = 0; i < viewHolders.size(); i++){
            holder = viewHolders.get(i);
            ret += holder.ingredientName;
            if(i < viewHolders.size()-1){
                ret += ", ";
            }
        }

        ret += " ]\n";

        return ret;
    }
}