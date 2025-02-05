package com.arkflame.staffmodex.armor;

import org.bukkit.Color;

public class ArmorSet {
    private String permission;
    private Color color;
    private String type;

    public ArmorSet(String permission, Color color, String type) {
        this.permission = permission;
        this.color = color;
        this.type = type;
    }

    public String getPermission() {
        return permission;
    }

    public Color getColor() {
        return color;
    }

    public String getType() {
        if (type == null) {
            return "LEATHER";
        }
        return type;
    }
}
