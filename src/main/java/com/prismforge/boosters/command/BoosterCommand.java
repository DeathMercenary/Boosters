package com.prismforge.boosters.command;

import com.prismforge.boosters.Boosters;
import com.prismforge.boosters.data.Booster;
import com.prismforge.boosters.data.BoosterQueue;
import com.prismforge.boosters.data.BoosterType;
import com.prismforge.boosters.manager.BoosterManager;
import com.prismforge.boosters.util.TimeUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.*;
import org.bukkit.entity.Player;

public class BoosterCommand implements CommandExecutor {

    private final Boosters plugin;

    public BoosterCommand(Boosters plugin) {
        this.plugin = plugin;
    }

    private void sendUsage(CommandSender sender) {
        sender.sendMessage(ChatColor.YELLOW + "Usage:");
        sender.sendMessage(ChatColor.GRAY + "/booster status <type>");
        sender.sendMessage(ChatColor.GRAY + "/booster activate <type> <strength> <duration>");
        sender.sendMessage(ChatColor.GRAY + "/createbooster <type> <display> <defaultStrength> <defaultDuration>");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        BoosterManager manager = plugin.getBoosterManager();

        if (args.length == 0) {
            if (sender instanceof Player) {
                Player player = (Player) sender;
                plugin.getBoosterGUI().openMainMenu(player);
            } else {
                sender.sendMessage(ChatColor.RED + "You must specify an argument when using this command from console.");
                sendUsage(sender);
            }
            return true;
        }
        if (args[0].equalsIgnoreCase("toggle")) {
            if (!(sender instanceof Player)) {
                sender.sendMessage(ChatColor.RED + "Only players can use this command.");
                return true;
            }
            Player player = (Player) sender;
            plugin.getBossBarUtil().toggle(player);
            return true;
        }

        if (args[0].equalsIgnoreCase("give")) {
            if (!sender.hasPermission("boosters.admin")) {
                sender.sendMessage(ChatColor.RED + "You don't have permission to use this command.");
                return true;
            }
            if (args.length < 5) {
                sender.sendMessage(ChatColor.RED + "Usage: /booster give <player> <type> <strength> <duration>");
                return true;
            }

            Player target = Bukkit.getPlayer(args[1]);
            if (target == null) {
                sender.sendMessage(ChatColor.RED + "Player not found.");
                return true;
            }

            String type = args[2].toLowerCase();
            double strength;
            int duration;

            try {
                strength = Double.parseDouble(args[3]);
                duration = Integer.parseInt(args[4]);
            } catch (NumberFormatException e) {
                sender.sendMessage(ChatColor.RED + "Invalid strength or duration.");
                return true;
            }

            if (manager.getType(type) == null) {
                sender.sendMessage(ChatColor.RED + "Booster type '" + type + "' is not registered.");
                return true;
            }

            plugin.getPlayerBoosterManager().addBooster(target.getUniqueId(),
                    new com.prismforge.boosters.data.PlayerBooster(type, strength, duration));

            sender.sendMessage(ChatColor.GREEN + "Given " + strength + "x " + type + " booster (" +
                    duration + "s) to " + target.getName() + ".");
            target.sendMessage(ChatColor.GOLD + "[Boosters] " + ChatColor.YELLOW +
                    "You received a " + strength + "x " + type + " booster for " + duration + " seconds!");

            return true;
        }
        if (args[0].equalsIgnoreCase("editduration")) {
            if (!sender.hasPermission("boosters.admin")) {
                sender.sendMessage(ChatColor.RED + "You don't have permission to use this command.");
                return true;
            }

            if (args.length < 3) {
                sender.sendMessage(ChatColor.RED + "Usage: /booster editduration <type> <seconds>");
                return true;
            }

            String type = args[1].toLowerCase();
            int newSeconds;

            try {
                newSeconds = Integer.parseInt(args[2]);
            } catch (NumberFormatException e) {
                sender.sendMessage(ChatColor.RED + "Invalid seconds value.");
                return true;
            }

            BoosterQueue queue = plugin.getBoosterManager().getActiveBoosters().get(type);
            if (queue == null) {
                sender.sendMessage(ChatColor.RED + "Invalid booster type: " + type);
                return true;
            }

            Booster current = queue.getCurrent();
            if (current == null) {
                sender.sendMessage(ChatColor.YELLOW + "No active " + type + " booster is running.");
                return true;
            }

            current.setRemainingSeconds(newSeconds);  // ✅ you must add this setter to Booster class!
            sender.sendMessage(ChatColor.GREEN + "Set active " + type + " booster duration to " + newSeconds + " seconds.");
            return true;
        }
        if (args[0].equalsIgnoreCase("gui")) {
            if (!(sender instanceof Player)) {
                sender.sendMessage(ChatColor.RED + "Only players can use this command.");
                return true;
            }
            Player player = (Player) sender;
            plugin.getBoosterGUI().openMainMenu(player);
            return true;
        }

        if (label.equalsIgnoreCase("createbooster")) {
            if (!sender.hasPermission("boosters.admin")) {
                sender.sendMessage(ChatColor.RED + "You don't have permission to use this command.");
                return true;
            }
            if (args.length < 4) {
                sender.sendMessage(ChatColor.RED + "Usage: /createbooster <type> <display> <strength> <duration>");
                return true;
            }

            String type = args[0].toLowerCase();
            String display = ChatColor.translateAlternateColorCodes('&', args[1]);
            double strength;
            int duration;

            try {
                strength = Double.parseDouble(args[2]);
                duration = Integer.parseInt(args[3]);
            } catch (NumberFormatException e) {
                sender.sendMessage(ChatColor.RED + "Invalid strength or duration.");
                return true;
            }

            BoosterType boosterType = new BoosterType(type, display, strength, duration);
            manager.registerType(boosterType);
            sender.sendMessage(ChatColor.GREEN + "Booster type '" + type + "' registered.");
            return true;
        }

        if (args[0].equalsIgnoreCase("status")) {
            if (args.length < 2) {
                sender.sendMessage(ChatColor.RED + "Usage: /booster status <type>");
                return true;
            }

            String type = args[1].toLowerCase();
            Booster active = manager.getActiveBooster(type);
            if (active != null) {
                sender.sendMessage(ChatColor.GREEN + "Current booster: " + active.getStrength() + "x " + type +
                        " booster, " + TimeUtil.formatSeconds(active.getDurationSeconds()) + " remaining.");
            } else {
                sender.sendMessage(ChatColor.YELLOW + "No active booster for type: " + type);
            }
            return true;
        }

        if (args[0].equalsIgnoreCase("activate")) {
            if (args.length < 4) {
                sender.sendMessage(ChatColor.RED + "Usage: /booster activate <type> <strength> <duration>");
                return true;
            }

            String type = args[1].toLowerCase();
            double strength;
            int duration;

            try {
                strength = Double.parseDouble(args[2]);
                duration = Integer.parseInt(args[3]);
            } catch (NumberFormatException e) {
                sender.sendMessage(ChatColor.RED + "Invalid strength or duration.");
                return true;
            }

            if (manager.getType(type) == null) {
                sender.sendMessage(ChatColor.RED + "Booster type '" + type + "' is not registered.");
                return true;
            }

            manager.addBooster(type, strength, duration);
            Bukkit.broadcastMessage(ChatColor.GOLD + "[Boosters] " + ChatColor.YELLOW +
                    (sender instanceof Player ? sender.getName() : "Console") +
                    " activated a " + strength + "x " + type + " booster for " + TimeUtil.formatSeconds(duration) + "!");
            return true;
        }

        sendUsage(sender);
        return true;
    }
}
