package com.arkflame.staffmodex.menus.items;

import org.bukkit.Material;
import org.bukkit.entity.Player;

import com.arkflame.staffmodex.StaffModeX;
import com.arkflame.staffmodex.modernlib.menus.items.MenuItem;

public class FreezePlayerItem extends MenuItem {
    private Player player;

    public FreezePlayerItem(Player player) {
        super(Material.BLAZE_ROD, StaffModeX.getInstance().getMsg().getText("menus.freezePlayer.title"),
                StaffModeX.getInstance().getFreezeManager().isFrozen(player) ? StaffModeX.getInstance().getMsg().getText("menus.freezePlayer.frozen") : StaffModeX.getInstance().getMsg().getText("menus.freezePlayer.notFrozen"));
        this.player = player;
    }
    
    @Override
    public void onClick(int slot) {
        StaffModeX.getInstance().getFreezeManager().toggleFreeze(player);
        setLore(StaffModeX.getInstance().getFreezeManager().isFrozen(player) ? StaffModeX.getInstance().getMsg().getText("menus.freezePlayer.frozen") : StaffModeX.getInstance().getMsg().getText("menus.freezePlayer.notFrozen"));
    }
}
