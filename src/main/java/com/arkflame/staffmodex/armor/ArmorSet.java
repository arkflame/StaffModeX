package com.arkflame.staffmodex.armor;

import org.bukkit.Color;

public class ArmorSet {
    private String permission;
    private Color color;

    public ArmorSet(String permission, Color color) {
        this.permission = permission;
        this.color = color;
    }

    public String getPermission() {
        return permission;
    }

    public Color getColor() {
        return color;
    }
}
