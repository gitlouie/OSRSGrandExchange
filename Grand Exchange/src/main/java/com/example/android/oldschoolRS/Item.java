package com.example.android.oldschoolRS;

/**
 * Created by Louie on 2/24/2018.
 */

public class Item {
    private String name;
    private String id;
    private boolean members;
    private String description;
    private String icon;
    private String price;
    private String thirtyDayTrend;
    private String nintyDayTrend;
    private String oneHundredEightyDayTrend;
    private String current;

    public Item(){

    }

    public void setID(String id){
        this.id = id;
        setIcon(id);
    }
    public String getID(){
        return id;
    }

    public void setMembers(boolean members){
        this.members = members;
    }
    public boolean getMembers(){
        return members;
    }

    public void setDescription(String description){
        this.description = description;
    }
    public String getDescription(){
        return description;
    }
    private void setIcon(String id){
        this.icon = "http://services.runescape.com/m=itemdb_oldschool/obj_big.gif?id=" + id;
    }
    public String getIcon(){
        return icon;
    }

    public void setPrice(String price){
        this.price = price;
    }
    public String getPrice(){
        return price;
    }
    public void setName(String name){
        this.name = name;
    }
    public String getName(){
        return name;
    }

    public String getThirtyDayTrend() {
        return thirtyDayTrend;
    }

    public void setThirtyDayTrend(String thirtyDayTrend) {
        this.thirtyDayTrend = thirtyDayTrend;
    }

    public String getNintyDayTrend() {
        return nintyDayTrend;
    }

    public void setNintyDayTrend(String nintyDayTrend) {
        this.nintyDayTrend = nintyDayTrend;
    }

    public void setOneHundredEightyDayTrend(String oneHundredEightyDayTrend) {
        this.oneHundredEightyDayTrend = oneHundredEightyDayTrend;
    }

    public String getOneHundredEightyDayTrend() {
        return oneHundredEightyDayTrend;
    }

    public String toString() {
        String output;
        output = "com.example.android.oldschoolRS.Item data:\n";
        output += "ID: "+ id+",\nDescription: "+description+"\nName: "+name+"\nmembers: "+(
                members ? "yes" : "no")+"\n\n\n";
        output += "Trend data\n";
        output += "Current price: "+ price + "\nToday's change: " + current +
                "\n1 Month change: " + thirtyDayTrend + "\n3 Month change: " + nintyDayTrend +
                "\n6 Month change: " + oneHundredEightyDayTrend;
        return output;
    }

    public String getCurrent() {
        return current;
    }

    public void setCurrent(String current) {
        this.current = current;
    }
}
