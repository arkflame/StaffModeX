package com.arkflame.staffmodex.menus.items;

import org.bukkit.Location;
import org.bukkit.Material;

import com.arkflame.staffmodex.StaffModeX;
import com.arkflame.staffmodex.modernlib.menus.items.MenuItem;

public class LocationItem extends MenuItem {
    public LocationItem(Location location) {
        super(Material.MAP, StaffModeX.getInstance().getMsg().getText("menus.location.title"),
                StaffModeX.getInstance().getMsg().getText("menus.location.coordinates")
                        .replace("{world}", location.getWorld().getName())
                        .replace("{x}", String.valueOf(location.getBlockX()))
                        .replace("{y}", String.valueOf(location.getBlockY()))
                        .replace("{z}", String.valueOf(location.getBlockZ())));
    }
}
