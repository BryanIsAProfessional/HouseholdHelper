package com.example.householdhelper.lists;

/**
 * Struct-like class to store data about quick add items
 *
 * @author Bryan Burdick
 * @version 1.0
 * @since 2021-02-06
 */
public class QuickAddItem {
    private String id;
    private String name;

    /**
     * empty constructor
     */
    public QuickAddItem(){

    }

    /**
     * construct with initial values
     * @param id the database id
     * @param name the name of the item
     */
    public QuickAddItem(String id, String name){
        this.id = id;
        this.name = name;
    }

    /**
     * Returns the name of the item
     * @return the name of the item
     */
    public String getName(){
        return name;
    }

    /**
     * Returns the id of the item used in the SQLite database
     * @return item id
     */
    public String getId(){
        return id;
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
}
