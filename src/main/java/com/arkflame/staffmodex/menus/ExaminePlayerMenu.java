package com.arkflame.staffmodex.menus;

import org.bukkit.entity.Player;

import com.arkflame.staffmodex.StaffModeX;
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
        setItem(9 + 0, new FoodItem(target, player));
        setItem(9 + 1, new ConnectionItem(target, player));
        setItem(9 + 2, new GameModeItem(target));
        
        if (StaffModeX.getInstance().getCfg().getBoolean("warning.enabled") || StaffModeX.getInstance().getCfg().getBoolean("report.enabled")) {
            setItem(9 + 3, infractionItem);
        }

        setItem(9 + 4, new LocationItem(target.getLocation(), target, player));
        setItem(9 + 5, new PlayerNotesItem(player, target));
        
        if (StaffModeX.getInstance().getCfg().getBoolean("freeze.enabled")) {
            setItem(9 + 6, new FreezePlayerItem(player, target));
        }
        
        if (StaffModeX.getInstance().getCfg().getBoolean("warning.enabled")) {
            setItem(9 + 7, new WarnPlayerItem(infractionItem, player, target));
        }
        
        setItem(9 + 8, new PlayerInventoryItem(player, target, this));

        setBackground(Materials.get(StaffModeX.getInstance().getCfg().getStringList("items.fill.material")), (short) StaffModeX.getInstance().getCfg().getInt("items.fill.damage"), StaffModeX.getInstance().getCfg().getText("items.fill.name"));
    }

    @Override
    public void openInventory(Player player) {
        if (!player.hasPermission("staffmodex.examine")) {
            player.sendMessage(StaffModeX.getInstance().getMessage("messages.examine.no-permission"));
            return;
        }
        
        super.openInventory(player);
    }
}
