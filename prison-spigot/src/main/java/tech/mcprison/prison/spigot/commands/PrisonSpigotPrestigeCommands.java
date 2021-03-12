package tech.mcprison.prison.spigot.commands;

import org.bukkit.Bukkit;
import org.bukkit.configuration.Configuration;
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
import tech.mcprison.prison.spigot.gui.ListenersPrisonManager;
import tech.mcprison.prison.spigot.gui.rank.SpigotConfirmPrestigeGUI;

/**
 * @author RoyalBlueRanger
 */
public class PrisonSpigotPrestigeCommands
				extends PrisonSpigotBaseCommands {

	private final Configuration messages = SpigotPrison.getInstance().getMessagesConfig();
	
	@Command(identifier = "prestiges", onlyPlayers = true)
	public void prestigesGUICommand(CommandSender sender) {

		if ( !isPrisonConfig( "prestiges") && !isPrisonConfig( "prestige.enabled" ) ) {
			Output.get().sendInfo(sender, SpigotPrison.format(messages.getString("Message.PrestigesDisabledDefault")));
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
			sender.dispatchCommand("rankup prestiges");
		}
	}

    @Command( identifier = "gui prestige", description = "GUI Prestige",
  		  aliases = {"prisonmanager prestige"} )
    public void prisonManagerPrestige(CommandSender sender ) {

        if ( isPrisonConfig("prestiges") || isPrisonConfig( "prestige.enabled" ) ) {

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
            			Output.get().sendInfo(sender, SpigotPrison.format(messages.getString("Message.DefaultLadderEmpty")));
            			return;
            		}
            		
            		RankLadder ladderPrestiges = lm.getLadder("prestiges");
            		if ( ( ladderPrestiges == null ||
            				!(ladderPrestiges.getLowestRank().isPresent()) ||
            				ladderPrestiges.getLowestRank().get().getName() == null)) {
            			Output.get().sendInfo(sender, SpigotPrison.format(messages.getString("Message.CantFindPrestiges")));
            			return;
            		}
            	}


            	if ( isPrisonConfig( "prestige-confirm-gui") ) {
            		try {

            			Player player = getSpigotPlayer( sender );

            			SpigotConfirmPrestigeGUI gui = new SpigotConfirmPrestigeGUI( player );
            			gui.open();
            		} catch (Exception ex) {
            			prestigeByChat( sender );
            		}
            	}
            	else {
            		prestigeByChat( sender );
            	}

            }
        }
    }

    private void prestigeByChat(CommandSender sender) {

		ListenersPrisonManager listenersPrisonManager = ListenersPrisonManager.get();
		listenersPrisonManager.chatEventActivator();

        Output.get().sendInfo(sender, SpigotPrison.format(getPrisonConfig("Lore.PrestigeWarning") +
        		getPrisonConfig("Lore.PrestigeWarning2") +
        		getPrisonConfig("Lore.PrestigeWarning3")));
        
        Output.get().sendInfo(sender, SpigotPrison.format(messages.getString("Message.ConfirmPrestige")));
        Output.get().sendInfo(sender, SpigotPrison.format(messages.getString("Message.CancelPrestige")));

        final Player player = getSpigotPlayer( sender );

        listenersPrisonManager.chatInteractData(player, ListenersPrisonManager.ChatMode.Prestige);
    }
}
