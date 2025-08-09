package com.github.theprogmatheus.devroom.treasurehunt.command.subcmd;

import com.github.theprogmatheus.devroom.treasurehunt.TreasureHunt;
import com.github.theprogmatheus.devroom.treasurehunt.command.AbstractCommand;
import com.github.theprogmatheus.devroom.treasurehunt.database.entity.TreasureEntity;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class CreateCommand extends AbstractCommand {

    public static Map<Player, TreasureEntity> creatingTreasures = new HashMap<>();

    public CreateCommand() {
        super("create", "Create a new treasure.", "create <treasureId> <command>");
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("§cThis command can only executed in-game.");
            return true;
        }

        if (args.length >= 2) {
            String treasureId = args[0];

            if (TreasureHunt.getInstance().getTreasureRepository().existsTreasure(treasureId)) {
                player.sendMessage("§cThere is already a treasure registered with this same ID.");
                return true;
            }

            StringBuilder treasureCommand = new StringBuilder();
            for (int i = 1; i < args.length; i++) {
                if (i != 1)
                    treasureCommand.append(" ");
                treasureCommand.append(args[i]);
            }

            creatingTreasures.put(player, new TreasureEntity(treasureId, null, 0, 0, 0, treasureCommand.toString(), 0));
            player.sendMessage("§eThe treasure creation has started, now you need to click on the block that will be the treasure to finish the creation.");
        } else
            sender.sendMessage("§cUsage: %s".formatted(this.usage));
        return true;
    }
}
