package com.github.theprogmatheus.devroom.treasurehunt;

import com.github.theprogmatheus.devroom.treasurehunt.database.DatabaseManager;
import com.github.theprogmatheus.devroom.treasurehunt.database.repository.TreasureRepository;
import org.bukkit.plugin.java.JavaPlugin;

public class TreasureHunt extends JavaPlugin {

    private DatabaseManager databaseManager;
    private TreasureRepository treasureRepository;

    @Override
    public void onLoad() {
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
    }


    @Override
    public void onDisable() {
        this.databaseManager.terminate();
    }


}
