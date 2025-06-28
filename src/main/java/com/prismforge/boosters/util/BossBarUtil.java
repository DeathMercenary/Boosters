package com.prismforge.boosters.util;

import com.prismforge.boosters.Boosters;
import com.prismforge.boosters.data.Booster;
import com.prismforge.boosters.manager.BoosterManager;
import org.bukkit.Bukkit;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

public class BossBarUtil {

    private final Boosters plugin;
    private final BoosterManager boosterManager;

    private final Set<UUID> toggledOn = new HashSet<UUID>();

    private final List<Booster> activeBoosterList = new ArrayList<Booster>();
    private int cycleIndex = 0;

    public final BossBar sharedBar;

    public BossBarUtil(Boosters plugin) {
        this.plugin = plugin;
        this.boosterManager = plugin.getBoosterManager();
        this.sharedBar = Bukkit.createBossBar("", BarColor.YELLOW, BarStyle.SOLID);

        // Update bossbar & cycle every second
        new BukkitRunnable() {
            @Override
            public void run() {
                updateActiveBoosterList();
                updateSharedBar();
            }
        }.runTaskTimer(plugin, 20L, 20L);
    }

    /**
     * Players toggle on/off.
     */
    public void toggle(Player player) {
        UUID uuid = player.getUniqueId();
        if (toggledOn.contains(uuid)) {
            toggledOn.remove(uuid);
            sharedBar.addPlayer(player);
            player.sendMessage("§a[Boosters] BossBar shown.");
        } else {
            toggledOn.add(uuid);
            sharedBar.removePlayer(player);
            player.sendMessage("§c[Boosters] BossBar hidden.");

        }
    }

    public boolean isToggled(Player player) {
        return toggledOn.contains(player.getUniqueId());
    }

    /**
     * Builds list of active boosters and cycles index.
     */
    private void updateActiveBoosterList() {
        activeBoosterList.clear();
        for (Booster b : boosterManager.getAllActiveBoosters()) {
            if (b != null && !b.isExpired()) {
                activeBoosterList.add(b);
            }
        }

        if (activeBoosterList.isEmpty()) {
            cycleIndex = 0;
        } else {
            cycleIndex++;
            if (cycleIndex >= activeBoosterList.size()) {
                cycleIndex = 0;
            }
        }
    }

    /**
     * Updates the shared bar content for all viewers.
     */
    private void updateSharedBar() {
        if (activeBoosterList.isEmpty()) {
            sharedBar.setVisible(false);
            return;
        }

        Booster booster = activeBoosterList.get(cycleIndex);

        sharedBar.setTitle("§e" + booster.getStrength() + "x " + booster.getType() +
                " Booster §7| " + com.prismforge.boosters.util.TimeUtil.formatSeconds(booster.getDurationSeconds()));

        double progress = 0.0;
        if (booster.getOriginalSeconds() > 0) {
            progress = (double) booster.getDurationSeconds() / booster.getOriginalSeconds();
            if (progress < 0.0) progress = 0.0;
            if (progress > 1.0) progress = 1.0;
        } else {
            progress = 0.0;
        }

        sharedBar.setProgress(progress);
        sharedBar.setVisible(true);
    }

    public void clearAll() {
        sharedBar.removeAll();
        toggledOn.clear();
    }

}
