package com.arkflame.staffmodex.player;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.arkflame.staffmodex.StaffModeX;
import com.arkflame.staffmodex.modernlib.config.ConfigWrapper;

public class VanishPlayer extends UUIDPlayer {
    private boolean vanished = false;

    public VanishPlayer(UUID uuid) {
        super(uuid);
    }

    public void toggleVanish() {
        ConfigWrapper msg = StaffModeX.getInstance().getMsg();
        Player player = getPlayer();
        if (!player.hasPermission("staffmodex.vanish")) {
            player.sendMessage(msg.getText("messages.vanish.no-permission"));
        } else if (!StaffModeX.getInstance().getStaffModeManager().isStaff(player)) {
            player.sendMessage(msg.getText("messages.vanish.not-staff"));
        } else if (isVanished()) {
            makeVisible();
            player.sendMessage(StaffModeX.getInstance().getMsg().getText("messages.vanish.unvanished"));
        } else {
            makeInvisible();
            player.sendMessage(StaffModeX.getInstance().getMsg().getText("messages.vanish.vanished"));
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
