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
    private Map<String, Long> cooldowns;

    public HelpopCommand() {
        super("helpop");
        this.cooldowns = new HashMap<>();
    }

    @Override
    public void onCommand(CommandSender sender, ModernArguments args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(StaffModeX.getInstance().getMessage("messages.only-players"));
            return;
        }

        if (!(sender.hasPermission("staffmodex.helpop"))) {
            sender.sendMessage(StaffModeX.getInstance().getMessage("messages.helpop.no-permission"));
            return;
        }

        Player player = (Player) sender;
        long currentTime = System.currentTimeMillis();
        
        // Handle command cooldown
        if (!player.hasPermission("staffmodex.bypass.cooldown")) {
            Long playerCooldown = cooldowns.get(player.getName());
            if (playerCooldown != null && playerCooldown > currentTime) {
                long remainingTime = (playerCooldown - currentTime) / 1000;
                player.sendMessage(StaffModeX.getInstance().getMessage("messages.helpop.cooldown")
                        .replace("{time}", String.valueOf(remainingTime)));
                return;
            }
        }
    
        // Require at least one argument
        if (!args.hasArg(0)) {
            player.sendMessage(StaffModeX.getInstance().getMessage("messages.helpop.usage"));
            return;
        }

        // Update cooldown based on config
        long cooldown = StaffModeX.getInstance().getConfig().getLong("helpop.cooldown", 60) * 1000;
                cooldowns.put(player.getName(), currentTime + cooldown);

        String playerName = player.getName();
        String message = String.join(" ", args.getArgs());

        String staffMessage = StaffModeX.getInstance().getMessage("messages.helpop.receive", "{message}", message, "{player}", playerName);
        player.sendMessage(StaffModeX.getInstance().getMessage("messages.helpop.sent", "{message}", message, "{player}", playerName));

        for (StaffPlayer staffPlayer : StaffModeX.getInstance().getStaffPlayerManager().getStaffPlayers().values()) {
            if (staffPlayer.hasPermission("staffmodex.helpop.receive")) {
                staffPlayer.sendMessage(staffMessage);
            }
        }
    }
}
