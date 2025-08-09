package com.github.theprogmatheus.devroom.treasurehunt.listener;

import com.github.theprogmatheus.devroom.treasurehunt.command.subcmd.CreateCommand;
import com.github.theprogmatheus.devroom.treasurehunt.database.entity.TreasureEntity;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import static com.github.theprogmatheus.devroom.treasurehunt.TreasureManager.*;

public class TreasureListeners implements Listener {

    @EventHandler
    public void onClickBlock(PlayerInteractEvent event) {
        Block block = event.getClickedBlock();
        if (block == null || !block.getType().isSolid())
            return;

        Player player = event.getPlayer();

        if (CreateCommand.creatingTreasures.containsKey(player)) {
            event.setCancelled(true);

            TreasureEntity placeholder = CreateCommand.creatingTreasures.get(player);

            placeholder.setWorld(block.getWorld().getName());
            placeholder.setX(block.getX());
            placeholder.setY(block.getY());
            placeholder.setZ(block.getZ());

            runTask(player, () -> createTreasure(player, placeholder));
        } else if (isTreasure(block)) {
            event.setCancelled(true);
            runTask(player, () -> claimTreasure(player, block));
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        CreateCommand.creatingTreasures.remove(event.getPlayer());
    }
}
