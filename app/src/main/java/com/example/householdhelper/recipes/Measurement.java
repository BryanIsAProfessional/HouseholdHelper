package com.example.householdhelper.recipes;

import com.example.householdhelper.helpers.Table;

public class Measurement implements Comparable<Measurement> {
    private String id;
    private String type;
    private String amount;
    private int order;
    private String tableName;
    private String tableId;

    public Measurement(){
        this.id = "-1";
        this.type = "";
        this.amount = "";
        this.order = -1;
        this.tableName = String.valueOf(Table.RECIPE);
        this.tableId = "-1";
    }

    public Measurement(String id, String tableName, String tableId, String type, String amount, int order){
        this.id = id;
        this.type = type;
        this.amount = amount;
        this.order = order;
        this.tableName = tableName;
        this.tableId = tableId;
    }

    public String getId(){
        return id;
    }

    public String getTableName(){
        return tableName;
    }

    public String getTableId(){
        return tableId;
    }

    public String getType(){
        return type;
    }

    public String getAmount(){
        return amount;
    }

    public int getOrder() { return order; }

    public void setAmount(String amount){
        this.amount = amount;
    }

    public void setId(String id){
        this.id = id;
    }

    public void setTableName(String tableName){
        this.tableName = tableName;
    }

    public void setTableId(String tableId){
        this.tableId = tableId;
    }

    public void setName(String type){
        this.type = type;
    }

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