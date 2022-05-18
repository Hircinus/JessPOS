package com.example.jesspos;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
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
                System.out.println(parts[1]);
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
        System.out.println("remove " + SKU);
    }
    public void addItems(String name, String quantity, String price) {
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
