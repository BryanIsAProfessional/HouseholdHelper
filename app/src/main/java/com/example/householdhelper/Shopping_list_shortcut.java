package com.example.householdhelper;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.householdhelper.helpers.DatabaseHelper;
import com.example.householdhelper.lists.ListActivity;
import com.example.householdhelper.lists.ListOfListsActivity;

/**
 * Fragment of shortcut links to bookmarked lists
 *
 * @author Bryan Burdick
 * @version 1.0
 * @since 2021-02-06
 */
public class Shopping_list_shortcut extends Fragment {

    // TODO: Rename and change types of parameters
    private String listId, todoListId;
    private Button shopListButton, todoButton;

    /**
     * empty constructor
     */
    public Shopping_list_shortcut() {
    }

    public static Shopping_list_shortcut newInstance() {
        Shopping_list_shortcut fragment = new Shopping_list_shortcut();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        listId = preferences.getString(getString(R.string.shopping_list), "-1");
        todoListId = preferences.getString(getString(R.string.todo_list), "-1");
    }

    /**
     * instantiate views in layout
     * @param view layout
     * @param savedInstanceState
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        shopListButton = view.findViewById(R.id.goToListButton);
        if(!listId.equals("-1")){
            shopListButton.setOnClickListener(v->{
                Intent intent = new Intent(getActivity(), ListActivity.class);
                DatabaseHelper db = new DatabaseHelper(getContext());
                Cursor ret = db.getListById(listId);
                if(ret.moveToFirst()){
                    intent.putExtra("listId", ret.getString(0));
                    intent.putExtra("listName", ret.getString(1));
                    intent.putExtra("listDateCreated", ret.getString(2));
                    intent.putExtra("listLastModified", ret.getString(3));
                    db.close();
                    startActivity(intent);
                }
                db.close();
            });
        }else{
            shopListButton.setText("Set your shopping list");
            shopListButton.setOnClickListener(v->{
                Intent intent = new Intent(getActivity(), ListOfListsActivity.class);
                startActivity(intent);
            });
        }

        todoButton = view.findViewById(R.id.goToToDoListButton);
        if(!todoListId.equals("-1")){
            todoButton.setOnClickListener(v->{
                Intent intent = new Intent(getActivity(), ListActivity.class);
                DatabaseHelper db = new DatabaseHelper(getContext());
                Cursor ret = db.getListById(todoListId);
                if(ret.moveToFirst()){
                    intent.putExtra("listId", ret.getString(0));
                    intent.putExtra("listName", ret.getString(1));
                    intent.putExtra("listDateCreated", ret.getString(2));
                    intent.putExtra("listLastModified", ret.getString(3));
                    db.close();
                    startActivity(intent);
                }
                db.close();
            });
        }else{
            todoButton.setText("Set your to-do list");
            todoButton.setOnClickListener(v->{
                Intent intent = new Intent(getActivity(), ListOfListsActivity.class);
                startActivity(intent);
            });
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_shopping_list_shortcut, container, false);
    }
}