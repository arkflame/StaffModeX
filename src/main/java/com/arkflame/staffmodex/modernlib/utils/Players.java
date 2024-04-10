package com.arkflame.staffmodex.modernlib.utils;

import org.bukkit.entity.Player;

public class Players {
    public static void setFlying(Player player, boolean flying) {
        player.setAllowFlight(flying);
        player.setFlying(flying);
    }

    public static void clearInventory(Player player) {
        player.getInventory().clear();
    }
}
