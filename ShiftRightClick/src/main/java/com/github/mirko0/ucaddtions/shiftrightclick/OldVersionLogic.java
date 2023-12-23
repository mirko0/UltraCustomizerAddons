package com.github.mirko0.ucaddtions.shiftrightclick;

import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class OldVersionLogic implements RightClickLogic {

    @Override
    public boolean getHand(PlayerInteractEvent event) {
        return false;
    }

    @Override
    public ItemStack getItemInMainHand(Player player) {
        return player.getInventory().getItemInHand();
    }
}
