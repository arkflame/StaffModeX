package com.arkflame.staffmodex.menus.items;

import org.bukkit.Material;
import org.bukkit.entity.Player;

import com.arkflame.staffmodex.modernlib.menus.items.MenuItem;

public class ConnectionItem extends MenuItem {
    public ConnectionItem(Player examinPlayer) {
        super(Material.COMPASS, "&bConnection", "&bIP: &7" + examinPlayer.getAddress().getAddress().getHostAddress(), "&bPort: &7" + examinPlayer.getAddress().getPort());
    }
}
