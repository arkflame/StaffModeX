package com.arkflame.staffmodex.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

import com.arkflame.staffmodex.StaffModeX;

public class BlockListeners implements Listener {
    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();

        if (StaffModeX.getInstance().getFreezeManager().isFrozen(player)) {
            event.setCancelled(true);
        }
    }
}
