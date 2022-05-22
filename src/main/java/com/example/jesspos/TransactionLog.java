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
    public void addTransaction(Employee employee, ArrayList<Item> items) {
        try (Scanner scanner = new Scanner(getSource());
             PrintWriter writer = new PrintWriter(getSource().getName() + ".data")) {
            Transaction newTrans = new Transaction(getNextID(), employee, Instant.now(), items);
            StringBuilder itemSKUs = new StringBuilder();
            for(int i = 0; i<items.size(); i++) {
                if(i==0) {
                    itemSKUs.append(items.get(i).getID());
                } else {
                    itemSKUs.append(";").append(items.get(i).getID());
                }
            }
            String input = newTrans.getID() + "," + newTrans.getEmployee().getName() + "," + newTrans.getRawDate() + "," + newTrans.getItemsCount() + "," + newTrans.getPriceDelta() + "," + itemSKUs;
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
