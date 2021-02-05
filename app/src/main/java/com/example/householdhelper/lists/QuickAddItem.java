package com.example.householdhelper.lists;

public class QuickAddItem {
    private String id;
    private String name;

    public QuickAddItem(){

    }

    public QuickAddItem(String id, String name){
        this.id = id;
        this.name = name;
    }

    public String getName(){
        return name;
    }

    public String getId(){
        return id;
    }

    public void setId(String id){
        this.id = id;
    }

    public void setName(String name){
        this.name = name;
    }
}
