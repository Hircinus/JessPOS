package com.example.jesspos;

import java.time.Instant;
import java.util.ArrayList;

public class Transaction implements Identifiable {
    private int ID;
    private Employee employee;
    private Instant rawDate;
    private String date;
    private int itemsCount;
    private ArrayList<Item> items;
    private double priceDelta;

    public Transaction(int ID, Employee employee, Instant date, ArrayList<Item> items) {
        this.ID = ID;
        this.employee = employee;
        this.rawDate = date;
        Time transTime = new Time(employee.getID(), date, date);
        this.date = transTime.getConpin();
        this.items = items;
        this.itemsCount = items.size();
        this.priceDelta = Math.round(priceDelta * 100.0) / 100.0;
    }

    public Transaction() {
        this.ID = 0;
        this.employee = new Employee();
        this.rawDate = Instant.now();
        Time transTime = new Time(employee.getID(), rawDate, rawDate);
        this.date = transTime.getConpin();
        this.items = new ArrayList<Item>();
        this.itemsCount = items.size();
        priceDelta = 0.00;
    }

    public Instant getRawDate() {
        return rawDate;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) { this.ID=ID; }

    public int getItemsCount() {
        return itemsCount;
    }

    public double getPriceDelta() {
        return priceDelta;
    }

    public Employee getEmployee() {
        return employee;
    }

    public ArrayList<Item> getItems() {
        return items;
    }
}
