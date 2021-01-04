package com.example.householdhelper;

import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ListFragment extends Fragment {

    private static final String TAG = "ListFragment";

    private ArrayList<ListItem> list = new ArrayList<>();
    private RecyclerView recyclerView;
    private ListItemsAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private DatabaseHelper db;
    private boolean firstTimeSetup;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        db = new DatabaseHelper(getContext());

        // initialize list from database
        initializeArrayList();

        // start recyclerview
        startRecyclerView();

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_list, container, false);
    }

    public void initializeArrayList(){
        Cursor ret = db.getAllLists();
        if(ret.moveToFirst()){
            list.clear();
            do{
                ListItem temp = new ListItem(ret.getString(0),ret.getString(2), Integer.valueOf(ret.getString(3)));
                list.add(temp);
            }while(ret.moveToNext());
        }
        if(firstTimeSetup && list.size() < 1){
            addTestItems();
        }
    }

    private void addTestItems() {
        list.add(new ListItem("-1", "Bread", 0));
    }

    public void startRecyclerView(){
        Log.d(TAG, "startRecyclerView: List items are " + printArrayList(list));
        recyclerView = getView().findViewById(R.id.listRecyclerView);
        layoutManager = new LinearLayoutManager(getContext());
        adapter = new ListItemsAdapter(list);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }

    public String printArrayList(ArrayList<ListItem> l){
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

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        view.findViewById(R.id.quickAddButton).setOnClickListener(view1 -> NavHostFragment.findNavController(ListFragment.this)
                .navigate(R.id.action_FirstFragment_to_SecondFragment));
    }
}