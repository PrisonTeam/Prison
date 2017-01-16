/*
 *  Prison is a Minecraft plugin for the prison game mode.
 *  Copyright (C) 2017 The Prison Team
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package tech.mcprison.prison.output;

import com.google.common.eventbus.Subscribe;
import tech.mcprison.prison.Prison;
import tech.mcprison.prison.events.PlayerJoinEvent;
import tech.mcprison.prison.util.Text;

import java.util.ArrayList;
import java.util.List;

/**
 * The alerts system ensures server operators see important Prison messages when they log on.
 *
 * @author Faizaan A. Datoo
 * @since API 1.0
 * @deprecated Doesn't work as planned, will be revisited in future API revisions.
 */
@Deprecated
public class Alert {

    // Fields

    private static Alert instance;
    private List<String> alerts;

    // Constructor

    private Alert() {
        instance = this;
        this.alerts = new ArrayList<>();
        Prison.get().getEventBus().register(this);
    }

    // Methods

    /**
     * Adds an alert to the list.
     *
     * @param alert The alert text.
     */
    public void addAlert(String alert, Object... args) {
        alerts.add(Text.translateAmpColorCodes(String.format("&c&lAlert: &r&7" + alert, args)));
    }

    public List<String> listAlerts() {
        return alerts;
    }

    /**
     * Clear all alerts
     */
    public void clearAlerts() {
        alerts.clear();
    }

    // Listeners

    @Subscribe public void onPlayerJoin(PlayerJoinEvent e) {
        if (alerts.size() > 0) {
            if (e.getPlayer().hasPermission("prison.alerts")) {
                e.getPlayer().sendMessage(
                    String.format(Prison.get().getMessages().alertsPresent, alerts.size()));
            }
        }
    }

    // Getters

    public static Alert get() {
        return instance;
    }

}
