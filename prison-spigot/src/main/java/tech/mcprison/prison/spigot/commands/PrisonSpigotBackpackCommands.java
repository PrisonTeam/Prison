package tech.mcprison.prison.spigot.commands;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import tech.mcprison.prison.Prison;
import tech.mcprison.prison.cache.PlayerCache;
import tech.mcprison.prison.cache.PlayerCachePlayerData;
import tech.mcprison.prison.commands.Arg;
import tech.mcprison.prison.commands.Command;
import tech.mcprison.prison.internal.CommandSender;
import tech.mcprison.prison.output.Output;
import tech.mcprison.prison.spigot.SpigotPrison;
import tech.mcprison.prison.spigot.backpacks.BackpacksUtil;
import tech.mcprison.prison.spigot.configs.MessagesConfig;
import tech.mcprison.prison.spigot.gui.backpacks.BackpacksAdminGUI;
import tech.mcprison.prison.spigot.gui.backpacks.BackpacksListPlayerGUI;

import java.util.List;

/**
 * @author GABRYCA
 * */
public class PrisonSpigotBackpackCommands extends PrisonSpigotBaseCommands {

    private final MessagesConfig messages = SpigotPrison.getInstance().getMessagesConfig();

    @Command(identifier = "backpack", description = "Backpacks Commands.", onlyPlayers = false)
    private void backpackMainCommand(CommandSender sender,
    @Arg(name = "ID", def = "null", description = "Leave empty if you want to open your main backpack, add an ID if you've more than one.") String id){

        if (sender.hasPermission("prison.admin") || sender.isOp()){
        	String registeredCmd = Prison.get().getCommandHandler().findRegisteredCommand("backpack help");
            sender.dispatchCommand(registeredCmd);
            return;
        }

        if (id.equalsIgnoreCase("null")){
            sender.dispatchCommand("gui backpackslist");
        } else {
            sender.dispatchCommand("gui backpack " + id);
        }
    }

    @Command(identifier = "backpack item", description = "Item to open backpack on right click.", onlyPlayers = true)
    private void backpackItemGive(CommandSender sender){

        Player p = getSpigotPlayer(sender);

        if (p == null) {
            Output.get().sendInfo(sender, SpigotPrison.format(messages.getString(MessagesConfig.StringID.spigot_message_console_error)));
            return;
        }

        if (isDisabledWorld(p)) return;

        BackpacksUtil bUtil = BackpacksUtil.get();
        if (bUtil == null){
            return;
        }

        if (bUtil.giveBackpackOpenItem(p)){
            Output.get().sendInfo(sender, "You got the Backpack open Item!");
        } else {
            Output.get().sendInfo(sender, "You already have the Backpack open Item, you can't own more than one!");
        }
    }

    @Command(identifier = "backpack list", description = "Open backpacks list if multi-backpacks's enabled.", onlyPlayers = true)
    private void backpacksListCommand(CommandSender sender){

        Player p = getSpigotPlayer(sender);

        if (p == null) {
            Output.get().sendInfo(sender, SpigotPrison.format(messages.getString(MessagesConfig.StringID.spigot_message_console_error)));
            return;
        }

        if (isDisabledWorld(p)) return;

        sender.dispatchCommand("gui backpackslist");
    }

    //TODO
    // Offline player can't be edited, should add compatibility in the future.
    @Command(identifier = "backpack delete", description = "Delete a player's backpack.", permissions = "prison.admin", onlyPlayers = false)
    private void deleteBackpackCommand(CommandSender sender,
    @Arg(name = "Owner", description = "The backpack owner name", def = "null") String name,
                                       @Arg(name = "id", description = "The backpack ID optional", def = "null") String id){

        if (name.equalsIgnoreCase("null")){
            Output.get().sendWarn(sender, SpigotPrison.format(messages.getString(MessagesConfig.StringID.spigot_message_backpack_missing_playername)));
            return;
        }

        boolean success = false;
        boolean isOnlinePlayer = Bukkit.getPlayerExact(name) != null;

        BackpacksUtil bUtil = BackpacksUtil.get();

        if (bUtil == null){
            return;
        }

        if (id.equalsIgnoreCase("null")) {
            if (isOnlinePlayer) {
                success = bUtil.resetBackpack(Bukkit.getPlayerExact(name), 0);
            }/* else {
                success = bUtil.resetBackpack(BackpacksUtil.get().getBackpackOwnerOffline(name));
            }*/
        } else {

            int idNumber;
            try{
                idNumber = Integer.parseInt(id);
            } catch (NumberFormatException ignored){
                Output.get().sendWarn(sender, "ID is not a valid number.");
                return;
            }

            if (isOnlinePlayer) {
                success = bUtil.resetBackpack(Bukkit.getPlayerExact(name), idNumber);
            } /*else {
                success = BackpacksUtil.get().resetBackpack(BackpacksUtil.get().getBackpackOwnerOffline(name, id), id);
            }*/
        }
        if (success) {
            Output.get().sendInfo(sender, messages.getString(MessagesConfig.StringID.spigot_message_backpack_delete_success));
        } else {
            Output.get().sendWarn(sender, messages.getString(MessagesConfig.StringID.spigot_message_backpack_delete_error));
        }
    }


    //TODO
    // Method in BackpackUtil to set a Backpack size.
    /*
    @Command(identifier = "backpack set size", description = "Resize a player's backpack.", permissions = "prison.admin", onlyPlayers = false)
    private void resizeBackpackCommand(CommandSender sender,
                                       @Arg(name = "Owner", description = "The backpack owner name", def = "null") String name,
                                       @Arg(name = "Backpack size", description = "Backpack size multiple of 9", def = "9") String size,
                                       @Arg(name = "id", description = "The backpack ID optional", def = "null") String id){

        if (name.equalsIgnoreCase("null")){
            Output.get().sendWarn(sender, SpigotPrison.format(messages.getString(MessagesConfig.StringID.spigot_message_backpack_missing_playername)));
            return;
        }

        int sizeInt;
        try{
            sizeInt = Integer.parseInt(size);
        } catch (NumberFormatException ex){
            Output.get().sendWarn(sender, SpigotPrison.format(messages.getString(MessagesConfig.StringID.spigot_message_backpack_size_must_be_multiple_of_9)));
            return;
        }

        // Must be multiple of 9.
        if ((sizeInt % 9 != 0 || sizeInt > 54) && sizeInt != 0){
            Output.get().sendWarn(sender, SpigotPrison.format(messages.getString(MessagesConfig.StringID.spigot_message_backpack_size_must_be_multiple_of_9)));
            return;
        }

        BackpacksUtil bUtil = BackpacksUtil.get();

        if (bUtil == null){
            return;
        }

        boolean isOnlinePlayer = Bukkit.getPlayerExact(name) != null;
        if (id.equalsIgnoreCase("null")) {
            if (isOnlinePlayer) {

                bUtil.setBackpackSize(Bukkit.getPlayerExact(name), sizeInt, null);

            } /*else {

                bUtil.setBackpackSize(BackpacksUtil.get().getBackpackOwnerOffline(name), sizeInt);

            }*//*
        } else {
            if (isOnlinePlayer){

                bUtil.setBackpackSize(Bukkit.getPlayerExact(name), sizeInt, id);

            } /*else {

                BackpacksUtil.get().setBackpackSize(BackpacksUtil.get().getBackpackOwnerOffline(name, id), sizeInt, id);

            }*//*
        }

        Output.get().sendInfo(sender, SpigotPrison.format(messages.getString(MessagesConfig.StringID.spigot_message_backpack_resize_success)));
    }*/

    @Command(identifier = "backpack limit", permissions = "prison.admin", description = "Backpacks limit for player, to use this multiple backpacks must be enabled from the backpacks config or it won't have effect.", onlyPlayers = false)
    private void backpackLimitMainCommand(CommandSender sender){
        sender.dispatchCommand("backpack limit help");
    }

    //TODO
    // Set how many backpacks can a player own.
    /*
    @Command(identifier = "backpack limit set", permissions = "prison.admin", description = "Set backpacks limit of a player, to use this multiple backpacks must be enabled or it won't have effect.", onlyPlayers = false)
    private void setBackpackLimitCommand(CommandSender sender,
                                         @Arg(name = "Owner", description = "The backpack owner name", def = "null") String name,
                                         @Arg(name = "Limit", description = "The Backpacks limit that a player can own", def = "null") String limit) {

        if (name.equalsIgnoreCase("null") || limit.equalsIgnoreCase("null")){
            Output.get().sendWarn(sender, SpigotPrison.format(messages.getString(MessagesConfig.StringID.spigot_message_command_wrong_format)));
            return;
        }

        int limitInt;
        try{
            limitInt = Integer.parseInt(limit);
        } catch (NumberFormatException ex){
            Output.get().sendWarn(sender, SpigotPrison.format(messages.getString(MessagesConfig.StringID.spigot_message_backpack_limit_not_number)));
            return;
        }

        if (Bukkit.getPlayerExact(name) != null){
            BackpacksUtil.get().setBackpacksLimit(Bukkit.getPlayerExact(name), limitInt);
        } else {
            BackpacksUtil.get().setBackpacksLimit(BackpacksUtil.get().getBackpackOwnerOffline(name), limitInt);
        }

        Output.get().sendInfo(sender, SpigotPrison.format(messages.getString(MessagesConfig.StringID.spigot_message_backpack_limit_edit_success)));
    }

    @Command(identifier = "backpack limit add", permissions = "prison.admin", description = "Increment backpacks limit of a player, multiple backpacks must be enabled or this won't take effect.", onlyPlayers = false)
    private void addBackpackLimitCommand(CommandSender sender,
                                         @Arg(name = "Owner", description = "The backpack owner name", def = "null") String name,
                                         @Arg(name = "Limit", description = "The Backpacks increment value", def = "null") String limit) {

        if (name.equalsIgnoreCase("null") || limit.equalsIgnoreCase("null")){
            Output.get().sendWarn(sender, SpigotPrison.format(messages.getString(MessagesConfig.StringID.spigot_message_command_wrong_format)));
            return;
        }

        int limitInt;
        try{
            limitInt = Integer.parseInt(limit);
        } catch (NumberFormatException ex){
            Output.get().sendWarn(sender, SpigotPrison.format(messages.getString(MessagesConfig.StringID.spigot_message_backpack_limit_not_number)));
            return;
        }

        if (Bukkit.getPlayerExact(name) != null){
            limitInt = BackpacksUtil.get().getBackpacksLimit(Bukkit.getPlayerExact(name)) + limitInt;
            BackpacksUtil.get().setBackpacksLimit(Bukkit.getPlayerExact(name), limitInt);
        } else {
            limitInt = BackpacksUtil.get().getBackpacksLimit(BackpacksUtil.get().getBackpackOwnerOffline(name)) + limitInt;
            BackpacksUtil.get().setBackpacksLimit(BackpacksUtil.get().getBackpackOwnerOffline(name), limitInt);
        }

        Output.get().sendInfo(sender, SpigotPrison.format(messages.getString(MessagesConfig.StringID.spigot_message_backpack_limit_edit_success)));
    }*/

    //TODO
    // Same as the previous command.
    /*
    @Command(identifier = "backpack limit decrement", permissions = "prison.admin", description = "Decrement backpacks limit of a player, to use this multiple backpacks must be enabled or it won't have effect.", onlyPlayers = false)
    private void decrementBackpackLimitCommand(CommandSender sender,
                                         @Arg(name = "Owner", description = "The backpack owner name", def = "null") String name,
                                         @Arg(name = "Value", description = "The Backpacks decrement value", def = "null") String limit) {

        if (name.equalsIgnoreCase("null") || limit.equalsIgnoreCase("null")){
            Output.get().sendWarn(sender, SpigotPrison.format(messages.getString(MessagesConfig.StringID.spigot_message_command_wrong_format)));
            return;
        }

        int limitInt;
        try{
            limitInt = Integer.parseInt(limit);
        } catch (NumberFormatException ex){
            Output.get().sendWarn(sender, SpigotPrison.format(messages.getString(MessagesConfig.StringID.spigot_message_backpack_limit_not_number)));
            return;
        }

        if (Bukkit.getPlayerExact(name) != null){
            limitInt = BackpacksUtil.get().getBackpacksLimit(Bukkit.getPlayerExact(name)) - limitInt;
            if (limitInt >= 0) {
                BackpacksUtil.get().setBackpacksLimit(Bukkit.getPlayerExact(name), limitInt);
            } else {
                Output.get().sendWarn(sender, SpigotPrison.format(messages.getString(MessagesConfig.StringID.spigot_message_backpack_limit_decrement_fail)));
                return;
            }
        } else {
            limitInt = BackpacksUtil.get().getBackpacksLimit(BackpacksUtil.get().getBackpackOwnerOffline(name)) - limitInt;
            if (limitInt >= 0) {
                BackpacksUtil.get().setBackpacksLimit(BackpacksUtil.get().getBackpackOwnerOffline(name), limitInt);
            } else {
                Output.get().sendWarn(sender, SpigotPrison.format(messages.getString(MessagesConfig.StringID.spigot_message_backpack_limit_decrement_fail)));
                return;
            }
        }

        Output.get().sendInfo(sender, SpigotPrison.format(messages.getString(MessagesConfig.StringID.spigot_message_backpack_limit_edit_success)));
    }*/

    @Command(identifier = "backpack admin", description = "Open backpack admin GUI", permissions = "prison.admin", onlyPlayers = true)
    private void openBackpackAdminGUI(CommandSender sender){

        Player p = getSpigotPlayer(sender);

        if (p == null) {
            Output.get().sendInfo(sender, SpigotPrison.format( messages.getString(MessagesConfig.StringID.spigot_message_console_error)));
            return;
        }

        BackpacksAdminGUI gui = new BackpacksAdminGUI(p);
        gui.open();
    }

    @Command(identifier = "gui backpack", description = "Backpack as a GUI", onlyPlayers = true)
    private void backpackGUIOpenCommand(CommandSender sender,
                                        @Arg(name = "Backpack-ID", def = "null", description = "If user have more than backpack, he'll be able to choose another backpack on ID") String id){

        Player p = getSpigotPlayer(sender);

        if (p == null) {
            Output.get().sendInfo(sender, SpigotPrison.format( messages.getString(MessagesConfig.StringID.spigot_message_console_error)));
            return;
        }

        int idNumber;
        try{
            idNumber = Integer.parseInt(id);
        } catch (NumberFormatException ignored){
            Output.get().sendWarn(sender, "ID isn't a valid number.");
            return;
        }

        if (isDisabledWorld(p)) return;

        BackpacksUtil bUtil = BackpacksUtil.get();

        if (bUtil == null){
            return;
        }


        if (bUtil.reachedBackpacksLimit(p) && !bUtil.getBackpacksIDs(p).contains(idNumber)){
            Output.get().sendInfo(sender, SpigotPrison.format(messages.getString(MessagesConfig.StringID.spigot_message_backpack_limit_reached) + " [" + bUtil.getNumberOwnedBackpacks(p) + "]"));
            return;
        }

        if (bUtil.isBackpackUsePermissionEnabled() && !p.hasPermission(bUtil.getBackpackUsePermission())){
            Output.get().sendWarn(sender, SpigotPrison.format(messages.getString(MessagesConfig.StringID.spigot_message_missing_permission) + " [" + bUtil.getBackpackUsePermission() + "]"));
            return;
        }

        if (!bUtil.canOwnBackpacks(p)){
            Output.get().sendInfo(sender, SpigotPrison.format(messages.getString(MessagesConfig.StringID.spigot_message_backpack_cant_own)));
            return;
        }

        if (!bUtil.getBackpacksIDs(p).contains(idNumber)){
            if (bUtil.createBackpack(p)) {
                Output.get().sendInfo(sender, "Backpack created with success!");
                bUtil.openBackpack(p, bUtil.getLatestBackpackID(p));
            } else {
                Output.get().sendWarn(sender, "Couldn't create Backpack, maybe you can't own one or you've reached the limit?");
            }
        } else {
            bUtil.openBackpack(p, idNumber);
        }
    }

    @Command(identifier = "gui backpackslist", description = "Backpack as a GUI", onlyPlayers = true)
    private void backpackListGUICommand(CommandSender sender){
        Player p = getSpigotPlayer(sender);

        if (p == null) {
            Output.get().sendInfo(sender, SpigotPrison.format( messages.getString(MessagesConfig.StringID.spigot_message_console_error)));
            return;
        }

        if (isDisabledWorld(p)) return;

        BackpacksUtil bUtil = BackpacksUtil.get();

        if (bUtil == null){
            return;
        }

        // New method.
        if (bUtil.isBackpackUsePermissionEnabled() && !p.hasPermission(bUtil.getBackpackUsePermission())){
            Output.get().sendWarn(sender, SpigotPrison.format(messages.getString(MessagesConfig.StringID.spigot_message_missing_permission) + " [" + bUtil.getBackpackUsePermission() + "]"));
            return;
        }
        BackpacksListPlayerGUI gui = new BackpacksListPlayerGUI(p);
        gui.open();
    }

    @Command(identifier = "gui backpackadmin", description = "Open backpack admin GUI", permissions = "prison.admin", onlyPlayers = true)
    private void openBackpackAdminCommandGUI(CommandSender sender){

        Player p = getSpigotPlayer(sender);

        if (p == null) {
            Output.get().sendInfo(sender, SpigotPrison.format( messages.getString(MessagesConfig.StringID.spigot_message_console_error)));
            return;
        }

        BackpacksAdminGUI gui = new BackpacksAdminGUI(p);
        gui.open();
    }

    private boolean isDisabledWorld(Player p) {

        BackpacksUtil bUtil = BackpacksUtil.get();

        if (bUtil == null){
            return false;
        }

        return bUtil.isInDisabledWorld(p);
    }
}
