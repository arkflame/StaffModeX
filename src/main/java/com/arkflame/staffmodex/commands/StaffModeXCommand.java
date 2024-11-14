package com.arkflame.staffmodex.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import com.arkflame.staffmodex.StaffModeX;
import com.arkflame.staffmodex.modernlib.commands.ModernArguments;
import com.arkflame.staffmodex.modernlib.commands.ModernCommand;

public class StaffModeXCommand extends ModernCommand {
    public StaffModeXCommand() {
        super("staffmodex");
    }

    @Override
    public void onCommand(CommandSender sender, ModernArguments args) {
        // Check for permission
        if (!sender.hasPermission("staffmodex.admin")) {
            sender.sendMessage(StaffModeX.getInstance().getMessage("messages.no-permission"));
            return;
        }

        // Get first argument
        String firstArg = args.getText(0);

        if ("reload".equalsIgnoreCase(firstArg)) {
            StaffModeX.getInstance().onDisable();
            StaffModeX.getInstance().onEnable();
            sender.sendMessage(StaffModeX.getInstance().getMessage("messages.reloaded"));
        } else {
            sender.sendMessage(ChatColor.BLUE + "StaffModeX by LinsaFTW");
        }
    }
}
