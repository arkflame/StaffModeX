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
        Hotbar hotbar = getHotbar(player);
        if (hotbar!= null) {
            
        for (int slot : hotbar.getSlots()) {
            player.getInventory().setItem(slot, null);
        }
        }
        players.remove(player);
    }

    public boolean isHotbarItem(Player player, int slot) {
        Hotbar hotbar = getHotbar(player);
        if (hotbar != null) {
            HotbarItem hotbarItem = hotbar.getItem(slot);
            if (hotbarItem != null) {
                return true;
            }
        }

        return false;
    }
}
