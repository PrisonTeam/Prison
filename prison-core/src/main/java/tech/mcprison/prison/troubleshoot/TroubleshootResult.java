package tech.mcprison.prison.troubleshoot;

/**
 * A TroubleshootResult is given to a user when the {@link Troubleshooter} has completed execution.
 * This could be due to success, requiring further action, or complete failure.
 *
 * @author Faizaan A. Datoo
 * @since API 1.0
 */
public class TroubleshootResult {

    private Result result;
    private String description;

    public TroubleshootResult(Result result, String description) {
        this.result = result;
        this.description = description;
    }

    public Result getResult() {
        return result;
    }

    public String getDescription() {
        return description;
    }

    public enum Result {
        SUCCESS, USER_ACTION, FAILURE
    }


}
