/*
 * Prison is a Minecraft plugin for the prison game mode.
 * Copyright (C) 2018 The Prison Team
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package xyz.faizaan.prison.alerts;

import xyz.faizaan.prison.Prison;
import xyz.faizaan.prison.commands.Command;
import xyz.faizaan.prison.internal.CommandSender;
import xyz.faizaan.prison.internal.Player;
import xyz.faizaan.prison.localization.Localizable;
import xyz.faizaan.prison.output.BulletedListComponent;
import xyz.faizaan.prison.output.ChatDisplay;
import xyz.faizaan.prison.output.Output;

import java.util.List;

/**
 * @author Faizaan A. Datoo
 */
public class AlertCommands {

    public AlertCommands() {
        Prison.get().getCommandHandler().registerCommands(this);
    }

    @Command(identifier = "prison alerts", description = "Lists your alerts.", permissions = "prison.alerts")
    public void prisonAlertsCommand(CommandSender sender) {
        if (!(sender instanceof Player)) {
            Prison.get().getLocaleManager().getLocalizable("cantAsConsole")
                .sendTo(sender, Localizable.Level.ERROR);
            return;
        }
        Player player = (Player) sender;

        ChatDisplay display = new ChatDisplay("Alerts");

        List<Alert> alerts = Alerts.getInstance().getAlertsFor(player.getUUID());
        if (alerts.size() == 0) {
            Output.get().sendInfo(player, "You have no alerts.");
            return;
        }

        BulletedListComponent.BulletedListBuilder builder =
            new BulletedListComponent.BulletedListBuilder();
        alerts.forEach(alert -> builder.add(alert.message));

        display.addComponent(builder.build());
        display.text("&8Type /prison alerts clear to clear your alerts.");
        display.text("&8Type /prison alerts clearall to clear everyone's alerts.");

        display.send(player);
    }

    @Command(identifier = "prison alerts clear", description = "Clears your alerts.", permissions = "prison.alerts.clear")
    public void prisonAlertsClearCommand(CommandSender sender) {
        if (!(sender instanceof Player)) {
            Prison.get().getLocaleManager().getLocalizable("cantAsConsole")
                .sendTo(sender, Localizable.Level.ERROR);
            return;
        }
        Player player = (Player) sender;

        List<Alert> alerts = Alerts.getInstance().getAlertsFor(player.getUUID());
        if (alerts.size() == 0) {
            Output.get().sendInfo(player, "You have no alerts.");
            return;
        }

        alerts.forEach(alert -> Alerts.getInstance().clearOne(alert.id, player.getUUID()));
        Output.get().sendInfo(player, "Your alerts have been cleared.");
    }

    @Command(identifier = "prison alerts clearall", description = "Clears the alerts for the whole server.", permissions = "prison.alerts.clear.all")
    public void prisonAlertsClearAllCommand(CommandSender sender) {
        if (!(sender instanceof Player)) {
            Prison.get().getLocaleManager().getLocalizable("cantAsConsole")
                .sendTo(sender, Localizable.Level.ERROR);
            return;
        }
        Player player = (Player) sender;

        Alerts.getInstance().clearAll();
        Output.get().sendInfo(player, "All alerts have been cleared.");
    }

}
