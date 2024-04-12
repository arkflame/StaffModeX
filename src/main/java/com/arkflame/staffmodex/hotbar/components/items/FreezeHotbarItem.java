package com.arkflame.staffmodex.hotbar.components.items;

import com.arkflame.staffmodex.StaffModeX;
import com.arkflame.staffmodex.hotbar.HotbarItem;
import com.arkflame.staffmodex.modernlib.utils.ChatColors;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.Arrays;

public class FreezeHotbarItem extends HotbarItem {
    public FreezeHotbarItem() {
        super(Material.BLAZE_ROD,
                StaffModeX.getInstance().getMsg().getText("hotbar.freeze.name"),
                1, (short) 0,
                Arrays.asList(StaffModeX.getInstance().getMsg().getText("hotbar.freeze.lore")));
    }

    @Override
    public void onInteract(Player player, Entity target) {
        if (!(target instanceof Player)) {
            player.sendMessage(ChatColors.color(StaffModeX.getInstance().getMsg().getText("hotbar.freeze.invalid")));
            return;
        }

        StaffModeX.getInstance().getFreezeManager().toggleFreeze((Player) target);
    }
}
