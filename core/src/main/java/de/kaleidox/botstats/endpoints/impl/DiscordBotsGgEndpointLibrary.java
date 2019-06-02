package de.kaleidox.botstats.endpoints.impl;

import java.util.Optional;

import de.kaleidox.botstats.BotList;
import de.kaleidox.botstats.endpoints.Endpoint;

import static java.util.Optional.of;

/**
 * Endpoint library implementation for {@link BotList#DISCORD_BOTS_GG}.
 */
public enum DiscordBotsGgEndpointLibrary implements Endpoint.Library {
    INSTANCE;

    public static final String BASE = "https://discord.bots.gg/api/v1";

    private final Endpoint STATS = new EndpointBaseImpl(BASE, "/bots/%d/stats");

    @Override
    public Optional<Endpoint> with(Endpoint.Target target) {
        switch (target) {
            case STATS:
                return of(STATS);
            default:
                return Optional.empty();
        }
    }
}
