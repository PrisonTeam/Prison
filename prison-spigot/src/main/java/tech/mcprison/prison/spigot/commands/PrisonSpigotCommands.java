package tech.mcprison.prison.spigot.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import tech.mcprison.prison.spigot.SpigotPrison;
import tech.mcprison.prison.spigot.gui.SpigotPrisonGUI;
import tech.mcprison.prison.spigot.spiget.BluesSemanticVersionData;

/**
 * @author GABRYCA
 */
public class PrisonSpigotCommands implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        String ver = Bukkit.getVersion().trim();
        ver = ver.substring( ver.indexOf("(MC: ") + 5, ver.length() -1 );
        BluesSemanticVersionData semVerMin = new BluesSemanticVersionData("1.9.0");
        BluesSemanticVersionData semVerTest = new BluesSemanticVersionData(ver);
        if ( semVerTest.compareTo(semVerMin ) < 0 ) {
            sender.sendMessage(SpigotPrison.format("&cSorry, but GUIs don't work with versions prior to 1.9.0 due to issues"));
            return true;
        }

        if (!(sender.hasPermission("prison.admin") || sender.hasPermission("prison.prisonmanagergui"))) {
            sender.sendMessage(SpigotPrison.format("&cSorry, but you don't have the permission &1[&c-Prison.admin &1or &c-Prison.prisonmanagergui"));
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

        if (args[0].equalsIgnoreCase("gui")) {
            Player p = (Player) sender;
            SpigotPrisonGUI gui = new SpigotPrisonGUI(p);
            gui.open();
        }

        return true;

    }
}
