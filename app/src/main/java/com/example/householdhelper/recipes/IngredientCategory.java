package com.example.householdhelper.recipes;

import java.util.ArrayList;

public class IngredientCategory {
    private String id;
    private String name;
    private ArrayList<Ingredient> ingredientList = new ArrayList<>();

    public IngredientCategory(){

    }

    public IngredientCategory(String id, String name, ArrayList<Ingredient> ingredientList){
        this.id = id;
        this.name = name;
        this.ingredientList = ingredientList;
    }

    public String getId(){
        return id;
    }

    public String getName(){
        return name;
    }

    public void setId(String id){
        this.id = id;
    }

    public void setName(String name){
        this.name = name;
    }

    public ArrayList<Ingredient> getIngredientList(){ return ingredientList; }
}