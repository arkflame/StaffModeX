package com.arkflame.staffmodex.commands;

import java.util.Arrays;

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

    public InfractionCommand(String name, InfractionType infractionType) {
        super(name);
        this.infractionType = infractionType;
    }

    @Override
    public void onCommand(CommandSender sender, ModernArguments args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;

            if (!args.hasArg(1)) {
                player.sendMessage(StaffModeX.getInstance().getMsg().getText("messages." + getName() + "-usage"));
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
                                .getText("messages." + getName() + "-success").replace("{player}", playerName);
                        player.sendMessage(successMessage);
                    } else {
                        player.sendMessage(StaffModeX.getInstance().getMsg().getText("messages.player-not-found"));
                    }
                }
            }.runTaskAsynchronously(StaffModeX.getInstance());
        }
    }
}
