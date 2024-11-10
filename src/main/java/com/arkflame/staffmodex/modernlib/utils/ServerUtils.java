package com.arkflame.staffmodex.modernlib.utils;

import org.bukkit.Bukkit;
import org.bukkit.Server;

import com.arkflame.staffmodex.StaffModeX;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.DecimalFormat;

public class ServerUtils {
    private static String serverVersion;
    private static Method getServerMethod;
    private static Field tpsField;
    private static Object serverInstance;
    private static DecimalFormat format = new DecimalFormat("##.##");

    static {
        initializeServerUtils();
    }

	public static String getVersion(Server server) {
		String packageName = server.getClass().getPackage().getName();
		String[] packageSplit = packageName.split("\\.");
		String version = packageSplit.length > 3 ? packageSplit[3] : null;
		return version;
	}

    private static void initializeServerUtils() {
        try {
            serverVersion = getVersion(Bukkit.getServer());
            int majorVersion = serverVersion == null ? 21 : Integer.parseInt(serverVersion.split("_")[1]);

            String className;
            if (majorVersion >= 17) {
                className = "net.minecraft.server.MinecraftServer";
            } else {
                className = "net.minecraft.server." + serverVersion + ".MinecraftServer";
            }

            Class<?> minecraftServerClass = Class.forName(className);
            getServerMethod = minecraftServerClass.getMethod("getServer");
            serverInstance = getServerMethod.invoke(null);
            tpsField = minecraftServerClass.getDeclaredField("recentTps");
            tpsField.setAccessible(true);
        } catch (Exception e) {
            e.printStackTrace();
            StaffModeX.getInstance().getLogger().severe("Failed to initialize server utilities");
        }
    }

    public static double getTPS(int time) {
        try {
            double[] tpsArray = (double[]) tpsField.get(serverInstance);
            return tpsArray[Math.min(time, tpsArray.length - 1)];
        } catch (IllegalAccessException | NullPointerException e) {
            // Failed to get TPS
        }
        return 20D;
    }

    public static String getTPSFormatted(int time) {
        double tps = getTPS(time);
        String tpsColor = tps >= 18.0 ? "&a" : tps >= 15.0 ? "&e" : "&c"; // Set color based on TPS value
        return tpsColor + format.format(tps);
    }
}
