package com.example.householdhelper.recipes;

import java.util.ArrayList;

/**
 * Struct-like class to store data about quick add items
 *
 * @author Bryan Burdick
 * @version 1.0
 * @since 2021-02-06
 */
public class IngredientCategory {
    private String id;
    private String name;
    private ArrayList<Ingredient> ingredientList = new ArrayList<>();

    /**
     * empty constructor
     */
    public IngredientCategory(){

    }

    /**
     * construct with initial values
     * @param id the database id
     * @param name the name of the item
     * @param ingredientList ArrayList of Ingredients
     */
    public IngredientCategory(String id, String name, ArrayList<Ingredient> ingredientList){
        this.id = id;
        this.name = name;
        this.ingredientList = ingredientList;
    }

    /**
     * Returns the id of the item used in the SQLite database
     * @return item id
     */
    public String getId(){
        return id;
    }

    /**
     * Returns the name of the item
     * @return the name of the item
     */
    public String getName(){
        return name;
    }

    /**
     * Sets the id of the item used in the SQLite database
     * @param id item id
     */
    public void setId(String id){
        this.id = id;
    }

    /**
     * Sets the name of the list item
     * @param name item name
     */
    public void setName(String name){
        this.name = name;
    }

    /**
     * Returns an ArrayList of Ingredients in the category
     * @return ArrayList of Ingredients stored in this object
     */
    public ArrayList<Ingredient> getIngredientList(){ return ingredientList; }
}