package tech.mcprison.prison.alerts;

import tech.mcprison.prison.Prison;
import tech.mcprison.prison.commands.Command;
import tech.mcprison.prison.internal.CommandSender;
import tech.mcprison.prison.internal.Player;
import tech.mcprison.prison.localization.Localizable;
import tech.mcprison.prison.output.BulletedListComponent;
import tech.mcprison.prison.output.ChatDisplay;
import tech.mcprison.prison.output.Output;

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
