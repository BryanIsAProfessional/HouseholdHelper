package com.example.householdhelper;

public class ListItem {
    private String id;
    private String name;
    private boolean isChecked;

    public ListItem(){

    }

    public ListItem(String id, String name, int state){
        this.id = id;
        this.name = name;
        isChecked = (state == 1) ? true : false;
    }

    public String getName(){
        return name;
    }

    public String getId(){
        return id;
    }

    public boolean isChecked(){
        return isChecked;
    }

    public void setId(String id){
        this.id = id;
    }

    public void setName(String name){
        this.name = name;
    }

    public void setIsChecked(boolean isChecked){
        this.isChecked = isChecked;
    }
}
