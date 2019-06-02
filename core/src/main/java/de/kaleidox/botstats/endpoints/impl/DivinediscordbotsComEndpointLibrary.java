package de.kaleidox.botstats.endpoints.impl;

import java.net.MalformedURLException;
import java.net.URL;
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

    private final Endpoint STATS = new EndpointImpl("/bot/%d/stats");

    @Override
    public Optional<Endpoint> with(Endpoint.Target target) {
        switch (target) {
            case STATS:
                return of(STATS);
            default:
                return Optional.empty();
        }
    }

    private class EndpointImpl implements Endpoint {
        private final String appendix;

        public EndpointImpl(String appendix) {
            this.appendix = appendix;
        }

        @Override
        public URL url(long botId) {
            try {
                if (requiresBotId())
                    if (botId == -1) {
                        throw new IllegalArgumentException("Cannot construct URL without a bot id!");
                    } else return new URL(String.format(BASE + appendix, botId));
                else return new URL(BASE + appendix);
            } catch (MalformedURLException e) {
                throw new AssertionError(e);
            }
        }

        @Override
        public boolean requiresBotId() {
            return appendix.contains("%d");
        }
    }
}
