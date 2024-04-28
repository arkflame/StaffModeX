package com.arkflame.staffmodex.inventories;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

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
        inventoryConfig.createSection(player.getUniqueId().toString());
        Inventory inventory = player.getInventory();
        for (int i = 0; i < inventory.getSize(); i++) {
            ItemStack item = inventory.getItem(i);
            if (item != null) {
                inventoryConfig.set(player.getUniqueId().toString() + "." + i, item);
            }
        }
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

        if (!inventoryConfig.contains(player.getUniqueId().toString())) {
            return;
        }

        Inventory inventory = player.getInventory();
        inventory.clear();

        ConfigurationSection invSection = inventoryConfig.getConfigurationSection(player.getUniqueId().toString());
        if (invSection == null) {
            player.sendMessage("Inv section is null!!!");
            return;
        }
        for (String key : invSection.getKeys(false)) {
            int slot = Integer.parseInt(key);
            ItemStack value = invSection.getItemStack(key);
            inventory.setItem(slot, value);
        }
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