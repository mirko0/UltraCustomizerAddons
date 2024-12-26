package com.github.mirko0.discordio.dbot;

import net.dv8tion.jda.api.requests.GatewayIntent;

import java.util.EnumSet;

public class Constants {

    public final static EnumSet<GatewayIntent> INTENTS = EnumSet.of(
            GatewayIntent.DIRECT_MESSAGES,
            GatewayIntent.GUILD_MEMBERS,
            GatewayIntent.GUILD_INVITES,
            GatewayIntent.GUILD_MESSAGES,
            GatewayIntent.MESSAGE_CONTENT,
            GatewayIntent.GUILD_MESSAGE_REACTIONS
    );
}
