package com.arkflame.staffmodex.player;

import org.bukkit.entity.Player;

import com.arkflame.staffmodex.StaffModeX;
import com.arkflame.staffmodex.infractions.InfractionType;
import com.arkflame.staffmodex.menus.items.InfractionItem;

public class WarningProcess {
    private StaffPlayer staffPlayer = null;
    private Player player = null;
    private StaffPlayer staffPlayerTarget = null;
    private Player target = null;
    private InfractionItem infractionItem = null;
    private boolean inProgress = false;

    public boolean isInProgress() {
        return inProgress;
    }

    public StaffPlayer getStaffPlayer() {
        return staffPlayer;
    }

    public Player getPlayer() {
        return player;
    }

    public StaffPlayer getStaffPlayerTarget() {
        return staffPlayerTarget;
    }

    public Player getTarget() {
        return target;
    }

    public InfractionItem getInfractionItem() {
        return infractionItem;
    }

    public void startWarning(InfractionItem item, Player player, Player target, StaffPlayer staffPlayer, StaffPlayer staffPlayerTarget) {
        this.infractionItem = item;
        this.player = player;
        this.target = target;
        this.staffPlayer = staffPlayer;
        this.staffPlayerTarget = staffPlayerTarget;
        this.inProgress = true;
    }

    public void clear() {
        infractionItem = null;
        player = null;
        target = null;
        staffPlayer = null;
        staffPlayerTarget = null;
    }

    public void complete(String reason) {
        inProgress = false;
        staffPlayerTarget.infraction(InfractionType.WARNING, staffPlayer, reason);
    }

    public void openWarningMenu(Player player) {
        if (infractionItem == null) {
            return;
        }
        infractionItem.asyncUpdate(target);
        infractionItem.getMenu().openInventory(player);
    }
}
