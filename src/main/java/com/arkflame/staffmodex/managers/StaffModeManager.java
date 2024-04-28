package com.arkflame.staffmodex.managers;

import com.arkflame.staffmodex.StaffModeX;
import com.arkflame.staffmodex.hotbar.HotbarManager;
import com.arkflame.staffmodex.hotbar.components.StaffHotbar;
import com.arkflame.staffmodex.modernlib.config.ConfigWrapper;
import com.arkflame.staffmodex.modernlib.utils.ChatColors;
import com.arkflame.staffmodex.modernlib.utils.Players;
import com.arkflame.staffmodex.modernlib.utils.PotionEffects;
import com.arkflame.staffmodex.player.StaffPlayer;

import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.HashSet;

public class StaffModeManager {
    private final Collection<Player> staffPlayers = new HashSet<>();

    public void toggleStaff(Player player) {
        if (isStaff(player)) {
            removeStaff(player);
        } else {
            addStaff(player);
        }
    }

    public void addStaff(Player player) {
        if (isStaff(player)) {
            return;
        }

        HotbarManager hotbarManager = StaffModeX.getInstance().getHotbarManager();
        /* Save this location */
        StaffPlayer staffPlayer = StaffModeX.getInstance().getStaffPlayerManager().getOrCreateStaffPlayer(player);
        if (staffPlayer != null) {
            staffPlayer.setOldLocation(player.getLocation());
            staffPlayer.makeInvisible();
            staffPlayer.setStaffChat(true);
        }
        StaffModeX.getInstance().getInventoryManager().savePlayerInventory(player);
        Players.clearInventory(player);
        hotbarManager.setHotbar(player, new StaffHotbar());
        Players.setFlying(player, true);
        ConfigWrapper msg = StaffModeX.getInstance().getMsg();
        player.sendMessage(ChatColors.color(msg.getText("staffmode.enter")));
        staffPlayers.add(player);
        PotionEffects.add(player, 0, Integer.MAX_VALUE, "NIGHT_VISION");
        PotionEffects.add(player, 0, Integer.MAX_VALUE, "JUMP", "JUMP_BOOST");
        PotionEffects.add(player, 1, Integer.MAX_VALUE, "SPEED");
    }

    public void removeStaff(Player player) {
        if (!isStaff(player)) {
            return;
        }

        HotbarManager hotbarManager = StaffModeX.getInstance().getHotbarManager();
        /* Restore old location */
        StaffPlayer staffPlayer = StaffModeX.getInstance().getStaffPlayerManager().getOrCreateStaffPlayer(player);
        if (staffPlayer != null) {
            staffPlayer.restoreOldLocation();
            staffPlayer.makeVisible();
        }
        hotbarManager.setHotbar(player, null);
        Players.clearInventory(player);
        StaffModeX.getInstance().getInventoryManager().loadPlayerInventory(player);
        StaffModeX.getInstance().getInventoryManager().deletePlayerInventory(player);
        Players.setFlying(player, false);
        ConfigWrapper msg = StaffModeX.getInstance().getMsg();
        player.sendMessage(ChatColors.color(msg.getText("staffmode.leave")));
        staffPlayers.remove(player);
        PotionEffects.remove(player, "NIGHT_VISION");
        PotionEffects.remove(player, "JUMP", "JUMP_BOOST");
        PotionEffects.remove(player, "SPEED");
    }

    public boolean isStaff(Player player) {
        return staffPlayers.contains(player);
    }

    public Collection<Player> getStaffPlayers() {
        return staffPlayers;
    }
}
