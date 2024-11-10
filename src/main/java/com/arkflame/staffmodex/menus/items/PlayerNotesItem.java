package com.arkflame.staffmodex.menus.items;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import com.arkflame.staffmodex.StaffModeX;
import com.arkflame.staffmodex.modernlib.menus.items.MenuItem;
import com.arkflame.staffmodex.player.StaffPlayer;

public class PlayerNotesItem extends MenuItem {
    private Player player;
    private Player target;

    public PlayerNotesItem(Player player, Player target) {
        super(Material.PAPER, StaffModeX.getInstance().getMsg().getText("menus.playerNotes.title"), "&bNotes: &7" + StaffModeX.getInstance().getMsg().getText("menus.playerNotes.loading"));
        this.player = player;
        this.target = target;

        new BukkitRunnable() {
            @Override
            public void run() {
                StaffPlayer staffPlayer = StaffModeX.getInstance().getStaffPlayerManager()
                        .getOrCreateStaffPlayer(player.getUniqueId());

                if (staffPlayer != null) {
                    String notes = staffPlayer.getNotes(target.getUniqueId());
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            updateLore(notes);
                        }
                    }.runTask(StaffModeX.getInstance());
                }
            }
        }.runTaskAsynchronously(StaffModeX.getInstance());
    }

    @Override
    public void onClick() {
        StaffPlayer staffPlayer = StaffModeX.getInstance().getStaffPlayerManager()
                .getOrCreateStaffPlayer(player.getUniqueId());
        staffPlayer.startWritingNote(this, target.getUniqueId(), target.getName());
        player.closeInventory();
        player.sendMessage(StaffModeX.getInstance().getMsg().getText("messages.note-started").replace("{player}", target.getName()));
    }

    public void updateLore(String notes) {
        if (notes == null) {
            setLore(StaffModeX.getInstance().getMsg().getText("menus.playerNotes.noNotes"));
        } else {
            setLore("&bNotes: &7" + notes);
        }
    }
}
