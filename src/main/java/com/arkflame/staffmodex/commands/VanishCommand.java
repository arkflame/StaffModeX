package com.arkflame.staffmodex.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.arkflame.staffmodex.StaffModeX;
import com.arkflame.staffmodex.modernlib.commands.ModernArguments;
import com.arkflame.staffmodex.modernlib.commands.ModernCommand;
import com.arkflame.staffmodex.modernlib.utils.Effects;

public class VanishCommand extends ModernCommand {
    public VanishCommand() {
        super("vanish");
    }

    @Override
    public void onCommand(CommandSender sender, ModernArguments args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            StaffModeX.getInstance().getVanishManager().toggleVanish(player);
            if (StaffModeX.getInstance().getVanishManager().isVanished(player)) {
                player.sendMessage(StaffModeX.getInstance().getMsg().getText("hotbar.vanish.vanished"));
                Effects.play(player, "FIREWORK_ROCKET_BLAST");
            } else {
                player.sendMessage(StaffModeX.getInstance().getMsg().getText("hotbar.vanish.unvanished"));
                Effects.play(player, "FIREWORK_ROCKET_BLAST_FAR");
            }
        } else {
            sender.sendMessage(StaffModeX.getInstance().getMsg().getText("messages.only-players"));
        }
    }
}
