package com.example.householdhelper.recipes;

import java.util.ArrayList;

public class InstructionCategory {
    private String id;
    private String name;
    private ArrayList<Instruction> instructionList = new ArrayList<>();

    public InstructionCategory(){

    }

    public InstructionCategory(String id, String name, ArrayList<Instruction> instructionList){
        this.id = id;
        this.name = name;
        this.instructionList = instructionList;
    }

    public String getId(){
        return id;
    }

    public String getName(){
        return name;
    }

    public void setId(String id){
        this.id = id;
    }

    public void setName(String name){
        this.name = name;
    }

    public ArrayList<Instruction> getInstructionList(){ return instructionList; }
}