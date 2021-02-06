package com.example.householdhelper.lists;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.householdhelper.R;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Extended RecyclerView Adapter for displaying shopping list items
 *
 * @author Bryan Burdick
 * @version 1.0
 * @since 2021-02-06
 */
public class ListItemsAdapter extends RecyclerView.Adapter<ListItemsAdapter.ListsAdapterViewHolder> {
    private final ArrayList<ListItem> list;
    public ListItemsAdapter.OnItemClickListener listener;

    /**
     * sorts items in the ArrayList
     */
    public void sort() {
        Collections.sort(list);
    }

    public static class ListsAdapterViewHolder extends RecyclerView.ViewHolder{
        public TextView listName;
        public CheckBox checkBox;

        public ListsAdapterViewHolder(View itemView){
            super(itemView);
            checkBox = itemView.findViewById(R.id.checkBox);
            listName = itemView.findViewById(R.id.listNameTextView);
        }
    }

    public interface OnItemClickListener{
        void onItemChecked(boolean checked, int position);
    }

    /**
     * sets onclicklistener
     * @param listener the new listener
     */
    public void setOnItemClickListener(ListItemsAdapter.OnItemClickListener listener){
        this.listener = listener;
    }

    /**
     * default constructor
     * @param inList an ArrayList of ListItems
     */
    public ListItemsAdapter(ArrayList<ListItem> inList){

        list = inList;
        Collections.sort(list);
    }

    /**
     * gets the ListItem at a position
     * @param position the index of the item in the ArrayList
     * @return the ListItem at given index
     */
    public ListItem getItem(int position){
        return list.get(position);
    }

    @Override
    public ListsAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
        ListsAdapterViewHolder viewHolder = new ListsAdapterViewHolder(v);
        return viewHolder;
    }

    /**
     * sets values of holder's views to match items in the ArrayList
     * @param holder current ViewHolder
     * @param position ViewHolder's index in the ArrayList
     */
    @Override
    public void onBindViewHolder(@NonNull ListsAdapterViewHolder holder, int position) {
        ListItem currentItem = list.get(holder.getAdapterPosition());
        holder.listName.setText(currentItem.getName());


        holder.checkBox.setOnCheckedChangeListener(null);
        holder.checkBox.setChecked(currentItem.isChecked());
        holder.checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            currentItem.setIsChecked(isChecked);
            listener.onItemChecked(isChecked, holder.getAdapterPosition());
        });
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