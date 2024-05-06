package com.arkflame.staffmodex.hotbar.components;

import com.arkflame.staffmodex.StaffModeX;
import com.arkflame.staffmodex.hotbar.Hotbar;
import com.arkflame.staffmodex.hotbar.components.items.CpsHotbarItem;
import com.arkflame.staffmodex.hotbar.components.items.ExamineHotbarItem;
import com.arkflame.staffmodex.hotbar.components.items.FollowHotbarItem;
import com.arkflame.staffmodex.hotbar.components.items.FreezeHotbarItem;
import com.arkflame.staffmodex.hotbar.components.items.KnockbackHotbarItem;
import com.arkflame.staffmodex.hotbar.components.items.PhaseHotbarItem;
import com.arkflame.staffmodex.hotbar.components.items.PlayersHotbarItem;
import com.arkflame.staffmodex.hotbar.components.items.RandomTeleportHotbarItem;
import com.arkflame.staffmodex.hotbar.components.items.StaffListHotbarItem;
import com.arkflame.staffmodex.hotbar.components.items.VanishHotbarItem;
import com.arkflame.staffmodex.modernlib.config.ConfigWrapper;
import com.arkflame.staffmodex.player.StaffPlayer;

public class StaffHotbar extends Hotbar {
    public StaffHotbar(StaffPlayer staffPlayer) {
        super();
        ConfigWrapper config = StaffModeX.getInstance().getCfg();
        if (config.getBoolean("items.hotbar.phase.enabled")) setItem(config.getInt("items.hotbar.phase.slot"), new PhaseHotbarItem());
        if (config.getBoolean("items.hotbar.r-teleport.enabled")) setItem(config.getInt("items.hotbar.r-teleport.slot"), new RandomTeleportHotbarItem());
        if (config.getBoolean("items.hotbar.vanish.enabled")) setItem(config.getInt("items.hotbar.vanish.slot"), new VanishHotbarItem(staffPlayer));
        if (config.getBoolean("items.hotbar.players.enabled")) setItem(config.getInt("items.hotbar.players.slot"), new PlayersHotbarItem());
        if (config.getBoolean("items.hotbar.stafflist.enabled")) setItem(config.getInt("items.hotbar.stafflist.slot"), new StaffListHotbarItem());
        if (config.getBoolean("items.hotbar.freeze.enabled")) setItem(config.getInt("items.hotbar.freeze.slot"), new FreezeHotbarItem());
        if (config.getBoolean("items.hotbar.cps.enabled")) setItem(config.getInt("items.hotbar.cps.slot"), new CpsHotbarItem());
        if (config.getBoolean("items.hotbar.examine.enabled")) setItem(config.getInt("items.hotbar.examine.slot"), new ExamineHotbarItem());
        if (config.getBoolean("items.hotbar.follow.enabled")) setItem(config.getInt("items.hotbar.follow.slot"), new FollowHotbarItem());
        if (config.getBoolean("items.hotbar.knockback.enabled")) setItem(config.getInt("items.hotbar.knockback.slot"), new KnockbackHotbarItem());
    }
}
