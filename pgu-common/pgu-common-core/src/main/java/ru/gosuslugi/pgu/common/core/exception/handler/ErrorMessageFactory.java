package ru.gosuslugi.pgu.common.core.exception.handler;

public interface ErrorMessageFactory<R> {
    R createErrorMessage(String reason, String status);
}
