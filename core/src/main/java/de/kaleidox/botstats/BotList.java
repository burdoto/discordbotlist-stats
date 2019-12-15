package de.kaleidox.botstats;

import java.util.function.Function;

import de.kaleidox.botstats.endpoints.Endpoint;
import de.kaleidox.botstats.endpoints.Scope;
import de.kaleidox.botstats.endpoints.impl.BotsOndiscordXyzEndpointLibrary;
import de.kaleidox.botstats.endpoints.impl.DiscordBotsGgEndpointLibrary;
import de.kaleidox.botstats.endpoints.impl.DiscordbotlistComEndpointLibrary;
import de.kaleidox.botstats.endpoints.impl.DivinediscordbotsComEndpointLibrary;
import de.kaleidox.botstats.endpoints.impl.TopGgEndpointLibrary;

import static de.kaleidox.botstats.endpoints.Scope.SERVER_COUNT;
import static de.kaleidox.botstats.endpoints.Scope.SHARD_ARRAY;
import static de.kaleidox.botstats.endpoints.Scope.SHARD_COUNT;
import static de.kaleidox.botstats.endpoints.Scope.SHARD_ID;
import static de.kaleidox.botstats.endpoints.Scope.USER_COUNT;
import static de.kaleidox.botstats.endpoints.Scope.VOICE_CONNECTION_COUNT;

/**
 * An enum featuring all supported discord bot lists.
 */
public enum BotList {
    /**
     * Entry for https://top.gg/
     */
    TOP_GG(BotListSettings::getTop_gg_token, TopGgEndpointLibrary.INSTANCE,
            SERVER_COUNT, SHARD_ARRAY, SHARD_ID, SHARD_COUNT),

    /**
     * Entry for https://discord.bots.gg/
     */
    DISCORD_BOTS_GG(BotListSettings::getDiscord_bots_gg_token, DiscordBotsGgEndpointLibrary.INSTANCE,
            SERVER_COUNT, SHARD_COUNT, SHARD_ID),

    /**
     * Entry for https://discordbotlist.com/
     */
    DISCORDBOTLIST_COM(BotListSettings::getDiscordbotlist_com_token, DiscordbotlistComEndpointLibrary.INSTANCE, SHARD_ID,
            SERVER_COUNT, USER_COUNT, VOICE_CONNECTION_COUNT),

    /**
     * Entry for https://divinediscordbots.com/
     */
    DIVINEDISCORDBOTS_COM(BotListSettings::getDivinediscordbots_com_token, DivinediscordbotsComEndpointLibrary.INSTANCE,
            SERVER_COUNT),

    /**
     * Entry for https://bots.ondiscord.xyz/
     */
    BOTS_ONDISCORD_XYZ(BotListSettings::getBots_ondiscord_xyz_token, BotsOndiscordXyzEndpointLibrary.INSTANCE,
            SERVER_COUNT);

    private final Function<BotListSettings, String> tokenExtractor;
    private final Endpoint.Library endpointLibrary;
    private final Scope[] statScopes;

    BotList(Function<BotListSettings, String> tokenExtractor, Endpoint.Library endpointLibrary, Scope... statScopes) {
        this.tokenExtractor = tokenExtractor;
        this.endpointLibrary = endpointLibrary;
        this.statScopes = statScopes;
    }

    /**
     * Fetches the token for this bot list page from the given settings object.
     *
     * @param settings The settings object to fetch the token from.
     *
     * @return The authorization token for this web page.
     */
    public String getToken(BotListSettings settings) {
        return tokenExtractor.apply(settings);
    }

    /**
     * Returns the endpointlibrary for this bot list.
     *
     * @return The Endpoint-Library to gather API endpoints for this bot list from.
     */
    public Endpoint.Library getEndpointLibrary() {
        return endpointLibrary;
    }

    /**
     * Returns an array of scopes that are supported by statistics in this bot list.
     *
     * @return All statistical scopes supported by this bot list.
     */
    public Scope[] getStatScopes() {
        return statScopes;
    }
}
