package com.arkflame.staffmodex.hotbar.components.items;

import com.arkflame.staffmodex.StaffModeX;
import com.arkflame.staffmodex.hotbar.HotbarItem;
import com.arkflame.staffmodex.modernlib.menus.Menu;
import com.arkflame.staffmodex.modernlib.menus.items.MenuItem;
import com.arkflame.staffmodex.modernlib.utils.Materials;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class PlayersHotbarItem extends HotbarItem {
    public PlayersHotbarItem() {
        super(Materials.get(StaffModeX.getInstance().getConfig().getStringList("items.hotbar.players.material")),
                StaffModeX.getInstance().getMsg().getText("hotbar.players.name"),
                1, (short) 0,
                StaffModeX.getInstance().getMsg().getTextList("hotbar.players.lore"));
    }

    @Override
    public void onInteract(Player player) {
        // Menu with heads of all players that are currently below Y 60 (mining)
        Menu menu = new Menu(StaffModeX.getInstance().getMsg().getText("hotbar.players.menu_title"),
                4);
        int i = 0;
        // Add heads of players to menu
        for (Player otherPlayer : Bukkit.getOnlinePlayers()) {
            if (i > 26) {
                break;
            }
            if (player != otherPlayer) {
                menu.setItem(i++, new PlayerItem(player, otherPlayer) {
                    @Override
                    public void onClick() {
                        player.closeInventory();
                        player.teleport(otherPlayer.getLocation());
                        player.sendMessage(StaffModeX.getInstance().getMsg()
                                .getText("hotbar.players.teleport").replace("{player}", otherPlayer.getName()));
                    }
                });
            }
        }
        menu.setItem(menu.getSize() - 9,
                new MenuItem(Material.ARROW, StaffModeX.getInstance().getMsg().getText("hotbar.players.close")) {
                    @Override
                    public void onClick() {
                        player.closeInventory();
                    }
                });
        menu.setBackground(Materials.get(StaffModeX.getInstance().getCfg().getStringList("items.fill.material")), (short) StaffModeX.getInstance().getCfg().getInt("items.fill.damage"), StaffModeX.getInstance().getCfg().getText("items.fill.name"));
        menu.openInventory(player);
    }
}
