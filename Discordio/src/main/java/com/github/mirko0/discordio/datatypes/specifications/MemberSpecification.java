package com.github.mirko0.discordio.datatypes.specifications;

import com.github.mirko0.discordio.AddonMain;
import me.TechsCode.UltraCustomizer.Folder;
import me.TechsCode.UltraCustomizer.UltraCustomizer;
import me.TechsCode.UltraCustomizer.base.misc.Callback;
import me.TechsCode.UltraCustomizer.base.translations.Phrase;
import me.TechsCode.UltraCustomizer.scriptSystem.objects.OutcomingVariable;
import me.TechsCode.UltraCustomizer.scriptSystem.objects.ScriptInstance;
import me.TechsCode.UltraCustomizer.scriptSystem.objects.datatypes.DataTypeSpecification;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;
import org.bukkit.entity.Player;

public class MemberSpecification extends DataTypeSpecification {
    private static final String PREFIX = "MemberSpecification";

    private static final Phrase GET_PHRASE = Phrase.create(PREFIX + ".title", "Select Server Member");

    @Override
    public String getName() {
        return "Member";
    }

    @Override
    public String getCreatePhrase() {
        return GET_PHRASE.get();
    }

    private static final Phrase GET_DESCRIPTION = Phrase.create(PREFIX + ".description", "Open Inventory to select discord server member");

    @Override
    public String[] getCreateDescription() {
        return new String[]{
                GET_DESCRIPTION.get()
        };
    }

    private static final Phrase GET_DISPLAY = Phrase.create(PREFIX + ".display", "Server Member");

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
        Member discordUser = (Member) object;
        return AddonMain.instance.getReferenceManager().addReference(discordUser);
    }

    @Override
    public Object deserialize(String data, Folder folder) {
        return AddonMain.instance.getReferenceManager().getReference(data);
    }


    @Override
    public void open(Player p, UltraCustomizer plugin, String name, String description, OutcomingVariable[] variables, Folder folder, Callback<Object> callback) {
        //TODO: Member picker????
    }
}
