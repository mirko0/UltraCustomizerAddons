package com.github.mirko0.vplus.specifications;

import me.TechsCode.UltraCustomizer.Folder;
import me.TechsCode.UltraCustomizer.UltraCustomizer;
import me.TechsCode.UltraCustomizer.base.misc.Callback;
import me.TechsCode.UltraCustomizer.base.translations.Phrase;
import me.TechsCode.UltraCustomizer.scriptSystem.objects.OutcomingVariable;
import me.TechsCode.UltraCustomizer.scriptSystem.objects.ScriptInstance;
import me.TechsCode.UltraCustomizer.scriptSystem.objects.datatypes.DataTypeSpecification;
import org.bukkit.entity.Player;

public class TestSpecification extends DataTypeSpecification {
    private static final Phrase GET_PHRASE = Phrase.create("TestSpecification.title", "Select Test");

    @Override
    public String getCreatePhrase() {
        return GET_PHRASE.get();
    }

    private static final Phrase GET_DESCRIPTION = Phrase.create("TestSpecificaiton.description", "Open Inventory to select test");

    @Override
    public String[] getCreateDescription() {
        return new String[]{
                GET_DESCRIPTION.get()
        };
    }

    private static final Phrase GET_DISPLAY = Phrase.create("TestSpecificaiton.selectedItem", "Select Test");

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
        return ((TestO) object).serialize();
    }

    @Override
    public Object deserialize(String data, Folder folder) {
        return TestO.deserialize(data);
    }


    @Override
    public void open(Player p, UltraCustomizer plugin, String name, String description, OutcomingVariable[] variables, Folder folder, Callback<Object> callback) {

    }
}
