package tech.mcprison.prison.alerts;

import com.google.common.eventbus.Subscribe;
import tech.mcprison.prison.Prison;
import tech.mcprison.prison.internal.events.player.PlayerJoinEvent;
import tech.mcprison.prison.output.Output;
import tech.mcprison.prison.store.Collection;
import tech.mcprison.prison.store.Document;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * A system for notifying server operators when there is something to be brought to their attention.
 * This is a singleton, so access it with {@link Alerts#getInstance()}.
 *
 * @author Faizaan A. Datoo
 * @since API 1.0
 */
public class Alerts {

    /*
     * Variables & Constants
     */

    private static Alerts instance;
    private List<Alert> alerts;
    private Collection alertsColl;

    /*
     * Constructor
     */

    private Alerts() {
        alertsColl = initAlertsColl();
        alerts = new ArrayList<>();

        for (Document d : alertsColl.getAll()) {
            alerts.add(new Alert(d));
        }

        new AlertCommands();
        Prison.get().getEventBus().register(this);
    }

    private Collection initAlertsColl() {
        Optional<Collection> collectionOptional =
            Prison.get().getMetaDatabase().getCollection("alerts");
        if (!collectionOptional.isPresent()) {
            Prison.get().getMetaDatabase().createCollection("alerts");
            collectionOptional = Prison.get().getMetaDatabase().getCollection("alerts");
        }

        return collectionOptional.orElseThrow(RuntimeException::new);
    }

    /*
     * Methods
     */

    public void sendAlert(String alertMsg, Object... format) {
        String msg = String.format(alertMsg, format);
        Alert alert = new Alert(alerts.size(), msg);
        alertsColl.insert(Integer.toString(alert.id), alert.toDocument());
    }

    public void clearOne(int id, UUID uid) {
        Alert alert = alerts.stream().filter(a -> a.id == id).findFirst().orElse(null);
        if (alert != null) {
            if (alert.readBy == null) {
                alert.readBy = new ArrayList<>();
            }
            alert.readBy.add(uid);
            alertsColl.insert(String.valueOf(alert.id), alert.toDocument());
        }
    }

    public void clearAll() {
        alerts.clear();
        Prison.get().getMetaDatabase().deleteCollection("alerts");
        alertsColl = initAlertsColl();
    }

    /*
     * Listeners
     */

    @Subscribe public void onPlayerJoin(PlayerJoinEvent e) {
        if (e.getPlayer().hasPermission("prison.admin")) {
            // He should see alerts
            int alerts = Alerts.getInstance().getAlertsFor(e.getPlayer().getUUID()).size();
            if (alerts > 0) {
                Output.get().sendInfo(e.getPlayer(),
                    "You have %d unread alerts from Prison. &3Type /prison alerts to read them.",
                    alerts);
            }
        }
    }

    /*
     * Getters
     */

    public static Alerts getInstance() {
        if (instance == null) {
            instance = new Alerts();
        }
        return instance;
    }

    public List<Alert> getAlertsFor(UUID uid) {
        List<Alert> ret = new ArrayList<>();

        for (Alert alert : alerts) {
            if (!alert.readBy.contains(uid)) {
                ret.add(alert);
            }
        }

        return ret;
    }

}
