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

    public boolean isForceVanish() {
        return getPlayer().hasPermission("staffmodex.vanish.force");
    }

    public void hidePlayer(boolean force, Player toBeHidden) {
        Player player = getPlayer();

        if (player == toBeHidden) {
            return;
        }

        if (force || !player.hasPermission("staffmodex.vanish.bypass")) {
            player.hidePlayer(toBeHidden);
        }
    }

    public void makeInvisible() {
        Player player = getPlayer();
        boolean force = isForceVanish();

        for (StaffPlayer staffPlayer : StaffModeX.getInstance().getStaffPlayerManager().getStaffPlayers().values()) {
            staffPlayer.hidePlayer(force, player);
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
