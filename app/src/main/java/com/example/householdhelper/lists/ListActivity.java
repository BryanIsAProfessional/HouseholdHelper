package com.example.householdhelper.lists;

import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.householdhelper.R;
import com.example.householdhelper.helpers.DatabaseHelper;

/**
 * The activity for viewing a single list. Contains a list fragment that handles list items.
 *
 * @author Bryan Burdick
 * @version 1.0
 * @since 2021-02-06
 */
public class ListActivity extends AppCompatActivity {

    public String id;
    public String listTitle;
    public String dateCreated;
    public String lastModified;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lists);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DatabaseHelper db = new DatabaseHelper(this);

        // set from getextras
        id = getIntent().getStringExtra("listId");
        listTitle = getIntent().getStringExtra("listName");
        dateCreated = getIntent().getStringExtra("listDateCreated");
        lastModified = getIntent().getStringExtra("listLastModified");

        // set action bar variables
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(listTitle);
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.list_options_menu, menu);
        return true;
    }

    // creates a dropdown menu to set this list as the active shopping list or to-do list in sharedpreferences
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        SharedPreferences sharedPref;
        SharedPreferences.Editor editor;
        switch(item.getItemId()){
            case R.id.setShoppingList:
                sharedPref = android.preference.PreferenceManager.getDefaultSharedPreferences(this);
                editor = sharedPref.edit();
                editor.putString(getString(R.string.shopping_list), id);
                editor.apply();
                return true;
            case R.id.setToDoList:
                sharedPref = android.preference.PreferenceManager.getDefaultSharedPreferences(this);
                editor = sharedPref.edit();
                editor.putString(getString(R.string.todo_list), id);
                editor.apply();
                return true;
        }
        return(super.onOptionsItemSelected(item));
    }

    /**
     * Chooses the theme based on the one selected in sharedpreferences, or default if none is selected
     * @return the selected theme
     */
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