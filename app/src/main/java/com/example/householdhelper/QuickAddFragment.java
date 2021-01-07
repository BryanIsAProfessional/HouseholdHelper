package com.example.householdhelper;

import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class QuickAddFragment extends Fragment implements QuickAddAdapter.OnItemClickListener, DeleteItemDialog.DeleteItemDialogListener {

    private static final String TAG = "QuickAddFragment";

    public String listId;

    private ArrayList<QuickAddItem> list = new ArrayList<>();
    private RecyclerView recyclerView;
    private QuickAddAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private DatabaseHelper db;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        db = new DatabaseHelper(getContext());
        listId = getActivity().getIntent().getStringExtra("listId");
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_quickadd, container, false);
    }

    public void initializeArrayList(){
        Cursor ret = db.getAllQuickAddItems();
        if(ret.moveToFirst()){
            list.clear();
            do{
                    QuickAddItem temp = new QuickAddItem(ret.getString(0),ret.getString(1));
                    list.add(temp);

            }while(ret.moveToNext());
        }
        printArrayList(list);
    }

    public void startRecyclerView(){
        Log.d(TAG, "startRecyclerView: Quick add items are " + printArrayList(list));
        recyclerView = getView().findViewById(R.id.quickAddRecyclerView);
        layoutManager = new LinearLayoutManager(getContext());
        adapter = new QuickAddAdapter(list, listId);
        recyclerView.setLayoutManager(layoutManager);
        adapter.setOnItemClickListener(new QuickAddAdapter.OnItemClickListener(){
            @Override
            public void onItemClick(int position) {
                onQuickAddItemClicked(position);
            }

            @Override
            public void onItemLongClick(int position) {
                onQuickAddItemLongClicked(position);
            }
        });
        recyclerView.setAdapter(adapter);
    }

    private void onQuickAddItemClicked(int position) {
        Log.d(TAG, "onQuickAddItemClicked: " + position);
        // check if item is already in the list
        Cursor ret = db.getListItems(listId);
        boolean found = false;

        if(ret.moveToFirst()){
            do{
                if( ((QuickAddAdapter.QuickAddAdapterViewHolder)recyclerView.findViewHolderForAdapterPosition(position)).itemNameTextView.getText().toString().equals(ret.getString(2)) ){
                    found = true;
                }
            }while(!found && ret.moveToNext());
        }

        if(!found){
            adapter.notifyDataSetChanged();

            // insert item into current list
            db.insertListItem(listId, list.get(position).getName(), "-1");
        }else{
            Toast.makeText(getContext(), "This item is already in your list!", Toast.LENGTH_SHORT);
        }

    }

    private void onQuickAddItemLongClicked(int position){
        Log.d(TAG, "onQuickAddItemLongClicked: " + position);

        // dialog popup to delete quick add item
        Bundle bundle = new Bundle();
        bundle.putString("itemName", list.get(position).getName());
        bundle.putInt("itemPosition", position);

        DeleteItemDialog deleteItemDialog = new DeleteItemDialog();
        deleteItemDialog.setArguments(bundle);
        deleteItemDialog.show(getActivity().getSupportFragmentManager(), "Delete from Quick Add");

        list.remove(position);
        adapter.notifyDataSetChanged();
    }

    public String printArrayList(ArrayList<QuickAddItem> l){
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
        Log.d(TAG, "onViewCreated: started");

        // initialize list from database
        initializeArrayList();

        // start recyclerview
        startRecyclerView();

        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        view.findViewById(R.id.returnButton).setOnClickListener(view1 -> NavHostFragment.findNavController(QuickAddFragment.this)
                .navigate(R.id.action_SecondFragment_to_FirstFragment));
    }

    @Override
    public void onItemClick(int position) {
        onQuickAddItemClicked(position);
    }

    @Override
    public void onItemLongClick(int position) {
        Log.d(TAG, "onItemLongClick: started");
        onQuickAddItemLongClicked(position);
    }

    @Override
    public void deleteQuickAddItem(String name, int position) {
        Log.d(TAG, "deleteQuickAddItem: started");
        //db.deleteQuickAddItemByName(name);
    }
}