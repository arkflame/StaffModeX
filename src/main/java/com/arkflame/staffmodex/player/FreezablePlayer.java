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
import com.arkflame.staffmodex.modernlib.utils.PotionEffects;
import com.arkflame.staffmodex.modernlib.utils.Sounds;
import com.arkflame.staffmodex.modernlib.utils.Titles;

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
        if (!player.hasPermission("staffmodex.freeze")) {
            player.sendMessage(msg.getText("messages.freeze.no-permission"));
        } else if (target != null && target.hasPermission("staffmodex.freeze.bypass")) {
            player.sendMessage(msg.getText("messages.freeze.has_bypass"));
        } else if (!StaffModeX.getInstance().getStaffModeManager().isStaff(player)) {
            player.sendMessage(msg.getText("messages.freeze.not-staff"));
        } else if (isFrozen()) {
            player.sendMessage(msg.getText("messages.freeze.unfreeze"));
            Sounds.play(player, 1.0F, 1.0F, "ENDERMAN_TELEPORT", "ENTITY_ENDERMAN_TELEPORT");
            unfreeze();
        } else {
            player.sendMessage(msg.getText("messages.freeze.freeze"));
            Sounds.play(player, 1.0F, 1.0F, "SUCCESSFUL_HIT", "ENTITY_ARROW_HIT_PLAYER");
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
        if (freezeStatus != null) {
            unfreeze();
            return;
        }

        Player player = getPlayer();
        freezeStatus = new FreezeStatus(origin, this);
        origin.addFrozenPlayerByMe(this);
        Player originPlayer = origin.getPlayer();
        ConfigWrapper msg = StaffModeX.getInstance().getMsg();
        if (player != null) {
            player.sendMessage(msg.getText("messages.freeze.frozen", "{player}", originPlayer == null ? "" : originPlayer.getName(), "{time}", String.valueOf(StaffModeX.getInstance().getCfg().getInt("freeze.time"))));
            Titles.sendActionBar(player, msg.getText("messages.freeze.frozen_action"));
            Titles.sendTitle(player, StaffModeX.getInstance().getMsg().getText("messages.freeze.frozen_title"), msg.getText("messages.freeze.frozen_subtitle"), 20, 60, 20);
            
            // Set the player's helmet to the ice cube
            freezeStatus.setHelmet(player.getEquipment().getHelmet());

            PotionEffects.add(player, 0, 20 * 60 * 20, "BLINDNESS");
            Sounds.play(player, 1.0F, 1.0F, "ANVIL_LAND", "BLOCK_ANVIL_LAND");

            player.getEquipment().setHelmet(new ItemStack(Material.PACKED_ICE));
        }
    }
    

    public void unfreeze() {
        if (freezeStatus == null) {
            return;
        }

        Player player = getPlayer();
        freezeStatus.getStaff().removeFrozenPlayerByMe(this);
        if (player != null) {
            ConfigWrapper msg = StaffModeX.getInstance().getMsg();
            player.sendMessage(msg.getText("messages.freeze.unfrozen"));
            Titles.sendActionBar(player, msg.getText("messages.freeze.unfrozen_action"));
            Titles.sendTitle(player, msg.getText("messages.freeze.unfrozen_title"), msg.getText("messages.freeze.unfrozen_subtitle"), 20, 60, 20);
            
            // Set the player's helmet back to air and clear for packed ice
            player.getEquipment().setHelmet(freezeStatus.getHelmet());

            PotionEffects.remove(player, "BLINDNESS");
            Sounds.play(player, 1.0F, 1.0F, "SUCCESSFUL_HIT", "ENTITY_ARROW_HIT_PLAYER");
        }

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
    
    private void sendFrozenChatMessage(Player player, String msg) {
        String message = StaffModeX.getInstance().getMsg().getText("messages.freeze.frozen_msg", "{player}", player.getName(), "{message}", msg);
        FreezablePlayer whoFroze = getWhoFroze();
        whoFroze.sendMessage(message);
        player.sendMessage(message);
        StaffModeX.getInstance().sendMessageToStaffPlayers(message, whoFroze.getUUID());
    }
    
    private void sendStaffChatMessage(Player player, String msg) {
        String message = StaffModeX.getInstance().getMsg().getText("messages.freeze.staff_msg", "{player}", player.getName(), "{message}", msg);
        for (FreezeStatus frozenByMe : getFrozenPlayersByMe()) {
            frozenByMe.getTarget().sendMessage(message);
        }
        player.sendMessage(message);
        StaffModeX.getInstance().sendMessageToStaffPlayers(message, player.getUniqueId());
    }

    public FreezablePlayer getWhoFroze() {
        return freezeStatus.getStaff();
    }
}
