package com.arkflame.staffmodex.menus.items;

import org.bukkit.Material;
import org.bukkit.entity.Player;

import com.arkflame.staffmodex.StaffModeX;
import com.arkflame.staffmodex.modernlib.menus.items.MenuItem;

public class ConnectionItem extends MenuItem {

    public ConnectionItem(Player target) {
        super(Material.COMPASS, StaffModeX.getInstance().getMsg().getText("menus.connection.title"),
                StaffModeX.getInstance().getMsg().getTextList("menus.connection.lore", "{ip}", target.getAddress().getAddress().getHostAddress(), "{port}", String.valueOf(target.getAddress().getPort())).toArray(new String[0]));
    }
}
