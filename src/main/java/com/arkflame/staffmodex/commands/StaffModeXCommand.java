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
            sendAuthor(sender);
            return;
        }

        // Get first argument
        String firstArg = args.getText(0);

        if ("reload".equalsIgnoreCase(firstArg)) {
            StaffModeX.getInstance().onDisable();
            StaffModeX.getInstance().onEnable();
            sender.sendMessage(StaffModeX.getInstance().getMessage("messages.reloaded"));
        } else {
            sendAuthor(sender);
        }
    }

    public void sendAuthor(CommandSender sender) {
        
        String pluginName = StaffModeX.getInstance().getDescription().getName();
        String version = StaffModeX.getInstance().getDescription().getVersion();
        String author = "LinsaFTW";
        String developmentTeam = "ArkFlame Development";

        String line = ChatColor.translateAlternateColorCodes('&', "&8&m&l----------------------------------------");
        String header = ChatColor.translateAlternateColorCodes('&', "&b&l\u272A " + pluginName + " &fv" + version + " &b&l\u272A");
        String authorLine = ChatColor.translateAlternateColorCodes('&', "&fAuthor: &b" + author);
        String teamLine = ChatColor.translateAlternateColorCodes('&', "&fDevelopment Team: &b" + developmentTeam);

        sender.sendMessage(line);
        sender.sendMessage(header);
        sender.sendMessage(authorLine);
        sender.sendMessage(teamLine);
        sender.sendMessage(line);
    }
}
