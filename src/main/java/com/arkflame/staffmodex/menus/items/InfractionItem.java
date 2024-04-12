package com.arkflame.staffmodex.menus.items;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import com.arkflame.staffmodex.StaffModeX;
import com.arkflame.staffmodex.menus.InfractionsMenu;
import com.arkflame.staffmodex.modernlib.menus.items.MenuItem;
import com.arkflame.staffmodex.player.StaffPlayer;

public class InfractionItem extends MenuItem {
    private Player player;
    private Player target;
    private StaffPlayer staffPlayer;
    private int warnings;
    private int reports;
    private String lastWarning;
    private String lastReport;

    public InfractionItem(Player player, Player target) {
        super(Material.PAPER, StaffModeX.getInstance().getMsg().getText("menus.infraction.title"), StaffModeX.getInstance().getMsg().getText("menus.infraction.loading"));
        this.player = player;
        this.target = target;
        
        asyncUpdate(target);
    }

    public void asyncUpdate(Player player) {
        new BukkitRunnable() {
            @Override
            public void run() {
                setStaffPlayer(StaffModeX.getInstance().getStaffPlayerManager().getOrCreateStaffPlayer(player.getUniqueId()));
                setWarnings(staffPlayer.getWarnings().count());
                setReports(staffPlayer.getReports().count());
                setLastWarning(staffPlayer.getWarnings().getLast());
                setLastReport(staffPlayer.getReports().getLast());

                new BukkitRunnable() {
                    @Override
                    public void run() {
                        updateLore();
                    }
                }.runTask(StaffModeX.getInstance());
            }
        }.runTaskAsynchronously(StaffModeX.getInstance());
    }

    public void updateLore() {
        String warningsMsg = StaffModeX.getInstance().getMsg().getText("menus.infraction.warnings").replace("{warnings}", String.valueOf(warnings));
        String reportsMsg = StaffModeX.getInstance().getMsg().getText("menus.infraction.reports").replace("{reports}", String.valueOf(reports));
        String lastWarningMsg = StaffModeX.getInstance().getMsg().getText("menus.infraction.lastWarning").replace("{lastWarning}", lastWarning != null ? lastWarning : "N/A");
        String lastReportMsg = StaffModeX.getInstance().getMsg().getText("menus.infraction.lastReport").replace("{lastReport}", lastReport != null ? lastReport : "N/A");

        setLore(warningsMsg, reportsMsg, lastWarningMsg, lastReportMsg);
    }

    public void setWarnings(int warnings) {
        this.warnings = warnings;
    }

    public void setReports(int reports) {
        this.reports = reports;
    }

    public void setLastWarning(String lastWarning) {
        this.lastWarning = lastWarning;
    }

    public void setLastReport(String lastReport) {
        this.lastReport = lastReport;
    }

    public void setStaffPlayer(StaffPlayer staffPlayer) {
        this.staffPlayer = staffPlayer;
    }

    @Override
    public void onClick() {
        if (staffPlayer == null) {
            return;
        }
        new InfractionsMenu(getMenu(), player, staffPlayer).openInventory(player);
    }
}
