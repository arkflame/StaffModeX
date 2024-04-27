package com.arkflame.staffmodex.player;

import java.util.Collection;
import java.util.UUID;

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

public class StaffPlayer extends UUIDPlayer {
    private final InfractionList warnings;
    private final InfractionList reports;

    private StaffNotes notes;
    private WarningProcess warningProcess;

    // This saves the old location of the staff
    private Location oldLocation = null;

    private StaffPlayerLoader staffPlayerLoader;
    private VanishPlayer vanishPlayer;
    private FreezablePlayer freezablePlayer;

    private boolean staffChat = false;

    public StaffPlayerLoader getStaffPlayerLoader() {
        return staffPlayerLoader;
    }

    public VanishPlayer getVanishPlayer() {
        return vanishPlayer;
    }

    public FreezablePlayer getFreezablePlayer() {
        return freezablePlayer;
    }

    public StaffPlayer(UUID uuid, ConfigWrapper config) {
        super(uuid);
        this.warnings = new InfractionList();
        this.reports = new InfractionList();
        this.notes = new StaffNotes();
        this.warningProcess = new WarningProcess();
        this.staffPlayerLoader = new StaffPlayerLoader(this, config);
        this.vanishPlayer = new VanishPlayer(uuid);
        this.freezablePlayer = new FreezablePlayer(uuid);
    }

    // Get old location of the staff
    public Location getOldLocation() {
        return oldLocation;
    }

    // Set old location of the staff
    public void setOldLocation(Location oldLocation) {
        this.oldLocation = oldLocation;
    }

    // Teleport the player to the old location (check not null)
    public void restoreOldLocation() {
        Player player = getPlayer();
        if (oldLocation != null) {
            player.teleport(oldLocation);
        }
    }

    public void toggleVanish() {
        vanishPlayer.toggleVanish();
    }

    public StaffPlayer save() {
        staffPlayerLoader.save();
        return this;
    }

    public StaffPlayer load() {
        staffPlayerLoader.load();
        return this;
    }

    public void infraction(InfractionType type, String reporter, String reason) {
        String timestamp = DateUtils.getCurrentTimestamp();
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

    public void makeVisible() {
        vanishPlayer.makeInvisible();
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
        return getPlayer().hasPermission("staffmode.staffchat");
    }

    public void sendStaffChat(String msg) {
        String message = StaffModeX.getInstance().getMsg().getText("messages.staffchat.chat", "{player}", getPlayer().getName(), "{message}", msg);
        for (StaffPlayer staffPlayer : StaffModeX.getInstance().getStaffPlayerManager().getStaffPlayers().values()) {
            if (staffPlayer.isStaffChatReceiver()) {
                staffPlayer.sendMessage(message);
            }
        }
    }

    public void toggleStaffChat() {
        staffChat = !staffChat; // Toggle the staff chat status
        ConfigWrapper msg = StaffModeX.getInstance().getMsg();
        String toggleMessage = staffChat ? msg.getText("messages.staffchat.enabled") : msg.getText("messages.staffchat.disabled");
        sendMessage(toggleMessage); // Send a message to the player indicating the toggle status
    }
}
