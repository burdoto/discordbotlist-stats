package de.kaleidox.botstats.endpoints.impl;

import java.net.MalformedURLException;
import java.net.URL;

import de.kaleidox.botstats.endpoints.Endpoint;

class EndpointBaseImpl implements Endpoint {
    private final String base;
    private final String appendix;

    EndpointBaseImpl(String base, String appendix) {
        this.base = base;
        this.appendix = appendix;
    }

    @Override
    public URL url(long botId) {
        try {
            if (requiresBotId())
                if (botId == -1) {
                    throw new IllegalArgumentException("Cannot construct URL without a bot id!");
                } else return new URL(String.format(base + appendix, botId));
            else return new URL(base + appendix);
        } catch (MalformedURLException e) {
            throw new AssertionError(e);
        }
    }

    @Override
    public boolean requiresBotId() {
        return appendix.contains("%d");
    }
}
