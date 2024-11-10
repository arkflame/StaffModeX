package com.arkflame.staffmodex.player;

import org.bukkit.configuration.ConfigurationSection;

import com.arkflame.staffmodex.StaffModeX;
import com.arkflame.staffmodex.infractions.Infraction;
import com.arkflame.staffmodex.managers.DatabaseManager;
import com.arkflame.staffmodex.modernlib.config.ConfigWrapper;

public class StaffPlayerLoader {
    private StaffPlayer staffPlayer;
    private ConfigWrapper infractionsConfig;
    private ConfigWrapper ipsConfig;

    public StaffPlayerLoader(StaffPlayer staffPlayer, ConfigWrapper infractionsConfig, ConfigWrapper ipsConfig) {
        this.staffPlayer = staffPlayer;
        this.infractionsConfig = infractionsConfig;
        this.ipsConfig = ipsConfig;
    }

    public void load() {
        DatabaseManager mySQLManager = StaffModeX.getInstance().getMySQLManager();
    
        if (mySQLManager.isInitializedSuccessfully()) {
            mySQLManager.loadInfractions(staffPlayer);
            return;
        }
    
        infractionsConfig.load();
    
        ConfigurationSection warningsSection = infractionsConfig.getConfig().getConfigurationSection("warnings");
        if (warningsSection != null) {
            staffPlayer.getWarnings().load(warningsSection);
        }
    
        ConfigurationSection reportsSection = infractionsConfig.getConfig().getConfigurationSection("reports");
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

        infraction.save(infractionsConfig.getConfig());
        infractionsConfig.save();
    }

    public void saveIP() {
        if (!StaffModeX.getInstance().getConfig().getBoolean("ip.enabled")) {
            return;
        }

        DatabaseManager mySQLManager = StaffModeX.getInstance().getMySQLManager();
        String ip = staffPlayer.getIP();

        if (mySQLManager.isInitializedSuccessfully()) {
            mySQLManager.saveIP(staffPlayer.getUUID(), ip);
            return;
        }

        ipsConfig.load();
        ipsConfig.getConfig().set("ip", ip);
        ipsConfig.save();
    }

    public void loadIP() {
        if (!StaffModeX.getInstance().getConfig().getBoolean("ip.enabled")) {
            return;
        }

        DatabaseManager mySQLManager = StaffModeX.getInstance().getMySQLManager();

        if (mySQLManager.isInitializedSuccessfully()) {
            mySQLManager.loadIP(staffPlayer);
            return;
        }

        ipsConfig.load();
        staffPlayer.setIP(ipsConfig.getConfig().getString("ip"));
    }
}
