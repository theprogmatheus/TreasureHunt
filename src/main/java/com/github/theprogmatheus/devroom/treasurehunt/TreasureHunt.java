package com.github.theprogmatheus.devroom.treasurehunt;

import com.github.theprogmatheus.devroom.treasurehunt.command.AbstractCommand;
import com.github.theprogmatheus.devroom.treasurehunt.command.TreasureCommand;
import com.github.theprogmatheus.devroom.treasurehunt.gui.Frame;
import com.github.theprogmatheus.devroom.treasurehunt.listener.TreasureListeners;
import org.bukkit.Bukkit;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.java.JavaPlugin;

public class TreasureHunt extends JavaPlugin {

    private static TreasureHunt instance;

    @Override
    public void onLoad() {
        instance = this;
    }

    @Override
    public void onEnable() {
        TreasureManager.init();
        Frame.init(this);
        this.registerCommand(new TreasureCommand());
        Bukkit.getPluginManager().registerEvents(new TreasureListeners(), this);
    }


    @Override
    public void onDisable() {
        TreasureManager.terminate();
    }

    public static TreasureHunt getInstance() {
        return instance;
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
