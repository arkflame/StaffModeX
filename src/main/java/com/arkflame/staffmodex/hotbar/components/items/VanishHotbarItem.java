package com.arkflame.staffmodex.hotbar.components.items;

import com.arkflame.staffmodex.StaffModeX;
import com.arkflame.staffmodex.hotbar.HotbarItem;
import com.arkflame.staffmodex.modernlib.utils.Materials;
import com.arkflame.staffmodex.player.StaffPlayer;

import org.bukkit.entity.Player;

public class VanishHotbarItem extends HotbarItem {
    public VanishHotbarItem() {
        super(Materials.get("LIME_DYE", "INK_SACK"),
                StaffModeX.getInstance().getMsg().getText("hotbar.vanish.name"),
                1, (short) 10,
                StaffModeX.getInstance().getMsg().getTextList("hotbar.vanish.lore"));
    }

    @Override
    public void onInteract(Player player) {
        StaffPlayer staffPlayer = StaffModeX.getInstance().getStaffPlayerManager().getOrCreateStaffPlayer(player);
        staffPlayer.toggleVanish();
        if (staffPlayer.isVanished()) {
            setType(Materials.get("LIME_DYE", "INK_SACK"));
            setDurability((short) 10);
        } else {
            setType(Materials.get("GRAY_DYE", "INK_SACK"));
            setDurability((short) 8);
        }
        player.getInventory().setItem(2, this);
    }
}
