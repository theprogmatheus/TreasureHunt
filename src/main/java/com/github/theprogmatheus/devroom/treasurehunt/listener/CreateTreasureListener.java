package com.github.theprogmatheus.devroom.treasurehunt.listener;

import com.github.theprogmatheus.devroom.treasurehunt.TreasureHunt;
import com.github.theprogmatheus.devroom.treasurehunt.command.subcmd.CreateCommand;
import com.github.theprogmatheus.devroom.treasurehunt.database.entity.TreasureEntity;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

public class CreateTreasureListener implements Listener {


    @EventHandler
    public void onClickBlock(PlayerInteractEvent event) {
        Block block = event.getClickedBlock();
        if (block == null || !block.getType().isSolid())
            return;

        Player player = event.getPlayer();

        TreasureEntity treasure = CreateCommand.creatingTreasures.get(player);
        if (treasure == null)
            return;

        treasure.setWorld(block.getWorld().getName());
        treasure.setX(block.getX());
        treasure.setY(block.getY());
        treasure.setZ(block.getZ());

        event.setCancelled(true);

        if (createTreasure(treasure)) {
            player.sendMessage("§aTreasure created successfully.");
        } else {
            player.sendMessage("§cAn error occurred while trying to create the treasure. Please try again.");
        }

        CreateCommand.creatingTreasures.remove(player);
    }


    private boolean createTreasure(TreasureEntity treasure) {
        try {
            return TreasureHunt.getInstance()
                    .getTreasureRepository()
                    .createTreasure(
                            treasure.getId(),
                            treasure.getWorld(),
                            treasure.getX(),
                            treasure.getY(),
                            treasure.getZ(),
                            treasure.getCommand()
                    );
        } catch (Exception exception) {
            exception.printStackTrace();
            return false;
        }
    }


}
