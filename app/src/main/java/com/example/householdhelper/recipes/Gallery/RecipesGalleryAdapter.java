package com.example.householdhelper.recipes.Gallery;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.householdhelper.R;
import com.example.householdhelper.helpers.BitmapResolver;
import com.example.householdhelper.recipes.Recipe;

import java.util.ArrayList;
import java.util.List;

/**
 * Extended RecyclerView Adapter for displaying recipe titles with an image
 *
 * @author Bryan Burdick
 * @version 1.0
 * @since 2021-02-06
 */
public class RecipesGalleryAdapter extends RecyclerView.Adapter<RecipesGalleryAdapter.RecipesAdapterViewHolder> implements Filterable {
    private final ArrayList<Recipe> list;
    public OnItemClickListener listener;
    private final Context context;
    private final ArrayList<Recipe> listFull;

    @Override
    public Filter getFilter() {
        return recipeFilter;
    }

    private final Filter recipeFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<Recipe> filteredList = new ArrayList<>();

            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(listFull);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();

                for (Recipe recipe : listFull) {
                    if (recipe.getName().toLowerCase().contains(filterPattern)) {
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
        public TextView recipeName, recipeAuthor, recipeServings, cookTime;
        public ImageView recipeImage;
        public ConstraintLayout galleryItemContainer;

        public RecipesAdapterViewHolder(View itemView, OnItemClickListener listener){
            super(itemView);

            cookTime = itemView.findViewById(R.id.galleryCookTimeTextView);
            recipeName = itemView.findViewById(R.id.listNameTextView);
            recipeAuthor = itemView.findViewById(R.id.galleryAuthorTextView);
            recipeServings = itemView.findViewById(R.id.galleryServingsTextView);
            recipeImage = itemView.findViewById(R.id.recipeImageView);
            galleryItemContainer = itemView.findViewById(R.id.galleryItemContainer);
            itemView.setOnClickListener(v->{
                listener.onItemClick(getAdapterPosition());
            });
        }
    }

    /**
     * default constructor
     * @param inList an ArrayList of Recipes
     */
    public RecipesGalleryAdapter(ArrayList<Recipe> inList, Context context){
        list = inList;
        this.context = context;
        listFull = new ArrayList<>(list);
    }

    /**
     * Rotates a bitmap to a given orientation
     * @param bitmap the bitmap to rotate
     * @param degrees degrees to rotate bitmap
     * @return the rotated bitmap
     */
    public static Bitmap rotateBitmap(Bitmap bitmap, int degrees) {
        Matrix matrix = new Matrix();
        matrix.postRotate(degrees);
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }

    @Override
    public RecipesAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recipe_gallery_item, parent, false);
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
        if(currentItem.getUri() != null){
            try{
                Bitmap bitmap = rotateBitmap(BitmapResolver.getBitmap(context.getContentResolver(), currentItem.getUri()), currentItem.getImageRotation());
                holder.recipeImage.setImageBitmap(bitmap);

            }catch(Exception e){
            }
        }

        holder.recipeName.setText(currentItem.getName());
        if(!currentItem.getAuthor().equals("")){
            holder.recipeAuthor.setText("by " + currentItem.getAuthor());
        }
        holder.recipeServings.setText("Serves " + currentItem.getServings());
        holder.cookTime.setText(" in " + currentItem.getCookTimeInMinutes() +  " Minutes");
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