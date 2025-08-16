package com.github.theprogmatheus.devroom.treasurehunt.gui.menus;

import com.github.theprogmatheus.devroom.treasurehunt.TreasureHunt;
import com.github.theprogmatheus.devroom.treasurehunt.TreasureManager;
import com.github.theprogmatheus.devroom.treasurehunt.database.entity.TreasureEntity;
import com.github.theprogmatheus.devroom.treasurehunt.database.repository.TreasureRepository;
import com.github.theprogmatheus.devroom.treasurehunt.gui.Button;
import com.github.theprogmatheus.devroom.treasurehunt.gui.PagedMenu;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class TreasureListMenu {


    private final TreasureManager manager;
    private final TreasureRepository repository;
    private final List<TreasureEntity> treasures;

    public TreasureListMenu() {
        this.manager = TreasureHunt.getInstance().getTreasureManager();
        this.repository = this.manager.getTreasureRepository();
        this.treasures = new ArrayList<>(this.manager.getTreasures().values());
    }

    public PagedMenu build(Player viewer) {
        PagedMenu frame = new PagedMenu("Treasures #%page%", 6);

        for (TreasureEntity treasure : this.treasures) {
            ItemStack item = new ItemStack(Material.CHEST);
            ItemMeta meta = item.getItemMeta();
            meta.setDisplayName("§6Treasure: %s".formatted(treasure.getId()));
            meta.setLore(Arrays.asList(
                    "§7Location: " + treasure.getWorld() + " (" + treasure.getX() + ", " + treasure.getY() + ", " + treasure.getZ() + ")",
                    "§7Command: " + treasure.getCommand(),
                    "§eLeft click to teleport",
                    "§cRight click to delete"
            ));
            item.setItemMeta(meta);

            Button button = new Button(item);
            button.getConsumers().put(ClickType.LEFT, e -> {
                World world = Bukkit.getWorld(treasure.getWorld());
                if (world != null)
                    viewer.teleport(new Location(world, treasure.getX(), treasure.getY(), treasure.getZ()));
            });

            button.getConsumers().put(ClickType.RIGHT, e -> {
                manager.runTask(
                        viewer,
                        () -> CompletableFuture
                                .supplyAsync(() -> repository.deleteTreasure(treasure.getId()))
                                .thenAccept(deleted -> {
                                    if (deleted) {
                                        manager.getTreasures().entrySet().removeIf(entry -> entry.getValue().equals(treasure));
                                        viewer.sendMessage("§aTreasure deleted successfully.");
                                    } else
                                        viewer.sendMessage("§cAn error occurred while trying to delete a treasure.");

                                    Bukkit.getScheduler().runTask(TreasureHunt.getInstance(), viewer::closeInventory);
                                })
                                .exceptionally(ex -> {
                                    viewer.closeInventory();
                                    viewer.sendMessage("§cUnexpected error: " + (ex.getMessage() != null ? ex.getMessage() : ex.getClass().getSimpleName()));
                                    viewer.sendMessage("§cSee more details in the console stacktrace.");
                                    ex.printStackTrace();
                                    return null;
                                })
                );
            });
            frame.addElement(button);
        }

        return frame;
    }
}
