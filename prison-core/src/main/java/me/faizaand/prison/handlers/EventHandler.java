package me.faizaand.prison.handlers;

import java.util.function.Function;

/**
 * A way of letting modules respond to events without being exposed to actual event code.
 * Modules can pass in the type of an event and the desired parameters, and these will be returned.
 *
 * @since 4.0
 */
public abstract class EventHandler {

    /**
     * Subscribes to a specific event.
     *
     * @param type     the type of the event.
     * @param types    the types of the data you need to respond.
     * @param callback a callback, that gets the data you requested, and returns true if the event should be canceled, false otherwise.
     *                 <b>Data is put into the Object[] array in the same order that you put their types into the Class[] array!</b>
     */
    abstract void subscribe(EventType type, Class<?>[] types, Function<Object[], Boolean> callback);


}
