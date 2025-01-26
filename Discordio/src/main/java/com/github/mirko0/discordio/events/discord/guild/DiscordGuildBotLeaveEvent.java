package com.github.mirko0.discordio.events.discord.guild;

import com.github.mirko0.discordio.events.discord.DiscordEvent;
import lombok.Getter;
import net.dv8tion.jda.api.events.guild.GuildLeaveEvent;

@Getter
public class DiscordGuildBotLeaveEvent extends DiscordEvent<GuildLeaveEvent> {
    public DiscordGuildBotLeaveEvent(GuildLeaveEvent event) {
        super(event);
    }
}
