package com.github.theprogmatheus.devroom.treasurehunt.gui;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class FrameClickListener implements Listener {

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getClickedInventory() == null)
            return;

        InventoryHolder holder = event.getInventory().getHolder();
        if (!(holder instanceof InventoryFrame frame))
            return;

        event.setCancelled(true);

        ItemStack clickedItem = event.getCurrentItem();
        if (clickedItem == null || clickedItem.getType() == Material.AIR)
            return;
        if (event.getView().getTopInventory() == event.getClickedInventory()) {
            Menu menu = frame.getMenu();

            int slot = event.getSlot();

            Map.Entry<Button, Predicate<InventoryFrame>> entry = menu.getButtons().get(slot);
            if (entry != null) {
                handleButtonClick(entry.getKey(), event);
                return;
            }

            if (menu instanceof PagedMenu) {
                Map<Integer, Button> pageButtons = (Map<Integer, Button>) frame.getProperties().get("page_buttons");

                Button button = pageButtons.get(slot);
                if (button != null) {
                    handleButtonClick(button, event);
                }
            }
        }
    }

    private void handleButtonClick(Button button, InventoryClickEvent event) {
        Consumer<InventoryClickEvent> consumer = button.getConsumers().get(event.getClick());
        if (consumer != null)
            consumer.accept(event);
    }
}
