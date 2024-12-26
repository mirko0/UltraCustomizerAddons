package com.github.mirko0.discordio.events.discord;

import com.sun.jdi.InvalidTypeException;
import lombok.Getter;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

@Getter
public class DiscordMessageEvent extends Event {
    private static final HandlerList handlers = new HandlerList();

    private MessageReceivedEvent discordEvent;

    public DiscordMessageEvent(MessageReceivedEvent event) {
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
