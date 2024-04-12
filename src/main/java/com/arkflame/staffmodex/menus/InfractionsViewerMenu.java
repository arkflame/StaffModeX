package com.arkflame.staffmodex.menus;

import org.bukkit.Material;
import org.bukkit.entity.Player;

import com.arkflame.staffmodex.StaffModeX;
import com.arkflame.staffmodex.infractions.Infraction;
import com.arkflame.staffmodex.infractions.InfractionList;
import com.arkflame.staffmodex.infractions.InfractionType;
import com.arkflame.staffmodex.modernlib.menus.Menu;
import com.arkflame.staffmodex.modernlib.menus.items.MenuItem;
import com.arkflame.staffmodex.player.StaffPlayer;
import com.arkflame.staffmodex.modernlib.utils.Materials;

public class InfractionsViewerMenu extends Menu {
    public InfractionsViewerMenu(InfractionType type, Player player, StaffPlayer staffPlayer, Menu oldMenu) {
        super(StaffModeX.getInstance().getMsg().getText("messages.infractions-menu-title"), 3);

        addInfractionsItems(type, player, staffPlayer, oldMenu);

        setBackground(Materials.get("STAINED_GLASS_PANE", "GRAY_STAINED_GLASS_PANE"), (short) 7, " ");
    }

    private void addInfractionsItems(InfractionType type, Player player, StaffPlayer staffPlayer, Menu oldMenu) {
        InfractionList infractions = type == InfractionType.REPORT ? staffPlayer.getReports()
                : staffPlayer.getWarnings();

        int slot = 0;
        for (Infraction infraction : infractions.getInfractions()) {
            Material material = type == InfractionType.REPORT ? Material.BOOK : Material.REDSTONE;
            String itemName = type == InfractionType.REPORT ? StaffModeX.getInstance().getMsg().getText("messages.report-title") : StaffModeX.getInstance().getMsg().getText("messages.warning-title");
            setItem(slot, new MenuItem(material, itemName, StaffModeX.getInstance().getMsg().getText("messages.reason-label", "{reason}", infraction.getReason()), StaffModeX.getInstance().getMsg().getText("messages.reporter-label", "{reporter}", infraction.getReporter()), StaffModeX.getInstance().getMsg().getText("messages.time-label", "{time}", infraction.getTimestamp())));
            slot++;
        }

        // Add back button
        setItem(getSize() - 1, new MenuItem(Material.ARROW, StaffModeX.getInstance().getMsg().getText("messages.back-button"), StaffModeX.getInstance().getMsg().getText("messages.return-to-previous-menu")) {
            @Override
            public void onClick() {
                oldMenu.openInventory(player);
            }
        });
    }
}
