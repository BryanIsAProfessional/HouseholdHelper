package com.example.householdhelper.schedule;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.householdhelper.R;
import com.example.householdhelper.helpers.DatabaseHelper;
import com.example.householdhelper.lists.DeleteItemDialog;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Extended RecyclerView Adapter for displaying medicine trackers
 *
 * @author Bryan Burdick
 * @version 1.0
 * @since 2021-02-06
 */
public class MedicineListAdapter extends RecyclerView.Adapter<MedicineListAdapter.MedicineAdapterViewHolder> {
    private final ArrayList<Medicine> list;
    private final ArrayList<MedicineAdapterViewHolder> viewHolders = new ArrayList<>();

    public static class MedicineAdapterViewHolder extends RecyclerView.ViewHolder{

        private Context context;
        private final TextView medicineNameTextView;
        private final RecyclerView recyclerView;
        private MedicineAdapter adapter;
        private RecyclerView.LayoutManager layoutManager;
        private final Button refillButton;
        private final ImageButton removePillButton;
        private final ImageButton addPillButton;
        private final ToggleButton autoToggle;

        public MedicineAdapterViewHolder(View itemView){
            super(itemView);
            recyclerView = itemView.findViewById(R.id.pillsRecyclerView);
            removePillButton = itemView.findViewById(R.id.removePillImageButton);
            refillButton = itemView.findViewById(R.id.refillButton);
            medicineNameTextView = itemView.findViewById(R.id.medicineNameTextView);
            addPillButton = itemView.findViewById(R.id.addPillImageButton);
            autoToggle = itemView.findViewById(R.id.autoToggle);
        }
    }

    /**
     * default constructor
     * @param inList ArrayList of Medicines
     */
    public MedicineListAdapter(ArrayList<Medicine> inList){
        list = inList;

    }

    @Override
    public MedicineAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.med_tracker_item, parent, false);
        MedicineAdapterViewHolder viewHolder = new MedicineAdapterViewHolder(v);
        viewHolder.context = parent.getContext();
        return viewHolder;
    }

    /**
     * sets values of holder's views to match items in the ArrayList
     * @param holder current ViewHolder
     * @param position ViewHolder's index in the ArrayList
     */
    @Override
    public void onBindViewHolder(@NonNull MedicineAdapterViewHolder holder, int position) {

        holder.autoToggle.setOnClickListener(null);
        holder.autoToggle.setChecked(list.get(position).getAutomatic());
        holder.autoToggle.setClickable(false);
        holder.autoToggle.setOnClickListener(v -> {
            DatabaseHelper db = new DatabaseHelper(holder.context);
            db.updateMeds(list.get(holder.getAdapterPosition()).getId(), holder.autoToggle.isChecked());
            list.get(holder.getAdapterPosition()).setAutomatic(holder.autoToggle.isChecked());
            holder.adapter.handleScheduleNotification();
            db.close();
        });

        holder.refillButton.setOnClickListener(v -> {
            DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            Date date = new Date();

            DatabaseHelper db = new DatabaseHelper(holder.context);
            db.updateMeds(list.get(holder.getAdapterPosition()).getId(), list.get(holder.getAdapterPosition()).getTotal(), dateFormat.format(date));
            list.get(holder.getAdapterPosition()).setRemaining(list.get(holder.getAdapterPosition()).getTotal());
            notifyItemChanged(holder.getAdapterPosition());
            db.close();

            holder.refillButton.setVisibility(View.GONE);
        });

        holder.itemView.setOnLongClickListener(v -> {

            Bundle bundle = new Bundle();
            bundle.putString("itemName", list.get(holder.getAdapterPosition()).getName());
            bundle.putInt("itemPosition", holder.getAdapterPosition());

            DeleteItemDialog dialog = new DeleteItemDialog();
            dialog.setArguments(bundle);
            dialog.setListener(position1 -> {
                DatabaseHelper db = new DatabaseHelper(holder.context);
                db.deleteMedById(list.get(holder.getAdapterPosition()).getId());
                list.remove(holder.getAdapterPosition());
                notifyItemRemoved(holder.getAdapterPosition());
                db.close();
            });
            dialog.show(((FragmentActivity)holder.context).getSupportFragmentManager(), "Stop tracking this medication?");
            return true;
        });

        holder.removePillButton.setOnClickListener(v->{
            holder.adapter.usePill();
            if(list.get(holder.getAdapterPosition()).getRemaining() < 1){
                holder.refillButton.setVisibility(View.VISIBLE);
            }
        });

        holder.addPillButton.setOnClickListener(v->{
            holder.adapter.restorePill();
            if(list.get(holder.getAdapterPosition()).getRemaining() > 0){
                holder.refillButton.setVisibility(View.GONE);
            }
        });

        if(list.get(holder.getAdapterPosition()).getRemaining() < 1){
            holder.refillButton.setVisibility(View.VISIBLE);
        }else{
            holder.refillButton.setVisibility(View.GONE);
        }


        holder.medicineNameTextView.setText(list.get(position).getName());

        holder.adapter = new MedicineAdapter(holder.context, list.get(position));
        holder.layoutManager = new GridLayoutManager(holder.context, 7, RecyclerView.VERTICAL, false);
        holder.recyclerView.setLayoutManager(holder.layoutManager);
        holder.recyclerView.setAdapter(holder.adapter);

        viewHolders.add(holder);
    }

    /**
     * returns the size of the ArrayList
     * @return size of the ArrayList
     */
    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public String toString(){
        String ret = "[ ";

        MedicineAdapterViewHolder holder;

        for(int i = 0; i < viewHolders.size(); i++){
            holder = viewHolders.get(i);
            ret += list.get(holder.getAdapterPosition()).toString();
            if(i < viewHolders.size()-1){
                ret += ", ";
            }
        }

        ret += " ]\n";

        return ret;
    }
}