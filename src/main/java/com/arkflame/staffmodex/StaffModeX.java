package com.arkflame.staffmodex;

import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import com.arkflame.staffmodex.commands.ExampleCommand;
import com.arkflame.staffmodex.listeners.InventoryListeners;
import com.arkflame.staffmodex.listeners.PlayerListeners;
import com.arkflame.staffmodex.modernlib.config.ConfigWrapper;
import com.arkflame.staffmodex.tasks.ExampleTask;

public class StaffModeX extends JavaPlugin {
    private ConfigWrapper config = new ConfigWrapper("config.yml");
    private ConfigWrapper messages = new ConfigWrapper("messages.yml");

    public ConfigWrapper getCfg() {
        return config;
    }

    public ConfigWrapper getMsg() {
        return messages;
    }

    @Override
    public void onEnable() {
        // Set static instance
        setInstance(this);

        // Save default config
        config.saveDefault().load();
        messages.saveDefault().load();

        // Register the example listener
        PluginManager pluginManager = this.getServer().getPluginManager();

        pluginManager.registerEvents(new InventoryListeners(), this);
        pluginManager.registerEvents(new PlayerListeners(), this);

        // Register the example task
        new ExampleTask().register();

        // Register example commands
        new ExampleCommand().register();
    }

    private static StaffModeX instance;

    public static void setInstance(StaffModeX instance) {
        StaffModeX.instance = instance;
    }

    public static StaffModeX getInstance() {
        return StaffModeX.instance;
    }
}