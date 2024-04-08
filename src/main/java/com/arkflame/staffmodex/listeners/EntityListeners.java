package com.arkflame.staffmodex.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

import com.arkflame.staffmodex.StaffModeX;
import com.arkflame.staffmodex.hotbar.Hotbar;

public class EntityListeners implements Listener {

    @EventHandler(ignoreCancelled = true)
    public void onEntityDamage(EntityDamageEvent event) {
        if (event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();
            // Get the hotbar the player has
            Hotbar hotbar = StaffModeX.getInstance().getHotbarManager().getHotbar(player);

            // Check your condition here
            if (hotbar == Hotbar.STAFF_HOTBAR) {
                // Prevent damage if the condition is met
                event.setCancelled(true);
            }
        }
    }
}
