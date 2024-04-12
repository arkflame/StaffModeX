package com.arkflame.staffmodex.infractions;

import java.util.ArrayList;
import java.util.List;
import org.bukkit.configuration.ConfigurationSection;

import com.arkflame.staffmodex.StaffModeX;

public class InfractionList {
    private final List<Infraction> infractions = new ArrayList<>();

    public List<Infraction> getInfractions() {
        return infractions;
    }

    public void addInfraction(Infraction infraction) {
        infractions.add(infraction);
    }

    public void setInfractions(List<Infraction> infractions) {
        this.infractions.clear();
        this.infractions.addAll(infractions);
    }

    public void load(ConfigurationSection infractionsSection) {
        if (infractionsSection != null) {
            StaffModeX.getInstance().getLogger().info("Loading infractions...");
            infractionsSection.getKeys(false).forEach(id -> {
                ConfigurationSection infractionSection = infractionsSection.getConfigurationSection(id);
                if (infractionSection != null) {
                    StaffModeX.getInstance().getLogger().info("Loading infraction with ID: " + id);
                    String timestamp = infractionSection.getString("timestamp");
                    String reporter = infractionSection.getString("reporter");
                    String reason = infractionSection.getString("reason");
                    addInfraction(new Infraction(id, timestamp, reporter, reason));
                }
            });
            StaffModeX.getInstance().getLogger().info("Infractions loaded successfully.");
        } else {
            StaffModeX.getInstance().getLogger().warning("Infractions section is null. No infractions to load.");
        }
    }    

    public int count() {
        return infractions.size();
    }

    public String getLast() {
        if (infractions.isEmpty()) {
            return "No infractions";
        }
        return infractions.get(infractions.size() - 1).getReason();
    }
}
