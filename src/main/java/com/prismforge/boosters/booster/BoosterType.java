package com.prismforge.boosters.booster;

import java.util.List;

/**
 * Represents a booster category/type (xp, chest, etc.)
 */
public class BoosterType {

    private final String id;
    private final String displayName;
    private final String activateCommand;
    private final List<String> tipCommands; // NEW

    public BoosterType(String id, String displayName, String activateCommand, List<String> tipCommands) {
        this.id = id;
        this.displayName = displayName;
        this.activateCommand = activateCommand;
        this.tipCommands = tipCommands;
    }

    public String getId() {
        return id;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getActivateCommand() {
        return activateCommand;
    }

    public List<String> getTipCommands() {
        return tipCommands;
    }
}
