package me.faizaand.prison.command;

import java.util.function.Predicate;

/**
 * Represents a command argument.
 *
 * @since API 4.0
 */
public class Argument {

    private String name;
    private boolean required = false;
    private String description;

    public static Argument newRequiredArg(String name, String description) {
        return new Argument(name, true, description);
    }

    public static Argument newOptionalArg(String name, String description) {
        return new Argument(name, false, description);
    }

    public Argument(String name, boolean required, String description) {
        this.name = name;
        this.required = required;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public boolean isRequired() {
        return required;
    }

    public String getDescription() {
        return description;
    }

}
