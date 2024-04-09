package com.arkflame.staffmodex.menus.items;

import org.bukkit.Material;
import org.bukkit.entity.Player;

import com.arkflame.staffmodex.StaffModeX;
import com.arkflame.staffmodex.modernlib.menus.items.MenuItem;

public class FreezePlayerItem extends MenuItem {
    public FreezePlayerItem(Player player) {
        super(Material.BLAZE_ROD, "&bFreeze Player", StaffModeX.getInstance().getFreezeManager().isFrozen(player) ? "&aThis player is frozen" : "&cCurrently not frozen");
    }
}
