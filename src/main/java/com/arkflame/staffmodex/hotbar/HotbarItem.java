package com.arkflame.staffmodex.hotbar;

import java.util.List;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public abstract class HotbarItem {
    public abstract void onClick(Player player);

    private ItemStack stack = new ItemStack(Material.AIR);

    public HotbarItem(Material material, String name, int amount, short damage, List<String> lore) {
        this.stack = new ItemStack(material, amount, damage);
        
        ItemMeta meta = stack.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(name);
            meta.setLore(lore);
        }
    }

    public ItemStack getStack() {
        return stack;
    }
}
