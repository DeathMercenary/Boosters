package com.prismforge.boosters;

import com.prismforge.boosters.command.BoosterCommand;
import com.prismforge.boosters.gui.BoosterGUI;
import com.prismforge.boosters.manager.BoosterManager;
import com.prismforge.boosters.manager.ConfigManager;
import com.prismforge.boosters.manager.PlayerBoosterManager;
import com.prismforge.boosters.task.BoosterTickTask;
import com.prismforge.boosters.util.BossBarUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class Boosters extends JavaPlugin {

    private static Boosters instance;
    private BoosterManager boosterManager;
    private ConfigManager configManager;

    @Override
    public void onEnable() {
        instance = this;

        saveDefaultConfig();

        this.boosterManager = new BoosterManager(this);
        this.configManager = new ConfigManager(this);
        this.playerBoosterManager = new PlayerBoosterManager(this);


        configManager.loadBoosters(boosterManager);

        getCommand("booster").setExecutor(new BoosterCommand(this));

        new BoosterTickTask(boosterManager).runTaskTimer(this, 20L, 20L); // every second
        this.bossBarUtil = new BossBarUtil(this);

        for (Player player : Bukkit.getOnlinePlayers()) {
            playerBoosterManager.load(player.getUniqueId());
        }
        Bukkit.getPluginManager().registerEvents(new org.bukkit.event.Listener() {
            @org.bukkit.event.EventHandler
            public void onJoin(org.bukkit.event.player.PlayerJoinEvent event) {
                playerBoosterManager.load(event.getPlayer().getUniqueId());
                bossBarUtil.sharedBar.addPlayer(event.getPlayer());
            }
            @org.bukkit.event.EventHandler
            public void onQuit(org.bukkit.event.player.PlayerQuitEvent event) {
                playerBoosterManager.unload(event.getPlayer().getUniqueId());
            }
        }, this);

        this.boosterGUI = new BoosterGUI(this);
        boosterManager.loadGlobalBoosters();


        getLogger().info("[Boosters] Plugin enabled successfully.");
    }
    @Override
    public void onDisable() {
        boosterManager.saveGlobalBoosters();
        bossBarUtil.clearAll();
        playerBoosterManager.saveAll();
        getLogger().info("[Boosters] Plugin disabled.");
    }
    private BoosterGUI boosterGUI;

    public BoosterGUI getBoosterGUI() {
        return boosterGUI;
    }
    private BossBarUtil bossBarUtil;
    public BossBarUtil getBossBarUtil() {
        return bossBarUtil;
    }


    private PlayerBoosterManager playerBoosterManager;

    public PlayerBoosterManager getPlayerBoosterManager() {
        return playerBoosterManager;
    }

    public static Boosters getInstance() {
        return instance;
    }

    public BoosterManager getBoosterManager() {
        return boosterManager;
    }
}
