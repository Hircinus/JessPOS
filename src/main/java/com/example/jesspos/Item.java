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

    public int getSKU() {
        return SKU;
    }

    public void setSKU(int SKU) {
        this.SKU = SKU;
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

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
