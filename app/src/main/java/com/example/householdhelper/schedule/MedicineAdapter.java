package com.example.householdhelper.schedule;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.householdhelper.R;
import com.example.householdhelper.helpers.DatabaseHelper;
import com.example.householdhelper.recipes.Measurement;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Extended RecyclerView Adapter for displaying medicines
 *
 * @author Bryan Burdick
 * @version 1.0
 * @since 2021-02-06
 */
public class MedicineAdapter extends RecyclerView.Adapter<MedicineAdapter.MedicineAdapterViewHolder> {
    private final ArrayList<Boolean> list;
    private final ArrayList<MedicineAdapterViewHolder> viewHolders = new ArrayList<>();
    public static final int MILLIS_PER_DAY = 86400000;
    private final Context context;
    private final DatabaseHelper db;
    private final Medicine medicine;


    public static class MedicineAdapterViewHolder extends RecyclerView.ViewHolder{

        private final ImageView imageView;
        private boolean isFilled;

        public MedicineAdapterViewHolder(View itemView){
            super(itemView);
            imageView = itemView.findViewById(R.id.medicineImageView);
        }
    }

    /**
     * default constructor
     * @param context application context for accessing database
     * @param medicine Medicine object
     */
    public MedicineAdapter(Context context, Medicine medicine){
        this.context = context;
        this.medicine = medicine;
        db = new DatabaseHelper(context);
        list = new ArrayList<>();
        for(int i = 0; i < this.medicine.getRemaining(); i++){
            list.add(true);
        }
        for(int i = 0; i < this.medicine.getTotal()-this.medicine.getRemaining(); i++){
            list.add(false);
        }
    }

    /**
     * restores a dose to current prescription
     */
    public void restorePill(){
        boolean isRestored = false;
        for(int i = 0; !isRestored & i < list.size(); i++){
            if(!list.get(i)){
                list.set(i, true);
                isRestored = true;
                notifyItemChanged(i);
                this.medicine.setRemaining(i+1);
            }
        }

        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date();

        if(db.updateMeds(this.medicine.getId(), getRemaining(), dateFormat.format(date)).equals("-1")){
        }else{
            handleScheduleNotification();
        }
    }

    /**
     * Returns the number of doses remaining
     * @return the number of doses remaining
     */
    public int getRemaining(){
        int count = 0;
        for(int i = 0; i < list.size(); i++){
            if(list.get(i)){
                count++;
            }
        }
        return count;
    }

    /**
     * removes a dose from current prescription
     */
    public void usePill(){
        boolean isUsed = false;
        for(int i = (list.size()-1); !isUsed & i >= 0; i--){
            if(list.get(i)){
                list.set(i, false);
                isUsed = true;
                notifyItemChanged(i);
                this.medicine.setRemaining(i);
            }
        }

        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date();

        if(!db.updateMeds(this.medicine.getId(), getRemaining(), dateFormat.format(date)).equals("-1")){
            handleScheduleNotification();
        }
    }

    /**
     * gathers information to schedule a notification, if tracked automatically
     */
    public void handleScheduleNotification() {
        if(this.medicine.getAutomatic()){
            Date date = new Date();

            String[] times = this.medicine.getNotifyAt().split(":");
            int hour = Integer.valueOf(times[0]);
            int minute = Integer.valueOf(times[1]);

            int daysForward = (int)(this.medicine.getRemaining() * this.medicine.getHoursBetween() / 24.0);
            daysForward -= medicine.getDaysBefore();

            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);

            calendar.set(Calendar.HOUR_OF_DAY, hour);
            calendar.set(Calendar.MINUTE, minute);

            calendar.setTimeInMillis(calendar.getTimeInMillis() + (daysForward * MILLIS_PER_DAY));

            scheduleNotification(0, "Refill Reminder", "Remember to pick up your " + this.medicine.getName() + " refill.", calendar);
        }else{
        }
    }

    /**
     * Schedules a notification for a specific time
     * @param notificationId notification id
     * @param title notification title
     * @param message notification message
     * @param reminderTime time to schedule notification at
     */
    private void scheduleNotification(int notificationId, String title, String message, Calendar reminderTime){
        Intent intent = new Intent(context, AlarmReceiver.class);
        intent.putExtra("notificationId", notificationId);
        intent.putExtra("title", title);
        intent.putExtra("message", message);

        PendingIntent alarmIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
//        Calendar startTime = Calendar.getInstance();
//        startTime.setTime(reminderTime);
        long alarmStartTime = reminderTime.getTimeInMillis();

        alarmManager.set(AlarmManager.RTC_WAKEUP, alarmStartTime, alarmIntent);
    }

    @Override
    public MedicineAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.medicine_list_item, parent, false);
        MedicineAdapterViewHolder viewHolder = new MedicineAdapterViewHolder(v);
        return viewHolder;
    }

    /**
     * sets values of holder's views to match items in the ArrayList
     * @param holder current ViewHolder
     * @param position ViewHolder's index in the ArrayList
     */
    @Override
    public void onBindViewHolder(@NonNull MedicineAdapterViewHolder holder, int position) {
        holder.isFilled = list.get(position);

        if(holder.isFilled){
            holder.imageView.setImageAlpha(255);
        }else{
            holder.imageView.setImageAlpha(30);
        }

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
            ret += holder.isFilled;
            if(i < viewHolders.size()-1){
                ret += ", ";
            }
        }

        ret += " ]\n";

        return ret;
    }
}