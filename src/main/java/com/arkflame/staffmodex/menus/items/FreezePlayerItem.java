package com.arkflame.staffmodex.menus.items;

import org.bukkit.Material;
import org.bukkit.entity.Player;

import com.arkflame.staffmodex.StaffModeX;
import com.arkflame.staffmodex.modernlib.menus.items.MenuItem;

public class FreezePlayerItem extends MenuItem {
    private Player player;
    private Player target;

    public FreezePlayerItem(Player player, Player target) {
        super(Material.BLAZE_ROD, StaffModeX.getInstance().getMsg().getText("menus.freezePlayer.title"),
                StaffModeX.getInstance().getFreezeManager().isFrozen(target)
                        ? StaffModeX.getInstance().getMsg().getText("menus.freezePlayer.frozen")
                        : StaffModeX.getInstance().getMsg().getText("menus.freezePlayer.notFrozen"));
        this.player = player;
        this.target = target;
    }

    @Override
    public void onClick(int slot) {
        StaffModeX.getInstance().getFreezeManager().toggleFreeze(player, target);
        setLore(StaffModeX.getInstance().getFreezeManager().isFrozen(target)
                ? StaffModeX.getInstance().getMsg().getText("menus.freezePlayer.frozen")
                : StaffModeX.getInstance().getMsg().getText("menus.freezePlayer.notFrozen"));
    }
}
