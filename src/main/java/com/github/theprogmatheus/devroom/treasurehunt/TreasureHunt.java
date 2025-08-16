package com.github.theprogmatheus.devroom.treasurehunt;

import com.github.theprogmatheus.devroom.treasurehunt.command.AbstractCommand;
import com.github.theprogmatheus.devroom.treasurehunt.command.TreasureCommand;
import com.github.theprogmatheus.devroom.treasurehunt.gui.Menu;
import com.github.theprogmatheus.devroom.treasurehunt.listener.TreasureListeners;
import org.bukkit.Bukkit;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.java.JavaPlugin;

public class TreasureHunt extends JavaPlugin {

    private static TreasureHunt instance;
    private final TreasureManager treasureManager = new TreasureManager();

    @Override
    public void onLoad() {
        instance = this;
    }

    @Override
    public void onEnable() {
        if (treasureManager.init()) {
            Menu.init(this);
            this.registerCommand(new TreasureCommand());
            Bukkit.getPluginManager().registerEvents(new TreasureListeners(), this);
        } else
            setEnabled(false);
    }


    @Override
    public void onDisable() {
        treasureManager.terminate();
    }


    public TreasureManager getTreasureManager() {
        return treasureManager;
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

    public static TreasureHunt getInstance() {
        return instance;
    }
}
