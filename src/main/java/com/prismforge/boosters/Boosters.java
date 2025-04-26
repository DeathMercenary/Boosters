package com.prismforge.boosters;

import com.prismforge.boosters.api.BoosterAPI;
import com.prismforge.boosters.booster.BoosterManager;
import com.prismforge.boosters.commands.BoostersCommand;
import com.prismforge.boosters.commands.CreateBoostCommand;
import com.prismforge.boosters.commands.GiveBoosterCommand;
import com.prismforge.boosters.listeners.InventoryClickListener;
import com.prismforge.boosters.players.PlayerBoosterManager;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Main class for the Boosters plugin.
 * Handles plugin startup and shutdown logic.
 */
public class Boosters extends JavaPlugin {

    private static Boosters instance;
    private BoosterManager boosterManager;
    private PlayerBoosterManager playerBoosterManager;
    private BoosterAPI boosterAPI;

    @Override
    public void onEnable() {
        instance = this;

        saveDefaultConfig();

        boosterManager = new BoosterManager(this);
        playerBoosterManager = new PlayerBoosterManager(this);
        boosterAPI = new BoosterAPI(this);

        // Register listeners and commands
        new InventoryClickListener(this);
        new CreateBoostCommand(this);
        new GiveBoosterCommand(this);
        new BoostersCommand(this); // (Will create this next!)

        getLogger().info("Boosters plugin has been enabled.");
    }


    @Override
    public void onDisable() {
        getLogger().info("Boosters plugin has been disabled.");
        // Any necessary cleanup here
    }

    /**
     * Get the Boosters plugin instance.
     * @return instance
     */
    public static Boosters getInstance() {
        return instance;
    }

    /**
     * Get the BoosterManager.
     * @return boosterManager
     */
    public BoosterManager getBoosterManager() {
        return boosterManager;
    }

    /**
     * Get the PlayerBoosterManager.
     * @return playerBoosterManager
     */
    public PlayerBoosterManager getPlayerBoosterManager() {
        return playerBoosterManager;
    }

    /**
     * Get the BoosterAPI.
     * @return boosterAPI
     */
    public BoosterAPI getBoosterAPI() {
        return boosterAPI;
    }
}
