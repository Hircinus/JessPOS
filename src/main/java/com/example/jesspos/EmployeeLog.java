package com.example.jesspos;

import java.io.File;
import java.io.FileNotFoundException;
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
                if(!name.toLowerCase().equals(parts[0].toLowerCase())) {
                    continue;
                } else {
                    return new Employee(parts[0]);
                }
            }
        } catch (
                FileNotFoundException fnfe) {
            fnfe.printStackTrace();
        }
        return null;
    }
}
