package com.arkflame.staffmodex.hotbar.components.items;

import com.arkflame.staffmodex.StaffModeX;
import com.arkflame.staffmodex.hotbar.HotbarItem;
import com.arkflame.staffmodex.modernlib.utils.Materials;
import com.arkflame.staffmodex.modernlib.utils.Sounds;

import org.bukkit.Server;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class RandomTeleportHotbarItem extends HotbarItem {
    // Map to track the last teleport index for each staff member
    private static final Map<UUID, Integer> TELEPORT_INDICES = new ConcurrentHashMap<>();

    public RandomTeleportHotbarItem() {
        super(Materials.get(StaffModeX.getInstance().getCfg().getStringList("items.hotbar.r-teleport.material")),
                StaffModeX.getInstance().getMsg().getText("hotbar.random_teleport.name"),
                1, (short) 0,
                StaffModeX.getInstance().getMsg().getTextList("hotbar.random_teleport.lore"));
    }

    @Override
    public void onInteract(Player player) {
        Server server = player.getServer();
        List<Player> otherPlayers = getOnlinePlayersExcludingSelf(player);

        if (otherPlayers.isEmpty()) {
            player.sendMessage(StaffModeX.getInstance().getMessage("hotbar.random_teleport.no_players"));
            return;
        }

        // Get the next player index for the staff member
        int nextIndex = getNextTeleportIndex(player.getUniqueId(), otherPlayers.size());
        Player targetPlayer = otherPlayers.get(nextIndex);

        // Teleport the staff member to the target player
        player.teleport(targetPlayer.getLocation());
        player.sendMessage(
                StaffModeX.getInstance().getMessage("hotbar.random_teleport.success")
                        .replace("{player}", targetPlayer.getDisplayName()));

        // Play teleport sound
        Sounds.play(player, 1.0f, 1.0f, "ENTITY_ENDERMAN_TELEPORT", "ENDERMAN_TELEPORT");
    }

    /**
     * Gets the list of online players excluding the given player.
     *
     * @param player The player to exclude.
     * @return A list of online players excluding the given player.
     */
    private List<Player> getOnlinePlayersExcludingSelf(Player player) {
        return player.getServer().getOnlinePlayers().stream()
                .filter(p -> !p.equals(player) && p.isOnline())
                .collect(Collectors.toList());
    }

    /**
     * Gets the next teleport index for the given staff member.
     * Uses modulus to ensure the index loops back to 0 when it reaches the end.
     *
     * @param staffUUID The UUID of the staff member.
     * @param playerCount The total number of online players (excluding the staff member).
     * @return The next teleport index.
     */
    private int getNextTeleportIndex(UUID staffUUID, int playerCount) {
        int currentIndex = TELEPORT_INDICES.getOrDefault(staffUUID, -1);
        int nextIndex = (currentIndex + 1) % playerCount; // Use modulus to loop back to 0
        TELEPORT_INDICES.put(staffUUID, nextIndex); // Update the index for the staff member
        return nextIndex;
    }
}