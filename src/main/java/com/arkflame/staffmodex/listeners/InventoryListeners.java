package com.arkflame.staffmodex.listeners;

import java.util.Set;

import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

import com.arkflame.staffmodex.StaffModeX;
import com.arkflame.staffmodex.hotbar.Hotbar;
import com.arkflame.staffmodex.hotbar.HotbarItem;
import com.arkflame.staffmodex.modernlib.menus.Menu;
import com.arkflame.staffmodex.modernlib.menus.items.MenuItem;

public class InventoryListeners implements Listener {
    @EventHandler(ignoreCancelled = true)
    public void onInventoryClick(InventoryClickEvent event) {
        HumanEntity human = event.getWhoClicked();
        if (human instanceof Player) {
            Player player = (Player) human;
            if (StaffModeX.getInstance().getHotbarManager().isHotbarItem(player, event.getSlot())
                    || StaffModeX.getInstance().getHotbarManager().isHotbarItem(player, event.getHotbarButton())) {
                event.setCancelled(true);
            }
        }
        Inventory inventory = event.getInventory();
        InventoryHolder holder = inventory.getHolder();

        if (holder instanceof Menu) {
            Menu menu = (Menu) holder;
            event.setCancelled(true);

            MenuItem menuItem = menu.getItem(event.getSlot());

            if (menuItem != null) {
                menuItem.onClick();
            }
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onInventoryDrag(InventoryDragEvent event) {
        HumanEntity human = event.getWhoClicked();
        if (human instanceof Player) {
            Player player = (Player) human;
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
        }
    }
}
