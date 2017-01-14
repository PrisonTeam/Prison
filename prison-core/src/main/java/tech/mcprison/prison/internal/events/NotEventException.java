package tech.mcprison.prison.internal.events;

/**
 * Created by DMP9 on 14/01/2017.
 */
public class NotEventException extends Exception {
    public NotEventException(){
        super("The class provided couldn't be passed as an event because it doesn't have the Event annotation");
    }
}
