package com.github.mirko0.ucaddtions.weatherevents;


import me.TechsCode.UltraCustomizer.UltraCustomizer;
import me.TechsCode.UltraCustomizer.base.item.XMaterial;
import me.TechsCode.UltraCustomizer.scriptSystem.objects.*;
import me.TechsCode.UltraCustomizer.scriptSystem.objects.datatypes.DataType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.weather.LightningStrikeEvent;

public class OnLightningStrike extends Constructor {
    public OnLightningStrike(UltraCustomizer ultraCustomizer) {
        super(ultraCustomizer);
    }

    @Override
    public String getName() {
        return "Lightning Strike (Event)";
    }

    @Override
    public String getInternalName() {
        return "lightning-strike-event";
    }

    @Override
    public XMaterial getMaterial() {
        return XMaterial.BLAZE_ROD;
    }

    @Override
    public String[] getDescription() {
        return new String[]{
                "Will be fired when an lightning",
                "strikes the ground"
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
        return new OutcomingVariable[]{
                new OutcomingVariable(
                        "event", "Event",
                        DataType.CANCELLABLE_EVENT, elementInfo)
        };
    }

    @EventHandler
    public void onLightningStrike(final LightningStrikeEvent event){
        call(elementInfo -> {
            ScriptInstance instance = new ScriptInstance();

            getOutcomingVariables(elementInfo)[0].register(instance, new DataRequester() {
                @Override
                public Object request() {
                    return event;
                }
            });

            return instance;
        });
    }
}
