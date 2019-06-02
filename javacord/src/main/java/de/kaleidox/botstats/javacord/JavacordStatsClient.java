package de.kaleidox.botstats.javacord;

import java.io.IOException;
import java.net.URL;
import java.util.concurrent.CompletableFuture;

import de.kaleidox.botstats.BotListSettings;
import de.kaleidox.botstats.exception.RequestFailedException;
import de.kaleidox.botstats.model.StatsClient;
import de.kaleidox.botstats.net.Method;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.apache.logging.log4j.Logger;
import org.javacord.api.DiscordApi;
import org.javacord.api.event.server.ServerEvent;
import org.javacord.api.util.logging.ExceptionLogger;
import org.javacord.core.util.logging.LoggerUtil;

/**
 * Javacord implementation of the StatsClient.
 */
public class JavacordStatsClient extends StatsClient {
    private static final Logger log = LoggerUtil.getLogger(JavacordStatsClient.class);

    private final DiscordApi api;
    private final OkHttpClient client;

    /**
     * Single-sharded constructor.
     *
     * @param settings The settings object.
     * @param api      A single DiscordApi instance (single shard).
     */
    public JavacordStatsClient(BotListSettings settings, DiscordApi api) {
        super(settings, new JavacordJsonFactory(api));
        this.api = api;

        client = new OkHttpClient.Builder().build();

        api.addServerJoinListener(this::serverChange);
        api.addServerLeaveListener(this::serverChange);
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
        return api.getYourself().getId();
    }

    @Override
    protected void logDebug(String message) {
        log.debug(message);
    }

    private void serverChange(ServerEvent serverLeaveEvent) {
        updateAllStats().exceptionally(ExceptionLogger.get()).join();
    }
}
