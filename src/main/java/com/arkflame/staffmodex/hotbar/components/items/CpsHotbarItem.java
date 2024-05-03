package com.arkflame.staffmodex.hotbar.components.items;

import com.arkflame.staffmodex.StaffModeX;
import com.arkflame.staffmodex.cps.CpsTestingManager;
import com.arkflame.staffmodex.hotbar.HotbarItem;
import com.arkflame.staffmodex.modernlib.utils.ChatColors;
import com.arkflame.staffmodex.modernlib.utils.Materials;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public class CpsHotbarItem extends HotbarItem {
    public CpsHotbarItem() {
        super(Materials.get(StaffModeX.getInstance().getConfig().getStringList("items.hotbar.cps.material")),
                StaffModeX.getInstance().getMsg().getText("hotbar.cps.name"),
                1, (short) 0,
                StaffModeX.getInstance().getMsg().getTextList("hotbar.cps.lore"));
    }

    @Override
    public void onInteract(Player player, Entity target) {
        if (!(target instanceof Player)) {
            player.sendMessage(ChatColors.color(StaffModeX.getInstance().getMsg().getText("hotbar.cps.invalid")));
            return;
        }

        Player testedPlayer = (Player) target;

        if (CpsTestingManager.isTesting(testedPlayer)) {
            player.sendMessage(ChatColors.color(StaffModeX.getInstance().getMsg().getText("hotbar.cps.already_testing")
                    .replace("{player}", testedPlayer.getName())));
            return;
        }

        // Put the tested player in testing mode for 10 seconds
        CpsTestingManager.startCpsTesting(testedPlayer);
        player.sendMessage(ChatColors.color(StaffModeX.getInstance().getMsg().getText("hotbar.cps.start_testing")
                .replace("{player}", testedPlayer.getName())));

        // Wait for 10 seconds
        Bukkit.getScheduler().runTaskLater(StaffModeX.getInstance(), () -> {
            // Get the average CPS of the tested player and format to limit to 1 decimal place
            double averageCps = CpsTestingManager.getAverageCps(testedPlayer);
            CpsTestingManager.stopCpsTesting(testedPlayer);
            String averageCpsString = String.format("%.1f", averageCps);

            // Send message with average CPS and a text saying if it's dangerous or not
            if (averageCps > 10) {
                player.sendMessage(ChatColors
                        .color(StaffModeX.getInstance().getMsg().getText("hotbar.cps.dangerous")
                                .replace("{player}", testedPlayer.getName())
                                .replace("{cps}", averageCpsString)));
            } else {
                player.sendMessage(ChatColors
                        .color(StaffModeX.getInstance().getMsg().getText("hotbar.cps.safe")
                                .replace("{player}", testedPlayer.getName())
                                .replace("{cps}", averageCpsString)));
            }
        }, 200L); // 10 seconds = 200 ticks
    }
}
