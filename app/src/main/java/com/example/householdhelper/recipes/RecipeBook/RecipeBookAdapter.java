package com.example.householdhelper.recipes.RecipeBook;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.householdhelper.R;
import com.example.householdhelper.recipes.Recipe;

import java.util.ArrayList;
import java.util.List;

public class RecipeBookAdapter extends RecyclerView.Adapter<RecipeBookAdapter.RecipesAdapterViewHolder> implements Filterable {
    private final ArrayList<Recipe> list;
    private final ArrayList<Recipe> listFull;

    public OnItemClickListener listener;
    private final Context context;
    public static final String TAG = "RecipeBookAdapter";

    @Override
    public Filter getFilter() {
        return recipeFilter;
    }

    private final Filter recipeFilter = new Filter(){
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<Recipe> filteredList = new ArrayList<>();

            if(constraint == null || constraint.length() == 0){
                filteredList.addAll(listFull);
            }else{
                String filterPattern = constraint.toString().toLowerCase().trim();

                for(Recipe recipe : listFull){
                    if(recipe.getName().toLowerCase().contains(filterPattern)){
                        filteredList.add(recipe);
                    }
                }
            }

            FilterResults results = new FilterResults();
            results.values = filteredList;
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            list.clear();
            list.addAll((List)results.values);
            notifyDataSetChanged();
        }
    };

    public interface OnItemClickListener{
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        this.listener = listener;
    }

    public static class RecipesAdapterViewHolder extends RecyclerView.ViewHolder{
        public TextView recipeName;

        public RecipesAdapterViewHolder(View itemView, OnItemClickListener listener){
            super(itemView);

            recipeName = itemView.findViewById(R.id.listNameTextView);
            itemView.setOnClickListener(v->{
                listener.onItemClick(getAdapterPosition());
            });
        }
    }

    public RecipeBookAdapter(ArrayList<Recipe> inList, Context context){
        list = inList;
        this.context = context;
        listFull = new ArrayList<>(list);
    }

    @Override
    public RecipesAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recipe_book_item, parent, false);
        RecipesAdapterViewHolder viewHolder = new RecipesAdapterViewHolder(v, listener);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecipesAdapterViewHolder holder, int position) {
        Recipe currentItem = list.get(position);
        holder.recipeName.setText(currentItem.getName());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}