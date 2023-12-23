package com.github.mirko0.ucaddtions.potapply;

import me.TechsCode.UltraCustomizer.UltraCustomizer;
import me.TechsCode.UltraCustomizer.base.item.XMaterial;
import me.TechsCode.UltraCustomizer.scriptSystem.objects.*;
import me.TechsCode.UltraCustomizer.scriptSystem.objects.datatypes.DataType;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class ApplyPotion extends Element {
    public ApplyPotion(UltraCustomizer ultraCustomizer) {
        super(ultraCustomizer);
    }

    public String getName() {
        return "Apply potion effect";
    }

    public String getInternalName() {
        return "apply-pot-eff";
    }

    public boolean isHidingIfNotCompatible() {
        return false;
    }

    public XMaterial getMaterial() {
        return XMaterial.POTION;
    }

    public String[] getDescription() {
        return new String[] { "Applies potion effect to player." };
    }

    public Argument[] getArguments(ElementInfo elementInfo) {
        return new Argument[] { new Argument("player", "Player", DataType.PLAYER, elementInfo), new Argument("effect", "Effect", DataType.STRING, elementInfo), new Argument("amplifier", "Amplifier", DataType.NUMBER, elementInfo), new Argument("duration", "Duration", DataType.NUMBER, elementInfo), new Argument("particles", "Show Particles", DataType.BOOLEAN, elementInfo) };
    }

    public OutcomingVariable[] getOutcomingVariables(ElementInfo elementInfo) {
        return new OutcomingVariable[0];
    }

    public Child[] getConnectors(ElementInfo elementInfo) {
        return new Child[] {new DefaultChild(elementInfo, "next")};
    }

    public void run(ElementInfo elementInfo, ScriptInstance scriptInstance) {
        Player player = (Player)getArguments(elementInfo)[0].getValue(scriptInstance);
        String effect = (String)getArguments(elementInfo)[1].getValue(scriptInstance);
        long duration = (Long) getArguments(elementInfo)[3].getValue(scriptInstance);
        long amplifier = (Long) getArguments(elementInfo)[2].getValue(scriptInstance);
        boolean particles = (Boolean) getArguments(elementInfo)[4].getValue(scriptInstance);
        PotionEffectType type = potType(effect);
        if (player == null) {
            this.plugin.getBootstrap().getLogger().warning("Player is offline (Potion ApplyAddon)");
            getConnectors(elementInfo)[0].run(scriptInstance);
            return;
        }
        if (type == null) {
            this.plugin.getBootstrap().getLogger().warning("Potion effect does not exist! (Potion ApplyAddon)");
            this.plugin.getBootstrap().getLogger().warning("Possible types are: (Potion ApplyAddon)");
            for (PotionEffectType potType : PotionEffectType.values())
                this.plugin.getBootstrap().getLogger().warning(ChatColor.YELLOW + potType.getName());
            getConnectors(elementInfo)[0].run(scriptInstance);
            return;
        }
        player.addPotionEffect(new PotionEffect(type, (int)duration, (int)amplifier, false, particles));
        getConnectors(elementInfo)[0].run(scriptInstance);
    }

    public PotionEffectType potType(String s) {
        for (PotionEffectType type : PotionEffectType.values()) {
            if (type.getName().equals(s))
                return type;
        }
        return null;
    }
}
