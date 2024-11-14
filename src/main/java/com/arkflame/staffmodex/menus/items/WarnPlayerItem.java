package com.arkflame.staffmodex.menus.items;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import com.arkflame.staffmodex.StaffModeX;
import com.arkflame.staffmodex.modernlib.menus.items.MenuItem;
import com.arkflame.staffmodex.player.StaffPlayer;

public class WarnPlayerItem extends MenuItem {
    private InfractionItem infractionItem;
    private StaffPlayer staffPlayer;
    private StaffPlayer staffPlayerTarget;
    private Player player;
    private Player target;

    public WarnPlayerItem(InfractionItem infractionItem, Player player, Player target) {
        super(Material.PAPER, StaffModeX.getInstance().getMsg().getText("menus.warnPlayer.title"), StaffModeX.getInstance().getMsg().getText("menus.warnPlayer.description"));
        this.infractionItem = infractionItem;
        this.player = player;
        this.target = target;

        new BukkitRunnable() {
            @Override
            public void run() {
                setStaffPlayer(StaffModeX.getInstance().getStaffPlayerManager()
                        .getOrCreateStaffPlayer(player.getUniqueId()));
                setStaffPlayerTarget(StaffModeX.getInstance().getStaffPlayerManager()
                        .getOrCreateStaffPlayer(target.getUniqueId()));
            }
        }.runTaskAsynchronously(StaffModeX.getInstance());
    }

    @Override
    public void onClick() {
        // Check for permission
        if (!player.hasPermission("staffmodex.warning")) {
            player.sendMessage(StaffModeX.getInstance().getMessage("messages.no-permission"));
            return;
        }

        player.closeInventory();
        staffPlayer.getWarningProcess().startWarning(infractionItem, player, target, staffPlayer, staffPlayerTarget);
        player.sendMessage(StaffModeX.getInstance().getMessage("messages.warning.started").replace("{player}", target.getName()));
    }

    public StaffPlayer getStaffPlayerTarget() {
        return staffPlayerTarget;
    }

    public void setStaffPlayerTarget(StaffPlayer staffPlayer) {
        this.staffPlayerTarget = staffPlayer;
    }

    public StaffPlayer getStaffPlayer() {
        return staffPlayer;
    }

    public void setStaffPlayer(StaffPlayer staffPlayer) {
        this.staffPlayer = staffPlayer;
    }
}
