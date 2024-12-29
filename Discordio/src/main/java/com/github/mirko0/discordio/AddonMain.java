package com.github.mirko0.discordio;

import com.github.mirko0.discordio.datatypes.ReferenceManager;
import com.github.mirko0.discordio.dbot.BotMain;
import com.github.mirko0.discordio.events.EventRegistry;
import lombok.Getter;
import me.TechsCode.UltraCustomizer.UltraCustomizer;
import me.TechsCode.UltraCustomizer.base.gui.Button;
import me.TechsCode.UltraCustomizer.base.gui.Entry;
import me.TechsCode.UltraCustomizer.base.item.XMaterial;
import me.TechsCode.UltraCustomizer.gui.Overview;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.Listener;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Collection;

/**
 * Used as a main class of addons.
 * For this to work you must call the instance somewhere in elements constructor does not matter what element you call it in.
 */
@Getter
public class AddonMain implements Listener {

    public static final AddonMain instance = new AddonMain("Discordio", "mirko0");
    private final String name, author;

    public static void log(String message) {
        UltraCustomizer.getInstance().log("|Discordio|: " + message);
    }

    public AddonMain(String name, String author) {
        this.name = name;
        this.author = author;
        try {
            onLoad();
            UltraCustomizer.getInstance().log(name + " onLoad executed. " + "Author: " + author);
        } catch (Exception e) {
            UltraCustomizer.getInstance().log(name + " onLoad failed please contact author. " + "Author: " + author);
            e.printStackTrace();
        }
    }

    private BotSettings settings;
    private EventRegistry eventRegistry;
    private BotMain discordBot;
    private YamlConfiguration configFile;
    private final File configFileO = new File("plugins/UltraCustomizer/discordio.yml");
    private final ReferenceManager referenceManager = new ReferenceManager();


    private void onLoad() {
        setupSettingsFile();
        Bukkit.getScheduler().runTaskAsynchronously(UltraCustomizer.getInstance().getBootstrap(), () -> {
            this.discordBot = new BotMain(settings);
        });
        eventRegistry = new EventRegistry();
        eventRegistry.registerEvents(instance);

        try {
            addButtonToOverview();
        } catch (NoSuchFieldException | IllegalAccessException e) {
            log("Error happened while adding button to overview");
        }
    }

    private void setupSettingsFile() {
        try {
            if (!configFileO.exists()) {
                configFileO.createNewFile();
            }
            YamlConfiguration config = YamlConfiguration.loadConfiguration(configFileO);
            String token = config.get("token", "NOT_SET").toString();
            String onlineStatus = config.get("onlineStatus", "ONLINE").toString();
            config.save(configFileO);
            this.configFile = config;
            settings = new BotSettings(token, OnlineStatus.valueOf(onlineStatus), getActivityFromConfig());
        } catch (IOException e) {
            UltraCustomizer.getInstance().log("Error while loading settings file");
            e.printStackTrace();
        }
    }

    public Activity getActivityFromConfig() {
        String activityText = configFile.get("activityText", "Hi!").toString();
        String activityType = configFile.get("activityType", "CUSTOM_STATUS").toString();
        String activityUrl = configFile.get("activityUrl", "NOT_SET").toString();
        return Activity.of(Activity.ActivityType.valueOf(activityType), activityText, activityUrl.equals("NOT_SET") ? null : activityUrl);
    }

    private void addButtonToOverview() throws NoSuchFieldException, IllegalAccessException {
        Class<?> clazz = Overview.class;

        // Locate the private static final field
        Field field = clazz.getDeclaredField("additionalButtons");

        // Make the field accessible
        field.setAccessible(true);

        // Get the value of the field (should be a Collection<Entry>)
        Collection<Entry> additionalButtons = (Collection<Entry>) field.get(null); // null because it's static

        // Add a new Entry to the collection
        additionalButtons.add(new Entry() {
            @Override
            public void button(Button button) {
                button.material(XMaterial.BEACON)
                        .name("§9Discordio Settings")
                        .lore("§7" + "Click to manage Discordio!")
                        .addEnchantment(Enchantment.SILK_TOUCH, 1)
                        .showEnchantments(false)
                ;
            }
        });
    }
}
