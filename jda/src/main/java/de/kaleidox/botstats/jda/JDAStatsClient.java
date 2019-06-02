package de.kaleidox.botstats.jda;

import java.io.IOException;
import java.net.URL;
import java.util.concurrent.CompletableFuture;

import de.kaleidox.botstats.BotListSettings;
import de.kaleidox.botstats.exception.RequestFailedException;
import de.kaleidox.botstats.model.StatsClient;
import de.kaleidox.botstats.net.Method;

import net.dv8tion.jda.bot.sharding.ShardManager;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.events.guild.GenericGuildEvent;
import net.dv8tion.jda.core.events.guild.GuildJoinEvent;
import net.dv8tion.jda.core.events.guild.GuildLeaveEvent;
import net.dv8tion.jda.core.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.core.events.guild.member.GuildMemberLeaveEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import net.dv8tion.jda.core.utils.JDALogger;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;

/**
 * JDA implementation of the StatsClient.
 */
public class JDAStatsClient extends StatsClient {
    private static final Logger log = JDALogger.getLog(JDAStatsClient.class);

    private final JDA jda;
    private final OkHttpClient client;

    /**
     * Constructor.
     *
     * @param settings The settings object.
     * @param jda      Any JDA object of your bot. Sharding will be handled automatically.
     */
    public JDAStatsClient(BotListSettings settings, JDA jda) {
        super(settings, new JDAJsonFactory(jda));
        this.jda = jda;

        client = new OkHttpClient.Builder().build();

        @Nullable ShardManager shardManager = jda.asBot().getShardManager();
        if (shardManager == null) {
            jda.addEventListener(new ListenerAdapter() {
                @Override
                public void onGuildJoin(GuildJoinEvent event) {
                    serverChange(event);
                }

                @Override
                public void onGuildLeave(GuildLeaveEvent event) {
                    serverChange(event);
                }
            });
        } else {
            shardManager.addEventListener(new ListenerAdapter() {
                @Override
                public void onGuildJoin(GuildJoinEvent event) {
                    serverChange(event);
                }

                @Override
                public void onGuildLeave(GuildLeaveEvent event) {
                    serverChange(event);
                }
            });
        }
    }

    @Override
    protected CompletableFuture<String> executeRequest(URL url, Method method, String authorization, String body) {
        Request request = new Request.Builder()
                .url(url)
                .header("Authorization", authorization)
                .method(method.getIdentifier(),
                        RequestBody.create(MediaType.parse("application/json"), method == Method.GET ? "" : body))
                .build();

        return CompletableFuture.supplyAsync(() -> {
                    try {
                        Response response = client.newCall(request).execute();

                        if (response.body() != null) {
                            return response.body().string();
                        } else return "{}";
                    } catch (IOException e) {
                        throw new RequestFailedException(e);
                    }
                }
        );
    }

    @Override
    protected long getOwnId() {
        return Long.parseLong(jda.getSelfUser().getId());
    }

    @Override
    protected void logDebug(String message) {
        log.debug(message);
    }

    private void serverChange(GenericGuildEvent event) {
        updateAllStats();
    }
}
