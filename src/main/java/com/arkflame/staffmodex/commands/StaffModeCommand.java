package com.arkflame.staffmodex.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.arkflame.staffmodex.StaffModeX;
import com.arkflame.staffmodex.hotbar.Hotbar;
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
            if (StaffModeX.getInstance().getHotbarManager().getHotbar(player) != null) {
                StaffModeX.getInstance().getHotbarManager().setHotbar(player, null);
                player.sendMessage("Deactivated staff mode");
            } else {
                StaffModeX.getInstance().getHotbarManager().setHotbar(player, Hotbar.STAFF_HOTBAR);
                player.sendMessage("Activated staff mode");
            }
        }
    }
}
