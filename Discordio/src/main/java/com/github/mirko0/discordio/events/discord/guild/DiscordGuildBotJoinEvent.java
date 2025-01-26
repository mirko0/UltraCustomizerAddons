package com.github.mirko0.discordio.events.discord.guild;

import com.github.mirko0.discordio.events.discord.DiscordEvent;
import lombok.Getter;
import net.dv8tion.jda.api.events.guild.GuildJoinEvent;

@Getter
public class DiscordGuildBotJoinEvent extends DiscordEvent<GuildJoinEvent> {
    public DiscordGuildBotJoinEvent(GuildJoinEvent event) {
        super(event);
    }
}
