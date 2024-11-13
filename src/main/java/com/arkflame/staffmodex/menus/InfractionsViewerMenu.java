package com.arkflame.staffmodex.menus;

import java.util.List;

import org.bukkit.Material;
import org.bukkit.entity.Player;

import com.arkflame.staffmodex.StaffModeX;
import com.arkflame.staffmodex.infractions.Infraction;
import com.arkflame.staffmodex.infractions.InfractionType;
import com.arkflame.staffmodex.modernlib.menus.Menu;
import com.arkflame.staffmodex.modernlib.menus.items.MenuItem;
import com.arkflame.staffmodex.player.StaffPlayer;
import com.arkflame.staffmodex.modernlib.utils.Materials;

public class InfractionsViewerMenu extends Menu {
    public InfractionsViewerMenu(InfractionType type, Player player, StaffPlayer staffPlayer, Menu oldMenu) {
        super(StaffModeX.getInstance().getMsg().getText("messages.infractions-menu-title"), 3);

        addInfractionsItems(type, player, staffPlayer, oldMenu);

        setBackground(Materials.get(StaffModeX.getInstance().getCfg().getStringList("items.fill.material")), (short) StaffModeX.getInstance().getCfg().getInt("items.fill.damage"), StaffModeX.getInstance().getCfg().getText("items.fill.name"));
    }

    private void addInfractionsItems(InfractionType type, Player player, StaffPlayer staffPlayer, Menu oldMenu) {
        List<Infraction> infractions = type == InfractionType.REPORT ? staffPlayer.getReports().getInfractions()
                : staffPlayer.getWarnings().getInfractions();

        int slot = 0; // Start from the last slot and iterate backwards
        for (int i = infractions.size() - 1; i >= 0; i--) {
            Infraction infraction = infractions.get(i);
            Material material = type == InfractionType.REPORT ? Material.BOOK : Material.REDSTONE;
            String itemName = type == InfractionType.REPORT ? StaffModeX.getInstance().getMsg().getText("messages.report.title") : StaffModeX.getInstance().getMsg().getText("messages.warning.title");
            setItem(slot, new MenuItem(material, itemName, StaffModeX.getInstance().getMsg().getText("messages.reason-label", "{reason}", infraction.getReason()), StaffModeX.getInstance().getMsg().getText("messages.reporter-label", "{reporter}", infraction.getReporterName()), StaffModeX.getInstance().getMsg().getText("messages.time-label", "{time}", infraction.getTimestamp())));
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
