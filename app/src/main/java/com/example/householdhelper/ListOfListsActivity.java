package com.example.householdhelper;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class ListOfListsActivity extends AppCompatActivity implements NewListDialog.NewListDialogListener {

    private static final String TAG = "ListOfListsActivity";

    private DatabaseHelper db;
    public FloatingActionButton newListButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_of_lists);

        db = new DatabaseHelper(this);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Shopping Lists");
        actionBar.setDisplayHomeAsUpEnabled(true);

        newListButton = findViewById(R.id.newListButton);
        newListButton.setOnClickListener(v -> {
            openNewListDialog();
        });
    }

    public void openNewListDialog(){
        NewListDialog newListDialog = new NewListDialog();
        newListDialog.show(getSupportFragmentManager(), "Create new list");
    }

    public void createNewList(String name){
        Log.d(TAG, "createNewList: started");
        Intent intent = new Intent(this, ListActivity.class);
        intent.putExtra("listName", name);
        intent.putExtra("listId", db.insertList(name));
        startActivity(intent);
    }

    @Override
    public void onBackPressed(){
        Log.d(TAG, "onBackPressed: started");
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}