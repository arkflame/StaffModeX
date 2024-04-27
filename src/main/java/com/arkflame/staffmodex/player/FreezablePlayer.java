package com.arkflame.staffmodex.player;

import java.util.Collection;
import java.util.HashSet;
import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;

import com.arkflame.staffmodex.StaffModeX;
import com.arkflame.staffmodex.modernlib.config.ConfigWrapper;
import com.arkflame.staffmodex.modernlib.utils.Players;

public class FreezablePlayer extends UUIDPlayer {
    // Are you frozen? Save status
    private FreezeStatus freezeStatus = null;

    // Players you froze
    private Collection<FreezeStatus> frozenPlayersByMe = new HashSet<>();

    public FreezablePlayer(UUID uuid) {
        super(uuid);
    }

    public void toggleFreeze(Player player) {
        Player target = getPlayer();
        ConfigWrapper msg = StaffModeX.getInstance().getMsg();
        if (!player.hasPermission("staffmode.freeze")) {
            player.sendMessage(msg.getText("messages.freeze.no_permission"));
        } else if (target.hasPermission("staffmode.freeze.bypass")) {
            player.sendMessage(msg.getText("messages.freeze.has_bypass"));
        } else if (isFrozen()) {
            player.sendMessage(msg.getText("messages.freeze.unfreeze"));
            unfreeze();
        } else {
            player.sendMessage(msg.getText("messages.freeze.freeze"));
            freeze(StaffModeX.getInstance().getStaffPlayerManager().getOrCreateStaffPlayer(player).getFreezablePlayer());
        }
    }

    public FreezeStatus getFreezeStatus() {
        return freezeStatus;
    }

    public boolean isFrozen() {
        return freezeStatus != null;
    }
    
    // This method should be called in a PlayerMoveEvent listener to actually prevent movement
    public void preventMovement(PlayerMoveEvent event) {
        if (isFrozen()) {
            Location to = event.getTo();
            Location newTo = event.getFrom();
            float pitch = to.getPitch();
            float yaw = to.getYaw();
            newTo.setPitch(pitch);
            newTo.setYaw(yaw);
            event.setTo(newTo);
        }
    }

    public void freeze(FreezablePlayer origin) {
        Player player = getPlayer();
        freezeStatus = new FreezeStatus(origin, this);
        origin.addFrozenPlayerByMe(this);
        player.sendMessage(StaffModeX.getInstance().getMsg().getText("messages.freeze.frozen", "{player}", origin.getPlayer().getName()));
    
        // Set the player's helmet to the ice cube
        freezeStatus.setHelmet(player.getEquipment().getHelmet());
        player.getEquipment().setHelmet(new ItemStack(Material.PACKED_ICE));
    }
    

    public void unfreeze() {
        Player player = getPlayer();
        freezeStatus.getStaff().removeFrozenPlayerByMe(this);
        player.sendMessage(StaffModeX.getInstance().getMsg().getText("messages.freeze.unfrozen"));
    
        // Set the player's helmet back to air and clear for packed ice
        player.getEquipment().setHelmet(freezeStatus.getHelmet());
        freezeStatus = null;
    }

    public void addFrozenPlayerByMe(FreezablePlayer player) {
        frozenPlayersByMe.add(player.getFreezeStatus());
    }
    
    public void removeFrozenPlayerByMe(FreezablePlayer player) {
        frozenPlayersByMe.remove(player.getFreezeStatus());
    }

	public Collection<FreezeStatus> getFrozenPlayersByMe() {
		return frozenPlayersByMe;
	}

    public boolean sendFreezeChat(String message) {
        Player player = getPlayer();
    
        if (isFrozen()) {
            sendFrozenChatMessage(player, message);
            return true;
        } else if (!getFrozenPlayersByMe().isEmpty()) {
            sendStaffChatMessage(player, message);
            return true;
        }

        return false;
    }
    
    private void sendMessageToStaffPlayers(String message, UUID playerToSkip) {
        for (StaffPlayer staffPlayer : StaffModeX.getInstance().getStaffPlayerManager().getStaffPlayers().values()) {
            if (staffPlayer.isStaffChatReceiver() && !staffPlayer.getUUID().equals(playerToSkip)) {
                staffPlayer.sendMessage(message);
            }
        }
    }
    
    private void sendFrozenChatMessage(Player player, String msg) {
        String message = StaffModeX.getInstance().getMsg().getText("messages.freeze.frozen_msg", "{player}", player.getName(), "{message}", msg);
        FreezablePlayer whoFroze = getWhoFroze();
        whoFroze.sendMessage(message);
        player.sendMessage(message);
        sendMessageToStaffPlayers(message, whoFroze.getUUID());
    }
    
    private void sendStaffChatMessage(Player player, String msg) {
        String message = StaffModeX.getInstance().getMsg().getText("messages.freeze.staff_msg", "{player}", player.getName(), "{message}", msg);
        for (FreezeStatus frozenByMe : getFrozenPlayersByMe()) {
            frozenByMe.getTarget().sendMessage(message);
        }
        player.sendMessage(message);
        sendMessageToStaffPlayers(message, player.getUniqueId());
    }

    public FreezablePlayer getWhoFroze() {
        return freezeStatus.getStaff();
    }
}
