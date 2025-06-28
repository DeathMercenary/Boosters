package com.prismforge.boosters.data;

public class Booster {
    private final String type;
    private final double strength;
    private final int originalSeconds; // NEW: original total
    private int remainingSeconds;      // same as before

    public Booster(String type, double strength, int durationSeconds) {
        this.type = type;
        this.strength = strength;
        this.originalSeconds = durationSeconds;  // store the original value
        this.remainingSeconds = durationSeconds;
    }
    public void setRemainingSeconds(int seconds) {
        this.remainingSeconds = seconds;
    }
    public String getType() { return type; }
    public double getStrength() { return strength; }

    public int getDurationSeconds() { return remainingSeconds; }
    public int getOriginalSeconds() { return originalSeconds; } // new getter

    public void tick() { remainingSeconds--; }
    public boolean isExpired() { return remainingSeconds <= 0; }
    public void extend(int seconds) { remainingSeconds += seconds; }
}

