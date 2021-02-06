package com.example.householdhelper.schedule;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;

import com.example.householdhelper.R;
import com.example.householdhelper.helpers.DatabaseHelper;
import com.example.householdhelper.lists.NewListDialog;

import java.sql.Time;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;

public class ScheduleActivity extends AppCompatActivity implements NewMedDialog.NewMedDialogListener {

    public static final int MILLIS_PER_DAY = 86400000;
    private DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.activity_schedule);

        db = new DatabaseHelper(this);
    }

    /**
     * schedules a notification at a specific time
     * @param notificationId notification id
     * @param title notification title
     * @param message notification message
     * @param reminderTime date that message should be scheduled
     */
    private void scheduleNotification(int notificationId, String title, String message, Calendar reminderTime){
        Intent intent = new Intent(ScheduleActivity.this, AlarmReceiver.class);
        intent.putExtra("notificationId", notificationId);
        intent.putExtra("title", title);
        intent.putExtra("message", message);

        PendingIntent alarmIntent = PendingIntent.getBroadcast(ScheduleActivity.this, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);

        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        long alarmStartTime = reminderTime.getTimeInMillis();

        alarmManager.set(AlarmManager.RTC_WAKEUP, alarmStartTime, alarmIntent);
    }

    /**
     * opens a dialog to get user data
     */
    private void openNewMedDialog() {
        NewMedDialog newMedDialog = new NewMedDialog();
        newMedDialog.show(getSupportFragmentManager(), "Track a new medication");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.schedule_options_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.addMedOption:
                openNewMedDialog();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * Chooses the theme based on the one selected in sharedpreferences, or default if none is selected
     * @return the selected theme
     */
    @Override
    public Resources.Theme getTheme(){
        Resources.Theme theme = super.getTheme();

        SharedPreferences preferences = android.preference.PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String themeName = preferences.getString("theme", "Default");

        switch(themeName){
            case "Default":
                //editor.putString();
                theme.applyStyle(R.style.Theme_HouseholdHelper, true);
                break;
            case "Beach":
                theme.applyStyle(R.style.Theme_Beach, true);
                break;
            case "Dark":
                theme.applyStyle(R.style.Theme_Dark, true);
                break;
        }
        return theme;
    }

    /**
     * inserts a medication into the database and schedules a notification if tracked automatically
     * @param name medication name
     * @param remaining doses remaining
     * @param total total doses in prescription
     * @param hoursBetween hours between doses
     * @param automatic tracked automatically
     * @param notifyAt time of day to schedule notifications
     * @param daysInAdvance days in advance to schedule notification before running out
     */
    @Override
    public void trackNewMed(String name, int remaining, int total, int hoursBetween, boolean automatic, String notifyAt, int daysInAdvance) {
        db.insertMeds(name, remaining, total, hoursBetween, automatic, notifyAt, daysInAdvance);

        if (automatic) {
            Date date = new Date();

            String[] times = notifyAt.split(":");
            int hour = Integer.valueOf(times[0]);
            int minute = Integer.valueOf(times[1]);

            int daysForward = remaining * hoursBetween / 24;
            daysForward -= daysInAdvance;

            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);

            calendar.set(Calendar.HOUR_OF_DAY, hour);
            calendar.set(Calendar.MINUTE, minute);

            calendar.setTimeInMillis(calendar.getTimeInMillis() + (daysForward * MILLIS_PER_DAY));

            scheduleNotification(0, "Refill Reminder", "Remember to pick up your " + name + " refill.", calendar);
        }

        Intent intent = getIntent();
        finish();
        startActivity(intent);
    }
}