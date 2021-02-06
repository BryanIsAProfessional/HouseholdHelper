package com.example.householdhelper.recipes;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.householdhelper.R;
import com.example.householdhelper.helpers.DatabaseHelper;
import com.example.householdhelper.lists.DeleteItemDialog;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Extended RecyclerView Adapter for displaying groups of instructions
 *
 * @author Bryan Burdick
 * @version 1.0
 * @since 2021-02-06
 */
public class InstructionCategoriesAdapter extends RecyclerView.Adapter<InstructionCategoriesAdapter.InstructionsAdapterViewHolder> {
    private ArrayList<InstructionCategory> categoryList;
    private final ArrayList<InstructionsAdapterViewHolder> viewHolders = new ArrayList<>();
    public boolean editMode;
    public DeleteListener listener;


    interface DeleteListener{
        void deleteSwipe(String id);
    }

    /**
     * Toggles whether text should be editable or not
     * @param editMode the new state for text editability
     */
    public void setEditMode(boolean editMode){
        this.editMode = editMode;
        InstructionsAdapterViewHolder holder;
        for(int i = 0; i < viewHolders.size(); i++){
            holder = viewHolders.get(i);
            holder.editMode = editMode;
            ((InstructionsAdapter)holder.recyclerView.getAdapter()).setEditMode(editMode);
            if(editMode){
                holder.categoryNameTextView.setVisibility(View.INVISIBLE);
                holder.categoryNameEditText.setVisibility(View.VISIBLE);
                holder.addInstructionButton.setVisibility(View.VISIBLE);
            }else{
                holder.categoryNameTextView.setVisibility(View.VISIBLE);
                holder.categoryNameEditText.setVisibility(View.INVISIBLE);
                holder.addInstructionButton.setVisibility(View.GONE);
            }
        }
    }

    public static class InstructionsAdapterViewHolder extends RecyclerView.ViewHolder{
        public TextView categoryNameTextView;
        public TextView categoryNameEditText;
        public RecyclerView recyclerView;
        public RecyclerView.LayoutManager instructionsLayoutManager;
        public String categoryName;
        private Context context;
        private ArrayList<Instruction> instructionsList;
        public Button addInstructionButton;
        public boolean editMode = false;

        public InstructionsAdapterViewHolder(View itemView){
            super(itemView);

            categoryNameTextView = itemView.findViewById(R.id.categoryNameTextView);
            categoryNameEditText = itemView.findViewById(R.id.categoryNameEditText);
            recyclerView = itemView.findViewById(R.id.instructionsRecyclerView);
            addInstructionButton = itemView.findViewById(R.id.addInstructionButton);
        }
    }

    /**
     * default constructor
     * @param categoryList ArrayList of InstructionCategory
     * @param editMode current state of editMode
     * @param listener onClickListener
     */
    public InstructionCategoriesAdapter(ArrayList<InstructionCategory> categoryList, boolean editMode, DeleteListener listener){
        this.categoryList = categoryList;
        this.editMode = editMode;
        this.listener = listener;

        this.categoryList = categoryList;
        if(this.categoryList.size() < 1){
            this.categoryList.add(new InstructionCategory("-1", "Instructions", new ArrayList<>()));
        }
    }

    @Override
    public InstructionsAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.instructions_category_item, parent, false);
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
        holder.categoryName = categoryList.get(position).getName();
        holder.categoryNameEditText.setText(holder.categoryName);
        holder.categoryNameTextView.setText(holder.categoryName);

        holder.instructionsList = categoryList.get(position).getInstructionList();

        InstructionsAdapter adapter = new InstructionsAdapter(holder.instructionsList);

        adapter.editMode = editMode;

        holder.itemView.setOnLongClickListener(v -> {

            if(editMode){
                holder.categoryNameEditText.requestFocus();
                holder.categoryNameEditText.clearFocus();

                Bundle bundle = new Bundle();
                bundle.putString("itemName", categoryList.get(holder.getAdapterPosition()).getName());
                bundle.putInt("itemPosition", holder.getAdapterPosition());

                DeleteItemDialog dialog = new DeleteItemDialog();
                dialog.setArguments(bundle);
                dialog.setListener(position1 -> {
                    DatabaseHelper db = new DatabaseHelper(holder.context);

                    for(int i = 0; i < categoryList.get(position1).getInstructionList().size(); i++){
                        db.deleteInstructionById(categoryList.get(position1).getInstructionList().get(i).getId());
                    }
                    categoryList.remove(position1);
                    notifyItemRemoved(position1);
                    db.close();
                });
                dialog.show(((AppCompatActivity)holder.context).getSupportFragmentManager(), "Delete this Category?");
                return true;
            }
            return false;
        });

        holder.categoryNameEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(holder.editMode){
                    holder.categoryName = String.valueOf(s);
                    holder.categoryNameTextView.setText(holder.categoryName);
                    categoryList.get(holder.getAdapterPosition()).setName(holder.categoryName);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        holder.categoryNameEditText.setOnKeyListener((v, keyCode, event) -> {
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

        holder.recyclerView.setAdapter(adapter);

        holder.instructionsLayoutManager = new LinearLayoutManager(holder.context) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        holder.recyclerView.setLayoutManager(holder.instructionsLayoutManager);

        ItemTouchHelper helper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP | ItemTouchHelper.DOWN, 0) {

            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                if(holder.editMode){
                    int position_initial = viewHolder.getAdapterPosition();
                    int position_target = target.getAdapterPosition();

                    Collections.swap(holder.instructionsList, position_initial, position_target);

                    recyclerView.getAdapter().notifyItemMoved(position_initial, position_target);
                    return true;
                }
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                if(holder.editMode){
                    int position = viewHolder.getAdapterPosition();
                    listener.deleteSwipe(holder.instructionsList.get(position).getId());
                    holder.instructionsList.remove(position);
                    holder.recyclerView.getAdapter().notifyItemRemoved(position);
                }
            }

            @Override
            public boolean isLongPressDragEnabled() {
                return holder.editMode;
            }

            @Override
            public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
                int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
                int swipeFlags;
                if(holder.editMode){
                    swipeFlags = ItemTouchHelper.RIGHT;
                }else{
                    swipeFlags = 0;
                }

                return makeMovementFlags(dragFlags, swipeFlags);
            }
        });

        helper.attachToRecyclerView(holder.recyclerView);

        holder.addInstructionButton.setOnClickListener(v->{
            if(holder.editMode){
                Instruction temp = new Instruction("-1", "new instruction", holder.recyclerView.getAdapter().getItemCount()-1);
                holder.instructionsList.add(temp);
                holder.recyclerView.getAdapter().notifyItemInserted(holder.instructionsList.size()-1);
            }
        });

        if(editMode){
            holder.categoryNameTextView.setVisibility(View.INVISIBLE);
            holder.categoryNameEditText.setVisibility(View.VISIBLE);
            holder.addInstructionButton.setVisibility(View.VISIBLE);
        }else{
            holder.categoryNameTextView.setVisibility(View.VISIBLE);
            holder.categoryNameEditText.setVisibility(View.INVISIBLE);
            holder.addInstructionButton.setVisibility(View.GONE);
        }

        viewHolders.add(holder);
    }

    /**
     * returns the size of the ArrayList
     * @return size of the ArrayList
     */
    @Override
    public int getItemCount() {
        return categoryList.size();
    }

    @Override
    public String toString(){
        String ret = "\n";

        InstructionsAdapterViewHolder holder;
        InstructionsAdapter adapter;

        for(int i = 0; i < viewHolders.size(); i++){
            holder = viewHolders.get(i);
            adapter = (InstructionsAdapter) holder.recyclerView.getAdapter();
            ret += holder.categoryName + ": " + adapter;
        }

        return ret;
    }
}