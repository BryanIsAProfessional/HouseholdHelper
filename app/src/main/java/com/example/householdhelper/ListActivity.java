package com.example.householdhelper;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.View;

public class ListActivity extends AppCompatActivity implements DeleteItemDialog.DeleteItemDialogListener {

    public String id;
    public String listTitle;
    public String dateCreated;
    public String lastModified;
    private DatabaseHelper db;
    public static final String TAG = "ListActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lists);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        db = new DatabaseHelper(this);

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
    public void deleteQuickAddItem(String name, int position) {
        Log.d(TAG, "deleteQuickAddItem: started");
        db.deleteQuickAddItemByName(name);
    }
}