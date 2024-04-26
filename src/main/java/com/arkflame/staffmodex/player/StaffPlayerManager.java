package com.arkflame.staffmodex.player;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.bukkit.OfflinePlayer;

import com.arkflame.staffmodex.modernlib.config.ConfigWrapper;

public class StaffPlayerManager {
    private final Map<UUID, StaffPlayer> staffPlayers = new ConcurrentHashMap<>();

    public StaffPlayer getStaffPlayer(UUID uuid) {
        return staffPlayers.getOrDefault(uuid, null);
    }

    public void addStaffPlayer(StaffPlayer staffPlayer) {
        staffPlayers.put(staffPlayer.getUuid(), staffPlayer);
    }

    public void setStaffPlayers(Map<UUID, StaffPlayer> staffPlayers) {
        this.staffPlayers.clear();
        this.staffPlayers.putAll(staffPlayers);
    }

    public Map<UUID, StaffPlayer> getStaffPlayers() {
        return staffPlayers;
    }

    public void removeStaffPlayer(UUID uuid) {
        staffPlayers.remove(uuid);
    }

    public StaffPlayer getOrCreateStaffPlayer(UUID uuid) {
        ConfigWrapper config = new ConfigWrapper("infractions/" + uuid + ".yml");
        return staffPlayers.computeIfAbsent(uuid, (k) -> new StaffPlayer(k, config).load());
    }

    public StaffPlayer getStaffPlayer(OfflinePlayer player) {
        return getStaffPlayer(player.getUniqueId());
    }
}
