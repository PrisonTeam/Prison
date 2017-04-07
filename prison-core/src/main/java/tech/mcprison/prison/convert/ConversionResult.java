package tech.mcprison.prison.convert;

/**
 * Contains information on the result of the conversion.
 *
 * @author Faizaan A. Datoo
 * @since API 1.0
 */
public class ConversionResult {

    private Status status;
    private String reason;

    public ConversionResult(Status status, String reason) {
        this.status = status;
        this.reason = reason;
    }

    public static ConversionResult success() {
        return new ConversionResult(Status.Success, "OK");
    }

    public static ConversionResult failure(String reason) {
        return new ConversionResult(Status.Failure, reason);
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public enum Status {

        Success, Failure;

    }

}
