package com.example.householdhelper.recipes;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.database.Cursor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.RelativeLayout;
import android.widget.SearchView;

import com.example.householdhelper.R;
import com.example.householdhelper.helpers.DatabaseHelper;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

public class RecipesActivity extends AppCompatActivity {

    public ArrayList<Recipe> list = new ArrayList<>();
    public DatabaseHelper db;

    public FragmentRefreshListener getFragmentRefreshListener() {
        return fragmentRefreshListener;
    }

    public void setFragmentRefreshListener(FragmentRefreshListener fragmentRefreshListener) {
        this.fragmentRefreshListener = fragmentRefreshListener;
    }

    private FragmentRefreshListener fragmentRefreshListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipes);

        BottomNavigationView navView = findViewById(R.id.recipes_nav_view);
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.recipesGalleryFragment, R.id.recipeBookFragment)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        ActivityCompat.requestPermissions(RecipesActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        ActivityCompat.requestPermissions(RecipesActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);

        navView.setSelectedItemId(R.id.recipeBookFragment);

        actionBar.setTitle(getResources().getString(R.string.title_recipe_book));

        db = new DatabaseHelper(this);
        fillRecipesList();
    }

    public void fillRecipesList(){
        list = new ArrayList<>();
        Cursor ret = db.getAllRecipes();
        if(ret.moveToFirst()){
            do{
                list.add(new Recipe(ret.getString(0),ret.getString(1),ret.getString(2),ret.getString(3),ret.getInt(4)+ret.getInt(5),ret.getInt(7),ret.getString(8),ret.getString(6), ret.getInt(11)));
            }while(ret.moveToNext());
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.recipe_book_options_menu, menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setContentDescription("search bar");

        searchView.setImeOptions(EditorInfo.IME_ACTION_DONE);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                getFragmentRefreshListener().onSearch(newText);
                return false;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.addRecipeOption:
                Intent intent = new Intent(this, RecipeActivity.class);
                intent.putExtra("recipeId", "-1");
                startActivity(intent);
                return true;
        }
        return(super.onOptionsItemSelected(item));
    }

    @Override
    protected void onRestart(){
        super.onRestart();

        fillRecipesList();
    }

    public interface FragmentRefreshListener{
        void onSearch(String searchTerm);
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