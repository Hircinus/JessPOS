package com.example.jesspos;

import javafx.collections.ObservableList;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Scanner;

public class FileHandler {

    private static final EmployeeLog employees = new EmployeeLog(new File("employees"));
    private static final InventoryLog inventory = new InventoryLog(new File("inventory"));
    private static final TransactionLog transactions = new TransactionLog(new File("transactions"));
    private static final TimeLog times = new TimeLog(new File("times"));
    private static final File shadow = new File("shadow");

    public FileHandler() {
        if(!shadow.isFile())
            setPassword("p4$$w0rd");
    }

    public void setPassword(String pass) {
        try (PrintWriter writer = new PrintWriter(shadow)) {
            writer.println(pass.hashCode());
        } catch (
                FileNotFoundException fnfe) {
            fnfe.printStackTrace();
        }
    }
    public boolean passIsCorrect(String pass) {
        try (Scanner scanner = new Scanner(shadow)) {
            while(scanner.hasNext()) {
                if(pass.hashCode() == Integer.parseInt(scanner.nextLine())) {
                    return true;
                }
            }
        } catch (
                FileNotFoundException fnfe) {
            fnfe.printStackTrace();
        }
        return false;
    }

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

    public ArrayList<String[]> scanSrc() {
        ArrayList<String[]> output = new ArrayList<>();
        try (
                Scanner scanner = new Scanner(getSource())) {
            while (scanner.hasNext()) {
                String[] parts = scanner.nextLine().split(",");
                output.add(parts);
            }
        } catch (
                FileNotFoundException fnfe) {
            fnfe.printStackTrace();
        }
        return output;
    }

    public int getItemsCount() {
        int count = 0;
        for(String[] parts : scanSrc()) {
            count++;
        }
        return count;
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
}
