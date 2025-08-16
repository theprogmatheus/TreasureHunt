package com.github.theprogmatheus.devroom.treasurehunt.listener;

import com.github.theprogmatheus.devroom.treasurehunt.TreasureHunt;
import com.github.theprogmatheus.devroom.treasurehunt.TreasureManager;
import com.github.theprogmatheus.devroom.treasurehunt.database.entity.TreasureEntity;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.EquipmentSlot;

public class TreasureListeners implements Listener {

    private final TreasureManager manager;

    public TreasureListeners() {
        this.manager = TreasureHunt.getInstance().getTreasureManager();
    }


    @EventHandler
    public void onClickBlock(PlayerInteractEvent event) {
        if (event.getHand() != EquipmentSlot.HAND)
            return;

        Block block = event.getClickedBlock();
        if (block == null || !block.getType().isSolid())
            return;

        Player player = event.getPlayer();

        if (manager.getCreatingTreasures().containsKey(player)) {
            event.setCancelled(true);

            TreasureEntity placeholder = manager.getCreatingTreasures().get(player);

            placeholder.setWorld(block.getWorld().getName());
            placeholder.setX(block.getX());
            placeholder.setY(block.getY());
            placeholder.setZ(block.getZ());

            manager.runTask(player, () -> manager.createTreasure(player, placeholder));
        } else if (manager.isTreasure(block)) {
            event.setCancelled(true);
            manager.runTask(player, () -> manager.claimTreasure(player, block));
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        manager.getCreatingTreasures().remove(event.getPlayer());
    }
}
