package com.github.mirko0.discordio.events.discord.guild;

import com.github.mirko0.discordio.events.discord.DiscordEvent;
import lombok.Getter;
import net.dv8tion.jda.api.events.guild.member.GuildMemberRemoveEvent;

@Getter
public class DiscordGuildUserLeaveEvent extends DiscordEvent<GuildMemberRemoveEvent> {

    public DiscordGuildUserLeaveEvent(GuildMemberRemoveEvent guildMemberRemoveEvent) {
        super(guildMemberRemoveEvent);
    }
}
