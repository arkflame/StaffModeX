package com.arkflame.staffmodex.menus.items;

import org.bukkit.Material;
import org.bukkit.entity.Player;

import com.arkflame.staffmodex.StaffModeX;
import com.arkflame.staffmodex.modernlib.menus.items.MenuItem;

public class FoodItem extends MenuItem {
    public FoodItem(Player player) {
        super(Material.BREAD, StaffModeX.getInstance().getMsg().getText("menus.food.title"),
                StaffModeX.getInstance().getMsg().getText("menus.food.health") + (int) player.getHealth() + "/" + (int) player.getMaxHealth(),
                StaffModeX.getInstance().getMsg().getText("menus.food.foodLevel") + player.getFoodLevel() + "/20");
    }
}
