package com.github.mirko0.discordio.dbot;

import com.github.mirko0.discordio.AddonMain;
import com.github.mirko0.discordio.BotSettings;
import com.github.mirko0.discordio.dbot.listeners.EventReformater;
import lombok.Getter;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.ChunkingFilter;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import net.dv8tion.jda.api.utils.cache.CacheFlag;

@Getter
public class BotMain extends ListenerAdapter {

    private JDA jda;

//    private ScheduledExecutorService threadpool;
    private BotSettings settings;

    private boolean running = false;

    public BotMain(BotSettings settings) {
        this.settings = settings;
        start();
    }

    public void start() {
        try {
            AddonMain.log("Initializing...");
//            this.threadpool = Executors.newScheduledThreadPool(100, r -> new Thread(r, "threadpool"));
            String token = settings.getToken();
            AddonMain.log("Priming Nukes");
            if (token == null || token.isEmpty() || token.equalsIgnoreCase("NOT_SET")) {
                AddonMain.log("Bot Token is not defined.");
                return;
            }
            AddonMain.log("Nukes primed");
            JDABuilder jdaBuilder = JDABuilder.createDefault(token)
                    .setMemberCachePolicy(MemberCachePolicy.ALL)
                    .setChunkingFilter(ChunkingFilter.ALL)
                    .enableIntents(Constants.INTENTS)
                    .setDisabledIntents(GatewayIntent.DIRECT_MESSAGE_TYPING, GatewayIntent.GUILD_MESSAGE_TYPING)
                    .enableCache(CacheFlag.ONLINE_STATUS, CacheFlag.MEMBER_OVERRIDES, CacheFlag.VOICE_STATE, CacheFlag.ACTIVITY)
                    .setMemberCachePolicy(MemberCachePolicy.ALL)
                    .setChunkingFilter(ChunkingFilter.ALL)
                    .setActivity(settings.getActivity())
                    .setStatus(settings.getOnlineStatus())
                    .addEventListeners(this, new EventReformater(this));
            this.jda = jdaBuilder.build();
            jda.awaitReady();

            AddonMain.log("Discordio is ready. Nukes launching! :*");
            running = true;
        } catch (InterruptedException e) {
            AddonMain.log("Discordio encountered an exception while loading.");
            e.printStackTrace();
        }
    }

    public void stop() {
        running = false;
        if (jda == null) return;
        jda.shutdownNow();
    }
}
