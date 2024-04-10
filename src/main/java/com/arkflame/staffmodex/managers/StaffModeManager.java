package com.arkflame.staffmodex.managers;

import java.util.Collection;
import java.util.HashSet;

import org.bukkit.entity.Player;

import com.arkflame.staffmodex.StaffModeX;
import com.arkflame.staffmodex.hotbar.HotbarManager;
import com.arkflame.staffmodex.hotbar.components.StaffHotbar;
import com.arkflame.staffmodex.modernlib.utils.ChatColors;
import com.arkflame.staffmodex.modernlib.utils.Players;

public class StaffModeManager {
    private Collection<Player> staffPlayers = new HashSet<>();

    public void toggleStaff(Player player) {
        HotbarManager hotbarManager = StaffModeX.getInstance().getHotbarManager();

        if (isStaff(player)) {
            // Deactivate
            hotbarManager.setHotbar(player, null);
            Players.clearInventory(player);
            StaffModeX.getInstance().getInventoryManager().loadPlayerInventory(player);
            StaffModeX.getInstance().getInventoryManager().deletePlayerInventory(player);
            StaffModeX.getInstance().getVanishManager().makeVisible(player);
            Players.setFlying(player, false);
            player.sendMessage(ChatColors.color("&aYou have left staff mode!"));
            
            removeStaff(player);
        } else {
            // Activate
            StaffModeX.getInstance().getInventoryManager().savePlayerInventory(player);
            StaffModeX.getInstance().getVanishManager().makeInvisible(player);
            Players.clearInventory(player);
            hotbarManager.setHotbar(player, new StaffHotbar());
            Players.setFlying(player, true);
            player.sendMessage(ChatColors.color("&aYou have entered staff mode!"));

            addStaff(player);
        }
    }

    public void addStaff(Player player) {
        staffPlayers.add(player);
    }

    public void removeStaff(Player player) {
        staffPlayers.remove(player);
    }

    public boolean isStaff(Player player) {
        return staffPlayers.contains(player);
    }

    public Collection<Player> getStaffPlayers() {
        return staffPlayers;
    }
}
