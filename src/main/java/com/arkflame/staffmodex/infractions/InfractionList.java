package com.arkflame.staffmodex.infractions;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.configuration.ConfigurationSection;

import com.arkflame.staffmodex.StaffModeX;

public class InfractionList {
    private List<Infraction> infractions = new ArrayList<>();

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
        this.infractions.clear();
        if (infractionsSection != null) {
            infractionsSection.getKeys(false).forEach(id -> {
                try {
                    ConfigurationSection infractionSection = infractionsSection.getConfigurationSection(id);
                    if (infractionSection != null) {
                        String timestamp = infractionSection.getString("timestamp");
                        String reporterUUID = infractionSection.getString("reporter_uuid");
                        String reason = infractionSection.getString("reason");
                        String accusedUUID = infractionSection.getString("accused_uuid");
                        String type = infractionSection.getString("type");
                        addInfraction(new Infraction(id, timestamp, reporterUUID, reason, UUID.fromString(accusedUUID),
                                UUID.fromString(reporterUUID), InfractionType.valueOf(type)));
                    }
                } catch (Exception ex) {
                    StaffModeX.getInstance().getLogger().severe("Error while loading infraction from config");
                    ex.printStackTrace();
                }
            });
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
