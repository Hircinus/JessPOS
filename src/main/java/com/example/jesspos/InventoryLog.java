package com.example.jesspos;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class InventoryLog extends FileHandler {
    public InventoryLog(File file) {
        super(file);
    }
    public ObservableList<Item> getItems() {
        ArrayList<Item> output = new ArrayList<>();
        try (
                Scanner scanner = new Scanner(getSource())) {
            while (scanner.hasNext()) {
                String[] parts = scanner.nextLine().split(",");
                output.add(new Item(Integer.parseInt(parts[0]),parts[1],Integer.parseInt(parts[2]),Double.parseDouble(parts[3])));
            }
        } catch (
                FileNotFoundException fnfe) {
            fnfe.printStackTrace();
        }
        return FXCollections.observableArrayList(output);
    }
    public Item getItem(String SKU) {
        try (
                Scanner scanner = new Scanner(getSource())) {
            while (scanner.hasNext()) {
                String[] parts = scanner.nextLine().split(",");
                if(!SKU.equals(parts[0])) {
                    continue;
                } else {
                    return new Item(Integer.parseInt(parts[0]), parts[1], Integer.parseInt(parts[2]), Double.parseDouble(parts[3]));
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
    public int getNextSKU() {
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
            scanner.useDelimiter("^.+\n$");
            while(scanner.hasNext()) {
                String currentLine = scanner.nextLine();
                String[] currentParts = currentLine.split(",");
                int currentSKU = Integer.parseInt(currentParts[0]);
                if(currentSKU == SKU) {
                    writer.println(currentSKU + "," + currentParts[1] + "," + 0 + "," + currentParts[3]);
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
    public void decrementItem(int SKU) {
        try (Scanner scanner = new Scanner(getSource());
             PrintWriter writer = new PrintWriter(getSource().getName() + ".data")) {
            scanner.useDelimiter("^.+\n$");
            while(scanner.hasNext()) {
                String currentLine = scanner.nextLine();
                String[] currentParts = currentLine.split(",");
                int currentSKU = Integer.parseInt(currentParts[0]);
                int quant = Integer.parseInt(currentParts[2]);
                if(currentSKU == SKU) {
                    if(quant<=1) {
                        removeItem(SKU);
                        break;
                    }
                    writer.println(currentSKU + "," + currentParts[1] + "," + --quant + "," + currentParts[3]);
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
    public void addItem(String name, String quantity, String price) {
        try (Scanner scanner = new Scanner(getSource());
                PrintWriter writer = new PrintWriter(getSource().getName() + ".data")) {
            String input = getNextSKU() + "," + name + "," + quantity + "," + price;
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
