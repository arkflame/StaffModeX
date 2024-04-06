package com.arkflame.staffmodex.tasks;

import org.bukkit.Bukkit;

import com.arkflame.staffmodex.StaffModeX;
import com.arkflame.staffmodex.modernlib.tasks.ModernTask;

public class ExampleTask extends ModernTask {
    public ExampleTask() {
        super(StaffModeX.getInstance(), StaffModeX.getInstance().getCfg().getInt("task-repeat-every") * 20L, false);
    }

    @Override
    public void run() {
        final String message = StaffModeX.getInstance().getMsg().getText("messages.from-task");
        Bukkit.getServer().broadcastMessage(message);
    }
}