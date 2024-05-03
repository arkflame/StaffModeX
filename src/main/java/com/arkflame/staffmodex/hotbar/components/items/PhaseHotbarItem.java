package com.arkflame.staffmodex.hotbar.components.items;

import com.arkflame.staffmodex.StaffModeX;
import com.arkflame.staffmodex.hotbar.HotbarItem;
import com.arkflame.staffmodex.modernlib.utils.Materials;
import com.arkflame.staffmodex.modernlib.utils.Sounds;

import java.util.HashSet;
import java.util.Set;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class PhaseHotbarItem extends HotbarItem {
    private static final int MAX_DISTANCE = 5;
    private static final Set<Material> WALKABLE_MATERIALS = new HashSet<>();

    static {
        WALKABLE_MATERIALS.add(Material.AIR);
        WALKABLE_MATERIALS.add(Material.WATER);
        WALKABLE_MATERIALS.add(Material.LAVA);
        WALKABLE_MATERIALS.add(Materials.get("CARPET"));
        WALKABLE_MATERIALS.add(Materials.get("RED_CARPET"));
        WALKABLE_MATERIALS.add(Materials.get("BLUE_CARPET"));
        WALKABLE_MATERIALS.add(Materials.get("GREEN_CARPET"));
        WALKABLE_MATERIALS.add(Materials.get("YELLOW_CARPET"));
        WALKABLE_MATERIALS.add(Materials.get("ORANGE_CARPET"));
        WALKABLE_MATERIALS.add(Materials.get("PINK_CARPET"));
        WALKABLE_MATERIALS.add(Materials.get("PURPLE_CARPET"));
        WALKABLE_MATERIALS.add(Materials.get("CYAN_CARPET"));
        WALKABLE_MATERIALS.add(Materials.get("LIGHT_BLUE_CARPET"));
        WALKABLE_MATERIALS.add(Materials.get("MAGENTA_CARPET"));
        WALKABLE_MATERIALS.add(Materials.get("BLACK_CARPET"));
        WALKABLE_MATERIALS.add(Materials.get("WHITE_CARPET"));
        WALKABLE_MATERIALS.add(Materials.get("BROWN_CARPET"));
        WALKABLE_MATERIALS.add(Materials.get("GRAY_CARPET"));
        WALKABLE_MATERIALS.add(Materials.get("LIGHT_GRAY_CARPET"));

        WALKABLE_MATERIALS.add(Materials.get("OAK_PRESSURE_PLATE"));
        WALKABLE_MATERIALS.add(Materials.get("SPRUCE_PRESSURE_PLATE"));
        WALKABLE_MATERIALS.add(Materials.get("BIRCH_PRESSURE_PLATE"));
        WALKABLE_MATERIALS.add(Materials.get("JUNGLE_PRESSURE_PLATE"));
        WALKABLE_MATERIALS.add(Materials.get("ACACIA_PRESSURE_PLATE"));
        WALKABLE_MATERIALS.add(Materials.get("DARK_OAK_PRESSURE_PLATE"));
        WALKABLE_MATERIALS.add(Materials.get("STONE_PRESSURE_PLATE"));
        WALKABLE_MATERIALS.add(Materials.get("LIGHT_WEIGHTED_PRESSURE_PLATE"));
        WALKABLE_MATERIALS.add(Materials.get("HEAVY_WEIGHTED_PRESSURE_PLATE"));
        WALKABLE_MATERIALS.add(Materials.get("CRIMSON_PRESSURE_PLATE"));
        WALKABLE_MATERIALS.add(Materials.get("WARPED_PRESSURE_PLATE"));
        WALKABLE_MATERIALS.add(Materials.get("POLISHED_BLACKSTONE_PRESSURE_PLATE"));
        WALKABLE_MATERIALS.add(Materials.get("IRON_DOOR"));
        WALKABLE_MATERIALS.add(Materials.get("OAK_DOOR"));
        WALKABLE_MATERIALS.add(Materials.get("IRON_TRAPDOOR"));
        WALKABLE_MATERIALS.add(Materials.get("OAK_TRAPDOOR"));
        WALKABLE_MATERIALS.add(Materials.get("IRON_DOOR_BLOCK"));
        WALKABLE_MATERIALS.add(Materials.get("OAK_DOOR_BLOCK"));
        WALKABLE_MATERIALS.add(Materials.get("IRON_TRAPDOOR_BLOCK"));
        WALKABLE_MATERIALS.add(Materials.get("OAK_TRAPDOOR_BLOCK"));
        WALKABLE_MATERIALS.add(Materials.get("LADDER"));
        WALKABLE_MATERIALS.add(Materials.get("VINE"));
        WALKABLE_MATERIALS.add(Materials.get("SUGAR_CANE"));
        WALKABLE_MATERIALS.add(Materials.get("SEAGRASS"));
        WALKABLE_MATERIALS.add(Materials.get("TALL_SEAGRASS"));
        WALKABLE_MATERIALS.add(Materials.get("BAMBOO"));
        WALKABLE_MATERIALS.add(Materials.get("BAMBOO_SAPLING"));
        WALKABLE_MATERIALS.add(Materials.get("LILY_PAD"));
        WALKABLE_MATERIALS.add(Materials.get("OAK_SLAB"));
        WALKABLE_MATERIALS.add(Materials.get("SPRUCE_SLAB"));
        WALKABLE_MATERIALS.add(Materials.get("BIRCH_SLAB"));
        WALKABLE_MATERIALS.add(Materials.get("JUNGLE_SLAB"));
        WALKABLE_MATERIALS.add(Materials.get("ACACIA_SLAB"));
        WALKABLE_MATERIALS.add(Materials.get("DARK_OAK_SLAB"));
        WALKABLE_MATERIALS.add(Materials.get("STONE_SLAB"));
        WALKABLE_MATERIALS.add(Materials.get("SMOOTH_STONE_SLAB"));
        WALKABLE_MATERIALS.add(Materials.get("SANDSTONE_SLAB"));
        WALKABLE_MATERIALS.add(Materials.get("PETRIFIED_OAK_SLAB"));
        WALKABLE_MATERIALS.add(Materials.get("COBBLESTONE_SLAB"));
        WALKABLE_MATERIALS.add(Materials.get("BRICK_SLAB"));
        WALKABLE_MATERIALS.add(Materials.get("STONE_BRICK_SLAB"));
        WALKABLE_MATERIALS.add(Materials.get("NETHER_BRICK_SLAB"));
        WALKABLE_MATERIALS.add(Materials.get("QUARTZ_SLAB"));
        WALKABLE_MATERIALS.add(Materials.get("RED_SANDSTONE_SLAB"));
        WALKABLE_MATERIALS.add(Materials.get("PURPUR_SLAB"));
        WALKABLE_MATERIALS.add(Materials.get("PRISMARINE_SLAB"));
        WALKABLE_MATERIALS.add(Materials.get("PRISMARINE_BRICK_SLAB"));
        WALKABLE_MATERIALS.add(Materials.get("DARK_PRISMARINE_SLAB"));
        WALKABLE_MATERIALS.add(Materials.get("POLISHED_GRANITE_SLAB"));
        WALKABLE_MATERIALS.add(Materials.get("SMOOTH_RED_SANDSTONE_SLAB"));
        WALKABLE_MATERIALS.add(Materials.get("MOSSY_STONE_BRICK_SLAB"));
        WALKABLE_MATERIALS.add(Materials.get("POLISHED_DIORITE_SLAB"));
        WALKABLE_MATERIALS.add(Materials.get("MOSSY_COBBLESTONE_SLAB"));
        WALKABLE_MATERIALS.add(Materials.get("END_STONE_BRICK_SLAB"));
        WALKABLE_MATERIALS.add(Materials.get("SMOOTH_SANDSTONE_SLAB"));
        WALKABLE_MATERIALS.add(Materials.get("SMOOTH_QUARTZ_SLAB"));
        WALKABLE_MATERIALS.add(Materials.get("GRANITE_SLAB"));
        WALKABLE_MATERIALS.add(Materials.get("ANDESITE_SLAB"));
        WALKABLE_MATERIALS.add(Materials.get("RED_NETHER_BRICK_SLAB"));
        WALKABLE_MATERIALS.add(Materials.get("POLISHED_ANDESITE_SLAB"));
        WALKABLE_MATERIALS.add(Materials.get("DIORITE_SLAB"));
        WALKABLE_MATERIALS.add(Materials.get("CRIMSON_SLAB"));
        WALKABLE_MATERIALS.add(Materials.get("WARPED_SLAB"));
        WALKABLE_MATERIALS.add(Materials.get("PETRIFIED_OAK_SLAB"));
        WALKABLE_MATERIALS.add(Materials.get("WARPED_PRESSURE_PLATE"));
        WALKABLE_MATERIALS.add(Materials.get("CRIMSON_PRESSURE_PLATE"));
    }

    public PhaseHotbarItem() {
        super(Materials.get(StaffModeX.getInstance().getConfig().getStringList("items.hotbar.phase.material")),
                StaffModeX.getInstance().getMsg().getText("hotbar.phase.name"),
                1, (short) 0,
                StaffModeX.getInstance().getMsg().getTextList("hotbar.phase.lore"));
    }

    @Override
    public void onInteract(Player player, Block block) {
        Location playerLocation = player.getLocation();
        Vector direction = playerLocation.getDirection().normalize();
        Location newLocation = playerLocation.clone();

        for (int i = 0; i < MAX_DISTANCE; i++) {
            newLocation.add(direction);

            if (WALKABLE_MATERIALS.contains(newLocation.getBlock().getType())
                    && WALKABLE_MATERIALS.contains(newLocation.getBlock().getRelative(0, 1, 0).getType())) {
                player.teleport(newLocation);
                Sounds.play(player, 1.0f, 1.0f, "ENTITY_ENDERMAN_TELEPORT", "ENDERMAN_TELEPORT");
                return;
            }
        }

        // Send message from config if no safe location is found within the limit
        player.sendMessage(StaffModeX.getInstance().getMsg().getText("hotbar.phase.no_safe_location"));
    }
}
