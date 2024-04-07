package com.arkflame.staffmodex.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.PlayerInventory;

import com.arkflame.staffmodex.StaffModeX;
import com.arkflame.staffmodex.hotbar.Hotbar;
import com.arkflame.staffmodex.hotbar.HotbarItem;

public class PlayerListeners implements Listener {
    @EventHandler
    public void onPlayerJoin(final PlayerJoinEvent event) {
        Player player = event.getPlayer();
        player.sendMessage("Joined Server");
    }

    @EventHandler
    public void onPlayerDropItem(final PlayerDropItemEvent event) {
        Player player = event.getPlayer();
        PlayerInventory inventory = player.getInventory();
        if (StaffModeX.getInstance().getHotbarManager().isHotbarItem(player, inventory.getHeldItemSlot())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onInteract(final PlayerInteractEvent event) {
        if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            Player player = event.getPlayer();
            Hotbar hotbar = StaffModeX.getInstance().getHotbarManager().getHotbar(player);
            if (hotbar != null) {
                PlayerInventory inventory = player.getInventory();
                int slot = inventory.getHeldItemSlot();
                HotbarItem hotbarItem = hotbar.getItem(slot);
                if (hotbarItem != null) {
                    hotbarItem.onInteract(player);
                    event.setCancelled(true);
                }
            }
        }
    }
}
