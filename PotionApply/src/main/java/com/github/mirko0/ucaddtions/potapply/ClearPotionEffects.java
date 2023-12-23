package com.github.mirko0.ucaddtions.potapply;

import me.TechsCode.UltraCustomizer.UltraCustomizer;
import me.TechsCode.UltraCustomizer.base.item.XMaterial;
import me.TechsCode.UltraCustomizer.scriptSystem.objects.*;
import me.TechsCode.UltraCustomizer.scriptSystem.objects.datatypes.DataType;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;

public class ClearPotionEffects extends Element {
    public ClearPotionEffects(UltraCustomizer ultraCustomizer) {
        super(ultraCustomizer);
    }

    public String getName() {
        return "Clear potion effects";
    }

    public String getInternalName() {
        return "clear-pot-effs";
    }

    public boolean isHidingIfNotCompatible() {
        return false;
    }

    public XMaterial getMaterial() {
        return XMaterial.MILK_BUCKET;
    }

    public String[] getDescription() {
        return new String[] { "Clears all potion effects from player." };
    }

    public Argument[] getArguments(ElementInfo elementInfo) {
        return new Argument[] { new Argument("player", "Player", DataType.PLAYER, elementInfo)};
    }

    public OutcomingVariable[] getOutcomingVariables(ElementInfo elementInfo) {
        return new OutcomingVariable[0];
    }

    public Child[] getConnectors(ElementInfo elementInfo) {
        return new Child[] {new DefaultChild(elementInfo, "next")};
    }

    public void run(ElementInfo elementInfo, ScriptInstance scriptInstance) {
        Player player = (Player)getArguments(elementInfo)[0].getValue(scriptInstance);
        if (player == null) {
            this.plugin.getBootstrap().getLogger().warning("Player is offline (Potion ApplyAddon)");
            getConnectors(elementInfo)[0].run(scriptInstance);
            return;
        }
        for (PotionEffectType value : PotionEffectType.values()) {
            player.removePotionEffect(value);
        }
        getConnectors(elementInfo)[0].run(scriptInstance);
    }

}