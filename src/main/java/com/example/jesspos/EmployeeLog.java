package com.example.jesspos;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Scanner;

public class EmployeeLog extends FileHandler {
    public EmployeeLog(File source) {
        super(source);
    }
    public Employee getEmployee(String name) {
        for(String[] parts : scanSrc()) {
            if(name.equalsIgnoreCase(parts[1]))
                return new Employee(Integer.parseInt(parts[0]), parts[1]);
        }
        return null;
    }
    public Employee getEmployee(int ID) {
        for(String[] parts : scanSrc()) {
            if(ID==Integer.parseInt(parts[0]))
                return new Employee(ID, parts[1]);
        }
        return null;
    }
    public ObservableList<Employee> getEmployees() {
        ArrayList<Employee> output = new ArrayList<>();
        for(String[] parts : scanSrc()) {
            output.add(new Employee(Integer.parseInt(parts[0]),parts[1]));
        }
        return FXCollections.observableArrayList(output);
    }
    public void addEmployee(String name) {
        try (Scanner scanner = new Scanner(getSource());
             PrintWriter writer = new PrintWriter(getSource().getName() + ".data")) {
            Employee e = new Employee(getNextID(), name);
            String input = e.getID() + "," + e.getName();
            scanner.useDelimiter("^.+\n$");
            while(scanner.hasNext()) {
                writer.println(scanner.nextLine());
            }
            writer.println(input);
        } catch (
                FileNotFoundException fnfe) {
            fnfe.printStackTrace();
        }
        moveFileTo(getSource(), new File(getSource().getName()+".data"));
    }
}
