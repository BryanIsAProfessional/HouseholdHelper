package com.example.householdhelper.lists;

import android.util.Log;

public class List{
    public static final String TAG = "List";
    private String id;
    private String name;
    private String dateCreated;
    private String lastModified;
    private int crossedItems, totalItems;

    public List(){

    }

    public List(String id, String name, String dateCreated, String lastModified, int crossedItems, int totalItems){
        this.id = id;
        this.name = name;
        this.dateCreated = dateCreated;
        this.lastModified = lastModified;
        this.crossedItems = crossedItems;
        this.totalItems = totalItems;
    }

    public String getid(){
        return this.id;
    }

    public String getName(){
        return this.name;
    }

    public String getDateCreated(){
        return this.dateCreated;
    }

    public String getLastModified(){
        return this.lastModified;
    }

    public int getListItemCount(){
        return totalItems;
    }

    public int getUncrossedItemCount(){
        return totalItems-crossedItems;
    }

    public int getCrossedItemCount(){
        return crossedItems;
    }

    public int getCompletionPercent(){
        double temp = 100.0 * (1.0 * crossedItems) / totalItems ;
        return (int)temp;
    }

    public void setId(String id){
        this.id = id;
    }

    public void setName(String name){
        this.name = name;
    }

    public void setDateCreated(String dateCreated){
        this.dateCreated = dateCreated;
    }

    public void setLastModified(String lastModified){
        this.lastModified = lastModified;
    }
}