package com.github.mirko0.discordio.events.discord;

import lombok.Getter;
import net.dv8tion.jda.api.events.guild.GuildJoinEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

@Getter
public class DiscordGuildBotJoinEvent extends Event {
    private static final HandlerList handlers = new HandlerList();

    private GuildJoinEvent discordEvent;

    public DiscordGuildBotJoinEvent(GuildJoinEvent event) {
        super(true);
        this.discordEvent = event;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
