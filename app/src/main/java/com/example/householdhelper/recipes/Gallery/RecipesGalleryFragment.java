package com.example.householdhelper.recipes.Gallery;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
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

import com.example.householdhelper.R;
import com.example.householdhelper.recipes.Recipe;
import com.example.householdhelper.recipes.RecipeActivity;
import com.example.householdhelper.recipes.RecipesActivity;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RecipesGalleryFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RecipesGalleryFragment extends Fragment{

    // the fragment initialization parameters
    private static final String TAG = "RecipesGalleryFragment";

    public ArrayList<Recipe> list;

    private RecyclerView recyclerView;
    private RecipesGalleryAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    public RecipesGalleryFragment() {
        // Required empty public constructor
    }

    /**
     * @return A new instance of fragment RecipesList.
     */
    // TODO: Rename and change types and number of parameters
    public static RecipesGalleryFragment newInstance() {
        RecipesGalleryFragment fragment = new RecipesGalleryFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = ((AppCompatActivity)getActivity()).getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("Food Gallery");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ((RecipesActivity)getActivity()).setFragmentRefreshListener(searchTerm -> adapter.getFilter().filter(searchTerm));

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_recipe_gallery, container, false);
    }

    public void startRecyclerView(){
        recyclerView = getView().findViewById(R.id.recipesRecyclerView);
        layoutManager = new LinearLayoutManager(getContext());
        adapter = new RecipesGalleryAdapter(list, getContext());
        adapter.setOnItemClickListener(position -> {
            Intent intent = new Intent(getContext(), RecipeActivity.class);
            intent.putExtra("recipeId", list.get(position).getId());
            startActivity(intent);
        });
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }

    public String printArrayList(ArrayList<Recipe> l){
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
        RecipesActivity activity = (RecipesActivity)getActivity();
        assert activity != null;
        list = new ArrayList<>();
        for(int i = 0; i < activity.list.size(); i++){
            if(activity.list.get(i).getUri() != null){
                list.add(activity.list.get(i));
            }
        }
        startRecyclerView();
    }

    @Override
    public void onResume() {
        super.onResume();
        RecipesActivity activity = (RecipesActivity)getActivity();
        assert activity != null;
        list = new ArrayList<>();
        for(int i = 0; i < activity.list.size(); i++){
            if(activity.list.get(i).getUri() != null){
                list.add(activity.list.get(i));
            }
        }
        startRecyclerView();
    }

}