package com.arkflame.staffmodex.managers;

import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class CpsTestingManager {
    private static final Map<Player, Long> testingPlayers = new HashMap<>();

    public static void startCpsTesting(Player player) {
        testingPlayers.put(player, System.currentTimeMillis());
    }

    public static double getAverageCps(Player player) {
        if (!testingPlayers.containsKey(player)) {
            return 0.0; // Player not found in testing
        }

        long startTime = testingPlayers.get(player);
        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;

        // Simulated CPS calculation for demonstration
        int totalClicks = 100; // Total clicks during testing
        double cps = (double) totalClicks / (duration / 1000.0); // CPS calculation

        testingPlayers.remove(player); // Remove player from testing

        return cps;
    }
}