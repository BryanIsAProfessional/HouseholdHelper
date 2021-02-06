package com.example.householdhelper.lists;

/**
 * Struct-like class to store data about list items
 *
 * @author Bryan Burdick
 * @version 1.0
 * @since 2021-02-06
 */
public class ListItem implements Comparable<ListItem>{
    private String id;
    private String name;
    private boolean isChecked;

    /**
     * empty constructor
     */
    public ListItem(){

    }

    /**
     * construct with initial values
     * @param id the database listId
     * @param name the name of the list
     * @param state the state of the list item (checked|unchecked) as an integer (0:1)
     */
    public ListItem(String id, String name, int state){
        this.id = id;
        this.name = name;
        isChecked = state == 1;
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
     * Returns true if the item is checked
     * @return is checked
     */
    public boolean isChecked(){
        return isChecked;
    }

    /**
     * Sets the id of the list item used in the SQLite database
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
     * Sets the checked state of the item
     * @param isChecked item's new checked state
     */
    public void setIsChecked(boolean isChecked){
        this.isChecked = isChecked;
    }

    /**
     * sorts checked items to the bottom of the list (for use with Comparable)
     * @param o the ListItem to be compared against this one
     * @return 0 if both items are the same state. 1 if this item should be sorted below item o, and -1 if this should be sorted above.
     */
    @Override
    public int compareTo(ListItem o) {
        return (o.isChecked() == isChecked) ? 0 : (isChecked ? 1 : -1);
    }
}
