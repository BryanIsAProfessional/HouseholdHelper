package com.example.householdhelper.lists;

/**
 * Struct-like class to store data about Shopping Lists
 *
 * @author Bryan Burdick
 * @version 1.0
 * @since 2021-02-06
 */
public class List{
    public static final String TAG = "List";
    private String id;
    private String name;
    private String dateCreated;
    private String lastModified;
    private int crossedItems, totalItems;

    /**
     * empty constructor
     */
    public List(){

    }

    /**
     * construct with initial values
     * @param id the database listId
     * @param name the name of the list
     * @param dateCreated the date the list was initially created
     * @param lastModified the last time this list was modified
     * @param crossedItems the number of items in the list that have been checked
     * @param totalItems the total number of items in the list
     */
    public List(String id, String name, String dateCreated, String lastModified, int crossedItems, int totalItems){
        this.id = id;
        this.name = name;
        this.dateCreated = dateCreated;
        this.lastModified = lastModified;
        this.crossedItems = crossedItems;
        this.totalItems = totalItems;
    }

    /**
     * Returns the id of the list used in the SQLite database
     * @return list id
     */
    public String getId(){
        return this.id;
    }

    /**
     * Returns the name of the list
     * @return list name
     */
    public String getName(){
        return this.name;
    }

    /**
     * Returns the date created as a String
     * @return date created
     */
    public String getDateCreated(){
        return this.dateCreated;
    }

    /**
     * Returns the date the list was last modified as a String
     * @return last modified
     */
    public String getLastModified(){
        return this.lastModified;
    }

    /**
     * Returns the total number of items in the list
     * @return total items in the list
     */
    public int getListItemCount(){
        return totalItems;
    }

    /**
     * Returns the number of unchecked items in the list
     * @return the number of unchecked items in the list
     */
    public int getUncrossedItemCount(){
        return totalItems-crossedItems;
    }

    /**
     * Returns the number of checked items in the list
     * @return the number of checked items in the list
     */
    public int getCrossedItemCount(){
        return crossedItems;
    }

    /**
     * Returns the percentage of checked items as an integer
     * @return percentage of checked items
     */
    public int getCompletionPercent(){
        double temp = 100.0 * (1.0 * crossedItems) / totalItems ;
        return (int)temp;
    }

    /**
     * Sets the listId for use in the SQLite database
     * @param id list id for database
     */
    public void setId(String id){
        this.id = id;
    }

    /**
     * Sets the name of the list
     * @param name list name
     */
    public void setName(String name){
        this.name = name;
    }

    /**
     * Sets the date this list was created
     * @param dateCreated the date this list was created
     */
    public void setDateCreated(String dateCreated){
        this.dateCreated = dateCreated;
    }

    /**
     * Sets the date this list was last modified
     * @param lastModified the date this list was last modified
     */
    public void setLastModified(String lastModified){
        this.lastModified = lastModified;
    }
}