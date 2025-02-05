package com.arkflame.staffmodex.hotbar.components.items;

import com.arkflame.staffmodex.StaffModeX;
import com.arkflame.staffmodex.hotbar.HotbarItem;
import com.arkflame.staffmodex.modernlib.utils.Materials;
import com.arkflame.staffmodex.modernlib.config.ConfigWrapper;
import com.arkflame.staffmodex.modernlib.menus.Menu;
import com.arkflame.staffmodex.modernlib.menus.items.MenuItem;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.HashSet;

public class StaffListHotbarItem extends HotbarItem {
    public StaffListHotbarItem() {
        super(Materials.get(StaffModeX.getInstance().getCfg().getStringList("items.hotbar.stafflist.material")),
                StaffModeX.getInstance().getMsg().getText("hotbar.stafflist.name"),
                1, (short) 0,
                StaffModeX.getInstance().getMsg().getTextList("hotbar.stafflist.lore"));
    }

    @Override
    public void onInteract(Player player) {
        Menu menu = new Menu(StaffModeX.getInstance().getMsg().getText("hotbar.stafflist.menu_title"),
                3);

        // Item to return to the menu
        menu.setItem(2 * 9,
                new MenuItem(Material.ARROW,
                        StaffModeX.getInstance().getMsg().getText("hotbar.stafflist.close")) {
                    @Override
                    public void onClick() {
                        player.closeInventory();
                    }
                });

        // Set background
        menu.setBackground(Materials.get(StaffModeX.getInstance().getCfg().getStringList("items.fill.material")), (short) StaffModeX.getInstance().getCfg().getInt("items.fill.damage"), StaffModeX.getInstance().getCfg().getText("items.fill.name"));
        menu.openInventory(player);

        // Set items async
        Bukkit.getScheduler().runTaskAsynchronously(StaffModeX.getInstance(), () -> {
            Collection<String> online = StaffModeX.getInstance().getRedisManager().getOnline();
            if (online == null) {
                online = new HashSet<>();
                for (Player ply : Bukkit.getOnlinePlayers()) {
                    if (ply.hasPermission("staffmodex.staffmode")) {
                        online.add(ply.getName());
                    }
                }
            }

            int i = 0;
            for (String playerName : online) {
                if (i >= 2 * 9) {
                    break;
                }
                if (playerName != null) {
                    int currentIndex = i++;
                    String serverName = StaffModeX.getInstance().getRedisManager().getConnectedServer(playerName);
                    Bukkit.getScheduler().runTask(StaffModeX.getInstance(), () -> {
                        ConfigWrapper msg = StaffModeX.getInstance().getMsg();
                        String server = (serverName == null || serverName.isEmpty() ? "N/A" : serverName);
                        Player otherPlayer = Bukkit.getPlayer(playerName);
                        boolean isOnline = otherPlayer != null;
                        String here = isOnline ? msg.getText("hotbar.staff_player_item.here") : "";
                        if (isOnline) {
                            menu.setItem(currentIndex, new OnlineStaffItem(playerName, msg, server, here, player, otherPlayer));
                        } else {
                            menu.setItem(currentIndex, new OfflineStaffItem(playerName, msg, server, here));
                        }
                    });
                }
            }
            player.updateInventory();
        });
    }
}
