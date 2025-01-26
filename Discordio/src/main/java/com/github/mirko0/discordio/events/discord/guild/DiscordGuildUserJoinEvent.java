package com.github.mirko0.discordio.events.discord.guild;

import com.github.mirko0.discordio.events.discord.DiscordEvent;
import lombok.Getter;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

@Getter
public class DiscordGuildUserJoinEvent extends DiscordEvent<GuildMemberJoinEvent> {
    public DiscordGuildUserJoinEvent(GuildMemberJoinEvent guildMemberJoinEvent) {
        super(guildMemberJoinEvent);
    }
}
