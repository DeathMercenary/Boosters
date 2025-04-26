package com.prismforge.boosters.listeners;

import com.prismforge.boosters.Boosters;
import com.prismforge.boosters.gui.BoosterGUI;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

/**
 * Listener for handling clicks inside Booster GUI.
 */
public class InventoryClickListener implements Listener {

    private final BoosterGUI boosterGUI;

    public InventoryClickListener(Boosters plugin) {
        this.boosterGUI = new BoosterGUI(plugin);
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        boosterGUI.handleClick(event);
    }
}
