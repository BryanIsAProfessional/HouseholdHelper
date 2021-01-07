package com.example.householdhelper;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class ListOfListsActivity extends AppCompatActivity implements NewListDialog.NewListDialogListener, ListsAdapter.OnItemClickListener {

    private static final String TAG = "ListOfListsActivity";

    private boolean firstTimeSetup;

    private ArrayList<List> listsList = new ArrayList<>();
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
                List temp = new List(ret.getString(0),ret.getString(1),ret.getString(2),ret.getString(3));
                listsList.add(temp);
            }while(ret.moveToNext());
        }
        if(firstTimeSetup && listsList.size() < 1){
            addTestItems();
        }
    }

    private void addTestItems() {
        listsList.add(new List("0", "Shopping list", "0","0"));
    }

    public void startRecyclerView(){
        Log.d(TAG, "startRecyclerView: List items are " + printArrayList(listsList));
        recyclerView = findViewById(R.id.listOfListsRecyclerView);
        layoutManager = new LinearLayoutManager(this);
        adapter = new ListsAdapter(listsList);
        recyclerView.setLayoutManager(layoutManager);
        adapter.setOnItemClickListener(new ListsAdapter.OnItemClickListener(){
            @Override
            public void onItemClick(int position) {
                Log.d(TAG, "onItemClick: " + position);
                openList(listsList.get(position).getName());
            }

            @Override
            public void onDeleteClick(int position) {
                Log.d(TAG, "onDeleteClick: " + position);
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
            Log.d(TAG, "createNewList: started");
            try {
                db.insertList(name);
            } catch (Exception e) {
                Log.d(TAG, "Error: " + e.toString());
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
        Log.d(TAG, "onBackPressed: started");
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    @Override
    public void onItemClick(int position) {
        Log.d(TAG, "onItemClick: " + position);
        openList(listsList.get(position).getName());
    }

    @Override
    public void onDeleteClick(int position) {
        Log.d(TAG, "onDeleteClick: " + position);
        deleteList(listsList.get(position).getName(), position);
        //recyclerView.getAdapter().notifyDataSetChanged();
    }
}