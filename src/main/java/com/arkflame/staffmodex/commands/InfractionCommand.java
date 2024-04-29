package com.arkflame.staffmodex.commands;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import com.arkflame.staffmodex.StaffModeX;
import com.arkflame.staffmodex.infractions.InfractionType;
import com.arkflame.staffmodex.modernlib.commands.ModernArguments;
import com.arkflame.staffmodex.modernlib.commands.ModernCommand;
import com.arkflame.staffmodex.player.StaffPlayer;

public abstract class InfractionCommand extends ModernCommand {
    private final InfractionType infractionType;
    private final Map<String, Long> cooldowns;

    public InfractionCommand(String name, InfractionType infractionType) {
        super(name);
        this.infractionType = infractionType;
        this.cooldowns = new HashMap<>();
    }

    @Override
    public void onCommand(CommandSender sender, ModernArguments args) {
        if (!sender.hasPermission("staffmodex." + getName())) {
            sender.sendMessage(StaffModeX.getInstance().getMsg().getText("messages.no-permission"));
            return;
        }
    
        if (!(sender instanceof Player)) {
            sender.sendMessage(StaffModeX.getInstance().getMsg().getText("messages.only-players"));
            return;
        }
    
        Player player = (Player) sender;
        long currentTime = System.currentTimeMillis();
    
        // Handle command cooldown
        if (!player.hasPermission("staffmodex.bypass.cooldown")) {
            Long playerCooldown = cooldowns.get(player.getName());
            if (playerCooldown != null && playerCooldown > currentTime) {
                long remainingTime = (playerCooldown - currentTime) / 1000;
                player.sendMessage(StaffModeX.getInstance().getMsg().getText("messages.cooldown")
                        .replace("{time}", String.valueOf(remainingTime)));
                return;
            }
        }
    
        // Require at least one argument
        if (!args.hasArg(1)) {
            player.sendMessage(StaffModeX.getInstance().getMsg().getText("messages." + infractionType.name().toLowerCase() + "-usage"));
            return;
        }
    
        String targetPlayerName = args.getText(0);
        String reason = String.join(" ", Arrays.copyOfRange(args.getArgs(), 1, args.getArgs().length));
    
        // Process infraction asynchronously
        new BukkitRunnable() {
            @Override
            public void run() {
                Player target = Bukkit.getPlayer(targetPlayerName);
                if (target == null) {
                    player.sendMessage(StaffModeX.getInstance().getMsg().getText("messages.player-not-found"));
                    return;
                }
    
                StaffPlayer targetStaffPlayer = StaffModeX.getInstance().getStaffPlayerManager().getOrCreateStaffPlayer(target);
                StaffPlayer staffPlayer = StaffModeX.getInstance().getStaffPlayerManager().getOrCreateStaffPlayer(player);
                targetStaffPlayer.infraction(infractionType, staffPlayer, reason);
                player.sendMessage(StaffModeX.getInstance().getMsg().getText("messages." + infractionType.name().toLowerCase() + "-success")
                        .replace("{player}", targetPlayerName));
    
                long cooldown = StaffModeX.getInstance().getConfig().getLong("infraction_cooldown", 60) * 1000;
                cooldowns.put(player.getName(), currentTime + cooldown);
            }
        }.runTaskAsynchronously(StaffModeX.getInstance());
    }    
}
