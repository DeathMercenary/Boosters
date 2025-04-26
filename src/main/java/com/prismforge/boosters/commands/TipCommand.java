package com.prismforge.boosters.commands;

import com.prismforge.boosters.Boosters;
import com.prismforge.boosters.booster.ActiveBooster;
import com.prismforge.boosters.booster.BoosterType;
import com.prismforge.boosters.tips.TipActionExecutor;
import com.prismforge.boosters.tips.TipTracker;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class TipCommand implements CommandExecutor {

    private final Boosters plugin;

    public TipCommand(Boosters plugin) {
        this.plugin = plugin;
        plugin.getCommand("tip").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("§cOnly players can use this command.");
            return true;
        }

        Player player = (Player) sender;
        List<ActiveBooster> active = plugin.getBoosterManager().getActiveBoosterManager().getActiveBoosters();

        if (active.isEmpty()) {
            player.sendMessage("§cThere are no active boosters to tip.");
            return true;
        }

        ActiveBooster booster = active.get(0); // We tip first active booster (or later show GUI)
        if (booster.getActivator().equals(player.getUniqueId())) {
            player.sendMessage("§cYou cannot tip yourself.");
            return true;
        }

        if (TipTracker.hasTipped(player.getUniqueId(), booster.getActivator())) {
            player.sendMessage("§cYou have already tipped for this booster.");
            return true;
        }

        Player creator = Bukkit.getPlayer(booster.getActivator());
        if (creator == null) {
            player.sendMessage("§cThe booster creator is not online.");
            return true;
        }

        BoosterType type = plugin.getBoosterManager().getBoosterType(booster.getType());
        if (type == null) {
            player.sendMessage("§cUnknown booster type.");
            return true;
        }

        // Execute rewards
        TipActionExecutor.executeTipActions(type.getTipCommands(), player, creator, booster.getStrength(), booster.getType());

        TipTracker.markTipped(player.getUniqueId(), booster.getActivator());
        player.sendMessage("§aYou tipped §b" + creator.getName() + "§a successfully!");
        creator.sendMessage("§aYou received a tip from §b" + player.getName() + "§a!");
        return true;
    }
}
