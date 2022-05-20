package com.example.jesspos;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Scanner;

public class TimeLog extends FileHandler {
    public TimeLog(File source) {
        super(source);
    }
    public ObservableList<Time> getTimes(int ID) {
        ArrayList<Time> output = new ArrayList<>();
        for(String[] parts : scanSrc()) {
            int currentID = Integer.parseInt(parts[0]);
            if(currentID==ID && !parts[1].equals(parts[2])) {
                output.add(new Time(ID, Instant.parse(parts[1]),Instant.parse(parts[2])));
            }
        }
        return FXCollections.observableArrayList(output);
    }
    public void punchIn(int ID) {
        try (Scanner scanner = new Scanner(getSource());
             PrintWriter writer = new PrintWriter(getSource().getName() + ".data")) {
            Instant maintenant = Instant.now();
            String input = ID + "," + maintenant + "," + maintenant;
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
    public void punchOut(int ID) {
        try (Scanner scanner = new Scanner(getSource());
             PrintWriter writer = new PrintWriter(getSource().getName() + ".data")) {
            scanner.useDelimiter("^.+\n$");
            String input = "";
            while(scanner.hasNext()) {
                String currentLine = scanner.nextLine();
                String[] parts = currentLine.split(",");
                if(Integer.parseInt(parts[0]) == ID && parts[2].equals(parts[1])) {
                    input = ID + "," + parts[1] + "," + Instant.now();
                } else {
                    writer.println(currentLine);
                }
            }
            writer.println(input);
        } catch (
                FileNotFoundException fnfe) {
            fnfe.printStackTrace();
        }
        moveFileTo(getSource(), new File(getSource().getName()+".data"));
    }
    public int getSignedInID() {
        int output = 0;
        for(String[] parts : scanSrc()) {
            int currentID = Integer.parseInt(parts[0]);
            if(parts[1].equals(parts[2])) {
                output = currentID;
            }
        }
        return output;
    }
    public String getLastTime(int ID){
        for(String[] parts : scanSrc()) {
            int currentID = Integer.parseInt(parts[0]);
            if(currentID==ID&&parts[1].equals(parts[2])) {
                Time currentTime = new Time(ID, Instant.parse(parts[1]), Instant.parse(parts[2]));
                return currentTime.getConpin();
            }
        }
        return "";
    }
}
