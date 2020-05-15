package tech.mcprison.prison.spigot.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import tech.mcprison.prison.spigot.SpigotPrison;

public class PrestigesPrestigeCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (!(sender instanceof Player || sender instanceof tech.mcprison.prison.internal.Player)) {
            sender.sendMessage(SpigotPrison.format("&cFor some reasons, it looks like you aren't a player"));
            return true;
        }

        if (sender.hasPermission("prison.prestige")){
            Bukkit.dispatchCommand(sender, "rankup prestiges");
        }

        return true;
    }
}
