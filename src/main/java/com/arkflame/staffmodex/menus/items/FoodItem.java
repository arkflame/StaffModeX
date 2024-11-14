package com.arkflame.staffmodex.menus.items;

import org.bukkit.Material;
import org.bukkit.entity.Player;

import com.arkflame.staffmodex.StaffModeX;
import com.arkflame.staffmodex.menus.ExaminePlayerMenu;
import com.arkflame.staffmodex.modernlib.menus.items.MenuItem;

public class FoodItem extends MenuItem {
    private Player player;
    private Player opener;

    public FoodItem(Player player, Player opener) {
        super(Material.BREAD, 1, (short) 0, StaffModeX.getInstance().getMsg().getText("menus.food.title"), StaffModeX.getInstance().getMsg().getTextList("menus.food.lore", "{health}", String.valueOf((int) player.getHealth()), "{maxHealth}", String.valueOf((int) player.getMaxHealth()), "{food}", String.valueOf(player.getFoodLevel())));
        this.player = player;
        this.opener = opener;
    }

    @Override
    public void onClick() {
        if (player != null && player.isOnline()) {
            if (opener != null && opener.isOnline()) {
                if (opener.hasPermission("staffmodex.heal")) {
                    boolean wasHealedOrFed = (player.getHealth() < player.getMaxHealth()) || (player.getFoodLevel() < 20);

                    if (wasHealedOrFed) {
                        player.setHealth(player.getMaxHealth());
                        player.setFoodLevel(20);
                        new ExaminePlayerMenu(opener, player).openInventory(opener);
                    }
                    player.sendMessage(StaffModeX.getInstance().getMessage("menus.food.healed"));
                } else {
                    player.sendMessage(StaffModeX.getInstance().getMessage("menus.food.no-permission-heal"));
                }
            }
        }
    }
}
