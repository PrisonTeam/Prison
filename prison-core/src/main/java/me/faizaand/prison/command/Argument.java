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
    private Class<?> type;
    private Predicate<String> validator;

    public static Argument newRequiredArg(String name, String description, Class<?> type, Predicate<String> validator) {
        return new Argument(name, true, description, type, validator);
    }

    public static Argument newOptionalArg(String name, String description, Class<?> type, Predicate<String> validator) {
        return new Argument(name, false, description, type, validator);
    }

    public Argument(String name, boolean required, String description, Class<?> type, Predicate<String> validator) {
        this.name = name;
        this.required = required;
        this.description = description;
        this.type = type;
        this.validator = validator;
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

    public Class<?> getType() {
        return type;
    }

    public Predicate<String> getValidator() {
        return validator;
    }
}
