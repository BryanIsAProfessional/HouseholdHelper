package com.example.householdhelper.recipes;

import android.app.Activity;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.householdhelper.R;

import java.util.ArrayList;

/**
 * Extended RecyclerView Adapter for displaying instructions
 *
 * @author Bryan Burdick
 * @version 1.0
 * @since 2021-02-06
 */
public class InstructionsAdapter extends RecyclerView.Adapter<InstructionsAdapter.InstructionsAdapterViewHolder> {
    private final ArrayList<Instruction> list;
    public boolean editMode;
    private final ArrayList <InstructionsAdapter.InstructionsAdapterViewHolder> viewHolders = new ArrayList<>();

    /**
     * Toggles whether text should be editable or not
     * @param editMode the new state for text editability
     */
    public void setEditMode(boolean editMode){
        this.editMode = editMode;
        InstructionsAdapter.InstructionsAdapterViewHolder holder;
        for(int i = 0; i < viewHolders.size(); i++){
            holder = viewHolders.get(i);
            holder.editMode = editMode;
            if(editMode){
                holder.instructionTextView.setVisibility(View.INVISIBLE);
                holder.instructionEditText.setVisibility(View.VISIBLE);
            }else{
                holder.instructionTextView.setVisibility(View.VISIBLE);
                holder.instructionEditText.setVisibility(View.INVISIBLE);
            }
        }
    }

    public static class InstructionsAdapterViewHolder extends RecyclerView.ViewHolder{
        public TextView instructionTextView, instructionOrderTextView;
        public EditText instructionEditText;
        public String instructionText;
        public boolean editMode = false;
        private Context context;

        public InstructionsAdapterViewHolder(View itemView){
            super(itemView);
            instructionTextView = itemView.findViewById(R.id.instructionTextView);
            instructionEditText = itemView.findViewById(R.id.instructionEditText);
            instructionOrderTextView = itemView.findViewById(R.id.instructionOrderTextView);
        }
    }

    /**
     * default constructor
     * @param inList ArrayList of Instructions
     */
    public InstructionsAdapter(ArrayList<Instruction> inList){
        list = inList;
    }

    @Override
    public InstructionsAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.instruction_list_item, parent, false);
        InstructionsAdapterViewHolder viewHolder = new InstructionsAdapterViewHolder(v);
        viewHolder.context = parent.getContext();
        return viewHolder;
    }

    /**
     * sets values of holder's views to match items in the ArrayList
     * @param holder current ViewHolder
     * @param position ViewHolder's index in the ArrayList
     */
    @Override
    public void onBindViewHolder(@NonNull InstructionsAdapterViewHolder holder, int position) {
        holder.editMode = editMode;
        Instruction currentItem = list.get(position);
        holder.instructionText = currentItem.getText();
        holder.instructionOrderTextView.setText((holder.getAdapterPosition()+1) + ". ");
        holder.instructionTextView.setText(holder.instructionText);
        holder.instructionEditText.setHint(holder.instructionText);
        holder.instructionEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(holder.editMode){
                    holder.instructionText = String.valueOf(s);
                    holder.instructionTextView.setText(holder.instructionText);
                    list.get(holder.getAdapterPosition()).setText(holder.instructionText);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        holder.instructionEditText.setOnKeyListener((v, keyCode, event) -> {
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
            holder.instructionTextView.setVisibility(View.GONE);
            holder.instructionEditText.setVisibility(View.VISIBLE);
        }else{
            holder.instructionTextView.setVisibility(View.VISIBLE);
            holder.instructionEditText.setVisibility(View.GONE);
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

        InstructionsAdapter.InstructionsAdapterViewHolder holder;

        for(int i = 0; i < viewHolders.size(); i++){
            holder = viewHolders.get(i);
            ret += holder.instructionText;
            if(i < viewHolders.size()-1){
                ret += ", ";
            }
        }

        ret += " ]\n";

        return ret;
    }
}