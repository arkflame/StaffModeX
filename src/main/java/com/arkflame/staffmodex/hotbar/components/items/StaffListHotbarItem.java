package com.arkflame.staffmodex.hotbar.components.items;

import com.arkflame.staffmodex.StaffModeX;
import com.arkflame.staffmodex.hotbar.HotbarItem;
import com.arkflame.staffmodex.modernlib.utils.ChatColors;
import com.arkflame.staffmodex.modernlib.utils.Materials;
import com.arkflame.staffmodex.modernlib.menus.Menu;
import com.arkflame.staffmodex.modernlib.menus.items.MenuItem;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.Collection;

public class StaffListHotbarItem extends HotbarItem {
    public StaffListHotbarItem() {
        super(Materials.get("SKULL_ITEM", "SKELETON_SKULL"),
                StaffModeX.getInstance().getMsg().getText("hotbar.stafflist.name"),
                1, (short) 0,
                StaffModeX.getInstance().getMsg().getTextList("hotbar.stafflist.lore"));
    }

    @Override
    public void onInteract(Player player) {
        Menu menu = new Menu(ChatColors.color(StaffModeX.getInstance().getMsg().getText("hotbar.stafflist.menu_title")), 3);
        Collection<Player> staff = StaffModeX.getInstance().getStaffModeManager().getStaffPlayers();
        int i = 0;
        for (Player staffMember : staff) {
            if (i >= 2 * 9) {
                break;
            }
            menu.setItem(i++, new PlayerItem(staffMember));
        }

        // Item to return to the menu
        menu.setItem(2 * 9, new MenuItem(Material.ARROW, StaffModeX.getInstance().getMsg().getText("hotbar.stafflist.close")) {
            @Override
            public void onClick() {
                player.closeInventory();
            }
        });
        menu.setBackground(Materials.get("STAINED_GLASS_PANE", "GRAY_STAINED_GLASS_PANE"), (short) 7, " ");
        menu.openInventory(player);
    }
}
