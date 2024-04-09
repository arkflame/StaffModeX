package com.arkflame.staffmodex.hotbar.components;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import com.arkflame.staffmodex.StaffModeX;
import com.arkflame.staffmodex.hotbar.Hotbar;
import com.arkflame.staffmodex.hotbar.HotbarItem;
import com.arkflame.staffmodex.managers.CpsTestingManager;
import com.arkflame.staffmodex.menus.ExaminePlayerMenu;
import com.arkflame.staffmodex.modernlib.utils.Materials;
import com.arkflame.staffmodex.modernlib.menus.Menu;
import com.arkflame.staffmodex.modernlib.utils.ChatColors;
import com.arkflame.staffmodex.modernlib.utils.Effects;
import com.arkflame.staffmodex.modernlib.utils.Sounds;

public class StaffHotbar extends Hotbar {
    public StaffHotbar() {
        super();
        String launcherName = "&6Launcher";
        String launcherLore = "&7Launches you towards the direction you are looking.";
        setItem(0, new HotbarItem(Material.COMPASS, launcherName, 1, (short) 0, Arrays.asList(launcherLore)) {
            @Override
            public void onInteract(Player player) {
                // Get player's location and direction
                Location playerLocation = player.getLocation();
                Vector playerDirection = playerLocation.getDirection();

                // Calculate velocity based on player's direction
                double velocityMultiplier = 2.0; // Adjust this value as needed for desired launch strength
                Vector velocity = playerDirection.multiply(velocityMultiplier);

                // Apply velocity to player
                player.setVelocity(velocity);

                // Play particle effects
                Effects.play(player, "EXPLOSION_LARGE");

                // Play sound effect
                Sounds.play(player, 1.0f, 1.0f, "ENTITY_FIREWORK_ROCKET_LAUNCH", "FIREWORK_LAUNCH");
            }
        });

        String randomTeleportName = "&aRandom Teleport";
        String randomTeleportLore = "&7Teleport to a random player location; useful for random integrity checks or monitoring player activity.";
        setItem(1, new HotbarItem(Materials.get("ENDER_EYE", "EYE_OF_ENDER"), randomTeleportName, 1, (short) 0,
                Arrays.asList(randomTeleportLore)) {
            @Override
            public void onInteract(Player player) {
                List<Player> otherPlayers = player.getWorld().getPlayers().stream()
                        .filter(p -> !p.equals(player))
                        .collect(Collectors.toList());
                if (otherPlayers.isEmpty()) {
                    player.sendMessage(ChatColors.color("&cThere are no other players to teleport to."));
                    return;
                }

                Player targetPlayer = otherPlayers.get(new Random().nextInt(otherPlayers.size()));
                player.teleport(targetPlayer.getLocation());
                player.sendMessage(
                        ChatColors.color("&aYou have been teleported to " + targetPlayer.getDisplayName() + "."));

                Sounds.play(player, 1.0f, 1.0f, "ENTITY_ENDERMAN_TELEPORT", "ENDERMAN_TELEPORT");
            }
        });

        String vanishName = "&bVanish";
        String vanishLore = "&7Toggle invisibility on/off; observe players without influencing their behavior.";
        setItem(2, new HotbarItem(Materials.get("LIME_DYE", "INK_SACK"), vanishName, 1, (short) 7,
                Arrays.asList(vanishLore)) {
            @Override
            public void onInteract(Player player) {
                StaffModeX.getInstance().getVanishManager().toggleVanish(player);
            }
        });

        String guiHubName = "&3GUI Hub";
        String guiHubLore = "&7Access central command interface; manage server settings, player issues, and plugins from one place.";
        setItem(3, new HotbarItem(Material.PAPER, guiHubName, 1, (short) 0, Arrays.asList(guiHubLore)) {
            @Override
            public void onInteract(Player player) {
            }
        });

        String staffListName = "&5Staff List";
        String staffListLore = "&7Display active staff members; quick reference for coordination and player support.";
        setItem(4, new HotbarItem(Materials.get("SKULL_ITEM", "SKELETON_SKULL"), staffListName, 1, (short) 0,
                Arrays.asList(staffListLore)) {
            @Override
            public void onInteract(Player player) {
            }
        });

        String freezeName = "&cFreeze";
        String freezeLore = "&7Immobilize a player; halt player movement for investigation or to address violations.";
        setItem(5, new HotbarItem(Material.BLAZE_ROD, freezeName, 1, (short) 0, Arrays.asList(freezeLore)) {
            @Override
            public void onInteract(Player player, Entity target) {
                if (target instanceof Player) {
                    StaffModeX.getInstance().getFreezeManager().toggleFreeze((Player) target);
                } else {
                    player.sendMessage(ChatColor.RED + "No valid target found.");
                }
            }
        });

        String cpsName = "&eCPS";
        String cpsLore = "&7Monitor clicks per second; identify potential use of auto-clickers or macros.";
        setItem(6, new HotbarItem(Materials.get("CLOCK", "WATCH"), cpsName, 1, (short) 0, Arrays.asList(cpsLore)) {
            @Override
            public void onInteract(Player player, Entity target) {
                if (!(target instanceof Player)) {
                    player.sendMessage(ChatColors.color("&cInvalid target for CPS testing."));
                    return;
                }

                Player testedPlayer = (Player) target;

                // Put the tested player in testing mode for 10 seconds
                // You can implement this functionality using a separate class or method
                // For demonstration purposes, let's assume a method called startCpsTesting in a
                // CpsTestingManager class
                CpsTestingManager.startCpsTesting(testedPlayer);

                // Wait for 10 seconds
                Bukkit.getScheduler().runTaskLater(StaffModeX.getInstance(), () -> {
                    // Get the average CPS of the tested player
                    double averageCps = CpsTestingManager.getAverageCps(testedPlayer);

                    // Return the average CPS to the staff member who tested the player
                    player.sendMessage(
                            ChatColors.color("&eAverage CPS of " + testedPlayer.getName() + ": " + averageCps));
                }, 200L); // 10 seconds = 200 ticks
            }
        });

        String examineName = "&dExamine";
        String examineLore = "&7Inspect player inventory and stats; crucial for checking compliance with server rules.";
        setItem(7, new HotbarItem(Material.CHEST, examineName, 1, (short) 0, Arrays.asList(examineLore)) {
            @Override
            public void onInteract(Player player, Entity target) {
                if (target instanceof Player) {
                    Player targetPlayer = (Player) target;
                    Menu menu = new ExaminePlayerMenu(player, targetPlayer);

                    menu.openInventory(player);
                }
            }
        });

        String followName = "&9Follow";
        String followLore = "&7Mount the player to observe their behavior.";
        setItem(8, new HotbarItem(Materials.get("LEAD", "LEASH"), followName, 1, (short) 0, Arrays.asList(followLore)) {
            @Override
            public void onInteract(Player player, Entity target) {
                if (!(target instanceof Player)) {
                    player.sendMessage(ChatColors.color("&cInvalid target for following."));
                    return;
                }

                Player targetPlayer = (Player) target;

                if (player.isInsideVehicle()) {
                    player.leaveVehicle();
                }

                targetPlayer.setPassenger(player);

                player.sendMessage(ChatColors.color("&aYou are now following " + targetPlayer.getDisplayName() + "."));
            }
        });

    }
}
