package com.github.theprogmatheus.devroom.treasurehunt.gui;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

public class PagedMenu extends Menu {

    private final List<Button> elements;
    private final List<Integer> reservedSlots;

    public PagedMenu(String title, int rows) {
        super(title, rows);
        this.elements = new ArrayList<>();
        this.reservedSlots = new ArrayList<>();
        this.createNextAndPreviousButtons();
    }

    private void createNextAndPreviousButtons() {
        int nextButtonSlot = (getRows() * 9) - 1;
        int previousButtonSlot = (getRows() * 9) - 9;

        Button nextButton = new Button(nextPageItem());
        nextButton.getConsumers().put(ClickType.LEFT, event -> {
            if (event.getWhoClicked() instanceof Player player) {
                if (event.getInventory().getHolder() instanceof InventoryFrame frame) {
                    if (frame.getMenu() instanceof PagedMenu pagedMenu)
                        pagedMenu.show(player, (int) frame.getProperties().get("page") + 1);
                }
            }
        });

        Button previousButton = new Button(previousPageItem());
        previousButton.getConsumers().put(ClickType.LEFT, event -> {
            if (event.getWhoClicked() instanceof Player player) {
                if (event.getInventory().getHolder() instanceof InventoryFrame frame) {
                    if (frame.getMenu() instanceof PagedMenu pagedMenu)
                        pagedMenu.show(player, (int) frame.getProperties().get("page") - 1);
                }
            }
        });

        getButtons().put(previousButtonSlot, new AbstractMap.SimpleEntry<>(previousButton,
                frame -> {
                    if (frame.getMenu() instanceof PagedMenu)
                        return (int) frame.getProperties().get("page") > 1;
                    return false;
                }));

        getButtons().put(nextButtonSlot, new AbstractMap.SimpleEntry<>(nextButton,
                frame -> {
                    if (frame.getMenu() instanceof PagedMenu pagedMenu)
                        return (int) frame.getProperties().get("page") < pagedMenu.getMaxPages();
                    return false;
                }));

        this.reservedSlots.add(previousButtonSlot);
        this.reservedSlots.add(nextButtonSlot);
    }

    private ItemStack previousPageItem() {
        ItemStack previousPageItem = new ItemStack(Material.ARROW);
        ItemMeta previousPageMeta = previousPageItem.getItemMeta();
        previousPageMeta.setDisplayName("§aPrevious Page");
        previousPageMeta.setLore(List.of("§7Click to return to the previous page."));
        previousPageItem.setItemMeta(previousPageMeta);

        return previousPageItem;
    }

    private ItemStack nextPageItem() {
        ItemStack nextPageItem = new ItemStack(Material.ARROW);
        ItemMeta nextPageMeta = nextPageItem.getItemMeta();
        nextPageMeta.setDisplayName("§aNext Page");
        nextPageMeta.setLore(List.of("§7Click to go to the next page."));
        nextPageItem.setItemMeta(nextPageMeta);
        return nextPageItem;
    }


    public void show(Player player, int page) {
        if (page <= 0)
            return;

        InventoryFrame inventoryFrame = new InventoryFrame(this, getFormattedTitle(page), this.getRows());
        inventoryFrame.getProperties().put("page", page);

        if (!this.elements.isEmpty()) {
            Map<Integer, Button> pageButtons = new HashMap<>();

            int maxPerPage = getMaxElementPerPage();
            int offset = (page - 1) * maxPerPage;
            int length = offset + maxPerPage;

            int nextSlot = 0;
            for (int i = offset; i < length; i++) {
                if (i >= this.elements.size())
                    break;

                while (nextSlot < inventoryFrame.getSize()
                        && this.reservedSlots.contains(nextSlot)) {
                    nextSlot++;
                }

                if (nextSlot >= inventoryFrame.getSize())
                    break;

                Button element = this.elements.get(i);

                inventoryFrame.setItem(nextSlot, element.getItemStack());
                pageButtons.put(nextSlot, element);
                nextSlot++;
            }

            inventoryFrame.getProperties().put("page_buttons", pageButtons);
        }

        applyButtons(inventoryFrame);

        inventoryFrame.open(player);
    }

    public String getFormattedTitle(int currentPage) {
        return getTitle().replace("%page%", String.valueOf(currentPage));
    }

    @Override
    public void show(Player player) {
        this.show(player, 1);
    }

    public void addElement(Button element) {
        this.elements.add(element);
    }


    public List<Button> getElements() {
        return elements;
    }

    public List<Integer> getReservedSlots() {
        return reservedSlots;
    }

    public int getMaxElementPerPage() {
        return (getRows() * 9) - this.reservedSlots.size();
    }

    public int getMaxPages() {
        return (int) Math.ceil((double) this.elements.size() / getMaxElementPerPage());
    }
}
