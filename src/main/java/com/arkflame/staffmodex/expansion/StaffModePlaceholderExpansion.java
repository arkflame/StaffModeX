package com.arkflame.staffmodex.expansion;

import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import com.arkflame.staffmodex.StaffModeX;
import com.arkflame.staffmodex.player.FreezeStatus;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;

public class StaffModePlaceholderExpansion extends PlaceholderExpansion {

    @Override
    public String getAuthor() {
        return "LinsaFTW";
    }
    
    @Override
    public String getIdentifier() {
        return "staffmode";
    }

    @Override
    public String getVersion() {
        return StaffModeX.getInstance().getDescription().getVersion();
    }
    
    @Override
    public String onRequest(OfflinePlayer player, String params) {
        switch(params) {
            case "enabled": {
                boolean status = false;
                if (player instanceof Player) {
                    status = StaffModeX.getInstance().getStaffModeManager().isStaff((Player) player);
                }
                return String.valueOf(status);
            }
            case "freeze_countdown": {
                if (player instanceof Player) {
                    FreezeStatus freezeStatus = StaffModeX.getInstance().getStaffPlayerManager().getOrCreateStaffPlayer(player).getFreezeStatus();
                    return freezeStatus.getCountdownFormatted();
                }
                return "0m 0s";
            }
            case "freeze_time": {
                if (player instanceof Player) {
                    FreezeStatus freezeStatus = StaffModeX.getInstance().getStaffPlayerManager().getOrCreateStaffPlayer(player).getFreezeStatus();
                    return freezeStatus.getTimeFormatted();
                }
                return "0m 0s";
            }
            case "playercount": {
                return String.valueOf(StaffModeX.getInstance().getVisiblePlayerCount());
            }
            default: {
                break;
            }
        }
        
        return null; // Placeholder is unknown by the Expansion
    }
}