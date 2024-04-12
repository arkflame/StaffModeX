package com.arkflame.staffmodex.menus.items;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import com.arkflame.staffmodex.StaffModeX;
import com.arkflame.staffmodex.modernlib.menus.items.MenuItem;
import com.arkflame.staffmodex.player.StaffPlayer;

public class InfractionItem extends MenuItem {
    int warnings;
    int reports;
    String lastWarning;
    String lastReport;

    public InfractionItem(Player player) {
        // Warnings, Reports, Reason
        super(Material.PAPER, "&bInfractions", "&7Loading...");
        
        asyncUpdate(player);
    }

    public void asyncUpdate(Player player) {
        new BukkitRunnable() {
            @Override
            public void run() {
                StaffPlayer staffPlayer = StaffModeX.getInstance().getStaffPlayerManager().getOrCreateStaffPlayer(player.getUniqueId());
                setWarnings(staffPlayer.getWarnings().count());
                setReports(staffPlayer.getReports().count());
                setLastWarning(staffPlayer.getWarnings().getLast());
                setLastReport(staffPlayer.getReports().getLast());

                new BukkitRunnable() {
                    @Override
                    public void run() {
                        updateLore(warnings, reports, lastWarning, lastReport);
                    }
                }.runTask(StaffModeX.getInstance());
            }
        }.runTaskAsynchronously(StaffModeX.getInstance());
    }

    public void updateLore(int warnings, int reports, String lastWarning, String lastReport) {
        setLore(
            "&bWarnings: &7" + warnings,
            "&bReports: &7" + reports,
            "&bLast Warning: &7" + lastWarning,
            "&bLast Report: &7" + lastReport
        );
    }

    public int getWarnings() {
        return warnings;
    }

    public void setWarnings(int warnings) {
        this.warnings = warnings;
    }

    public int getReports() {
        return reports;
    }

    public void setReports(int reports) {
        this.reports = reports;
    }

    public String getLastWarning() {
        return lastWarning;
    }

    public void setLastWarning(String lastWarning) {
        this.lastWarning = lastWarning;
    }

    public String getLastReport() {
        return lastReport;
    }

    public void setLastReport(String lastReport) {
        this.lastReport = lastReport;
    }

    
}
