package com.arkflame.staffmodex.armor;

import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.LeatherArmorMeta;

import com.arkflame.staffmodex.StaffModeX;
import com.arkflame.staffmodex.modernlib.config.ConfigWrapper;

import java.util.HashMap;
import java.util.Map;

public class ArmorManager {
    private final Map<String, ArmorSet> armorSets;

    public ArmorManager(ConfigWrapper config) {
        this.armorSets = new HashMap<>();
        loadArmorSets(config);
    }

    private void loadArmorSets(ConfigWrapper config) {
        if (!config.isConfigurationSection("armors.sets")) {
            return;
        }

        ConfigurationSection setsConfig = config.getConfigurationSection("armors.sets");
        for (String key : setsConfig.getKeys(false)) {
            ConfigurationSection setConfig = setsConfig.getConfigurationSection(key);
            String permission = setConfig.getString("permission");
            String colorName = setConfig.getString("color");
            Color color = getColorFromString(colorName);

            if (permission != null && color != null) {
                armorSets.put(key, new ArmorSet(permission, color));
            }
        }
    }

    private Color getColorFromString(String colorName) {
        try {
            return (Color) Color.class.getField(colorName.toUpperCase()).get(null);
        } catch (Exception e) {
            return null;
        }
    }

    public void giveArmor(Player player) {
        if (!StaffModeX.getInstance().getCfg().getBoolean("armors.enabled")) {
            return;
        }

        Color color = getColorForPlayer(player);
        if (color == null) return; // No matching armor found
    
        ItemStack helmet = createColoredArmor(Material.LEATHER_HELMET, color);
        ItemStack chestplate = createColoredArmor(Material.LEATHER_CHESTPLATE, color);
        ItemStack leggings = createColoredArmor(Material.LEATHER_LEGGINGS, color);
        ItemStack boots = createColoredArmor(Material.LEATHER_BOOTS, color);
    
        PlayerInventory inventory = player.getInventory();
        inventory.setHelmet(helmet);
        inventory.setChestplate(chestplate);
        inventory.setLeggings(leggings);
        inventory.setBoots(boots);
    }
    
    private Color getColorForPlayer(Player player) {
        for (ArmorSet armorSet : armorSets.values()) {
            if (player.hasPermission(armorSet.getPermission())) {
                return armorSet.getColor();
            }
        }
        return null;
    }
    
    private ItemStack createColoredArmor(Material material, Color color) {
        ItemStack armorPiece = new ItemStack(material);
        LeatherArmorMeta meta = (LeatherArmorMeta) armorPiece.getItemMeta();
        if (color != null) {
            meta.setColor(color);
        }
        armorPiece.setItemMeta(meta);
        return armorPiece;
    }
    
}
