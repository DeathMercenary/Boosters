package com.prismforge.boosters.gui;

import com.prismforge.boosters.Boosters;
import com.prismforge.boosters.booster.Booster;
import com.prismforge.boosters.booster.ActiveBooster;
import com.prismforge.boosters.booster.BoosterType;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Handles the Boosters GUI.
 */
public class BoosterGUI {

    private static final DecimalFormat decimalFormat = new DecimalFormat("#.##");
    private static final String INVENTORY_TITLE = "§b§lYour Boosters";

    private final Boosters plugin;

    public BoosterGUI(Boosters plugin) {
        this.plugin = plugin;
    }

    /**
     * Opens the booster GUI for a player.
     */
    public void open(Player player) {
        List<Booster> boosters = plugin.getPlayerBoosterManager().getPlayerBoosters(player.getUniqueId());
        int size = Math.max(27, ((boosters.size() / 9) + 1) * 9);
        Inventory inv = Bukkit.createInventory(null, size, INVENTORY_TITLE);

        for (int i = 0; i < boosters.size(); i++) {
            Booster booster = boosters.get(i);
            BoosterType type = plugin.getBoosterManager().getBoosterType(booster.getType());

            ItemStack item = new ItemStack(Material.EXP_BOTTLE); // Simple XP bottle for now
            ItemMeta meta = item.getItemMeta();
            meta.setDisplayName("§b" + decimalFormat.format(booster.getStrength()) + "x " + type.getDisplayName());
            List<String> lore = new ArrayList<>();
            lore.add("§7Duration: §f" + booster.getDurationMinutes() + " minutes");
            lore.add("§e§lClick to activate!");
            meta.setLore(lore);
            item.setItemMeta(meta);

            inv.setItem(i, item);
        }

        player.openInventory(inv);
    }

    /**
     * Handles inventory clicks inside the boosters GUI.
     */
    public void handleClick(InventoryClickEvent event) {
        if (event.getView().getTitle().equals(INVENTORY_TITLE)) {
            event.setCancelled(true);
            if (event.getCurrentItem() == null || event.getCurrentItem().getType() == Material.AIR) return;

            Player player = (Player) event.getWhoClicked();
            ItemStack item = event.getCurrentItem();
            ItemMeta meta = item.getItemMeta();
            if (meta == null || meta.getDisplayName() == null) return;

            // Parse booster info
            String displayName = meta.getDisplayName();
            String[] parts = displayName.split("x ");
            if (parts.length != 2) {
                player.sendMessage("§cInvalid booster.");
                return;
            }

            double strength;
            String typeName;

            try {
                strength = Double.parseDouble(parts[0].replace("§b", ""));
                typeName = parts[1].replace("§r", "").replace(" Booster", "").toLowerCase();
            } catch (Exception e) {
                player.sendMessage("§cCould not parse booster information.");
                return;
            }

            List<Booster> playerBoosters = plugin.getPlayerBoosterManager().getPlayerBoosters(player.getUniqueId());
            Booster selectedBooster = null;
            for (Booster booster : playerBoosters) {
                if (booster.getType().equalsIgnoreCase(typeName) && booster.getStrength() == strength) {
                    selectedBooster = booster;
                    break;
                }
            }

            if (selectedBooster == null) {
                player.sendMessage("§cYou don't have that booster.");
                return;
            }

            // Activate it
            activateBooster(player, selectedBooster);
            player.closeInventory();
        }
    }

    /**
     * Activates a booster and removes it from the player's inventory.
     */
    private void activateBooster(Player player, Booster booster) {
        plugin.getPlayerBoosterManager().removeBooster(player.getUniqueId(), booster);

        // Activate
        plugin.getBoosterManager().getActiveBoosterManager().activateBooster(
                new ActiveBooster(player.getUniqueId(), booster.getType(), booster.getStrength(), booster.getDurationMinutes())
        );

        // Run the configured activation command
        BoosterType type = plugin.getBoosterManager().getBoosterType(booster.getType());
        if (type != null) {
            String command = type.getActivateCommand()
                    .replace("%player%", player.getName())
                    .replace("%strength%", String.valueOf(booster.getStrength()))
                    .replace("%duration%", String.valueOf(booster.getDurationMinutes()));
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command);
        }

        player.sendMessage("§aActivated §b" + booster.getStrength() + "x " + booster.getType() + " §abooster for §f" + booster.getDurationMinutes() + " minutes!");
        player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1f, 1f);
    }
}
