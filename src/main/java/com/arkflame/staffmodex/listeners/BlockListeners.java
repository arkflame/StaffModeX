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
    public void onBlockBreak(final BlockBreakEvent event) {
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
                    .sendMessage(StaffModeX.getInstance().getMsg().getText("messages.freeze.cannot-interact"));
            event.setCancelled(true);
        }

        Block block = event.getBlock();

        if (StaffModeX.getInstance().getConfig().getBoolean("diamond-finder.enabled")) {
            // If the mined block is a diamond ore, get a hashset with all the diamond ores
            // in the same vein.
            Collection<Block> vein = veinManager.getConnectedDiamondOres(player, block);
            if (vein != null) {
                int minStreak = StaffModeX.getInstance().getConfig().getInt("diamond-finder.min-streak");
                int streak = veinManager.getStreak(player);

                if (streak >= minStreak) {
                    UUID uuid = player.getUniqueId();
                    String size = String.valueOf(vein.size());
                    StaffModeX.getInstance().sendMessageToStaffPlayers(
                            StaffModeX.getInstance().getMsg().getText("messages.diamond-finder.found", "{player}",
                                    player.getName(), "{streak}", String.valueOf(streak), "{size}", size),
                            uuid);
                }
            }
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onBlockPlace(final BlockPlaceEvent event) {
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
            staffPlayer.sendMessage(StaffModeX.getInstance().getMsg().getText("messages.freeze.cannot-interact"));
            event.setCancelled(true);
        }
    }
}
