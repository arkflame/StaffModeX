package com.arkflame.staffmodex.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.arkflame.staffmodex.StaffModeX;
import com.arkflame.staffmodex.modernlib.commands.ModernArguments;
import com.arkflame.staffmodex.modernlib.commands.ModernCommand;
import com.arkflame.staffmodex.player.StaffPlayer;

public class FreezeCommand extends ModernCommand {
    public FreezeCommand() {
        super("freeze", "ss");
    }

    @Override
    public void onCommand(CommandSender sender, ModernArguments args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(StaffModeX.getInstance().getMsg().getText("messages.only-players"));
            return;
        }

        Player player = (Player) sender;
        Player target = null;

        String targetName = args.getText(0);
        if (targetName != null && !targetName.isEmpty()) {
            target = StaffModeX.getInstance().getServer().getPlayer(targetName);
            if (target == null) {
                sender.sendMessage(StaffModeX.getInstance().getMsg().getText("messages.freeze.player-not-online", "{player}", targetName));
                return;
            }
        }

        if (target == null) {
            sender.sendMessage(StaffModeX.getInstance().getMsg().getText("messages.freeze.usage"));
            return;
        }

        StaffPlayer targetStaff = StaffModeX.getInstance().getStaffPlayerManager().getOrCreateStaffPlayer(target);
        targetStaff.toggleFreeze(player);
    }
}
