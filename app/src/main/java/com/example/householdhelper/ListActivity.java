package com.example.householdhelper;

import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;

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
}