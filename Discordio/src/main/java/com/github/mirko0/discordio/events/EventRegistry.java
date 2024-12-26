package com.github.mirko0.discordio.events;

import com.github.mirko0.discordio.AddonMain;
import me.TechsCode.UltraCustomizer.UltraCustomizer;
import org.bukkit.Bukkit;

public class EventRegistry {

    public void registerEvents(AddonMain instance) {
        Bukkit.getServer().getPluginManager().registerEvents(new InventoryEvents(instance), UltraCustomizer.getInstance().getBootstrap());
    }

}
