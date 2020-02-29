package tech.mcprison.prison.spigot.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import tech.mcprison.prison.spigot.gui.SpigotRanksGUI;

public class PrisonSpigotCommands implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (!(sender.hasPermission("prison.admin") || sender.hasPermission("prison.prisonmanagergui"))) {
            sender.sendMessage("§cSorry, but you don't have the permission §1[§c-Prison.admin §1or §c-Prison.prisonmanagergui");
            return true;
        }
            if (args.length == 0) {
                sender.sendMessage("§cIncorrect usage, the command should be /prisonmanager gui");
                return true;
            }
            if (!(sender instanceof Player)) {
                sender.sendMessage("§cYou must be a player to use this command... Hi console!");
                return true;
            }
            Player p = (Player) sender;
            SpigotRanksGUI gui = new SpigotRanksGUI(p);
            gui.open();

        return true;
    }
}
