package tech.mcprison.prison.spigot.commands;

import org.bukkit.Bukkit;
import org.bukkit.configuration.Configuration;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import tech.mcprison.prison.Prison;
import tech.mcprison.prison.commands.Arg;
import tech.mcprison.prison.commands.Command;
import tech.mcprison.prison.internal.CommandSender;
import tech.mcprison.prison.modules.Module;
import tech.mcprison.prison.modules.ModuleManager;
import tech.mcprison.prison.ranks.PrisonRanks;
import tech.mcprison.prison.ranks.managers.LadderManager;
import tech.mcprison.prison.spigot.SpigotPrison;
import tech.mcprison.prison.spigot.gui.ListenersPrisonManager;
import tech.mcprison.prison.spigot.gui.SpigotPrisonGUI;
import tech.mcprison.prison.spigot.gui.mine.SpigotPlayerMinesGUI;
import tech.mcprison.prison.spigot.gui.rank.SpigotConfirmPrestigeGUI;
import tech.mcprison.prison.spigot.gui.rank.SpigotPlayerPrestigesGUI;
import tech.mcprison.prison.spigot.gui.rank.SpigotPlayerRanksGUI;

/**
 * @author GABRYCA
 * @author RoyalBlueRanger
 */
public class PrisonSpigotCommands
				extends PrisonSpigotBaseCommands
				implements Listener {

	private final Configuration messages = SpigotPrison.getInstance().getMessagesConfig();

	//    CommandSender senderOfCommand;

	@Command(identifier = "mines", onlyPlayers = false,
			altPermissions = {"-none-", "mines.admin"})
	public void minesGUICommand(CommandSender sender) {
		if (!sender.hasPermission("mines.admin") && isPrisonConfig("mines-gui-enabled") ) {

			sender.dispatchCommand("gui mines");
		}
		else {
			sender.dispatchCommand("mines help");
		}
	}


	@Command(identifier = "ranks", onlyPlayers = false,
			altPermissions = {"-none-", "ranks.admin"})
	public void ranksGUICommand(CommandSender sender,
				@Arg(name = "ladder", def = "default",
				description = "If player has no permission to /ranks then /ranks list will be ran instead.")
									String ladderName) {
		if (!sender.hasPermission("ranks.admin")) {

			if ((ladderName.equalsIgnoreCase("default") || ladderName.equalsIgnoreCase("ranks")) &&
					isPrisonConfig("ranks-gui-enabled") ) {

				sender.dispatchCommand("gui ranks");
			}
			else if (ladderName.equalsIgnoreCase("prestiges") &&
					isPrisonConfig( "ranks-gui-prestiges-enabled") ) {

				sender.dispatchCommand("gui prestiges");
			}
			else {
				sender.dispatchCommand("ranks list " + ladderName);
			}
		}
		else {
			sender.dispatchCommand("ranks help");
		}
	}


	@Command(identifier = "prestiges", onlyPlayers = true, altPermissions = {"-none-", "prison.admin"})
	public void prestigesGUICommand(CommandSender sender) {

		if ( !isPrisonConfig( "prestiges") ) {
			sender.sendMessage(SpigotPrison.format(messages.getString("Gui.Message.PrestigesDisabledDefault")));
			return;
		}

		if ( isConfig( "prestiges-gui-enabled") ) {
			sender.dispatchCommand( "gui prestiges");
		}
		else {
			sender.dispatchCommand( "ranks list prestiges");
		}
	}


	@Command(identifier = "prestige", onlyPlayers = true, altPermissions = "-none-")
	public void prestigesPrestigeCommand(CommandSender sender) {

		if ( isPrisonConfig( "prestiges" ) ) {
			sender.dispatchCommand("gui prestige");
		}
	}



    @Command( identifier = "gui prestige", description = "GUI Prestige",
  		  aliases = {"prisonmanager prestige"} )
    public void prisonManagerPrestige(CommandSender sender ) {

        if ( isPrisonConfig("prestiges") ) {

            if (!(PrisonRanks.getInstance().getLadderManager().getLadder("prestiges").isPresent())) {
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
            	}

            	if (lm != null && (!(lm.getLadder("default").isPresent()) ||
            			!(lm.getLadder("default").get().getLowestRank().isPresent()) ||
            			lm.getLadder("default").get().getLowestRank().get().name == null)) {
            		sender.sendMessage(SpigotPrison.format(messages.getString("Gui.Message.DefaultLadderEmpty")));
            		return;
            	}

            	if (lm != null && (!(lm.getLadder("prestiges").isPresent()) ||
            			!(lm.getLadder("prestiges").get().getLowestRank().isPresent()) ||
            			lm.getLadder("prestiges").get().getLowestRank().get().name == null)) {
            		sender.sendMessage(SpigotPrison.format(messages.getString("Gui.Message.CantFindPrestiges")));
            		return;
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

        sender.sendMessage(SpigotPrison.format(getPrisonConfig("Gui.Lore.PrestigeWarning") +
        		getPrisonConfig("Gui.Lore.PrestigeWarning2") +
        		getPrisonConfig("Gui.Lore.PrestigeWarning3")));
        
        sender.sendMessage(SpigotPrison.format(messages.getString("Gui.Message.ConfirmPrestige")));
        sender.sendMessage(SpigotPrison.format(messages.getString("Gui.Message.CancelPrestige")));

        final Player player = getSpigotPlayer( sender );

        listenersPrisonManager.addMode("prestige");
        listenersPrisonManager.addChatEventPlayer(player);
        listenersPrisonManager.id = Bukkit.getScheduler().scheduleSyncDelayedTask(SpigotPrison.getInstance(), () -> {
            if (listenersPrisonManager.chatEventCheck()) {
                listenersPrisonManager.chatEventDeactivate();
                player.sendMessage(SpigotPrison.format(messages.getString("Gui.Message.PrestigeRanOutOfTime")));
                listenersPrisonManager.removeChatEventPlayer(player);
                listenersPrisonManager.removeMode();
            }
        }, 20L * 30);
    }


    @Command( identifier = "gui prestiges", description = "GUI Prestiges",
    		  aliases = {"prisonmanager prestiges"},
    		  onlyPlayers = true )
    private void prisonManagerPrestiges( CommandSender sender ) {

        if ( !isPrisonConfig("prestiges") ) {
            sender.sendMessage(SpigotPrison.format(messages.getString("Gui.Message.PrestigesAreDisabled")));
            return;
        }


        if ( !isPrisonConfig("prison-gui-enabled") || !isConfig("Options.Prestiges.GUI_Enabled")){
            sender.sendMessage(SpigotPrison.format(messages.getString("Gui.Message.GuiOrPrestigesDisabled")));
            return;
        }

        if ( isConfig("Options.Prestiges.Permission_GUI_Enabled") ){
        	String perm = getConfig( "Options.Prestiges.Permission_GUI");

            if ( !sender.hasPermission( perm ) ){
                sender.sendMessage(SpigotPrison.format(messages.getString("Gui.Message.missingGuiPrestigesPermission") + " [" +
        				perm + "]"));
                return;
            }
        }

        Player player = getSpigotPlayer( sender );
        SpigotPlayerPrestigesGUI gui = new SpigotPlayerPrestigesGUI( player );
        gui.open();
    }


    @Command( identifier = "gui mines", description = "GUI Mines",
  		  aliases = {"prisonmanager mines"},
		  onlyPlayers = true )
    private void prisonManagerMines(CommandSender sender) {

        if ( !isPrisonConfig("prison-gui-enabled") || !isConfig("Options.Mines.GUI_Enabled") ){
            sender.sendMessage(SpigotPrison.format(messages.getString("Gui.Message.mineOrGuiDisabled")));
            return;
        }


        if ( isConfig("Options.Mines.Permission_GUI_Enabled") ){
        	String perm = getConfig( "Options.Mines.Permission_GUI");

            if ( !sender.hasPermission( perm ) ){
                sender.sendMessage(SpigotPrison.format(messages.getString("Gui.Message.mineMissingGuiPermission") + " [" +
        				perm + "]"));
                return;
            }
        }

        Player player = getSpigotPlayer( sender );
        SpigotPlayerMinesGUI gui = new SpigotPlayerMinesGUI( player );
        gui.open();
    }


    @Command( identifier = "gui ranks", description = "GUI Ranks",
    		  aliases = {"prisonmanager ranks"},
    		  onlyPlayers = true )
    private void prisonManagerRanks(CommandSender sender) {

        if (!isPrisonConfig("prison-gui-enabled") || !isConfig("Options.Ranks.GUI_Enabled")) {
        	sender.sendMessage(SpigotPrison.format(String.format( messages.getString("Gui.Message.rankGuiDisabledOrAllGuiDisabled"), getPrisonConfig("prison-gui-enabled"), getConfig("Options.Ranks.GUI_Enabled") )));
        	return;
        }

        if (isConfig("Options.Ranks.Permission_GUI_Enabled")) {
        	String perm = getConfig( "Options.Ranks.Permission_GUI");
        	if (!sender.hasPermission(perm)) {

        		sender.sendMessage(SpigotPrison.format(messages.getString("Gui.Message.rankGuiMissingPermission") + " [" +
        				perm + "]"));
        		return;
        	}
        }

        Player player = getSpigotPlayer( sender );
        SpigotPlayerRanksGUI gui = new SpigotPlayerRanksGUI( player );
        gui.open();
    }


    /**
     * NOTE: onlyPlayers needs to be false so players can use /gui help on the command, even from console.
     *
     * @param sender
     */
    @Command( identifier = "gui", description = "The GUI",
    		  aliases = {"prisonmanager", "prisonmanager gui", "gui admin"},
    		  permissions = {"prison.admin", "prison.prisonmanagergui"},
    		  onlyPlayers = false
    		)
    private void prisonManagerGUI(CommandSender sender) {

        Player player = getSpigotPlayer(sender);

        if (player == null) {
        	sender.sendMessage( SpigotPrison.format(messages.getString("Gui.Message.CantRunGUIFromConsole")));
        	return;
        }

    	SpigotPrisonGUI gui = new SpigotPrisonGUI(player);
        gui.open();
    }
}
