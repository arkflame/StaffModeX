package com.arkflame.staffmodex.player;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class UUIDPlayer {
    private Player player = null;
    private UUID uuid;

    public UUIDPlayer(UUID uuid) {
        this.uuid = uuid;
    }

    public UUID getUUID() {
        return uuid;
    }

    public Player getPlayer() {
        if (player == null) {
            player = Bukkit.getPlayer(uuid);
        }
        return player;
    }

    public void sendMessage(String msg) {
        getPlayer();
        if (player != null) {
            player.sendMessage(msg);
        }
    }

    public String getName() {
        getPlayer();
        if (player != null) {
            return player.getName();
        }
        return "N/A";
    }
}
