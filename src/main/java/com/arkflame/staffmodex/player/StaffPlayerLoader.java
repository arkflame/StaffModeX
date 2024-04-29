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
            mySQLManager.loadInfractions(staffPlayer);
            return;
        }
    
        config.load();
    
        ConfigurationSection warningsSection = config.getConfig().getConfigurationSection("warnings");
        if (warningsSection != null) {
            staffPlayer.getWarnings().load(warningsSection);
        }
    
        ConfigurationSection reportsSection = config.getConfig().getConfigurationSection("reports");
        if (reportsSection != null) {
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
