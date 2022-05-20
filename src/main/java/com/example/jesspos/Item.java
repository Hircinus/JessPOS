package com.example.jesspos;

public class Item {
    private int SKU;
    private String name;
    private int quantity;
    private double price;

    public Item(int SKU, String name, int quantity, double price) {
        this.SKU = SKU;
        this.name = name;
        this.quantity = quantity;
        this.price = price;
    }

    public Item() {
        this.SKU = 0;
        this.name = "";
        this.quantity = 0;
        this.price = 0.00;
    }

    public int getSKU() {
        return SKU;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getQuantity() {
        return quantity;
    }

    public double getPrice() {
        return price;
    }
}
