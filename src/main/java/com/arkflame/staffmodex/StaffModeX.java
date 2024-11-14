package com.arkflame.staffmodex;

import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import com.arkflame.staffmodex.armor.ArmorManager;
import com.arkflame.staffmodex.commands.ExamineCommand;
import com.arkflame.staffmodex.commands.FreezeCommand;
import com.arkflame.staffmodex.commands.HelpopCommand;
import com.arkflame.staffmodex.commands.IPCommand;
import com.arkflame.staffmodex.commands.InfractionsCommand;
import com.arkflame.staffmodex.commands.ReportCommand;
import com.arkflame.staffmodex.commands.StaffChatCommand;
import com.arkflame.staffmodex.commands.StaffModeCommand;
import com.arkflame.staffmodex.commands.StaffModeXCommand;
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
import com.arkflame.staffmodex.managers.VeinManager;
import com.arkflame.staffmodex.modernlib.commands.ModernCommand;
import com.arkflame.staffmodex.modernlib.config.ConfigWrapper;
import com.arkflame.staffmodex.modernlib.menus.listeners.MenuListener;
import com.arkflame.staffmodex.player.StaffPlayer;
import com.arkflame.staffmodex.player.StaffPlayerManager;
import com.arkflame.staffmodex.tasks.StaffActionBarTask;

import java.util.Collection;
import java.util.HashSet;
import java.util.UUID;

public class StaffModeX extends JavaPlugin {
    private HotbarManager hotbarManager = new HotbarManager();
    private InventoryManager inventoryManager = new InventoryManager(this, "inventories.yml");
    private StaffModeManager staffModeManager = new StaffModeManager();
    private StaffPlayerManager staffPlayerManager = new StaffPlayerManager();
    private RedisManager redisManager;
    private DatabaseManager mySQLManager;

    private ConfigWrapper config;
    private ConfigWrapper messages;

    private Collection<ModernCommand> commands = new HashSet<>();

    private ArmorManager armorManager;

    private Collection<UUID> visiblePlayers = new HashSet<>();

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

    public ArmorManager getArmorManager() {
        return armorManager;
    }

    @Override
    public void onEnable() {
        // Set static instance
        setInstance(this);
        
        // Reload config
        reloadConfig();

        // Save default config
        config = new ConfigWrapper("config.yml").saveDefault().load();
        messages = new ConfigWrapper("messages.yml").saveDefault().load();

        // Armor MAnager
        armorManager = new ArmorManager(config);

        // Initialize Redis
        redisManager = new RedisManager(this);

        // Initialize MySQL
        mySQLManager = new DatabaseManager(
                config.getBoolean("mysql.enabled"),
                config.getString("mysql.url"),
                config.getString("mysql.username"),
                config.getString("mysql.password"));

        VeinManager veinManager = new VeinManager();

        // Register Listeners
        PluginManager pluginManager = this.getServer().getPluginManager();

        pluginManager.registerEvents(new BlockListeners(veinManager), this);
        pluginManager.registerEvents(new EntityListeners(), this);
        pluginManager.registerEvents(new InventoryListeners(), this);
        pluginManager.registerEvents(new PlayerListeners(), this);
        pluginManager.registerEvents(new MenuListener(), this);

        // Register Commands
        if (StaffModeX.getInstance().getConfig().getBoolean("examine.enabled")) {
            registerCommand(new ExamineCommand());
        }
        if (StaffModeX.getInstance().getConfig().getBoolean("freeze.enabled")) {
            registerCommand(new FreezeCommand());
        }
        if (StaffModeX.getInstance().getConfig().getBoolean("helpop.enabled")) {
            registerCommand(new HelpopCommand());
        }
        if (StaffModeX.getInstance().getConfig().getBoolean("report.enabled") ||
                StaffModeX.getInstance().getConfig().getBoolean("warning.enabled")) {
            registerCommand(new InfractionsCommand());
        }
        if (StaffModeX.getInstance().getConfig().getBoolean("ip.enabled")) {
            registerCommand(new IPCommand());
        }
        if (StaffModeX.getInstance().getConfig().getBoolean("report.enabled")) {
            registerCommand(new ReportCommand());
        }
        if (StaffModeX.getInstance().getConfig().getBoolean("staffchat.enabled")) {
            registerCommand(new StaffChatCommand());
        }
        if (StaffModeX.getInstance().getConfig().getBoolean("staffmode.enabled")) {
            registerCommand(new StaffModeCommand());
        }
        registerCommand(new StaffModeXCommand());
        if (StaffModeX.getInstance().getConfig().getBoolean("warning.enabled")) {
            registerCommand(new WarnCommand());
        }
        if (StaffModeX.getInstance().getConfig().getBoolean("vanish.enabled")) {
            registerCommand(new VanishCommand());
        }

        if (StaffModeX.getInstance().getConfig().getBoolean("action_bar.enabled")) {
            // Register tasks
            new StaffActionBarTask().register();
        }

        // Small check to make sure that PlaceholderAPI is installed
        if (pluginManager.getPlugin("PlaceholderAPI") != null) {
            new StaffModePlaceholderExpansion().register();
        }

        getServer().getScheduler().runTaskAsynchronously(this, () -> {
            // Load everyone's data
            for (Player player : this.getServer().getOnlinePlayers()) {
                getStaffPlayerManager().getOrCreateStaffPlayer(player).load();
            }
        });
    }

    @Override
    public void onDisable() {
        HandlerList.unregisterAll(this);

        for (Player player : this.getServer().getOnlinePlayers()) {
            StaffPlayer staffPlayer = getStaffPlayerManager().getStaffPlayer(player);
            if (staffPlayer != null) {
                staffPlayer.unfreeze();
            }

            // Remove from staff
            getStaffModeManager().removeStaff(player);

            // Close everyone's inventory
            player.closeInventory();
        }

        unregisterCommands();
    }

    public void registerCommand(ModernCommand command) {
        commands.add(command);
        command.register();
    }

    public void unregisterCommands() {
        for (ModernCommand command : commands) {
            command.unregisterBukkitCommand();
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

    public void sendMessageToStaffPlayers(String message, UUID playerToSkip) {
        for (StaffPlayer staffPlayer : StaffModeX.getInstance().getStaffPlayerManager().getStaffPlayers().values()) {
            if (staffPlayer.isStaffChatReceiver() && !staffPlayer.getUUID().equals(playerToSkip)) {
                staffPlayer.sendMessage(message);
            }
        }
    }

    public String getServerName() {
        return StaffModeX.getInstance().getCfg().getString("server_name");
    }

    // Count of visible players
    public int getVisiblePlayerCount() {
        return visiblePlayers.size();
    }

    // Set player as visible or invisible
    public void setVisible(Player player, boolean visible) {
        if (visible) {
            visiblePlayers.add(player.getUniqueId());
        } else {
            visiblePlayers.remove(player.getUniqueId());
        }
    }

    /*
     * Returns the message with the prefix
     */
    public String getMessage(String path, String ...placeholders) {
        return getMsg().getText("messages.prefix") + getMsg().getText(path, placeholders);
    }
}