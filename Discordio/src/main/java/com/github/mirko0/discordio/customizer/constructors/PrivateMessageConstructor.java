package com.github.mirko0.discordio.customizer.constructors;

import com.github.mirko0.discordio.datatypes.QDataTypes;
import com.github.mirko0.discordio.events.discord.DiscordPrivateMessageEvent;
import me.TechsCode.UltraCustomizer.UltraCustomizer;
import me.TechsCode.UltraCustomizer.base.item.XMaterial;
import me.TechsCode.UltraCustomizer.scriptSystem.objects.*;
import org.bukkit.event.EventHandler;

public class PrivateMessageConstructor extends Constructor {
    public PrivateMessageConstructor(UltraCustomizer plugin) {
        super(plugin);
    }

    @Override
    public boolean isUnlisted() {
        return false;
    }

    @Override
    public String getName() {
        return "[D] Private Message Received";
    }

    @Override
    public String getInternalName() {
        return "discordio-private-message-r";
    }

    @Override
    public XMaterial getMaterial() {
        return XMaterial.WRITTEN_BOOK;
    }

    @Override
    public String[] getDescription() {
        return new String[]{"Receive a direct message from user."};
    }

    @Override
    public Argument[] getArguments(ElementInfo elementInfo) {
        return new Argument[0];
    }

    @Override
    public OutcomingVariable[] getOutcomingVariables(ElementInfo elementInfo) {
        return new OutcomingVariable[]{
                new OutcomingVariable("user", "Author", QDataTypes.DISCORD_USER, elementInfo),
                new OutcomingVariable("message", "Message", QDataTypes.DISCORD_MESSAGE, elementInfo)
        };
    }

    @EventHandler
    public void onMessage(DiscordPrivateMessageEvent event) {
        call(elementInfo -> {
            ScriptInstance instance = new ScriptInstance();
            getOutcomingVariables(elementInfo)[0].register(instance, new DataRequester() {
                public Object request() {
                    return event.getDiscordEvent().getAuthor();
                }
            });
            getOutcomingVariables(elementInfo)[1].register(instance, new DataRequester() {
                public Object request() {
                    return event.getDiscordEvent().getMessage();
                }
            });
            return instance;
        });
    }
}
