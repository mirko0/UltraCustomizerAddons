package com.github.mirko0.discordio.datatypes.specifications;

import com.github.mirko0.discordio.AddonMain;
import me.TechsCode.UltraCustomizer.Folder;
import me.TechsCode.UltraCustomizer.UltraCustomizer;
import me.TechsCode.UltraCustomizer.base.misc.Callback;
import me.TechsCode.UltraCustomizer.base.translations.Phrase;
import me.TechsCode.UltraCustomizer.scriptSystem.objects.OutcomingVariable;
import me.TechsCode.UltraCustomizer.scriptSystem.objects.ScriptInstance;
import me.TechsCode.UltraCustomizer.scriptSystem.objects.datatypes.DataTypeSpecification;
import net.dv8tion.jda.api.entities.Guild;
import org.bukkit.entity.Player;

public class GuildSpecification extends DataTypeSpecification {
    private static final Phrase GET_PHRASE = Phrase.create("GuildSpecificaiton.title", "Select Discord Server");

    @Override
    public String getName() {
        return "Discord Server";
    }

    @Override
    public String getCreatePhrase() {
        return GET_PHRASE.get();
    }

    private static final Phrase GET_DESCRIPTION = Phrase.create("GuildSpecification.description", "Open Inventory to select discord server.");

    @Override
    public String[] getCreateDescription() {
        return new String[]{
                GET_DESCRIPTION.get()
        };
    }

    private static final Phrase GET_DISPLAY = Phrase.create("DiscordGuild.display", "Discord Server");

    @Override
    public String getDisplay(Object object, OutcomingVariable[] variables) {
        return GET_DISPLAY.get();
    }

    @Override
    public Object getAsValue(Object object, ScriptInstance instance, OutcomingVariable[] variables) {
        return object;
    }

    @Override
    public String serialize(Object object) {
        Guild casted = (Guild) object;
        return casted.getId();
    }

    @Override
    public Object deserialize(String data, Folder folder) {
        return AddonMain.instance.getDiscordBot().getJda().getGuildById(data);
    }


    @Override
    public void open(Player p, UltraCustomizer plugin, String name, String description, OutcomingVariable[] variables, Folder folder, Callback<Object> callback) {
        //TODO: Guild picker????
    }
}
