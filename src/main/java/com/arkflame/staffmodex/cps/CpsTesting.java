package com.arkflame.staffmodex.cps;

public class CpsTesting {
    private long startTime;
    private int totalClicks;

    public CpsTesting() {
        startTime = System.currentTimeMillis();
        totalClicks = 0;
    }

    public void click() {
        totalClicks++;
    }

    public double getAverageCps() {
        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;

        return (double) totalClicks / (duration / 1000.0);
    }
}
