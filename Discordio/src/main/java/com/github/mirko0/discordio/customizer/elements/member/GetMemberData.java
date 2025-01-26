package com.github.mirko0.discordio.customizer.elements.member;

import com.github.mirko0.discordio.datatypes.QDataTypes;
import me.TechsCode.UltraCustomizer.UltraCustomizer;
import me.TechsCode.UltraCustomizer.base.item.XMaterial;
import me.TechsCode.UltraCustomizer.scriptSystem.objects.*;
import me.TechsCode.UltraCustomizer.scriptSystem.objects.datatypes.DataType;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;

public class GetMemberData extends Element {
    public GetMemberData(UltraCustomizer plugin) {
        super(plugin);
    }

    @Override
    public String getName() {
        return "[D] Get Discord Member Data";
    }

    @Override
    public String getInternalName() {
        return "discordio-member-data";
    }

    @Override
    public boolean isHidingIfNotCompatible() {
        return false;
    }

    @Override
    public XMaterial getMaterial() {
        return XMaterial.PLAYER_HEAD;
    }

    @Override
    public String[] getDescription() {
        return new String[]{"Returns data from discord member.", "Used for retrieving id, name..."};
    }

    @Override
    public Argument[] getArguments(ElementInfo elementInfo) {
        return new Argument[]{
                new Argument("member", "Member", QDataTypes.DISCORD_MEMBER, elementInfo)
        };
    }

    @Override
    public OutcomingVariable[] getOutcomingVariables(ElementInfo elementInfo) {
        return new OutcomingVariable[]{
                new OutcomingVariable("id", "Id", DataType.STRING, elementInfo),
                new OutcomingVariable("username", "Username", DataType.STRING, elementInfo),
                new OutcomingVariable("effectiveName", "Effective Name", DataType.STRING, elementInfo),
                new OutcomingVariable("mention", "User Mention", DataType.STRING, elementInfo),
                new OutcomingVariable("timeCreated", "Account Creation Date", QDataTypes.OFFSET_DATE_TIME, elementInfo)
        };
    }

    @Override
    public Child[] getConnectors(ElementInfo elementInfo) {
        return new Child[]{new DefaultChild(elementInfo, "next")};
    }

    @Override
    public void run(ElementInfo elementInfo, ScriptInstance scriptInstance) {
        Member user = (Member) this.getArguments(elementInfo)[0].getValue(scriptInstance);

        this.getOutcomingVariables(elementInfo)[0].register(scriptInstance, new DataRequester() {
            @Override
            public Object request() {
                return user.getId();
            }
        });
        this.getOutcomingVariables(elementInfo)[1].register(scriptInstance, new DataRequester() {
            @Override
            public Object request() {
                return user.getUser().getName();
            }
        });
        this.getOutcomingVariables(elementInfo)[2].register(scriptInstance, new DataRequester() {
            @Override
            public Object request() {
                return user.getEffectiveName();
            }
        });
        this.getOutcomingVariables(elementInfo)[3].register(scriptInstance, new DataRequester() {
            @Override
            public Object request() {
                return user.getAsMention();
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
