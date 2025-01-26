package com.github.mirko0.discordio.customizer.constructors.guild;

import com.github.mirko0.discordio.datatypes.QDataTypes;
import com.github.mirko0.discordio.events.discord.guild.DiscordGuildBotLeaveEvent;
import me.TechsCode.UltraCustomizer.UltraCustomizer;
import me.TechsCode.UltraCustomizer.base.item.XMaterial;
import me.TechsCode.UltraCustomizer.scriptSystem.objects.*;
import org.bukkit.event.EventHandler;

import java.time.OffsetDateTime;

public class LeaveGuildConstructor extends Constructor {
    public LeaveGuildConstructor(UltraCustomizer plugin) {
        super(plugin);
    }

    @Override
    public boolean isUnlisted() {
        return false;
    }

    @Override
    public String getName() {
        return "[D] Leave Server";
    }

    @Override
    public String getInternalName() {
        return "bot-leave-guild";
    }

    @Override
    public XMaterial getMaterial() {
        return XMaterial.BAT_SPAWN_EGG;
    }

    @Override
    public String[] getDescription() {
        return new String[]{"Happens when bot leaves a server."};
    }

    @Override
    public Argument[] getArguments(ElementInfo elementInfo) {
        return new Argument[0];
    }

    @Override
    public OutcomingVariable[] getOutcomingVariables(ElementInfo elementInfo) {
        return new OutcomingVariable[]{
                new OutcomingVariable("guild", "Left Server", QDataTypes.DISCORD_GUILD, elementInfo),
                new OutcomingVariable("time", "Leave Time", QDataTypes.OFFSET_DATE_TIME, elementInfo)
        };
    }

    @EventHandler
    public void onBotLeaveEvent(DiscordGuildBotLeaveEvent event) {
        OffsetDateTime now = OffsetDateTime.now();
        call(elementInfo -> {
            ScriptInstance instance = new ScriptInstance();
            getOutcomingVariables(elementInfo)[0].register(instance, new DataRequester() {
                public Object request() {
                    return event.getDiscordEvent().getGuild();
                }
            });
            getOutcomingVariables(elementInfo)[1].register(instance, new DataRequester() {
                public Object request() {
                    return now;
                }
            });
            return instance;
        });
    }
}
