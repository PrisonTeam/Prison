package me.faizaand.prison.events;

import java.util.function.Function;

public class Subscription {

    private EventPriority priority;
    private Class<?>[] types;
    private Function<Object[], Boolean> callback;

    public Subscription(EventPriority priority, Class<?>[] types, Function<Object[], Boolean> callback) {
        this.priority = priority;
        this.types = types;
        this.callback = callback;
    }

    public Class<?>[] getTypes() {
        return types;
    }

    public boolean hasType(Class<?> type) {
        for (Class<?> t : types) if (t == type) return true;
        return false;
    }

    public Function<Object[], Boolean> getCallback() {
        return callback;
    }

}
