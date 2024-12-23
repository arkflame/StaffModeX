package com.arkflame.staffmodex.hotbar.components.items;

import com.arkflame.staffmodex.StaffModeX;
import com.arkflame.staffmodex.hotbar.HotbarItem;
import com.arkflame.staffmodex.modernlib.utils.Materials;
import com.arkflame.staffmodex.modernlib.utils.Sounds;

import org.bukkit.Server;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class RandomTeleportHotbarItem extends HotbarItem {
    public RandomTeleportHotbarItem() {
        super(Materials.get(StaffModeX.getInstance().getCfg().getStringList("items.hotbar.r-teleport.material")),
                StaffModeX.getInstance().getMsg().getText("hotbar.random_teleport.name"),
                1, (short) 0,
                StaffModeX.getInstance().getMsg().getTextList("hotbar.random_teleport.lore"));
    }

    @Override
    public void onInteract(Player player) {
        Server server = player.getServer();
        List<Player> otherPlayers = server.getOnlinePlayers().stream()
                .filter(p -> !p.equals(player) && p.isOnline())
                .collect(Collectors.toList());
        if (otherPlayers.isEmpty()) {
            player.sendMessage(StaffModeX.getInstance().getMessage("hotbar.random_teleport.no_players"));
            return;
        }

        Player targetPlayer = otherPlayers.get(new Random().nextInt(otherPlayers.size()));
        player.teleport(targetPlayer.getLocation());
        player.sendMessage(
                StaffModeX.getInstance().getMessage("hotbar.random_teleport.success")
                        .replace("{player}", targetPlayer.getDisplayName()));

        Sounds.play(player, 1.0f, 1.0f, "ENTITY_ENDERMAN_TELEPORT", "ENDERMAN_TELEPORT");
    }
}
