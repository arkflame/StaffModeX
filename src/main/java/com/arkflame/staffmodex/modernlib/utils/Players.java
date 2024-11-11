package com.arkflame.staffmodex.modernlib.utils;

import java.util.List;

import org.bukkit.GameMode;
import org.bukkit.entity.Player;

public class Players {
    public static void setFlying(Player player, boolean flying) {
        if (player.getAllowFlight() != flying) {
            player.setAllowFlight(flying);
        }
        if (player.isFlying() != flying) {
            player.setFlying(flying);
        }
    }

    public static void clearInventory(Player player) {
        player.getInventory().setArmorContents(null);
        player.getInventory().clear();
    }

    public static void sendMessage(Player player, List<String> textList) {
        for (String text : textList) {
            player.sendMessage(text);
        }
    }

    public static void heal(Player player) {
        player.setHealth(player.getMaxHealth());
        player.setFoodLevel(20);
    }

    public static void setGameMode(Player player, String gameModeName) {
        try {
            setGameMode(player, GameMode.valueOf(gameModeName));
        } catch (IllegalStateException ex) {
            // Ignore
        }
    }

    public static void setGameMode(Player player, GameMode gameMode) {
        player.setGameMode(gameMode);
    }

    public static String getIP(Player target, Player player) {
        if (player.hasPermission("staffmodex.ip")) {
            return target.getAddress().getAddress().getHostAddress();
        } else {
            return "{...}";
        }
    }
}
