package com.arkflame.staffmodex.player;

import org.bukkit.configuration.ConfigurationSection;

import com.arkflame.staffmodex.StaffModeX;
import com.arkflame.staffmodex.infractions.Infraction;
import com.arkflame.staffmodex.managers.DatabaseManager;
import com.arkflame.staffmodex.modernlib.config.ConfigWrapper;

public class StaffPlayerLoader {
    private final StaffPlayer staffPlayer;
    private final ConfigWrapper config;

    public StaffPlayerLoader(StaffPlayer staffPlayer, ConfigWrapper config) {
        this.staffPlayer = staffPlayer;
        this.config = config;
    }

    public void load() {
        DatabaseManager mySQLManager = StaffModeX.getInstance().getMySQLManager();
    
        if (mySQLManager.isInitializedSuccessfully()) {
            StaffModeX.getInstance().getLogger().info("Using MySQL for data loading.");
            mySQLManager.loadInfractions(staffPlayer);
            return;
        } else {
            StaffModeX.getInstance().getLogger().info("MySQL not initialized. Using local configuration.");
        }
    
        StaffModeX.getInstance().getLogger().info("Loading data from configuration file.");
    
        config.load();
    
        ConfigurationSection warningsSection = config.getConfig().getConfigurationSection("warnings");
        if (warningsSection != null) {
            StaffModeX.getInstance().getLogger().info("Loading warnings from configuration file.");
            staffPlayer.getWarnings().load(warningsSection);
        }
    
        ConfigurationSection reportsSection = config.getConfig().getConfigurationSection("reports");
        if (reportsSection != null) {
            StaffModeX.getInstance().getLogger().info("Loading reports from configuration file.");
            staffPlayer.getReports().load(reportsSection);
        }
    }    

    public void save(Infraction infraction) {
        DatabaseManager mySQLManager = StaffModeX.getInstance().getMySQLManager();

        if (mySQLManager.isInitializedSuccessfully()) {
            mySQLManager.saveInfraction(infraction);
            return;
        }

        infraction.save(config.getConfig());

        config.save();
    }
}
