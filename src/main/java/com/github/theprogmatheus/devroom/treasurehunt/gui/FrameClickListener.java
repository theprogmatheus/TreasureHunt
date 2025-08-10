package com.github.theprogmatheus.devroom.treasurehunt.gui;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.InventoryHolder;

import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class FrameClickListener implements Listener {

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getClickedInventory() == null)
            return;

        if (event.getView().getTopInventory() != event.getClickedInventory())
            return;

        InventoryHolder holder = event.getInventory().getHolder();
        if (!(holder instanceof Frame frame))
            return;

        event.setCancelled(true);

        int slot = event.getSlot();

        Map.Entry<Button, Predicate<Frame>> entry = frame.getButtons().get(slot);
        if (entry != null && entry.getValue().test(frame)) {
            handleButtonClick(entry.getKey(), event);
            return;
        }

        if (frame instanceof PagedFrame pagedFrame) {
            Button button = pagedFrame.getButtonAt(event.getWhoClicked().getUniqueId(), slot);
            if (button != null) {
                handleButtonClick(button, event);
            }
        }
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        InventoryHolder holder = event.getInventory().getHolder();
        if (holder instanceof PagedFrame pagedFrame) {
            pagedFrame.clearCache(event.getPlayer().getUniqueId());
        }
    }


    private void handleButtonClick(Button button, InventoryClickEvent event) {
        Consumer<InventoryClickEvent> consumer = button.getConsumers().get(event.getClick());
        if (consumer != null)
            consumer.accept(event);
    }
}
