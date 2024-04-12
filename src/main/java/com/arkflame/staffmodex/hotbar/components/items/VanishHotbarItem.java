package com.arkflame.staffmodex.hotbar.components.items;

import com.arkflame.staffmodex.StaffModeX;
import com.arkflame.staffmodex.hotbar.HotbarItem;
import com.arkflame.staffmodex.modernlib.utils.ChatColors;
import com.arkflame.staffmodex.modernlib.utils.Effects;
import com.arkflame.staffmodex.modernlib.utils.Materials;
import org.bukkit.entity.Player;

import java.util.Arrays;

public class VanishHotbarItem extends HotbarItem {
    public VanishHotbarItem() {
        super(Materials.get("LIME_DYE", "INK_SACK"),
                StaffModeX.getInstance().getMsg().getText("hotbar.vanish.name"),
                1, (short) 10,
                Arrays.asList(StaffModeX.getInstance().getMsg().getText("hotbar.vanish.lore")));
    }

    @Override
    public void onInteract(Player player) {
        StaffModeX.getInstance().getVanishManager().toggleVanish(player);
        if (StaffModeX.getInstance().getVanishManager().isVanished(player)) {
            setType(Materials.get("LIME_DYE", "INK_SACK"));
            setDurability((short) 10);
            player.sendMessage(ChatColors.color(StaffModeX.getInstance().getMsg().getText("hotbar.vanish.vanished")));
            Effects.play(player, "FIREWORK_ROCKET_BLAST");
        } else {
            setType(Materials.get("GRAY_DYE", "INK_SACK"));
            setDurability((short) 8);
            player.sendMessage(ChatColors.color(StaffModeX.getInstance().getMsg().getText("hotbar.vanish.unvanished")));
            Effects.play(player, "FIREWORK_ROCKET_BLAST_FAR");
        }
        player.getInventory().setItem(2, this);
    }
}
