package com.github.mirko0.ucaddtions.shiftrightclick;

import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public interface RightClickLogic {

    boolean getHand(PlayerInteractEvent event);

    ItemStack getItemInMainHand(Player player);
}
