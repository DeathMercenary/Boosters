package com.prismforge.boosters.commands;

import com.prismforge.boosters.Boosters;
import com.prismforge.boosters.booster.BoosterType;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class CreateBoostCommand implements CommandExecutor {

    private final Boosters plugin;

    public CreateBoostCommand(Boosters plugin) {
        this.plugin = plugin;
        plugin.getCommand("createboost").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("boosters.admin")) {
            sender.sendMessage("§cYou don't have permission.");
            return true;
        }

        if (args.length < 1) {
            sender.sendMessage("§eUsage: /createboost <type>");
            return true;
        }

        String type = args[0].toLowerCase();

        if (plugin.getBoosterManager().getBoosterType(type) != null) {
            sender.sendMessage("§cThat booster type already exists.");
            return true;
        }

        // Add to config
        plugin.getConfig().set("boosters." + type + ".display-name", "&b" + type.substring(0, 1).toUpperCase() + type.substring(1) + " Booster");
        plugin.getConfig().set("boosters." + type + ".activate-command", "boostercommand %player% %strength% %duration%");
        plugin.saveConfig();

        // Reload boosters
        plugin.getBoosterManager().loadBoosters();

        sender.sendMessage("§aCreated booster type: §f" + type);
        return true;
    }
}
