package com.arkflame.staffmodex.utils;

import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class Inventories {
    public static Inventory copyInventory(Inventory source) {
        // Create a copy of the destination inventory with no holder
        Inventory copiedInventory = Bukkit.createInventory(null, source.getSize(), "Contents");
        
        // Ensure both inventories have the same size
        if (source.getSize() != source.getSize()) {
            return copiedInventory; // If sizes don't match, return empty copied inventory
        }
        
        // Copy items from the source to the copied inventory
        for (int i = 0; i < source.getSize(); i++) {
            ItemStack item = source.getItem(i);
            if (item != null) {
                copiedInventory.setItem(i, item.clone());
            }
        }
        
        return copiedInventory;
    }
}
