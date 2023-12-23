package com.github.mirko0.ucaddtions.flytoggle;

import me.TechsCode.UltraCustomizer.UltraCustomizer;
import me.TechsCode.UltraCustomizer.base.item.XMaterial;
import me.TechsCode.UltraCustomizer.scriptSystem.objects.*;
import me.TechsCode.UltraCustomizer.scriptSystem.objects.datatypes.DataType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerToggleFlightEvent;

public class OnFlyToggle extends Constructor {
    public OnFlyToggle(UltraCustomizer ultraCustomizer) {
        super(ultraCustomizer);
    }

    public String getName() {
        return "On Fly Toggle";
    }

    public String getInternalName() {
        return "fly-toggle";
    }

    public boolean isHidingIfNotCompatible() {
        return false;
    }

    public boolean isUnlisted() {
        return false;
    }

    public XMaterial getMaterial() {
        return XMaterial.FEATHER;
    }

    public String[] getDescription() {
        return new String[] { "Triggers when player presses jump button twice" };
    }

    public Argument[] getArguments(ElementInfo elementInfo) {
        return new Argument[0];
    }

    public OutcomingVariable[] getOutcomingVariables(ElementInfo elementInfo) {
        return new OutcomingVariable[] { new OutcomingVariable("event", "Event", DataType.CANCELLABLE_EVENT, elementInfo), new OutcomingVariable("player", "Player", DataType.PLAYER, elementInfo), new OutcomingVariable("isFlying", "Is Player Flying", DataType.BOOLEAN, elementInfo) };
    }

    @EventHandler
    public void onFlyAttempt(final PlayerToggleFlightEvent event) {
        call(elementInfo -> {
            ScriptInstance instance = new ScriptInstance();
            getOutcomingVariables(elementInfo)[0].register(instance, new DataRequester() {
                public Object request() {
                    return event;
                }
            });
            getOutcomingVariables(elementInfo)[1].register(instance, new DataRequester() {
                public Object request() {
                    return event.getPlayer();
                }
            });
            getOutcomingVariables(elementInfo)[2].register(instance, new DataRequester() {
                public Object request() {
                    return event.isFlying();
                }
            });
            return instance;
        });
    }
}
