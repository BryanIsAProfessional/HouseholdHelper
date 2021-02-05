package com.example.householdhelper.schedule;

public class Medicine implements Comparable<Medicine> {
    private String id;
    private String name;
    private int daysBefore;

    public void setName(String name) {
        this.name = name;
    }

    public void setRemaining(int remaining) {
        this.remaining = remaining;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public void setHoursBetween(int hoursBetween) {
        this.hoursBetween = hoursBetween;
    }

    public void setAutomatic(boolean automatic) {
        this.automatic = automatic;
    }

    private int remaining;
    private int total;
    private int hoursBetween;
    private boolean automatic;
    private String lastChanged;
    private String notifyAt;
    private int order;

    public Medicine(){

    }

    public Medicine(String id, String name, int remaining, int total, int hoursBetween, boolean automatic, String lastChanged, String notifyAt, int order, int daysBefore){
        this.id = id;
        this.name = name;
        this.order = order;
        this.remaining = remaining;
        this.total = total;
        this.hoursBetween = hoursBetween;
        this.automatic = automatic;
        this.lastChanged = lastChanged;
        this.notifyAt = notifyAt;
        this.daysBefore = daysBefore;
    }

    public String getId(){
        return id;
    }

    public String getName(){
        return name;
    }

    public int getRemaining(){ return remaining;}

    public int getTotal(){return total;}

    public int getHoursBetween(){ return hoursBetween;}

    public boolean getAutomatic(){return automatic;}

    public int getOrder(){
        return order;
    }

    public void setOrder(int order){
        this.order = order;
    }

    public void setId(String id){
        this.id = id;
    }

    @Override
    public int compareTo(Medicine order) {
        return this.order > order.getOrder() ? 1 : (this.order == order.getOrder() ? 0 : -1);
    }

    public String getLastChanged() {
        return lastChanged;
    }

    public void setLastChanged(String lastChanged) {
        this.lastChanged = lastChanged;
    }

    public String getNotifyAt() {
        return notifyAt;
    }

    public void setNotifyAt(String notifyAt) {
        this.notifyAt = notifyAt;
    }

    @Override
    public String toString() {
        return "Medicine{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", remaining=" + remaining +
                ", total=" + total +
                ", hoursBetween=" + hoursBetween +
                ", automatic=" + automatic +
                ", lastChanged='" + lastChanged + '\'' +
                ", notifyAt='" + notifyAt + '\'' +
                ", order=" + order +
                ", daysBefore='" + daysBefore + '\'' +
                '}';
    }

    public int getDaysBefore() {
        return daysBefore;
    }

    public void setDaysBefore(int daysBefore) {
        this.daysBefore = daysBefore;
    }
}