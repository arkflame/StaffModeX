package com.arkflame.staffmodex.menus.items;

import org.bukkit.Material;
import org.bukkit.entity.Player;

import com.arkflame.staffmodex.modernlib.menus.items.MenuItem;

public class FoodItem extends MenuItem {
    public FoodItem(Player player) {
        super(Material.BREAD, "&bFood", "&bHealth: &7" + (int) player.getHealth() + "/" + (int) player.getMaxHealth(), "&bFood: &7" + player.getFoodLevel() + "/20");
    }
}
