package tech.mcprison.prison.spigot.commands;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import tech.mcprison.prison.Prison;
import tech.mcprison.prison.commands.Arg;
import tech.mcprison.prison.commands.Command;
import tech.mcprison.prison.internal.CommandSender;
import tech.mcprison.prison.output.Output;
import tech.mcprison.prison.spigot.SpigotPrison;
import tech.mcprison.prison.spigot.backpacks.BackpacksUtil;

import java.util.List;

/**
 * @author GABRYCA
 * */
public class PrisonSpigotBackpackCommands extends PrisonSpigotBaseCommands {

    @Command(identifier = "backpack", description = "Backpacks", onlyPlayers = false)
    private void backpackMainCommand(CommandSender sender,
    @Arg(name = "ID", def = "null", description = "Leave empty if you want to open your main backpack, add an ID if you've more than one.") String id){

        if (sender.hasPermission("prison.admin") || sender.isOp()){
        	String registeredCmd = Prison.get().getCommandHandler().findRegisteredCommand("backpack help");
            sender.dispatchCommand(registeredCmd);
            return;
        }

        if (!getBoolean(BackpacksUtil.get().getBackpacksConfig().getString("Options.Multiple-BackPacks-For-Player-Enabled"))) {
            sender.dispatchCommand("gui backpack");
        } else if (id.equalsIgnoreCase("null")){
            sender.dispatchCommand("gui backpackslist");
        } else {
            sender.dispatchCommand("gui backpack " + id);
        }
    }

    @Command(identifier = "backpack item", description = "Item to open backpack on right click", onlyPlayers = true)
    private void backpackItemGive(CommandSender sender){

        Player p = getSpigotPlayer(sender);

        if (p == null) {
            Output.get().sendInfo(sender, SpigotPrison.format( getMessages().getString("Message.CantGiveItemFromConsole")));
            return;
        }

        if (isDisabledWorld(p)) return;

        BackpacksUtil.get().giveBackpackToPlayer(p);
    }

    @Command(identifier = "backpack list", description = "Open backpacks list if multi-backpacks's enabled.", onlyPlayers = true)
    private void backpacksListCommand(CommandSender sender){

        Player p = getSpigotPlayer(sender);

        if (p == null) {
            Output.get().sendInfo(sender, SpigotPrison.format( getMessages().getString("Message.CantGiveItemFromConsole")));
            return;
        }

        if (isDisabledWorld(p)) return;

        if (getBoolean(BackpacksUtil.get().getBackpacksConfig().getString("Options.Multiple-BackPacks-For-Player-Enabled"))) {
            sender.dispatchCommand("gui backpackslist");
        }
    }

    @Command(identifier = "backpack delete", description = "Delete a player's backpack.", permissions = "prison.admin", onlyPlayers = false)
    private void deleteBackpackCommand(CommandSender sender,
    @Arg(name = "Backpack Owner", description = "The backpack owner name", def = "null") String name,
                                       @Arg(name = "id", description = "The backpack ID optional", def = "null") String id){

        if (name.equalsIgnoreCase("null")){
            Output.get().sendWarn(sender, SpigotPrison.format(getMessages().getString("Message.BackPackNeedPlayer")));
            return;
        }

        boolean success;
        boolean isOnlinePlayer = Bukkit.getPlayerExact(name) != null;
        if (id.equalsIgnoreCase("null")) {
            if (isOnlinePlayer) {
                success = BackpacksUtil.get().resetBackpack(Bukkit.getPlayerExact(name));
            } else {
                success = BackpacksUtil.get().resetBackpack(BackpacksUtil.get().getBackpackOwnerOffline(name));
            }
        } else {
            if (isOnlinePlayer) {
                success = BackpacksUtil.get().resetBackpack(Bukkit.getPlayerExact(name), id);
            } else {
                success = BackpacksUtil.get().resetBackpack(BackpacksUtil.get().getBackpackOwnerOffline(name, id), id);
            }
        }
        if (success) {
            Output.get().sendInfo(sender, SpigotPrison.format(getMessages().getString("Message.BackPackDeleteOperationSuccess")));
        } else {
            Output.get().sendWarn(sender, SpigotPrison.format(getMessages().getString("Message.BackPackDeleteOperationFail")));
        }
    }

    @Command(identifier = "backpack set size", description = "Resize a player's backpack.", permissions = "prison.admin", onlyPlayers = false)
    private void resizeBackpackCommand(CommandSender sender,
                                       @Arg(name = "Backpack Owner", description = "The backpack owner name", def = "null") String name,
                                       @Arg(name = "Backpack size", description = "Backpack size multiple of 9", def = "9") String size,
                                       @Arg(name = "id", description = "The backpack ID optional", def = "null") String id){

        if (name.equalsIgnoreCase("null")){
            Output.get().sendWarn(sender, SpigotPrison.format(getMessages().getString("Message.BackPackNeedPlayer")));
            return;
        }

        int sizeInt;
        try{
            sizeInt = Integer.parseInt(size);
        } catch (NumberFormatException ex){
            Output.get().sendWarn(sender, SpigotPrison.format(getMessages().getString("Message.BackPackResizeNotInt")));
            return;
        }

        // Must be multiple of 9.
        if (sizeInt % 9 != 0 || sizeInt > 54){
            Output.get().sendWarn(sender, SpigotPrison.format(getMessages().getString("Message.BackPackResizeNotMultiple9")));
            return;
        }

        boolean isOnlinePlayer = Bukkit.getPlayerExact(name) != null;
        if (id.equalsIgnoreCase("null")) {
            if (isOnlinePlayer) {

                BackpacksUtil.get().setBackpackSize(Bukkit.getPlayerExact(name), sizeInt);

            } else {

                BackpacksUtil.get().setBackpackSize(BackpacksUtil.get().getBackpackOwnerOffline(name), sizeInt);

            }
        } else {
            if (isOnlinePlayer){

                BackpacksUtil.get().setBackpackSize(Bukkit.getPlayerExact(name), sizeInt, id);

            } else {

                BackpacksUtil.get().setBackpackSize(BackpacksUtil.get().getBackpackOwnerOffline(name, id), sizeInt, id);

            }
        }

        Output.get().sendInfo(sender, SpigotPrison.format(getMessages().getString("Message.BackPackResizeDone")));
    }

    private boolean isDisabledWorld(Player p) {
        String worldName = p.getWorld().getName();
        List<String> disabledWorlds = BackpacksUtil.get().getBackpacksConfig().getStringList("Options.DisabledWorlds");
        return disabledWorlds.contains(worldName);
    }
}
