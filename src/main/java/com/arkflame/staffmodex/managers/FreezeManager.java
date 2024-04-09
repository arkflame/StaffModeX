package com.arkflame.staffmodex.managers;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerMoveEvent;

import net.md_5.bungee.api.ChatColor;

public class FreezeManager {
    private final Set<UUID> frozenPlayers = new HashSet<>();

    public void toggleFreeze(Player player) {
        if (isFrozen(player)) {
            unfreezePlayer(player);
        } else {
            freezePlayer(player);
        }
    }

    private void freezePlayer(Player player) {
        frozenPlayers.add(player.getUniqueId());
        player.sendMessage(ChatColor.RED + "You have been frozen and cannot move.");
        // Additional actions can be added here (e.g., hiding from other players)
    }

    private void unfreezePlayer(Player player) {
        frozenPlayers.remove(player.getUniqueId());
        player.sendMessage(ChatColor.GREEN + "You are now free to move again.");
        // Reverse any actions taken when the player was frozen
    }

    public boolean isFrozen(Player player) {
        return frozenPlayers.contains(player.getUniqueId());
    }
    
    // This method should be called in a PlayerMoveEvent listener to actually prevent movement
    public void preventMovement(PlayerMoveEvent event) {
        if (isFrozen(event.getPlayer())) {
            Location to = event.getTo();
            Location newTo = event.getFrom();
            float pitch = to.getPitch();
            float yaw = to.getYaw();
            newTo.setPitch(pitch);
            newTo.setYaw(yaw);
            event.setTo(newTo);
        }
    }
}
