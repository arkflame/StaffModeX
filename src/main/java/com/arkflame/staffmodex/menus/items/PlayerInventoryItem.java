package com.arkflame.staffmodex.menus.items;

import java.util.Collection;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;

import com.arkflame.staffmodex.modernlib.menus.Menu;
import com.arkflame.staffmodex.modernlib.menus.items.MenuItem;
import com.arkflame.staffmodex.modernlib.utils.Materials;

public class PlayerInventoryItem extends MenuItem {
    private final Player player;
    private final Player target;
    private final Menu examinePlayerMenu;

    public PlayerInventoryItem(Player player, Player target, Menu examinePlayerMenu) {
        super(Material.CHEST, "&bInventory", "&7View the target's inventory");
        this.player = player;
        this.target = target;
        this.examinePlayerMenu = examinePlayerMenu;
    }

    @Override
    public void onClick() {
        Menu menu = new Menu(target.getName() + "'s inventory", 5);
        // Iterate target players inventory and add it to the menu
        for (int i = 0; i < target.getInventory().getSize(); i++) {
            ItemStack stack = target.getInventory().getItem(i);
            if (stack != null) {
                MenuItem item = new MenuItem(stack);
                if (i < 9) {
                    menu.setItem((3 * 9) + i, item);
                } else {
                    menu.setItem(i - 9, item);
                }
            }
        }
        menu.openInventory(player);

        // Item to return to the menu
        menu.setItem(4 * 9, new MenuItem(Material.ARROW, "&bBack") {
            @Override
            public void onClick() {
                examinePlayerMenu.openInventory(player);
            }
        });

        Collection<PotionEffect> effects = target.getActivePotionEffects();

        // Item that shows effect names duration and amplifier in lore
        List<String> loreEffects = effects.isEmpty() ? List.of("&7No Effects")
                : effects.stream().map(this::formatEffect).toList();
        menu.setItem(5 * 9 - 5, new MenuItem(Material.WATER_BUCKET, "&bEffects", loreEffects.toArray(new String[0])));

        // Populate armor
        for (int i = 0; i < 4; i++) {
            ItemStack stack = target.getInventory().getArmorContents()[i];
            if (stack != null && stack.getType()!= Material.AIR) {
                MenuItem item = new MenuItem(stack);
                menu.setItem(5 * 9 - i - 1, item);
            } else {
                menu.setItem(5 * 9 - i - 1, new MenuItem(Materials.get("STAINED_GLASS_PANE", "RED_STAINED_GLASS_PANE"), 1, (short) 14, "&7Empty Slot"));
            }
        }

        menu.setBackground(Materials.get("STAINED_GLASS_PANE", "GRAY_STAINED_GLASS_PANE"), (short) 7, " ");
    }

    private String toRomanNumber(int amplifier) {
        if (amplifier == 0) {
            return "I";
        } else if (amplifier == 1) {
            return "II";
        } else if (amplifier == 2) {
            return "III";
        } else if (amplifier == 3) {
            return "IV";
        } else if (amplifier == 4) {
            return "V";
        } else if (amplifier == 5) {
            return "VI";
        } else if (amplifier == 6) {
            return "VII";
        } else if (amplifier == 7) {
            return "VIII";
        } else if (amplifier == 8) {
            return "IX";
        } else {
            return "X";
        }
    }

    private String formatEffect(PotionEffect effect) {
        return "&7" + effect.getType().getName() + " &8(" + effect.getDuration() + "s, "
                + toRomanNumber(effect.getAmplifier()) + ")";
    }
}
