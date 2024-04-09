package com.arkflame.staffmodex.menus.items;

import org.bukkit.Material;
import org.bukkit.entity.Player;

import com.arkflame.staffmodex.modernlib.menus.items.MenuItem;

public class InfractionItem extends MenuItem {
    public InfractionItem(Player player) {
        // Warnings, Reports, Reason
        super(Material.PAPER, "&bInfractions", "&bWarnings: &7" + "(NOT IMPLEMENTED)", "&bReports: &7" + "(NOT IMPLEMENTED)", "&bReason: &7" + "(NOT IMPLEMENTED)");
    }
}
