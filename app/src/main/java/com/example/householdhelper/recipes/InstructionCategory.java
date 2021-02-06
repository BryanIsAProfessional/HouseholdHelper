package com.example.householdhelper.recipes;

import java.util.ArrayList;

/**
 * Struct-like class to store data about instruction categories
 *
 * @author Bryan Burdick
 * @version 1.0
 * @since 2021-02-06
 */
public class InstructionCategory {
    private String id;
    private String name;
    private ArrayList<Instruction> instructionList = new ArrayList<>();

    /**
     * empty constructor
     */
    public InstructionCategory(){

    }

    /**
     * construct with initial values
     * @param id database id
     * @param name category name
     * @param instructionList ArrayList of Instructions
     */
    public InstructionCategory(String id, String name, ArrayList<Instruction> instructionList){
        this.id = id;
        this.name = name;
        this.instructionList = instructionList;
    }

    /**
     * Returns the id of the item used in the SQLite database
     * @return item id
     */
    public String getId(){
        return id;
    }

    /**
     * Returns the name of the item
     * @return the name of the item
     */
    public String getName(){
        return name;
    }

    /**
     * Sets the id of the item used in the SQLite database
     * @param id item id
     */
    public void setId(String id){
        this.id = id;
    }

    /**
     * Sets the name of the list item
     * @param name item name
     */
    public void setName(String name){
        this.name = name;
    }

    public ArrayList<Instruction> getInstructionList(){ return instructionList; }
}