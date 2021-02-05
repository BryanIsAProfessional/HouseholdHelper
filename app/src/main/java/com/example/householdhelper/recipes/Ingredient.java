package com.example.householdhelper.recipes;

import java.util.ArrayList;

public class Ingredient implements Comparable<Ingredient> {
    private String id;
    private String name;
    private int order;
    private ArrayList<Measurement> measurementsList;

    public Ingredient(){

    }

    public Ingredient(String id, String name, int order, ArrayList<Measurement> measurementsList){
        this.id = id;
        this.name = name;
        this.order = order;
        this.measurementsList = measurementsList;
    }

    public ArrayList<Measurement> getMeasurementsList(){
        return measurementsList;
    }

    public String getId(){
        return id;
    }

    public String getName(){
        return name;
    }

    public int getOrder(){
        return order;
    }

    public void setOrder(int order){
        this.order = order;
    }

    public void setId(String id){
        this.id = id;
    }

    public void setName(String name){
        this.name = name;
    }

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