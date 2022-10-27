package ru.practicum.explore.with.me.event.exception;

public class EventCancelException extends RuntimeException {
    public EventCancelException() {
        super("Only pending or canceled events can be changed");
    }
}