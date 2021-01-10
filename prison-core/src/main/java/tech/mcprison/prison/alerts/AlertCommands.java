package tech.mcprison.prison.alerts;

import java.util.ArrayList;
import java.util.List;

import tech.mcprison.prison.Prison;
import tech.mcprison.prison.commands.Command;
import tech.mcprison.prison.internal.CommandSender;
import tech.mcprison.prison.internal.Player;
import tech.mcprison.prison.output.BulletedListComponent;
import tech.mcprison.prison.output.ChatDisplay;
import tech.mcprison.prison.output.Output;

/**
 * @author Faizaan A. Datoo
 */
public class AlertCommands {

    public AlertCommands() {
        Prison.get().getCommandHandler().registerCommands(this);
    }

    @Command(identifier = "prison alerts", description = "Lists your alerts.", 
    		permissions = "prison.alerts", onlyPlayers = false )
    public void prisonAlertsCommand(CommandSender sender) {
//        if (!(sender instanceof Player)) {
//            Prison.get().getLocaleManager().getLocalizable("cantAsConsole")
//                .sendTo(sender, LogLevel.ERROR);
//            return;
//        }
    	
    	List<Alert> alerts = new ArrayList<>();

    	ChatDisplay display = new ChatDisplay("Alerts");

    	BulletedListComponent.BulletedListBuilder builder =
    			new BulletedListComponent.BulletedListBuilder();


        if ((sender instanceof Player)) {
        	Player player = (Player) sender;
        	
        	alerts = Alerts.getInstance().getAlertsFor(player.getUUID());
        }

        alerts.forEach(alert -> builder.add(alert.message));

        display.addComponent(builder.build());
        if (alerts.size() == 0) {
        	Output.get().sendInfo(sender, "There are no alerts.");
        }
        else {
        	
        	display.addText("&8Type /prison alerts clear to clear your alerts.");
        	display.addText("&8Type /prison alerts clearall to clear everyone's alerts.");
        	
        	display.send(sender);
        }
    }

    @Command(identifier = "prison alerts clear", description = "Clears your alerts.", 
    		permissions = "prison.alerts.clear", onlyPlayers = false )
    public void prisonAlertsClearCommand(CommandSender sender) {
        if (!(sender instanceof Player)) {

        	// If console, then clear all since there is no "Player" to use:
        	prisonAlertsClearAllCommand( sender );
//            Prison.get().getLocaleManager().getLocalizable("cantAsConsole")
//                .sendTo(sender, LogLevel.ERROR);
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

    @Command(identifier = "prison alerts clearall", 
    		description = "Clears the alerts for the whole server.", 
    		permissions = "prison.alerts.clear.all", onlyPlayers = false )
    public void prisonAlertsClearAllCommand(CommandSender sender) {
//        if (!(sender instanceof Player)) {
//            Prison.get().getLocaleManager().getLocalizable("cantAsConsole")
//                .sendTo(sender, LogLevel.ERROR);
//            return;
//        }
//        Player player = (Player) sender;

        Alerts.getInstance().clearAll();
        Output.get().sendInfo(sender, "All alerts have been cleared.");
    }

}
