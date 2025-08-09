package com.github.theprogmatheus.devroom.treasurehunt.command.subcmd;

import com.github.theprogmatheus.devroom.treasurehunt.TreasureHunt;
import com.github.theprogmatheus.devroom.treasurehunt.command.AbstractCommand;
import com.github.theprogmatheus.devroom.treasurehunt.database.repository.TreasureRepository;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class DeleteCommand extends AbstractCommand {

    public DeleteCommand() {
        super("delete", "Delete an treasure.", "delete <treasureId>");
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length > 0) {
            TreasureRepository repo = TreasureHunt.getInstance().getTreasureRepository();
            String treasureId = args[0];
            if (repo.existsTreasure(treasureId)) {
                if (repo.deleteTreasure(treasureId))
                    sender.sendMessage("§aTreasure deleted successfully.");
                else
                    sender.sendMessage("§cAn error occurred while trying to delete a treasure.");
            } else
                sender.sendMessage("§cThere is no treasure registered with the given ID: %s".formatted(treasureId));
        } else
            sender.sendMessage("§cUsage: %s".formatted(this.usage));
        return true;
    }
}
