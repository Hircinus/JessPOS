package com.example.jesspos;

import javafx.collections.ObservableList;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

public class FileHandler {

    private static final EmployeeLog employees = new EmployeeLog(new File("employees"));
    private static final InventoryLog inventory = new InventoryLog(new File("inventory"));
    private static final TransactionLog transactions = new TransactionLog(new File("transactions"));
    private static final TimeLog times = new TimeLog(new File("times"));

    private File source;
    public FileHandler(File source) {
        if(!source.isFile()) {
            try {
                source.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        this.source = source;
    }

    public FileHandler() {}

    public File getSource() {
        return source;
    }

    public EmployeeLog getEmployeesFile() {
        return employees;
    }

    public TimeLog getTimesFile() {
        return times;
    }

    public InventoryLog getInventoryFile() {
        return inventory;
    }

    public TransactionLog getTransactionsFile() {
        return transactions;
    }

    public void setSource(File source) {
        this.source = source;
    }
    public void moveFileTo(File file1, File file2) {
        try (Scanner scanner = new Scanner(file2);
             PrintWriter writer = new PrintWriter(file1)) {
            while(scanner.hasNext()) {
                writer.println(scanner.nextLine());
            }
        } catch (
                FileNotFoundException fnfe) {
            fnfe.printStackTrace();
        } finally {
            file2.delete();
        }
    }
}
