package com.github.theprogmatheus.devroom.treasurehunt.gui;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.*;

public class PagedFrame extends Frame {

    private final List<Button> elements;
    private final List<Integer> reservedSlots;
    private final Map<UUID, Map<Integer, Button>> pageButtonCache;

    public PagedFrame(String title, int rows) {
        super(title, rows);
        this.elements = new ArrayList<>();
        this.reservedSlots = new ArrayList<>();
        this.pageButtonCache = new HashMap<>();
    }

    public void show(Player player, int page) {
        int maxPages = getMaxPages();
        if (page <= 0 || page > maxPages)
            return;

        Inventory inventory = Bukkit.createInventory(
                this,
                this.getRows() * 9,
                getFormattedTitle(page)
        );

        Map<Integer, Button> pageButtons = new HashMap<>();

        int maxPerPage = getMaxElementPerPage();
        int offset = (page - 1) * maxPerPage;
        int length = offset + maxPerPage;

        int nextSlot = 0;
        for (int i = offset; i < length; i++) {
            if (i >= this.elements.size())
                break;

            while (nextSlot < inventory.getSize()
                    && this.reservedSlots.contains(nextSlot)) {
                nextSlot++;
            }

            if (nextSlot >= inventory.getSize())
                break;

            Button element = this.elements.get(i);

            inventory.setItem(nextSlot, element.getItemStack());
            pageButtons.put(nextSlot, element);
            nextSlot++;
        }

        applyButtons(inventory);
        this.pageButtonCache.put(player.getUniqueId(), pageButtons);
        player.openInventory(inventory);
    }

    public Button getButtonAt(UUID uuid, int slot) {
        Map<Integer, Button> buttons = pageButtonCache.get(uuid);
        return (buttons != null) ? buttons.get(slot) : null;
    }

    public void clearCache(UUID uuid) {
        pageButtonCache.remove(uuid);
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
