package de.kaleidox.botstats.catnip;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.CompletableFuture;

import de.kaleidox.botstats.BotListSettings;
import de.kaleidox.botstats.exception.RequestFailedException;
import de.kaleidox.botstats.model.StatsClient;
import de.kaleidox.botstats.net.Method;

import com.mewna.catnip.Catnip;
import com.mewna.catnip.entity.guild.Guild;
import com.mewna.catnip.shard.DiscordEvent;
import com.mewna.catnip.util.logging.LogAdapter;
import io.vertx.core.eventbus.MessageConsumer;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class CatnipStatsClient extends StatsClient {
    private final LogAdapter log;
    private final Catnip api;
    private final OkHttpClient client;

    private final Collection<MessageConsumer<?>> consumers;

    /**
     * Single-sharded constructor.
     *
     * @param settings The settings object.
     * @param api      A Catnip instance.
     */
    public CatnipStatsClient(BotListSettings settings, Catnip api) {
        super(settings, new CatnipJsonFactory(api));
        ((CatnipJsonFactory) jsonFactory).client = this;

        this.log = api.logAdapter();
        this.api = api;

        client = new OkHttpClient.Builder().build();
        consumers = new ArrayList<>();

        consumers.add(api.on(DiscordEvent.GUILD_CREATE, this::serverChange));
        consumers.add(api.on(DiscordEvent.GUILD_DELETE, this::serverChange));
    }

    @Override
    public void close() {
        consumers.forEach(MessageConsumer::unregister);
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
        try {
            //noinspection ConstantConditions
            return api.selfUser().idAsLong();
        } catch (NullPointerException npe) {
            throw new AssertionError("No Shards are connected!");
        }
    }

    @Override
    protected void logDebug(String message) {
        log.debug(message);
    }

    private void serverChange(Guild guild) {
        updateAllStats().join();
    }
}
