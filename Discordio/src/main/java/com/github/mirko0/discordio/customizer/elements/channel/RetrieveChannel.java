package com.github.mirko0.discordio.customizer.elements.channel;

import com.github.mirko0.discordio.AddonMain;
import com.github.mirko0.discordio.datatypes.QDataTypes;
import com.github.mirko0.discordio.datatypes.specifications.ChannelTypeSpecification;
import com.github.mirko0.discordio.datatypes.specifications.GuildSpecification;
import com.github.mirko0.discordio.dbot.BotMain;
import me.TechsCode.UltraCustomizer.UltraCustomizer;
import me.TechsCode.UltraCustomizer.base.item.XMaterial;
import me.TechsCode.UltraCustomizer.scriptSystem.objects.*;
import me.TechsCode.UltraCustomizer.scriptSystem.objects.datatypes.DataType;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.channel.middleman.GuildChannel;
import net.dv8tion.jda.api.entities.channel.unions.MessageChannelUnion;

public class RetrieveChannel extends Element {
    public RetrieveChannel(UltraCustomizer plugin) {
        super(plugin);
    }

    @Override
    public String getName() {
        return "[D] Retrieve Message Channel";
    }

    @Override
    public String getInternalName() {
        return "discordio-m-channel-retrieve";
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
        return new String[]{"Retrieves message channel from", "selected guild and channel id."};
    }

    @Override
    public Argument[] getArguments(ElementInfo elementInfo) {
        return new Argument[]{
                new Argument("channelId", "Message Channel", DataType.STRING, elementInfo),
                new Argument("guild", "Guild", QDataTypes.DISCORD_GUILD, elementInfo)
        };
    }

    @Override
    public OutcomingVariable[] getOutcomingVariables(ElementInfo elementInfo) {
        return new OutcomingVariable[]{
                new OutcomingVariable("status", "Status", DataType.STRING, elementInfo),
                new OutcomingVariable("channel", "Message Channel", QDataTypes.MESSAGE_CHANNEL, elementInfo),
                };
    }

    @Override
    public Child[] getConnectors(ElementInfo elementInfo) {
        return new Child[]{
                new Child(elementInfo, "success") {
                    @Override
                    public String getName() {
                        return "Object Found";
                    }

                    @Override
                    public String[] getDescription() {
                        return new String[]{"Will be executed if the", "object was successfully found."};
                    }

                    @Override
                    public XMaterial getIcon() {
                        return XMaterial.GREEN_STAINED_GLASS_PANE;
                    }
                },
                new Child(elementInfo, "error") {
                    @Override
                    public String getName() {
                        return "Object Not Found";
                    }

                    @Override
                    public String[] getDescription() {
                        return new String[]{"Will be executed if the object", "was not found."};
                    }
                    @Override
                    public XMaterial getIcon() {
                        return XMaterial.RED_STAINED_GLASS_PANE;
                    }
                }};
    }
    @Override
    public void run(ElementInfo elementInfo, ScriptInstance scriptInstance) {
        String id = (String) this.getArguments(elementInfo)[0].getValue(scriptInstance);
        Guild guild = (Guild) this.getArguments(elementInfo)[1].getValue(scriptInstance);
        BotMain discordBot = AddonMain.instance.getDiscordBot();
        if (!discordBot.isRunning()) {
            AddonMain.log("Discord Bot is not running unable to execute RetrieveChanneldata. Please enter bot token to start the bot.");
            return;
        }
        long idLong;

        try {
            idLong = Long.parseLong(id);
        } catch (NumberFormatException e) {
            this.getOutcomingVariables(elementInfo)[0].register(scriptInstance, new DataRequester() {
                @Override
                public Object request() {
                    return "INVALID_ID";
                }
            });
            this.getConnectors(elementInfo)[1].run(scriptInstance);
            return;
        }

        GuildChannel channel = guild.getGuildChannelById(id);
        if (channel == null || !(channel instanceof MessageChannelUnion)) {
            this.getOutcomingVariables(elementInfo)[0].register(scriptInstance, new DataRequester() {
                @Override
                public Object request() {
                    return "NOT_FOUND";
                }
            });
            this.getConnectors(elementInfo)[1].run(scriptInstance);
            return;
        }
        MessageChannelUnion union = (MessageChannelUnion) channel;
        this.getOutcomingVariables(elementInfo)[0].register(scriptInstance, new DataRequester() {
            @Override
            public Object request() {
                return "SUCCESS";
            }
        });
        this.getOutcomingVariables(elementInfo)[1].register(scriptInstance, new DataRequester() {
            @Override
            public Object request() {
                return union;
            }
        });
        this.getConnectors(elementInfo)[0].run(scriptInstance);
    }
}
