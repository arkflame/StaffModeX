package com.arkflame.staffmodex.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.arkflame.staffmodex.StaffModeX;
import com.arkflame.staffmodex.modernlib.commands.ModernArguments;
import com.arkflame.staffmodex.modernlib.commands.ModernCommand;

public class StaffModeCommand extends ModernCommand {
    public StaffModeCommand() {
        super("staffmode");
    }

    @Override
    public void onCommand(CommandSender sender, ModernArguments args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;

            StaffModeX.getInstance().getStaffModeManager().toggleStaff(player);
        }
    }
}
