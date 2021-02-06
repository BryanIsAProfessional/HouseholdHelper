package com.example.householdhelper.recipes.RecipeBook;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import com.example.householdhelper.MainActivity;
import com.example.householdhelper.R;
import com.example.householdhelper.recipes.Recipe;
import com.example.householdhelper.recipes.RecipeActivity;
import com.example.householdhelper.recipes.RecipesActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

/**
 * The fragment for viewing recipe titles. Handles retrieving recipes from
 * database and displaying them in a recyclerview.
 *
 * @author Bryan Burdick
 * @version 1.0
 * @since 2021-02-06
 */
public class RecipeBookFragment extends Fragment {

    public ArrayList<Recipe> list = new ArrayList<>();

    private RecyclerView recyclerView;
    private RecipeBookAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    public RecipeBookFragment() {
        // Required empty public constructor
    }

    /**
     * @return A new instance of fragment RecipesList.
     */
    // TODO: Rename and change types and number of parameters
    public static RecipeBookFragment newInstance() {
        RecipeBookFragment fragment = new RecipeBookFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = ((RecipesActivity)getActivity()).getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("Recipe Book");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        ((RecipesActivity)getActivity()).setFragmentRefreshListener(searchTerm -> adapter.getFilter().filter(searchTerm));


        return inflater.inflate(R.layout.fragment_recipe_book, container, false);
    }

    /**
     * passes the layoutManager, adapter, and ArrayList to the recyclerview
     */
    public void startRecyclerView(){
        recyclerView = getView().findViewById(R.id.recipeBookRecyclerView);
        layoutManager = new LinearLayoutManager(getContext());
        adapter = new RecipeBookAdapter(list);
        adapter.setOnItemClickListener(position -> {
            Intent intent = new Intent(getContext(), RecipeActivity.class);
            intent.putExtra("recipeId", list.get(position).getId());
            startActivity(intent);
        });
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        RecipesActivity activity = (RecipesActivity)getActivity();

        list = activity.list;
        startRecyclerView();
    }

    /**
     * refreshes recyclerview content when resuming
     */
    @Override
    public void onResume() {
        super.onResume();
        RecipesActivity activity = (RecipesActivity)getActivity();
        list = activity.list;
        startRecyclerView();
    }
}