package tech.mcprison.prison.spigot.commands;

import tech.mcprison.prison.commands.Command;
import tech.mcprison.prison.internal.CommandSender;

public class PrisonSpigotBackPacksCommands extends PrisonSpigotBaseCommands{

    @Command(identifier = "backpack", description = "Backpacks", onlyPlayers = false)
    private void sellAllGuiCommandNew(CommandSender sender){

        if (sender.hasPermission("prison.admin") || sender.isOp()){

            sender.dispatchCommand("backpack help");
            return;
        }

        sender.dispatchCommand("gui backpack");
    }
}
