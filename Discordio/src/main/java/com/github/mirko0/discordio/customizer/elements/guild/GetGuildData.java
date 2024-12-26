package com.github.mirko0.discordio.customizer.elements.guild;

import com.github.mirko0.discordio.AddonMain;
import com.github.mirko0.discordio.datatypes.QDataTypes;
import me.TechsCode.UltraCustomizer.UltraCustomizer;
import me.TechsCode.UltraCustomizer.base.item.XMaterial;
import me.TechsCode.UltraCustomizer.scriptSystem.objects.*;
import me.TechsCode.UltraCustomizer.scriptSystem.objects.datatypes.DataType;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.channel.ChannelType;
import net.dv8tion.jda.api.entities.channel.unions.MessageChannelUnion;

import java.time.OffsetDateTime;

public class GetGuildData extends Element {
    public GetGuildData(UltraCustomizer plugin) {
        super(plugin);
    }

    @Override
    public String getName() {
        return "[D] Get Discord Server Data";
    }

    @Override
    public String getInternalName() {
        return "discordio-guild-data";
    }

    @Override
    public boolean isHidingIfNotCompatible() {
        return false;
    }

    @Override
    public XMaterial getMaterial() {
        return XMaterial.BEACON;
    }

    @Override
    public String[] getDescription() {
        return new String[]{"Returns data from discord server.", "Used for retrieving id, name..."};
    }

    @Override
    public Argument[] getArguments(ElementInfo elementInfo) {
        return new Argument[]{
                new Argument("guild", "Discord Server", QDataTypes.DISCORD_GUILD, elementInfo)
        };
    }

    @Override
    public OutcomingVariable[] getOutcomingVariables(ElementInfo elementInfo) {
        return new OutcomingVariable[]{
                new OutcomingVariable("guildId", "Server Id", DataType.STRING, elementInfo),
                new OutcomingVariable("guildName", "Server Name", DataType.STRING, elementInfo),
                new OutcomingVariable("ownerId", "Server Owner Id", DataType.STRING, elementInfo),
                new OutcomingVariable("memberCount", "Server Member Count", DataType.NUMBER, elementInfo),
                new OutcomingVariable("timeCreated", "Account Creation Date", QDataTypes.OFFSET_DATE_TIME, elementInfo)
        };
    }

    @Override
    public Child[] getConnectors(ElementInfo elementInfo) {
        return new Child[]{new DefaultChild(elementInfo, "next")};
    }

    @Override
    public void run(ElementInfo elementInfo, ScriptInstance scriptInstance) {
        Guild guild = (Guild) this.getArguments(elementInfo)[0].getValue(scriptInstance);
        String id = guild.getId();
        String name = guild.getName();
        String serverOwnerId = guild.getOwnerId();
        int memberCount = guild.getMemberCount();
        OffsetDateTime timeCreated = guild.getTimeCreated();


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
                return serverOwnerId;
            }
        });
        this.getOutcomingVariables(elementInfo)[3].register(scriptInstance, new DataRequester() {
            @Override
            public Object request() {
                return (long) memberCount;
            }
        });
        this.getOutcomingVariables(elementInfo)[4].register(scriptInstance, new DataRequester() {
            @Override
            public Object request() {
                return timeCreated;
            }
        });

        this.getConnectors(elementInfo)[0].run(scriptInstance);
    }
}
