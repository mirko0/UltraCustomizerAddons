package com.github.mirko0.discordio.customizer.constructors;

import com.github.mirko0.discordio.AddonMain;
import com.github.mirko0.discordio.datatypes.QDataTypes;
import com.github.mirko0.discordio.events.discord.DiscordMessageEvent;
import me.TechsCode.UltraCustomizer.UltraCustomizer;
import me.TechsCode.UltraCustomizer.base.item.XMaterial;
import me.TechsCode.UltraCustomizer.scriptSystem.objects.*;
import me.TechsCode.UltraCustomizer.scriptSystem.objects.datatypes.DataType;
import org.bukkit.event.EventHandler;

public class ChannelMessageConstructor extends Constructor {
    public ChannelMessageConstructor(UltraCustomizer plugin) {
        super(plugin);
    }

    @Override
    public boolean isUnlisted() {
        return false;
    }

    @Override
    public String getName() {
        return "[D] Channel Message Received";
    }

    @Override
    public String getInternalName() {
        return "discordio-channel-message-r";
    }

    @Override
    public XMaterial getMaterial() {
        return XMaterial.WRITTEN_BOOK;
    }

    @Override
    public String[] getDescription() {
        return new String[]{"Receive a message from a discord server channel."};
    }

    @Override
    public Argument[] getArguments(ElementInfo elementInfo) {
        return new Argument[0];
    }

    @Override
    public OutcomingVariable[] getOutcomingVariables(ElementInfo elementInfo) {
        return new OutcomingVariable[]{
                new OutcomingVariable("user", "Discord User", QDataTypes.DISCORD_USER, elementInfo),
                new OutcomingVariable("channel", "Message Channel", QDataTypes.MESSAGE_CHANNEL, elementInfo),
                new OutcomingVariable("guild", "Discord Server", QDataTypes.DISCORD_GUILD, elementInfo),
                new OutcomingVariable("text", "Text", DataType.STRING, elementInfo),
        };
    }

    @EventHandler
    public void onMessage(DiscordMessageEvent event) {
        call(elementInfo -> {
            ScriptInstance instance = new ScriptInstance();
            getOutcomingVariables(elementInfo)[0].register(instance, new DataRequester() {
                public Object request() {
                    return event.getDiscordEvent().getAuthor().getEffectiveName();
                }
            });
            getOutcomingVariables(elementInfo)[1].register(instance, new DataRequester() {
                public Object request() {
                    return event.getDiscordEvent().getChannel();
                }
            });
            getOutcomingVariables(elementInfo)[2].register(instance, new DataRequester() {
                public Object request() {
                    return event.getDiscordEvent().getGuild();
                }
            });
            getOutcomingVariables(elementInfo)[3].register(instance, new DataRequester() {
                public Object request() {
                    return event.getDiscordEvent().getMessage().getContentDisplay();
                }
            });
            return instance;
        });
    }
}
