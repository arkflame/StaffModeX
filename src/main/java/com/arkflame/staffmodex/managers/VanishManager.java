package com.arkflame.staffmodex.managers;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class VanishManager {
    private final Set<UUID> vanishedPlayers = new HashSet<>();

    public void toggleVanish(Player player) {
        if (isVanished(player)) {
            makeVisible(player);
        } else {
            makeInvisible(player);
        }
    }

    public void makeInvisible(Player player) {
        for (Player otherPlayer : Bukkit.getOnlinePlayers()) {
            otherPlayer.hidePlayer(player);
        }
        vanishedPlayers.add(player.getUniqueId());
        player.sendMessage(ChatColor.GREEN + "You are now invisible to other players.");
    }

    public void makeVisible(Player player) {
        for (Player otherPlayer : Bukkit.getOnlinePlayers()) {
            otherPlayer.showPlayer(player);
        }
        vanishedPlayers.remove(player.getUniqueId());
        player.sendMessage(ChatColor.GREEN + "You are now visible to other players.");
    }

    public boolean isVanished(Player player) {
        return vanishedPlayers.contains(player.getUniqueId());
    }
}
