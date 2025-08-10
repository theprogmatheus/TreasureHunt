package com.github.theprogmatheus.devroom.treasurehunt.command.subcmd;

import com.github.theprogmatheus.devroom.treasurehunt.TreasureManager;
import com.github.theprogmatheus.devroom.treasurehunt.command.AbstractCommand;
import com.github.theprogmatheus.devroom.treasurehunt.database.entity.TreasureEntity;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

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

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length == 1) {
            return TreasureManager.treasures.values()
                    .stream()
                    .map(TreasureEntity::getId)
                    .filter(id -> id.startsWith(args[0]))
                    .collect(Collectors.toList());
        }
        return List.of();
    }
}
