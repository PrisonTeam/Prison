package me.faizaand.prison.events;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

/**
 * A way of letting modules respond to events without being exposed to actual event code.
 * Modules can pass in the type of an event and the desired parameters, and these will be returned.
 *
 * @since 4.0
 */
public interface EventManager {

    /**
     * Subscribes to a specific event.
     *
     * @param type     the type of the event.
     * @param types    the types of the data you need to respond.
     * @param callback a callback, that gets the data you requested, and returns true if the event should be canceled, false otherwise.
     *                 <b>Data is put into the Object[] array in the same order that you put their types into the Class[] array!</b>
     * @param priority the priority of this event.
     */
    void subscribe(EventType type, Class<?>[] types, Function<Object[], Boolean> callback, EventPriority priority);

    /**
     * Registers an event handler that responds to a certain event type.
     * Only one handler may be registered for any one event type, so if you wish to replace it
     * for whatever reason, you must use {@link #unregisterHandler(EventType)}.
     *
     * @param forType the {@link EventType} that this handler deals with.
     * @param handler the {@link EventHandler} to register.
     * @throws IllegalArgumentException if the event type already has a handler.
     */
    void registerHandler(EventType forType, EventHandler handler);

    /**
     * Unregisters the handler that's currently registered for an event type.
     * If the event type does not have a handler attached, this method will do nothing.
     *
     * @param type the {@link EventType} whose handler will be unregistered.
     */
    void unregisterHandler(EventType type);

    /**
     * Returns a list of all of the registered handlers.
     *
     * @return a {@link List} of {@link EventHandler}s.
     */
    List<EventHandler> getHandlers();

    /**
     * Returns the event handler for a specific event type.
     *
     * @param forType the {@link EventType} that the handler deals with.
     * @return an optional containing the {@link EventHandler} if found, or empty if no handler is
     * registered for the specified event type.
     */
    Optional<EventHandler> getHandler(EventType forType);

}
