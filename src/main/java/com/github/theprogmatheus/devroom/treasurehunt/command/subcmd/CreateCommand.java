package com.github.theprogmatheus.devroom.treasurehunt.command.subcmd;

import com.github.theprogmatheus.devroom.treasurehunt.TreasureHunt;
import com.github.theprogmatheus.devroom.treasurehunt.TreasureManager;
import com.github.theprogmatheus.devroom.treasurehunt.command.AbstractCommand;
import com.github.theprogmatheus.devroom.treasurehunt.database.entity.TreasureEntity;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;

public class CreateCommand extends AbstractCommand {

    private final TreasureManager manager;

    public CreateCommand() {
        super("create", "Create a new treasure.", "create <treasureId> <command>");
        this.manager = TreasureHunt.getInstance().getTreasureManager();
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("§cThis command can only executed in-game.");
            return true;
        }

        if (args.length >= 2) {
            String treasureId = args[0];
            StringBuilder treasureCommand = new StringBuilder();
            for (int i = 1; i < args.length; i++) {
                if (i != 1)
                    treasureCommand.append(" ");
                treasureCommand.append(args[i]);
            }

            player.sendMessage("§eStarting treasure creation, please wait...");
            CompletableFuture.supplyAsync(() -> manager.getTreasureRepository().existsTreasure(treasureId))
                    .thenAccept(existsTreasure -> {
                        if (existsTreasure) {
                            player.sendMessage("§cThere is already a treasure registered with this same ID.");
                        } else {
                            manager.getCreatingTreasures().put(player, new TreasureEntity(treasureId, null, 0, 0, 0, treasureCommand.toString(), 0));
                            player.sendMessage("§aThe treasure creation has started, now you need to click on the block that will be the treasure to finish the creation.");
                        }
                    }).exceptionally(ex -> {
                        player.sendMessage("§cUnexpected error: " + (ex.getMessage() != null ? ex.getMessage() : ex.getClass().getSimpleName()));
                        player.sendMessage("§cSee more details in the console stacktrace.");
                        ex.printStackTrace();
                        return null;
                    });
        } else
            sender.sendMessage("§cUsage: %s".formatted(this.usage));
        return true;
    }
}
