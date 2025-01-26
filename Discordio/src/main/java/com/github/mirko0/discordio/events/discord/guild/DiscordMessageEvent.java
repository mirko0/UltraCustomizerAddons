package com.github.mirko0.discordio.events.discord.guild;

import com.github.mirko0.discordio.events.discord.DiscordEvent;
import lombok.Getter;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

@Getter
public class DiscordMessageEvent extends DiscordEvent<MessageReceivedEvent> {
    public DiscordMessageEvent(MessageReceivedEvent messageReceivedEvent) {
        super(messageReceivedEvent);
    }
}
