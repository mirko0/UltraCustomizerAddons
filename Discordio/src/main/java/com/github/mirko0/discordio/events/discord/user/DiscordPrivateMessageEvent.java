package com.github.mirko0.discordio.events.discord.user;

import com.github.mirko0.discordio.events.discord.DiscordEvent;
import lombok.Getter;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

@Getter
public class DiscordPrivateMessageEvent extends DiscordEvent<MessageReceivedEvent> {
    public DiscordPrivateMessageEvent(MessageReceivedEvent messageReceivedEvent) {
        super(messageReceivedEvent);
    }
}
