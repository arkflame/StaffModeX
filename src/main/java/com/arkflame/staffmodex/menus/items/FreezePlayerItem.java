package com.arkflame.staffmodex.menus.items;

import org.bukkit.Material;
import org.bukkit.entity.Player;

import com.arkflame.staffmodex.StaffModeX;
import com.arkflame.staffmodex.modernlib.menus.items.MenuItem;
import com.arkflame.staffmodex.player.StaffPlayer;

public class FreezePlayerItem extends MenuItem {
    private Player player;
    private Player target;

    public FreezePlayerItem(Player player, Player target) {
        super(Material.ICE, StaffModeX.getInstance().getMsg().getText("menus.freezePlayer.title"),
                StaffModeX.getInstance().getStaffPlayerManager().getOrCreateStaffPlayer(target).isFrozen()
                        ? StaffModeX.getInstance().getMsg().getText("menus.freezePlayer.frozen")
                        : StaffModeX.getInstance().getMsg().getText("menus.freezePlayer.notFrozen"));
        this.player = player;
        this.target = target;
    }

    @Override
    public void onClick(int slot) {
        StaffPlayer staffPlayer = StaffModeX.getInstance().getStaffPlayerManager().getOrCreateStaffPlayer(target);
        staffPlayer.toggleFreeze(player);
        setLore(staffPlayer.isFrozen()
                ? StaffModeX.getInstance().getMsg().getText("menus.freezePlayer.frozen")
                : StaffModeX.getInstance().getMsg().getText("menus.freezePlayer.notFrozen"));
    }
}
