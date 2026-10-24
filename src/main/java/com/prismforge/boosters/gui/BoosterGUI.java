package com.prismforge.boosters.gui;

import com.prismforge.boosters.Boosters;
import com.prismforge.boosters.data.Booster;
import com.prismforge.boosters.data.BoosterQueue;
import com.prismforge.boosters.data.PlayerBooster;
import com.prismforge.boosters.manager.PlayerBoosterManager;
import com.prismforge.boosters.util.TimeUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

public class BoosterGUI implements Listener {

    private final Boosters plugin;
    private final PlayerBoosterManager manager;

    public BoosterGUI(Boosters plugin) {
        this.plugin = plugin;
        this.manager = plugin.getPlayerBoosterManager();
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    public void open(Player player) {
        UUID uuid = player.getUniqueId();
        List<PlayerBooster> boosters = manager.getBoosters(uuid);

        int size = ((boosters.size() / 9) + 1) * 9;
        Inventory inv = Bukkit.createInventory(null, size, ChatColor.BLUE + "Your Boosters");

        for (int i = 0; i < boosters.size(); i++) {
            PlayerBooster booster = boosters.get(i);
            ItemStack item = new ItemStack(Material.EMERALD); // or custom type per booster
            ItemMeta meta = item.getItemMeta();
            meta.setDisplayName(ChatColor.GOLD + booster.getType() + " Booster");
            meta.setLore(java.util.Arrays.asList(
                    ChatColor.YELLOW + "Strength: " + booster.getStrength() + "x",
                    ChatColor.YELLOW + "Duration: " + TimeUtil.formatSeconds(booster.getDuration()),
                    ChatColor.GREEN + "Click to activate!"
            ));
            item.setItemMeta(meta);
            inv.setItem(i, item);
        }

        player.openInventory(inv);
    }
    public void openMainMenu(Player player) {
        Inventory inv = Bukkit.createInventory(null, 9, ChatColor.BLUE + "Booster Menu");

        ItemStack personal = new ItemStack(Material.CHEST);
        ItemMeta pMeta = personal.getItemMeta();
        pMeta.setDisplayName(ChatColor.GOLD + "Your Boosters");
        pMeta.setLore(Collections.singletonList(ChatColor.GRAY + "Click to view your boosters."));
        personal.setItemMeta(pMeta);
        inv.setItem(3, personal);

        ItemStack queued = new ItemStack(Material.CLOCK);
        ItemMeta qMeta = queued.getItemMeta();
        qMeta.setDisplayName(ChatColor.AQUA + "Queued Boosters");
        qMeta.setLore(Collections.singletonList(ChatColor.GRAY + "Click to view upcoming global boosters."));
        queued.setItemMeta(qMeta);
        inv.setItem(5, queued);

        player.openInventory(inv);
    }

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        if (event.getView().getTitle() == null) return;
        Player player = (Player) event.getWhoClicked();

        String title = event.getView().getTitle();

        if(title.toLowerCase().contains("booster")) event.setCancelled(true);
        if (title.contains("Booster Menu")) {
            ItemStack clicked = event.getCurrentItem();
            if (clicked == null || !clicked.hasItemMeta()) return;

            String name = ChatColor.stripColor(clicked.getItemMeta().getDisplayName());
            if (name.equalsIgnoreCase("Your Boosters")) {
                openPersonal(player);
            } else if (name.equalsIgnoreCase("Queued Boosters")) {
                openQueued(player);
            }
            return;
        }

        if (title.contains("Your Boosters")) {
            List<PlayerBooster> boosters = manager.getBoosters(player.getUniqueId());
            int slot = event.getRawSlot();
            if (slot < 0 || slot >= boosters.size()) return;

            PlayerBooster clickedBooster = boosters.get(slot);
            plugin.getBoosterManager().addBooster(clickedBooster.getType(), clickedBooster.getStrength(), clickedBooster.getDuration());
            manager.removeBooster(player.getUniqueId(), clickedBooster);
            Bukkit.broadcastMessage(ChatColor.GOLD + "[Boosters] " + ChatColor.YELLOW + player.getName() +
                    " activated a " + clickedBooster.getStrength() + "x " + clickedBooster.getType() +
                    " booster for " + com.prismforge.boosters.util.TimeUtil.formatSeconds(clickedBooster.getDuration()) + "!");
            player.closeInventory();
        }
    }

    public void openPersonal(Player player) {
        UUID uuid = player.getUniqueId();
        List<PlayerBooster> boosters = manager.getBoosters(uuid);
        int size = Math.max(9, ((boosters.size() / 9) + 1) * 9);
        Inventory inv = Bukkit.createInventory(null, size, ChatColor.BLUE + "Your Boosters");

        for (int i = 0; i < boosters.size(); i++) {
            PlayerBooster booster = boosters.get(i);
            ItemStack item = new ItemStack(Material.EMERALD);
            ItemMeta meta = item.getItemMeta();
            meta.setDisplayName(ChatColor.GOLD + booster.getType() + " Booster");
            meta.setLore(Arrays.asList(
                    ChatColor.YELLOW + "Strength: " + booster.getStrength() + "x",
                    ChatColor.YELLOW + "Duration: " + com.prismforge.boosters.util.TimeUtil.formatSeconds(booster.getDuration()),
                    ChatColor.GREEN + "Click to activate!"
            ));
            item.setItemMeta(meta);
            inv.setItem(i, item);
        }
        player.openInventory(inv);
    }

    public void openQueued(Player player) {
        Collection<BoosterQueue> queues = plugin.getBoosterManager().getActiveBoosters().values();
        List<Booster> allQueued = new ArrayList<Booster>();
        for (BoosterQueue q : queues) {
            if (q.getCurrent() != null) allQueued.add(q.getCurrent());
            allQueued.addAll(q.getQueue());
        }

        int size = Math.max(9, ((allQueued.size() / 9) + 1) * 9);
        Inventory inv = Bukkit.createInventory(null, size, ChatColor.BLUE + "Queued Boosters");

        for (int i = 0; i < allQueued.size(); i++) {
            Booster booster = allQueued.get(i);
            ItemStack item = new ItemStack(Material.CLOCK);
            ItemMeta meta = item.getItemMeta();
            meta.setDisplayName(ChatColor.AQUA + booster.getType() + " Booster");
            meta.setLore(Arrays.asList(
                    ChatColor.YELLOW + "Strength: " + booster.getStrength() + "x",
                    ChatColor.YELLOW + "Time Remaining: " + com.prismforge.boosters.util.TimeUtil.formatSeconds(booster.getDurationSeconds())
            ));
            item.setItemMeta(meta);
            inv.setItem(i, item);
        }

        player.openInventory(inv);
    }


}
