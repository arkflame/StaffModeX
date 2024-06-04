package com.arkflame.staffmodex.inventories;

import java.io.File;
import java.io.IOException;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import com.arkflame.staffmodex.StaffModeX;

public class InventoryManager {
    private File inventoryFile;
    private FileConfiguration inventoryConfig;

    public InventoryManager(StaffModeX plugin, String fileName) {
        inventoryFile = new File(plugin.getDataFolder(), fileName);
        if (!inventoryFile.exists()) {
            try {
                if (!inventoryFile.getParentFile().exists()) {
                    inventoryFile.getParentFile().mkdirs();
                }
                inventoryFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        inventoryConfig = YamlConfiguration.loadConfiguration(inventoryFile);
    }

    public void savePlayerInventory(Player player) {
        if (player == null) {
            return;
        }
        inventoryConfig.createSection(player.getUniqueId().toString() + ".inventory");

        // Inventory
        PlayerInventory inventory = player.getInventory();
        for (int i = 0; i < inventory.getSize(); i++) {
            ItemStack item = inventory.getItem(i);
            if (item != null) {
                inventoryConfig.set(player.getUniqueId().toString() + ".inventory" + "." + i, item);
            }
        }
        // Armor
        ItemStack[] armorContents = inventory.getArmorContents();
        for (int i = 0; i < armorContents.length; i++) {
            ItemStack item = armorContents[i];
            if (item != null) {
                inventoryConfig.set(player.getUniqueId().toString() + ".armor" + "." + i, item);
            }
        }

        // Health and food
        inventoryConfig.set(player.getUniqueId().toString() + ".health", player.getHealth());
        inventoryConfig.set(player.getUniqueId().toString() + ".food", player.getFoodLevel());

        // Flying status
        inventoryConfig.set(player.getUniqueId().toString() + ".flying", player.isFlying());
        inventoryConfig.set(player.getUniqueId().toString() + ".allow-flight", player.getAllowFlight());
        try {
            inventoryConfig.save(inventoryFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadPlayerInventory(Player player) {
        if (player == null) {
            return;
        }

        String uuidString = player.getUniqueId().toString();

        if (!inventoryConfig.contains(uuidString + ".inventory")) {
            return;
        }

        PlayerInventory inventory = player.getInventory();
        inventory.clear();

        // Inventory
        ConfigurationSection invSection = inventoryConfig
                .getConfigurationSection(uuidString + ".inventory");
        if (invSection != null) {
            for (String key : invSection.getKeys(false)) {
                int slot = Integer.parseInt(key);
                ItemStack value = invSection.getItemStack(key);
                inventory.setItem(slot, value);
            }
        }
        // Armor
        ConfigurationSection armorSection = inventoryConfig
                .getConfigurationSection(uuidString + ".armor");
        if (armorSection != null) {
            ItemStack[] armorContents = inventory.getArmorContents();
            for (String key : armorSection.getKeys(false)) {
                try {
                    int slot = Integer.parseInt(key);
                    ItemStack value = armorSection.getItemStack(key);
                    armorContents[slot] = value;
                } catch (Exception ex) {
                    // Do not fall for errors
                }
            }
            inventory.setArmorContents(armorContents);
        }

        // Health and food
        player.setHealth(inventoryConfig.getDouble(uuidString + ".health", player.getHealth()));
        player.setFoodLevel(inventoryConfig.getInt(player.getUniqueId().toString() + ".food", player.getFoodLevel()));

        // Flying status
        player.setAllowFlight(
                inventoryConfig.getBoolean(uuidString + ".allow-flight", player.getAllowFlight()));
        player.setFlying(inventoryConfig.getBoolean(uuidString + ".flying", player.isFlying()));
    }

    public void deletePlayerInventory(Player player) {
        if (player == null) {
            return;
        }
        inventoryConfig.set(player.getUniqueId().toString(), null);
        try {
            inventoryConfig.save(inventoryFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}