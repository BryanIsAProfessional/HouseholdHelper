package com.example.householdhelper.recipes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;

import com.example.householdhelper.R;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.householdhelper.helpers.BitmapResolver;
import com.example.householdhelper.helpers.DatabaseHelper;
import com.example.householdhelper.helpers.Table;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Collections;

/**
 * The activity for viewing a recipe.
 *
 * @author Bryan Burdick
 * @version 1.0
 * @since 2021-02-06
 */
public class RecipeActivity extends AppCompatActivity implements IngredientCategoriesAdapter.DeleteListener {

    public static final int PICK_IMAGE = 1;

    public String recipeId, recipeName, dateCreated, lastMade, author, newRecipeName, newAuthor, imageLink, shoppingListId;
    public String[] ingredientCategories, instructionCategories;

    public int prepTime, cookTime, newPrepTime, newCookTime, servings, newServings, imageRotation;

    public Bitmap image;

    public TextView recipeTitleTextView, prepTimeTextView, cookTimeTextView, totalTimeTextView, servingsTextView, authorTextView, prepMinutesTextView, cookMinutesTextView, imageInstructionsTextView;
    public EditText recipeTitleEditText, prepTimeEditText, cookTimeEditText, servingsEditText, authorEditText;
    public ImageView recipePicture;
    public ImageButton addToShoppingListButton, rotateImageButton;
    public Button addIngredientCategoryButton, addInstructionCategoryButton;

    public boolean editMode, inShopList;

    private RecyclerView ingredientCategoriesRecyclerView, instructionsRecyclerView;
    private IngredientCategoriesAdapter ingredientCategoriesAdapter;
    private InstructionCategoriesAdapter instructionCategoriesAdapter;
    private RecyclerView.LayoutManager ingredientCategoriesLayoutManager, instructionsLayoutManager;

    public ArrayList<IngredientCategory> ingredientCategoriesList = new ArrayList<>();
    public ArrayList<IngredientCategory> backupIngredientCategoriesList = new ArrayList<>();

    public ArrayList<InstructionCategory> instructionCategoriesList = new ArrayList<>();
    public ArrayList<InstructionCategory> backupInstructionCategoriesList = new ArrayList<>();

    public DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        db = new DatabaseHelper(this);
        editMode = false;

        if(getIntent().hasExtra("recipeId")){
            recipeId = getIntent().getStringExtra("recipeId");
        } else {
            recipeId = "-1";
        }

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        shoppingListId = preferences.getString(getString(R.string.shopping_list), "-1");

        setVariablesFromDatabase();
        initializeViews();
        setAuthor();
        setServings();
        setCookTimes();

        startRecyclerView();

        if(recipeId.equals("-1")){
            toggleEditMode();
        }
    }

    /**
     * Initializes variables from database.
     *
     */
    private void setVariablesFromDatabase() {
        if (!recipeId.isEmpty() && !recipeId.equals("-1")) {
            Cursor ret = db.getRecipeById(recipeId);
            if (ret.moveToFirst()) {
                recipeName = ret.getString(1);
                dateCreated = ret.getString(2);
                lastMade = ret.getString(3);
                prepTime = ret.getInt(4);
                cookTime = ret.getInt(5);
                author = ret.getString(6);
                servings = ret.getInt(7);
                imageLink = ret.getString(8);
                ingredientCategories = ret.getString(9).split("`");
                instructionCategories = ret.getString(10).split("`");
                imageRotation = ret.getInt(11);

                Cursor listRet = db.getAllRecipeInstructions(recipeId);

                for(int i = 0; i < instructionCategories.length; i++){
                    instructionCategoriesList.add(new InstructionCategory("-1", instructionCategories[i], new ArrayList<>()));
                }

                if(listRet.moveToFirst()){

                    do{
                        boolean inserted = false;
                        // add item to list
                        String id = listRet.getString(0);
                        String text = listRet.getString(1);
                        String category = listRet.getString(3);
                        int order = listRet.getInt(4);


                        for(int i = 0; !inserted && i < instructionCategoriesList.size(); i++){

                            if(instructionCategoriesList.get(i).getName().equals(category)){
                                instructionCategoriesList.get(i).getInstructionList().add(new Instruction(id, text, order));
                                inserted = true;
                            }
                        }

                        copyArrayListInstruction(instructionCategoriesList);

                    }while(listRet.moveToNext());

                    for(int i = 0; i < instructionCategoriesList.size(); i++){
                        Collections.sort(instructionCategoriesList.get(i).getInstructionList());
                    }

                }

                Cursor listRet2 = db.getAllRecipeItems(recipeId);

                for(int i = 0; i < ingredientCategories.length; i++){
                    ingredientCategoriesList.add(new IngredientCategory("-1", ingredientCategories[i], new ArrayList<>()));
                }

                if(listRet2.moveToFirst()){

                    do{
                        boolean inserted = false;
                        // add item to list
                        String id = listRet2.getString(0);
                        String name = listRet2.getString(1);
                        String category = listRet2.getString(5);
                        int order = listRet2.getInt(6);


                        for(int i = 0; !inserted && i < ingredientCategoriesList.size(); i++){


                            if(ingredientCategoriesList.get(i).getName().equals(category)){

                                ArrayList<Measurement> measurementArrayList = new ArrayList<>();

                                Cursor measurementRet = db.getAllMeasurements(Table.RECIPE, id);
                                if(measurementRet.moveToFirst()){
                                    do{
                                        measurementArrayList.add(new Measurement(measurementRet.getString(0), measurementRet.getString(1), measurementRet.getString(2), measurementRet.getString(3), measurementRet.getString(4),  -1));
                                    }while(measurementRet.moveToNext());

                                    for(int p = 0; p < measurementArrayList.size(); p++){
                                    }

                                }else{
                                }

                                ingredientCategoriesList.get(i).getIngredientList().add(new Ingredient(id, name, order, measurementArrayList));
                                inserted = true;
                            }
                        }

                        copyArrayListIngredient(ingredientCategoriesList);

                    }while(listRet2.moveToNext());

                    for(int i = 0; i < ingredientCategoriesList.size(); i++){
                        Collections.sort(ingredientCategoriesList.get(i).getIngredientList());
                    }

                }

            }
        }else{
            dateCreated = lastMade = author = "";
            recipeName = "Recipe Name";
            prepTime = cookTime = servings =  0;
        }
        newRecipeName = recipeName;
        newPrepTime = prepTime;
        newCookTime = cookTime;
        newServings = servings;
        newAuthor = author;

        inShopList = db.listContainsRecipe(recipeId);
    }

    /**
     * Initializes all views in layout. Also most onclicks are defined here
     */
    private void initializeViews() {

        rotateImageButton = findViewById(R.id.rotateImageButton);
        rotateImageButton.setOnClickListener(v->{
            if(image != null){
                imageRotation += 90;
                if(imageRotation > 360){
                    imageRotation -= 360;
                }
                rotateImage(image, imageRotation);
            }

        });

        addToShoppingListButton = findViewById(R.id.addToShoppingListButton);
        if(!shoppingListId.equals("-1")){
            addToShoppingListButton.setOnClickListener(v->{
                handleAddShopListClick();
            });
        }else{
            addToShoppingListButton.setVisibility(View.GONE);
        }

        if(inShopList){
            addToShoppingListButton.setImageDrawable(getResources().getDrawable(R.drawable.ic_baseline_local_grocery_store_48_on));
        }

        imageInstructionsTextView = findViewById(R.id.imageInstructionsTextView);
        addIngredientCategoryButton = findViewById(R.id.addIngredientCategoryButton);
        addIngredientCategoryButton.setOnClickListener(v -> {
            ingredientCategoriesList.add(new IngredientCategory("-1", "Ingredients", new ArrayList<>()));
            ingredientCategoriesAdapter.notifyItemInserted(ingredientCategoriesList.size());
        });
        addInstructionCategoryButton = findViewById(R.id.addInstructionCategoryButton);
        addIngredientCategoryButton.setVisibility(View.GONE);
        addInstructionCategoryButton.setVisibility(View.GONE);
        addInstructionCategoryButton.setOnClickListener(v -> {
            instructionCategoriesList.add(new InstructionCategory("-1", "Instructions", new ArrayList<>()));
            instructionCategoriesAdapter.notifyItemInserted(instructionCategoriesList.size());
        });

        recipeTitleTextView = findViewById(R.id.recipeTitleTextView);
        recipeTitleTextView.setText(recipeName);

        prepTimeTextView = findViewById(R.id.prepTimeTextView);
        cookTimeTextView = findViewById(R.id.cookTimeTextView);
        totalTimeTextView = findViewById(R.id.totalTimeTextView);
        servingsTextView = findViewById(R.id.servingsTextView);
        authorTextView = findViewById(R.id.authorTextView);
        prepMinutesTextView = findViewById(R.id.prepMinutesTextView);
        cookMinutesTextView = findViewById(R.id.cookMinutesTextView);

        recipeTitleEditText = findViewById(R.id.recipeTitleEditText);
        if(recipeId.equals("-1")){
            recipeTitleEditText.setHint(recipeName);
        }else{
            recipeTitleEditText.setText(recipeName);
        }


        prepTimeEditText = findViewById(R.id.prepTimeEditText);
        cookTimeEditText = findViewById(R.id.cookTimeEditText);
        servingsEditText = findViewById(R.id.servingsEditText);
        authorEditText = findViewById(R.id.authorEditText);

        recipeTitleEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                recipeTitleTextView.setText(s);
                newRecipeName = String.valueOf(s);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        recipeTitleEditText.setOnKeyListener((v, keyCode, event) -> {
            // If the event is a key-down event on the "enter" button
            if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                    (keyCode == KeyEvent.KEYCODE_ENTER)) {
                // Close keyboard
                InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                return true;
            }
            return false;
        });

        prepTimeEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                try{
                    newPrepTime = Integer.valueOf(String.valueOf(s).trim());
                    setCookTimes();
                }catch(Exception e){
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        prepTimeEditText.setOnKeyListener((v, keyCode, event) -> {
            // If the event is a key-down event on the "enter" button
            if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                    (keyCode == KeyEvent.KEYCODE_ENTER)) {
                // Close keyboard
                InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                return true;
            }
            return false;
        });

        cookTimeEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                try{
                    newCookTime = Integer.parseInt(String.valueOf(s).trim());
                    setCookTimes();
                }catch(Exception e){
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        cookTimeEditText.setOnKeyListener((v, keyCode, event) -> {
            // If the event is a key-down event on the "enter" button
            if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                    (keyCode == KeyEvent.KEYCODE_ENTER)) {
                // Close keyboard
                InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                return true;
            }
            return false;
        });

        servingsEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                try{
                    newServings = Integer.valueOf(String.valueOf(s).trim());
                    setServings();
                }catch(Exception e){
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        servingsEditText.setOnKeyListener((v, keyCode, event) -> {
            // If the event is a key-down event on the "enter" button
            if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                    (keyCode == KeyEvent.KEYCODE_ENTER)) {
                // Close keyboard
                InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                return true;
            }
            return false;
        });

        authorEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                newAuthor = String.valueOf(s);
                setAuthor();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        authorEditText.setOnKeyListener((v, keyCode, event) -> {
            // If the event is a key-down event on the "enter" button
            if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                    (keyCode == KeyEvent.KEYCODE_ENTER)) {
                // Close keyboard
                InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                return true;
            }
            return false;
        });

        recipePicture = findViewById(R.id.recipePictureImageView);

        recipePicture.setOnClickListener(v->{
            if(editMode){
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);

                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);
            }
        });

        if(imageLink != null && !imageLink.isEmpty()){
            setImage(imageLink);
            imageInstructionsTextView.setVisibility(View.GONE);
            if(editMode){
                rotateImageButton.setVisibility(View.VISIBLE);
            }else{
                rotateImageButton.setVisibility(View.GONE);
            }
        }else{
            recipePicture.setImageDrawable(getResources().getDrawable(R.drawable.ic_image_placeholder));
            rotateImageButton.setVisibility(View.GONE);
            if(!editMode){
                recipePicture.setVisibility(View.GONE);
                imageInstructionsTextView.setVisibility(View.GONE);
            }else{
                imageInstructionsTextView.setVisibility(View.VISIBLE);
                recipePicture.setVisibility(View.VISIBLE);
            }
        }



        toggleTextVisibility(editMode);
    }

    /**
     * passes the layoutManager, adapter, and ArrayList to the recyclerview
     */
    public void startRecyclerView(){
        ingredientCategoriesRecyclerView = findViewById(R.id.ingredientCategoriesRecyclerView);
        ingredientCategoriesLayoutManager = new LinearLayoutManager(this) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        ingredientCategoriesAdapter = new IngredientCategoriesAdapter(ingredientCategoriesList, editMode, id -> {
            if(!id.equals("-1")){
                db.deleteRecipeItemById(id);
            }
        });
        ingredientCategoriesRecyclerView.setLayoutManager(ingredientCategoriesLayoutManager);
        ingredientCategoriesRecyclerView.setAdapter(ingredientCategoriesAdapter);

        RecyclerView.ItemDecoration divider = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        ingredientCategoriesRecyclerView.addItemDecoration(divider);

        ItemTouchHelper helper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP | ItemTouchHelper.DOWN, 0) {

            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {

                int position_initial = viewHolder.getAdapterPosition();
                int position_target = target.getAdapterPosition();

                Collections.swap(ingredientCategoriesList, position_initial, position_target);

                recyclerView.getAdapter().notifyItemMoved(position_initial, position_target);

                return true;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            }

            @Override
            public boolean isLongPressDragEnabled() {
                return editMode;
            }

            @Override
            public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
                int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
                int swipeFlags = 0;
                return makeMovementFlags(dragFlags, swipeFlags);
            }
        });

        helper.attachToRecyclerView(ingredientCategoriesRecyclerView);

        instructionsRecyclerView = findViewById(R.id.instructionCategoriesRecyclerView);
        instructionsLayoutManager = new LinearLayoutManager(this) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        instructionCategoriesAdapter = new InstructionCategoriesAdapter(instructionCategoriesList, editMode, id -> {
            if(!id.equals("-1")){
                db.deleteInstructionById(id);
            }
        });
        instructionsRecyclerView.setLayoutManager(instructionsLayoutManager);
        instructionsRecyclerView.setAdapter(instructionCategoriesAdapter);

        ItemTouchHelper ingredientsHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP | ItemTouchHelper.DOWN, 0) {

            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {

                int position_initial = viewHolder.getAdapterPosition();
                int position_target = target.getAdapterPosition();

                Collections.swap(ingredientCategoriesList, position_initial, position_target);

                recyclerView.getAdapter().notifyItemMoved(position_initial, position_target);

                return true;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            }

            @Override
            public boolean isLongPressDragEnabled() {
                return editMode;
            }

            @Override
            public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
                int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
                int swipeFlags = 0;
                return makeMovementFlags(dragFlags, swipeFlags);
            }
        });

        ingredientsHelper.attachToRecyclerView(instructionsRecyclerView);

    }

    /**
     * formats the text for the author field depending on whether it is in edit mode or not
     */
    private void setAuthor() {
        if(!editMode){
            authorTextView.setText("Author: " + author);
        }else{
            authorTextView.setText("Author: ");
        }
    }

    /**
     * formats the text for the servings field depending on whether it is in edit mode or not
     */
    private void setServings() {
        if(!editMode){
            servingsTextView.setText("Servings: " + servings);
        }else{
            servingsTextView.setText("Servings: ");
        }
    }

    /**
     * calculates total cook time and formats text for time fields
     */
    private void setCookTimes() {
        if(!editMode){
            prepTimeTextView.setText("Prep Time: " + getCookTimeString(prepTime));
            cookTimeTextView.setText("Cook Time: " + getCookTimeString(cookTime));
            totalTimeTextView.setText("Total Time: " + getCookTimeString(prepTime + cookTime));
        }else{
            prepTimeTextView.setText("Prep Time: ");
            cookTimeTextView.setText("Cook Time: ");
            totalTimeTextView.setText("Total Time: " + getCookTimeString(newPrepTime + newCookTime));
        }

    }

    /**
     * Converts minutes to a String in the format (X hours and Y minutes)
     * @param initialTime time in minutes
     * @return the new formatted time
     */
    private String getCookTimeString(int initialTime){
        String ret = "";
        int hours = initialTime / 60;
        int minutes = initialTime % 60;

        if(hours > 0){
            ret += hours;

            if(hours > 1) {
                ret += " Hours";
            }else{
                ret += " Hour";
            }
        }

        if(minutes > 0){
            if(!ret.equals("")){
                ret += " and ";
            }
            
            ret += minutes;

            if(minutes > 1) {
                ret += " Minutes";
            }else{
                ret += " Minute";
            }
        }

        return ret;
    }

    /**
     * toggles whether editable elements should be visible or not
     */
    private void toggleEditMode() {
        editMode = !editMode;

        ingredientCategoriesAdapter.setEditMode(editMode);
        instructionCategoriesAdapter.setEditMode(editMode);

        if(editMode){
            prepTimeTextView.setText("Prep Time: ");
            cookTimeTextView.setText("Cook Time: ");
            servingsTextView.setText("Servings: ");
            authorTextView.setText("Author: ");

            recipePicture.setVisibility(View.VISIBLE);
            if(image != null){
                rotateImageButton.setVisibility(View.VISIBLE);
            }else{
                rotateImageButton.setVisibility(View.GONE);
            }

            if(imageLink == null){
                imageInstructionsTextView.setVisibility(View.VISIBLE);
            }else{
                if(imageLink.equals("-1")){
                    imageInstructionsTextView.setVisibility(View.VISIBLE);
                }
                imageInstructionsTextView.setVisibility(View.GONE);
            }

            if(recipeId.equals("-1")){
                prepTimeEditText.setHint(String.valueOf(prepTime));
                cookTimeEditText.setHint(String.valueOf(cookTime));
                servingsEditText.setHint(String.valueOf(servings));
                authorEditText.setHint(author);
            }else{
                prepTimeEditText.setText(String.valueOf(prepTime));
                cookTimeEditText.setText(String.valueOf(cookTime));
                servingsEditText.setText(String.valueOf(servings));
                authorEditText.setText(author);
            }

            newRecipeName = recipeName;
            newAuthor = author;
            newCookTime = cookTime;
            newPrepTime = prepTime;

            addIngredientCategoryButton.setVisibility(View.VISIBLE);
            addInstructionCategoryButton.setVisibility(View.VISIBLE);
            addToShoppingListButton.setVisibility(View.GONE);
        }else{
            rotateImageButton.setVisibility(View.GONE);
            if(!shoppingListId.equals("-1")){
                addToShoppingListButton.setVisibility(View.VISIBLE);
            }else{
                addToShoppingListButton.setVisibility(View.GONE);
            }

            imageInstructionsTextView.setVisibility(View.GONE);

            if(imageLink == null){
                recipePicture.setVisibility(View.GONE);
            }else{
                if(imageLink.equals("-1")){
                    recipePicture.setVisibility(View.GONE);
                }
                recipePicture.setVisibility(View.VISIBLE);
            }

            addIngredientCategoryButton.setVisibility(View.GONE);
            addInstructionCategoryButton.setVisibility(View.GONE);
        }
        if(!editMode && checkUnsavedChanges()){

            // TODO: unsaved changes popup
        }


        setCookTimes();
        setServings();
        setAuthor();

        toggleTextVisibility(editMode);
    }

    /**
     * sets edit text visibility based on edit mode status
     * @param editMode current status of edit mode
     */
    private void toggleTextVisibility(boolean editMode){
        if(editMode){
            recipeTitleTextView.setVisibility(View.INVISIBLE);

            recipeTitleEditText.setVisibility(View.VISIBLE);
            prepTimeEditText.setVisibility(View.VISIBLE);
            cookTimeEditText.setVisibility(View.VISIBLE);
            servingsEditText.setVisibility(View.VISIBLE);
            authorEditText.setVisibility(View.VISIBLE);

            cookMinutesTextView.setVisibility(View.VISIBLE);
            prepMinutesTextView.setVisibility(View.VISIBLE);
        }else{
            recipeTitleTextView.setVisibility(View.VISIBLE);

            recipeTitleEditText.setVisibility(View.INVISIBLE);
            prepTimeEditText.setVisibility(View.INVISIBLE);
            cookTimeEditText.setVisibility(View.INVISIBLE);
            servingsEditText.setVisibility(View.GONE);
            authorEditText.setVisibility(View.GONE);

            cookMinutesTextView.setVisibility(View.INVISIBLE);
            prepMinutesTextView.setVisibility(View.INVISIBLE);
        }
    }

    /**
     *
     */
    private void handleSaveChanges() {

        if(newRecipeName.isEmpty()){
           newRecipeName = "new recipe";
        }

        if(checkValidSaveChanges()){
            saveChanges();
        }
    }

    /**
     * checks if there are unsaved changes
     * @return true if unsaved changes
     */
    private boolean checkUnsavedChanges() {
        if(!recipeName.equals(newRecipeName) || prepTime != newPrepTime || cookTime != newCookTime || !author.equals(newAuthor)){
            return true;
        }

        return !checkArrayListEqual(ingredientCategoriesList, backupIngredientCategoriesList);
    }

    /**
     * checks if data can be inserted safely into the database
     * @return validity of changes
     */
    private boolean checkValidSaveChanges(){
        return !newRecipeName.isEmpty() && newPrepTime >= 0 && newCookTime >= 0;
    }

    /**
     * handles inserting or updating database items
     */
    private void saveChanges(){
        recipeName = newRecipeName;

        if(newPrepTime < 0){
            newPrepTime = 0;
        }

        prepTime = newPrepTime;

        if(newCookTime < 0){
            newCookTime = 0;
        }

        cookTime = newCookTime;
        author = newAuthor;

        if(newServings < 0){
            newServings = 0;
        }

        servings = newServings;

        newRecipeName = "";
        newPrepTime = -1;
        newCookTime = -1;
        newAuthor = "";
        newServings = -1;

        String ingredientCategoriesTemp = getIngredientCategoriesAsString();
        String instructionCategoriesTemp = getInstructionCategoriesAsString();

        if(recipeId.equals("-1")){
            recipeId = db.insertRecipe(recipeName, prepTime, cookTime, author, servings, imageLink, ingredientCategoriesTemp, instructionCategoriesTemp, imageRotation);
        }else{
            db.updateRecipe(recipeId, recipeName, lastMade, prepTime, cookTime, author, servings, imageLink, ingredientCategoriesTemp, instructionCategoriesTemp, imageRotation);
        }
        saveAllIngredients();
        saveAllInstructions();

        copyArrayListIngredient(ingredientCategoriesList);
        copyArrayListInstruction(instructionCategoriesList);

        if(recipeId.equals("-1")){
            Toast.makeText(this, "Something you entered can't be submitted", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(this, "Your changes have been saved", Toast.LENGTH_SHORT).show();
        }

    }

    /**
     * iterates through ingredient categories arraylist and returns their names as a single string delineated with a [`]
     * @return categories as a delineated string
     */
    private String getIngredientCategoriesAsString() {
        String ret = "";
        for(int i = 0; i < ingredientCategoriesList.size(); i++){
            ret += ingredientCategoriesList.get(i).getName() + "`";
        }

        if(!ret.equals("")){
            ret = ret.substring(0, ret.length()-1);
        }

        return ret;
    }

    /**
     * iterates through instruction categories arraylist and returns their names as a single string delineated with a [`]
     * @return categories as a delineated string
     */
    private String getInstructionCategoriesAsString() {
        String ret = "";
        for(int i = 0; i < instructionCategoriesList.size(); i++){
            ret += instructionCategoriesList.get(i).getName() + "`";
        }

        if(!ret.equals("")){
            ret = ret.substring(0, ret.length()-1);
        }

        return ret;
    }

    /**
     * inserts or updates all ingredients to the database
     */
    private void saveAllIngredients(){
        for(int i = 0; i < ingredientCategoriesList.size(); i++){
            IngredientCategory currentCategory = ingredientCategoriesList.get(i);
            for(int j = 0; j < currentCategory.getIngredientList().size(); j++){
                Ingredient currentItem = currentCategory.getIngredientList().get(j);
                if(currentItem.getId().equals("-1")){
                    currentItem.setId(db.insertRecipeItem(currentItem.getName(), recipeId, "-1", "-1", currentCategory.getName(), j));
                }else{
                    db.updateRecipeItem(currentItem.getId(), currentItem.getName(), recipeId, currentCategory.getName(), j);
                }

                if(currentItem.getMeasurementsList() != null){
                    for(int k = 0; k < currentItem.getMeasurementsList().size(); k++){
                        Measurement currentMeasurement = currentItem.getMeasurementsList().get(k);
                        if(currentMeasurement.getId() == null || currentMeasurement.getId().equals("-1")){

                            currentMeasurement.setId(db.insertMeasurement(Table.RECIPE, currentItem.getId(), currentMeasurement.getType(), String.valueOf(currentMeasurement.getAmount())));
                        }else{
                            db.updateMeasurement(currentMeasurement.getId(), null,String.valueOf(currentMeasurement.getAmount()));
                        }
                    }
                }

                

            }
        }
    }

    /**
     * inserts or updates all instructions to the database
     */
    private void saveAllInstructions(){
        for(int i = 0; i < instructionCategoriesList.size(); i++){
            InstructionCategory currentCategory = instructionCategoriesList.get(i);
            for(int j = 0; j < currentCategory.getInstructionList().size(); j++){
                Instruction currentItem = currentCategory.getInstructionList().get(j);
                if(currentItem.getId().equals("-1")){
                    currentItem.setId(db.insertInstruction(currentItem.getText(), recipeId, currentCategory.getName(), j));
                }else{
                    db.updateInstruction(currentItem.getId(), currentItem.getText(), recipeId, currentCategory.getName(), j);
                }
            }
        }
    }

    /**
     * saves a bitmap to local storage and stores the link
     * @param bitmap
     */
    private void saveImage(Bitmap bitmap) {

        ActivityCompat.requestPermissions(RecipeActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);

        FileOutputStream out = null;
        File file = getFilesDir();
        File dir = new File(file.getAbsolutePath() + "/HouseholdHelper");

        dir.mkdirs();

        String fileName = String.format("%d.png", System.currentTimeMillis());
        File outFile = new File(dir, fileName);

        try{
            out = new FileOutputStream(outFile);
        }catch(Exception e){
            e.printStackTrace();
        }

        if(out != null){
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);

            try{
                out.flush();
            }catch(Exception e){
                e.printStackTrace();
            }
            try{
                out.close();
            }catch(Exception e){
                e.printStackTrace();
            }

            //imageLink = dir.toURI().toString() + fileName;

            imageLink = Uri.fromFile(outFile).toString();
        }
    }

    /**
     * sets the recipe image based on a given Uri
     * @param uri the link to the image
     */
    private void setImage(String uri) {
        try{
            imageLink = uri;

            image = BitmapResolver.getBitmap(getContentResolver(), Uri.parse(uri));

            if(image != null && recipePicture != null){
                recipePicture.setImageBitmap(rotateBitmap(image, imageRotation));
                imageInstructionsTextView.setVisibility(View.GONE);
                rotateImageButton.setVisibility(View.VISIBLE);
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    /**
     * rotates a bitmap to a given orientation
     * @param bitmap the bitmap to rotate
     * @param rotation the new orientation
     */
    private void rotateImage(Bitmap bitmap, int rotation) {
        recipePicture.setImageBitmap(rotateBitmap(bitmap, rotation));
    }

    /**
     * copies an arraylist to compare changes against
     * @param list1 the list to copy from
     *
     */
    private void copyArrayListIngredient(@NotNull ArrayList<IngredientCategory> list1){
        ArrayList<IngredientCategory> list2 = new ArrayList<>();
        for(int i = 0; i < list1.size(); i++){
            IngredientCategory currentCategory = list1.get(i);
            list2.add(new IngredientCategory(currentCategory.getId(), currentCategory.getName(), new ArrayList<>()));

            for(int j = 0; j < currentCategory.getIngredientList().size(); j++){
                Ingredient currentIngredient = currentCategory.getIngredientList().get(j);

                ArrayList<Measurement> measurementList = new ArrayList<>();

                for(int k = 0; k < currentIngredient.getMeasurementsList().size(); k++){
                    Measurement currentMeasurement = currentIngredient.getMeasurementsList().get(k);
                    measurementList.add(new Measurement(currentMeasurement.getId(), currentMeasurement.getTableName(), currentMeasurement.getTableId(), currentMeasurement.getType(), currentMeasurement.getAmount(), currentMeasurement.getOrder()));

                }

                list2.get(i).getIngredientList().add(new Ingredient(currentIngredient.getId(), currentIngredient.getName(), currentIngredient.getOrder(), measurementList));
            }
        }
    }

    /**
     * copies an arraylist to compare changes against
     * @param list1 the list to copy from
     *
     */
    private void copyArrayListInstruction(@NotNull ArrayList<InstructionCategory> list1){
        ArrayList<InstructionCategory> list2 = new ArrayList<>();
        for(int i = 0; i < list1.size(); i++){
            InstructionCategory currentCategory = list1.get(i);
            list2.add(new InstructionCategory(currentCategory.getId(), currentCategory.getName(), new ArrayList<>()));

            for(int j = 0; j < currentCategory.getInstructionList().size(); j++){
                Instruction currentInstruction = currentCategory.getInstructionList().get(j);
                list2.get(i).getInstructionList().add(new Instruction(currentInstruction.getId(), currentInstruction.getText(), currentInstruction.getOrder()));
            }
        }
    }

    /**
     * compares all elements in two arraylists to see if they contain the exact same data
     * @param list1 the first list to compare
     * @param list2 the second list to compare
     * @return if lists contain identical data
     */
    private boolean checkArrayListEqual(@NotNull ArrayList<IngredientCategory> list1, @NotNull ArrayList<IngredientCategory> list2){
        if(list1.size() != list2.size()){
            return false;
        }

        for(int i = 0; i < list1.size(); i++){
            IngredientCategory lhsCat = list1.get(i);
            IngredientCategory rhsCat = list2.get(i);

            if(!lhsCat.getName().equals(rhsCat.getName()) || lhsCat.getIngredientList().size() != rhsCat.getIngredientList().size()){
                return false;
            }

            for(int j = 0; j < lhsCat.getIngredientList().size(); j++){
                Ingredient lhs = lhsCat.getIngredientList().get(j);
                Ingredient rhs = rhsCat.getIngredientList().get(j);

                if(!lhs.getName().equals(rhs.getName())){
                    return false;
                }

            }
        }

        return true;
    }

    /**
     * determines whether items should be added to or removed from shopping list
     */
    public void handleAddShopListClick(){
        if(!inShopList){
            addToShoppingList();
            addToShoppingListButton.setImageDrawable(getResources().getDrawable(R.drawable.ic_baseline_local_grocery_store_48_on));
            inShopList = true;
        }else{
            removeFromShoppingList();
            addToShoppingListButton.setImageDrawable(getResources().getDrawable(R.drawable.ic_baseline_local_grocery_store_48));
            inShopList = false;
        }
    }

    /**
     * removes recipe items from shopping list
     */
    public void removeFromShoppingList(){
        db.deleteListItemByRecipeId(recipeId);
    }

    /**
     * inserts recipe items to shopping list
     */
    public void addToShoppingList(){
        ArrayList<String> itemsAlreadyInserted = new ArrayList<>();

        Cursor ret = db.getListItems(shoppingListId);
        if(ret.moveToFirst()){
            do{
                if(ret.getString(4).equals(recipeId)){
                    itemsAlreadyInserted.add(ret.getString(2));
                }
            }while(ret.moveToNext());
        }

        for(int i = 0; i < ingredientCategoriesList.size(); i++){
            for(int j = 0; j < ingredientCategoriesList.get(i).getIngredientList().size(); j++){

                boolean found = false;

                for(int k = 0; !found && k < itemsAlreadyInserted.size(); k++){
                    // dont add if the item is already in the shopping list
                    if(itemsAlreadyInserted.get(k).equals(ingredientCategoriesList.get(i).getIngredientList().get(j).getName())){
                        itemsAlreadyInserted.remove(k);
                        found = true;
                    }
                }

                if(!found){

                    String name = ingredientCategoriesList.get(i).getIngredientList().get(j).getName();
                    String measurements = "";

                    for(int l = 0; l < ingredientCategoriesList.get(i).getIngredientList().get(j).getMeasurementsList().size(); l++){
                        measurements += ingredientCategoriesList.get(i).getIngredientList().get(j).getMeasurementsList().get(l).getAmount() + " " + ingredientCategoriesList.get(i).getIngredientList().get(j).getMeasurementsList().get(l).getType() + ", ";
                    }

                    if(!measurements.isEmpty()){
                        measurements = measurements.substring(0, measurements.lastIndexOf(","));
                        name += " - " + measurements;
                    }

                    db.insertListItem(shoppingListId, name, recipeId);
                }

            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.recipe_options_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if(checkUnsavedChanges()){
                    // TODO: unsaved changes popup
                    handleSaveChanges();
                }
                finish();
                return true;
            case R.id.deleteRecipeOption:
                if(recipeId != "-1"){
                    db.deleteRecipeById(recipeId);
                }
                finish();
                return true;
            case R.id.editRecipeOption:
                if(editMode && checkUnsavedChanges()){
                    handleSaveChanges();
                }
                toggleEditMode();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent){
        super.onActivityResult(requestCode, resultCode, intent);

        if(requestCode == PICK_IMAGE && resultCode == RESULT_OK){

            try {
                Uri uri = intent.getData();
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                saveImage(bitmap);
                setImage(imageLink);

            }catch(Exception e){
                e.printStackTrace();
            }
        }
    }

    /**
     * rotates a bitmap
     * @param bitmap the bitmap to rotate
     * @param degrees the new orientation
     * @return the rotated bitmap
     */
    public static Bitmap rotateBitmap(Bitmap bitmap, int degrees) {
        Matrix matrix = new Matrix();
        matrix.postRotate(degrees);
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }

    @Override
    public void onBackPressed(){
        if(checkUnsavedChanges()){
            handleSaveChanges();
        }
        finish();
    }

    @Override
    public void deleteSwipe(String id) {
    }

    /**
     * Chooses the theme based on the one selected in sharedpreferences, or default if none is selected
     * @return the selected theme
     */
    @Override
    public Resources.Theme getTheme(){
        Resources.Theme theme = super.getTheme();

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String themeName = preferences.getString("theme", "Default");

        switch(themeName){
            case "Default":
                //editor.putString();
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

