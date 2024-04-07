package com.arkflame.staffmodex.hotbar.components.staff;

import java.util.Arrays;

import org.bukkit.Material;
import org.bukkit.entity.Player;

import com.arkflame.staffmodex.StaffModeX;
import com.arkflame.staffmodex.hotbar.HotbarItem;
import com.arkflame.staffmodex.modernlib.utils.ChatColors;

public class StaffHotbarItem extends HotbarItem {
    public StaffHotbarItem() {
        super(Material.DIAMOND, ChatColors.color("&cDisplayname"), 1, (short) 0, ChatColors.color(Arrays.asList("&cDisplayname")));
    }

    @Override
    public void onInteract(Player player) {
        player.sendMessage("Clicked staff hotbar item");
        StaffModeX.getInstance().getHotbarManager().setHotbar(player, null);
    }
}
