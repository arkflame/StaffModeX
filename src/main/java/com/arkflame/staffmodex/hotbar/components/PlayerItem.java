package com.arkflame.staffmodex.hotbar.components;

import org.bukkit.entity.Player;

import com.arkflame.staffmodex.modernlib.menus.items.MenuItem;
import com.arkflame.staffmodex.modernlib.utils.Materials;

public class PlayerItem extends MenuItem {
    public PlayerItem(Player miner) {
        super(Materials.get("SKULL_ITEM", "PLAYER_HEAD"), 1, (short) 3,
                "&b" + miner.getName(),
                "&bLocation: &7" + miner.getLocation().getBlockX() + ", " + miner.getLocation().getBlockY() + ", "
                        + miner.getLocation().getBlockZ()
                        + "\n&bHealth: &7" + miner.getHealth() + "/" + miner.getMaxHealth() + "\n&bFood: &7"
                        + miner.getFoodLevel()
                        + "\n&bXP: &7" + miner.getExp() + "/" + miner.getExpToLevel() + "\n&bLevel: &7"
                        + miner.getLevel()
                        + "\n&bGamemode: &7" + miner.getGameMode().name()
                        + "\n&bIP: &7" + miner.getAddress().getAddress().getHostAddress() + ":"
                        + miner.getAddress().getPort()
                        + "\n&bUUID: &7" + miner.getUniqueId().toString());
    }

    @Override
    public void onClick() {
        // Must be overridden
    }
}
