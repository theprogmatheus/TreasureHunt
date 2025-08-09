package com.github.theprogmatheus.devroom.treasurehunt.listener;

import com.github.theprogmatheus.devroom.treasurehunt.TreasureHunt;
import com.github.theprogmatheus.devroom.treasurehunt.command.subcmd.CreateCommand;
import com.github.theprogmatheus.devroom.treasurehunt.database.entity.TreasureEntity;
import com.github.theprogmatheus.devroom.treasurehunt.database.repository.TreasureRepository;
import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

public class TreasureListeners implements Listener {


    @EventHandler
    public void onClickBlock(PlayerInteractEvent event) {
        Block block = event.getClickedBlock();
        if (block == null || !block.getType().isSolid())
            return;

        Player player = event.getPlayer();

        String world = block.getWorld().getName();
        int x = block.getX();
        int y = block.getY();
        int z = block.getZ();

        TreasureRepository repo = TreasureHunt.getInstance().getTreasureRepository();

        TreasureEntity treasure = CreateCommand.creatingTreasures.get(player);
        if (treasure != null) {
            event.setCancelled(true);
            if (repo.createTreasure(treasure.getId(), world, x, y, z, treasure.getCommand())) {
                player.sendMessage("§aTreasure created successfully.");
            } else {
                player.sendMessage("§cAn error occurred while trying to create the treasure. Please try again.");
            }
            CreateCommand.creatingTreasures.remove(player);
        } else {
            treasure = repo.getTreasureByPosition(world, x, y, z);
            if (treasure == null)
                return;

            event.setCancelled(true);
            if (!repo.hasClaimed(treasure.getId(), player.getUniqueId())) {
                if (repo.claim(treasure.getId(), player.getUniqueId())) {
                    player.sendMessage("§aCongratulations, you found the treasure :)");
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), treasure.getCommand().replace("%player%", player.getName()));
                } else
                    player.sendMessage("§cAn error occurred while trying to claim the treasure.");
            } else {
                player.sendMessage("§eYou've already found the treasure, congratulations :)");
            }
        }
    }


}
