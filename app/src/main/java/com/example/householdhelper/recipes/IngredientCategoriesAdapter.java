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
import android.widget.EditText;
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

import java.net.InterfaceAddress;
import java.util.ArrayList;
import java.util.Collections;

public class IngredientCategoriesAdapter extends RecyclerView.Adapter<IngredientCategoriesAdapter.IngredientsAdapterViewHolder> {
    private final ArrayList<IngredientCategory> categoryList;
    private final ArrayList<IngredientsAdapterViewHolder> viewHolders = new ArrayList<>();
    public boolean editMode;
    public DeleteListener listener;

    public static final String TAG = "IngredntCategoryAdapter";

    interface DeleteListener{
        void deleteSwipe(String id);
    }

    public void setEditMode(boolean editMode){
        this.editMode = editMode;
        IngredientsAdapterViewHolder holder;
        for(int i = 0; i < viewHolders.size(); i++){
            holder = viewHolders.get(i);
            holder.editMode = editMode;
            ((IngredientsAdapter)holder.recyclerView.getAdapter()).setEditMode(editMode);
            if(editMode){
                holder.categoryNameTextView.setVisibility(View.INVISIBLE);
                holder.categoryNameEditText.setVisibility(View.VISIBLE);
                holder.addIngredientButton.setVisibility(View.VISIBLE);
            }else{
                holder.categoryNameTextView.setVisibility(View.VISIBLE);
                holder.categoryNameEditText.setVisibility(View.INVISIBLE);
                holder.addIngredientButton.setVisibility(View.GONE);
            }
        }
    }

    public static class IngredientsAdapterViewHolder extends RecyclerView.ViewHolder{
        public TextView categoryNameTextView;
        public TextView categoryNameEditText;
        public RecyclerView recyclerView;
        public RecyclerView.LayoutManager ingredientsLayoutManager;
        public String categoryName;
        private Context context;
        private ArrayList<Ingredient> ingredientsList;
        public Button addIngredientButton;
        public boolean editMode = false;

        public IngredientsAdapterViewHolder(View itemView){
            super(itemView);
            categoryNameTextView = itemView.findViewById(R.id.categoryNameTextView);
            categoryNameEditText = itemView.findViewById(R.id.categoryNameEditText);
            recyclerView = itemView.findViewById(R.id.ingredientsRecyclerView);
            addIngredientButton = itemView.findViewById(R.id.addIngredientButton);
        }
    }

    public IngredientCategoriesAdapter(ArrayList<IngredientCategory> categoryList, boolean editMode, DeleteListener listener){
        this.categoryList = categoryList;
        this.editMode = editMode;
        this.listener = listener;

        if(this.categoryList.size() < 1){
            this.categoryList.add(new IngredientCategory("-1", "Ingredients", new ArrayList<>()));
        }
    }

    @Override
    public IngredientsAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.ingredients_category_item, parent, false);
        IngredientsAdapterViewHolder viewHolder = new IngredientsAdapterViewHolder(v);
        viewHolder.context = parent.getContext();
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull IngredientsAdapterViewHolder holder, int position) {
        holder.editMode = editMode;
        holder.categoryName = categoryList.get(position).getName();
        holder.categoryNameEditText.setText(holder.categoryName);
        holder.categoryNameTextView.setText(holder.categoryName);

        holder.ingredientsList = categoryList.get(position).getIngredientList();

        IngredientsAdapter adapter = new IngredientsAdapter(holder.ingredientsList);

        adapter.editMode = editMode;

        holder.itemView.setOnLongClickListener(v -> {

            if(editMode){
                Bundle bundle = new Bundle();
                bundle.putString("itemName", categoryList.get(holder.getAdapterPosition()).getName());
                bundle.putInt("itemPosition", holder.getAdapterPosition());

                DeleteItemDialog dialog = new DeleteItemDialog();
                dialog.setArguments(bundle);
                dialog.setListener(position1 -> {

                    if(holder.categoryNameEditText.hasFocus()){
                        holder.categoryNameEditText.clearFocus();
                        InputMethodManager imm = (InputMethodManager) holder.context.getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(holder.categoryNameEditText.getWindowToken(), 0);
                    }

                    DatabaseHelper db = new DatabaseHelper(holder.context);

                    for(int i = 0; i < categoryList.get(position1).getIngredientList().size(); i++){
                        db.deleteIngredientById(categoryList.get(position1).getIngredientList().get(i).getId());
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

        holder.ingredientsLayoutManager = new LinearLayoutManager(holder.context) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        holder.recyclerView.setLayoutManager(holder.ingredientsLayoutManager);

        ItemTouchHelper helper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP | ItemTouchHelper.DOWN, 0) {

            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                if(holder.editMode){
                    int position_initial = viewHolder.getAdapterPosition();
                    int position_target = target.getAdapterPosition();

                    Collections.swap(holder.ingredientsList, position_initial, position_target);

                    recyclerView.getAdapter().notifyItemMoved(position_initial, position_target);
                    return true;
                }
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                if(holder.editMode){
                    int position = viewHolder.getAdapterPosition();
                    listener.deleteSwipe(holder.ingredientsList.get(position).getId());
                    holder.ingredientsList.remove(position);
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

        holder.addIngredientButton.setOnClickListener(v->{
            if(holder.editMode){
                Ingredient temp = new Ingredient("-1", "new ingredient", holder.recyclerView.getAdapter().getItemCount()-1, new ArrayList<>());
                holder.ingredientsList.add(temp);
                holder.recyclerView.getAdapter().notifyItemInserted(holder.ingredientsList.size()-1);
            }
            for(int i = 0; i < holder.ingredientsList.size(); i++){
                String ret = "[ ";
                ret += holder.ingredientsList.get(i) + ", ";
                ret += " ]";
            }
        });

        if(editMode){
            holder.categoryNameTextView.setVisibility(View.INVISIBLE);
            holder.categoryNameEditText.setVisibility(View.VISIBLE);
            holder.addIngredientButton.setVisibility(View.VISIBLE);
        }else{
            holder.categoryNameTextView.setVisibility(View.VISIBLE);
            holder.categoryNameEditText.setVisibility(View.INVISIBLE);
            holder.addIngredientButton.setVisibility(View.GONE);
        }

        viewHolders.add(holder);
    }

    @Override
    public int getItemCount() {
        return categoryList.size();
    }

    @Override
    public String toString(){
        String ret = "\n";

        IngredientsAdapterViewHolder holder;
        IngredientsAdapter adapter;

        for(int i = 0; i < viewHolders.size(); i++){
            holder = viewHolders.get(i);
            adapter = (IngredientsAdapter)holder.recyclerView.getAdapter();
            ret += holder.categoryName + ": " + adapter;
        }

        return ret;
    }
}