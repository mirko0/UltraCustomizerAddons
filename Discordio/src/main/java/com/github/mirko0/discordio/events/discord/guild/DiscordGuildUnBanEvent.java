package com.github.mirko0.discordio.events.discord.guild;

import com.github.mirko0.discordio.events.discord.DiscordEvent;
import lombok.Getter;
import net.dv8tion.jda.api.events.guild.GuildUnbanEvent;

@Getter
public class DiscordGuildUnBanEvent extends DiscordEvent<GuildUnbanEvent> {
    public DiscordGuildUnBanEvent(GuildUnbanEvent event) {
        super(event);
    }
}
