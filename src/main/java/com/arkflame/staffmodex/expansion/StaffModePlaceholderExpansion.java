package com.arkflame.staffmodex.expansion;

import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import com.arkflame.staffmodex.StaffModeX;

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
            default: {
                break;
            }
        }
        
        return null; // Placeholder is unknown by the Expansion
    }
}