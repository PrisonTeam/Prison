package me.faizaand.prison.events;

import java.util.function.Function;

public class Subscription {

    private EventPriority priority;
    private Function<Object[], Object[]> callback;

    public Subscription(EventPriority priority, Function<Object[], Object[]> callback) {
        this.priority = priority;
        this.callback = callback;
    }

    public Function<Object[], Object[]> getCallback() {
        return callback;
    }

    public EventPriority getPriority() {
        return priority;
    }

}
