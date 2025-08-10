package com.github.theprogmatheus.devroom.treasurehunt.gui.menus;

import com.github.theprogmatheus.devroom.treasurehunt.database.entity.TreasureClaimEntity;
import com.github.theprogmatheus.devroom.treasurehunt.gui.Button;
import com.github.theprogmatheus.devroom.treasurehunt.gui.PagedFrame;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class TreasureClaimsMenu {

    private final String treasure;
    private final List<TreasureClaimEntity> claims;

    public TreasureClaimsMenu(String treasure, List<TreasureClaimEntity> claims) {
        this.treasure = treasure;
        this.claims = claims;
    }

    public PagedFrame build(Player viewer) {
        PagedFrame frame = new PagedFrame("Claims for " + treasure, 6);

        for (TreasureClaimEntity claim : claims) {
            OfflinePlayer player = Bukkit.getOfflinePlayer(claim.getPlayerId());

            ItemStack skull = new ItemStack(Material.PLAYER_HEAD);
            SkullMeta skullMeta = (SkullMeta) skull.getItemMeta();
            skullMeta.setOwningPlayer(player);
            skullMeta.setDisplayName("§b" + player.getName());
            skullMeta.setLore(Arrays.asList(
                    "§7Claimed at: " + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(claim.getClaimedAt()))
            ));
            skull.setItemMeta(skullMeta);
            frame.addElement(new Button(skull));
        }
        return frame;
    }
}
