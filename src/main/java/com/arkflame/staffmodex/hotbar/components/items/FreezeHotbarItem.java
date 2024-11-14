package com.arkflame.staffmodex.hotbar.components.items;

import com.arkflame.staffmodex.StaffModeX;
import com.arkflame.staffmodex.hotbar.HotbarItem;
import com.arkflame.staffmodex.modernlib.utils.Materials;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public class FreezeHotbarItem extends HotbarItem {
    public FreezeHotbarItem() {
        super(Materials.get(StaffModeX.getInstance().getCfg().getStringList("items.hotbar.freeze.material")),
                StaffModeX.getInstance().getMsg().getText("hotbar.freeze.name"),
                1, (short) 0,
                StaffModeX.getInstance().getMsg().getTextList("hotbar.freeze.lore"));
    }

    @Override
    public void onInteract(Player player, Entity target) {
        if (!(target instanceof Player)) {
            player.sendMessage(StaffModeX.getInstance().getMessage("hotbar.freeze.invalid"));
            return;
        }

        StaffModeX.getInstance().getStaffPlayerManager().getOrCreateStaffPlayer((Player) target).toggleFreeze(player);
    }
}
