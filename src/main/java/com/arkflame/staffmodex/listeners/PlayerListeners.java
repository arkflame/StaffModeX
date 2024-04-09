package com.arkflame.staffmodex.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.PlayerInventory;

import com.arkflame.staffmodex.StaffModeX;
import com.arkflame.staffmodex.hotbar.Hotbar;
import com.arkflame.staffmodex.hotbar.HotbarItem;

public class PlayerListeners implements Listener {
    @EventHandler(ignoreCancelled = true)
    public void onPlayerJoin(final PlayerJoinEvent event) {
        Player player = event.getPlayer();
        StaffModeX.getInstance().getHotbarManager().setHotbar(player, null);
        StaffModeX.getInstance().getInventoryManager().loadPlayerInventory(player);
        StaffModeX.getInstance().getInventoryManager().deletePlayerInventory(player);
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerDropItem(final PlayerDropItemEvent event) {
        Player player = event.getPlayer();
        if (StaffModeX.getInstance().getHotbarManager().getHotbar(player) != null) {
            event.setCancelled(true);
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerPickupItem(final PlayerPickupItemEvent event) {
        Player player = event.getPlayer();
        if (StaffModeX.getInstance().getHotbarManager().getHotbar(player) != null) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerInteract(final PlayerInteractEvent event) {
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

    @EventHandler
    public void onPlayerInteractEntity(final PlayerInteractEntityEvent event) {
        Player player = event.getPlayer();
        Hotbar hotbar = StaffModeX.getInstance().getHotbarManager().getHotbar(player);
        if (hotbar != null) {
            PlayerInventory inventory = player.getInventory();
            int slot = inventory.getHeldItemSlot();
            HotbarItem hotbarItem = hotbar.getItem(slot);
            if (hotbarItem != null) {
                hotbarItem.onInteract(player, event.getRightClicked());
                event.setCancelled(true);
            }
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerMove(PlayerMoveEvent event) {
        StaffModeX.getInstance().getFreezeManager().preventMovement(event);
    }
}
