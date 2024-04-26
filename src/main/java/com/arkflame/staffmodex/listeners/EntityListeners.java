package com.arkflame.staffmodex.listeners;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityTargetEvent;

import com.arkflame.staffmodex.StaffModeX;
import com.arkflame.staffmodex.hotbar.Hotbar;
import com.arkflame.staffmodex.hotbar.components.StaffHotbar;

public class EntityListeners implements Listener {

    @EventHandler(ignoreCancelled = true)
    public void onEntityDamage(EntityDamageEvent event) {
        if (event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();
            // Get the hotbar the player has
            Hotbar hotbar = StaffModeX.getInstance().getHotbarManager().getHotbar(player);

            // Check your condition here
            if (hotbar instanceof StaffHotbar) {
                // Prevent damage if the condition is met
                event.setCancelled(true);
            }
        }
    }
    
    @EventHandler(ignoreCancelled = true)
    public void onEntityTarget(EntityTargetEvent event) {
        Entity target = event.getTarget();

        if (target instanceof Player) {
            boolean staff = StaffModeX.getInstance().getStaffModeManager().isStaff((Player) target);

            if (staff) {
                event.setCancelled(true);
            }
        }
    }
}
