package com.arkflame.staffmodex.hotbar.components.items;

import org.bukkit.enchantments.Enchantment;

import com.arkflame.staffmodex.StaffModeX;
import com.arkflame.staffmodex.hotbar.HotbarItem;
import com.arkflame.staffmodex.modernlib.utils.Materials;

public class KnockbackHotbarItem extends HotbarItem {
    public KnockbackHotbarItem() {
        super(Materials.get(StaffModeX.getInstance().getConfig().getStringList("items.hotbar.knockback.material")),
                StaffModeX.getInstance().getMsg().getText("hotbar.knockback.name"),
                1, (short) 0,
                StaffModeX.getInstance().getMsg().getTextList("hotbar.knockback.lore"));

        try {
            addEnchantment(Enchantment.KNOCKBACK, 1);
        } catch (Exception ex) {
            // Catch for invalid enchantment in new versions
        }
    }
}
