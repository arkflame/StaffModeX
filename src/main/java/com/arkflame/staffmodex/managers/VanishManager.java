package com.arkflame.staffmodex.managers;

import com.arkflame.staffmodex.StaffModeX;

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
        for (Player otherPlayer : Bukkit.getOnlinePlayers()) {
            otherPlayer.hidePlayer(player);
        }
        vanishedPlayers.add(player.getUniqueId());
        player.sendMessage(StaffModeX.getInstance().getMsg().getText("vanish.toggle_on"));
    }

    public void makeVisible(Player player) {
        for (Player otherPlayer : Bukkit.getOnlinePlayers()) {
            otherPlayer.showPlayer(player);
        }
        vanishedPlayers.remove(player.getUniqueId());
        player.sendMessage(StaffModeX.getInstance().getMsg().getText("vanish.toggle_off"));
    }

    public boolean isVanished(Player player) {
        return vanishedPlayers.contains(player.getUniqueId());
    }
}
