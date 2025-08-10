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

public class Menu implements InventoryHolder {

    private final String title;
    private final int rows;
    private final Map<Integer, Map.Entry<Button, Predicate<InventoryFrame>>> buttons;

    public Menu(String title, int rows) {
        this.title = title;
        this.rows = rows;
        this.buttons = new HashMap<>();
    }

    public void show(Player player) {
        InventoryFrame inventory = new InventoryFrame(this, this.title, this.rows);

        applyButtons(inventory);

        inventory.open(player);
    }

    protected void applyButtons(InventoryFrame inventory) {
        for (var entry : buttons.entrySet()) {
            if (entry.getValue().getValue().test(inventory)) {
                inventory.getInventory().setItem(entry.getKey(), entry.getValue().getKey().getItemStack());
            }
        }
    }

    public void put(int slot, Button button) {
        this.put(slot, button, frame -> true);
    }

    public void put(int slot, Button button, Predicate<InventoryFrame> show) {
        this.buttons.put(slot, new AbstractMap.SimpleEntry<>(button, show));
    }

    public String getTitle() {
        return title;
    }

    public int getRows() {
        return rows;
    }

    public Map<Integer, Map.Entry<Button, Predicate<InventoryFrame>>> getButtons() {
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
