package com.arkflame.staffmodex.managers;

import com.arkflame.staffmodex.StaffModeX;
import com.arkflame.staffmodex.hotbar.HotbarManager;
import com.arkflame.staffmodex.hotbar.components.StaffHotbar;
import com.arkflame.staffmodex.modernlib.config.ConfigWrapper;
import com.arkflame.staffmodex.modernlib.utils.ChatColors;
import com.arkflame.staffmodex.modernlib.utils.Players;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.HashSet;

public class StaffModeManager {
    private final Collection<Player> staffPlayers = new HashSet<>();

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
            ConfigWrapper msg = StaffModeX.getInstance().getMsg();
            player.sendMessage(ChatColors.color(msg.getText("staffmode.leave")));

            removeStaff(player);
        } else {
            // Activate
            StaffModeX.getInstance().getInventoryManager().savePlayerInventory(player);
            StaffModeX.getInstance().getVanishManager().makeInvisible(player);
            Players.clearInventory(player);
            hotbarManager.setHotbar(player, new StaffHotbar());
            Players.setFlying(player, true);
            ConfigWrapper msg = StaffModeX.getInstance().getMsg();
            player.sendMessage(ChatColors.color(msg.getText("staffmode.enter")));

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
