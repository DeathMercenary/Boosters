package com.prismforge.boosters.api;

import com.prismforge.boosters.Boosters;
import com.prismforge.boosters.booster.ActiveBooster;
import com.prismforge.boosters.booster.Booster;
import com.prismforge.boosters.booster.BoosterType;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Public API for the Boosters plugin.
 * Allows external plugins to query or interact with boosters.
 */
public class BoosterAPI {

    private final Boosters plugin;

    public BoosterAPI(Boosters plugin) {
        this.plugin = plugin;
    }

    /**
     * Returns all currently active boosters.
     * @return List of active boosters
     */
    public List<ActiveBooster> getActiveBoosters() {
        return plugin.getBoosterManager().getActiveBoosterManager().getActiveBoosters();
    }

    /**
     * Checks if a booster type is currently active.
     * @param type Booster type (e.g., \"xp\")
     * @return true if active
     */
    public boolean isBoosterTypeActive(String type) {
        return plugin.getBoosterManager().getActiveBoosterManager().isTypeActive(type);
    }

    /**
     * Gets a list of boosters a player owns.
     * @param playerUUID Player's UUID
     * @return List of boosters
     */
    public List<Booster> getPlayerBoosters(UUID playerUUID) {
        return plugin.getPlayerBoosterManager().getPlayerBoosters(playerUUID);
    }

    /**
     * Gets the number of boosters a player owns of a specific type.
     * @param playerUUID Player UUID
     * @param type Booster type
     * @return Count
     */
    public int getPlayerBoosterCount(UUID playerUUID, String type) {
        List<Booster> boosters = getPlayerBoosters(playerUUID);
        int count = 0;
        for (Booster booster : boosters) {
            if (booster.getType().equalsIgnoreCase(type)) {
                count++;
            }
        }
        return count;
    }

    /**
     * Gives a booster to a player.
     * @param playerUUID Player UUID
     * @param booster Booster to give
     */
    public void giveBooster(UUID playerUUID, Booster booster) {
        plugin.getPlayerBoosterManager().addBooster(playerUUID, booster);
    }

    /**
     * Removes a booster from a player's inventory.
     * @param playerUUID Player UUID
     * @param booster Booster to remove
     */
    public void removeBooster(UUID playerUUID, Booster booster) {
        plugin.getPlayerBoosterManager().removeBooster(playerUUID, booster);
    }

    /**
     * Gets the BoosterType definition from config.
     * @param type Booster type
     * @return BoosterType object
     */
    public BoosterType getBoosterType(String type) {
        return plugin.getBoosterManager().getBoosterType(type);
    }
}
