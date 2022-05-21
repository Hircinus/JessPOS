package com.example.jesspos;

public class Employee {
    private int ID;
    private String name;
    private double salary;

    public Employee(int ID, String name, double salary) {
        this.ID = ID;
        this.name = name;
        this.salary = salary;
    }

    public Employee() {
        this.ID = 0;
        this.name = "";
        this.salary = 0.00;
    }

    public double getSalary() {
        return salary;
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
