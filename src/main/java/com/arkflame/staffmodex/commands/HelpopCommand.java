package com.arkflame.staffmodex.commands;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.arkflame.staffmodex.StaffModeX;
import com.arkflame.staffmodex.modernlib.commands.ModernArguments;
import com.arkflame.staffmodex.modernlib.commands.ModernCommand;
import com.arkflame.staffmodex.player.StaffPlayer;

public class HelpopCommand extends ModernCommand {
    private final Map<String, Long> cooldowns;

    public HelpopCommand() {
        super("helpop");
        this.cooldowns = new HashMap<>();
    }

    @Override
    public void onCommand(CommandSender sender, ModernArguments args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(StaffModeX.getInstance().getMsg().getText("messages.only-players"));
            return;
        }

        if (!(sender.hasPermission("staffmodex.helpop"))) {
            sender.sendMessage(StaffModeX.getInstance().getMsg().getText("messages.helpop.no-permission"));
            return;
        }

        Player player = (Player) sender;
        
        // Handle command cooldown
        if (!player.hasPermission("staffmodex.bypass.cooldown")) {
            long currentTime = System.currentTimeMillis();
            Long playerCooldown = cooldowns.get(player.getName());
            if (playerCooldown != null && playerCooldown > currentTime) {
                long remainingTime = (playerCooldown - currentTime) / 1000;
                player.sendMessage(StaffModeX.getInstance().getMsg().getText("messages.helpop.cooldown")
                        .replace("{time}", String.valueOf(remainingTime)));
                return;
            }
        }
    
        // Require at least one argument
        if (!args.hasArg(0)) {
            player.sendMessage(StaffModeX.getInstance().getMsg().getText("messages.helpop.usage"));
            return;
        }

        String playerName = player.getName();
        String message = String.join(" ", args.getArgs());

        String staffMessage = StaffModeX.getInstance().getMsg().getText("messages.helpop.receive", "{message}", message, "{player}", playerName);
        player.sendMessage(StaffModeX.getInstance().getMsg().getText("messages.helpop.sent", "{message}", message, "{player}", playerName));

        for (StaffPlayer staffPlayer : StaffModeX.getInstance().getStaffPlayerManager().getStaffPlayers().values()) {
            if (staffPlayer.hasPermission("staffmodex.helpop.receive")) {
                staffPlayer.sendMessage(staffMessage);
            }
        }
    }
}
