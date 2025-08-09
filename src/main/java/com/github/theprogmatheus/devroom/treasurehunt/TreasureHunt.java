package com.github.theprogmatheus.devroom.treasurehunt;

import com.github.theprogmatheus.devroom.treasurehunt.command.AbstractCommand;
import com.github.theprogmatheus.devroom.treasurehunt.command.TreasureCommand;
import com.github.theprogmatheus.devroom.treasurehunt.database.DatabaseManager;
import com.github.theprogmatheus.devroom.treasurehunt.database.repository.TreasureRepository;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.java.JavaPlugin;

public class TreasureHunt extends JavaPlugin {

    private static TreasureHunt instance;

    private DatabaseManager databaseManager;
    private TreasureRepository treasureRepository;

    @Override
    public void onLoad() {
        instance = this;
        this.databaseManager = new DatabaseManager(
                "localhost",
                "minecraft",
                "root",
                "root",
                "treasure_hunt_"
        );
    }

    @Override
    public void onEnable() {
        this.databaseManager.init();
        this.treasureRepository = new TreasureRepository(this.databaseManager);
        this.treasureRepository.initTables();
        this.registerCommand(new TreasureCommand());
    }


    @Override
    public void onDisable() {
        this.databaseManager.terminate();
    }

    public static TreasureHunt getInstance() {
        return instance;
    }

    public DatabaseManager getDatabaseManager() {
        return databaseManager;
    }

    public TreasureRepository getTreasureRepository() {
        return treasureRepository;
    }

    private void registerCommand(AbstractCommand command) {
        PluginCommand pluginCommand = getCommand(command.name);
        if (pluginCommand != null) {
            pluginCommand.setExecutor(command);
            pluginCommand.setTabCompleter(command);
            pluginCommand.setDescription(command.description);
            pluginCommand.setUsage(command.usage);
            pluginCommand.setPermission("treasurehunt.cmd.%s".formatted(command.name));
        }
    }

}
