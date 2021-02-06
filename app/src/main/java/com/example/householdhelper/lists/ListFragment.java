package com.example.householdhelper.lists;

import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.householdhelper.helpers.DatabaseHelper;
import com.example.householdhelper.R;

import java.util.ArrayList;
import java.util.Collections;

/**
 * The fragment for viewing a single list. Handles retrieving list items from
 * database and displaying them in a recyclerview.
 *
 * @author Bryan Burdick
 * @version 1.0
 * @since 2021-02-06
 */
public class ListFragment extends Fragment {

    public String listId;

    private final ArrayList<ListItem> list = new ArrayList<>();
    private RecyclerView recyclerView;
    private ListItemsAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private DatabaseHelper db;
    public boolean batchAddOpen = false;

    public Button submitButton, clearButton, batchAddButton;
    public EditText newItemEditText;
    public CheckBox newItemCheckBox;
    public LinearLayout batchAddLayout;


    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        db = new DatabaseHelper(getContext());
        listId = getActivity().getIntent().getStringExtra("listId");

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_list, container, false);
    }

    /**
     * instantiates all declared views and sets onclick listeners
     */
    private void attachButtons() {
        batchAddLayout = getView().findViewById(R.id.batchAddLayout);
        newItemCheckBox = getView().findViewById(R.id.newItemCheckBox);

        clearButton = getView().findViewById(R.id.clearButton);
        batchAddButton = getView().findViewById(R.id.batchAddButton);
        submitButton = getView().findViewById(R.id.submitButton);
        newItemEditText = getView().findViewById(R.id.newItemNameEditText);
        newItemEditText.setOnKeyListener((v, keyCode, event) -> {
            if (keyCode == KeyEvent.KEYCODE_ENTER) {
                submitButton.callOnClick();
                return true;
            }
            return false;
        });

        submitButton.setOnClickListener(v->{
            // gather information to submit to database
            String name = newItemEditText.getText().toString();

            if(name.isEmpty()){
                Toast.makeText(getContext(), "Enter a name for your list", Toast.LENGTH_SHORT).show();
            }else {

                // capitalize first letter of the item
                name = name.substring(0,1).toUpperCase() + name.substring(1).toLowerCase();

                // add to database and collect id
                String id = db.insertListItem(listId, name, "-1");

                // also add to quick add db if checked
                if (newItemCheckBox.isChecked()) {
                    db.insertQuickAddItem(name);
                    newItemCheckBox.setChecked(false);
                }

                // add to list and refresh RecyclerView
                list.add(new ListItem(id, name, 0));
                adapter.notifyDataSetChanged();

                // reset batch add
                newItemEditText.setText("");
            }
        });

        clearButton.setOnClickListener(v->{
            for(int i = list.size()-1; i >= 0; i--){
                if(list.get(i).isChecked()){
                    String id = list.get(i).getId();
                    db.deleteListItemById(id);
                    list.remove(i);
                }

            }
            adapter.notifyDataSetChanged();
        });

        batchAddButton.setOnClickListener(v->{
            if(batchAddOpen){
                batchAddLayout.setVisibility(View.GONE);
                batchAddOpen = false;
            }else{
                batchAddLayout.setVisibility(View.VISIBLE);
                batchAddOpen = true;
            }
        });
    }

    /**
     * retrieves list items from database and stores them in an ArrayList<ListItem>
     */
    public void initializeArrayList(){
        Cursor ret = db.getListItems(listId);
        if(ret.moveToFirst()){
            list.clear();
            do{
                ListItem temp = new ListItem(ret.getString(0),ret.getString(2), ret.getInt(3));
                list.add(temp);
            }while(ret.moveToNext());
        }
    }

    /**
     * passes the layoutManager, adapter, and ArrayList to the recyclerview
     */
    public void startRecyclerView(){
        recyclerView = getView().findViewById(R.id.listRecyclerView);
        layoutManager = new LinearLayoutManager(getContext()) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };

        adapter = new ListItemsAdapter(list);
        adapter.setOnItemClickListener((checked, position) -> {

            DatabaseHelper db = new DatabaseHelper(getContext());
            list.get(position).setIsChecked(checked);
            String status = checked ? "1" : "0";
            db.updateListItem(list.get(position).getId(), list.get(position).getName(), status, "-1");

            recyclerView.post(() -> {
                adapter.sort();
                adapter.notifyDataSetChanged();
            });

            db.close();
        });
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // button onclick setups
        attachButtons();

        // initialize list from database
        initializeArrayList();

        // start recyclerview
        startRecyclerView();

        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        view.findViewById(R.id.quickAddButton).setOnClickListener(view1 -> NavHostFragment.findNavController(ListFragment.this)
                .navigate(R.id.action_ListFragment_to_QuickAddFragment));
    }
}