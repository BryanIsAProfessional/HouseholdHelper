package com.example.householdhelper.recipes;

/**
 * Struct-like class to store data about instructions
 *
 * @author Bryan Burdick
 * @version 1.0
 * @since 2021-02-06
 */
public class Instruction implements Comparable<Instruction> {
    private String id;
    private String text;
    private int order;

    /**
     * empty constructor
     */
    public Instruction(){

    }

    /**
     * construct with initial values
     * @param id the database id
     * @param text the name of the item
     * @param order the index this object was saved in
     */
    public Instruction(String id, String text, int order){
        this.id = id;
        this.text = text;
        this.order = order;
    }

    /**
     * Returns the id of the item used in the SQLite database
     * @return item id
     */
    public String getId(){
        return id;
    }

    /**
     * Returns the instruction's text
     * @return the instruction's text
     */
    public String getText(){
        return text;
    }

    /**
     * Returns the object's desired index
     * @return the index this object was saved in
     */
    public int getOrder(){
        return order;
    }

    /**
     * Set the index this object should be saved with
     * @param order the index this object should be saved with
     */
    public void setOrder(int order){
        this.order = order;
    }

    /**
     * Sets the id of the item used in the SQLite database
     * @param id item id
     */
    public void setId(String id){
        this.id = id;
    }

    /**
     * Sets the instruction's text
     * @param name instruction's new text
     */
    public void setText(String name){
        this.text = name;
    }

    @Override
    public String toString(){

        return "{ " + id + ", " + text + " }";
    }

    @Override
    public int compareTo(Instruction order) {
        return this.order > order.getOrder() ? 1 : (this.order == order.getOrder() ? 0 : -1);
    }
}