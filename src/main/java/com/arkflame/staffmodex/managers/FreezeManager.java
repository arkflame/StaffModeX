package com.arkflame.staffmodex.managers;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerMoveEvent;

import com.arkflame.staffmodex.StaffModeX;
import com.arkflame.staffmodex.modernlib.config.ConfigWrapper;

public class FreezeManager {
    private final Set<UUID> frozenPlayers = new HashSet<>();

    public void toggleFreeze(Player player, Player target) {
        ConfigWrapper msg = StaffModeX.getInstance().getMsg();
        if (!player.hasPermission("staffmode.freeze")) {
            player.sendMessage(msg.getText("messages.freeze.no_permission"));
        } else if (target.hasPermission("staffmode.freeze.bypass")) {
            player.sendMessage(msg.getText("messages.freeze.has_bypass"));
        } else if (isFrozen(target)) {
            player.sendMessage(msg.getText("messages.freeze.unfreeze"));
            unfreezePlayer(target);
        } else {
            player.sendMessage(msg.getText("messages.freeze.freeze"));
            freezePlayer(target);
        }
    }

    private void freezePlayer(Player player) {
        frozenPlayers.add(player.getUniqueId());
        player.sendMessage(StaffModeX.getInstance().getMsg().getText("messages.freeze.frozen"));
    }

    private void unfreezePlayer(Player player) {
        frozenPlayers.remove(player.getUniqueId());
        player.sendMessage(StaffModeX.getInstance().getMsg().getText("messages.freeze.unfrozen"));
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
