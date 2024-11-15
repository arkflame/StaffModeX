package com.arkflame.staffmodex.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.arkflame.staffmodex.StaffModeX;
import com.arkflame.staffmodex.modernlib.commands.ModernArguments;
import com.arkflame.staffmodex.modernlib.commands.ModernCommand;
import com.arkflame.staffmodex.player.StaffPlayer;

public class VanishCommand extends ModernCommand {
    public VanishCommand() {
        super("vanish", "v");
    }

    @Override
    public void onCommand(CommandSender sender, ModernArguments args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            StaffPlayer staffPlayer = StaffModeX.getInstance().getStaffPlayerManager().getOrCreateStaffPlayer(player);
            staffPlayer.toggleVanish();
        } else {
            sender.sendMessage(StaffModeX.getInstance().getMessage("messages.only-players"));
        }
    }
}
