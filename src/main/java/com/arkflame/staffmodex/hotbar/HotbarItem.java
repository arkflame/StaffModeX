package com.arkflame.staffmodex.hotbar;

import java.util.List;

import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.arkflame.staffmodex.modernlib.utils.ChatColors;

public class HotbarItem {
    public void onInteract(Player player) {
        // Override to implement logic
    }

    public void onInteract(Player player, Entity target) {
        // Override to implement logic
    }

    private ItemStack stack = new ItemStack(Material.AIR);

    public HotbarItem(Material material, String name, int amount, short damage, List<String> lore) {
        stack = new ItemStack(material, amount, damage);
        
        ItemMeta meta = stack.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(ChatColors.color(name));
            meta.setLore(ChatColors.color(lore));
            stack.setItemMeta(meta);
        }
    }

    public ItemStack getStack() {
        return stack;
    }
}
