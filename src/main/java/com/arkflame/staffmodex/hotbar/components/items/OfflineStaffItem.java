package com.arkflame.staffmodex.hotbar.components.items;

import com.arkflame.staffmodex.modernlib.config.ConfigWrapper;
import com.arkflame.staffmodex.modernlib.menus.items.MenuItem;
import com.arkflame.staffmodex.modernlib.utils.Materials;

public class OfflineStaffItem extends MenuItem {
    public OfflineStaffItem(String playerName, ConfigWrapper msg, String server, String here) {
        super(Materials.get("SKULL_ITEM", "PLAYER_HEAD"), 
        1, 
        (short) 3,
        msg.getText("hotbar.staff_player_item_offline.title", "{playerName}", playerName), 
        msg.getTextList("hotbar.staff_player_item_offline.description", "{playerName}", playerName, "{serverName}", server, "{here}", here));
    }
}
