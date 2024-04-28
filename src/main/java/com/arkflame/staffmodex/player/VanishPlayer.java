package com.arkflame.staffmodex.player;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class VanishPlayer extends UUIDPlayer {
    private boolean vanished = false;

    public VanishPlayer(UUID uuid) {
        super(uuid);
    }

    public void toggleVanish() {
        if (isVanished()) {
            makeVisible();
        } else {
            makeInvisible();
        }
    }

    public void makeInvisible() {
        Player player = getPlayer();
        boolean force = player.hasPermission("staffmodex.vanish.force");

        for (Player otherPlayer : Bukkit.getOnlinePlayers()) {
            if (force || !otherPlayer.hasPermission("staffmodex.vanish.bypass")) {
                otherPlayer.hidePlayer(player);
            }
        }
        vanished = true;
    }

    public void makeVisible() {
        Player player = getPlayer();
        for (Player otherPlayer : Bukkit.getOnlinePlayers()) {
            otherPlayer.showPlayer(player);
        }
        vanished = false;
    }

    public boolean isVanished() {
        return vanished;
    }
}
