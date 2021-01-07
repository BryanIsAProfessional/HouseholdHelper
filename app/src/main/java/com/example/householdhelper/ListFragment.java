package com.example.householdhelper;

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

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ListFragment extends Fragment {

    private static final String TAG = "ListFragment";

    public String listId;

    private ArrayList<ListItem> list = new ArrayList<>();
    private ArrayList<CrossedListItem> crossedList = new ArrayList<>();
    private RecyclerView recyclerView, crossedListRecyclerView;
    private ListItemsAdapter adapter;
    private CrossedListItemsAdapter crossedListAdapter;
    private RecyclerView.LayoutManager layoutManager, crossedListLayoutManager;
    private DatabaseHelper db;
    public boolean batchAddOpen = false;

    public Button submitButton, clearButton, crossOffButton, batchAddButton;
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

    private void attachButtons() {
        batchAddLayout = getView().findViewById(R.id.batchAddLayout);
        newItemCheckBox = getView().findViewById(R.id.newItemCheckBox);

        clearButton = getView().findViewById(R.id.clearButton);
        crossOffButton = getView().findViewById(R.id.crossOffButton);
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
            for(int i = crossedList.size()-1; i >= 0; i--){
                String id = crossedList.get(i).getId();
                db.deleteListItemById(id);
                crossedList.remove(i);
            }
            clearButton.setClickable(false);
            clearButton.setAlpha((float) 0.1);
            crossedListAdapter.notifyDataSetChanged();
        });

        crossOffButton.setOnClickListener(v->{
            Log.d(TAG, "crossOffButton: started");

            // iterate through all items
            for(int i = recyclerView.getAdapter().getItemCount()-1; i >= 0; i--){
                Log.d(TAG, "crossOffButton: checking position " + i);
                ListItemsAdapter tempAdapter = (ListItemsAdapter)recyclerView.getAdapter();
                ListItem tempItem = tempAdapter.getItem(i);

                ListItemsAdapter.ListsAdapterViewHolder viewHolder = (ListItemsAdapter.ListsAdapterViewHolder)recyclerView.findViewHolderForAdapterPosition(i);
                // TODO: error here with ids causing a null error
                try {
                    if (viewHolder.checkBox.isChecked()) {
                        // update state in database
                        db.updateListItem(tempItem.getId(), tempItem.getName(), "2", "-1");

                        // if the checkbox is checked, move it to crossedList and delete it from list
                        crossedList.add(new CrossedListItem(tempItem.getId(), tempItem.getName()));
                        list.remove(i);
                    }
                }catch(Exception e){
                    Log.d(TAG, "crossOffButton: error: " + e.toString());
                }
            }

            if(crossedList.size() > 0){
                clearButton.setClickable(true);
                clearButton.setAlpha((float) 1);
            }

            // notify both adapters of changes
            adapter.notifyDataSetChanged();
            crossedListAdapter.notifyDataSetChanged();
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

    public void initializeArrayList(){
        Cursor ret = db.getListItems(listId);
        if(ret.moveToFirst()){
            list.clear();
            do{
                if(ret.getInt(3) < 2){
                    ListItem temp = new ListItem(ret.getString(0),ret.getString(2), ret.getInt(3));
                    list.add(temp);
                }else{
                    CrossedListItem temp = new CrossedListItem(ret.getString(0),ret.getString(2));
                    crossedList.add(temp);
                }

            }while(ret.moveToNext());
        }
    }

    public void startRecyclerView(){
        Log.d(TAG, "startRecyclerView: List items are " + printArrayList(list));
        recyclerView = getView().findViewById(R.id.listRecyclerView);
        layoutManager = new LinearLayoutManager(getContext()) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        adapter = new ListItemsAdapter(list);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        crossedListRecyclerView = getView().findViewById(R.id.crossedListRecyclerView);
        crossedListLayoutManager = new LinearLayoutManager(getContext()) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        crossedListAdapter = new CrossedListItemsAdapter(crossedList);
        crossedListRecyclerView.setLayoutManager(crossedListLayoutManager);
        crossedListRecyclerView.setAdapter(crossedListAdapter);

        if(crossedList.size() < 1){
            clearButton.setClickable(false);
            clearButton.setAlpha((float) 0.1);
        }
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

        // button onclick setups
        attachButtons();

        // initialize list from database
        initializeArrayList();

        // start recyclerview
        startRecyclerView();

        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        view.findViewById(R.id.quickAddButton).setOnClickListener(view1 -> NavHostFragment.findNavController(ListFragment.this)
                .navigate(R.id.action_FirstFragment_to_SecondFragment));
    }
}