package com.arkflame.staffmodex.hotbar.components.items;

import com.arkflame.staffmodex.StaffModeX;
import com.arkflame.staffmodex.hotbar.HotbarItem;
import com.arkflame.staffmodex.modernlib.menus.Menu;
import com.arkflame.staffmodex.modernlib.menus.items.MenuItem;
import com.arkflame.staffmodex.modernlib.utils.ChatColors;
import com.arkflame.staffmodex.modernlib.utils.Materials;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.stream.Collectors;

public class MinerGuiHotbarItem extends HotbarItem {
    public MinerGuiHotbarItem() {
        super(Material.PAPER,
                StaffModeX.getInstance().getMsg().getText("hotbar.minergui.name"),
                1, (short) 0,
                StaffModeX.getInstance().getMsg().getTextList("hotbar.minergui.lore"));
    }

    @Override
    public void onInteract(Player player) {
        // Menu with heads of all players that are currently below Y 60 (mining)
        Menu menu = new Menu(ChatColors.color(StaffModeX.getInstance().getMsg().getText("hotbar.minergui.menu_title")), 4);
        // Get players below coordinate Y 60
        Collection<Player> miners = player.getWorld().getPlayers().stream().filter(p -> p.getLocation().getY() < 60)
                .collect(Collectors.toSet());
        int i = 0;
        // Add heads of players to menu
        for (Player miner : miners) {
            menu.setItem(i++, new PlayerItem(miner) {
                @Override
                public void onClick() {
                    player.closeInventory();
                    player.teleport(miner.getLocation());
                    player.sendMessage(ChatColors.color(StaffModeX.getInstance().getMsg().getText("hotbar.minergui.teleport").replace("{player}", miner.getName())));
                }
            });
        }
        menu.setItem(menu.getSize() - 9, new MenuItem(Material.ARROW, StaffModeX.getInstance().getMsg().getText("hotbar.minergui.close")) {
            @Override
            public void onClick() {
                player.closeInventory();
            }
        });
        menu.setBackground(Materials.get("STAINED_GLASS_PANE", "GRAY_STAINED_GLASS_PANE"), (short) 7, " ");
        menu.openInventory(player);
    }
}
