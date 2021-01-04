package com.example.householdhelper;

public class List{
    private String id;
    private String name;
    private String dateCreated;
    private String lastModified;

    public List(){

    }

    public List(String i, String n, String d, String l){
        id = i;
        name = n;
        dateCreated = d;
        lastModified = l;
    }

    public String getid(){
        return id;
    }

    public String getName(){
        return name;
    }

    public String getDateCreated(){
        return dateCreated;
    }

    public String getLastModified(){
        return lastModified;
    }

    public void setId(String i){
        id = i;
    }

    public void setName(String n){
        name = n;
    }

    public void setDateCreated(String d){
        dateCreated = d;
    }

    public void setLastModified(String l){
        lastModified = l;
    }
}