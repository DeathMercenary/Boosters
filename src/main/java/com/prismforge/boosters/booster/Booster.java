package com.prismforge.boosters.booster;

/**
 * Represents a single Booster.
 * Booster type + strength + duration (in minutes).
 */
public class Booster {

    private final String type;
    private final double strength;
    private final int durationMinutes;

    public Booster(String type, double strength, int durationMinutes) {
        this.type = type;
        this.strength = strength;
        this.durationMinutes = durationMinutes;
    }

    public String getType() {
        return type;
    }

    public double getStrength() {
        return strength;
    }

    public int getDurationMinutes() {
        return durationMinutes;
    }

    @Override
    public String toString() {
        return "Booster{" +
                "type='" + type + '\'' +
                ", strength=" + strength +
                ", durationMinutes=" + durationMinutes +
                '}';
    }
}
