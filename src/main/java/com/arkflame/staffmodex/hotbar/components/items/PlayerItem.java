package com.arkflame.staffmodex.hotbar.components.items;

import java.util.List;

import org.bukkit.entity.Player;

import com.arkflame.staffmodex.modernlib.menus.items.MenuItem;
import com.arkflame.staffmodex.modernlib.utils.Materials;
import com.arkflame.staffmodex.modernlib.utils.Players;

import me.clip.placeholderapi.PlaceholderAPI;

import com.arkflame.staffmodex.StaffModeX;
import com.arkflame.staffmodex.menus.ExaminePlayerMenu;

public class PlayerItem extends MenuItem {
    public static String[] getPlaceholders(Player player, Player miner) {
        return new String[] {
                "{playerName}", miner.getName(),
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
                "{ip}", Players.getIP(miner, player),
                "{uuid}", miner.getUniqueId().toString()
        };
    }

    public static List<String> getDescription(Player player, Player miner) {
        List<String> description = StaffModeX.getInstance().getMsg().getTextList(
                "hotbar.playerItem.description",
                getPlaceholders(player, miner));
        description = PlaceholderAPI.setPlaceholders(miner, description);
        return description;
    }

    public static String getTitle(Player player, Player miner) {
        String title = StaffModeX.getInstance().getMsg().getText("hotbar.playerItem.title",
                getPlaceholders(player, miner));
        title = PlaceholderAPI.setPlaceholders(miner, title);
        return title;
    }

    private final Player player;
    private final Player otherPlayer;

    public PlayerItem(Player player, Player otherPlayer) {
        super(Materials.get("SKULL_ITEM", "PLAYER_HEAD"), 1, (short) 3, getTitle(player, otherPlayer),
                getDescription(player, otherPlayer));
        this.player = player;
        this.otherPlayer = otherPlayer;
    }

    @Override
    public void onLeftClick() {
        player.closeInventory();
        player.teleport(otherPlayer.getLocation());
        player.sendMessage(StaffModeX.getInstance().getMsg()
                .getText("hotbar.players.teleport").replace("{player}", otherPlayer.getName()));
    }

    @Override
    public void onRightClick() {
        player.closeInventory();
        new ExaminePlayerMenu(player, otherPlayer).openInventory(player);
    }
}
