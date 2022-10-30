package ru.practicum.explore.with.me.event.exception;

public class HitSendException extends RuntimeException {
    public HitSendException(String message) {
        super("Cannot send hit: " + message);
    }
}