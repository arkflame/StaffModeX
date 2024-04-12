package com.arkflame.staffmodex.hotbar.components.items;

import com.arkflame.staffmodex.StaffModeX;
import com.arkflame.staffmodex.hotbar.HotbarItem;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.Arrays;

public class LauncherHotbarItem extends HotbarItem {
    public LauncherHotbarItem() {
        super(Material.COMPASS,
                StaffModeX.getInstance().getMsg().getText("hotbar.launcher.name"),
                1, (short) 0,
                Arrays.asList(StaffModeX.getInstance().getMsg().getText("hotbar.launcher.lore")));
    }

    @Override
    public void onInteract(Player player) {
        // Get player's location and direction
        Location playerLocation = player.getLocation();
        Vector playerDirection = playerLocation.getDirection();

        // Calculate velocity based on player's direction
        double velocityMultiplier = 4.0; // Adjust this value as needed for desired launch strength
        Vector velocity = playerDirection.multiply(velocityMultiplier);

        // Apply velocity to player
        player.setVelocity(velocity);
    }
}
