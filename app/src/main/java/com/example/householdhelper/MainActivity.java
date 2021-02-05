package com.example.householdhelper;


import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import com.example.householdhelper.helpers.DatabaseHelper;
import com.example.householdhelper.helpers.WelcomeGenerator;
import com.example.householdhelper.lists.ListOfListsActivity;
import com.example.householdhelper.recipes.RecipesActivity;
import com.example.householdhelper.schedule.ScheduleActivity;
import com.example.householdhelper.settings.SettingsActivity;
import com.google.android.material.navigation.NavigationView;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.core.view.GravityCompat;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = "MainActivity";

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private Toolbar toolbar;
    private TextView welcomeTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = findViewById(R.id.toolbar);
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        welcomeTextView = findViewById(R.id.textViewWelcome);

        WelcomeGenerator welcomer = new WelcomeGenerator();

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String name = preferences.getString("name", "");

        welcomeTextView.setText(welcomer.generateWelcome(name));

        setSupportActionBar(toolbar);

        navigationView.bringToFront();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(menuItem -> {
            Intent intent;
            switch (menuItem.getItemId()) {
                case R.id.nav_schedule:
                    intent = new Intent(getApplicationContext(), ScheduleActivity.class);
                    startActivity(intent);
                    break;
                case R.id.nav_recipes:
                    intent = new Intent(getApplicationContext(), RecipesActivity.class);
                    startActivity(intent);
                    break;
                case R.id.nav_lists:
                    intent = new Intent(getApplicationContext(), ListOfListsActivity.class);
                    startActivity(intent);
                    break;
                case R.id.nav_settings:
                    intent = new Intent(getApplicationContext(), SettingsActivity.class);
                    startActivity(intent);
                    break;
            }
            drawerLayout.closeDrawer(GravityCompat.START);
            return true;
        });
    }

    @Override
    public void onBackPressed(){
        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }
        else{super.onBackPressed();}
    }

    @Override
    public Resources.Theme getTheme(){
        Resources.Theme theme = super.getTheme();

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String themeName = preferences.getString("theme", "Default");

        switch(themeName){
            case "Default":
                //editor.putString();
                theme.applyStyle(R.style.Theme_HouseholdHelper_NoActionBar, true);
                break;
            case "Beach":
                theme.applyStyle(R.style.Theme_Beach_NoActionBar, true);
                break;
            case "Dark":
                theme.applyStyle(R.style.Theme_Dark_NoActionBar, true);
                break;
        }
        return theme;
    }
}