package de.kaleidox.botstats.exception;

/**
 * Used for rethrowing exceptions caused by failed web requests.
 */
public class RequestFailedException extends RuntimeException {
    /**
     * Constructor.
     *
     * @param cause The exception thrown by the web request.
     */
    public RequestFailedException(Throwable cause) {
        super(cause);
    }
}
