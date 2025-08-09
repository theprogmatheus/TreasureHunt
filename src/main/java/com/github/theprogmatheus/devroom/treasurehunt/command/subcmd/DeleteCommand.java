package com.github.theprogmatheus.devroom.treasurehunt.command.subcmd;

import com.github.theprogmatheus.devroom.treasurehunt.TreasureManager;
import com.github.theprogmatheus.devroom.treasurehunt.command.AbstractCommand;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;

public class DeleteCommand extends AbstractCommand {

    public DeleteCommand() {
        super("delete", "Delete an treasure.", "delete <treasureId>");
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length > 0) {
            String treasureId = args[0];

            sender.sendMessage("§eTrying to delete treasure %s, please wait...".formatted(treasureId));
            CompletableFuture.supplyAsync(() -> TreasureManager.treasureRepository.existsTreasure(treasureId))
                    .thenCompose(existsTreasure -> {
                        if (!existsTreasure) {
                            sender.sendMessage("§cThere is no treasure registered with the given ID: %s".formatted(treasureId));
                            return CompletableFuture.completedFuture(null);
                        }
                        return CompletableFuture.supplyAsync(() -> TreasureManager.treasureRepository.deleteTreasure(treasureId));
                    }).thenAccept(deleted -> {
                        if (deleted == null)
                            return;
                        if (deleted) {
                            TreasureManager.treasures.entrySet().removeIf(entry -> entry.getValue().getId().equals(treasureId));
                            sender.sendMessage("§aTreasure deleted successfully.");
                        } else
                            sender.sendMessage("§cAn error occurred while trying to delete a treasure.");
                    }).exceptionally(ex -> {
                        sender.sendMessage("§cUnexpected error: " + (ex.getMessage() != null ? ex.getMessage() : ex.getClass().getSimpleName()));
                        if (sender instanceof Player player)
                            player.sendMessage("§cSee more details in the console stacktrace.");
                        ex.printStackTrace();
                        return null;
                    });
        } else
            sender.sendMessage("§cUsage: %s".formatted(this.usage));
        return true;
    }
}
