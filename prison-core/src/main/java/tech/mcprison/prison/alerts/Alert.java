package tech.mcprison.prison.alerts;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Represents a single alert.
 *
 * @author Faizaan A. Datoo
 * @since API 1.0
 */
public class Alert {

    public int id;
    public String message;
    public List<UUID> readBy;

    public Alert() {
    }

    public Alert(int id, String message) {
        this.id = id;
        this.message = message;
        this.readBy = new ArrayList<>();
    }

}
