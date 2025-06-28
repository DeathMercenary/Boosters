package com.prismforge.boosters.data;

public class BoosterType {
    private final String name;
    private final String displayName;
    private final double defaultStrength;
    private final int defaultDuration;

    public BoosterType(String name, String displayName, double defaultStrength, int defaultDuration) {
        this.name = name;
        this.displayName = displayName;
        this.defaultStrength = defaultStrength;
        this.defaultDuration = defaultDuration;
    }

    public String getName() { return name; }
    public String getDisplayName() { return displayName; }
    public double getDefaultStrength() { return defaultStrength; }
    public int getDefaultDuration() { return defaultDuration; }
}
