package com.example.householdhelper.lists;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.database.Cursor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.householdhelper.helpers.DatabaseHelper;
import com.example.householdhelper.MainActivity;
import com.example.householdhelper.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Collections;

public class ListOfListsActivity extends AppCompatActivity implements NewListDialog.NewListDialogListener, ListsAdapter.OnItemClickListener {

    private static final String TAG = "ListOfListsActivity";

    private boolean firstTimeSetup;

    private final ArrayList<List> listsList = new ArrayList<>();
    private DatabaseHelper db;
    public FloatingActionButton newListButton;
    private RecyclerView recyclerView;
    private ListsAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_of_lists);

        db = new DatabaseHelper(this);

        // TODO: set from saved settings
        firstTimeSetup = true;

        // initialize list from database
        initializeArrayList();

        // start recyclerview
        startRecyclerView();

        // set action bar variables
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Shopping Lists");
        actionBar.setDisplayHomeAsUpEnabled(true);

        // find views and attach listeners
        newListButton = findViewById(R.id.newListButton);
        newListButton.setOnClickListener(v -> {
            openNewListDialog();
        });
    }

    public void initializeArrayList(){
        Cursor ret = db.getAllLists();
        if(ret.moveToFirst()){
            listsList.clear();
            do{
                String id = ret.getString(0);
                int totalItems = db.getListItemCount(id);
                int crossedItems = db.getListItemCrossedCount(id);
                List temp = new List(id, ret.getString(1), ret.getString(2), ret.getString(3), crossedItems, totalItems);
                listsList.add(temp);
            }while(ret.moveToNext());
        }
    }

    public void startRecyclerView(){
        recyclerView = findViewById(R.id.listOfListsRecyclerView);
        layoutManager = new LinearLayoutManager(this);
        adapter = new ListsAdapter(listsList, this);
        recyclerView.setLayoutManager(layoutManager);
        adapter.setOnItemClickListener(new ListsAdapter.OnItemClickListener(){
            @Override
            public void onItemClick(int position) {
                openList(listsList.get(position).getName());
            }

            @Override
            public void onDeleteClick(int position) {

                String id = listsList.get(position).getid();
                SharedPreferences preferences = android.preference.PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                String shopListId = preferences.getString(getString(R.string.shopping_list), "-1");
                String todoListId = preferences.getString(getString(R.string.todo_list), "-1");

                if(id.equals(shopListId)){

                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString(getString(R.string.shopping_list), "-1");
                    editor.apply();
                }

                if(id.equals(todoListId)){
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString(getString(R.string.todo_list), "-1");
                    editor.apply();
                }

                deleteList(listsList.get(position).getName(), position);
            }
        });

        recyclerView.setAdapter(adapter);
    }

    public String printArrayList(ArrayList<List> l){
        String ret = "[ ";
        for (int i = 0; i < l.size(); i++){
            if(i > 0){
                ret += ", ";
            }
            ret += l.get(i).getName();
        }
        ret += " ]";
        return ret;
    }

    public void openNewListDialog(){
        NewListDialog newListDialog = new NewListDialog();
        newListDialog.show(getSupportFragmentManager(), "Create new list");
    }

    public void createNewList(String name){
        if(!name.isEmpty()) {
            try {
                db.insertList(name);
            } catch (Exception e) {
            }
            openList(name);
        }else{
            Toast.makeText(getBaseContext(), "Enter a name for your list", Toast.LENGTH_SHORT).show();
        }
    }

    public void deleteList(String name, int position){
        db.deleteListByName(name);
        listsList.remove(position);
        adapter.notifyDataSetChanged();
    }

    public void openList(String name){
        Intent intent = new Intent(this, ListActivity.class);
        Cursor ret = db.getListByName(name);
        if(ret.moveToFirst()){
            intent.putExtra("listId", ret.getString(0));
            intent.putExtra("listName", ret.getString(1));
            intent.putExtra("listDateCreated", ret.getString(2));
            intent.putExtra("listLastModified", ret.getString(3));
            startActivity(intent);
        }
    }

    @Override
    public void onBackPressed(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    @Override
    public void onItemClick(int position) {
        openList(listsList.get(position).getName());
    }

    @Override
    public void onDeleteClick(int position) {
        deleteList(listsList.get(position).getName(), position);
        //recyclerView.getAdapter().notifyDataSetChanged();
    }

    @Override
    public Resources.Theme getTheme(){
        Resources.Theme theme = super.getTheme();

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String themeName = preferences.getString("theme", "Default");

        switch(themeName){
            case "Default":
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
}