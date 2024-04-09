package com.arkflame.staffmodex.menus.items;

import org.bukkit.Material;
import org.bukkit.entity.Player;

import com.arkflame.staffmodex.modernlib.menus.items.MenuItem;

public class PlayerNotesItem extends MenuItem {
    public PlayerNotesItem(Player player) {
        super(Material.PAPER, "&bNotes", "&bNotes: &7" + "(NOT IMPLEMENTED)");
    }
}
