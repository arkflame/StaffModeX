package com.arkflame.staffmodex.player;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.arkflame.staffmodex.StaffModeX;
import com.arkflame.staffmodex.modernlib.config.ConfigWrapper;

import de.myzelyam.api.vanish.VanishAPI;

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
            player.sendMessage(StaffModeX.getInstance().getMessage("messages.vanish.unvanished"));
        } else {
            makeInvisible();
            player.sendMessage(StaffModeX.getInstance().getMessage("messages.vanish.vanished"));
        }
    }

    public boolean isForceVanish() {
        return getPlayer().hasPermission("staffmodex.vanish.force");
    }

    public boolean isPremiumVanishHook() {
        return StaffModeX.getInstance().getCfg().getBoolean("vanish.hooks.premiumvanish")
                && StaffModeX.getInstance().getServer().getPluginManager().isPluginEnabled("PremiumVanish");
    }

    public void hidePlayer(boolean force, Player toBeHidden) {
        Player player = getPlayer();

        if (player == null) {
            return;
        }

        if (player == toBeHidden) {
            return;
        }

        if (force || !player.hasPermission("staffmodex.vanish.bypass")) {
            boolean premiumVanishHook = isPremiumVanishHook();
            if (premiumVanishHook) {
                VanishAPI.hidePlayer(toBeHidden);
            } else {
                player.hidePlayer(toBeHidden);
            }
        }
    }

    public void makeInvisible() {
        Player player = getPlayer();
        boolean force = isForceVanish();

        for (StaffPlayer staffPlayer : StaffModeX.getInstance().getStaffPlayerManager().getStaffPlayers().values()) {
            staffPlayer.hidePlayer(force, player);
        }
        vanished = true;
        StaffModeX.getInstance().setVisible(player, false);
    }

    public void makeVisible() {
        Player player = getPlayer();
        boolean premiumVanishHook = isPremiumVanishHook();
        for (Player otherPlayer : Bukkit.getOnlinePlayers()) {
            if (premiumVanishHook) {
                VanishAPI.showPlayer(player);
            } else {
                otherPlayer.showPlayer(player);
            }
        }
        vanished = false;
        StaffModeX.getInstance().setVisible(player, true);
    }

    public boolean isVanished() {
        return vanished;
    }
}
