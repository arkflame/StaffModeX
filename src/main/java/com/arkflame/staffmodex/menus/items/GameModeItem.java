package com.arkflame.staffmodex.menus.items;

import org.bukkit.Material;
import org.bukkit.entity.Player;

import com.arkflame.staffmodex.StaffModeX;
import com.arkflame.staffmodex.modernlib.menus.items.MenuItem;

public class GameModeItem extends MenuItem {
    public GameModeItem(Player player) {
        super(Material.GRASS,
                StaffModeX.getInstance().getMsg().getText("menus.gameMode.title"),
                StaffModeX.getInstance().getMsg().getText("menus.gameMode.gameMode").replace("{gamemode}", player.getGameMode().toString()));
    }
}
