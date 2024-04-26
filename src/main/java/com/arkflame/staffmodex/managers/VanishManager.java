package com.arkflame.staffmodex.managers;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Bukkit;
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
        boolean force = player.hasPermission("staffmode.vanish.force");

        for (Player otherPlayer : Bukkit.getOnlinePlayers()) {
            if (force || !otherPlayer.hasPermission("staffmode.vanish.bypass")) {
                otherPlayer.hidePlayer(player);
            }
        }
        vanishedPlayers.add(player.getUniqueId());
    }

    public void makeVisible(Player player) {
        for (Player otherPlayer : Bukkit.getOnlinePlayers()) {
            otherPlayer.showPlayer(player);
        }
        vanishedPlayers.remove(player.getUniqueId());
    }

    public boolean isVanished(Player player) {
        return vanishedPlayers.contains(player.getUniqueId());
    }
}
