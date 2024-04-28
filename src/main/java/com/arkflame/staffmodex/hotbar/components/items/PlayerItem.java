package com.arkflame.staffmodex.hotbar.components.items;

import org.bukkit.entity.Player;

import com.arkflame.staffmodex.modernlib.menus.items.MenuItem;
import com.arkflame.staffmodex.modernlib.utils.Materials;
import com.arkflame.staffmodex.StaffModeX;

public class PlayerItem extends MenuItem {
    public PlayerItem(Player miner) {
        super(Materials.get("SKULL_ITEM", "PLAYER_HEAD"), 1, (short) 3,
                StaffModeX.getInstance().getMsg().getText("hotbar.playerItem.title", "{playerName}", miner.getName()),
                StaffModeX.getInstance().getMsg().getTextList("hotbar.playerItem.description",
                        "{blockX}", String.valueOf(miner.getLocation().getBlockX()),
                        "{blockY}", String.valueOf(miner.getLocation().getBlockY()),
                        "{blockZ}", String.valueOf(miner.getLocation().getBlockZ()),
                        "{health}", String.valueOf(miner.getHealth()),
                        "{maxHealth}", String.valueOf(miner.getMaxHealth()),
                        "{foodLevel}", String.valueOf(miner.getFoodLevel()),
                        "{exp}", String.valueOf(miner.getExp()),
                        "{expToLevel}", String.valueOf(miner.getExpToLevel()),
                        "{level}", String.valueOf(miner.getLevel()),
                        "{gameMode}", miner.getGameMode().name(),
                        "{ip}", miner.getAddress().getAddress().getHostAddress() + ":" + miner.getAddress().getPort(),
                        "{uuid}", miner.getUniqueId().toString()).toArray(new String[0]));
    }

    @Override
    public void onClick() {
        // Must be overridden
    }
}
