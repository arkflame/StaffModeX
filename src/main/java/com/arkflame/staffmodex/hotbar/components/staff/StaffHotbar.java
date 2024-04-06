package com.arkflame.staffmodex.hotbar.components.staff;

import com.arkflame.staffmodex.hotbar.Hotbar;

public class StaffHotbar extends Hotbar {
    public StaffHotbar() {
        super();
        addItem(0, new StaffHotbarItem());
    }
}
