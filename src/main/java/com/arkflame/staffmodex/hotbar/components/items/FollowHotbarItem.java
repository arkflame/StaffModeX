package com.arkflame.staffmodex.hotbar.components.items;

import com.arkflame.staffmodex.StaffModeX;
import com.arkflame.staffmodex.hotbar.HotbarItem;
import com.arkflame.staffmodex.modernlib.utils.Materials;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public class FollowHotbarItem extends HotbarItem {
    public FollowHotbarItem() {
        super(Materials.get(StaffModeX.getInstance().getCfg().getStringList("items.hotbar.follow.material")),
                StaffModeX.getInstance().getMsg().getText("hotbar.follow.name"),
                1, (short) 0,
                StaffModeX.getInstance().getMsg().getTextList("hotbar.follow.lore"));
    }

    @Override
    public void onInteract(Player player, Entity target) {
        if (!(target instanceof Player)) {
            player.sendMessage(StaffModeX.getInstance().getMessage("hotbar.follow.invalid"));
            return;
        }

        Player targetPlayer = (Player) target;

        if (player.isInsideVehicle()) {
            player.leaveVehicle();
        }

        targetPlayer.setPassenger(player);

        player.sendMessage(StaffModeX.getInstance().getMessage("hotbar.follow.success")
                .replace("{player}", targetPlayer.getDisplayName()));
    }
}
