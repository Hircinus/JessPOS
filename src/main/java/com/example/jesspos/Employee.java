package com.example.jesspos;

public class Employee {
    private int ID;
    private String name;

    public Employee(int ID, String name) {
        this.ID = ID;
        this.name = name;
    }

    public Employee() {
        this.ID = 0;
        this.name = "";
    }

    public int getID() {
        return ID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
