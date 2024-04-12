package com.arkflame.staffmodex.commands;

import com.arkflame.staffmodex.infractions.InfractionType;

public class ReportCommand extends InfractionCommand {
    public ReportCommand() {
        super("report", InfractionType.REPORT);
    }
}
