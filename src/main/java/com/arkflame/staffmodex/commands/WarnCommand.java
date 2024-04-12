package com.arkflame.staffmodex.commands;

import com.arkflame.staffmodex.infractions.InfractionType;

public class WarnCommand extends InfractionCommand {
    public WarnCommand() {
        super("warn", InfractionType.WARNING);
    }
}
