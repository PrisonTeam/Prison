package tech.mcprison.prison.error;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a single error, and all the data attached.
 *
 * @author Faizaan A. Datoo
 * @since API 1.0
 */
public class Error {

    private String description;
    private List<ErrorStackTrace> stackTraces;

    public Error(String description) {
        this.description = description;
        this.stackTraces = new ArrayList<>();
    }

    public Error appendStackTrace(String desc, Throwable t) {
        this.stackTraces.add(new ErrorStackTrace(t, desc));
        return this;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<ErrorStackTrace> getStackTraces() {
        return stackTraces;
    }
}
