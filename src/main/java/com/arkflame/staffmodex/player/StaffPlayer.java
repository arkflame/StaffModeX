package com.arkflame.staffmodex.player;

import java.util.Collection;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerMoveEvent;

import com.arkflame.staffmodex.StaffModeX;
import com.arkflame.staffmodex.infractions.Infraction;
import com.arkflame.staffmodex.infractions.InfractionList;
import com.arkflame.staffmodex.infractions.InfractionType;
import com.arkflame.staffmodex.menus.items.PlayerNotesItem;
import com.arkflame.staffmodex.modernlib.config.ConfigWrapper;
import com.arkflame.staffmodex.modernlib.utils.DateUtils;

import me.clip.placeholderapi.PlaceholderAPI;

public class StaffPlayer extends UUIDPlayer {
    private InfractionList warnings;
    private InfractionList reports;

    private StaffNotes notes;
    private WarningProcess warningProcess;

    // This saves the old location
    private Location restoreLocation = null;

    // This saves the old gamemode
    private GameMode restoreGameMode = null;

    // This saves the old staff chat
    private boolean restoreStaffChat = false;

    private StaffPlayerLoader staffPlayerLoader;
    private VanishPlayer vanishPlayer;
    private FreezablePlayer freezablePlayer;

    private boolean staffChat = false;

    private String ip = null;

    public StaffPlayerLoader getStaffPlayerLoader() {
        return staffPlayerLoader;
    }

    public VanishPlayer getVanishPlayer() {
        return vanishPlayer;
    }

    public FreezablePlayer getFreezablePlayer() {
        return freezablePlayer;
    }

    public StaffPlayer(UUID uuid, ConfigWrapper infractionsConfig, ConfigWrapper ipsConfig) {
        super(uuid);
        this.warnings = new InfractionList();
        this.reports = new InfractionList();
        this.notes = new StaffNotes();
        this.warningProcess = new WarningProcess();
        this.staffPlayerLoader = new StaffPlayerLoader(this, infractionsConfig, ipsConfig);
        this.vanishPlayer = new VanishPlayer(uuid);
        this.freezablePlayer = new FreezablePlayer(uuid);
    }

    // Get old location of the staff
    public Location getRestoreLocation() {
        return restoreLocation;
    }

    // Set old location of the staff
    public void setRestoreLocation(Location restoreLocation) {
        this.restoreLocation = restoreLocation;
    }

    // Teleport the player to the old location (check not null)
    public void restoreLocation() {
        Player player = getPlayer();
        if (restoreLocation != null) {
            player.teleport(restoreLocation);
        }
    }

    public GameMode getRestoreGameMode() {
        return restoreGameMode;
    }

    public void setRestoreGameMode(GameMode gameMode) {
        this.restoreGameMode = gameMode;
    }

    public void restoreGameMode() {
        Player player = getPlayer();
        if (restoreGameMode != null) {
            player.setGameMode(restoreGameMode);
        }
    }

    public void setRestoreStaffChat(boolean staffChat) {
        this.restoreStaffChat = staffChat;
    }

    public void restoreStaffChat() {
        if (staffChat) {
            setStaffChat(restoreStaffChat);
        }
    }

    public void toggleVanish() {
        vanishPlayer.toggleVanish();
    }

    public StaffPlayer load() {
        staffPlayerLoader.load();
        return this;
    }

    public void infraction(InfractionType type, StaffPlayer reporter, String reason) {
        String timestamp = DateUtils.getCurrentTimestamp();
        Infraction infraction = new Infraction(timestamp, reporter.getName(), reason, getUUID(), reporter.getUUID(), type);
        if (type == InfractionType.WARNING) {
            warnings.addInfraction(infraction);

            sendMessage(StaffModeX.getInstance().getMsg().getText("messages.warning.receive-self", "{staff}", reporter.getName(), "{player}", getName(), "{reason}", reason));
            for (StaffPlayer staffPlayer : StaffModeX.getInstance().getStaffPlayerManager().getStaffPlayers().values()) {
                if (staffPlayer.hasPermission("staffmodex.warning.receive")) {
                    staffPlayer.sendMessage(StaffModeX.getInstance().getMsg().getText("messages.warning.receive", "{staff}", reporter.getName(), "{player}", getName(), "{reason}", reason));
                }
            }
        } else if (type == InfractionType.REPORT) {
            reports.addInfraction(infraction);
            
            for (StaffPlayer staffPlayer : StaffModeX.getInstance().getStaffPlayerManager().getStaffPlayers().values()) {
                if (staffPlayer.hasPermission("staffmodex.report.receive")) {
                    staffPlayer.sendMessage(StaffModeX.getInstance().getMsg().getText("messages.report.receive", "{staff}", reporter.getName(), "{player}", getName(), "{reason}", reason));
                }
            }
        }
        staffPlayerLoader.save(infraction);
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

    public void makeVisible() {
        vanishPlayer.makeVisible();
    }

    public void makeInvisible() {
        vanishPlayer.makeInvisible();
    }

    public boolean isVanished() {
        return vanishPlayer.isVanished();
    }

    public boolean isFrozen() {
        return freezablePlayer.isFrozen();
    }

    public void toggleFreeze(Player player) {
        freezablePlayer.toggleFreeze(player);
    }

    public FreezeStatus getFreezeStatus() {
        return freezablePlayer.getFreezeStatus();
    }

    public Collection<FreezeStatus> getFrozenPlayersByMe() {
        return freezablePlayer.getFrozenPlayersByMe();
    }

    public FreezablePlayer getWhoFroze() {
        return freezablePlayer.getWhoFroze();
    }

    public void unfreeze() {
        freezablePlayer.unfreeze();
    }

    public void freeze(StaffPlayer origin) {
        freezablePlayer.freeze(origin.getFreezablePlayer());
    }

    public void preventMovement(PlayerMoveEvent event) {
        freezablePlayer.preventMovement(event);
    }

    public boolean sendFreezeChat(String message) {
        return freezablePlayer.sendFreezeChat(message);
    }

    public boolean isStaffChat() {
        return staffChat;
    }

    public void setStaffChat(boolean staffChat) {
        this.staffChat = staffChat;
    }

    public boolean isStaffChatReceiver() {
        Player player = getPlayer();
        return player != null ? player.hasPermission("staffmodex.staffchat") : false;
    }

    public void sendStaffChat(String msg) {
        String message = StaffModeX.getInstance().getMsg().getText("messages.staffchat.chat", "{player}", getPlayer().getName(), "{message}", msg, "{server}", StaffModeX.getInstance().getServerName());
        message = PlaceholderAPI.setPlaceholders(getPlayer(), message);
        for (StaffPlayer staffPlayer : StaffModeX.getInstance().getStaffPlayerManager().getStaffPlayers().values()) {
            if (staffPlayer.isStaffChatReceiver()) {
                staffPlayer.sendMessage(message);
            }
        }
        Bukkit.getScheduler().runTaskAsynchronously(StaffModeX.getInstance(), () -> StaffModeX.getInstance().getRedisManager().sendStaffChatMessage(getName(), message));
    }

    public void toggleStaffChat() {
        staffChat = !staffChat; // Toggle the staff chat status
        ConfigWrapper msg = StaffModeX.getInstance().getMsg();
        String toggleMessage = staffChat ? msg.getText("messages.staffchat.enabled") : msg.getText("messages.staffchat.disabled");
        sendMessage(toggleMessage); // Send a message to the player indicating the toggle status
    }

    public boolean hasPermission(String permission) {
        Player player = getPlayer();
        return player != null ? player.hasPermission(permission) : false;
    }

    public void hidePlayer(boolean force, Player player) {
        vanishPlayer.hidePlayer(force, player);
    }

    public boolean isForceVanish() {
        return vanishPlayer.isForceVanish();
    }

    public void setIP(String ip) {
        this.ip = ip;
    }

    public String getIP() {
        return ip;
    }
}
