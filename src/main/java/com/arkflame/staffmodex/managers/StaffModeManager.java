package com.arkflame.staffmodex.managers;

import com.arkflame.staffmodex.StaffModeX;
import com.arkflame.staffmodex.hotbar.HotbarManager;
import com.arkflame.staffmodex.hotbar.components.StaffHotbar;
import com.arkflame.staffmodex.modernlib.config.ConfigWrapper;
import com.arkflame.staffmodex.modernlib.utils.Players;
import com.arkflame.staffmodex.modernlib.utils.PotionEffects;
import com.arkflame.staffmodex.player.StaffPlayer;

import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;

public class StaffModeManager {
    private Collection<Player> staffPlayers = new HashSet<>();

    public void toggleStaff(Player player) {
        if (isStaff(player)) {
            removeStaff(player);
            Bukkit.getScheduler().runTaskAsynchronously(StaffModeX.getInstance(),
                    () -> StaffModeX.getInstance().getRedisManager().removePlayerFromStaffMode(player.getName()));
        } else {
            addStaff(player);
            Bukkit.getScheduler().runTaskAsynchronously(StaffModeX.getInstance(),
                    () -> StaffModeX.getInstance().getRedisManager().addPlayerToStaffMode(player.getName()));
        }
    }

    public void applyPotionEffects(Player player) {
        ConfigWrapper cfg = StaffModeX.getInstance().getCfg();
        ConfigurationSection effectsSection = cfg.getConfigurationSection("staffmode.effects");
    
        if (effectsSection != null) {
            for (String effectName : effectsSection.getKeys(false)) {
                ConfigurationSection effectSection = effectsSection.getConfigurationSection(effectName);
                if (effectSection != null) {
                    int amplifier = effectSection.getInt("amplifier", 1);
                    int duration = effectSection.getInt("duration", Integer.MAX_VALUE);
    
                    List<String> aliases = effectSection.getStringList("aliases");
                    String[] effects = aliases.toArray(new String[0]);
    
                    // Apply the potion effects using your API
                    PotionEffects.add(player, amplifier, duration, effects);
                } else {
                    StaffModeX.getInstance().getLogger().warning("Invalid configuration section for effect: " + effectName);
                }
            }
        } else {
            StaffModeX.getInstance().getLogger().warning("No potion effects found in the configuration.");
        }
    }

    public void removePotionEffects(Player player) {
        ConfigWrapper cfg = StaffModeX.getInstance().getCfg();
        ConfigurationSection effectsSection = cfg.getConfigurationSection("staffmode.effects");

        if (effectsSection != null) {
            for (String effectName : effectsSection.getKeys(false)) {
                ConfigurationSection effectSection = effectsSection.getConfigurationSection(effectName);
                if (effectSection != null) {
                    List<String> aliases = effectSection.getStringList("aliases");
                    String[] effects = aliases.toArray(new String[0]);

                    // Remove the potion effects using your API
                    PotionEffects.remove(player, effects);
                } else {
                    StaffModeX.getInstance().getLogger().warning("Invalid configuration section for effect: " + effectName);
                }
            }
        } else {
            StaffModeX.getInstance().getLogger().warning("No potion effects found in the configuration.");
        }
    }

    public void addStaff(Player player) {
        if (isStaff(player)) {
            return;
        }

        HotbarManager hotbarManager = StaffModeX.getInstance().getHotbarManager();
        /* Save this location */
        StaffPlayer staffPlayer = StaffModeX.getInstance().getStaffPlayerManager().getOrCreateStaffPlayer(player);
        if (staffPlayer != null) {
            staffPlayer.setRestoreLocation(player.getLocation());
            staffPlayer.setRestoreGameMode(player.getGameMode());
            staffPlayer.setRestoreStaffChat(staffPlayer.isStaffChat());
            if (StaffModeX.getInstance().getCfg().getBoolean("vanish.enabled") &&
                    StaffModeX.getInstance().getCfg().getBoolean("vanish.on_staff_mode")) {
                staffPlayer.makeInvisible();
            }

            if (StaffModeX.getInstance().getCfg().getBoolean("staffchat.enabled") &&
                    StaffModeX.getInstance().getCfg().getBoolean("staffchat.on_staff_mode")) {
                staffPlayer.setStaffChat(true);
            }
        }
        // Save inventory
        StaffModeX.getInstance().getInventoryManager().savePlayerInventory(player);
        Players.clearInventory(player);
        Players.heal(player);
        if (StaffModeX.getInstance().getCfg().getBoolean("gamemode.enabled")) {
            Players.setGameMode(player, StaffModeX.getInstance().getCfg().getString("gamemode.mode"));
        }
        hotbarManager.setHotbar(player, new StaffHotbar(staffPlayer));
        Players.setFlying(player, true);
        player.sendMessage(StaffModeX.getInstance().getMessage("staffmode.enter"));
        staffPlayers.add(player);

        // Potion Effects
        applyPotionEffects(player);

        // Armor
        StaffModeX.getInstance().getArmorManager().giveArmor(player);
    }

    public void removeStaff(Player player) {
        if (!isStaff(player)) {
            return;
        }

        HotbarManager hotbarManager = StaffModeX.getInstance().getHotbarManager();
        hotbarManager.setHotbar(player, null);
        Players.clearInventory(player);
        // Restore inventory
        StaffModeX.getInstance().getInventoryManager().loadPlayerInventory(player);
        StaffModeX.getInstance().getInventoryManager().deletePlayerInventory(player);
        // Reset fall distance
        player.setFallDistance(0F);
        player.sendMessage(StaffModeX.getInstance().getMessage("staffmode.leave"));
        staffPlayers.remove(player);

        // Remove potion effects
        removePotionEffects(player);

        StaffPlayer staffPlayer = StaffModeX.getInstance().getStaffPlayerManager().getOrCreateStaffPlayer(player);
        if (staffPlayer != null) {
            // Restore location
            if (StaffModeX.getInstance().getCfg().getBoolean("staffmode.restore_location")) {
                staffPlayer.restoreLocation();
            }

            // Restore gamemode
            if (StaffModeX.getInstance().getCfg().getBoolean("gamemode.enabled")) {
                staffPlayer.restoreGameMode();
            }

            // Restore staff chat
            staffPlayer.restoreStaffChat();

            // Make visible
            if (StaffModeX.getInstance().getCfg().getBoolean("vanish.enabled") &&
                    StaffModeX.getInstance().getCfg().getBoolean("vanish.on_staff_mode")) {
                staffPlayer.makeVisible();
            }
        }
    }

    public boolean isStaff(Player player) {
        return staffPlayers.contains(player);
    }

    public Collection<Player> getStaffPlayers() {
        return staffPlayers;
    }
}
