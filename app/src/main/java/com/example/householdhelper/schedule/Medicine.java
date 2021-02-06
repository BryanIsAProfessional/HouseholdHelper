package com.example.householdhelper.schedule;

/**
 * Struct-like class to store data about medicines
 *
 * @author Bryan Burdick
 * @version 1.0
 * @since 2021-02-06
 */
public class Medicine implements Comparable<Medicine> {
    private String id;
    private String name;
    private int daysBefore;

    private int remaining;
    private int total;
    private int hoursBetween;
    private boolean automatic;
    private String lastChanged;
    private String notifyAt;
    private int order;

    /**
     * empty constructor
     */
    public Medicine(){

    }

    /**
     * construct with initial values
     * @param id database id
     * @param name name of the item
     * @param remaining doses remaining
     * @param total total doses in a full prescription
     * @param hoursBetween hours between doses
     * @param automatic is prescription tracked automatically
     * @param lastChanged date prescription was last changed
     * @param notifyAt time of day to schedule notifications
     * @param order order of med tracker in adapter
     * @param daysBefore days before to schedule notification
     */
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
     * Returns the number of doses remaining
     * @return the number of doses remaining
     */
    public int getRemaining(){ return remaining;}

    /**
     * Returns the total number of doses
     * @return the total number of doses
     */
    public int getTotal(){return total;}

    /**
     * Returns the number of hours between doses
     * @return the number of hours between doses
     */
    public int getHoursBetween(){ return hoursBetween;}

    /**
     * Returns if the medication is tracked automatically
     * @return if the medication is tracked automatically
     */
    public boolean getAutomatic(){return automatic;}

    /**
     * Returns the order of this item
     * @return the order of this item
     */
    public int getOrder(){
        return order;
    }

    /**
     * Returns the number of days in advance to notify if the prescription is empty
     * @return the number of days in advance to notify if the prescription is empty
     */
    public int getDaysBefore() {
        return daysBefore;
    }

    /**
     * Sets the number of days in advance to notify if the prescription is empty
     * @param daysBefore the number of days in advance to notify if the prescription is empty
     */
    public void setDaysBefore(int daysBefore) {
        this.daysBefore = daysBefore;
    }

    /**
     * Sets the order of the item
     * @param order the order of the item
     */
    public void setOrder(int order){
        this.order = order;
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
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Sets the number of doses remaining
     * @param remaining the number of doses remaining
     */
    public void setRemaining(int remaining) {
        this.remaining = remaining;
    }

    /**
     * Sets the total number of doses in a full prescription
     * @param total the total number of doses in a full prescription
     */
    public void setTotal(int total) {
        this.total = total;
    }

    /**
     * Sets the number of hours between doses
     * @param hoursBetween the number of hours between doses
     */
    public void setHoursBetween(int hoursBetween) {
        this.hoursBetween = hoursBetween;
    }

    /**
     * Sets if the prescription is tracked automatically
     * @param automatic automatic tracking
     */
    public void setAutomatic(boolean automatic) {
        this.automatic = automatic;
    }

    @Override
    public int compareTo(Medicine order) {
        return this.order > order.getOrder() ? 1 : (this.order == order.getOrder() ? 0 : -1);
    }

    /**
     * Returns the time this medication was last changed
     * @return the time this medication was last changed
     */
    public String getLastChanged() {
        return lastChanged;
    }

    /**
     * Sets the time this medication was last changed
     * @param lastChanged the time this medication was last changed
     */
    public void setLastChanged(String lastChanged) {
        this.lastChanged = lastChanged;
    }

    /**
     * Returns the time of day to schedule notifications at
     * @return the time of day to schedule notifications at
     */
    public String getNotifyAt() {
        return notifyAt;
    }

    /**
     * Set the time of day to schedule notifications at
     * @param notifyAt the time of day to schedule notifications at
     */
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

}