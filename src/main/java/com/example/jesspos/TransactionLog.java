package com.example.jesspos;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Scanner;

public class TransactionLog extends FileHandler {
    public TransactionLog(File source) {
        super(source);
    }
    public ObservableList<Transaction> getTransactions() {
        ArrayList<Transaction> output = new ArrayList<>();
        for(String[] parts : scanSrc()) {
            String[] itemSKUs = parts[5].split(";");
            ArrayList<Item> items = new ArrayList<>();
            for(String SKU : itemSKUs) {
                try {
                    items.add(getInventoryFile().getItem(SKU));
                } catch (InventoryLog.ItemNotFoundException e) {
                    e.printStackTrace();
                }
            }
            output.add(new Transaction(Integer.parseInt(parts[0]),getEmployeesFile().getEmployee(parts[1]),Instant.parse(parts[2]),items));
        }
        return FXCollections.observableArrayList(output);
    }
    public Transaction getItem(String ID) {
        for(String[] parts : scanSrc()) {
            String[] itemSKUs = parts[5].split(";");
            ArrayList<Item> items = new ArrayList<>();
            for(String SKU : itemSKUs) {
                try {
                    items.add(getInventoryFile().getItem(SKU));
                } catch (InventoryLog.ItemNotFoundException e) {
                    e.printStackTrace();
                }
            }
            if(ID.equals(parts[0]))
                return new Transaction(Integer.parseInt(parts[0]),getEmployeesFile().getEmployee(parts[1]),Instant.parse(parts[2]),items);
        }
        return null;
    }
    public void removeItem(int SKU) {
        try (Scanner scanner = new Scanner(getSource());
             PrintWriter writer = new PrintWriter(getSource().getName() + ".data")) {
            boolean found = false;
            scanner.useDelimiter("^.+\n$");
            while(scanner.hasNext()) {
                String currentLine = scanner.nextLine();
                String[] currentParts = currentLine.split(",");
                int currentSKU = Integer.parseInt(currentParts[0]);
                if(currentSKU == SKU) {
                    found = true;
                }
                else if (found) {
                    writer.println(--currentSKU + "," + currentParts[1] + "," + currentParts[2] + "," + currentParts[3]);
                }
                else {
                    writer.println(currentLine);
                }
            }
        } catch (
                FileNotFoundException fnfe) {
            fnfe.printStackTrace();
        }
        moveFileTo(getSource(), new File(getSource().getName()+".data"));
    }
    public void addTransaction(Employee employee, ArrayList<Item> items) {
        try (Scanner scanner = new Scanner(getSource());
             PrintWriter writer = new PrintWriter(getSource().getName() + ".data")) {
            Transaction newTrans = new Transaction(getNextID(), employee, Instant.now(), items);
            StringBuilder itemSKUs = new StringBuilder();
            for(int i = 0; i<items.size(); i++) {
                if(i==0) {
                    itemSKUs.append(items.get(i).getSKU());
                } else {
                    itemSKUs.append(";").append(items.get(i).getSKU());
                }
            }
            String input = newTrans.getID() + "," + newTrans.getEmployee().getName() + "," + newTrans.getDate() + "," + newTrans.getItemsCount() + "," + newTrans.getPriceDelta() + "," + itemSKUs;
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
