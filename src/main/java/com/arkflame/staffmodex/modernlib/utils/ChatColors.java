package com.arkflame.staffmodex.modernlib.utils;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class ChatColors {
    public static String color(String text) {
        return ChatColor.translateAlternateColorCodes('&', text);
    }
    
    public static void sendMessage(Player player, String text) {
        player.sendMessage(color(text));
    }
}
