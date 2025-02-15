package com.arkflame.staffmodex.listeners;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;

import com.arkflame.staffmodex.StaffModeX;
import com.arkflame.staffmodex.managers.VeinManager;
import com.arkflame.staffmodex.player.StaffPlayer;

import java.util.Collection;
import java.util.UUID;

public class BlockListeners implements Listener {
    private VeinManager veinManager;

    public BlockListeners(VeinManager veinManager) {
        this.veinManager = veinManager;
    }

    @EventHandler(ignoreCancelled = true)
    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();

        if (StaffModeX.getInstance().getStaffModeManager().isStaff(player)) {
            event.setCancelled(true);
            return;
        }

        StaffPlayer staffPlayer = StaffModeX.getInstance().getStaffPlayerManager()
                .getOrCreateStaffPlayer(player);

        if (staffPlayer == null) {
            return;
        }

        if (staffPlayer.isFrozen()) {
            staffPlayer
                    .sendMessage(StaffModeX.getInstance().getMessage("messages.freeze.cannot-interact"));
            event.setCancelled(true);
        }

        Block block = event.getBlock();

        if (StaffModeX.getInstance().getCfg().getBoolean("diamond-finder.enabled")) {
            // If the mined block is a diamond ore, get a hashset with all the diamond ores
            // in the same vein.
            Collection<Block> vein = veinManager.getConnectedDiamondOres(player, block);
            if (vein != null) {
                int minStreak = StaffModeX.getInstance().getCfg().getInt("diamond-finder.min-streak");
                int streak = veinManager.getStreak(player);

                if (streak >= minStreak) {
                    UUID uuid = player.getUniqueId();
                    String size = String.valueOf(vein.size());
                    StaffModeX.getInstance().sendMessageToStaffPlayers(
                            StaffModeX.getInstance().getMessage("messages.diamond-finder.found", "{player}",
                                    player.getName(), "{streak}", String.valueOf(streak), "{size}", size),
                            uuid);
                }
            }
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onBlockPlace(BlockPlaceEvent event) {
        Player player = event.getPlayer();

        if (StaffModeX.getInstance().getStaffModeManager().isStaff(player)) {
            if (!player.hasPermission("staffmodex.build")) {
                event.setCancelled(true);
                return;
            }
        }

        StaffPlayer staffPlayer = StaffModeX.getInstance().getStaffPlayerManager()
                .getOrCreateStaffPlayer(player);

        if (staffPlayer == null) {
            return;
        }

        if (staffPlayer.isFrozen()) {
            staffPlayer.sendMessage(StaffModeX.getInstance().getMessage("messages.freeze.cannot-interact"));
            event.setCancelled(true);
        }
    }
}
