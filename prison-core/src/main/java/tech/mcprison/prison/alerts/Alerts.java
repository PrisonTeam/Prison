package tech.mcprison.prison.alerts;

import com.google.common.eventbus.Subscribe;
import tech.mcprison.prison.Prison;
import tech.mcprison.prison.internal.Player;
import tech.mcprison.prison.internal.events.player.PlayerJoinEvent;
import tech.mcprison.prison.output.Output;

import java.util.ArrayList;
import java.util.List;
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

    /*
     * Constructor
     */

    private Alerts() {
        alerts = new ArrayList<>();

        new AlertCommands();
        Prison.get().getEventBus().register(this);
    }

    /*
     * Methods
     */

    public static Alerts getInstance() {
        if (instance == null) {
            instance = new Alerts();
        }
        return instance;
    }

    public void sendAlert(String alertMsg, Object... format) {
        String msg = String.format(alertMsg, format);
        Output.get().logInfo(msg);

        Alert alert = new Alert(alerts.size(), msg);
        alerts.add(alert);

        for (Player player : Prison.get().getPlatform().getOnlinePlayers()) {
            if (player.hasPermission("prison.alerts")) {
                Output.get().sendInfo(player,
                        "You have a new alert from Prison. &3Type /prison alerts to read it.");
            }
        }
    }

    public void clearOne(int id, UUID uid) {
        Alert alert = alerts.stream().filter(a -> a.id == id).findFirst().orElse(null);
        if (alert != null) {
            if (alert.readBy == null) {
                alert.readBy = new ArrayList<>();
            }
            alert.readBy.add(uid);
        }
    }

    public void clearAll() {
        alerts.clear();
    }

    /*
     * Listeners
     */

    public void showAlerts(Player player) {
        int alerts = Alerts.getInstance().getAlertsFor(player.getUUID()).size();
        if (alerts > 0) {
            Output.get().sendInfo(player,
                    "You have %d unread alerts from Prison. &3Type /prison alerts to read them.",
                    alerts);
        }
    }

    /*
     * Getters
     */

    @Subscribe
    public void onPlayerJoin(PlayerJoinEvent e) {
        if (e.getPlayer().hasPermission("prison.admin")) {
            // He should see alerts
            showAlerts(e.getPlayer());
        }
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
