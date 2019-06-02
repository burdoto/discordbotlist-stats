package de.kaleidox.botstats.net;

/**
 * An enum containing possible HTTP request methods.
 */
public enum Method {
    /**
     * Represents a GET method
     */
    GET,

    /**
     * Represents a PUT method
     */
    PUT,

    /**
     * Represents a POST method
     */
    POST,

    /**
     * Represents a DELETE method
     */
    DELETE;

    /**
     * Gets the Identifier that can be used to place this request.
     *
     * @return The identifier of the request.
     */
    public String getIdentifier() {
        return name();
    }
}
