package tech.mcprison.prison.spigot.commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.entity.Player;

import tech.mcprison.prison.Prison;
import tech.mcprison.prison.commands.Arg;
import tech.mcprison.prison.commands.Command;
import tech.mcprison.prison.commands.Wildcard;
import tech.mcprison.prison.internal.CommandSender;
import tech.mcprison.prison.modules.Module;
import tech.mcprison.prison.output.Output;
import tech.mcprison.prison.ranks.PrisonRanks;
import tech.mcprison.prison.spigot.SpigotPrison;
import tech.mcprison.prison.spigot.configs.MessagesConfig;
import tech.mcprison.prison.spigot.gui.rank.SpigotConfirmPrestigeGUI;

/**
 * @author RoyalBlueRanger
 */
public class PrisonSpigotPrestigeCommands
				extends PrisonSpigotBaseCommands {

	private final MessagesConfig messages = SpigotPrison.getInstance().getMessagesConfig();
	
	@Command(identifier = "prestiges", onlyPlayers = true)
	public void prestigesGUICommand(CommandSender sender) {

		if ( !isPrisonConfig( "prestiges") && !isPrisonConfig( "prestige.enabled" ) ) {
			Output.get().sendInfo(sender, SpigotPrison.format(messages.getString(MessagesConfig.StringID.spigot_message_prestiges_disabled)));
			return;
		}
		
        Module ranksModule = Prison.get().getModuleManager().getModule( PrisonRanks.MODULE_NAME );
        
        if ( ranksModule == null || ranksModule != null && !ranksModule.isEnabled() ) {
        	
	        	Output.get().sendWarn( sender, "The command '/prestiges' is disabled because the Ranks module is not active." );
	        	return;
        }

		if ( isConfig( "Options.Prestiges.GUI_Enabled") ) {
			sender.dispatchCommand( "gui prestiges");
		} else {
			sender.dispatchCommand( "ranks list prestiges");
		}
	}


    @Command( identifier = "gui prestigeConfirm", 
    		description = "GUI Prestige Confirmation screeen.  Use the command `/prestige help` instead of "
    				+ "trying to call this directly. This is strictly a 'dumb' internal GUI that has "
    				+ "no validation logic.",
  		  aliases = {"prisonmanager prestige"} )
    public void prisonManagerPrestige(CommandSender sender,
    		@Wildcard(join=true)
    		@Arg(name = "lores", def = "",
    			description = "This field represents a list lore values that will be " +
    				"shown in the prestige confirmation GUI. This should not be used directly." )
    		String lores) {
    	
	    	if ( lores == null || lores.trim().length() == 0 ) {
	    		sender.sendMessage( "Invalid use of `/gui prestigeConfirm`. Please use `/prestige` instead." );
	    		return;
	    	}
	    	
	    	List<String> lore = new ArrayList<>();
	    	
	    	for ( String loreRaw : lores.split( " " ) ) {
	    		String loreValue = loreRaw.replace( "_", " ").trim();
	    		if ( loreValue.length() > 0 ) {
	    			lore.add( loreValue );
	    		}
	    	}
	    	
	    	Player player = getSpigotPlayer( sender );
	
		SpigotConfirmPrestigeGUI gui = new SpigotConfirmPrestigeGUI( player, lore );
		gui.open();
    	
    }

}
