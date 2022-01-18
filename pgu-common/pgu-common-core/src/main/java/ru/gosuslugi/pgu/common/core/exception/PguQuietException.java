package ru.gosuslugi.pgu.common.core.exception;

/**
 * Exceptions which won't be logged. Quiet.
 */
public class PguQuietException extends PguException {

    public PguQuietException(String message) {
        super(message);
    }

    public PguQuietException(String message, Throwable cause) {
        super(message, cause);
    }

    public PguQuietException(Throwable cause) {
        super(cause);
    }

}
