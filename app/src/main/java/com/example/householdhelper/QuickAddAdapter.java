package com.example.householdhelper;

import android.database.Cursor;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class QuickAddAdapter extends RecyclerView.Adapter<QuickAddAdapter.QuickAddAdapterViewHolder> {
    private ArrayList<QuickAddItem> list;
    public OnItemClickListener listener;
    public static final String TAG = "QuickAddAdapter";
    public String listId;
    public Cursor listItems;

    private DatabaseHelper db;

    public interface OnItemClickListener{
        void onItemClick(int position);
        void onItemLongClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        this.listener = listener;
    }

    public static class QuickAddAdapterViewHolder extends RecyclerView.ViewHolder{
        public TextView itemNameTextView;

        public QuickAddAdapterViewHolder(View itemView, OnItemClickListener listener, ArrayList<QuickAddItem> list){
            super(itemView);

            itemNameTextView = itemView.findViewById(R.id.quickAddItemName);
            itemNameTextView.setTextColor(Color.BLACK);
        }
    }

    public QuickAddAdapter(ArrayList<QuickAddItem> list, String listId){
        this.list = list;
        this.listId = listId;
    }

    @Override
    public QuickAddAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.quickadd_lists_item, parent, false);
        QuickAddAdapterViewHolder viewHolder = new QuickAddAdapterViewHolder(v, listener, list);
        db = new DatabaseHelper(parent.getContext());
        listItems = db.getListItems(listId);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull QuickAddAdapterViewHolder holder, int position) {
        QuickAddItem currentItem = list.get(position);
        holder.itemNameTextView.setText(currentItem.getName());

        String itemName = holder.itemNameTextView.getText().toString();

        boolean isInList = false;

        if(listItems.moveToFirst()){
            do{
                Log.d(TAG, "QuickAddAdapterViewHolder: checking " + itemName + " against " + listItems.getString(2));
                if(listItems.getString(2).equals(itemName)){
                    Log.d(TAG, "QuickAddAdapterViewHolder: " + itemName + " found in list");
                    holder.itemNameTextView.setTextColor(Color.LTGRAY);
                    isInList = true;
                }
            }while(!isInList && listItems.moveToNext());
        }

        if(!isInList){
            Log.d(TAG, "QuickAddAdapterViewHolder: " + itemName + " is not in the list");
            holder.itemView.setOnClickListener(v->{
                listener.onItemClick(holder.getAdapterPosition());
                holder.itemNameTextView.setTextColor(Color.LTGRAY);
            });
        }else{
            Log.d(TAG, "QuickAddAdapterViewHolder: " + itemName + " is in the list");
            holder.itemView.setOnClickListener(v->{
                Toast.makeText(holder.itemView.getContext(), "Item is already in your list!", Toast.LENGTH_SHORT).show();
            });
        }

        holder.itemView.setOnLongClickListener(v->{
            listener.onItemLongClick(holder.getAdapterPosition());
            return true;
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}