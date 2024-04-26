package com.arkflame.staffmodex.menus;

import org.bukkit.entity.Player;

import com.arkflame.staffmodex.menus.items.ConnectionItem;
import com.arkflame.staffmodex.menus.items.FoodItem;
import com.arkflame.staffmodex.menus.items.FreezePlayerItem;
import com.arkflame.staffmodex.menus.items.GameModeItem;
import com.arkflame.staffmodex.menus.items.InfractionItem;
import com.arkflame.staffmodex.menus.items.LocationItem;
import com.arkflame.staffmodex.menus.items.PlayerInventoryItem;
import com.arkflame.staffmodex.menus.items.PlayerNotesItem;
import com.arkflame.staffmodex.menus.items.WarnPlayerItem;
import com.arkflame.staffmodex.modernlib.menus.Menu;
import com.arkflame.staffmodex.modernlib.utils.Materials;

public class ExaminePlayerMenu extends Menu {

    public ExaminePlayerMenu(Player player, Player target) {
        super("&6Examining " + target.getName(), 3);
        InfractionItem infractionItem = new InfractionItem(player, target);
        setItem(9 + 0, new FoodItem(target));
        setItem(9 + 1, new ConnectionItem(target));
        setItem(9 + 2, new GameModeItem(target));
        setItem(9 + 3, infractionItem);
        setItem(9 + 4, new LocationItem(target.getLocation()));
        setItem(9 + 5, new PlayerNotesItem(player, target));
        setItem(9 + 6, new FreezePlayerItem(player, target));
        setItem(9 + 7, new WarnPlayerItem(infractionItem, player, target));
        setItem(9 + 8, new PlayerInventoryItem(player, target, this));

        setBackground(Materials.get("STAINED_GLASS_PANE", "GRAY_STAINED_GLASS_PANE"), (short) 7, " ");
    }
}
