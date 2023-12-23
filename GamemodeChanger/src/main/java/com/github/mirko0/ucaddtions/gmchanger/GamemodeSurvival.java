package com.github.mirko0.ucaddtions.gmchanger;

import me.TechsCode.UltraCustomizer.UltraCustomizer;
import me.TechsCode.UltraCustomizer.base.item.XMaterial;
import me.TechsCode.UltraCustomizer.scriptSystem.objects.*;
import me.TechsCode.UltraCustomizer.scriptSystem.objects.datatypes.DataType;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;

public class GamemodeSurvival extends Element {
    public GamemodeSurvival(UltraCustomizer ultraCustomizer) {
        super(ultraCustomizer);
    }

    @Override
    public String getName() {
        return "Toggle Survival";
    }

    @Override
    public String getInternalName() {
        return "gm-survival";
    }

    @Override
    public boolean isHidingIfNotCompatible() {
        return false;
    }

    @Override
    public XMaterial getMaterial() {
        return XMaterial.ENDER_CHEST;
    }

    @Override
    public String[] getDescription() {
        return new String[]{
                "Changes players gamemode."
        };
    }

    @Override
    public Argument[] getArguments(ElementInfo elementInfo) {
        return new Argument[]{
                new Argument("player", "Player", DataType.PLAYER, elementInfo),
        };
    }

    @Override
    public OutcomingVariable[] getOutcomingVariables(ElementInfo elementInfo) {
        return new OutcomingVariable[0];
    }

    @Override
    public Child[] getConnectors(ElementInfo elementInfo) {
        return new Child[]{
                new DefaultChild(elementInfo, "next")
        };
    }

    @Override
    public void run(ElementInfo elementInfo, ScriptInstance instance) {
        Player player = (Player) this.getArguments(elementInfo)[0].getValue(instance);

        player.setGameMode(GameMode.SURVIVAL);
        this.getConnectors(elementInfo)[0].run(instance);
    }
}
