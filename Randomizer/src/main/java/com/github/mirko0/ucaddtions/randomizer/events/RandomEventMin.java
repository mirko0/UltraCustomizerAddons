package com.github.mirko0.ucaddtions.randomizer.events;

import me.TechsCode.UltraCustomizer.UltraCustomizer;
import me.TechsCode.UltraCustomizer.base.item.XMaterial;
import me.TechsCode.UltraCustomizer.base.legacy.task.UpdateEvent;
import me.TechsCode.UltraCustomizer.base.legacy.task.UpdateTime;
import me.TechsCode.UltraCustomizer.scriptSystem.objects.*;
import org.bukkit.event.EventHandler;

import java.util.Random;

public class RandomEventMin extends Constructor {
    public RandomEventMin(UltraCustomizer ultraCustomizer) {
        super(ultraCustomizer);
    }

    @Override
    public String getName() {
        return "Random Event (Minute)";
    }

    @Override
    public String getInternalName() {
        return "randomizer-event-min";
    }

    @Override
    public XMaterial getMaterial() {
        return XMaterial.CHEST;
    }

    @Override
    public String[] getDescription() {
        return new String[]{
                "Will have 50% chance of firing every minute.",
        };
    }

    @Override
    public boolean isUnlisted() {
        return false;
    }


    @Override
    public Argument[] getArguments(ElementInfo elementInfo) {
        return new Argument[0];
    }

    @Override
    public OutcomingVariable[] getOutcomingVariables(ElementInfo elementInfo) {
        return new OutcomingVariable[0];
    }

    @EventHandler
    public void onTick(final UpdateEvent event) {
        int randomNumber = new Random().nextInt(2);
        boolean execute = randomNumber == 0;
        if (!execute) return;

        if (event.getUpdateTime().equals(UpdateTime.MIN)) {
            call(elementInfo -> new ScriptInstance());
        }
    }
}
