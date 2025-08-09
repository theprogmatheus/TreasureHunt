package com.github.theprogmatheus.devroom.treasurehunt.command;

import com.github.theprogmatheus.devroom.treasurehunt.command.subcmd.CompletedCommand;
import com.github.theprogmatheus.devroom.treasurehunt.command.subcmd.CreateCommand;
import com.github.theprogmatheus.devroom.treasurehunt.command.subcmd.DeleteCommand;
import com.github.theprogmatheus.devroom.treasurehunt.command.subcmd.ListCommand;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class TreasureCommand extends AbstractCommand {

    private final Map<String, AbstractCommand> subCommands;

    public TreasureCommand() {
        super("treasure", "Run the treasure command for more info.", "treasure <subcommand>");

        this.subCommands = new HashMap<>();
        this.subCommands.put("create", new CreateCommand());
        this.subCommands.put("delete", new DeleteCommand());
        this.subCommands.put("completed", new CompletedCommand());
        this.subCommands.put("list", new ListCommand());
    }


    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length > 0) {
            AbstractCommand subCommand = this.subCommands.get(args[0].toLowerCase());
            if (subCommand != null)
                return subCommand.onCommand(sender, command, label, args);
        }

        for (AbstractCommand subCommand : this.subCommands.values()) {
            sender.sendMessage("§a/%s %s §8- §7%s".formatted(this.name, subCommand.usage, subCommand.description));
        }

        return false;
    }

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length >= 1) {
            if (args.length == 1) {
                return this.subCommands.values()
                        .stream()
                        .filter(subCommand -> subCommand.name.startsWith(args[0].toLowerCase()))
                        .map(subCommand -> subCommand.name)
                        .collect(Collectors.toList());
            }

            AbstractCommand subCommand = this.subCommands.get(args[0].toLowerCase());
            if (subCommand != null)
                return subCommand.onTabComplete(sender, command, label, args);
        }
        return List.of();
    }

}
