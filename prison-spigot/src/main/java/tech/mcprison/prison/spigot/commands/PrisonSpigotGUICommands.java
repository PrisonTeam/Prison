package tech.mcprison.prison.spigot.commands;

import org.bukkit.entity.Player;

import tech.mcprison.prison.commands.Arg;
import tech.mcprison.prison.commands.Command;
import tech.mcprison.prison.internal.CommandSender;
import tech.mcprison.prison.output.Output;
import tech.mcprison.prison.ranks.PrisonRanks;
import tech.mcprison.prison.ranks.data.RankLadder;
import tech.mcprison.prison.spigot.SpigotPrison;
import tech.mcprison.prison.spigot.configs.MessagesConfig;
import tech.mcprison.prison.spigot.game.SpigotPlayer;
import tech.mcprison.prison.spigot.gui.SpigotPrisonGUI;
import tech.mcprison.prison.spigot.gui.guiutility.SpigotGUIComponents;
import tech.mcprison.prison.spigot.gui.mine.SpigotMinesGUI;
import tech.mcprison.prison.spigot.gui.mine.SpigotPlayerMinesGUI;
import tech.mcprison.prison.spigot.gui.rank.SpigotLaddersGUI;
import tech.mcprison.prison.spigot.gui.rank.SpigotPlayerRanksGUI;
import tech.mcprison.prison.spigot.gui.rank.SpigotRanksGUI;

/**
 * @author GABRYCA
 * @author RoyalBlueRanger (rBluer)
 */
public class PrisonSpigotGUICommands extends PrisonSpigotBaseCommands {

    private final MessagesConfig messages = getMessages();

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
            Output.get().sendInfo(sender, SpigotPrison.format( getMessages().getString(MessagesConfig.StringID.spigot_message_console_error)));
            return;
        }

        if (player.hasPermission("prison.admin") || player.hasPermission("prison.prisonmanagergui")) {
            SpigotPrisonGUI gui = new SpigotPrisonGUI(player);
            gui.open();
            return;
        }
        
        // If the player will be shown the /gui help, then force close any open inventory:
        player.closeInventory();

        sender.dispatchCommand("gui help");
    }

    @Command( identifier = "gui prestiges", description = "GUI Prestiges",
            onlyPlayers = true )
    private void prisonManagerPrestiges( CommandSender sender,
    		@Arg(name = "page", description = "If there are more than 45 prestiges, then the " +
    				"prestiges are shown on multiple pages.  The page parameter starts with " +
    				"page 1.", def = "1" ) int page
    		) {
    	cmdPrisonManagerPrestiges( sender, page, "gui prestiges", "gui" );
    }
    protected void cmdPrisonManagerPrestiges( CommandSender sender, int page, String cmdPage, String cmdReturn ) {

        if ( !isPrisonConfig("prestiges") && !isPrisonConfig( "prestige.enabled" ) ) {
            Output.get().sendInfo(sender, SpigotPrison.format(messages.getString(MessagesConfig.StringID.spigot_message_prestiges_disabled)));
            return;
        }


        if ( !isPrisonConfig("prison-gui-enabled") || !isConfig("Options.Prestiges.GUI_Enabled")){
            Output.get().sendInfo(sender, SpigotPrison.format(messages.getString(MessagesConfig.StringID.spigot_message_prestiges_or_gui_disabled)));
            return;
        }

        if ( isConfig("Options.Prestiges.Permission_GUI_Enabled") ){
            String perm = getConfig( "Options.Prestiges.Permission_GUI");

            if ( !sender.hasPermission( perm ) ){
                Output.get().sendInfo(sender, SpigotPrison.format(messages.getString(MessagesConfig.StringID.spigot_message_missing_permission) + " [" +
                        perm + "]"));
                return;
            }
        }

        Player player = getSpigotPlayer( sender );

        if (player == null) {
            Output.get().sendInfo(sender, SpigotPrison.format( getMessages().getString(MessagesConfig.StringID.spigot_message_console_error)));
            return;
        }


        SpigotPlayerRanksGUI gui = new SpigotPlayerRanksGUI( player, "prestiges", page, cmdPage, cmdReturn );
//        SpigotPlayerPrestigesGUI gui = new SpigotPlayerPrestigesGUI( player, page, cmdPage, cmdReturn );
        gui.open();
    }

    @Command( identifier = "gui mines", description = "GUI Mines",
            onlyPlayers = true )
    private void prisonManagerMines(CommandSender sender,
    		@Arg(name = "page", description = "If there are more than 45 mines, then the " +
    				"mines are shown on multiple pages.  The page parameter starts with " +
    				"page 1.", def = "1" ) int page
    		) {

    	cmdPrisonManagerMines( sender, page, "gui mines", "gui" );
    }
    
    protected void cmdPrisonManagerMines( CommandSender sender, int page, String cmdPage, String cmdReturn ) {
        Player player = getSpigotPlayer(sender);

        if (player == null) {
            Output.get().sendInfo(sender, SpigotPrison.format( getMessages().getString(MessagesConfig.StringID.spigot_message_console_error)));
            return;
        }

        if ( !isPrisonConfig("prison-gui-enabled") || !isConfig("Options.Mines.GUI_Enabled") ){
            Output.get().sendInfo(sender, SpigotPrison.format( getMessages().getString(MessagesConfig.StringID.spigot_message_mines_or_gui_disabled)));
            return;
        }


        if ( isConfig("Options.Mines.Permission_GUI_Enabled") ){
            String perm = getConfig( "Options.Mines.Permission_GUI");

            if ( !sender.hasPermission( perm ) ){
                Output.get().sendInfo(sender, SpigotPrison.format( getMessages().getString(MessagesConfig.StringID.spigot_message_missing_permission) + " [" +
                        perm + "]"));
                return;
            }
        }

        SpigotPlayerMinesGUI gui = new SpigotPlayerMinesGUI( player, page, cmdPage, cmdReturn );
        gui.open();
    }
    
    @Command( identifier = "gui admin mines", description = "GUI Mines",
    		onlyPlayers = true )
    private void prisonManagerAdminMines(CommandSender sender,
    		@Arg(name = "page", description = "If there are more than 45 mines, then the " +
    				"mines are shown on multiple pages.  The page parameter starts with " +
    				"page 1.", def = "1" ) int page
    		) {
    	
    	cmdPrisonManagerAdminMines( sender, page, "gui admin mines", "gui admin" );
    }
    
    protected void cmdPrisonManagerAdminMines( CommandSender sender, int page, String cmdPage, String cmdReturn ) {
    	Player player = getSpigotPlayer(sender);
    	
    	if (player == null) {
    		Output.get().sendInfo(sender, SpigotPrison.format( getMessages().getString(MessagesConfig.StringID.spigot_message_console_error)));
    		return;
    	}
    	
    	if ( !player.hasPermission("prison.admin") && !player.hasPermission("prison.prisonmanagergui")) {
            return;
        }
    	
//    	if ( !isPrisonConfig("prison-gui-enabled") || !isConfig("Options.Mines.GUI_Enabled") ){
//    		Output.get().sendInfo(sender, SpigotPrison.format( getMessages().getString(MessagesConfig.StringID.spigot_message_mines_or_gui_disabled)));
//    		return;
//    	}
    	
    	
//    	if ( isConfig("Options.Mines.Permission_GUI_Enabled") ){
//    		String perm = getConfig( "Options.Mines.Permission_GUI");
//    		
//    		if ( !sender.hasPermission( perm ) ){
//    			Output.get().sendInfo(sender, SpigotPrison.format( getMessages().getString(MessagesConfig.StringID.spigot_message_missing_permission) + " [" +
//    					perm + "]"));
//    			return;
//    		}
//    	}
    	
    	SpigotMinesGUI gui = new SpigotMinesGUI( player, page, cmdPage, cmdReturn );
    	gui.open();
    }

    @Command( identifier = "gui ranks", description = "GUI Ranks",
            onlyPlayers = true )
    private void prisonManagerRanks(CommandSender sender,
    		@Arg(name = "page", description = "If there are more than 45 ranks, then the " +
    				"ranks are shown on multiple pages.  The page parameter starts with " +
    				"page 1.", def = "1" ) int page
    		) {
    	
    	cmdPrisonManagerRanks( sender, page, "gui ranks", "gui" );
    }
    protected void cmdPrisonManagerRanks(CommandSender sender, int page, String cmdPage, String cmdReturn ) {
        Player player = getSpigotPlayer(sender);

        if (player == null) {
            Output.get().sendInfo(sender, SpigotPrison.format( getMessages().getString(MessagesConfig.StringID.spigot_message_console_error)));
            return;
        }

        if (!isPrisonConfig("prison-gui-enabled") || !isConfig("Options.Ranks.GUI_Enabled")) {
            Output.get().sendInfo(sender, SpigotPrison.format(String.format(String.format(
                    getMessages().getString(MessagesConfig.StringID.spigot_message_ranks_or_gui_disabled),
                    getPrisonConfig("prison-gui-enabled"), getConfig("Options.Ranks.GUI_Enabled") ))));
            return;
        }

        if (isConfig("Options.Ranks.Permission_GUI_Enabled")) {
            String perm = getConfig( "Options.Ranks.Permission_GUI");
            if (!sender.hasPermission(perm)) {

                Output.get().sendInfo(sender, SpigotPrison.format( getMessages().getString(MessagesConfig.StringID.spigot_message_missing_permission) + " [" +
                        perm + "]"));
                return;
            }
        }

        SpigotPlayerRanksGUI gui = new SpigotPlayerRanksGUI( player, "default", page, cmdPage, cmdReturn );
        gui.open();
    }
    
    @Command( identifier = "gui admin ranks", description = "GUI Ranks",
    		onlyPlayers = true )
    private void prisonManagerAdminRanks(CommandSender sender,
    		
    		@Arg(name = "ladder", description = "Ladder to use for displaying all the ranks",
    				def = "default" ) String ladder,
    		
    		@Arg(name = "page", description = "If there are more than 45 ranks, then the " +
    				"ranks are shown on multiple pages.  The page parameter starts with " +
    				"page 1.", def = "1" ) int page
    		) {
    	
    	String cmdPage = "gui admin ranks " + ladder;
    	cmdPrisonManagerAdminRanks( sender, ladder, page, cmdPage, "gui admin" );
    }
    protected void cmdPrisonManagerAdminRanks(CommandSender sender, String ladder,
    									int page, String cmdPage, String cmdReturn ) {
    	Player player = getSpigotPlayer(sender);
    	
    	if (player == null) {
    		Output.get().sendInfo(sender, SpigotPrison.format( getMessages().getString(MessagesConfig.StringID.spigot_message_console_error)));
    		return;
    	}
    	
    	if ( !player.hasPermission("prison.admin") && !player.hasPermission("prison.prisonmanagergui")) {
            return;
        }
    	
    	
    	// ladder
    	RankLadder rLadder = PrisonRanks.getInstance().getLadderManager().getLadder( ladder );
    	if ( rLadder == null ) {
    		
    		SpigotPlayer sPlayer = new SpigotPlayer(player);
    		sPlayer.setActionBar( "Invalid ladder name" );
    		player.closeInventory();
    		return;
    	}
    	
//    	if (!isPrisonConfig("prison-gui-enabled") || !isConfig("Options.Ranks.GUI_Enabled")) {
//    		Output.get().sendInfo(sender, SpigotPrison.format(String.format(String.format(
//    				getMessages().getString(MessagesConfig.StringID.spigot_message_ranks_or_gui_disabled),
//    				getPrisonConfig("prison-gui-enabled"), getConfig("Options.Ranks.GUI_Enabled") ))));
//    		return;
//    	}
//    	
//    	if (isConfig("Options.Ranks.Permission_GUI_Enabled")) {
//    		String perm = getConfig( "Options.Ranks.Permission_GUI");
//    		if (!sender.hasPermission(perm)) {
//    			
//    			Output.get().sendInfo(sender, SpigotPrison.format( getMessages().getString(MessagesConfig.StringID.spigot_message_missing_permission) + " [" +
//    					perm + "]"));
//    			return;
//    		}
//    	}
    	
    	SpigotRanksGUI gui = new SpigotRanksGUI( player, rLadder, page, cmdPage, cmdReturn );
    	//SpigotPlayerRanksGUI gui = new SpigotPlayerRanksGUI( player, page, cmdPage, cmdReturn );
    	gui.open();
    }
    
    @Command( identifier = "gui ladders", 
    		description = "GUI Ladders. The permissions are based upon the ranks perms.",
    		onlyPlayers = true )
    private void prisonManagerLadders(CommandSender sender,
    		@Arg(name = "page", description = "If there are more than 45 ladders, then the " +
    				"ladders are shown on multiple pages.  The page parameter starts with " +
    				"page 1.", def = "1" ) int page
    		) {
    	
    	cmdPrisonManagerLadders( sender, page, "gui ladders", "gui" );
    }
    protected void cmdPrisonManagerLadders(CommandSender sender, int page, String cmdPage, String cmdReturn ) {
    	
    	Player player = getSpigotPlayer(sender);
    	
    	if (player == null) {
    		Output.get().sendInfo(sender, SpigotPrison.format( getMessages().getString(MessagesConfig.StringID.spigot_message_console_error)));
    		return;
    	}
    	
    	if (!isPrisonConfig("prison-gui-enabled") || !isConfig("Options.Ranks.GUI_Enabled")) {
    		Output.get().sendInfo(sender, SpigotPrison.format(String.format(String.format(
    				getMessages().getString(MessagesConfig.StringID.spigot_message_ranks_or_gui_disabled),
    				getPrisonConfig("prison-gui-enabled"), getConfig("Options.Ranks.GUI_Enabled") ))));
    		return;
    	}
    	
    	if (isConfig("Options.Ranks.Permission_GUI_Enabled")) {
    		String perm = getConfig( "Options.Ranks.Permission_GUI");
    		if (!sender.hasPermission(perm)) {
    			
    			Output.get().sendInfo(sender, SpigotPrison.format( getMessages().getString(MessagesConfig.StringID.spigot_message_missing_permission) + " [" +
    					perm + "]"));
    			return;
    		}
    	}
    	
    	SpigotLaddersGUI gui = new SpigotLaddersGUI( player, page, cmdPage, cmdReturn );
    	gui.open();
    }

//    @Command(identifier = "gui sellall", description = "SellAll GUI command", onlyPlayers = true)
//    private void sellAllGuiCommandNew(CommandSender sender){
//
//    	String registeredCmd = Prison.get().getCommandHandler().findRegisteredCommand( "sellall gui" );
//        sender.dispatchCommand(registeredCmd);
//    }

    // Backpack GUI commands got moved to the Backpacks class so they won't be loaded if backpacks are disabled.

    @Command(identifier = "gui reload", description = "Reload GUIs", permissions = "prison.admin",onlyPlayers = false)
    public void reloadGUICommand(CommandSender sender){
        SpigotGUIComponents.updateMessages();
        SpigotGUIComponents.updateSellAllConfig();
        SpigotGUIComponents.updateGUIConfig();
        Output.get().sendInfo(sender, SpigotPrison.format(messages.getString(MessagesConfig.StringID.spigot_message_gui_reload_success)));
    }
}
