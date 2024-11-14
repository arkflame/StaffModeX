package com.arkflame.staffmodex.armor;

import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;

import com.arkflame.staffmodex.StaffModeX;
import com.arkflame.staffmodex.modernlib.config.ConfigWrapper;
import com.arkflame.staffmodex.modernlib.utils.Materials;

import java.util.HashMap;
import java.util.Map;

public class ArmorManager {
    private Map<String, ArmorSet> armorSets;

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
            String typeName = setConfig.getString("type");
            Color color = getColorFromString(colorName);

            if (permission != null && color != null) {
                armorSets.put(key, new ArmorSet(permission, color, typeName));
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

        ArmorSet armor = getArmor(player);
        if (armor == null)
            return; // No matching armor found
        Color color = armor.getColor();
        String type = armor.getType();

        ItemStack helmet = createArmor(Materials.get(type + "_HELMET"), color);
        ItemStack chestplate = createArmor(Materials.get(type + "_CHESTPLATE"), color);
        ItemStack leggings = createArmor(Materials.get(type + "_LEGGINGS"), color);
        ItemStack boots = createArmor(Materials.get(type + "_BOOTS"), color);

        PlayerInventory inventory = player.getInventory();
        inventory.setHelmet(helmet);
        inventory.setChestplate(chestplate);
        inventory.setLeggings(leggings);
        inventory.setBoots(boots);
    }

    private ArmorSet getArmor(Player player) {
        for (ArmorSet armorSet : armorSets.values()) {
            if (player.hasPermission(armorSet.getPermission())) {
                return armorSet;
            }
        }
        return null;
    }

    private ItemStack createArmor(Material material, Color color) {
        if (material == null) {
            return null;
        }
        ItemStack armorPiece = new ItemStack(material);
        ItemMeta originalMeta = armorPiece.getItemMeta();
        if (originalMeta instanceof LeatherArmorMeta) {
            LeatherArmorMeta meta = (LeatherArmorMeta) originalMeta;
            if (color != null) {
                meta.setColor(color);
            }
            armorPiece.setItemMeta(meta);
        }
        return armorPiece;
    }

}
