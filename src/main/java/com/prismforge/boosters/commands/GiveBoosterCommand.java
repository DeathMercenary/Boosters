package com.prismforge.boosters.commands;

import com.prismforge.boosters.Boosters;
import com.prismforge.boosters.booster.Booster;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.util.UUID;

public class GiveBoosterCommand implements CommandExecutor {

    private final Boosters plugin;

    public GiveBoosterCommand(Boosters plugin) {
        this.plugin = plugin;
        plugin.getCommand("givebooster").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("boosters.admin")) {
            sender.sendMessage("§cYou don't have permission.");
            return true;
        }

        if (args.length < 4) {
            sender.sendMessage("§eUsage: /givebooster <player> <type> <strength> <durationMinutes>");
            return true;
        }

        OfflinePlayer target = Bukkit.getOfflinePlayer(args[0]);
        if (target == null || (!target.isOnline() && !target.hasPlayedBefore())) {
            sender.sendMessage("§cPlayer not found.");
            return true;
        }

        String type = args[1].toLowerCase();
        double strength;
        int durationMinutes;
        try {
            strength = Double.parseDouble(args[2]);
            durationMinutes = Integer.parseInt(args[3]);
        } catch (NumberFormatException e) {
            sender.sendMessage("§cStrength and duration must be numbers.");
            return true;
        }

        Booster booster = new Booster(type, strength, durationMinutes);
        plugin.getPlayerBoosterManager().addBooster(target.getUniqueId(), booster);

        sender.sendMessage("§aGave booster §f" + strength + "x " + type + "§a to §f" + target.getName() + "§a for §f" + durationMinutes + "m.");
        return true;
    }
}
