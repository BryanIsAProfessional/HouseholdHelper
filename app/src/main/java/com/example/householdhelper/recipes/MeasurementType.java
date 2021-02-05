package com.example.householdhelper.recipes;

public class MeasurementType implements Comparable<MeasurementType> {
    private String id;
    private String name;
    private int order;

    public MeasurementType(){

    }

    public MeasurementType(String id, String name, int order){
        this.id = id;
        this.name = name;
        this.order = order;
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

    @Override
    public String toString(){

        return "{ " + id + ", " + name + " }";
    }

    @Override
    public int compareTo(MeasurementType order) {
        return this.order > order.getOrder() ? 1 : (this.order == order.getOrder() ? 0 : -1);
    }
}