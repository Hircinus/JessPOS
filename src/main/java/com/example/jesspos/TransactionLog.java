package com.example.jesspos;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;

public class TransactionLog extends FileHandler {
    public TransactionLog(File source) {
        super(source);
    }
    public ObservableList<Transaction> getTransactions() {
        ArrayList<Transaction> output = new ArrayList<>();
        try (
                Scanner scanner = new Scanner(getSource())) {
            while (scanner.hasNext()) {
                String[] parts = scanner.nextLine().split(",");
                String[] itemSKUs = parts[5].split(";");
                ArrayList<Item> items = new ArrayList<>();
                for(String SKU : itemSKUs) {
                    items.add(getInventoryFile().getItem(SKU));
                }
                output.add(new Transaction(Integer.parseInt(parts[0]),getEmployeesFile().getEmployee(parts[1]),Instant.parse(parts[2]),items));
            }
        } catch (
                FileNotFoundException fnfe) {
            fnfe.printStackTrace();
        }
        return FXCollections.observableArrayList(output);
    }
    public Transaction getItem(String ID) {
        try (
                Scanner scanner = new Scanner(getSource())) {
            while (scanner.hasNext()) {
                String[] parts = scanner.nextLine().split(",");
                String[] itemSKUs = parts[5].split(";");
                ArrayList<Item> items = new ArrayList<>();
                for(String SKU : itemSKUs) {
                    items.add(getInventoryFile().getItem(SKU));
                }
                if(!ID.equals(parts[0])) {
                    continue;
                } else {
                    return new Transaction(Integer.parseInt(parts[0]),getEmployeesFile().getEmployee(parts[1]),Instant.parse(parts[2]),items);
                }
            }
        } catch (
                FileNotFoundException fnfe) {
            fnfe.printStackTrace();
        }
        return null;
    }
    public int getItemsCount() {
        int count = 0;
        try (
                Scanner scanner = new Scanner(getSource())) {
            scanner.useDelimiter("^.+\n$");
            while (scanner.hasNext()) {
                count++;
            }
        } catch (
                FileNotFoundException fnfe) {
            fnfe.printStackTrace();
        }
        return count;
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
                    continue;
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
            String itemSKUs = "";
            for(int i = 0; i<items.size(); i++) {
                getInventoryFile().decrementItem(items.get(i).getSKU());
                if(i==0) {
                    itemSKUs += items.get(i).getSKU();
                } else {
                    itemSKUs += ";" + items.get(i).getSKU();
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
