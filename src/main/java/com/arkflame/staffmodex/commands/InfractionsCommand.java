package com.arkflame.staffmodex.commands;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.arkflame.staffmodex.StaffModeX;
import com.arkflame.staffmodex.menus.InfractionsMenu;
import com.arkflame.staffmodex.modernlib.commands.ModernArguments;
import com.arkflame.staffmodex.modernlib.commands.ModernCommand;
import com.arkflame.staffmodex.player.StaffPlayer;

public class InfractionsCommand extends ModernCommand {
    public InfractionsCommand() {
        super("infractions", "warnings");
    }

    @Override
    public void onCommand(CommandSender sender, ModernArguments args) {
        Bukkit.getScheduler().runTaskAsynchronously(StaffModeX.getInstance(), () -> {
            if (!(sender instanceof Player)) {
                sender.sendMessage(StaffModeX.getInstance().getMessage("messages.only-players"));
                return;
            }

            Player player = (Player) sender;
            OfflinePlayer target = null;

            String targetName = args.getText(0);
            if (targetName != null && !targetName.isEmpty()) {
                target = StaffModeX.getInstance().getServer().getOfflinePlayer(targetName);
            }

            if (target == null) {
                sender.sendMessage(StaffModeX.getInstance().getMessage("messages.infractions.usage"));
                return;
            }

            StaffPlayer staffPlayer = StaffModeX.getInstance().getStaffPlayerManager().getOrCreateStaffPlayer(target);

            if (!target.isOnline()) {
                staffPlayer.load();
            }
            Bukkit.getScheduler().runTask(StaffModeX.getInstance(), () -> {
                new InfractionsMenu(null, player, staffPlayer).openInventory(player);
            });
        });
    }
}
