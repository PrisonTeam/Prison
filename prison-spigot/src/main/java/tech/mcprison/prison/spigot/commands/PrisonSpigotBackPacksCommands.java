package tech.mcprison.prison.spigot.commands;

import org.bukkit.configuration.Configuration;
import org.bukkit.entity.Player;
import tech.mcprison.prison.commands.Command;
import tech.mcprison.prison.internal.CommandSender;
import tech.mcprison.prison.output.Output;
import tech.mcprison.prison.spigot.SpigotPrison;
import tech.mcprison.prison.spigot.backpacks.BackPacksUtil;

public class PrisonSpigotBackPacksCommands extends PrisonSpigotBaseCommands{

    private BackPacksUtil backPacksUtil = BackPacksUtil.get();
    private Configuration messages = SpigotPrison.getInstance().getMessagesConfig();

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

        backPacksUtil.giveBackPackToPlayer(p);
    }

    /*@Command(identifier = "backpack size", description = "Set backpack size of a Player", permissions = "prison.backpack.size",onlyPlayers = false)
    private void backpackSizeCommand(CommandSender sender,
                                     @Arg(def = "null", description = "Playername", name = "Player") String player,
                                     @Arg(def = "54", description = "Size of backpack multiple of 9", name = "Size") int size){

        if(player == null || player.equalsIgnoreCase("null")){
            Output.get().sendWarn(sender, SpigotPrison.format(messages.getString("Message.BackPackNeedPlayer")));
            return;
        }

        Optional<tech.mcprison.prison.internal.Player> oP = Prison.get().getPlatform().getOfflinePlayer(player);
        Player p = null;
        if (oP.isPresent()) {
            p = getSpigotPlayer(oP.get());
        }

        if (p == null){
            Output.get().sendWarn(sender, SpigotPrison.format(messages.getString("Message.BackPackPlayerNotFound")));
            return;
        }

        backPacksUtil.setBackpackSize(p, size);
    }*/
}
