package com.github.theprogmatheus.devroom.treasurehunt;

import com.github.theprogmatheus.devroom.treasurehunt.database.DatabaseManager;
import org.bukkit.plugin.java.JavaPlugin;

public class TreasureHunt extends JavaPlugin {

    private DatabaseManager databaseManager;

    @Override
    public void onLoad() {
        this.databaseManager = new DatabaseManager("localhost", "minecraft", "root", "root");
    }

    @Override
    public void onEnable() {
        this.databaseManager.init();
    }


    @Override
    public void onDisable() {
        this.databaseManager.terminate();
    }


}
