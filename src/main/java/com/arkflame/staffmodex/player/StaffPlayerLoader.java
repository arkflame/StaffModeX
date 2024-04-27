package com.arkflame.staffmodex.player;

import org.bukkit.configuration.ConfigurationSection;

import com.arkflame.staffmodex.StaffModeX;
import com.arkflame.staffmodex.modernlib.config.ConfigWrapper;

public class StaffPlayerLoader {
    private final StaffPlayer staffPlayer;
    private final ConfigWrapper config;

    public StaffPlayerLoader(StaffPlayer staffPlayer, ConfigWrapper config) {
        this.staffPlayer = staffPlayer;
        this.config = config;
    }

    public void load() {
        StaffModeX.getInstance().getLogger().info("Loading configuration...");
        config.load();
    
        ConfigurationSection warningsSection = config.getConfig().getConfigurationSection("warnings");
        if (warningsSection != null) {
            StaffModeX.getInstance().getLogger().info("Loading warnings...");
            staffPlayer.getWarnings().load(warningsSection);
            StaffModeX.getInstance().getLogger().info("Warnings loaded successfully.");
        } else {
            StaffModeX.getInstance().getLogger().warning("No warnings section found. Skipping loading of warnings.");
        }
    
        ConfigurationSection reportsSection = config.getConfig().getConfigurationSection("reports");
        if (reportsSection != null) {
            StaffModeX.getInstance().getLogger().info("Loading reports...");
            staffPlayer.getReports().load(reportsSection);
            StaffModeX.getInstance().getLogger().info("Reports loaded successfully.");
        } else {
            StaffModeX.getInstance().getLogger().warning("No reports section found. Skipping loading of reports.");
        }
        StaffModeX.getInstance().getLogger().info("Configuration loaded successfully.");
    }
    

    public void save() {
        ConfigurationSection warningsSection = config.getConfig().createSection("warnings");
        staffPlayer.getWarnings().getInfractions().forEach(infraction -> infraction.save(warningsSection));

        ConfigurationSection reportsSection = config.getConfig().createSection("reports");
        staffPlayer.getReports().getInfractions().forEach(infraction -> infraction.save(reportsSection));

        config.save();
    }
}
