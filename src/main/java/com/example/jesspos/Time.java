package com.example.jesspos;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;

public class Time {
    private int ID;
    private Instant pin;
    private String conpin;
    private Instant pout;
    private String conpout;

    public long getDelta() {
        return delta;
    }

    public void setDelta(long delta) {
        this.delta = delta;
    }

    private long delta;

    public Time(int ID, Instant pin, Instant pout) {
        this.ID = ID;
        this.pin = pin;
        this.pout = pout;
        LocalDateTime ldt = LocalDateTime.ofInstant(pin, ZoneId.systemDefault());
        String newMinute = generateNewMinute(ldt);
        String newHour = generateNewHour(ldt);
        this.conpin = ldt.getMonth() + " " + (ldt.getDayOfMonth() + 00) + " ; " + newHour + ":" + newMinute;
        ldt = LocalDateTime.ofInstant(pout, ZoneId.systemDefault());
        newMinute = generateNewMinute(ldt);
        newHour = generateNewHour(ldt);
        this.conpout = ldt.getMonth() + " " + (ldt.getDayOfMonth() + 00) + " ; " + newHour + ":" + newMinute;
        Duration d = Duration.between(pin, pout);
        this.delta = d.toMinutes();
    }

    public static String generateNewMinute(LocalDateTime ldt) {
        String newMinute = "";
        if ((ldt.getMinute() + "").matches("^[0-9]{1}$"))
            newMinute = ldt.getMinute() + "0";
        else
            newMinute = "" + ldt.getMinute();
        return newMinute;
    }
    public static String generateNewHour(LocalDateTime ldt) {
        String newHour = "";
        if ((ldt.getHour() + "").matches("^[0-9]{1}$"))
            newHour = "0" + ldt.getHour();
        else
            newHour = "" + ldt.getHour();
        return newHour;
    }

    public String getConpin() {
        return conpin;
    }

    public void setConpin(String conpin) {
        this.conpin = conpin;
    }

    public String getConpout() {
        return conpout;
    }

    public void setConpout(String conpout) {
        this.conpout = conpout;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public Instant getPin() {
        return pin;
    }

    public void setPin(Instant pin) {
        this.pin = pin;
    }

    public Instant getPout() {
        return pout;
    }

    public void setPout(Instant pout) {
        this.pout = pout;
    }
}
