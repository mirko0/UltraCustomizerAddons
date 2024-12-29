package com.github.mirko0.discordio.customizer.elements.message;

import com.github.mirko0.discordio.datatypes.QDataTypes;
import me.TechsCode.UltraCustomizer.UltraCustomizer;
import me.TechsCode.UltraCustomizer.base.item.XMaterial;
import me.TechsCode.UltraCustomizer.scriptSystem.objects.*;
import me.TechsCode.UltraCustomizer.scriptSystem.objects.datatypes.DataType;
import net.dv8tion.jda.api.entities.Message;

public class GetMessageData extends Element {
    public GetMessageData(UltraCustomizer plugin) {
        super(plugin);
    }

    @Override
    public String getName() {
        return "[D] Get Discord Message Data";
    }

    @Override
    public String getInternalName() {
        return "discordio-message-data";
    }

    @Override
    public boolean isHidingIfNotCompatible() {
        return false;
    }

    @Override
    public XMaterial getMaterial() {
        return XMaterial.BOOK;
    }

    @Override
    public String[] getDescription() {
        return new String[]{"Returns data from discord message.", "Used for retrieving content, author..."};
    }

    @Override
    public Argument[] getArguments(ElementInfo elementInfo) {
        return new Argument[]{
                new Argument("message", "Message", QDataTypes.DISCORD_MESSAGE, elementInfo)
        };
    }

    @Override
    public OutcomingVariable[] getOutcomingVariables(ElementInfo elementInfo) {
        return new OutcomingVariable[]{
                new OutcomingVariable("id", "Message Id", DataType.STRING, elementInfo),
                new OutcomingVariable("author", "Author", QDataTypes.DISCORD_USER, elementInfo),
                new OutcomingVariable("contentRaw", "Raw Content", DataType.STRING, elementInfo),
                new OutcomingVariable("contentDisplay", "Display Content", DataType.STRING, elementInfo),
                new OutcomingVariable("contentStripped", "Stripped Content", DataType.STRING, elementInfo),
                new OutcomingVariable("jumpUrl", "Jump URL", DataType.STRING, elementInfo),
                new OutcomingVariable("timeCreated", "Account Creation Date", QDataTypes.OFFSET_DATE_TIME, elementInfo)
        };
    }

    @Override
    public Child[] getConnectors(ElementInfo elementInfo) {
        return new Child[]{new DefaultChild(elementInfo, "next")};
    }

    @Override
    public void run(ElementInfo elementInfo, ScriptInstance scriptInstance) {
        Message message = (Message) this.getArguments(elementInfo)[0].getValue(scriptInstance);

        this.getOutcomingVariables(elementInfo)[0].register(scriptInstance, new DataRequester() {
            @Override
            public Object request() {
                return message.getId();
            }
        });
        this.getOutcomingVariables(elementInfo)[1].register(scriptInstance, new DataRequester() {
            @Override
            public Object request() {
                return message.getAuthor();
            }
        });
        this.getOutcomingVariables(elementInfo)[2].register(scriptInstance, new DataRequester() {
            @Override
            public Object request() {
                return message.getContentRaw();
            }
        });
        this.getOutcomingVariables(elementInfo)[3].register(scriptInstance, new DataRequester() {
            @Override
            public Object request() {
                return message.getContentDisplay();
            }
        });
        this.getOutcomingVariables(elementInfo)[4].register(scriptInstance, new DataRequester() {
            @Override
            public Object request() {
                return message.getContentStripped();
            }
        });
        this.getOutcomingVariables(elementInfo)[5].register(scriptInstance, new DataRequester() {
            @Override
            public Object request() {
                return message.getJumpUrl();
            }
        });
        this.getOutcomingVariables(elementInfo)[6].register(scriptInstance, new DataRequester() {
            @Override
            public Object request() {
                return message.getTimeCreated();
            }
        });
        this.getConnectors(elementInfo)[0].run(scriptInstance);
    }
}
