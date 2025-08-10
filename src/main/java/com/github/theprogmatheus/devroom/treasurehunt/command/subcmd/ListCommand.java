package com.github.theprogmatheus.devroom.treasurehunt.command.subcmd;

import com.github.theprogmatheus.devroom.treasurehunt.command.AbstractCommand;
import com.github.theprogmatheus.devroom.treasurehunt.gui.menus.TreasureListMenu;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class ListCommand extends AbstractCommand {

    public ListCommand() {
        super("list", "Open a gui menu with all treasures created.", "list");
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("§cThis command can only executed in-game.");
            return true;
        }
        new TreasureListMenu().build(player).show(player);
        return true;
    }


}
