package com.arkflame.staffmodex.infractions;

import java.util.UUID;

import org.bukkit.configuration.ConfigurationSection;

public class Infraction {
    private UUID id;
    private String timestamp;
    private String reporterName;
    private String reason;
    private UUID accusedUUID;
    private UUID reporterUUID;
    private InfractionType type;

    public Infraction(UUID id, String timestamp, String reporterName, String reason, UUID accusedUUID, UUID reporterUUID, InfractionType type) {
        this.id = id;
        this.timestamp = timestamp;
        this.reporterName = reporterName;
        this.reason = reason;
        this.accusedUUID = accusedUUID;
        this.reporterUUID = reporterUUID;
        this.type = type;
    }

    public Infraction(String id, String timestamp, String reporterName, String reason, UUID accusedUUID, UUID reporterUUID, InfractionType type) {
        this(UUID.fromString(id), timestamp, reporterName, reason, accusedUUID, reporterUUID, type);
    }

    public Infraction(String timestamp, String reporterName, String reason, UUID accusedUUID, UUID reporterUUID, InfractionType type) {
        this(UUID.randomUUID(), timestamp, reporterName, reason, accusedUUID, reporterUUID, type);
    }

    public String getTimestamp() {
        return timestamp;
    }

    public String getReporterName() {
        return reporterName;
    }

    public String getReason() {
        return reason;
    }

    public UUID getId() {
        return id;
    }

    public void save(ConfigurationSection config) {
        String path = type == InfractionType.REPORT ? "reports" : "warnings" + "." + this.id.toString() + ".";
        config.set(path + "timestamp", timestamp);
        config.set(path + "reporter_uuid", reporterUUID.toString());
        config.set(path + "reason", reason);
        config.set(path + "accused_uuid", accusedUUID.toString());
        config.set(path + "type", type.name());
    }

    public UUID getAccusedUUID() {
       return accusedUUID;
    }

    public UUID getReporterUUID() {
        return reporterUUID;
    }

    public InfractionType getType() {
        return type;
    }
}
