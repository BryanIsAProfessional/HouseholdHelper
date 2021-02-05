package com.example.householdhelper.recipes;

import android.net.Uri;

public class Recipe {
    private String id;
    private String name;
    private String dateCreated;
    private String lastModified;
    private String author;
    private Uri imageLink;
    private int cookTime;
    private int servings;
    private int imageRotation;

    public Recipe(){

    }

    public Recipe(String id, String name, String dateCreated, String lastMade, int cookTime, int servings, String imageLink, String author, int imageRotation){
        this.id = id;
        this.name = name;
        this.dateCreated = dateCreated;
        this.lastModified = lastMade;
        this.cookTime = cookTime;
        this.servings = servings;
        this.author = author;
        this.imageRotation = imageRotation;
        try{
            this.imageLink = Uri.parse(imageLink);
        }catch(NullPointerException e){
            this.imageLink = null;
        }

    }

    public String getId(){
        return id;
    }

    public String getName(){
        return name;
    }

    public String getDateCreated(){
        return dateCreated;
    }

    public String getLastMade(){
        return lastModified;
    }

    public Uri getUri(){
        return imageLink;
    }

    public int getCookTimeInMinutes(){
        return cookTime;
    }

    public int getServings(){
        return servings;
    }

    public void setId(String i){
        id = i;
    }

    public void setName(String n){
        name = n;
    }

    public void setDateCreated(String d){
        dateCreated = d;
    }

    public void setLastMade(String l){
        lastModified = l;
    }

    public void setUri(String imageLink){
        this.imageLink = Uri.parse(imageLink);
    }

    public void setCookTime(int cookTime){
        this.cookTime = cookTime;
    }

    public void setServings(int servings){
        this.servings = servings;
    }

    public void setAuthor(String author){
        this.author = author;
    }

    public String getAuthor() {
        return this.author;
    }

    public int getImageRotation() {
        return imageRotation;
    }

    public void setImageRotation(int imageRotation) {
        this.imageRotation = imageRotation;
    }
}