package com.arkflame.staffmodex;

import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import com.arkflame.staffmodex.commands.ExamineCommand;
import com.arkflame.staffmodex.commands.FreezeCommand;
import com.arkflame.staffmodex.commands.InfractionsCommand;
import com.arkflame.staffmodex.commands.ReportCommand;
import com.arkflame.staffmodex.commands.StaffChatCommand;
import com.arkflame.staffmodex.commands.StaffModeCommand;
import com.arkflame.staffmodex.commands.VanishCommand;
import com.arkflame.staffmodex.commands.WarnCommand;
import com.arkflame.staffmodex.expansion.StaffModePlaceholderExpansion;
import com.arkflame.staffmodex.hotbar.HotbarManager;
import com.arkflame.staffmodex.inventories.InventoryManager;
import com.arkflame.staffmodex.listeners.BlockListeners;
import com.arkflame.staffmodex.listeners.EntityListeners;
import com.arkflame.staffmodex.listeners.InventoryListeners;
import com.arkflame.staffmodex.listeners.PlayerListeners;
import com.arkflame.staffmodex.managers.DatabaseManager;
import com.arkflame.staffmodex.managers.RedisManager;
import com.arkflame.staffmodex.managers.StaffModeManager;
import com.arkflame.staffmodex.modernlib.config.ConfigWrapper;
import com.arkflame.staffmodex.modernlib.menus.listeners.MenuListener;
import com.arkflame.staffmodex.player.StaffPlayer;
import com.arkflame.staffmodex.player.StaffPlayerManager;
import com.arkflame.staffmodex.tasks.StaffActionBarTask;

public class StaffModeX extends JavaPlugin {
    private HotbarManager hotbarManager = new HotbarManager();
    private InventoryManager inventoryManager = new InventoryManager(this, "inventories.yml");
    private StaffModeManager staffModeManager = new StaffModeManager();
    private StaffPlayerManager staffPlayerManager = new StaffPlayerManager();
    private RedisManager redisManager;
    private DatabaseManager mySQLManager;

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

    public InventoryManager getInventoryManager() {
        return inventoryManager;
    }

    public StaffModeManager getStaffModeManager() {
        return staffModeManager;
    }

    public StaffPlayerManager getStaffPlayerManager() {
        return staffPlayerManager;
    }

    public RedisManager getRedisManager() {
        return redisManager;
    }

    @Override
    public void onEnable() {
        // Set static instance
        setInstance(this);

        // Save default config
        config = new ConfigWrapper("config.yml").saveDefault().load();
        messages = new ConfigWrapper("messages.yml").saveDefault().load();

        // Initialize Redis
        redisManager = new RedisManager(this);

        // Initialize MySQL
        mySQLManager = new DatabaseManager(
                config.getConfig().getBoolean("mysql.enabled"),
                config.getString("mysql.url"),
                config.getString("mysql.username"),
                config.getString("mysql.password"));

        // Register Listeners
        PluginManager pluginManager = this.getServer().getPluginManager();

        pluginManager.registerEvents(new BlockListeners(), this);
        pluginManager.registerEvents(new EntityListeners(), this);
        pluginManager.registerEvents(new InventoryListeners(), this);
        pluginManager.registerEvents(new PlayerListeners(), this);
        pluginManager.registerEvents(new MenuListener(), this);

        // Register Commands
        new ExamineCommand().register();
        new FreezeCommand().register();
        new InfractionsCommand().register();
        new ReportCommand().register();
        new StaffChatCommand().register();
        new StaffModeCommand().register();
        new WarnCommand().register();
        new VanishCommand().register();

        // Register tasks
        new StaffActionBarTask().register();

        // Small check to make sure that PlaceholderAPI is installed
        if (pluginManager.getPlugin("PlaceholderAPI") != null) {
            new StaffModePlaceholderExpansion().register();
        }
    }

    @Override
    public void onDisable() {
        HandlerList.unregisterAll(this);

        for (Player player : this.getServer().getOnlinePlayers()) {
            StaffPlayer staffPlayer = getStaffPlayerManager().getStaffPlayer(player);
            if (staffPlayer != null) {
                staffPlayer.unfreeze();
            }
            getStaffModeManager().removeStaff(player);
        }

        // Close everyone's inventory
        for (Player player : this.getServer().getOnlinePlayers()) {
            player.closeInventory();
        }
    }

    private static StaffModeX instance;

    public static void setInstance(StaffModeX instance) {
        StaffModeX.instance = instance;
    }

    public static StaffModeX getInstance() {
        return StaffModeX.instance;
    }

    public DatabaseManager getMySQLManager() {
        return mySQLManager;
    }
}