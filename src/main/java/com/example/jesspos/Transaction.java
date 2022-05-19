package com.example.jesspos;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;

public class Transaction {
    private int ID;
    private Employee employee;
    private Instant date;
    private int itemsCount;
    private ArrayList<Item> items;
    private double priceDelta;
    private ArrayList<Integer> itemSKUs = new ArrayList<>();

    public Transaction(int ID, Employee employee, Instant date, ArrayList<Item> items) {
        this.ID = ID;
        this.employee = employee;
        this.date = date;
        this.items = items;
        this.itemsCount = items.size();
        for(Item i : items) {
            this.priceDelta += i.getPrice();
            this.itemSKUs.add(i.getSKU());
        }
        this.priceDelta = Math.round(priceDelta * 100) / 100.0;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public int getItemsCount() {
        return itemsCount;
    }

    public void setItemsCount(int itemsCount) {
        this.itemsCount = itemsCount;
    }

    public double getPriceDelta() {
        return priceDelta;
    }

    public void setPriceDelta(double priceDelta) {
        this.priceDelta = priceDelta;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public Instant getDate() { return date; }

    public void setDate(Instant date) {
        this.date = date;
    }

    public ArrayList<Item> getItems() {
        return items;
    }

    public void setItems(ArrayList<Item> items) {
        this.items = items;
    }
}
