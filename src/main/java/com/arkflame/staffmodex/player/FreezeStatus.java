package com.arkflame.staffmodex.player;

import java.util.concurrent.TimeUnit;

import org.bukkit.inventory.ItemStack;

import com.arkflame.staffmodex.StaffModeX;

public class FreezeStatus {
    private FreezablePlayer staff;
    private FreezablePlayer target;
    private long startTime;
    private long duration;

    private ItemStack helmet = null;

    public FreezeStatus(FreezablePlayer staff, FreezablePlayer target) {
        this.staff = staff;
        this.target = target;
        this.startTime = System.currentTimeMillis();
        this.duration = TimeUnit.MINUTES.toMillis(StaffModeX.getInstance().getCfg().getInt("freeze.time", 5));
    }

    public FreezablePlayer getStaff() {
        return this.staff;
    }

    public FreezablePlayer getTarget() {
        return this.target;
    }

    public long getTimeElapsed() {
        return System.currentTimeMillis() - startTime;
    }

    public String getTimeFormatted() {
        long timeElapsed = getTimeElapsed() / 1000;
        long minutes = timeElapsed / 60;
        long seconds = timeElapsed % 60;
        return String.format("%dm %ds", minutes, seconds);
    }

    public String getCountdownFormatted() {
        long timeRemaining = Math.max(0, duration - getTimeElapsed());
        long minutes = timeRemaining / 60000;
        long seconds = (timeRemaining % 60000) / 1000;
        return String.format("%dm %ds", minutes, seconds);
    }

    public boolean isCountdownFinished() {
        return getTimeElapsed() >= duration;
    }

    public ItemStack getHelmet() {
        return this.helmet;
    }

    public void setHelmet(ItemStack helmet) {
        this.helmet = helmet;
    }
}
