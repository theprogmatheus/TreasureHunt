package com.github.theprogmatheus.devroom.treasurehunt;

import com.github.theprogmatheus.devroom.treasurehunt.database.DatabaseManager;
import com.github.theprogmatheus.devroom.treasurehunt.database.entity.TreasureEntity;
import com.github.theprogmatheus.devroom.treasurehunt.database.repository.TreasureRepository;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;

public class TreasureManager {


    private final Map<String, CompletableFuture<?>> tasksInProgress = new ConcurrentHashMap<>();
    private final Map<Player, TreasureEntity> creatingTreasures = new HashMap<>();

    private final Map<Block, TreasureEntity> treasures = new ConcurrentHashMap<>();
    private DatabaseManager databaseManager;
    private TreasureRepository treasureRepository;

    public boolean init() {
        File configFile = new File(TreasureHunt.getInstance().getDataFolder(), "config.yml");
        if (!configFile.exists()) {
            TreasureHunt.getInstance().saveResource("config.yml", false);
            TreasureHunt.getInstance().getLogger().warning("Config newly created, you need to configure mysql in \"config.yml\" configuration file to use your plugin for the first time.");
            return false;
        }

        FileConfiguration config = TreasureHunt.getInstance().getConfig();
        databaseManager = new DatabaseManager(
                config.getBoolean("mysql.debug"),
                config.getString("mysql.hostname"),
                config.getString("mysql.database"),
                config.getString("mysql.username"),
                config.getString("mysql.password"),
                config.getString("mysql.table_prefix")
        );
        if (!databaseManager.init())
            return false;

        treasureRepository = new TreasureRepository(databaseManager);
        treasureRepository.initTables();


        treasureRepository.getTreasures().forEach(treasure -> {
            World world = Bukkit.getWorld(treasure.getWorld());
            if (world == null) return;
            treasures.put(world.getBlockAt(treasure.getX(), treasure.getY(), treasure.getZ()), treasure);
        });

        return true;
    }

    public void terminate() {
        if (databaseManager != null)
            databaseManager.terminate();
    }

    public boolean isTreasure(Block block) {
        return treasures.containsKey(block);
    }

    public void runTask(CommandSender sender, Supplier<CompletableFuture<Void>> taskSupplier) {
        String id = sender instanceof Player player ? player.getUniqueId().toString() : sender.getName();
        if (tasksInProgress.containsKey(id)) {
            sender.sendMessage("§cYou already have an action in progress. Please wait...");
            return;
        }
        var task = taskSupplier.get();
        task = task
                .whenComplete((res, ex) -> tasksInProgress.remove(id));

        tasksInProgress.put(id, task);
    }


    public CompletableFuture<Void> claimTreasure(Player player, Block block) {
        TreasureEntity treasure = treasures.get(block);

        var future = CompletableFuture.supplyAsync(() -> treasureRepository.hasClaimed(treasure.getId(), player.getUniqueId()))
                .thenCompose(hasClaimed -> {
                    if (hasClaimed) {
                        player.sendMessage("§eYou've already found the treasure, congratulations :)");
                        return CompletableFuture.completedFuture(null);
                    }
                    return CompletableFuture.supplyAsync(() -> treasureRepository.claim(treasure.getId(), player.getUniqueId()));
                })
                .thenAccept(claimed -> {
                    if (claimed == null) return;

                    if (claimed) {
                        player.sendMessage("§aCongratulations, you found the treasure :)");
                        Bukkit.getScheduler().runTask(TreasureHunt.getInstance(),
                                () -> Bukkit.dispatchCommand(Bukkit.getConsoleSender(), treasure.getCommand().replace("%player%", player.getName())));
                    } else {
                        player.sendMessage("§cAn error occurred while trying to claim the treasure.");
                    }
                })
                .exceptionally(ex -> {
                    player.sendMessage("§cUnexpected error: %s".formatted(ex.getMessage() != null ? ex.getMessage() : ex.getClass().getSimpleName()));
                    player.sendMessage("§cSee more details in the console stacktrace.");
                    ex.printStackTrace();
                    return null;
                });
        return future;
    }

    public CompletableFuture<Void> createTreasure(Player player, TreasureEntity treasure) {

        player.sendMessage("§eCreating treasure for this block, please wait...");
        return CompletableFuture.supplyAsync(() -> treasureRepository.getTreasureByPosition(treasure.getWorld(), treasure.getX(), treasure.getY(), treasure.getZ()) != null)
                .thenCompose(existsTreasureInThisBlock -> {
                    if (existsTreasureInThisBlock) {
                        player.sendMessage("§cThere is already a treasure registered in this block, try another block.");
                        return CompletableFuture.completedFuture(null);
                    }

                    creatingTreasures.remove(player);
                    return CompletableFuture.supplyAsync(() -> treasureRepository.createTreasure(
                            treasure.getId(),
                            treasure.getWorld(),
                            treasure.getX(),
                            treasure.getY(),
                            treasure.getZ(),
                            treasure.getCommand())
                    );
                })
                .thenAccept(created -> {
                    if (created == null)
                        return;

                    if (created) {
                        Bukkit.getScheduler().runTask(TreasureHunt.getInstance(), () -> {
                            treasure.setCreatedAt(System.currentTimeMillis());
                            Block block = player.getWorld().getBlockAt(treasure.getX(), treasure.getY(), treasure.getZ());
                            treasures.put(block, treasure);
                        });
                        player.sendMessage("§aTreasure created successfully.");
                    } else {
                        player.sendMessage("§cAn error occurred while trying to create the treasure. Please try again.");
                    }

                })
                .exceptionally(ex -> {
                    player.sendMessage("§cUnexpected error: %s".formatted(ex.getMessage() != null ? ex.getMessage() : ex.getClass().getSimpleName()));
                    player.sendMessage("§cSee more details in the console stacktrace.");
                    ex.printStackTrace();
                    return null;
                });
    }

    public Map<String, CompletableFuture<?>> getTasksInProgress() {
        return tasksInProgress;
    }

    public Map<Player, TreasureEntity> getCreatingTreasures() {
        return creatingTreasures;
    }

    public Map<Block, TreasureEntity> getTreasures() {
        return treasures;
    }

    public DatabaseManager getDatabaseManager() {
        return databaseManager;
    }

    public TreasureRepository getTreasureRepository() {
        return treasureRepository;
    }
}
