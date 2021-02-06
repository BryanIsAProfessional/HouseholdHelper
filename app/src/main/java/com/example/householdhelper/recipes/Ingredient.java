package com.example.householdhelper.recipes;

import java.util.ArrayList;

/**
 * Struct-like class to store data about ingredients
 *
 * @author Bryan Burdick
 * @version 1.0
 * @since 2021-02-06
 */
public class Ingredient implements Comparable<Ingredient> {
    private String id;
    private String name;
    private int order;
    private ArrayList<Measurement> measurementsList;

    /**
     * empty constructor
     */
    public Ingredient(){

    }

    /**
     * construct with intial values
     * @param id the database id
     * @param name the name of the item
     * @param order saved index of the item
     * @param measurementsList ArrayList of measurements associated with this ingredient
     */
    public Ingredient(String id, String name, int order, ArrayList<Measurement> measurementsList){
        this.id = id;
        this.name = name;
        this.order = order;
        this.measurementsList = measurementsList;
    }

    /**
     * get the ArrayList of measurements for this ingredient
     * @return ArrayList of Measurement objects
     */
    public ArrayList<Measurement> getMeasurementsList(){
        return measurementsList;
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

    /**
     * Sets the ArrayList of associated measurements
     * @param measurementsList ArrayList of measurements associated with this ingredient
     */
    public void setMeasurementsList(ArrayList<Measurement> measurementsList){
        this.measurementsList = measurementsList;
    }

    @Override
    public String toString(){

        return "{ " + id + ", " + name + " }";
    }

    @Override
    public int compareTo(Ingredient order) {
        return this.order > order.getOrder() ? 1 : (this.order == order.getOrder() ? 0 : -1);
    }
}