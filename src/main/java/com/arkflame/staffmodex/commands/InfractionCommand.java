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
        // Check for permission
        if (!sender.hasPermission("staffmodex." + getName())) {
            sender.sendMessage(StaffModeX.getInstance().getMsg().getText("messages.no-permission"));
            return;
        }

        if (sender instanceof Player) {
            Player player = (Player) sender;
            long currentTime = System.currentTimeMillis();

            // Check if player is on cooldown
            if (!player.hasPermission("staffmodex.bypass.cooldown")) {
                if (cooldowns.containsKey(player.getName()) && cooldowns.get(player.getName()) > currentTime) {
                    long remainingTime = (cooldowns.get(player.getName()) - currentTime) / 1000;
                    player.sendMessage(StaffModeX.getInstance().getMsg().getText("messages.cooldown")
                            .replace("{time}", String.valueOf(remainingTime)));
                    return;
                }
            }

            if (!args.hasArg(1)) {
                player.sendMessage(StaffModeX.getInstance().getMsg()
                        .getText("messages." + infractionType.name().toLowerCase() + "-usage"));
                return;
            }

            String playerName = args.getText(0);
            // Gather all arguments past the first one to form the reason string
            String[] argsArray = args.getArgs();
            String reason = String.join(" ", Arrays.copyOfRange(argsArray, 1, argsArray.length));

            new BukkitRunnable() {
                @Override
                public void run() {
                    StaffPlayer staffPlayer = StaffModeX.getInstance().getStaffPlayerManager()
                            .getOrCreateStaffPlayer(Bukkit.getOfflinePlayer(playerName).getUniqueId());

                    if (staffPlayer != null) {
                        staffPlayer.infraction(infractionType, player.getName(), reason);
                        String successMessage = StaffModeX.getInstance().getMsg()
                                .getText("messages." + infractionType.name().toLowerCase() + "-success")
                                .replace("{player}", playerName);
                        player.sendMessage(successMessage);
                        // Set cooldown for player
                        long cooldown = StaffModeX.getInstance().getConfig().getLong("infraction_cooldown", 60) * 1000;
                        cooldowns.put(player.getName(), currentTime + cooldown);
                    } else {
                        player.sendMessage(StaffModeX.getInstance().getMsg().getText("messages.player-not-found"));
                    }
                }
            }.runTaskAsynchronously(StaffModeX.getInstance());
        } else {
            sender.sendMessage(StaffModeX.getInstance().getMsg().getText("messages.only-players"));
        }
    }
}
