package com.github.mirko0.discordio.customizer.elements.channel;

import com.github.mirko0.discordio.datatypes.QDataTypes;
import me.TechsCode.UltraCustomizer.UltraCustomizer;
import me.TechsCode.UltraCustomizer.base.item.XMaterial;
import me.TechsCode.UltraCustomizer.scriptSystem.objects.*;
import me.TechsCode.UltraCustomizer.scriptSystem.objects.datatypes.DataType;
import net.dv8tion.jda.api.entities.channel.ChannelType;
import net.dv8tion.jda.api.entities.channel.unions.MessageChannelUnion;

import java.time.OffsetDateTime;

public class GetChannelData extends Element {
    public GetChannelData(UltraCustomizer plugin) {
        super(plugin);
    }

    @Override
    public String getName() {
        return "[D] Get Message Channel Data";
    }

    @Override
    public String getInternalName() {
        return "discordio-m-channel-data";
    }

    @Override
    public boolean isHidingIfNotCompatible() {
        return false;
    }

    @Override
    public XMaterial getMaterial() {
        return XMaterial.ENCHANTED_BOOK;
    }

    @Override
    public String[] getDescription() {
        return new String[]{"Returns data from message channel.", "Used for retrieving id, name..."};
    }

    @Override
    public Argument[] getArguments(ElementInfo elementInfo) {
        return new Argument[]{
                new Argument("channel", "Message Channel", QDataTypes.MESSAGE_CHANNEL, elementInfo)
        };
    }

    @Override
    public OutcomingVariable[] getOutcomingVariables(ElementInfo elementInfo) {
        return new OutcomingVariable[]{
                new OutcomingVariable("channelId", "Channel Id", DataType.STRING, elementInfo),
                new OutcomingVariable("name", "Channel Name", DataType.STRING, elementInfo),
                new OutcomingVariable("channelMention", "Channel Mention", DataType.STRING, elementInfo),
                new OutcomingVariable("latestMessageId", "Latest Message Id", DataType.STRING, elementInfo),
                new OutcomingVariable("channelType", "Channel Type", QDataTypes.CHANNEL_TYPE, elementInfo),
                new OutcomingVariable("timeCreated", "Account Creation Date", QDataTypes.OFFSET_DATE_TIME, elementInfo)
        };
    }

    @Override
    public Child[] getConnectors(ElementInfo elementInfo) {
        return new Child[]{new DefaultChild(elementInfo, "next")};
    }

    @Override
    public void run(ElementInfo elementInfo, ScriptInstance scriptInstance) {
        MessageChannelUnion channel = (MessageChannelUnion) this.getArguments(elementInfo)[0].getValue(scriptInstance);
        String id = channel.getId();
        String name = channel.getName();
        String mention = channel.getAsMention();
        ChannelType type = channel.getType();
        OffsetDateTime timeCreated = channel.getTimeCreated();
        String latestMessageId = channel.getLatestMessageId();


        this.getOutcomingVariables(elementInfo)[0].register(scriptInstance, new DataRequester() {
            @Override
            public Object request() {
                return id;
            }
        });
        this.getOutcomingVariables(elementInfo)[1].register(scriptInstance, new DataRequester() {
            @Override
            public Object request() {
                return name;
            }
        });
        this.getOutcomingVariables(elementInfo)[2].register(scriptInstance, new DataRequester() {
            @Override
            public Object request() {
                return mention;
            }
        });
        this.getOutcomingVariables(elementInfo)[3].register(scriptInstance, new DataRequester() {
            @Override
            public Object request() {
                return latestMessageId;
            }
        });
        this.getOutcomingVariables(elementInfo)[4].register(scriptInstance, new DataRequester() {
            @Override
            public Object request() {
                return type;
            }
        });
        this.getOutcomingVariables(elementInfo)[5].register(scriptInstance, new DataRequester() {
            @Override
            public Object request() {
                return timeCreated;
            }
        });

        this.getConnectors(elementInfo)[0].run(scriptInstance);
    }
}
