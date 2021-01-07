package com.example.householdhelper;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ListsAdapter extends RecyclerView.Adapter<ListsAdapter.ListsAdapterViewHolder> {
    private ArrayList<List> list;
    public OnItemClickListener listener;
    public static final String TAG = "ListsAdapter";

    public interface OnItemClickListener{
        void onItemClick(int position);

        void onDeleteClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        this.listener = listener;
    }

    public static class ListsAdapterViewHolder extends RecyclerView.ViewHolder{
        public TextView listName;
        public ImageButton deleteButton;

        public ListsAdapterViewHolder(View itemView, OnItemClickListener listener){
            super(itemView);

            listName = itemView.findViewById(R.id.quickAddItemName);
            deleteButton = itemView.findViewById(R.id.deleteButton);
            itemView.setOnClickListener(v->listener.onItemClick(getAdapterPosition()));
            deleteButton.setOnClickListener(v-> {
                Log.d(TAG, "ListsAdapterViewHolder: delete button clicked (ListsAdapterViewHolder)");
                listener.onDeleteClick(getAdapterPosition());
            });
        }
    }

    public ListsAdapter(ArrayList<List> inList){
        list = inList;
    }

    @Override
    public ListsAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.lists_item, parent, false);
        ListsAdapterViewHolder viewHolder = new ListsAdapterViewHolder(v, listener);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ListsAdapterViewHolder holder, int position) {
        List currentItem = list.get(position);
        holder.listName.setText(currentItem.getName());
        holder.deleteButton.setOnClickListener(v-> {
            Log.d(TAG, "ListsAdapterViewHolder: delete button clicked (onBindViewHolder)");
            listener.onDeleteClick(holder.getAdapterPosition());
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}