package com.arkflame.staffmodex.hotbar.components.items;

import org.bukkit.entity.Player;

import com.arkflame.staffmodex.modernlib.config.ConfigWrapper;

public class OnlineStaffItem extends PlayerItem {
    public OnlineStaffItem(String playerName, ConfigWrapper msg, String server, String here, Player player, Player otherPlayer) {
        super(player, otherPlayer);
        setDisplayName(msg.getText("hotbar.staff_player_item_offline.title", "{playerName}", playerName));
        setLore(msg.getTextList("hotbar.staff_player_item_offline.description", "{playerName}", playerName, "{serverName}", server, "{here}", here));
    }
}
