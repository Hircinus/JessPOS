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
        try (
                Scanner scanner = new Scanner(getSource())) {
            while (scanner.hasNext()) {
                String[] parts = scanner.nextLine().split(",");
                if(!name.toLowerCase().equals(parts[1].toLowerCase())) {
                    continue;
                } else {
                    return new Employee(Integer.parseInt(parts[0]), parts[1]);
                }
            }
        } catch (
                FileNotFoundException fnfe) {
            fnfe.printStackTrace();
        }
        return null;
    }
    public Employee getEmployee(int ID) {
        try (
                Scanner scanner = new Scanner(getSource())) {
            while (scanner.hasNext()) {
                String[] parts = scanner.nextLine().split(",");
                if(ID!=Integer.parseInt(parts[0])) {
                    continue;
                } else {
                    return new Employee(ID, parts[1]);
                }
            }
        } catch (
                FileNotFoundException fnfe) {
            fnfe.printStackTrace();
        }
        return null;
    }
    public ObservableList<Employee> getEmployees() {
        ArrayList<Employee> output = new ArrayList<>();
        try (
                Scanner scanner = new Scanner(getSource())) {
            while (scanner.hasNext()) {
                String[] parts = scanner.nextLine().split(",");
                output.add(new Employee(Integer.parseInt(parts[0]),parts[1]));
            }
        } catch (
                FileNotFoundException fnfe) {
            fnfe.printStackTrace();
        }
        return FXCollections.observableArrayList(output);
    }
    public int getNextID() {
        int last = 0;
        try (
                Scanner scanner = new Scanner(getSource())) {
            scanner.useDelimiter("^.+\n$");
            while (scanner.hasNext()) {
                last = Integer.parseInt(scanner.nextLine().split(",")[0]);
            }
        } catch (
                FileNotFoundException fnfe) {
            fnfe.printStackTrace();
        }
        return ++last;
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
