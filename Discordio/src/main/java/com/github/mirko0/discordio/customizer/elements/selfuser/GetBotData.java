package com.github.mirko0.discordio.customizer.elements.selfuser;

import com.github.mirko0.discordio.AddonMain;
import com.github.mirko0.discordio.datatypes.QDataTypes;
import com.github.mirko0.discordio.datatypes.specifications.OffsetDateTimeSpecification;
import com.github.mirko0.discordio.dbot.BotMain;
import me.TechsCode.UltraCustomizer.UltraCustomizer;
import me.TechsCode.UltraCustomizer.base.item.XMaterial;
import me.TechsCode.UltraCustomizer.scriptSystem.objects.*;
import me.TechsCode.UltraCustomizer.scriptSystem.objects.datatypes.DataType;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.SelfUser;

public class GetBotData extends Element {
    public GetBotData(UltraCustomizer plugin) {
        super(plugin);
    }

    @Override
    public String getName() {
        return "[D] Get Discord Bot Data";
    }

    @Override
    public String getInternalName() {
        return "discordio-bot-data";
    }

    @Override
    public boolean isHidingIfNotCompatible() {
        return false;
    }

    @Override
    public XMaterial getMaterial() {
        return XMaterial.TOTEM_OF_UNDYING;
    }

    @Override
    public String[] getDescription() {
        return new String[]{"Returns data from your discord bot.", "Used for retrieving id, name..."};
    }

    @Override
    public Argument[] getArguments(ElementInfo elementInfo) {
        return new Argument[0];
    }

    @Override
    public OutcomingVariable[] getOutcomingVariables(ElementInfo elementInfo) {
        return new OutcomingVariable[]{
                new OutcomingVariable("id", "Bot Id", DataType.STRING, elementInfo),
                new OutcomingVariable("name", "Bot Name", DataType.STRING, elementInfo),
                new OutcomingVariable("inviteUrl", "Invite Link", DataType.STRING, elementInfo),
                new OutcomingVariable("serverCount", "Bot Server Count", DataType.NUMBER, elementInfo),
                new OutcomingVariable("timeCreated", "Bot Creation Date", QDataTypes.OFFSET_DATE_TIME, elementInfo)
        };
    }

    @Override
    public Child[] getConnectors(ElementInfo elementInfo) {
        return new Child[]{new DefaultChild(elementInfo, "next")};
    }

    @Override
    public void run(ElementInfo elementInfo, ScriptInstance scriptInstance) {
        BotMain discordBot = AddonMain.instance.getDiscordBot();
        if (!discordBot.isRunning()) {
            AddonMain.log("Discord Bot is not running unable to execute GetBotData. Please enter bot token to start the bot.");
            return;
        }
        JDA jda = discordBot.getJda();
        SelfUser user = jda.getSelfUser();
        this.getOutcomingVariables(elementInfo)[0].register(scriptInstance, new DataRequester() {
            @Override
            public Object request() {
                return user.getId();
            }
        });
        this.getOutcomingVariables(elementInfo)[1].register(scriptInstance, new DataRequester() {
            @Override
            public Object request() {
                return user.getName();
            }
        });
        this.getOutcomingVariables(elementInfo)[2].register(scriptInstance, new DataRequester() {
            @Override
            public Object request() {
                return jda.getInviteUrl(Permission.ADMINISTRATOR);
            }
        });
        this.getOutcomingVariables(elementInfo)[3].register(scriptInstance, new DataRequester() {
            @Override
            public Object request() {
                return (long) jda.getGuilds().size();
            }
        });
        this.getOutcomingVariables(elementInfo)[4].register(scriptInstance, new DataRequester() {
            @Override
            public Object request() {
                return user.getTimeCreated();
            }
        });

        this.getConnectors(elementInfo)[0].run(scriptInstance);
    }
}
