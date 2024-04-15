package com.arkflame.staffmodex.hotbar.components.items;

import com.arkflame.staffmodex.StaffModeX;
import com.arkflame.staffmodex.hotbar.HotbarItem;
import com.arkflame.staffmodex.menus.ExaminePlayerMenu;
import com.arkflame.staffmodex.modernlib.menus.Menu;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.Arrays;

public class ExamineHotbarItem extends HotbarItem {
    public ExamineHotbarItem() {
        super(Material.CHEST,
                StaffModeX.getInstance().getMsg().getText("hotbar.examine.name"),
                1, (short) 0,
                Arrays.asList(StaffModeX.getInstance().getMsg().getTextList("hotbar.examine.lore")));
    }

    @Override
    public void onInteract(Player player, Entity target) {
        if (target instanceof Player) {
            Player targetPlayer = (Player) target;
            Menu menu = new ExaminePlayerMenu(player, targetPlayer);

            menu.openInventory(player);
        }
    }
}
