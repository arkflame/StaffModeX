package com.arkflame.staffmodex.hotbar.components;

import com.arkflame.staffmodex.StaffModeX;
import com.arkflame.staffmodex.hotbar.Hotbar;
import com.arkflame.staffmodex.hotbar.components.items.CpsHotbarItem;
import com.arkflame.staffmodex.hotbar.components.items.ExamineHotbarItem;
import com.arkflame.staffmodex.hotbar.components.items.FollowHotbarItem;
import com.arkflame.staffmodex.hotbar.components.items.FreezeHotbarItem;
import com.arkflame.staffmodex.hotbar.components.items.PhaseHotbarItem;
import com.arkflame.staffmodex.hotbar.components.items.PlayersHotbarItem;
import com.arkflame.staffmodex.hotbar.components.items.RandomTeleportHotbarItem;
import com.arkflame.staffmodex.hotbar.components.items.StaffListHotbarItem;
import com.arkflame.staffmodex.hotbar.components.items.VanishHotbarItem;
import com.arkflame.staffmodex.player.StaffPlayer;

public class StaffHotbar extends Hotbar {
    public StaffHotbar(StaffPlayer staffPlayer) {
        super();
        if (StaffModeX.getInstance().getConfig().getBoolean("items.hotbar.phase.enabled")) setItem(0, new PhaseHotbarItem());
        if (StaffModeX.getInstance().getConfig().getBoolean("items.hotbar.r-teleport.enabled")) setItem(1, new RandomTeleportHotbarItem());
        if (StaffModeX.getInstance().getConfig().getBoolean("items.hotbar.vanish.enabled")) setItem(2, new VanishHotbarItem(staffPlayer));
        if (StaffModeX.getInstance().getConfig().getBoolean("items.hotbar.players.enabled")) setItem(3, new PlayersHotbarItem());
        if (StaffModeX.getInstance().getConfig().getBoolean("items.hotbar.stafflist.enabled")) setItem(4, new StaffListHotbarItem());
        if (StaffModeX.getInstance().getConfig().getBoolean("items.hotbar.freeze.enabled")) setItem(5, new FreezeHotbarItem());
        if (StaffModeX.getInstance().getConfig().getBoolean("items.hotbar.cps.enabled")) setItem(6, new CpsHotbarItem());
        if (StaffModeX.getInstance().getConfig().getBoolean("items.hotbar.examine.enabled")) setItem(7, new ExamineHotbarItem());
        if (StaffModeX.getInstance().getConfig().getBoolean("items.hotbar.follow.enabled")) setItem(8, new FollowHotbarItem());
    }
}
