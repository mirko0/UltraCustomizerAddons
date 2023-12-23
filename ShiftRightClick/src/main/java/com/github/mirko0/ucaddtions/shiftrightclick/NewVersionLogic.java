package com.github.mirko0.ucaddtions.shiftrightclick;

import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class NewVersionLogic implements RightClickLogic {
    @Override
    public boolean getHand(PlayerInteractEvent event) {
        Method method = null;
        try {
            //check if its fakeevent from insaneshops and if it is get the superclass, because fakeevent does not have getHand method.
            if (event.getClass().getName().toLowerCase().contains("fakeevent")) {
                method = event.getClass().getSuperclass().getDeclaredMethod("getHand");
            }else {
                method = event.getClass().getDeclaredMethod("getHand");
            }
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
            return true;
        }
        try {
            EquipmentSlot hand = (EquipmentSlot) method.invoke(event);
            //Bukkit.getLogger().warning("TESTHandLogic: " + object.toString());
            return hand != EquipmentSlot.HAND;
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public ItemStack getItemInMainHand(Player player) {
        Method method = null;
        try {
            method = player.getInventory().getClass().getDeclaredMethod("getItemInMainHand");
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
            return null;
        }
        method.setAccessible(true);
        try {
            Object object = method.invoke(player.getInventory());
            //Bukkit.getLogger().warning("TESTItemLogic: " + object.toString());
            return (ItemStack) object;
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
            return null;
        }

    }
}
