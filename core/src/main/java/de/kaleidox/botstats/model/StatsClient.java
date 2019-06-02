package de.kaleidox.botstats.model;

import java.net.URL;
import java.util.concurrent.CompletableFuture;

import de.kaleidox.botstats.BotList;
import de.kaleidox.botstats.BotListSettings;
import de.kaleidox.botstats.endpoints.Endpoint;
import de.kaleidox.botstats.net.Method;

import static de.kaleidox.botstats.net.Method.POST;

/**
 * Base class for handling bot list stats.
 */
public abstract class StatsClient {
    protected final BotListSettings settings;
    protected final JsonFactory jsonFactory;

    /**
     * Constructor.
     *
     * @param settings    A settings object with all tokens that are to be used.
     * @param jsonFactory A JsonFactory instance used for creating json strings.
     */
    protected StatsClient(BotListSettings settings, JsonFactory jsonFactory) {
        this.settings = settings;
        this.jsonFactory = jsonFactory;
    }

    /**
     * Updates the server count on all available bot lists.
     * <p>
     * This method is automatically invoked by GuildMemberJoin and GuildMemberLeave events in the default supported
     * libraries.
     *
     * @return A future that completes when all stats have been updated.
     */
    public synchronized CompletableFuture<Void> updateAllStats() {
        final CompletableFuture[] futures = new CompletableFuture[settings.definedTokenCount()];

        BotList[] lists = BotList.values();
        for (int i = 0; i < lists.length; i++) {
            BotList botList = lists[i];
            String token = botList.getToken(settings);
            Endpoint endpoint = botList.getEndpointLibrary().with(Endpoint.Target.STATS).orElse(null);

            if (token == null || endpoint == null) {
                logDebug((token == null ? "Token" : "Endpoint") + " for " + botList + " was null! Skipping.");
                futures[i] = CompletableFuture.completedFuture(null);
                continue;
            }

            URL url = endpoint.url(getOwnId());
            String body = jsonFactory.getStatJson(botList.getStatScopes());

            futures[i] = executeRequest(url, POST, token, body);
        }

        return CompletableFuture.allOf(futures);
    }

    /**
     * Used to place a web request using the librarie's default REST dependency for no extra dependencies.
     *
     * @param url           The URL to place the request to.
     * @param method        The HTTP method for the request.
     * @param authorization The authorization token for the web page.
     * @param body          The request body. {@code application/json} supported only.
     *
     * @return A future that completes with the response of the request.
     */
    protected abstract CompletableFuture<String> executeRequest(
            URL url,
            @SuppressWarnings("SameParameterValue") Method method,
            String authorization,
            String body
    );

    /**
     * Used to get the ID of the bot itself.
     *
     * @return The ID of the bot itself.
     */
    protected abstract long getOwnId();

    /**
     * Used to print information on DEBUG level using the logging implementation provided by the libraries.
     *
     * @param message The message to print.
     */
    protected abstract void logDebug(String message);
}
