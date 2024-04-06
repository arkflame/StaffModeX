package com.arkflame.example;

import org.bukkit.plugin.java.JavaPlugin;

import com.arkflame.example.commands.ExampleCommand;
import com.arkflame.example.listeners.PlayerJoinListener;
import com.arkflame.example.tasks.ExampleTask;
import com.arkflame.modernlib.config.ConfigWrapper;

public class ExamplePlugin extends JavaPlugin {
    private ConfigWrapper config = new ConfigWrapper();
    private ConfigWrapper messages = new ConfigWrapper();

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
        ConfigWrapper.saveDefaultConfig("config.yml");
        ConfigWrapper.saveDefaultConfig("messages.yml");
        config.load("config.yml");
        messages.load("messages.yml");

        // Register the example listener
        this.getServer().getPluginManager().registerEvents(new PlayerJoinListener(), this);

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