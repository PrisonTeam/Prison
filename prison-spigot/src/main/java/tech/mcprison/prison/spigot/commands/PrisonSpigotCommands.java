package tech.mcprison.prison.spigot.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import tech.mcprison.prison.Prison;
import tech.mcprison.prison.mines.managers.PlayerManager;
import tech.mcprison.prison.ranks.commands.RankUpCommand;
import tech.mcprison.prison.ranks.data.Rank;
import tech.mcprison.prison.ranks.data.RankLadder;
import tech.mcprison.prison.ranks.data.RankPlayer;
import tech.mcprison.prison.ranks.managers.RankManager;
import tech.mcprison.prison.spigot.SpigotPrison;
import tech.mcprison.prison.spigot.gui.SpigotPrisonGUI;
import tech.mcprison.prison.spigot.gui.mine.SpigotPlayerMinesGUI;
import tech.mcprison.prison.spigot.gui.rank.SpigotPlayerRanksGUI;
import tech.mcprison.prison.spigot.spiget.BluesSpigetSemVerComparator;

import java.util.Map;

/**
 * @author GABRYCA
 * @author RoyalBlueRanger
 */
public class PrisonSpigotCommands implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if ( new BluesSpigetSemVerComparator().compareMCVersionTo("1.9.0") < 0 ) {
            sender.sendMessage(SpigotPrison.format("&cSorry, but GUIs don't work with versions prior to 1.9.0 due to issues"));
            return true;
        }

        if (args.length == 0) {
            sender.sendMessage(SpigotPrison.format("&cIncorrect usage, the command should be /prisonmanager gui"));
            return true;
        }
        if (!(sender instanceof Player || sender instanceof tech.mcprison.prison.internal.Player)) {
            sender.sendMessage(SpigotPrison.format("&cFor some reasons, it looks like you aren't a player"));
            return true;
        }

        if (!(sender.hasPermission("prison.admin") || sender.hasPermission("prison.prisonmanagergui"))) {
            if (args[0].equalsIgnoreCase("ranks")){
                Player p = null;
                if (sender instanceof Player) {
                    p = (Player) sender;
                }

                // tech.mcprison.prison.internal.Player player = (tech.mcprison.prison.internal.Player) sender;

                // RankPlayer rankPlayer = (new RankUpCommand()).getPlayer(player, player.getUUID());

                SpigotPlayerRanksGUI gui = new SpigotPlayerRanksGUI(p /*rankPlayer*/);
                gui.open();
                return true;
            } else if (args[0].equalsIgnoreCase("mines")){
                Player p = null;
                if (sender instanceof Player){
                    p = (Player) sender;
                }

                SpigotPlayerMinesGUI gui = new SpigotPlayerMinesGUI(p);
                gui.open();
                return true;
            }
        } else {
            if (args[0].equalsIgnoreCase("ranks")){
                Player p = null;
                if (sender instanceof Player) {
                    p = (Player) sender;
                }

                // tech.mcprison.prison.internal.Player player = (tech.mcprison.prison.internal.Player) sender;

                // RankPlayer rankPlayer = (new RankUpCommand()).getPlayer(player, player.getUUID());

                SpigotPlayerRanksGUI gui = new SpigotPlayerRanksGUI(p /*rankPlayer*/);
                gui.open();
                return true;
            } else if (args[0].equalsIgnoreCase("mines")){
                Player p = null;
                if (sender instanceof Player){
                    p = (Player) sender;
                }

                SpigotPlayerMinesGUI gui = new SpigotPlayerMinesGUI(p);
                gui.open();
                return true;
            }
            if (args[0].equalsIgnoreCase("gui")) {
                Player p = null;
                if (sender instanceof Player) {
                    p = (Player) sender;
                }
                SpigotPrisonGUI gui = new SpigotPrisonGUI(p);
                gui.open();
                return true;
            }
        }

        return true;

    }
}
