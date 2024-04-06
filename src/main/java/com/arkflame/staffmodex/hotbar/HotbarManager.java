package com.arkflame.staffmodex.hotbar;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.entity.Player;

public class HotbarManager {
    private Map<Player, Hotbar> players = new HashMap<>();

    public Hotbar getHotbar(Player player) {
        return players.getOrDefault(player, null);
    }

    public void setHotbar(Player player, Hotbar hotbar) {
        if (hotbar == null) {
            removeHotbar(player);
        } else {
            players.put(player, hotbar);
            hotbar.give(player);
        }
    }

    public void removeHotbar(Player player) {
        players.remove(player);
    }
}
