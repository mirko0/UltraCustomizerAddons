package com.github.mirko0.ucaddtions.flytoggle;

import me.TechsCode.UltraCustomizer.UltraCustomizer;
import me.TechsCode.UltraCustomizer.base.item.XMaterial;
import me.TechsCode.UltraCustomizer.scriptSystem.objects.*;
import me.TechsCode.UltraCustomizer.scriptSystem.objects.datatypes.DataType;
import org.bukkit.entity.Player;

public class EnableFlight extends Element {
    public EnableFlight(UltraCustomizer ultraCustomizer) {
        super(ultraCustomizer);
    }

    @Override
    public String getName() {
        return "Allow Flight";
    }

    @Override
    public String getInternalName() {
        return "allow-player-flight";
    }

    @Override
    public boolean isHidingIfNotCompatible() {
        return false;
    }

    @Override
    public XMaterial getMaterial() {
        return XMaterial.FEATHER;
    }

    @Override
    public String[] getDescription() {
        return new String[]{
                "Allows player to fly."
        };
    }

    @Override
    public Argument[] getArguments(ElementInfo elementInfo) {
        return new Argument[]{
                new Argument("player","Player", DataType.PLAYER, elementInfo)
        };
    }

    @Override
    public OutcomingVariable[] getOutcomingVariables(ElementInfo elementInfo) {
        return new OutcomingVariable[0];
    }

    @Override
    public Child[] getConnectors(ElementInfo elementInfo) {
        return new Child[] { new DefaultChild(elementInfo, "next") };
    }

    @Override
    public void run(ElementInfo elementInfo, ScriptInstance scriptInstance) {
        final Player player = (Player) this.getArguments(elementInfo)[0].getValue(scriptInstance);
        player.setAllowFlight(true);
        this.getConnectors(elementInfo)[0].run(scriptInstance);
    }

}
