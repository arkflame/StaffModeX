package com.arkflame.staffmodex.hotbar.components.items;

import com.arkflame.staffmodex.StaffModeX;
import com.arkflame.staffmodex.hotbar.HotbarItem;
import com.arkflame.staffmodex.modernlib.utils.ChatColors;
import com.arkflame.staffmodex.modernlib.utils.Materials;
import com.arkflame.staffmodex.modernlib.utils.Sounds;

import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class RandomTeleportHotbarItem extends HotbarItem {
    public RandomTeleportHotbarItem() {
        super(Materials.get("ENDER_EYE", "EYE_OF_ENDER"),
                StaffModeX.getInstance().getMsg().getText("hotbar.random_teleport.name"),
                1, (short) 0,
                Arrays.asList(StaffModeX.getInstance().getMsg().getText("hotbar.random_teleport.lore")));
    }

    @Override
    public void onInteract(Player player) {
        List<Player> otherPlayers = player.getWorld().getPlayers().stream()
                .filter(p -> !p.equals(player))
                .collect(Collectors.toList());
        if (otherPlayers.isEmpty()) {
            player.sendMessage(ChatColors.color(StaffModeX.getInstance().getMsg().getText("hotbar.random_teleport.no_players")));
            return;
        }

        Player targetPlayer = otherPlayers.get(new Random().nextInt(otherPlayers.size()));
        player.teleport(targetPlayer.getLocation());
        player.sendMessage(
                ChatColors.color(StaffModeX.getInstance().getMsg().getText("hotbar.random_teleport.success")
                        .replace("{player}", targetPlayer.getDisplayName())));

        Sounds.play(player, 1.0f, 1.0f, "ENTITY_ENDERMAN_TELEPORT", "ENDERMAN_TELEPORT");
    }
}
