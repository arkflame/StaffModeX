package com.arkflame.staffmodex.player;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import com.arkflame.staffmodex.StaffModeX;
import com.arkflame.staffmodex.infractions.Infraction;
import com.arkflame.staffmodex.infractions.InfractionList;
import com.arkflame.staffmodex.infractions.InfractionType;
import com.arkflame.staffmodex.menus.items.PlayerNotesItem;
import com.arkflame.staffmodex.modernlib.config.ConfigWrapper;

public class StaffPlayer {
    private final UUID uuid;
    private final InfractionList warnings;
    private final InfractionList reports;
    private final ConfigWrapper config;

    private StaffNotes notes;
    private WarningProcess warningProcess;

    // This saves the old location of the staff
    private Location oldLocation;

    // Get old location of the staff
    public Location getOldLocation() {
        return oldLocation;
    }

    // Set old location of the staff
    public void setOldLocation(Location oldLocation) {
        this.oldLocation = oldLocation;
    }

    // Teleport the player to the old location (check not null)
    public void restoreOldLocation(Player player) {
        if (oldLocation != null) {
            player.teleport(oldLocation);
        }
    }

    public StaffPlayer(UUID uuid, ConfigWrapper config) {
        this.uuid = uuid;
        this.warnings = new InfractionList();
        this.reports = new InfractionList();
        this.config = config;
        this.notes = new StaffNotes();
        this.warningProcess = new WarningProcess();
    }

    public UUID getUuid() {
        return uuid;
    }

    public StaffPlayer load() {
        StaffModeX.getInstance().getLogger().info("Loading configuration...");
        config.load();
    
        ConfigurationSection warningsSection = config.getConfig().getConfigurationSection("warnings");
        if (warningsSection != null) {
            StaffModeX.getInstance().getLogger().info("Loading warnings...");
            warnings.load(warningsSection);
            StaffModeX.getInstance().getLogger().info("Warnings loaded successfully.");
        } else {
            StaffModeX.getInstance().getLogger().warning("No warnings section found. Skipping loading of warnings.");
        }
    
        ConfigurationSection reportsSection = config.getConfig().getConfigurationSection("reports");
        if (reportsSection != null) {
            StaffModeX.getInstance().getLogger().info("Loading reports...");
            reports.load(reportsSection);
            StaffModeX.getInstance().getLogger().info("Reports loaded successfully.");
        } else {
            StaffModeX.getInstance().getLogger().warning("No reports section found. Skipping loading of reports.");
        }
        StaffModeX.getInstance().getLogger().info("Configuration loaded successfully.");
        return this;
    }
    

    public void save() {
        ConfigurationSection warningsSection = config.getConfig().createSection("warnings");
        warnings.getInfractions().forEach(infraction -> infraction.save(warningsSection));

        ConfigurationSection reportsSection = config.getConfig().createSection("reports");
        reports.getInfractions().forEach(infraction -> infraction.save(reportsSection));

        config.save();
    }
    
    public static String getCurrentTimestamp() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        return formatter.format(date);
    }

    public void infraction(InfractionType type, String reporter, String reason) {
        String timestamp = getCurrentTimestamp();
        Infraction infraction = new Infraction(timestamp, reporter, reason);
        if (type == InfractionType.WARNING) {
            warnings.addInfraction(infraction);
        } else if (type == InfractionType.REPORT) {
            reports.addInfraction(infraction);
        }
        save();
    }

    public InfractionList getWarnings() {
        return warnings;
    }

    public InfractionList getReports() {
        return reports;
    }

    public boolean isWritingNote() {
        return notes.isWritingNote();
    }

    public StaffNote writeNote(String message) {
        return notes.writeNote(message);
    }

    public void startWritingNote(PlayerNotesItem item, UUID uuid, String name) {
        notes.startWritingNote(item, uuid, name);
    }

    public String getNotes(UUID uniqueId) {
        return notes.getNote(uniqueId);
    }

    public void openWriteMenu(Player player, String notes) {
        this.notes.openWriteMenu(player, notes);
    }

    public WarningProcess getWarningProcess() {
        return warningProcess;
    }
}
