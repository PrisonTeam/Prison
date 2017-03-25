package tech.mcprison.prison.error;

import org.apache.commons.lang3.exception.ExceptionUtils;

/**
 * Represents a single stack trace and a description.
 *
 * @author Faizaan A. Datoo
 * @since API 1.0
 */
public class ErrorStackTrace {

    private Throwable throwable;
    private String description;

    public ErrorStackTrace(Throwable throwable, String description) {
        this.throwable = throwable;
        this.description = description;
    }

    public Throwable getThrowable() {
        return throwable;
    }

    public void setThrowable(Throwable throwable) {
        this.throwable = throwable;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override public String toString() {
        String exceptionText = ExceptionUtils.getStackTrace(throwable);
        return ("Description: " + description + "\n") + "Stack trace:\n" + exceptionText;
    }
}
