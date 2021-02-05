package com.example.householdhelper.lists;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.example.householdhelper.R;

import java.util.ArrayList;

public class ListsAdapter extends RecyclerView.Adapter<ListsAdapter.ListsAdapterViewHolder> {
    private final ArrayList<List> list;
    public OnItemClickListener listener;
    public static final String TAG = "ListsAdapter";
    public String shoppingListId, toDoListId;
    public Context context;

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
        public ImageView shopListIndicatorImageView, todoListIndicatorImageView;
        private final ProgressBar progressBar;

        public ListsAdapterViewHolder(View itemView, OnItemClickListener listener){
            super(itemView);

            listName = itemView.findViewById(R.id.listNameTextView);
            deleteButton = itemView.findViewById(R.id.deleteButton);
            itemView.setOnClickListener(v->listener.onItemClick(getAdapterPosition()));
            deleteButton.setOnClickListener(v-> {
                listener.onDeleteClick(getAdapterPosition());
            });
            progressBar = itemView.findViewById(R.id.listCompletionBar);
            shopListIndicatorImageView = itemView.findViewById(R.id.shopListIndicatorImageView);
            todoListIndicatorImageView = itemView.findViewById(R.id.todoListIndicatorImageView);
        }
    }

    public ListsAdapter(ArrayList<List> inList, Context context){
        this.context = context;
        list = inList;
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        shoppingListId = preferences.getString(context.getString(R.string.shopping_list), "");
        toDoListId = preferences.getString(context.getString(R.string.todo_list), "");
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
            listener.onDeleteClick(holder.getAdapterPosition());
        });
        holder.progressBar.setProgress(currentItem.getCompletionPercent());


        if(list.get(holder.getAdapterPosition()).getid().equals(toDoListId)){
            holder.shopListIndicatorImageView.setVisibility(View.GONE);
        }else if(list.get(holder.getAdapterPosition()).getid().equals(shoppingListId)){
            holder.todoListIndicatorImageView.setVisibility(View.GONE);
        }else{
            holder.shopListIndicatorImageView.setVisibility(View.GONE);
            holder.todoListIndicatorImageView.setVisibility(View.GONE);
        }

    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}