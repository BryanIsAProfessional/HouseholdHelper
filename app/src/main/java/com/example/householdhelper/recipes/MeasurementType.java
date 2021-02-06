package com.example.householdhelper.recipes;

/**
 * Struct-like class to store data about a type of measurement
 *
 * @author Bryan Burdick
 * @version 1.0
 * @since 2021-02-06
 */
public class MeasurementType implements Comparable<MeasurementType> {
    private String id;
    private String name;
    private int order;

    /**
     * empty constructor
     */
    public MeasurementType(){

    }

    /**
     * construct with initial values
     * @param id database id
     * @param name item name
     * @param order index this item should be saved/restored on
     */
    public MeasurementType(String id, String name, int order){
        this.id = id;
        this.name = name;
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
     * Returns the name of the item
     * @return the name of the item
     */
    public String getName(){
        return name;
    }

    /**
     * Returns the index this ingredient was saved with
     * @return ingredient's desired index
     */
    public int getOrder(){
        return order;
    }

    /**
     * Sets the order of the item
     * @param order ingredient's desired index when it is retrieved from the database
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
     * Sets the name of the list item
     * @param name item name
     */
    public void setName(String name){
        this.name = name;
    }

    @Override
    public String toString(){

        return "{ " + id + ", " + name + " }";
    }

    @Override
    public int compareTo(MeasurementType order) {
        return this.order > order.getOrder() ? 1 : (this.order == order.getOrder() ? 0 : -1);
    }
}