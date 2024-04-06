package com.arkflame.example;

import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import com.arkflame.example.commands.ExampleCommand;
import com.arkflame.example.listeners.InventoryListeners;
import com.arkflame.example.listeners.PlayerListeners;
import com.arkflame.example.tasks.ExampleTask;
import com.arkflame.modernlib.config.ConfigWrapper;

public class ExamplePlugin extends JavaPlugin {
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

    private static ExamplePlugin instance;

    public static void setInstance(ExamplePlugin instance) {
        ExamplePlugin.instance = instance;
    }

    public static ExamplePlugin getInstance() {
        return ExamplePlugin.instance;
    }
}