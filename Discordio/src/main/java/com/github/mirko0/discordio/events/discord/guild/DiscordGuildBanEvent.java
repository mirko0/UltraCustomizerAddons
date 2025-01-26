package com.github.mirko0.discordio.events.discord.guild;

import com.github.mirko0.discordio.events.discord.DiscordEvent;
import lombok.Getter;
import net.dv8tion.jda.api.events.guild.GuildBanEvent;

@Getter
public class DiscordGuildBanEvent extends DiscordEvent<GuildBanEvent> {
    public DiscordGuildBanEvent(GuildBanEvent event) {
        super(event);
    }
}
