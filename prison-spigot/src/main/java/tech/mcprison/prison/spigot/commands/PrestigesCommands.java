package tech.mcprison.prison.spigot.commands;

import org.apache.commons.lang3.ArrayUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import tech.mcprison.prison.spigot.SpigotPrison;

public class PrestigesCommands implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        // Check parameters
        if (args.length == 0) {

            if (sender.hasPermission("prison.admin")) {
                sender.sendMessage(SpigotPrison.format("&8---------------------------"));
                sender.sendMessage(SpigotPrison.format("     &3Prestiges commands:"));
                sender.sendMessage(SpigotPrison.format("&8---------------------------"));
                sender.sendMessage(SpigotPrison.format("&3/prestige  &8&l| &3Prestige, permission prison.prestige."));
                sender.sendMessage(SpigotPrison.format("&3/prestiges  &8&l| &3Get a list of prestiges commands."));
                sender.sendMessage(SpigotPrison.format("&3/prestiges template <ladder> <commandToAdd> <commandToRemove> "));
                sender.sendMessage(SpigotPrison.format("&8&l| &3Make the prestiges ladder."));
                sender.sendMessage(SpigotPrison.format("Replace &1<ladder> &3with the name of the ladder where to check if the player's at the last rank"));
                sender.sendMessage(SpigotPrison.format("Also add the rankupCommand to the last rank of the ladder selected to give the permission -prison.prestige-. "));
                sender.sendMessage(SpigotPrison.format("Put instead of &1<commandToAdd> &3the command to &a&lADD &3to a player the permission, this &cdepends by your permission plugin. "));
                sender.sendMessage(SpigotPrison.format("&3Do the same but to &c&lREMOVE &3the permission instead of the &1<commandToRemove>&3."));

            } else {
                sender.sendMessage(SpigotPrison.format("&8---------------------------"));
                sender.sendMessage(SpigotPrison.format("     &3Prestiges commands:"));
                sender.sendMessage(SpigotPrison.format("&8---------------------------"));
                sender.sendMessage(SpigotPrison.format("&3/prestige     &8&l| &3Prestige, you can execute it if you're at the last rank and there're prestiges in the server."));
            }

            return true;
        }

        String param = args[0];
        int NotNull = 0;
        while (NotNull != args.length - 1){
            args[NotNull] = args[NotNull + 1];
            NotNull++;
        }

        args = (String[]) ArrayUtils.removeElement(args, args[NotNull]);

        if (param.equalsIgnoreCase("template")){
            return PrestigesTemplateCommand.onCommand(sender, args);
        }

        return true;
    }
}
