package com.arkflame.staffmodex.player;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class UUIDPlayer {
    private UUID uuid;
    private String name = null;

    public UUIDPlayer(UUID uuid) {
        this.uuid = uuid;
    }

    public UUID getUUID() {
        return uuid;
    }

    public Player getPlayer() {
        return Bukkit.getPlayer(uuid);
    }

    public void sendMessage(String msg) {
        Player player = getPlayer();
        if (player != null) {
            player.sendMessage(msg);
        }
    }

    public String getName() {
        if (name != null) {
            return name;
        } 
        Player player = getPlayer();
        if (player != null) {
            name = player.getName();
            return name;
        }
        return "N/A";
    }
}
