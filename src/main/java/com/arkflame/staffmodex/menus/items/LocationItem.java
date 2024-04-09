package com.arkflame.staffmodex.menus.items;

import org.bukkit.Location;
import org.bukkit.Material;

import com.arkflame.staffmodex.modernlib.menus.items.MenuItem;

public class LocationItem extends MenuItem {
    public LocationItem(Location location) {
        super(Material.MAP, "&bLocation", "&bLocation: &7" + location.getWorld().getName() + ", " + location.getX() + ", " + location.getY() + ", " + location.getZ());
    }
}
