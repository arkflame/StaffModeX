package com.arkflame.staffmodex.menus.items;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import com.arkflame.staffmodex.StaffModeX;
import com.arkflame.staffmodex.modernlib.menus.items.MenuItem;

public class LocationItem extends MenuItem {
    private Player target;
    private Player opener;

    public LocationItem(Location location, Player target, Player opener) {
        super(Material.MAP, 1, (short) 0, StaffModeX.getInstance().getMsg().getText("menus.location.title"),
                StaffModeX.getInstance().getMsg().getTextList("menus.location.lore",
                        "{world}", location.getWorld().getName(),
                        "{x}", String.valueOf(location.getBlockX()),
                        "{y}", String.valueOf(location.getBlockY()),
                        "{z}", String.valueOf(location.getBlockZ())));
        this.target = target;
        this.opener = opener;
    }

    @Override
    public void onClick() {
        if (target != null && target.isOnline()) {
            if (opener != null && opener.isOnline()) {
                if (opener.hasPermission("staffmodex.teleport")) {
                    opener.teleport(target);
                    opener.sendMessage(StaffModeX.getInstance().getMessage("menus.location.teleported"));
                } else {
                    opener.sendMessage(StaffModeX.getInstance().getMessage("menus.location.no-permission-teleport"));
                }
            }
        }
    }
}
