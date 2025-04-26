package com.prismforge.boosters.tips;

import com.prismforge.boosters.Boosters;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.UUID;

/**
 * Executes tip actions when a player tips a booster.
 */
public class TipActionExecutor {

    public static void executeTipActions(List<String> actions, Player tipper, Player creator, double strength, String type) {
        if (actions == null || actions.isEmpty()) return;

        for (String action : actions) {
            String parsed = action
                    .replace("%tipper%", tipper.getName())
                    .replace("%creator%", creator.getName())
                    .replace("%strength%", String.valueOf(strength))
                    .replace("%type%", type);

            if (parsed.toLowerCase().startsWith("console:")) {
                String command = parsed.substring("console:".length()).trim();
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command);
            } else if (parsed.toLowerCase().startsWith("internal:")) {
                String internalCommand = parsed.substring("internal:".length()).trim();
                handleInternalFunction(internalCommand, tipper, creator, strength, type);
            } else {
                // Default fallback: assume console command
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), parsed);
            }
        }
    }

    private static void handleInternalFunction(String command, Player tipper, Player creator, double strength, String type) {
        String[] args = command.split(" ");
        if (args.length == 0) return;

        String method = args[0];

        if (method.equalsIgnoreCase("skills.addXp") && args.length >= 3) {
            String target = args[1];
            int amount;
            try {
                amount = Integer.parseInt(args[2]);
            } catch (NumberFormatException e) {
                return;
            }
            Player targetPlayer = target.equalsIgnoreCase("%tipper%") ? tipper : creator;

            if (Bukkit.getPluginManager().isPluginEnabled("Skills")) {
                try {
                    // Soft-call SkillsAPI.addXP(Player, int) without hard dependency
                    Class<?> clazz = Class.forName("com.prismforge.skills.api.SkillsAPI");
                    clazz.getMethod("addXP", Player.class, int.class).invoke(null, targetPlayer, amount);
                } catch (Exception e) {
                    // Safe fail, no crash
                    e.printStackTrace();
                }
            }
        }
    }

}
