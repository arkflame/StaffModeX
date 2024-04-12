package com.arkflame.staffmodex.modernlib.utils;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class ChatColors {
    public static String color(String text) {
        if (text == null) {
            return null;
        }
        return ChatColor.translateAlternateColorCodes('&', text);
    }

    public static List<String> color(List<String> text) {
        for (int i = 0; i < text.size(); i++) {
            text.set(i, color(text.get(i)));
        }
        return text;
    }
    
    public static void sendMessage(Player player, String text) {
        player.sendMessage(color(text));
    }
}
