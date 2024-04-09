package com.arkflame.staffmodex.menus.items;

import org.bukkit.Material;
import org.bukkit.entity.Player;

import com.arkflame.staffmodex.modernlib.menus.items.MenuItem;

public class WarnPlayerItem extends MenuItem {
    public WarnPlayerItem(Player player) {
        super(Material.PAPER, "&bWarn Player", "&7Warn this user");
    }
}
