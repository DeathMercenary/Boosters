package com.prismforge.boosters.commands;

import com.prismforge.boosters.Boosters;
import com.prismforge.boosters.gui.BoosterGUI;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * /boosters command
 * Opens the booster GUI for the player.
 */
public class BoostersCommand implements CommandExecutor {

    private final Boosters plugin;
    private final BoosterGUI boosterGUI;

    public BoostersCommand(Boosters plugin) {
        this.plugin = plugin;
        this.boosterGUI = new BoosterGUI(plugin);
        plugin.getCommand("boosters").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("§cOnly players can use this command.");
            return true;
        }

        Player player = (Player) sender;
        boosterGUI.open(player);
        return true;
    }
}
