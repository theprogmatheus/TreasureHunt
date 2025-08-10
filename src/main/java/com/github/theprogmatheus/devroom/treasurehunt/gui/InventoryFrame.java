package com.github.theprogmatheus.devroom.treasurehunt.gui;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.Consumer;

public class InventoryFrame implements InventoryHolder, Inventory {

    private final Menu menu;
    private final String title;
    private final int rows;
    private final Inventory inventory;
    private final Properties properties;

    public InventoryFrame(Menu menu, String title, int rows) {
        this.menu = menu;
        this.inventory = Bukkit.createInventory(this, (this.rows = rows) * 9, this.title = title);
        this.properties = new Properties();
    }

    public Menu getMenu() {
        return menu;
    }

    public String getTitle() {
        return title;
    }

    public int getRows() {
        return rows;
    }

    public Properties getProperties() {
        return properties;
    }

    public void open(Player player) {
        player.openInventory(getInventory());
    }

    @NotNull
    @Override
    public Inventory getInventory() {
        return inventory;
    }

    @Override
    public int getSize() {
        return inventory.getSize();
    }

    @Override
    public int getMaxStackSize() {
        return inventory.getMaxStackSize();
    }

    @Override
    public void setMaxStackSize(int i) {
        inventory.setMaxStackSize(i);
    }

    @Nullable
    @Override
    public ItemStack getItem(int i) {
        return inventory.getItem(i);
    }

    @Override
    public void setItem(int i, @Nullable ItemStack itemStack) {
        inventory.setItem(i, itemStack);
    }

    @NotNull
    @Override
    public HashMap<Integer, ItemStack> addItem(@NotNull ItemStack... itemStacks) throws IllegalArgumentException {
        return inventory.addItem(itemStacks);
    }

    @NotNull
    @Override
    public HashMap<Integer, ItemStack> removeItem(@NotNull ItemStack... itemStacks) throws IllegalArgumentException {
        return inventory.removeItem(itemStacks);
    }

    @NotNull
    @Override
    public ItemStack[] getContents() {
        return inventory.getContents();
    }

    @Override
    public void setContents(@NotNull ItemStack[] itemStacks) throws IllegalArgumentException {
        inventory.setContents(itemStacks);
    }

    @NotNull
    @Override
    public ItemStack[] getStorageContents() {
        return inventory.getStorageContents();
    }

    @Override
    public void setStorageContents(@NotNull ItemStack[] itemStacks) throws IllegalArgumentException {
        inventory.setStorageContents(itemStacks);
    }

    @Override
    public boolean contains(@NotNull Material material) throws IllegalArgumentException {
        return inventory.contains(material);
    }

    @Contract("null -> false")
    @Override
    public boolean contains(@Nullable ItemStack itemStack) {
        return inventory.contains(itemStack);
    }

    @Override
    public boolean contains(@NotNull Material material, int i) throws IllegalArgumentException {
        return inventory.contains(material, i);
    }

    @Contract("null, _ -> false")
    @Override
    public boolean contains(@Nullable ItemStack itemStack, int i) {
        return inventory.contains(itemStack, i);
    }

    @Contract("null, _ -> false")
    @Override
    public boolean containsAtLeast(@Nullable ItemStack itemStack, int i) {
        return inventory.containsAtLeast(itemStack, i);
    }

    @NotNull
    @Override
    public HashMap<Integer, ? extends ItemStack> all(@NotNull Material material) throws IllegalArgumentException {
        return inventory.all(material);
    }

    @NotNull
    @Override
    public HashMap<Integer, ? extends ItemStack> all(@Nullable ItemStack itemStack) {
        return inventory.all(itemStack);
    }

    @Override
    public int first(@NotNull Material material) throws IllegalArgumentException {
        return inventory.first(material);
    }

    @Override
    public int first(@NotNull ItemStack itemStack) {
        return inventory.first(itemStack);
    }

    @Override
    public int firstEmpty() {
        return inventory.firstEmpty();
    }

    @Override
    public boolean isEmpty() {
        return inventory.isEmpty();
    }

    @Override
    public void remove(@NotNull Material material) throws IllegalArgumentException {
        inventory.remove(material);
    }

    @Override
    public void remove(@NotNull ItemStack itemStack) {
        inventory.remove(itemStack);
    }

    @Override
    public void clear(int i) {
        inventory.clear(i);
    }

    @Override
    public void clear() {
        inventory.clear();
    }

    @NotNull
    @Override
    public List<HumanEntity> getViewers() {
        return inventory.getViewers();
    }

    @NotNull
    @Override
    public InventoryType getType() {
        return inventory.getType();
    }

    @Nullable
    @Override
    public InventoryHolder getHolder() {
        return inventory.getHolder();
    }

    @NotNull
    @Override
    public ListIterator<ItemStack> iterator() {
        return inventory.iterator();
    }

    @NotNull
    @Override
    public ListIterator<ItemStack> iterator(int i) {
        return inventory.iterator(i);
    }

    @Nullable
    @Override
    public Location getLocation() {
        return inventory.getLocation();
    }

    @Override
    public void forEach(Consumer<? super ItemStack> action) {
        inventory.forEach(action);
    }

    @Override
    public Spliterator<ItemStack> spliterator() {
        return inventory.spliterator();
    }
}
