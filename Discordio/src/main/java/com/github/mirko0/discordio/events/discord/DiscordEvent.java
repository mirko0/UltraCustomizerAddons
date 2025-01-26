package com.github.mirko0.discordio.events.discord;

import lombok.Getter;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

@Getter
public abstract class DiscordEvent<EventType> extends Event {

    private final EventType discordEvent;

    public DiscordEvent(EventType type) {
        this.discordEvent = type;
    }

    private static final HandlerList handlers = new HandlerList();

    @Override
    public @NotNull HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
