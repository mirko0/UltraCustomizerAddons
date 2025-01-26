package com.github.mirko0.discordio.customizer.constructors.user;

import com.github.mirko0.discordio.datatypes.QDataTypes;
import com.github.mirko0.discordio.events.discord.guild.DiscordGuildUserLeaveEvent;
import me.TechsCode.UltraCustomizer.UltraCustomizer;
import me.TechsCode.UltraCustomizer.base.item.XMaterial;
import me.TechsCode.UltraCustomizer.scriptSystem.objects.*;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.guild.member.GuildMemberRemoveEvent;
import org.bukkit.event.EventHandler;

import java.time.OffsetDateTime;

public class MemberLeaveConstructor extends Constructor {
    public MemberLeaveConstructor(UltraCustomizer plugin) {
        super(plugin);
    }

    @Override
    public boolean isUnlisted() {
        return false;
    }

    @Override
    public String getName() {
        return "[D] User Leave Server";
    }

    @Override
    public String getInternalName() {
        return "member-leave-guild";
    }

    @Override
    public XMaterial getMaterial() {
        return XMaterial.BAT_SPAWN_EGG;
    }

    @Override
    public String[] getDescription() {
        return new String[]{"Happens when user leaves a server."};
    }

    @Override
    public Argument[] getArguments(ElementInfo elementInfo) {
        return new Argument[0];
    }

    @Override
    public OutcomingVariable[] getOutcomingVariables(ElementInfo elementInfo) {
        return new OutcomingVariable[]{
                new OutcomingVariable("guild", "Left Server", QDataTypes.DISCORD_GUILD, elementInfo),
                new OutcomingVariable("member", "Member", QDataTypes.DISCORD_MEMBER, elementInfo),
                new OutcomingVariable("user", "User", QDataTypes.DISCORD_USER, elementInfo),
                new OutcomingVariable("time", "Leave Time", QDataTypes.OFFSET_DATE_TIME, elementInfo)
        };
    }

    @EventHandler
    public void onUserLeaveGuild(DiscordGuildUserLeaveEvent event) {
        OffsetDateTime now = OffsetDateTime.now();
        GuildMemberRemoveEvent dEvent = event.getDiscordEvent();
        User user = dEvent.getUser();
        Member member = dEvent.getMember();
        call(elementInfo -> {
            ScriptInstance instance = new ScriptInstance();
            getOutcomingVariables(elementInfo)[0].register(instance, new DataRequester() {
                public Object request() {
                    return dEvent.getGuild();
                }
            });
            getOutcomingVariables(elementInfo)[1].register(instance, new DataRequester() {
                public Object request() {
                    return member;
                }
            });
            getOutcomingVariables(elementInfo)[2].register(instance, new DataRequester() {
                public Object request() {
                    return user;
                }
            });
            getOutcomingVariables(elementInfo)[3].register(instance, new DataRequester() {
                public Object request() {
                    return now;
                }
            });
            return instance;
        });
    }
}
