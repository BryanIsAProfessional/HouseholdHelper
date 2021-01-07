package com.example.householdhelper;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class CrossedListItemsAdapter extends RecyclerView.Adapter<CrossedListItemsAdapter.CrossedAdapterViewHolder> {
    private ArrayList<CrossedListItem> list;
    public static final String TAG = "CrossedListItemsAdapter";

    public static class CrossedAdapterViewHolder extends RecyclerView.ViewHolder{
        public TextView listName;

        public CrossedAdapterViewHolder(View itemView){
            super(itemView);
            listName = itemView.findViewById(R.id.quickAddItemName);
        }
    }

    public CrossedListItemsAdapter(ArrayList<CrossedListItem> inList){
        list = inList;
    }

    @Override
    public CrossedAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.crossed_list_item, parent, false);
        CrossedAdapterViewHolder viewHolder = new CrossedAdapterViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CrossedAdapterViewHolder holder, int position) {
        CrossedListItem currentItem = list.get(position);
        holder.listName.setText(currentItem.getName());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}