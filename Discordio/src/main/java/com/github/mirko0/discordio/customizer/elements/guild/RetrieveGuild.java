package com.github.mirko0.discordio.customizer.elements.guild;

import com.github.mirko0.discordio.AddonMain;
import com.github.mirko0.discordio.datatypes.QDataTypes;
import com.github.mirko0.discordio.dbot.BotMain;
import me.TechsCode.UltraCustomizer.UltraCustomizer;
import me.TechsCode.UltraCustomizer.base.item.XMaterial;
import me.TechsCode.UltraCustomizer.scriptSystem.objects.*;
import me.TechsCode.UltraCustomizer.scriptSystem.objects.datatypes.DataType;
import net.dv8tion.jda.api.entities.Guild;

public class RetrieveGuild extends Element {
    public RetrieveGuild(UltraCustomizer plugin) {
        super(plugin);
        QDataTypes.registerDataTypes();
        AddonMain instance = AddonMain.instance;
    }

    @Override
    public String getName() {
        return "[D] Retrieve Discord Server";
    }

    @Override
    public String getInternalName() {
        return "discordio-guild-retrieve";
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
        return new String[]{"Retrieves discord server from id."};
    }

    @Override
    public Argument[] getArguments(ElementInfo elementInfo) {
        return new Argument[]{
                new Argument("guildId", "Discord Server Id", DataType.STRING, elementInfo)
        };
    }

    @Override
    public OutcomingVariable[] getOutcomingVariables(ElementInfo elementInfo) {
        return new OutcomingVariable[]{
                new OutcomingVariable("status", "Status", DataType.STRING, elementInfo),
                new OutcomingVariable("guild", "Server", QDataTypes.DISCORD_GUILD, elementInfo),
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
        BotMain discordBot = AddonMain.instance.getDiscordBot();
        if (!discordBot.isRunning()) {
            AddonMain.log("Discord Bot is not running unable to execute RetrieveGuildData. Please enter bot token to start the bot.");
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

        Guild guild = discordBot.getJda().getGuildById(idLong);
        if (guild == null) {
            this.getOutcomingVariables(elementInfo)[0].register(scriptInstance, new DataRequester() {
                @Override
                public Object request() {
                    return "NOT_FOUND";
                }
            });
            this.getConnectors(elementInfo)[1].run(scriptInstance);
            return;
        }

        this.getOutcomingVariables(elementInfo)[0].register(scriptInstance, new DataRequester() {
            @Override
            public Object request() {
                return "SUCCESS";
            }
        });
        this.getOutcomingVariables(elementInfo)[1].register(scriptInstance, new DataRequester() {
            @Override
            public Object request() {
                return guild;
            }
        });

        this.getConnectors(elementInfo)[0].run(scriptInstance);
    }
}
