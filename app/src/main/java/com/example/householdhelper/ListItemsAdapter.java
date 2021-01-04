package com.example.householdhelper;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ListItemsAdapter extends RecyclerView.Adapter<ListItemsAdapter.ListsAdapterViewHolder> {
    private ArrayList<ListItem> list;
    public static final String TAG = "ListItemsAdapter";

    public static class ListsAdapterViewHolder extends RecyclerView.ViewHolder{
        public TextView listName;
        public CheckBox checkBox;

        public ListsAdapterViewHolder(View itemView){
            super(itemView);
            checkBox = itemView.findViewById(R.id.checkBox);
            listName = itemView.findViewById(R.id.listItemName);
        }
    }

    public ListItemsAdapter(ArrayList<ListItem> inList){
        list = inList;
    }

    @Override
    public ListsAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
        ListsAdapterViewHolder viewHolder = new ListsAdapterViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ListsAdapterViewHolder holder, int position) {
        ListItem currentItem = list.get(position);
        holder.listName.setText(currentItem.getName());
        if(currentItem.isChecked()){
            holder.checkBox.setChecked(true);
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}