package com.arkflame.staffmodex.managers;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.Material;
import java.util.Set;
import java.util.HashSet;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Map;
import java.util.HashMap;
import java.util.UUID;


public class VeinManager {
    // Initialize a set to store already detected blocks
    private Set<String> detectedBlocks = new HashSet<>();

    private Map<UUID, Integer> streaks = new HashMap<>();
    /**
    * Retrieves the current streak count for the given UUID.
    * @param uuid The UUID of the player whose streak is being queried.
    * @return The current streak count or 0 if the player does not exist in the map.
    */
   public int getStreak(UUID uuid) {
       return streaks.getOrDefault(uuid, 0);
   }

   public int getStreak(Player player) {
       return getStreak(player.getUniqueId());
   }

   /**
    * Increments the streak count for the given UUID by one.
    * @param uuid The UUID of the player whose streak is to be incremented.
    */
   public void incrementStreak(UUID uuid) {
       int currentStreak = streaks.getOrDefault(uuid, 0);
       streaks.put(uuid, currentStreak + 1);
   }

   public void incrementStreak(Player player) {
    incrementStreak(player.getUniqueId());
   }

    public boolean isDetected(Block block) {
        return detectedBlocks.contains(block.getLocation().toString());
    }

    public void addDetected(Block block) {
        detectedBlocks.add(block.getLocation().toString());
    }

    private void addDetected(Set<Block> vein) {
        for (Block block : vein) {
            addDetected(block);
        }
    }

    /**
     * Finds all diamond ores connected to the initially mined diamond ore.
     * @param player 
     * 
     * @param startBlock The initial diamond ore block that was mined.
     * @return A Set containing all connected diamond ore blocks.
     */
    public Set<Block> getConnectedDiamondOres(Player player, Block startBlock) {
        Set<Block> vein = null;
        if (startBlock.getType() != Material.DIAMOND_ORE) {
            return vein;
        }

        if (isDetected(startBlock)) {
            return vein;
        }

        vein = new HashSet<>();
        incrementStreak(player);
        Deque<Block> stack = new ArrayDeque<>();
        stack.push(startBlock);

        while (!stack.isEmpty()) {
            Block currentBlock = stack.pop();
            if (vein.add(currentBlock)) {
                addAdjacentDiamondOresToStack(currentBlock, stack, vein);
            }
        }
        addDetected(vein);
        return vein;
    }

    /**
     * Adds adjacent diamond ore blocks to the stack for further processing.
     * 
     * @param currentBlock The current block being processed.
     * @param stack        The stack to add new blocks to.
     * @param vein         The set of blocks already identified as part of the vein.
     */
    private void addAdjacentDiamondOresToStack(Block currentBlock, Deque<Block> stack, Set<Block> vein) {
        for (int xOffset = -1; xOffset <= 1; xOffset++) {
            for (int yOffset = -1; yOffset <= 1; yOffset++) {
                for (int zOffset = -1; zOffset <= 1; zOffset++) {
                    if (Math.abs(xOffset) + Math.abs(yOffset) + Math.abs(zOffset) == 1) {
                        Block adjacentBlock = currentBlock.getRelative(xOffset, yOffset, zOffset);
                        if (isDetected(adjacentBlock)) {
                            continue;
                        }
                        if (adjacentBlock.getType() == Material.DIAMOND_ORE && !vein.contains(adjacentBlock)) {
                            stack.push(adjacentBlock);
                        }
                    }
                }
            }
        }
    }
}
