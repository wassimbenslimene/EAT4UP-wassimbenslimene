package com.example.myapplication6.model;
import java.util.List;
public class Request {
    private String name,table,totale;
    private List<Order> foods; //List of food Order

    public Request() {
    }

    public Request( String name, String table, String totale, List<Order> foods) {

        this.name = name;
        this.table = table;
        this.totale = totale;
        this.foods = foods;
    }




    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public String getTotale() {
        return totale;
    }

    public void setTotale(String totale) {
        this.totale = totale;
    }

    public List<Order> getFoods() {
        return foods;
    }

    public void setFoods(List<Order> foods) {
        this.foods = foods;
    }

    public String getTable() {
        return table;
    }

    public void setTable(String table) {
        this.table = table;
    }
}
