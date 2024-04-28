package com.arkflame.staffmodex.hotbar.components;

import com.arkflame.staffmodex.hotbar.Hotbar;
import com.arkflame.staffmodex.hotbar.components.items.CpsHotbarItem;
import com.arkflame.staffmodex.hotbar.components.items.ExamineHotbarItem;
import com.arkflame.staffmodex.hotbar.components.items.FollowHotbarItem;
import com.arkflame.staffmodex.hotbar.components.items.FreezeHotbarItem;
import com.arkflame.staffmodex.hotbar.components.items.LauncherHotbarItem;
import com.arkflame.staffmodex.hotbar.components.items.PlayersHotbarItem;
import com.arkflame.staffmodex.hotbar.components.items.RandomTeleportHotbarItem;
import com.arkflame.staffmodex.hotbar.components.items.StaffListHotbarItem;
import com.arkflame.staffmodex.hotbar.components.items.VanishHotbarItem;

public class StaffHotbar extends Hotbar {
    public StaffHotbar() {
        super();
        setItem(0, new LauncherHotbarItem());
        setItem(1, new RandomTeleportHotbarItem());
        setItem(2, new VanishHotbarItem());
        setItem(3, new PlayersHotbarItem());
        setItem(4, new StaffListHotbarItem());
        setItem(5, new FreezeHotbarItem());
        setItem(6, new CpsHotbarItem());
        setItem(7, new ExamineHotbarItem());
        setItem(8, new FollowHotbarItem());
    }
}
