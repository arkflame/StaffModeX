package com.arkflame.staffmodex.utils;

import org.bukkit.Bukkit;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class Inventories {
    public static Inventory copyInventory(Inventory source) {
        if (source.getType() == InventoryType.CHEST) {
            int originalSize = source.getSize();

            // Not a multiple of 9
            if (originalSize % 9 != 0) {
                return null;
            }
            
            // Create a copy of the destination inventory with no holder
            Inventory copiedInventory = Bukkit.createInventory(null, originalSize, "Contents");

            // Ensure both inventories have the same size
            if (originalSize != copiedInventory.getSize()) {
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
        } else {
            return null;
        }
    }
}
