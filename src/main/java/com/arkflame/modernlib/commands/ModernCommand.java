package com.arkflame.modernlib.commands;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import com.arkflame.example.ExamplePlugin;

public abstract class ModernCommand extends Command {
    public ModernCommand(String name) {
        super(name);
    }

    public void register() {
        try {
            // Get the command map to register the command
            Object commandMap = getCommandMap();
            // Get the method to register the command
            Method registerMethod = getRegisterMethod(commandMap);

            // Register the command
            registerMethod.invoke(commandMap, getName(), this);
        } catch (NoSuchFieldException | SecurityException | IllegalArgumentException |IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            ExamplePlugin.getInstance().getLogger().severe("Exception while handling command register");
            e.printStackTrace();
        }
    }

    private Method getRegisterMethod(Object commandMap) throws NoSuchMethodException, SecurityException {
        return commandMap.getClass().getMethod("register", String.class, Command.class);
    }

    private Object getCommandMap()  throws IllegalAccessException, NoSuchFieldException {
        Server server = Bukkit.getServer();
        Field bukkitCommandMap = server.getClass().getDeclaredField("commandMap");

        bukkitCommandMap.setAccessible(true);

        return bukkitCommandMap.get(server);
    }

    @Override
    public boolean execute(CommandSender sender, String label, String[] args) {
        onCommand(sender, new ModernArguments(label, args));
        return true;
    }

    // Implement this method in your own class to define logic
    public abstract void onCommand(CommandSender sender, ModernArguments args);
}
