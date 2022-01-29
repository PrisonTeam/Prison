package tech.mcprison.prison.spigot.commands;

import java.util.Optional;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import tech.mcprison.prison.Prison;
import tech.mcprison.prison.commands.Command;
import tech.mcprison.prison.internal.CommandSender;
import tech.mcprison.prison.modules.Module;
import tech.mcprison.prison.modules.ModuleManager;
import tech.mcprison.prison.output.Output;
import tech.mcprison.prison.ranks.PrisonRanks;
import tech.mcprison.prison.ranks.data.RankLadder;
import tech.mcprison.prison.ranks.managers.LadderManager;
import tech.mcprison.prison.spigot.SpigotPrison;
import tech.mcprison.prison.spigot.configs.MessagesConfig;
import tech.mcprison.prison.spigot.gui.ListenersPrisonManager;
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
		
        Optional<Module> ranksModule = Prison.get().getModuleManager().getModule( PrisonRanks.MODULE_NAME );
        if ( !ranksModule.isPresent() || ranksModule.isPresent() && !ranksModule.get().isEnabled() ) {
        	
        	Output.get().sendWarn( sender, "The command '/prestiges' is disabled because the Ranks module is not active." );
        	return;
        }

		if ( isConfig( "Options.Prestiges.GUI_Enabled") ) {
			sender.dispatchCommand( "gui prestiges");
		} else {
			sender.dispatchCommand( "ranks list prestiges");
		}
	}

	@Command(identifier = "prestige", onlyPlayers = true)
	public void prestigesPrestigeCommand(CommandSender sender) {

		if ( isPrisonConfig( "prestiges" ) || isPrisonConfig( "prestige.enabled" ) ) {
			
	    	
	        Optional<Module> ranksModule = Prison.get().getModuleManager().getModule( PrisonRanks.MODULE_NAME );
	        if ( !ranksModule.isPresent() || ranksModule.isPresent() && !ranksModule.get().isEnabled() ) {
	        	
	        	Output.get().sendWarn( sender, "The command '/prestige' is disabled because the Ranks module is not active." );
	        	return;
	        }
	        
	        
			prisonManagerPrestige(sender);
		}
	}

    @Command( identifier = "gui prestige", description = "GUI Prestige",
  		  aliases = {"prisonmanager prestige"} )
    public void prisonManagerPrestige(CommandSender sender ) {

        if ( isPrisonConfig( "prestige.enabled" ) ) {

        	
            Optional<Module> ranksModule = Prison.get().getModuleManager().getModule( PrisonRanks.MODULE_NAME );
            if ( !ranksModule.isPresent() || ranksModule.isPresent() && !ranksModule.get().isEnabled() ) {
            	
            	Output.get().sendWarn( sender, "The command '/gui prestiges' is disabled because the Ranks module is not active." );
            	return;
            }
            
            if ( PrisonRanks.getInstance().getLadderManager().getLadder("prestiges") == null ) {
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "ranks ladder create prestiges");
            }

            PrisonRanks rankPlugin;

            ModuleManager modMan = Prison.get().getModuleManager();
            Module module = modMan == null ? null : modMan.getModule( PrisonRanks.MODULE_NAME ).orElse( null );

            if ( module != null ) {

            	rankPlugin = (PrisonRanks) module;

            	LadderManager lm = null;
            	if (rankPlugin != null) {
            		lm = rankPlugin.getLadderManager();
            		
            		RankLadder ladderDefault = lm.getLadder("default");
            		if ( ( ladderDefault == null  ||
            				!(ladderDefault.getLowestRank().isPresent()) ||
            				ladderDefault.getLowestRank().get().getName() == null)) {
            			Output.get().sendInfo(sender, SpigotPrison.format(messages.getString(MessagesConfig.StringID.spigot_message_ladder_default_empty)));
            			return;
            		}
            		
            		RankLadder ladderPrestiges = lm.getLadder("prestiges");
            		if ( ( ladderPrestiges == null ||
            				!(ladderPrestiges.getLowestRank().isPresent()) ||
            				ladderPrestiges.getLowestRank().get().getName() == null)) {
            			Output.get().sendInfo(sender, SpigotPrison.format(messages.getString(MessagesConfig.StringID.spigot_message_prestiges_empty)));
            			return;
            		}
            	}


            	if ( isPrisonConfig( "prestige.confirmation-enabled") && isPrisonConfig( "prestige.prestige-confirm-gui") ) {
            		try {

            			Player player = getSpigotPlayer( sender );

            			SpigotConfirmPrestigeGUI gui = new SpigotConfirmPrestigeGUI( player );
            			gui.open();
            		} catch (Exception ex) {
            			prestigeByChat( sender );
            		}
            	}
            	else if ( isPrisonConfig( "prestige.confirmation-enabled") ) {
            		prestigeByChat( sender );
            	}
            	else {
            		// Bypassing prestige confirmations:
            		Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "rankup prestiges");
            	}
            }
        }
        else {
        	sender.sendMessage( "Prestiges are disabled. Refresh and then reconfigure config.yml and try again." );
        }
    }

    private void prestigeByChat(CommandSender sender) {

		ListenersPrisonManager listenersPrisonManager = ListenersPrisonManager.get();
		listenersPrisonManager.chatEventActivator();

		Output.get().sendInfo(sender, messages.getString(MessagesConfig.StringID.spigot_gui_lore_prestige_warning_1) + " "
				+ messages.getString(MessagesConfig.StringID.spigot_gui_lore_prestige_warning_2) + " " + messages.getString(MessagesConfig.StringID.spigot_gui_lore_prestige_warning_3));

		Output.get().sendInfo(sender, "&a" + messages.getString(MessagesConfig.StringID.spigot_message_prestiges_confirm));
		Output.get().sendInfo(sender, "&c" + messages.getString(MessagesConfig.StringID.spigot_message_prestiges_cancel));

        final Player player = getSpigotPlayer( sender );
        listenersPrisonManager.chatInteractData(player, ListenersPrisonManager.ChatMode.Prestige);
    }
}
