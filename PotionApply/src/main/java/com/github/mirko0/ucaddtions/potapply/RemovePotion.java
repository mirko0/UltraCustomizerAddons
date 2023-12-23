package com.github.mirko0.ucaddtions.potapply;

import me.TechsCode.UltraCustomizer.UltraCustomizer;
import me.TechsCode.UltraCustomizer.base.item.XMaterial;
import me.TechsCode.UltraCustomizer.scriptSystem.objects.*;
import me.TechsCode.UltraCustomizer.scriptSystem.objects.datatypes.DataType;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;

public class RemovePotion extends Element {
    public RemovePotion(UltraCustomizer ultraCustomizer) {
        super(ultraCustomizer);
    }

    public String getName() {
        return "Remove potion effect";
    }

    public String getInternalName() {
        return "remove-pot-eff";
    }

    public boolean isHidingIfNotCompatible() {
        return false;
    }

    public XMaterial getMaterial() {
        return XMaterial.GLASS_BOTTLE;
    }

    public String[] getDescription() {
        return new String[] { "Removes potion effect from player." };
    }

    public Argument[] getArguments(ElementInfo elementInfo) {
        return new Argument[] { new Argument("player", "Player", DataType.PLAYER, elementInfo), new Argument("effect", "Effect", DataType.STRING, elementInfo)};
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
        player.removePotionEffect(type);
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
