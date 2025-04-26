package com.prismforge.boosters.booster;

import com.prismforge.boosters.Boosters;
import org.bukkit.Bukkit;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;

/**
 * Handles bossbar display for boosters.
 */
public class BossbarHandler {

    private static BossBar bossBar;

    /**
     * Updates the bossbar to show a booster.
     */
    public static void updateBossbar(ActiveBooster booster) {
        if (bossBar == null) {
            bossBar = Bukkit.createBossBar("", BarColor.BLUE, BarStyle.SOLID);
        }

        String boosterName = Boosters.getInstance().getBoosterManager().getBoosterType(booster.getType()).getDisplayName();
        double progress = Math.max(0.0, Math.min(1.0, booster.getRemainingMillis() / (booster.getEndTimeMillis() - (System.currentTimeMillis() - booster.getRemainingMillis()))));

        bossBar.setProgress(progress);
        bossBar.setTitle("§b§l" + booster.getStrength() + "x " + boosterName + "§f by " + getPlayerName(booster.getActivator()));

        for (Player player : Bukkit.getOnlinePlayers()) {
            bossBar.addPlayer(player);
        }

        bossBar.setVisible(true);
    }

    /**
     * Hides the bossbar.
     */
    public static void hideBossbarForAll() {
        if (bossBar != null) {
            bossBar.removeAll();
            bossBar.setVisible(false);
        }
    }

    private static String getPlayerName(java.util.UUID uuid) {
        Player player = Bukkit.getPlayer(uuid);
        return (player != null) ? player.getName() : "Unknown";
    }
}
