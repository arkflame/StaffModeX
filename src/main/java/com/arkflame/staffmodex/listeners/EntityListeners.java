package com.arkflame.staffmodex.listeners;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityTargetEvent;

import com.arkflame.staffmodex.StaffModeX;
import com.arkflame.staffmodex.player.StaffPlayer;

public class EntityListeners implements Listener {

    @EventHandler(ignoreCancelled = true)
    public void onEntityDamage(EntityDamageEvent event) {
        Entity entity = event.getEntity();

        if (entity instanceof Player) {
            Player player = (Player) entity;
            StaffPlayer staffPlayer = StaffModeX.getInstance().getStaffPlayerManager().getOrCreateStaffPlayer(player);
            if (staffPlayer.isFrozen() || StaffModeX.getInstance().getStaffModeManager().isStaff(player)) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        Entity damager = event.getDamager();

        if (damager instanceof Player) {
            Player player = (Player) damager;
            StaffPlayer staffPlayer = StaffModeX.getInstance().getStaffPlayerManager().getOrCreateStaffPlayer(player);
            if (staffPlayer.isFrozen() || StaffModeX.getInstance().getStaffModeManager().isStaff(player)) {
                event.setCancelled(true);
                return;
            }
        }

        Entity entity = event.getEntity();

        if (entity instanceof Player) {
            Player player = (Player) entity;
            StaffPlayer staffPlayer = StaffModeX.getInstance().getStaffPlayerManager().getOrCreateStaffPlayer(player);
            if (staffPlayer.isFrozen() || StaffModeX.getInstance().getStaffModeManager().isStaff(player)) {
                event.setCancelled(true);
                return;
            }
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onEntityTarget(EntityTargetEvent event) {
        Entity target = event.getTarget();

        if (target instanceof Player) {
            Player player = (Player) target;
            boolean staff = StaffModeX.getInstance().getStaffModeManager().isStaff(player);

            if (staff) {
                event.setCancelled(true);
            }

            StaffPlayer staffPlayer = StaffModeX.getInstance().getStaffPlayerManager()
                    .getOrCreateStaffPlayer(player);

            if (staffPlayer == null) {
                return;
            }

            if (staffPlayer.isFrozen()) {
                event.setCancelled(true);
            }
        }
    }
}
