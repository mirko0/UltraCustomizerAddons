package com.github.mirko0.discordio.customizer.constructors.user;

import com.github.mirko0.discordio.datatypes.QDataTypes;
import com.github.mirko0.discordio.events.discord.guild.DiscordGuildUserJoinEvent;
import me.TechsCode.UltraCustomizer.UltraCustomizer;
import me.TechsCode.UltraCustomizer.base.item.XMaterial;
import me.TechsCode.UltraCustomizer.scriptSystem.objects.*;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import org.bukkit.event.EventHandler;

import java.time.OffsetDateTime;

public class MemberJoinConstructor extends Constructor {
    public MemberJoinConstructor(UltraCustomizer plugin) {
        super(plugin);
    }

    @Override
    public boolean isUnlisted() {
        return false;
    }

    @Override
    public String getName() {
        return "[D] User Join Server";
    }

    @Override
    public String getInternalName() {
        return "member-join-guild";
    }

    @Override
    public XMaterial getMaterial() {
        return XMaterial.BAT_SPAWN_EGG;
    }

    @Override
    public String[] getDescription() {
        return new String[]{"Happens when user joins a new server."};
    }

    @Override
    public Argument[] getArguments(ElementInfo elementInfo) {
        return new Argument[0];
    }

    @Override
    public OutcomingVariable[] getOutcomingVariables(ElementInfo elementInfo) {
        return new OutcomingVariable[]{
                new OutcomingVariable("guild", "Joined Server", QDataTypes.DISCORD_GUILD, elementInfo),
                new OutcomingVariable("member", "Member", QDataTypes.DISCORD_MEMBER, elementInfo),
                new OutcomingVariable("user", "User", QDataTypes.DISCORD_USER, elementInfo),
                new OutcomingVariable("time", "Join Time", QDataTypes.OFFSET_DATE_TIME, elementInfo)
        };
    }

    @EventHandler
    public void onUserJoinGuild(DiscordGuildUserJoinEvent event) {
        OffsetDateTime now = OffsetDateTime.now();
        GuildMemberJoinEvent dEvent = event.getDiscordEvent();
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
