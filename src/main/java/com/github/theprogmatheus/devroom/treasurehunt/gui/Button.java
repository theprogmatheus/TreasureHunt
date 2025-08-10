package com.github.theprogmatheus.devroom.treasurehunt.gui;

import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class Button {

    private final ItemStack itemStack;
    private final Map<ClickType, Consumer<InventoryClickEvent>> consumers;

    public Button(ItemStack itemStack) {
        this.itemStack = itemStack;
        this.consumers = new HashMap<>();
    }

    public ItemStack getItemStack() {
        return itemStack;
    }

    public Map<ClickType, Consumer<InventoryClickEvent>> getConsumers() {
        return consumers;
    }
}
