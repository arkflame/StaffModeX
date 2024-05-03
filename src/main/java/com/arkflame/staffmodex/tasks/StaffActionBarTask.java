package com.arkflame.staffmodex.tasks;

import org.bukkit.entity.Player;

import com.arkflame.staffmodex.StaffModeX;
import com.arkflame.staffmodex.modernlib.tasks.ModernTask;
import com.arkflame.staffmodex.modernlib.utils.ServerUtils;
import com.arkflame.staffmodex.modernlib.utils.Titles;
import com.arkflame.staffmodex.player.FreezeStatus;
import com.arkflame.staffmodex.player.StaffPlayer;

public class StaffActionBarTask extends ModernTask {
    public StaffActionBarTask() {
        super(StaffModeX.getInstance(), 5L, false);
    }

    @Override
    public void run() {
        for (Player player : StaffModeX.getInstance().getStaffModeManager().getStaffPlayers()) {
            StaffPlayer staffPlayer = StaffModeX.getInstance().getStaffPlayerManager().getOrCreateStaffPlayer(player);

            if (!staffPlayer.getFrozenPlayersByMe().isEmpty()) {
                if (StaffModeX.getInstance().getConfig().getBoolean("action_bar.on_freeze")) {
                    for (FreezeStatus freezeStatus : staffPlayer.getFrozenPlayersByMe()) {
                        Player otherPlayer = freezeStatus.getTarget().getPlayer();
                        String msg = StaffModeX.getInstance().getMsg().getText("messages.freeze.action_bar",
                                "{countdown}", freezeStatus.getCountdownFormatted());

                        Titles.sendActionBar(player, msg);
                        Titles.sendActionBar(otherPlayer, msg);
                    }
                }
            } else {
                if (StaffModeX.getInstance().getConfig().getBoolean("action_bar.on_staff")) {
                    String vanished = staffPlayer.isVanished() ? "&a✔" : "&c✖";
                    String staffChat = staffPlayer.isStaffChat() ? "&a✔" : "&c✖";
                    String tps = ServerUtils.getTPSFormatted(0);
                    String msg = StaffModeX.getInstance().getMsg().getText("staffmode.action_bar", "{vanished}",
                            vanished,
                            "{staffchat}", staffChat, "{tps}", tps);

                    Titles.sendActionBar(player, msg);
                }
            }
        }
    }
}