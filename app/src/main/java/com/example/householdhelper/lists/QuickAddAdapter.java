package com.example.householdhelper.lists;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.householdhelper.R;
import com.example.householdhelper.helpers.DatabaseHelper;

import java.util.ArrayList;

/**
 * Extended RecyclerView Adapter for displaying quick add items
 *
 * @author Bryan Burdick
 * @version 1.0
 * @since 2021-02-06
 */
public class QuickAddAdapter extends RecyclerView.Adapter<QuickAddAdapter.QuickAddAdapterViewHolder> {
    private final ArrayList<QuickAddItem> list;
    public OnItemClickListener listener;
    public String listId;
    public Cursor listItems;
    private final Context context;

    private final DatabaseHelper db;

    public interface OnItemClickListener{
        void onItemLongClick(int position);
    }

    /**
     * sets onclicklistener
     * @param listener the new listener
     */
    public void setOnItemClickListener(OnItemClickListener listener){
        this.listener = listener;
    }

    public static class QuickAddAdapterViewHolder extends RecyclerView.ViewHolder{
        public TextView listNameTextView;
        public CheckBox checkBox;

        public QuickAddAdapterViewHolder(View itemView){
            super(itemView);

            listNameTextView = itemView.findViewById(R.id.listNameTextView);
            checkBox = itemView.findViewById(R.id.checkBox);
        }
    }

    /**
     * default constructor
     * @param list an ArrayList of QuickAddItems
     * @param listId the id of the shopping list to add to
     * @param context activity context for instantiating database
     */
    public QuickAddAdapter(ArrayList<QuickAddItem> list, String listId, Context context){
        this.list = list;
        this.listId = listId;
        this.context = context;
        db = new DatabaseHelper(context);
        listItems = db.getListItems(listId);
    }

    @Override
    public QuickAddAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
        QuickAddAdapterViewHolder viewHolder = new QuickAddAdapterViewHolder(v);
        return viewHolder;
    }

    /**
     * sets values of holder's views to match items in the ArrayList
     * @param holder current ViewHolder
     * @param position ViewHolder's index in the ArrayList
     */
    @Override
    public void onBindViewHolder(@NonNull QuickAddAdapterViewHolder holder, int position) {
        QuickAddItem currentItem = list.get(position);
        holder.listNameTextView.setText(currentItem.getName());

        String itemName = holder.listNameTextView.getText().toString();

        holder.checkBox.setOnCheckedChangeListener(null);

        Drawable drawable = context.getResources().getDrawable(R.drawable.ic_baseline_double_arrow_24);

        holder.checkBox.setButtonDrawable(drawable);

        if(listItems.moveToFirst()){
            do{
                if(listItems.getString(2).equals(itemName)){
                    holder.checkBox.setChecked(true);
                }
            }while(!holder.checkBox.isChecked() && listItems.moveToNext());
        }

        holder.checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if(isChecked){
                db.insertListItem(listId, itemName, "-1");
            }else{
                db.deleteListItemByName(listId, itemName);
            }
        });

        holder.itemView.setOnLongClickListener(v->{
            listener.onItemLongClick(position);
            return true;
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