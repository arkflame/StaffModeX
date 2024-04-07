package com.arkflame.staffmodex.hotbar.components.staff;

import java.util.Arrays;

import org.bukkit.Material;
import org.bukkit.entity.Player;

import com.arkflame.staffmodex.hotbar.Hotbar;
import com.arkflame.staffmodex.hotbar.HotbarItem;
import com.arkflame.staffmodex.modernlib.utils.Materials;

public class StaffHotbar extends Hotbar {
    public StaffHotbar() {
        super();
        String launcherName = "&6Launcher";
        String launcherLore = "&7Instant elevation increase; vertical mobility for overseeing or reaching high places rapidly.";
        setItem(0, new HotbarItem(Material.COMPASS, launcherName, 1, (short) 0, Arrays.asList(launcherLore)) {
            @Override
            public void onInteract(Player player) { }
        });

        String randomTeleportName = "&aRandom Teleport";
        String randomTeleportLore = "&7Teleport to a random player location; useful for random integrity checks or monitoring player activity.";
        setItem(1, new HotbarItem(Materials.get("ENDER_EYE", "EYE_OF_ENDER"), randomTeleportName, 1, (short) 0, Arrays.asList(randomTeleportLore)) {
            @Override
            public void onInteract(Player player) { }
        });

        String vanishName = "&bVanish";
        String vanishLore = "&7Toggle invisibility on/off; observe players without influencing their behavior.";
        setItem(2, new HotbarItem(Materials.get("LIME_DYE", "INK_SACK"), vanishName, 1, (short) 7, Arrays.asList(vanishLore)) {
            @Override
            public void onInteract(Player player) { }
        });

        String guiHubName = "&3GUI Hub";
        String guiHubLore = "&7Access central command interface; manage server settings, player issues, and plugins from one place.";
        setItem(3, new HotbarItem(Material.PAPER, guiHubName, 1, (short) 0, Arrays.asList(guiHubLore)) {
            @Override
            public void onInteract(Player player) { }
        });

        String staffListName = "&5Staff List";
        String staffListLore = "&7Display active staff members; quick reference for coordination and player support.";
        setItem(4, new HotbarItem(Materials.get("SKULL_ITEM", "SKELETON_SKULL"), staffListName, 1, (short) 0, Arrays.asList(staffListLore)) {
            @Override
            public void onInteract(Player player) { }
        });

        String freezeName = "&cFreeze";
        String freezeLore = "&7Immobilize a player; halt player movement for investigation or to address violations.";
        setItem(5, new HotbarItem(Material.BLAZE_ROD, freezeName, 1, (short) 0, Arrays.asList(freezeLore)) {
            @Override
            public void onInteract(Player player) { }
        });

        String cpsName = "&eCPS";
        String cpsLore = "&7Monitor clicks per second; identify potential use of auto-clickers or macros.";
        setItem(6, new HotbarItem(Materials.get("CLOCK", "WATCH"), cpsName, 1, (short) 0, Arrays.asList(cpsLore)) {
            @Override
            public void onInteract(Player player) { }
        });

        String examineName = "&dExamine";
        String examineLore = "&7Inspect player inventory and stats; crucial for checking compliance with server rules.";
        setItem(7, new HotbarItem(Material.CHEST, examineName, 1, (short) 0, Arrays.asList(examineLore)) {
            @Override
            public void onInteract(Player player) { }
        });

        String followName = "&9Follow";
        String followLore = "&7Automatically move towards a player; maintain surveillance or provide assistance.";
        setItem(8, new HotbarItem(Materials.get("LEAD", "LEASH"), followName, 1, (short) 0, Arrays.asList(followLore)) {
            @Override
            public void onInteract(Player player) { }
        });

    }
}
