package com.arkflame.staffmodex.menus.items;

import org.bukkit.Material;
import org.bukkit.entity.Player;

import com.arkflame.staffmodex.StaffModeX;
import com.arkflame.staffmodex.modernlib.menus.items.MenuItem;

public class ConnectionItem extends MenuItem {
    private final Player examinPlayer;

    public ConnectionItem(Player examinPlayer) {
        super(Material.COMPASS, StaffModeX.getInstance().getMsg().getText("menus.connection.title"),
                StaffModeX.getInstance().getMsg().getText("menus.connection.ip") + examinPlayer.getAddress().getAddress().getHostAddress(),
                StaffModeX.getInstance().getMsg().getText("menus.connection.port") + examinPlayer.getAddress().getPort());
        this.examinPlayer = examinPlayer;
    }
}
