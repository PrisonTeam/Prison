package tech.mcprison.prison.mines.commands;

import tech.mcprison.prison.commands.Arg;
import tech.mcprison.prison.commands.Command;
import tech.mcprison.prison.internal.CommandSender;
import tech.mcprison.prison.internal.Player;

public class PowertoolCommands {
    @Command(identifier = "autosmelt", description = "Enables/disables the autosmelt tool.", permissions = "mines.autosmelt")
    public void autosmeltCommand(CommandSender sender) {
        if (!(sender instanceof Player)){
            sender.sendMessage("");
        }
    }
}
