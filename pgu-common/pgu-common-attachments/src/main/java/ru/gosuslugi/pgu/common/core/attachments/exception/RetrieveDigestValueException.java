package ru.gosuslugi.pgu.common.core.attachments.exception;

/**
 * Исключение при ошибках получения Digest Value от Terrabyte
 */
public class RetrieveDigestValueException extends RuntimeException {

    public RetrieveDigestValueException(String s) {
        super(s);
    }

    public RetrieveDigestValueException(String message, Throwable cause) {
        super(message, cause);
    }

    public RetrieveDigestValueException(Throwable cause) {
        super(cause);
    }

}
