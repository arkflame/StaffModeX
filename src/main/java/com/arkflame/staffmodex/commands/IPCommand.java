package com.arkflame.staffmodex.commands;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.arkflame.staffmodex.StaffModeX;
import com.arkflame.staffmodex.modernlib.commands.ModernArguments;
import com.arkflame.staffmodex.modernlib.commands.ModernCommand;
import com.arkflame.staffmodex.player.StaffPlayer;

public class IPCommand extends ModernCommand {
    public IPCommand() {
        super("ip", "iplog");
    }

    @Override
    public void onCommand(CommandSender sender, ModernArguments args) {
        if (!sender.hasPermission("staffmodex.ip")) {
            sender.sendMessage(StaffModeX.getInstance().getMsg().getText("messages.ip.no-permission"));
            return;
        }

        OfflinePlayer target = null;
        String targetName = args.getText(0);

        if (targetName != null && !targetName.isEmpty()) {
            target = StaffModeX.getInstance().getServer().getPlayer(targetName);
            if (target == null) {
                target = StaffModeX.getInstance().getServer().getOfflinePlayer(targetName);
                StaffPlayer staffPlayer = StaffModeX.getInstance().getStaffPlayerManager()
                        .getOrCreateStaffPlayer(target);
                Bukkit.getScheduler().runTaskAsynchronously(StaffModeX.getInstance(), () -> {
                    staffPlayer.getStaffPlayerLoader().loadIP();
                    String ip = staffPlayer.getIP();
                    if (ip != null) {
                        sender.sendMessage(StaffModeX.getInstance().getMsg().getText("messages.ip.message", "{player}",
                                targetName, "{ip}", ip));
                    } else {
                        sender.sendMessage(StaffModeX.getInstance().getMsg().getText("messages.ip.no-ip", "{player}",
                                targetName));
                    }
                });
                return;
            }
        }

        if (target == null) {
            sender.sendMessage(StaffModeX.getInstance().getMsg().getText("messages.ip.usage"));
            return;
        }

        if (target instanceof Player) {
            StaffPlayer staffPlayer = StaffModeX.getInstance().getStaffPlayerManager().getOrCreateStaffPlayer(target);
            sender.sendMessage(StaffModeX.getInstance().getMsg().getText("messages.ip.message", "{player}", targetName,
                    "{ip}", staffPlayer.getIP()));
        }
    }
}
