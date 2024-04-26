package com.arkflame.staffmodex.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.scheduler.BukkitRunnable;

import com.arkflame.staffmodex.StaffModeX;
import com.arkflame.staffmodex.cps.CpsTestingManager;
import com.arkflame.staffmodex.hotbar.Hotbar;
import com.arkflame.staffmodex.hotbar.HotbarItem;
import com.arkflame.staffmodex.player.StaffNote;
import com.arkflame.staffmodex.player.StaffPlayer;

public class PlayerListeners implements Listener {
    @EventHandler(ignoreCancelled = true)
    public void onAsyncPlayerChat(final AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        StaffPlayer staffPlayer = StaffModeX.getInstance().getStaffPlayerManager().getStaffPlayer(player.getUniqueId());

        if (staffPlayer == null) {
            return;
        }

        if (staffPlayer.isWritingNote()) {
            String text = event.getMessage();
            new BukkitRunnable() {
                @Override
                public void run() {
                    staffPlayer.openWriteMenu(player, text);
                }
            }.runTask(StaffModeX.getInstance());
            StaffNote note = staffPlayer.writeNote(text);
            event.setCancelled(true);
            player.sendMessage(StaffModeX.getInstance().getMsg().getText("messages.note-writing-success").replace("{player}", note.getName()));
        }

        if (staffPlayer.getWarningProcess().isInProgress()) {
            String text = event.getMessage();
            String warnedName = staffPlayer.getWarningProcess().getTarget().getName();
            staffPlayer.getWarningProcess().complete(text);
            new BukkitRunnable() {
                @Override
                public void run() {
                    staffPlayer.getWarningProcess().openWarningMenu(player);
                }
            }.runTask(StaffModeX.getInstance());
            staffPlayer.getWarningProcess().clear();
            event.setCancelled(true);
            player.sendMessage(StaffModeX.getInstance().getMsg().getText("messages.warning-success").replace("{player}", warnedName));
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerJoin(final PlayerJoinEvent event) {
        Player player = event.getPlayer();
        if (StaffModeX.getInstance().getVanishManager().isVanished(player)) {
            event.setJoinMessage(null);
        }
        StaffModeX.getInstance().getHotbarManager().setHotbar(player, null);
        StaffModeX.getInstance().getInventoryManager().loadPlayerInventory(player);
        StaffModeX.getInstance().getInventoryManager().deletePlayerInventory(player);
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerQuit(final PlayerQuitEvent event) {
        Player player = event.getPlayer();
        if (StaffModeX.getInstance().getVanishManager().isVanished(player)) {
            event.setQuitMessage(null);
        }
        StaffModeX.getInstance().getHotbarManager().setHotbar(player, null);
        StaffModeX.getInstance().getInventoryManager().loadPlayerInventory(player);
        StaffModeX.getInstance().getInventoryManager().deletePlayerInventory(player);
        StaffModeX.getInstance().getStaffModeManager().removeStaff(player);
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
        boolean leftClick = event.getAction() == Action.LEFT_CLICK_AIR || event.getAction() == Action.LEFT_CLICK_BLOCK;
        boolean rightClick = event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK;

        if (leftClick) {
            CpsTestingManager.click(event.getPlayer());
        }
        
        if (leftClick || rightClick) {
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

    @EventHandler(ignoreCancelled = true)
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();
        if (StaffModeX.getInstance().getStaffModeManager().isStaff(player)) {
            event.setKeepInventory(true);
        }
    }
}
