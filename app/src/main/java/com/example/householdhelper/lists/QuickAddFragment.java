package com.example.householdhelper.lists;

import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.householdhelper.R;
import com.example.householdhelper.helpers.DatabaseHelper;

import java.util.ArrayList;

/**
 * The fragment for viewing quick add items. Handles retrieving items from
 * database and displaying them in a recyclerview, and inserting/removing items
 * from the active list when items are clicked.
 *
 * @author Bryan Burdick
 * @version 1.0
 * @since 2021-02-06
 */
public class QuickAddFragment extends Fragment implements QuickAddAdapter.OnItemClickListener {

    public String listId;

    private final ArrayList<QuickAddItem> list = new ArrayList<>();
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

    /**
     * retrieves list items from database and stores them in an ArrayList<QuickAddItem>
     */
    public void initializeArrayList(){
        Cursor ret = db.getAllQuickAddItems();
        if(ret.moveToFirst()){
            list.clear();
            do{
                    QuickAddItem temp = new QuickAddItem(ret.getString(0),ret.getString(1));
                    list.add(temp);

            }while(ret.moveToNext());
        }
    }

    /**
     * passes the layoutManager, adapter, and ArrayList to the recyclerview
     */
    public void startRecyclerView(){
        recyclerView = getView().findViewById(R.id.quickAddRecyclerView);
        layoutManager = new LinearLayoutManager(getContext());
        adapter = new QuickAddAdapter(list, listId, getContext());
        recyclerView.setLayoutManager(layoutManager);
        adapter.setOnItemClickListener(position -> onQuickAddItemLongClicked(position));
        recyclerView.setAdapter(adapter);
    }

    /**
     * opens a yes/no dialog if a quick add item is long clicked. Calls to remove the item from the database
     * and the recyclerview if yes.
     * @param position the position of the item in the Recyclerview adapter
     */
    private void onQuickAddItemLongClicked(int position){

        // dialog popup to delete quick add item
        Bundle bundle = new Bundle();
        bundle.putString("itemName", list.get(position).getName());
        bundle.putInt("itemPosition", position);

        DeleteItemDialog deleteItemDialog = new DeleteItemDialog();
        deleteItemDialog.setArguments(bundle);
        deleteItemDialog.setListener(position1 -> {
            handleDeleteQuickAddItem(position1);
        });

        deleteItemDialog.show(getActivity().getSupportFragmentManager(), "Delete from Quick Add");


    }

    /**
     * Removes the item at index from the database and recyclerview.
     * @param position the index of the item to remove
     */
    private void handleDeleteQuickAddItem(int position){
        db.deleteQuickAddItemById(list.get(position).getId());
        list.remove(position);
        adapter.notifyDataSetChanged();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // initialize list from database
        initializeArrayList();

        // start recyclerview
        startRecyclerView();

        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        view.findViewById(R.id.returnButton).setOnClickListener(view1 -> NavHostFragment.findNavController(QuickAddFragment.this)
                .navigate(R.id.action_SecondFragment_to_FirstFragment));
    }

    @Override
    public void onItemLongClick(int position) {
        onQuickAddItemLongClicked(position);
    }
}