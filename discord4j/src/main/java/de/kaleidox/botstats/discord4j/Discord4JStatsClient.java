package de.kaleidox.botstats.discord4j;

import java.net.URL;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import de.kaleidox.botstats.BotListSettings;
import de.kaleidox.botstats.model.StatsClient;
import de.kaleidox.botstats.net.Method;

import discord4j.core.DiscordClient;
import discord4j.core.event.domain.guild.GuildEvent;
import discord4j.core.event.domain.guild.MemberJoinEvent;
import discord4j.core.event.domain.guild.MemberLeaveEvent;
import reactor.core.publisher.Mono;
import reactor.netty.ByteBufFlux;
import reactor.netty.http.client.HttpClient;
import reactor.util.Logger;
import reactor.util.Loggers;

/**
 * Discord4J implementation of the StatsClient
 */
public class Discord4JStatsClient extends StatsClient {
    private static final Logger log = Loggers.getLogger(DiscordClient.class);

    private final DiscordClient d4j;
    private final HttpClient client;

    /**
     * Single-Sharded constructor.
     *
     * @param settings The settings object.
     * @param d4j      A single Discord4J instance (single shard).
     */
    public Discord4JStatsClient(BotListSettings settings, DiscordClient d4j) {
        super(settings, new Discord4JSingleShardedJsonFactory(d4j));
        this.d4j = d4j;

        client = HttpClient.create();

        d4j.getEventDispatcher()
                .on(MemberJoinEvent.class)
                .subscribe(this::serverChange);

        d4j.getEventDispatcher()
                .on(MemberLeaveEvent.class)
                .subscribe(this::serverChange);
    }

    /**
     * Single-Sharded constructor.
     *
     * @param settings The settings object.
     * @param d4js     A single Discord4J instance (single shard).
     */
    public Discord4JStatsClient(BotListSettings settings, List<DiscordClient> d4js) {
        super(settings, new Discord4JMultiShardedJsonFactory(d4js));
        this.d4j = d4js.get(0);

        client = HttpClient.create();

        d4js.forEach(d4j -> {
            d4j.getEventDispatcher()
                    .on(MemberJoinEvent.class)
                    .subscribe(this::serverChange);
            d4j.getEventDispatcher()
                    .on(MemberLeaveEvent.class)
                    .subscribe(this::serverChange);
        });
    }

    @Override
    protected CompletableFuture<String> executeRequest(URL url, Method method, String authorization, String body) {
        HttpClient clt = client.headers(builder -> builder.add("Authorization", authorization));

        switch (method) {
            case GET:
                return clt.get()
                        .uri(url.toExternalForm())
                        .responseContent()
                        .aggregate()
                        .asString()
                        .toFuture();
            case PUT:
                return clt.put()
                        .uri(url.toExternalForm())
                        .send(ByteBufFlux.fromString(Mono.just(body)))
                        .responseContent()
                        .aggregate()
                        .asString()
                        .toFuture();
            case POST:
                return clt.post()
                        .uri(url.toExternalForm())
                        .send(ByteBufFlux.fromString(Mono.just(body)))
                        .responseContent()
                        .aggregate()
                        .asString()
                        .toFuture();
            case DELETE:
                return clt.delete()
                        .uri(url.toExternalForm())
                        .send(ByteBufFlux.fromString(Mono.just(body)))
                        .responseContent()
                        .aggregate()
                        .asString()
                        .toFuture();
            default:
                throw new AssertionError("Unexpected method: " + method);
        }
    }

    @Override
    protected long getOwnId() {
        return d4j.getSelfId().orElseThrow(AssertionError::new).asLong();
    }

    @Override
    protected void logDebug(String message) {
        log.debug(message);
    }

    private Void serverChange(GuildEvent event) {
        updateAllStats();
        return null;
    }
}
