package com.example.householdhelper.recipes;

import android.net.Uri;

/**
 * Struct-like class to store data about recipes
 *
 * @author Bryan Burdick
 * @version 1.0
 * @since 2021-02-06
 */
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

    /**
     * empty constructor
     */
    public Recipe(){

    }

    /**
     * construct with initial values
     * @param id database id
     * @param name recipe name
     * @param dateCreated date created
     * @param lastMade date this recipe was made last
     * @param cookTime time in minutes to cook
     * @param servings servings
     * @param imageLink link to image in memory
     * @param author name of author
     * @param imageRotation orientation for image
     */
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
     * Returns the date this recipe was added
     * @return the date this recipe was added
     */
    public String getDateCreated(){
        return dateCreated;
    }

    /**
     * Returns the date this recipe was made modified
     * @return the date this recipe was made modified
     */
    public String getLastMade(){
        return lastModified;
    }

    /**
     * Returns the Uri of the recipe image
      * @return the Uri of the recipe image
     */    
    public Uri getUri(){
        return imageLink;
    }

    /**
     * Returns the cook time in minutes
     * @return the recipe cook time in minutes
     */
    public int getCookTimeInMinutes(){
        return cookTime;
    }

    /**
     * Returns the number of servings this recipe makes
     * @return number of servings
     */
    public int getServings(){
        return servings;
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
     * Sets the date this recipe was added
     * @param dateCreated the date this recipe was added
     */
    public void setDateCreated(String dateCreated){
        this.dateCreated = dateCreated;
    }

    /**
     * Sets the date this recipe was last changed
     * @param lastModified the date this recipe was last changed
     */
    public void setLastModified(String lastModified){
        this.lastModified = lastModified;
    }

    /**
     * Sets the link to the recipe image
     * @param imageLink the link to the recipe image
     */
    public void setUri(String imageLink){
        this.imageLink = Uri.parse(imageLink);
    }

    /**
     * sets the cook time of the recipe in minutes
     * @param cookTime the cook time of the recipe in minutes
     */
    public void setCookTime(int cookTime){
        this.cookTime = cookTime;
    }

    /**
     * Sets the number of servings this recipe makes
     * @param servings the number of servings this recipe makes
     */
    public void setServings(int servings){
        this.servings = servings;
    }

    /**
     * Sets the author of this recipe
     * @param author the author of this recipe
     */
    public void setAuthor(String author){
        this.author = author;
    }

    /**
     * Returns the author of this recipe
     * @return the author of this recipe
     */
    public String getAuthor() {
        return this.author;
    }

    /**
     * Returns the correct orientation for the recipe image
     * @return the orientation for the recipe image
     */
    public int getImageRotation() {
        return imageRotation;
    }

    /**
     * Sets the correct orientation for the recipe image
     * @param imageRotation the correct orientation for the recipe image
     */
    public void setImageRotation(int imageRotation) {
        this.imageRotation = imageRotation;
    }
}