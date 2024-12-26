package com.github.mirko0.discordio.dbot.listeners;

import com.github.mirko0.discordio.AddonMain;
import com.github.mirko0.discordio.dbot.BotMain;
import com.github.mirko0.discordio.events.discord.*;
import me.TechsCode.UltraCustomizer.UltraCustomizer;
import net.dv8tion.jda.api.entities.channel.ChannelType;
import net.dv8tion.jda.api.events.guild.GuildJoinEvent;
import net.dv8tion.jda.api.events.guild.GuildLeaveEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberRemoveEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;

public class EventReformater extends ListenerAdapter {
    public BotMain botMain;

    public EventReformater(BotMain botMain) {
        this.botMain = botMain;
    }

    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        if (event.getChannelType().equals(ChannelType.PRIVATE)) {
            Bukkit.getPluginManager().callEvent(new DiscordPrivateMessageEvent(event));
        }else {
            Bukkit.getPluginManager().callEvent(new DiscordMessageEvent(event));
        }
    }

    @Override
    public void onGuildJoin(@NotNull GuildJoinEvent event) {
        Bukkit.getPluginManager().callEvent(new DiscordGuildBotJoinEvent(event));
    }

    @Override
    public void onGuildLeave(@NotNull GuildLeaveEvent event) {
        Bukkit.getPluginManager().callEvent(new DiscordGuildBotLeaveEvent(event));
    }

    @Override
    public void onGuildMemberJoin(@NotNull GuildMemberJoinEvent event) {
        Bukkit.getPluginManager().callEvent(new DiscordGuildUserJoinEvent(event));
    }

    @Override
    public void onGuildMemberRemove(@NotNull GuildMemberRemoveEvent event) {
        Bukkit.getPluginManager().callEvent(new DiscordGuildUserLeaveEvent(event));
    }
}
