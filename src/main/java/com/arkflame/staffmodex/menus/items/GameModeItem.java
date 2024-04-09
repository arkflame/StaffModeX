package com.arkflame.staffmodex.menus.items;

import org.bukkit.Material;
import org.bukkit.entity.Player;

import com.arkflame.staffmodex.modernlib.menus.items.MenuItem;

public class GameModeItem extends MenuItem {
    public GameModeItem(Player player) {
        super(Material.GRASS, "&bGameMode", "&bGameMode: &7" + player.getGameMode().toString());
    }
}
