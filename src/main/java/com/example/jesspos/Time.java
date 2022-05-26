// Stores Time objects used in TimeLog
// Depends on EmployeeLog
package com.example.jesspos;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;

public class Time implements Identifiable {
    private int ID;
    private String conpin;
    private String conpout;
    private double pay;

    public long getDelta() {
        return delta;
    }

    private long delta;

    public Time(int ID, Instant pin, Instant pout) {
        this.ID = ID;
        // convert Instant pin to system timezone adjusted data
        LocalDateTime ldt = LocalDateTime.ofInstant(pin, ZoneId.systemDefault());
        String newMinute = generateNewMinute(ldt);
        String newHour = generateNewHour(ldt);
        this.conpin = ldt.getMonth() + " " + ldt.getDayOfMonth() + " ; " + newHour + ":" + newMinute;
        // convert Instant pout to system timezone adjusted data
        ldt = LocalDateTime.ofInstant(pout, ZoneId.systemDefault());
        newMinute = generateNewMinute(ldt);
        newHour = generateNewHour(ldt);
        this.conpout = ldt.getMonth() + " " + ldt.getDayOfMonth() + " ; " + newHour + ":" + newMinute;
        Duration d = Duration.between(pin, pout);
        this.delta = d.toMinutes();
        this.pay = Math.round(((new FileHandler().getEmployeesFile().getEmployee(ID).getSalary()) * this.delta / 60.00) * 100.00) / 100.00;
    }

    public Time() {
        this.ID = 0;
        Instant now = Instant.now();
        LocalDateTime ldt = LocalDateTime.ofInstant(now, ZoneId.systemDefault());
        this.conpin = ldt.getMonth() + " " + ldt.getDayOfMonth() + " ; " + generateNewHour(ldt) + ":" + generateNewMinute(ldt);
        ldt = LocalDateTime.ofInstant(now, ZoneId.systemDefault());
        this.conpout = ldt.getMonth() + " " + ldt.getDayOfMonth() + " ; " + generateNewHour(ldt) + ":" + generateNewMinute(ldt);
        Duration d = Duration.between(now, now);
        this.delta = d.toMinutes();
    }

    public static String generateNewMinute(LocalDateTime ldt) {
        String newMinute;
        // add trailing zero to minute if only one digit
        if ((ldt.getMinute() + "").matches("^[0-9]$"))
            newMinute = ldt.getMinute() + "0";
        else
            newMinute = "" + ldt.getMinute();
        return newMinute;
    }
    public static String generateNewHour(LocalDateTime ldt) {
        String newHour;
        // add leading zero to hour if only one digit
        if ((ldt.getHour() + "").matches("^[0-9]$"))
            newHour = "0" + ldt.getHour();
        else
            newHour = "" + ldt.getHour();
        return newHour;
    }

    public double getPay() {
        return pay;
    }

    public String getConpin() {
        return conpin;
    }

    public String getConpout() {
        return conpout;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) { this.ID=ID; }
}
