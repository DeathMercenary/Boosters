package com.prismforge.boosters.data;

public class PlayerBooster {
    private final String type;
    private final double strength;
    private final int duration; // in seconds

    public PlayerBooster(String type, double strength, int duration) {
        this.type = type;
        this.strength = strength;
        this.duration = duration;
    }

    public String getType() {
        return type;
    }

    public double getStrength() {
        return strength;
    }

    public int getDuration() {
        return duration;
    }
}
