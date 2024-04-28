package com.arkflame.staffmodex.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.arkflame.staffmodex.StaffModeX;
import com.arkflame.staffmodex.modernlib.commands.ModernArguments;
import com.arkflame.staffmodex.modernlib.commands.ModernCommand;

public class StaffModeCommand extends ModernCommand {
    public StaffModeCommand() {
        super("staffmode", "staff", "mod");
    }

    @Override
    public void onCommand(CommandSender sender, ModernArguments args) {
        // Check for permission
        if (!sender.hasPermission("staffmodex.staffmode")) {
            sender.sendMessage(StaffModeX.getInstance().getMsg().getText("messages.no-permission"));
            return;
        }

        if (sender instanceof Player) {
            Player player = (Player) sender;

            StaffModeX.getInstance().getStaffModeManager().toggleStaff(player);
        } else {
            sender.sendMessage(StaffModeX.getInstance().getMsg().getText("messages.only-players"));
        }
    }
}
