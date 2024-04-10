package com.arkflame.staffmodex;

import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import com.arkflame.staffmodex.commands.StaffModeCommand;
import com.arkflame.staffmodex.hotbar.HotbarManager;
import com.arkflame.staffmodex.inventories.InventoryManager;
import com.arkflame.staffmodex.listeners.EntityListeners;
import com.arkflame.staffmodex.listeners.InventoryListeners;
import com.arkflame.staffmodex.listeners.PlayerListeners;
import com.arkflame.staffmodex.managers.FreezeManager;
import com.arkflame.staffmodex.managers.StaffModeManager;
import com.arkflame.staffmodex.managers.VanishManager;
import com.arkflame.staffmodex.modernlib.config.ConfigWrapper;
import com.arkflame.staffmodex.modernlib.menus.listeners.MenuListener;

public class StaffModeX extends JavaPlugin {
    private HotbarManager hotbarManager = new HotbarManager();
    private FreezeManager freezeManager = new FreezeManager();
    private VanishManager vanishManager = new VanishManager();
    private InventoryManager inventoryManager = new InventoryManager(this, "inventories.yml");
    private StaffModeManager staffModeManager = new StaffModeManager();

    private ConfigWrapper config;
    private ConfigWrapper messages;

    public ConfigWrapper getCfg() {
        return config;
    }

    public ConfigWrapper getMsg() {
        return messages;
    }

    public HotbarManager getHotbarManager() {
        return hotbarManager;
    }

    public FreezeManager getFreezeManager() {
        return freezeManager;
    }

    public VanishManager getVanishManager() {
        return vanishManager;
    }

    public InventoryManager getInventoryManager() {
        return inventoryManager;
    }

    public StaffModeManager getStaffModeManager() {
        return staffModeManager;
    }

    @Override
    public void onEnable() {
        // Set static instance
        setInstance(this);

        // Save default config
        config = new ConfigWrapper("config.yml").saveDefault().load();
        messages = new ConfigWrapper("messages.yml").saveDefault().load();

        // Register the example listener
        PluginManager pluginManager = this.getServer().getPluginManager();

        pluginManager.registerEvents(new EntityListeners(), this);
        pluginManager.registerEvents(new InventoryListeners(), this);
        pluginManager.registerEvents(new PlayerListeners(), this);

        pluginManager.registerEvents(new MenuListener(), this);

        // Register the example task
        // new ExampleTask().register();

        // Register example commands
        new StaffModeCommand().register();
    }

    @Override
    public void onDisable() {
        HandlerList.unregisterAll(this);

        for (Player player : this.getServer().getOnlinePlayers()) {
            if (hotbarManager.getHotbar(player) != null) {
                hotbarManager.setHotbar(player, null);
                StaffModeX.getInstance().getInventoryManager().loadPlayerInventory(player);
                StaffModeX.getInstance().getInventoryManager().deletePlayerInventory(player);
                player.setAllowFlight(false);
                player.setFlying(false);
            }
        }
    }

    private static StaffModeX instance;

    public static void setInstance(StaffModeX instance) {
        StaffModeX.instance = instance;
    }

    public static StaffModeX getInstance() {
        return StaffModeX.instance;
    }
}