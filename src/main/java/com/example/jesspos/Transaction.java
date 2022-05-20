package com.example.jesspos;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;

public class Transaction {
    private int ID;
    private Employee employee;
    private Instant rawDate;
    private String date;
    private int itemsCount;
    private ArrayList<Item> items;
    private double priceDelta;
    private ArrayList<Integer> itemSKUs = new ArrayList<>();

    public Transaction(int ID, Employee employee, Instant date, ArrayList<Item> items) {
        this.ID = ID;
        this.employee = employee;
        this.rawDate = date;
        Time transTime = new Time(employee.getID(), date, date);
        this.date = transTime.getConpin();
        this.items = items;
        this.itemsCount = items.size();
        for(Item i : items) {
            this.priceDelta += i.getPrice();
            this.itemSKUs.add(i.getSKU());
        }
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
        itemSKUs = new ArrayList<Integer>();
        priceDelta = 0.00;
    }

    public Instant getRawDate() {
        return rawDate;
    }

    public int getID() {
        return ID;
    }

    public int getItemsCount() {
        return itemsCount;
    }

    public double getPriceDelta() {
        return priceDelta;
    }

    public Employee getEmployee() {
        return employee;
    }

    public String getDate() { return date; }

    public ArrayList<Item> getItems() {
        return items;
    }
}
