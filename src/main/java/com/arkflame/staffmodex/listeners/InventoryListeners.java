package com.arkflame.staffmodex.listeners;

import java.util.Set;

import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;

import com.arkflame.staffmodex.StaffModeX;
import com.arkflame.staffmodex.hotbar.Hotbar;
import com.arkflame.staffmodex.hotbar.HotbarItem;
import com.arkflame.staffmodex.player.StaffPlayer;

public class InventoryListeners implements Listener {
    @EventHandler(ignoreCancelled = true)
    public void onInventoryClick(InventoryClickEvent event) {
        HumanEntity human = event.getWhoClicked();
        if (human instanceof Player) {
            Player player = (Player) human;
            if (event.getClickedInventory() != player.getInventory()) {
                return;
            }

            if (StaffModeX.getInstance().getStaffModeManager().isStaff(player)) {
                event.setCancelled(true);
                return;
            }

            StaffPlayer staffPlayer = StaffModeX.getInstance().getStaffPlayerManager()
                    .getOrCreateStaffPlayer(player);
    
            if (staffPlayer != null && staffPlayer.isFrozen()) {
                staffPlayer.sendMessage(StaffModeX.getInstance().getMessage("messages.freeze.cannot-click-inventory"));
                event.setCancelled(true);
            }
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onInventoryDrag(InventoryDragEvent event) {
        HumanEntity human = event.getWhoClicked();
        if (human instanceof Player) {
            Player player = (Player) human;
            if (StaffModeX.getInstance().getStaffModeManager().isStaff(player)) {
                event.setCancelled(true);
                return;
            }
            Hotbar hotbar = StaffModeX.getInstance().getHotbarManager().getHotbar(player);
            if (hotbar != null) {
                Set<Integer> slots = event.getInventorySlots();
                for (int slot : slots) {
                    HotbarItem hotbarItem = hotbar.getItem(slot);
                    if (hotbarItem != null) {
                        event.setCancelled(true);
                    }
                }
            }

        StaffPlayer staffPlayer = StaffModeX.getInstance().getStaffPlayerManager()
                .getOrCreateStaffPlayer(player);

        if (staffPlayer == null) {
            return;
        }

        if (staffPlayer.isFrozen()) {
            staffPlayer.sendMessage(StaffModeX.getInstance().getMessage("messages.freeze.cannot-drag-inventory"));
            event.setCancelled(true);
        }
        }
    }
}
