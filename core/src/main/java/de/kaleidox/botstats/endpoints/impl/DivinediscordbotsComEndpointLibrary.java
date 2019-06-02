package de.kaleidox.botstats.endpoints.impl;

import java.util.Optional;

import de.kaleidox.botstats.BotList;
import de.kaleidox.botstats.endpoints.Endpoint;

import static java.util.Optional.of;

/**
 * Endpoint library implementation for {@link BotList#DIVINEDISCORDBOTS_COM}.
 */
public enum DivinediscordbotsComEndpointLibrary implements Endpoint.Library {
    INSTANCE;

    public static final String BASE = "https://divinediscordbots.com";

    private final Endpoint STATS = new EndpointBaseImpl(BASE, "/bot/%d/stats");

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
