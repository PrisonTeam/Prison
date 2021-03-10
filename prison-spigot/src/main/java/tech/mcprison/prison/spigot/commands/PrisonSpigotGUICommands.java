package tech.mcprison.prison.spigot.commands;

import org.bukkit.configuration.Configuration;
import org.bukkit.entity.Player;

import tech.mcprison.prison.Prison;
import tech.mcprison.prison.commands.Arg;
import tech.mcprison.prison.commands.Command;
import tech.mcprison.prison.internal.CommandSender;
import tech.mcprison.prison.output.Output;
import tech.mcprison.prison.spigot.SpigotPrison;
import tech.mcprison.prison.spigot.backpacks.BackpacksUtil;
import tech.mcprison.prison.spigot.gui.SpigotPrisonGUI;
import tech.mcprison.prison.spigot.gui.backpacks.BackpacksListPlayer;
import tech.mcprison.prison.spigot.gui.mine.SpigotPlayerMinesGUI;
import tech.mcprison.prison.spigot.gui.rank.SpigotPlayerPrestigesGUI;
import tech.mcprison.prison.spigot.gui.rank.SpigotPlayerRanksGUI;

/**
 * @author GABRYCA
 * @author RoyalBlueRanger (rBluer)
 */
public class PrisonSpigotGUICommands extends PrisonSpigotBaseCommands {

    private final Configuration messages = SpigotPrison.getInstance().getMessagesConfig();

    /**
     * NOTE: onlyPlayers needs to be false so players can use /gui help on the command, even from console.
     *
     * @param sender
     */
    @Command( identifier = "gui", description = "The Prison's GUI",
            aliases = {"gui admin"},
            altPermissions = {"prison.admin", "prison.prisonmanagergui"},
            onlyPlayers = false
    )
    private void prisonManagerGUI(CommandSender sender) {

        Player player = getSpigotPlayer(sender);

        if (player == null) {
            Output.get().sendInfo(sender, SpigotPrison.format( getMessages().getString("Message.CantRunGUIFromConsole")));
            return;
        }

        if (player.hasPermission("prison.admin") || player.hasPermission("prison.prisonmanagergui")) {
            SpigotPrisonGUI gui = new SpigotPrisonGUI(player);
            gui.open();
            return;
        }

        sender.dispatchCommand("gui help");
    }

    @Command( identifier = "gui prestiges", description = "GUI Prestiges",
            onlyPlayers = true )
    private void prisonManagerPrestiges( CommandSender sender ) {

        if ( !isPrisonConfig("prestiges") && !isPrisonConfig( "prestige.enabled" ) ) {
            Output.get().sendInfo(sender, SpigotPrison.format(messages.getString("Message.PrestigesAreDisabled")));
            return;
        }


        if ( !isPrisonConfig("prison-gui-enabled") || !isConfig("Options.Prestiges.GUI_Enabled")){
            Output.get().sendInfo(sender, SpigotPrison.format(messages.getString("Message.GuiOrPrestigesDisabled")));
            return;
        }

        if ( isConfig("Options.Prestiges.Permission_GUI_Enabled") ){
            String perm = getConfig( "Options.Prestiges.Permission_GUI");

            if ( !sender.hasPermission( perm ) ){
                Output.get().sendInfo(sender, SpigotPrison.format(messages.getString("Message.missingGuiPrestigesPermission") + " [" +
                        perm + "]"));
                return;
            }
        }

        Player player = getSpigotPlayer( sender );

        if (player == null) {
            Output.get().sendInfo(sender, SpigotPrison.format( getMessages().getString("Message.CantRunGUIFromConsole")));
            return;
        }

        SpigotPlayerPrestigesGUI gui = new SpigotPlayerPrestigesGUI( player );
        gui.open();
    }

    @Command( identifier = "gui mines", description = "GUI Mines",
            onlyPlayers = true )
    private void prisonManagerMines(CommandSender sender) {

        Player player = getSpigotPlayer(sender);

        if (player == null) {
            Output.get().sendInfo(sender, SpigotPrison.format( getMessages().getString("Message.CantRunGUIFromConsole")));
            return;
        }

        if ( !isPrisonConfig("prison-gui-enabled") || !isConfig("Options.Mines.GUI_Enabled") ){
            Output.get().sendInfo(sender, SpigotPrison.format( getMessages().getString("Message.mineOrGuiDisabled")));
            return;
        }


        if ( isConfig("Options.Mines.Permission_GUI_Enabled") ){
            String perm = getConfig( "Options.Mines.Permission_GUI");

            if ( !sender.hasPermission( perm ) ){
                Output.get().sendInfo(sender, SpigotPrison.format( getMessages().getString("Message.mineMissingGuiPermission") + " [" +
                        perm + "]"));
                return;
            }
        }

        SpigotPlayerMinesGUI gui = new SpigotPlayerMinesGUI( player );
        gui.open();
    }

    @Command( identifier = "gui ranks", description = "GUI Ranks",
            onlyPlayers = true )
    private void prisonManagerRanks(CommandSender sender) {

        Player player = getSpigotPlayer(sender);

        if (player == null) {
            Output.get().sendInfo(sender, SpigotPrison.format( getMessages().getString("Message.CantRunGUIFromConsole")));
            return;
        }

        if (!isPrisonConfig("prison-gui-enabled") || !isConfig("Options.Ranks.GUI_Enabled")) {
            Output.get().sendInfo(sender, SpigotPrison.format(String.format(String.format(
                    getMessages().getString("Message.rankGuiDisabledOrAllGuiDisabled"),
                    getPrisonConfig("prison-gui-enabled"), getConfig("Options.Ranks.GUI_Enabled") ))));
            return;
        }

        if (isConfig("Options.Ranks.Permission_GUI_Enabled")) {
            String perm = getConfig( "Options.Ranks.Permission_GUI");
            if (!sender.hasPermission(perm)) {

                Output.get().sendInfo(sender, SpigotPrison.format( getMessages().getString("Message.rankGuiMissingPermission") + " [" +
                        perm + "]"));
                return;
            }
        }

        SpigotPlayerRanksGUI gui = new SpigotPlayerRanksGUI( player );
        gui.open();
    }

    @Command(identifier = "gui sellall", description = "SellAll GUI command", onlyPlayers = true)
    private void sellAllGuiCommandNew(CommandSender sender){

    	String registeredCmd = Prison.get().getCommandHandler().findRegisteredCommand( "sellall gui" );
        sender.dispatchCommand(registeredCmd);
    }

    @Command(identifier = "gui backpack", description = "Backpack as a GUI", onlyPlayers = true)
    private void backpackGUIOpenCommand(CommandSender sender,
    @Arg(name = "Backpack-ID", def = "null", description = "If user have more than backpack, he'll be able to choose another backpack on ID") String id){

        Player p = getSpigotPlayer(sender);

        if (p == null) {
            Output.get().sendInfo(sender, SpigotPrison.format( getMessages().getString("Message.CantRunGUIFromConsole")));
            return;
        }

        if (getBoolean(BackpacksUtil.get().getBackpacksConfig().getString("Options.Multiple-BackPacks-For-Player-Enabled")) && (Integer.parseInt(BackpacksUtil.get().getBackpacksConfig().getString("Options.Multiple-BackPacks-For-Player")) <= BackpacksUtil.get().getNumberOwnedBackpacks(p)) && !BackpacksUtil.get().getBackpacksIDs(p).contains(id)){
            Output.get().sendInfo(sender, SpigotPrison.format(messages.getString("Message.BackPackOwnLimitReached") + " [" + BackpacksUtil.get().getNumberOwnedBackpacks(p) + "]"));
            return;
        }

        // New method.
        if (!id.equalsIgnoreCase("null") && getBoolean(BackpacksUtil.get().getBackpacksConfig().getString("Options.Multiple-BackPacks-For-Player-Enabled"))){
            BackpacksUtil.get().openBackpack(p, id);
        } else {
            BackpacksUtil.get().openBackpack(p);
        }
    }

    @Command(identifier = "gui backpackslist", description = "Backpack as a GUI", onlyPlayers = true)
    private void backpackListGUICommand(CommandSender sender){
        Player p = getSpigotPlayer(sender);

        if (p == null) {
            Output.get().sendInfo(sender, SpigotPrison.format( getMessages().getString("Message.CantRunGUIFromConsole")));
            return;
        }

        // New method.
        if (getBoolean(BackpacksUtil.get().getBackpacksConfig().getString("Options.Multiple-BackPacks-For-Player-Enabled"))){
            BackpacksListPlayer gui = new BackpacksListPlayer(p);
            gui.open();
        }
    }

}
