package com.arkflame.staffmodex.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.arkflame.staffmodex.StaffModeX;
import com.arkflame.staffmodex.modernlib.commands.ModernArguments;
import com.arkflame.staffmodex.modernlib.commands.ModernCommand;
import com.arkflame.staffmodex.player.StaffPlayer;

public class StaffChatCommand extends ModernCommand {
    public StaffChatCommand() {
        super("staffchat");
    }

    @Override
    public void onCommand(CommandSender sender, ModernArguments args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (player.hasPermission("staffmode.staffchat")) {
                StaffPlayer staffPlayer = StaffModeX.getInstance().getStaffPlayerManager().getOrCreateStaffPlayer(player);

                staffPlayer.toggleStaffChat();

            }
        } else {
            sender.sendMessage(StaffModeX.getInstance().getMsg().getText("messages.only-players"));
        }
    }
}
