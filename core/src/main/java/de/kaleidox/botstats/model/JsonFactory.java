package de.kaleidox.botstats.model;

import de.kaleidox.botstats.endpoints.Scope;

/**
 * Base class for generating JSON strings to be used in requests.
 */
public abstract class JsonFactory {
    /**
     * Creates a JSON string with all fields that match the provided {@code scopes}.
     *
     * @param scopes The scopes to include in the JSON string-
     *
     * @return The generated JSON string.
     */
    public abstract String getStatJson(Scope... scopes);
}
