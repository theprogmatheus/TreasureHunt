package com.github.theprogmatheus.devroom.treasurehunt.gui;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Predicate;

public class Frame implements InventoryHolder {

    private final String title;
    private final int rows;
    private final Map<Integer, Map.Entry<Button, Predicate<Frame>>> buttons;

    public Frame(String title, int rows) {
        this.title = title;
        this.rows = rows;
        this.buttons = new HashMap<>();
    }

    public void show(Player player) {
        Inventory inventory = Bukkit.createInventory(this, this.rows * 9, this.title);
        applyButtons(inventory);
        player.openInventory(inventory);
    }

    protected void applyButtons(Inventory inventory) {
        for (var entry : buttons.entrySet()) {
            if (entry.getValue().getValue().test(this)) {
                inventory.setItem(entry.getKey(), entry.getValue().getKey().getItemStack());
            }
        }
    }

    public void put(int slot, Button button) {
        this.put(slot, button, frame -> true);
    }

    public void put(int slot, Button button, Predicate<Frame> show) {
        this.buttons.put(slot, new AbstractMap.SimpleEntry<>(button, show));
    }

    public String getTitle() {
        return title;
    }

    public int getRows() {
        return rows;
    }

    public Map<Integer, Map.Entry<Button, Predicate<Frame>>> getButtons() {
        return buttons;
    }

    @NotNull
    @Override
    public Inventory getInventory() {
        return null;
    }

    public static void init(Plugin plugin) {
        Bukkit.getPluginManager().registerEvents(new FrameClickListener(), plugin);
    }
}
