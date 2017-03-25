package tech.mcprison.prison.alerts;

import tech.mcprison.prison.store.Document;

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

    public Alert(Document alert) {
        this.id = Math.toIntExact(Math.round((double) alert.get("id")));
        this.message = (String) alert.get("message");
        List<String> readByStrings = (List<String>) alert.get("readBy");
        this.readBy = new ArrayList<>();
        for (String s : readByStrings) {
            readBy.add(UUID.fromString(s));
        }
    }

    public Document toDocument() {
        Document ret = new Document();
        ret.put("id", id);
        ret.put("message", message);
        List<String> readByStrings = new ArrayList<>();
        for (UUID uid : readBy) {
            readByStrings.add(uid.toString());
        }
        ret.put("readBy", readByStrings);
        return ret;
    }

}
