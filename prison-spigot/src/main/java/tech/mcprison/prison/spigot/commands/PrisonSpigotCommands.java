package tech.mcprison.prison.spigot.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import tech.mcprison.prison.spigot.SpigotPrison;
import tech.mcprison.prison.spigot.gui.SpigotPrisonGUI;

/**
 * @author GABRYCA
 */
public class PrisonSpigotCommands implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        // Get the version
        String versionBukkit = Bukkit.getVersion();
        // Don't open with these versions
        if (versionBukkit.contains("1.8.1") || versionBukkit.contains("1.8.2") || versionBukkit.contains("1.8.3") || versionBukkit.contains("1.8.4") || versionBukkit.contains("1.8.5") || versionBukkit.contains("1.8.6") || versionBukkit.contains("1.8.7") || versionBukkit.contains("1.8.8") || versionBukkit.contains("1.8.9") || versionBukkit.contains("1.8") || versionBukkit.contains("1.8.0")) {
            sender.sendMessage(SpigotPrison.format("&cSorry, but GUIs don't works with 1.8.9 or older version due to issues"));
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
        if (!(sender instanceof Player)) {
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
