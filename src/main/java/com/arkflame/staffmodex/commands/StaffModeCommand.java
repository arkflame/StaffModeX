package com.arkflame.staffmodex.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.arkflame.staffmodex.StaffModeX;
import com.arkflame.staffmodex.hotbar.HotbarManager;
import com.arkflame.staffmodex.hotbar.components.StaffHotbar;
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
            HotbarManager hotbarManager = StaffModeX.getInstance().getHotbarManager();
    
            if (hotbarManager.getHotbar(player) != null) {
                hotbarManager.setHotbar(player, null);
                StaffModeX.getInstance().getInventoryManager().loadPlayerInventory(player);
                StaffModeX.getInstance().getInventoryManager().deletePlayerInventory(player);
                StaffModeX.getInstance().getVanishManager().makeVisible(player);
                player.setAllowFlight(false);
                player.setFlying(false);
                player.sendMessage("Deactivated staff mode");
            } else {
                StaffModeX.getInstance().getInventoryManager().savePlayerInventory(player);
                player.getInventory().clear();
                StaffModeX.getInstance().getVanishManager().makeInvisible(player);
                hotbarManager.setHotbar(player, new StaffHotbar());
                player.setAllowFlight(true);
                player.setFlying(true);
                player.sendMessage("Activated staff mode");
            }
        }
    }    
}
