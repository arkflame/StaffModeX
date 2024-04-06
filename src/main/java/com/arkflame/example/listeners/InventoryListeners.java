package com.arkflame.example.listeners;

import org.bukkit.entity.HumanEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;

public class InventoryListeners implements Listener {
    @EventHandler(ignoreCancelled = true)
    public void onInventoryClick(InventoryClickEvent event) {
        HumanEntity human = event.getWhoClicked();
        human.sendMessage("Clicked Inventory");
    }

    @EventHandler(ignoreCancelled = true)
    public void onInventoryDrag(InventoryDragEvent event) {
        HumanEntity human = event.getWhoClicked();
        human.sendMessage("Dragged Inventory");
    }
}
