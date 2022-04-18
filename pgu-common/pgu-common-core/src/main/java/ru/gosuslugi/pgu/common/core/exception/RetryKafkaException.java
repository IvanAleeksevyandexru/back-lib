package ru.gosuslugi.pgu.common.core.exception;


/**
 * Ошибка для маркирования сообщений, который нужно переотправлять через механизм ретраев в kafka
 */
public class RetryKafkaException extends PguException {

    public RetryKafkaException(Exception cause) {
        super("RetryKafkaException: " + cause.getClass().getSimpleName() + ": " + cause.getMessage(), cause);
    }

}
