package com.arkflame.staffmodex.cps;

import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class CpsTestingManager {
    private static final Map<Player, CpsTesting> testingPlayers = new HashMap<>();

    public static void startCpsTesting(Player player) {
        testingPlayers.put(player, new CpsTesting());
    }

    public static boolean isTesting(Player player) {
        return testingPlayers.containsKey(player);
    }

    public static double getAverageCps(Player player) {
        if (!isTesting(player)) {
            return 0.0; // Player not found in testing
        }

        return testingPlayers.get(player).getAverageCps();
    }

    public static void click(Player player) {
        if (!isTesting(player)) {
            return; // Player not found in testing
        }

        testingPlayers.get(player).click();
    }

    public static void stopCpsTesting(Player testedPlayer) {
        if (!isTesting(testedPlayer)) {
            return; // Player not found in testing
        }

        testingPlayers.remove(testedPlayer);
    }
}