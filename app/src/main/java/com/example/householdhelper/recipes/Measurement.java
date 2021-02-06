package com.example.householdhelper.recipes;

import com.example.householdhelper.helpers.Table;

/**
 * Struct-like class to store data about measurements
 *
 * @author Bryan Burdick
 * @version 1.0
 * @since 2021-02-06
 */
public class Measurement implements Comparable<Measurement> {
    private String id;
    private String type;
    private String amount;
    private int order;
    private String tableName;
    private String tableId;

    /**
     * default constructor
     */
    public Measurement(){
        this.id = "-1";
        this.type = "";
        this.amount = "";
        this.order = -1;
        this.tableName = String.valueOf(Table.RECIPE);
        this.tableId = "-1";
    }

    /**
     * construct with initial values
     * @param id the database id
     * @param tableName the table name containing the item associated with this measurement
     * @param tableId the id of the item associated with this measurement
     * @param type the type of measurement (e.g. ounce)
     * @param amount the quantity of measurement
     * @param order the index this object was saved in
     */
    public Measurement(String id, String tableName, String tableId, String type, String amount, int order){
        this.id = id;
        this.type = type;
        this.amount = amount;
        this.order = order;
        this.tableName = tableName;
        this.tableId = tableId;
    }

    /**
     * Returns the id of the item used in the SQLite database
     * @return item id
     */
    public String getId(){
        return id;
    }

    /**
     * Returns the table name containing the item this measurement is associated with
     * @return the table name containing the item this measurement is associated with
     */
    public String getTableName(){
        return tableName;
    }

    /**
     * Returns the table id of the item this measurement is associated with
     * @return the table id of the item this measurement is associated with
     */
    public String getTableId(){
        return tableId;
    }

    /**
     * Returns the type of measurement (e.g. ounce)
     * @return measurement type
     */
    public String getType(){
        return type;
    }

    /**
     * Returns the quantity of measurement
     * @return measurement quantity
     */
    public String getAmount(){
        return amount;
    }

    /**
     * Returns the object's desired index
     * @return the index this object was saved in
     */
    public int getOrder() { return order; }

    public void setAmount(String amount){
        this.amount = amount;
    }

    /**
     * Sets the id of the item used in the SQLite database
     * @param id item id
     */
    public void setId(String id){
        this.id = id;
    }

    /**
     * Sets the table name containing the item this measurement is associated with
     * @param tableName the new table name
     */
    public void setTableName(String tableName){
        this.tableName = tableName;
    }

    /**
     * Sets the id of the item this measurement is associated with
     * @param tableId the new table name
     */
    public void setTableId(String tableId){
        this.tableId = tableId;
    }

    /**
     * Sets the type of measurement (e.g. ounce)
     * @param type new measurement type
     */
    public void setType(String type){
        this.type = type;
    }

    /**
     * Set the index this object should be saved with
     * @param order the index this object should be saved with
     */
    public void setOrder(int order){
        this.order = order;
    }

    @Override
    public String toString(){

        return "{ " + id + ", " + amount + ", " + type + " }";
    }

    @Override
    public int compareTo(Measurement order) {
        return this.order > order.getOrder() ? 1 : (this.order == order.getOrder() ? 0 : -1);
    }
}