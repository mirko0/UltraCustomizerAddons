package com.github.mirko0.discordio.events;

import com.github.mirko0.discordio.AddonMain;
import com.github.mirko0.discordio.customizer.guis.SettingsGui;
import lombok.AllArgsConstructor;
import me.TechsCode.UltraCustomizer.UltraCustomizer;
import me.TechsCode.UltraCustomizer.gui.Overview;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

@AllArgsConstructor
public class InventoryEvents implements Listener {

    private AddonMain instance;

    @EventHandler(priority = EventPriority.LOWEST)
    public void onItemClick(InventoryClickEvent event) {
        ItemStack item = event.getCurrentItem();
        if (item == null) return;
        ItemMeta meta = item.getItemMeta();
        if (meta == null) return;
        if (!meta.hasDisplayName()) return;
        if (meta.getDisplayName().contains("Discordio Settings")) {
            new SettingsGui((Player) event.getWhoClicked(), UltraCustomizer.getInstance()) {
                @Override
                public void onBack() {
                    new Overview(((Player) event.getWhoClicked()), UltraCustomizer.getInstance());
                }
            };
        }
    }
}
