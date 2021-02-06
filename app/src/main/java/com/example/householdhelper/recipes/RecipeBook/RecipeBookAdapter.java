package com.example.householdhelper.recipes.RecipeBook;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.householdhelper.R;
import com.example.householdhelper.recipes.Recipe;

import java.util.ArrayList;
import java.util.List;

/**
 * Extended RecyclerView Adapter for displaying recipe titles
 *
 * @author Bryan Burdick
 * @version 1.0
 * @since 2021-02-06
 */
public class RecipeBookAdapter extends RecyclerView.Adapter<RecipeBookAdapter.RecipesAdapterViewHolder> implements Filterable {
    private final ArrayList<Recipe> list;
    private final ArrayList<Recipe> listFull;

    public OnItemClickListener listener;

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

    /**
     * sets onclicklistener
     * @param listener the new listener
     */
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

    /**
     * default constructor
     * @param inList an ArrayList of Recipe
     */
    public RecipeBookAdapter(ArrayList<Recipe> inList){
        list = inList;
        listFull = new ArrayList<>(list);
    }

    @Override
    public RecipesAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recipe_book_item, parent, false);
        RecipesAdapterViewHolder viewHolder = new RecipesAdapterViewHolder(v, listener);
        return viewHolder;
    }

    /**
     * sets values of holder's views to match items in the ArrayList
     * @param holder current ViewHolder
     * @param position ViewHolder's index in the ArrayList
     */
    @Override
    public void onBindViewHolder(@NonNull RecipesAdapterViewHolder holder, int position) {
        Recipe currentItem = list.get(position);
        holder.recipeName.setText(currentItem.getName());
    }

    /**
     * returns the size of the ArrayList
     * @return size of the ArrayList
     */
    @Override
    public int getItemCount() {
        return list.size();
    }
}