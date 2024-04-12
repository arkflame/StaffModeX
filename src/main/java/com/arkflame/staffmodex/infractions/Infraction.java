package com.arkflame.staffmodex.infractions;

import java.util.UUID;
import org.bukkit.configuration.ConfigurationSection;

public class Infraction {
    private final UUID id;
    private String timestamp;
    private String reporter;
    private String reason;

    public Infraction(UUID id, String timestamp, String reporter, String reason) {
        this.id = id;
        this.timestamp = timestamp;
        this.reporter = reporter;
        this.reason = reason;
    }

    public Infraction(String id, String timestamp, String reporter, String reason) {
        this(UUID.fromString(id), timestamp, reporter, reason);
    }

    public Infraction(String timestamp, String reporter, String reason) {
        this(UUID.randomUUID(), timestamp, reporter, reason);
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getReporter() {
        return reporter;
    }

    public void setReporter(String reporter) {
        this.reporter = reporter;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public UUID getId() {
        return id;
    }

    public void save(ConfigurationSection infractionsSection) {
        ConfigurationSection infractionSection = infractionsSection.createSection(id.toString());
        infractionSection.set("timestamp", timestamp);
        infractionSection.set("reporter", reporter);
        infractionSection.set("reason", reason);
    }
}
