package tech.mcprison.prison.convert;

/**
 * Contains information on the result of the conversion.
 *
 * @author Faizaan A. Datoo
 * @since API 1.0
 */
public class ConversionResult {

    private String agentName;
    private Status status;
    private String reason;

    public ConversionResult(String agentName, Status status, String reason) {
        this.agentName = agentName;
        this.status = status;
        this.reason = reason;
    }

    public static ConversionResult success(String agentName) {
        return new ConversionResult(agentName, Status.Success, "OK");
    }

    public static ConversionResult failure(String agentName, String reason) {
        return new ConversionResult(agentName, Status.Failure, reason);
    }

    public String getAgentName() {
        return agentName;
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
