package me.faizaand.prison.events;

import com.google.common.base.Preconditions;

import java.util.*;
import java.util.function.Function;

/**
 * A standard implementation of {@link EventManager}.
 *
 * @since 4.0
 */
public class StandardEventManager implements EventManager {

    private Map<EventType, EventHandler> handlers;

    public StandardEventManager() {
        this.handlers = new HashMap<>();
    }

    @Override
    public void subscribe(EventType type, Function<Object[], Object[]> callback, EventPriority priority) {
        Preconditions.checkArgument(handlers.containsKey(type), "event type %s has no registered handlers", type.name());

        Subscription subscription = new Subscription(priority, callback);
        handlers.get(type).addSubscription(subscription);
    }

    @Override
    public void registerHandler(EventType forType, EventHandler handler) {
        if (handlers.containsKey(forType))
            throw new IllegalArgumentException("event type " + forType.name() + " already has handler registered");

        handlers.put(forType, handler);
    }

    @Override
    public void unregisterHandler(EventType type) {
        handlers.remove(type);
    }

    @Override
    public List<EventHandler> getHandlers() {
        return new ArrayList<>(handlers.values());
    }

    @Override
    public Optional<EventHandler> getHandler(EventType forType) {
        return Optional.ofNullable(handlers.get(forType));
    }

}
