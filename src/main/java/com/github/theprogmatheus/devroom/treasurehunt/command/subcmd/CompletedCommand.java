package com.github.theprogmatheus.devroom.treasurehunt.command.subcmd;

import com.github.theprogmatheus.devroom.treasurehunt.TreasureHunt;
import com.github.theprogmatheus.devroom.treasurehunt.TreasureManager;
import com.github.theprogmatheus.devroom.treasurehunt.command.AbstractCommand;
import com.github.theprogmatheus.devroom.treasurehunt.database.entity.TreasureEntity;
import com.github.theprogmatheus.devroom.treasurehunt.gui.menus.TreasureClaimsMenu;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

public class CompletedCommand extends AbstractCommand {


    public CompletedCommand() {
        super("completed", "Returns the list of players who found that treasure", "completed <treasureId>");
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("§cThis command can only executed in-game.");
            return true;
        }
        if (args.length >= 1) {
            String treasureId = args[0];

            CompletableFuture<Void> task = CompletableFuture.supplyAsync(() -> TreasureManager.treasureRepository.existsTreasure(treasureId))
                    .thenCompose(existsTreasure -> {
                        if (!existsTreasure) {
                            sender.sendMessage("§cThere is no treasure registered with the given ID: %s".formatted(treasureId));
                            return CompletableFuture.completedFuture(null);
                        }
                        return CompletableFuture.supplyAsync(() -> TreasureManager.treasureRepository.getClaims(treasureId));
                    }).thenAccept(claims -> {
                        if (claims == null)
                            return;

                        Bukkit.getScheduler().runTask(TreasureHunt.getInstance(),
                                () -> new TreasureClaimsMenu(treasureId, claims).build(player).show(player));
                    }).exceptionally(ex -> {
                        player.sendMessage("§cUnexpected error: " + (ex.getMessage() != null ? ex.getMessage() : ex.getClass().getSimpleName()));
                        player.sendMessage("§cSee more details in the console stacktrace.");
                        ex.printStackTrace();
                        return null;
                    });
            TreasureManager.runTask(player, () -> task);
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
