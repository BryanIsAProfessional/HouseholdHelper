package com.example.householdhelper.lists;

public class ListItem implements Comparable<ListItem>{
    private String id;
    private String name;
    private boolean isChecked;

    public ListItem(){

    }

    public ListItem(String id, String name, int state){
        this.id = id;
        this.name = name;
        isChecked = state == 1;
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

    @Override
    public int compareTo(ListItem o) {
        return (o.isChecked() == isChecked) ? 0 : (isChecked ? 1 : -1);
    }

    @Override
    public String toString() {
        return "ListItem{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", isChecked=" + isChecked +
                '}';
    }
}
