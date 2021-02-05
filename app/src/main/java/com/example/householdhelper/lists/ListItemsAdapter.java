package com.example.householdhelper.lists;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.RotateDrawable;
import android.util.Log;
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

public class ListItemsAdapter extends RecyclerView.Adapter<ListItemsAdapter.ListsAdapterViewHolder> {
    private final ArrayList<ListItem> list;
    public ListItemsAdapter.OnItemClickListener listener;
    public static final String TAG = "ListItemsAdapter";


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

    public void setOnItemClickListener(ListItemsAdapter.OnItemClickListener listener){
        this.listener = listener;
    }

    public ListItemsAdapter(ArrayList<ListItem> inList){

        list = inList;
        Collections.sort(list);
    }

    public ListItem getItem(int position){
        return list.get(position);
    }

    @Override
    public ListsAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
        ListsAdapterViewHolder viewHolder = new ListsAdapterViewHolder(v);
        return viewHolder;
    }

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

    @Override
    public int getItemCount() {
        return list.size();
    }
    
    
}