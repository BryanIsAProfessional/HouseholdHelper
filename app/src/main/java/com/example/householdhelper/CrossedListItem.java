package com.example.householdhelper;

public class CrossedListItem {
    private String id;
    private String name;

    public CrossedListItem(){

    }

    public CrossedListItem(String id, String name){
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
