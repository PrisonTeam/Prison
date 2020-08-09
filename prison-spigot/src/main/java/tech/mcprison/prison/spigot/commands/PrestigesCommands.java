package tech.mcprison.prison.spigot.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import tech.mcprison.prison.ranks.PrisonRanks;
import tech.mcprison.prison.spigot.SpigotPrison;
import tech.mcprison.prison.spigot.gui.rank.SpigotPlayerPrestigesGUI;

import java.util.Objects;

/**
 * @author GABRYCA
 */
public class PrestigesCommands implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (!(Objects.requireNonNull(SpigotPrison.getInstance().getConfig().getString("prestiges")).equalsIgnoreCase("true"))) {
            sender.sendMessage(SpigotPrison.format("&cPrestiges are disabled by default, please edit it in your config.yml!"));
            return true;
        }

        if (!(PrisonRanks.getInstance().getLadderManager().getLadder("prestiges").isPresent())) {
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "ranks ladder create prestiges");
        }

        Player p = null;
        if (sender instanceof Player){
            p = (Player) sender;
        }

        if (p != null) {
            Bukkit.dispatchCommand(p, "prisonmanager prestiges");
        } else {
            Bukkit.dispatchCommand(sender, "ranks list prestiges");
        }

        return true;
    }
}
