package com.prismforge.boosters.booster;

import java.util.UUID;

/**
 * Represents an active booster currently running on the server.
 */
public class ActiveBooster {

    private final UUID activator;
    private final String type;
    private final double strength;
    private long endTimeMillis;

    /**
     * Create an ActiveBooster.
     * @param activator UUID of the player who activated it
     * @param type Booster type (e.g., \"xp\", \"chest\")
     * @param strength Booster strength (e.g., 2.0x, 1.5x)
     * @param durationMinutes Duration in minutes
     */
    public ActiveBooster(UUID activator, String type, double strength, int durationMinutes) {
        this.activator = activator;
        this.type = type;
        this.strength = strength;
        this.endTimeMillis = System.currentTimeMillis() + (durationMinutes * 60_000L);
    }

    public UUID getActivator() {
        return activator;
    }

    public String getType() {
        return type;
    }

    public double getStrength() {
        return strength;
    }

    public long getEndTimeMillis() {
        return endTimeMillis;
    }

    public boolean isExpired() {
        return System.currentTimeMillis() >= endTimeMillis;
    }

    public long getRemainingMillis() {
        return Math.max(0, endTimeMillis - System.currentTimeMillis());
    }
}
