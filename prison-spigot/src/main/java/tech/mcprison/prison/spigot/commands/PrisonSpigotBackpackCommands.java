package tech.mcprison.prison.spigot.commands;

import org.bukkit.entity.Player;
import tech.mcprison.prison.commands.Command;
import tech.mcprison.prison.internal.CommandSender;
import tech.mcprison.prison.output.Output;
import tech.mcprison.prison.spigot.SpigotPrison;
import tech.mcprison.prison.spigot.backpacks.BackpacksUtil;

public class PrisonSpigotBackpackCommands extends PrisonSpigotBaseCommands {

    @Command(identifier = "backpack", description = "Backpacks", onlyPlayers = false)
    private void backpackMainCommand(CommandSender sender){

        if (sender.hasPermission("prison.admin") || sender.isOp()){

            sender.dispatchCommand("backpack help");
            return;
        }

        sender.dispatchCommand("gui backpack");
    }

    @Command(identifier = "backpack item", description = "Item to open backpack on right click", onlyPlayers = true)
    private void backpackItemGive(CommandSender sender){

        Player p = getSpigotPlayer(sender);

        if (p == null) {
            Output.get().sendInfo(sender, SpigotPrison.format( getMessages().getString("Message.CantGiveItemFromConsole")));
            return;
        }

        BackpacksUtil.get().giveBackpackToPlayer(p);
    }

}
