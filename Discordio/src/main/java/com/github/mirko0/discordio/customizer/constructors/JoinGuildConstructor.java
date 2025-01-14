package com.github.mirko0.discordio.customizer.constructors;

import com.github.mirko0.discordio.datatypes.QDataTypes;
import com.github.mirko0.discordio.events.discord.DiscordGuildBotJoinEvent;
import me.TechsCode.UltraCustomizer.UltraCustomizer;
import me.TechsCode.UltraCustomizer.base.item.XMaterial;
import me.TechsCode.UltraCustomizer.scriptSystem.objects.Argument;
import me.TechsCode.UltraCustomizer.scriptSystem.objects.Constructor;
import me.TechsCode.UltraCustomizer.scriptSystem.objects.ElementInfo;
import me.TechsCode.UltraCustomizer.scriptSystem.objects.OutcomingVariable;
import org.bukkit.event.EventHandler;

public class JoinGuildConstructor extends Constructor {
    public JoinGuildConstructor(UltraCustomizer plugin) {
        super(plugin);
    }

    @Override
    public boolean isUnlisted() {
        return false;
    }

    @Override
    public String getName() {
        return "[D] Join Server";
    }

    @Override
    public String getInternalName() {
        return "bot-join-guild";
    }

    @Override
    public XMaterial getMaterial() {
        return XMaterial.BAT_SPAWN_EGG;
    }

    @Override
    public String[] getDescription() {
        return new String[]{"Happens when bot joins a new server."};
    }

    @Override
    public Argument[] getArguments(ElementInfo elementInfo) {
        return new Argument[0];
    }

    @Override
    public OutcomingVariable[] getOutcomingVariables(ElementInfo elementInfo) {
        return new OutcomingVariable[]{
                new OutcomingVariable("guild", "Joined Server", QDataTypes.DISCORD_GUILD, elementInfo)
        };
    }

    @EventHandler
    public void onBotJoinGuild(DiscordGuildBotJoinEvent event) {
    }
}
