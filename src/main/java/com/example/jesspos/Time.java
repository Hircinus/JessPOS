package com.example.jesspos;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;

public class Time {
    private int ID;
    private Instant pin;
    private Instant pout;

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
        Duration d = Duration.between(pin, pout);
        this.delta = d.toMinutes();
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
