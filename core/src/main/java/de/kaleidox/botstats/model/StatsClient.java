package de.kaleidox.botstats.model;

import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

import de.kaleidox.botstats.BotList;
import de.kaleidox.botstats.BotListSettings;
import de.kaleidox.botstats.endpoints.Endpoint;
import de.kaleidox.botstats.net.Method;

import lombok.Getter;

import static de.kaleidox.botstats.net.Method.POST;

/**
 * Base class for handling bot list stats.
 */
public abstract class StatsClient implements Closeable {
    protected @Getter final BotListSettings settings;
    protected final JsonFactory jsonFactory;
    protected final Map<BotList, String> tokenMap;

    /**
     * Constructor.
     *
     * @param settings    A settings object with all tokens that are to be used.
     * @param jsonFactory A JsonFactory instance used for creating json strings.
     */
    protected StatsClient(BotListSettings settings, JsonFactory jsonFactory) {
        this.settings = settings;
        this.jsonFactory = jsonFactory;

        tokenMap = new ConcurrentHashMap<>();
        if (settings.getTokenFile() != null) {
            // use token file
            System.out.println("Reading tokens from token file...");

            try {
                updateTokensFromFile();
            } catch (IOException e) {
                System.err.println("An exception occurred reading the token file:");
                e.printStackTrace(System.err);
            }
        } else {
            // use defined tokens
            System.out.println("Using tokens defined in BotListSettings...");

            updateTokensFromSettings();
        }
    }

    /**
     * Call this method to re-initialize all tokens defined in the attached {@link BotListSettings} object.
     * Does nothing if no tokens are defined in the settings object.
     */
    public void updateTokensFromSettings() {
        String buf;

        if ((buf = settings.getDiscordbots_org_token()) != null)
            tokenMap.put(BotList.DISCORDBOTS_ORG, buf);

        if ((buf = settings.getDiscord_bots_gg_token()) != null)
            tokenMap.put(BotList.DISCORD_BOTS_GG, buf);

        if ((buf = settings.getDiscordbotlist_com_token()) != null)
            tokenMap.put(BotList.DISCORDBOTLIST_COM, buf);

        if ((buf = settings.getDivinediscordbots_com_token()) != null)
            tokenMap.put(BotList.DIVINEDISCORDBOTS_COM, buf);

        if ((buf = settings.getBots_ondiscord_xyz_token()) != null)
            tokenMap.put(BotList.BOTS_ONDISCORD_XYZ, buf);
    }

    /**
     * Call this method to re-initialize all tokens from the defined token file.
     * Does nothing if no token file was specified.
     *
     * @throws IOException Any exception that occurred during read or write actions.
     */
    public void updateTokensFromFile() throws IOException {
        File f;
        if ((f = settings.getTokenFile()) == null)
            return;

        f.createNewFile();
        Properties properties = new Properties();

        FileInputStream in = new FileInputStream(f);
        properties.load(in);
        in.close();

        String buf;

        if ((buf = properties.getProperty("discordbots_org_token")) == null || !buf.equals("[token]"))
            properties.setProperty("discordbots_org_token", "[token]");
        else tokenMap.put(BotList.DISCORDBOTS_ORG, buf);

        if ((buf = properties.getProperty("discord_bots_gg_token")) == null || !buf.equals("[token]"))
            properties.setProperty("discord_bots_gg_token", "[token]");
        else tokenMap.put(BotList.DISCORD_BOTS_GG, buf);

        if ((buf = properties.getProperty("discordbotlist_com_token")) == null || buf.equals("[token]"))
            properties.setProperty("discordbotlist_com_token", "[token]");
        else tokenMap.put(BotList.DISCORDBOTLIST_COM, buf);

        if ((buf = properties.getProperty("divinediscordbots_com_token")) == null || !buf.equals("[token]"))
            properties.setProperty("divinediscordbots_com_token", "[token]");
        else tokenMap.put(BotList.DIVINEDISCORDBOTS_COM, buf);

        if ((buf = properties.getProperty("bots_ondiscord_xyz_token")) == null || !buf.equals("[token]"))
            properties.setProperty("bots_ondiscord_xyz_token", "[token]");
        else tokenMap.put(BotList.BOTS_ONDISCORD_XYZ, buf);

        FileOutputStream out = new FileOutputStream(f);
        properties.store(out, "Define your bot list tokens here. " +
                "\"[token]\" is the default value for no token.");
        out.close();
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
        if (settings.getPostStatsTester().get()) {
            final BotList[] lists = BotList.values();
            final CompletableFuture[] futures = new CompletableFuture[lists.length];

            for (int i = 0; i < lists.length; i++) {
                BotList botList = lists[i];
                String token = tokenMap.get(botList);
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
        return CompletableFuture.completedFuture(null);
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
