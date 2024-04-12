package com.arkflame.staffmodex.menus;

import org.bukkit.Material;
import org.bukkit.entity.Player;

import com.arkflame.staffmodex.StaffModeX;
import com.arkflame.staffmodex.infractions.InfractionType;
import com.arkflame.staffmodex.modernlib.menus.Menu;
import com.arkflame.staffmodex.modernlib.menus.items.MenuItem;
import com.arkflame.staffmodex.player.StaffPlayer;
import com.arkflame.staffmodex.modernlib.utils.Materials;

public class InfractionsMenu extends Menu {
    public InfractionsMenu(Menu oldMenu, Player player, StaffPlayer staffPlayer) {
        super(StaffModeX.getInstance().getMsg().getText("menus.infractions.title"), 3);

        Menu menu = this;

        // Add item for warnings
        setItem(0, new MenuItem(Material.REDSTONE, StaffModeX.getInstance().getMsg().getText("menus.infractions.warnings.title"), StaffModeX.getInstance().getMsg().getText("menus.infractions.warnings.description")) {
            @Override
            public void onClick() {
                player.sendMessage(StaffModeX.getInstance().getMsg().getText("menus.infractions.warnings.opening"));
                new InfractionsViewerMenu(InfractionType.WARNING, player, staffPlayer, menu).openInventory(player);
            }
        });
        // Add item for reports
        setItem(1, new MenuItem(Material.BOOK, StaffModeX.getInstance().getMsg().getText("menus.infractions.reports.title"), StaffModeX.getInstance().getMsg().getText("menus.infractions.reports.description")) {
            @Override
            public void onClick() {
                player.sendMessage(StaffModeX.getInstance().getMsg().getText("menus.infractions.reports.opening"));
                new InfractionsViewerMenu(InfractionType.REPORT, player, staffPlayer, menu).openInventory(player);
            }
        });

        // Add back button
        setItem(getSize() - 1, new MenuItem(Material.ARROW, StaffModeX.getInstance().getMsg().getText("menus.infractions.back.title"), StaffModeX.getInstance().getMsg().getText("menus.infractions.back.description")) {
            @Override
            public void onClick() {
                oldMenu.openInventory(player);
            }
        });

        setBackground(Materials.get("STAINED_GLASS_PANE", "GRAY_STAINED_GLASS_PANE"), (short) 7, " ");
    }
}
