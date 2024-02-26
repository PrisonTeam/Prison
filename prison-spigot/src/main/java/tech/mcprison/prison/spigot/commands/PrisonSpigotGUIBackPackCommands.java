package tech.mcprison.prison.spigot.commands;

import java.util.List;

import org.bukkit.entity.Player;

import tech.mcprison.prison.commands.Arg;
import tech.mcprison.prison.commands.Command;
import tech.mcprison.prison.internal.CommandSender;
import tech.mcprison.prison.output.Output;
import tech.mcprison.prison.spigot.SpigotPrison;
import tech.mcprison.prison.spigot.backpacks.BackpacksUtil;
import tech.mcprison.prison.spigot.configs.MessagesConfig;
import tech.mcprison.prison.spigot.gui.backpacks.BackpacksAdminGUI;
import tech.mcprison.prison.spigot.gui.backpacks.BackpacksListPlayerGUI;

public class PrisonSpigotGUIBackPackCommands
	extends PrisonSpigotBaseCommands
{

	private final MessagesConfig messages = SpigotPrison.getInstance().getMessagesConfig();

    @Command(identifier = "gui backpack", description = "Backpack as a GUI", onlyPlayers = true)
    private void backpackGUIOpenCommand(CommandSender sender,
        @Arg(name = "Backpack-ID", def = "null", 
        description = "If user have more than backpack, he'll be able to choose another backpack on ID") String id){

        Player p = getSpigotPlayer(sender);

        if (p == null) {
            Output.get().sendInfo(sender, SpigotPrison.format( messages.getString(MessagesConfig.StringID.spigot_message_console_error)));
            return;
        }

        if (isDisabledWorld(p)) return;

        if (getBoolean(BackpacksUtil.get().getBackpacksConfig().getString("Options.Multiple-BackPacks-For-Player-Enabled")) && (BackpacksUtil.get().reachedBackpacksLimit(p) && !BackpacksUtil.get().getBackpacksIDs(p).contains(id))){
            Output.get().sendInfo(sender, SpigotPrison.format(messages.getString(MessagesConfig.StringID.spigot_message_backpack_limit_reached) + " [" + BackpacksUtil.get().getNumberOwnedBackpacks(p) + "]"));
            return;
        }

        if (getBoolean(BackpacksUtil.get().getBackpacksConfig().getString("Options.BackPack_Use_Permission_Enabled")) && !p.hasPermission(BackpacksUtil.get().getBackpacksConfig().getString("Options.BackPack_Use_Permission"))){
            Output.get().sendWarn(sender, SpigotPrison.format(
            		messages.getString(MessagesConfig.StringID.spigot_message_missing_permission) 
//            			+ " [" + BackpacksUtil.get().getBackpacksConfig().getString("Options.BackPack_Use_Permission") + "]"
            			));
            return;
        }

        if (!BackpacksUtil.get().canOwnBackpack(p)){
            Output.get().sendInfo(sender, SpigotPrison.format(messages.getString(MessagesConfig.StringID.spigot_message_backpack_cant_own)));
            return;
        }

        // New method.
        if (!id.equalsIgnoreCase("null") && getBoolean(BackpacksUtil.get().getBackpacksConfig().getString("Options.Multiple-BackPacks-For-Player-Enabled"))){
            BackpacksUtil.get().openBackpack(p, id);
        } else {
            BackpacksUtil.get().openBackpack(p, (String) null );
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

        // New method.
        if (getBoolean(BackpacksUtil.get().getBackpacksConfig().getString("Options.Multiple-BackPacks-For-Player-Enabled"))){
            if (getBoolean(BackpacksUtil.get().getBackpacksConfig().getString("Options.BackPack_Use_Permission_Enabled")) && !p.hasPermission(BackpacksUtil.get().getBackpacksConfig().getString("Options.BackPack_Use_Permission"))){
                Output.get().sendWarn(sender, SpigotPrison.format(
                		messages.getString(MessagesConfig.StringID.spigot_message_missing_permission) 
//                		+ " [" + BackpacksUtil.get().getBackpacksConfig().getString("Options.BackPack_Use_Permission") + "]"
                		));
                return;
            }
            BackpacksListPlayerGUI gui = new BackpacksListPlayerGUI(p);
            gui.open();
        }
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
        String worldName = p.getWorld().getName();
        List<String> disabledWorlds = BackpacksUtil.get().getBackpacksConfig().getStringList("Options.DisabledWorlds");
        return disabledWorlds.contains(worldName);
    }

}
