package com.arkflame.staffmodex.hotbar.components.items;

import com.arkflame.staffmodex.StaffModeX;
import com.arkflame.staffmodex.hotbar.HotbarItem;
import com.arkflame.staffmodex.modernlib.utils.Materials;
import com.arkflame.staffmodex.player.StaffPlayer;

import org.bukkit.Material;
import org.bukkit.entity.Player;

public class VanishHotbarItem extends HotbarItem {
    private static Material getEnabledMaterial() {
        return Materials
                .get(StaffModeX.getInstance().getCfg().getStringList("items.hotbar.vanish.turned_on.material"));
    }

    private static Material getDisabledMaterial() {
        return Materials
                .get(StaffModeX.getInstance().getCfg().getStringList("items.hotbar.vanish.turned_off.material"));
    }

    private static short getEnabledDamage() {
        return (short) StaffModeX.getInstance().getCfg().getInt("items.hotbar.vanish.turned_on.damage");
    }

    private static short getDisabledDamage() {
        return (short) StaffModeX.getInstance().getCfg().getInt("items.hotbar.vanish.turned_off.damage");
    }

    public VanishHotbarItem(StaffPlayer staffPlayer) {
        super(staffPlayer.isVanished() ? getEnabledMaterial() : getDisabledMaterial(),
                StaffModeX.getInstance().getMsg().getText("hotbar.vanish.name"),
                1,
                staffPlayer.isVanished() ? getEnabledDamage() : getDisabledDamage(),
                StaffModeX.getInstance().getMsg().getTextList("hotbar.vanish.lore"));
    }

    @Override
    public void onInteract(Player player) {
        StaffPlayer staffPlayer = StaffModeX.getInstance().getStaffPlayerManager().getOrCreateStaffPlayer(player);
        staffPlayer.toggleVanish();
        if (staffPlayer.isVanished()) {
            getItem().setType(getEnabledMaterial());
            getItem().setDurability(getEnabledDamage());
        } else {
            getItem().setType(getDisabledMaterial());
            getItem().setDurability(getDisabledDamage());
        }
        player.getInventory().setItem(StaffModeX.getInstance().getCfg().getInt("items.hotbar.vanish.slot"), getItem());
    }
}
