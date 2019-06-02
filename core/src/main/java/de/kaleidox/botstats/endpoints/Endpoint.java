package de.kaleidox.botstats.endpoints;

import java.net.URL;
import java.util.Optional;

/**
 * Represents an API endpoint.
 */
public interface Endpoint {
    /**
     * Used to generate an URL for this endpoint using the specified bot id.
     * <p>
     * If {@link #requiresBotId()} returns {@code TRUE} and {@code botId} is {@code -1} (See {@link #url()}),
     * this method will throw an {@link IllegalArgumentException}.
     *
     * @param botId The ID to insert into the URL.
     *
     * @return The URL.
     * @throws IllegalArgumentException If no valid ID was provided when an ID was required.
     */
    URL url(long botId) throws IllegalArgumentException;

    /**
     * Checks whether this endpoint requires a bot id to be constructed.
     *
     * @return Whether a bot id is required.
     */
    boolean requiresBotId();

    /**
     * Used to generate an URL without a bot id parameter.
     * <p>
     * If {@link #requiresBotId()} returns {@code TRUE}, this method will throw an {@link IllegalArgumentException}.
     *
     * @return The URL.
     * @throws IllegalArgumentException If {@link #requiresBotId()} returned {@code TRUE}.
     */
    default URL url() throws IllegalArgumentException {
        if (requiresBotId())
            throw new IllegalArgumentException("Cannot construct URL without a bot id!");
        return url(-1);
    }

    /**
     * Represents a library with different endpoints.
     */
    interface Library {
        /**
         * Acquires the endpoint that can reach the provided {@link Target}, if available in this bot list.
         *
         * @param target The target to be reached.
         *
         * @return The endpoint to be used to reach the target, if available.
         */
        Optional<Endpoint> with(Target target);
    }

    /**
     * An enumeration of possible targets.
     */
    enum Target {
        /**
         * Represents posting of statistics.
         */
        STATS
    }
}
