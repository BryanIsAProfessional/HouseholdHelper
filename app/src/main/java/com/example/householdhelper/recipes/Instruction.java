package com.example.householdhelper.recipes;

public class Instruction implements Comparable<Instruction> {
    private String id;
    private String text;
    private int order;

    public Instruction(){

    }

    public Instruction(String id, String text, int order){
        this.id = id;
        this.text = text;
        this.order = order;
    }

    public String getId(){
        return id;
    }

    public String getText(){
        return text;
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

    public void setText(String name){
        this.text = name;
    }

    @Override
    public String toString(){

        return "{ " + id + ", " + text + " }";
    }

    @Override
    public int compareTo(Instruction order) {
        return this.order > order.getOrder() ? 1 : (this.order == order.getOrder() ? 0 : -1);
    }
}