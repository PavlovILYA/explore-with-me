package ru.practicum.explore.with.me.event.exception;

public class EventPublishException extends RuntimeException {
    public EventPublishException(String message) {
        super(message);
    }
}