package tech.mcprison.prison.spigot.commands;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import tech.mcprison.prison.Prison;
import tech.mcprison.prison.commands.Arg;
import tech.mcprison.prison.commands.Command;
import tech.mcprison.prison.internal.CommandSender;
import tech.mcprison.prison.modules.Module;
import tech.mcprison.prison.modules.ModuleManager;
import tech.mcprison.prison.ranks.PrisonRanks;
import tech.mcprison.prison.ranks.managers.LadderManager;
import tech.mcprison.prison.spigot.SpigotPrison;
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

    private boolean isChatEventActive;
    private int id;
    private String mode;
//    CommandSender senderOfCommand;

    @EventHandler(priority = EventPriority.LOWEST)
    public void onChat(AsyncPlayerChatEvent e) {
        if (isChatEventActive) {
            Player p = e.getPlayer();
            String message = e.getMessage();
            Bukkit.getScheduler().cancelTask(id);
            if (mode.equalsIgnoreCase("prestige")){
                if (message.equalsIgnoreCase("cancel")) {
                    isChatEventActive = false;
                    p.sendMessage(SpigotPrison.format("&cPrestige cancelled"));
                    e.setCancelled(true);
                } else if (message.equalsIgnoreCase("confirm")) {
                    Bukkit.getScheduler().runTask(SpigotPrison.getInstance(), () -> Bukkit.getServer().dispatchCommand(p, "rankup prestiges"));
                    e.setCancelled(true);
                    isChatEventActive = false;
                }
            }
        }
    }


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
			sender.sendMessage(SpigotPrison.format("&cPrestiges are disabled by default, please edit it in your config.yml!"));
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
            		sender.sendMessage(SpigotPrison.format("&cError: The default ladder has no rank."));
            		return;
            	}
            	
            	if (lm != null && (!(lm.getLadder("prestiges").isPresent()) ||
            			!(lm.getLadder("prestiges").get().getLowestRank().isPresent()) ||
            			lm.getLadder("prestiges").get().getLowestRank().get().name == null)) {
            		sender.sendMessage(SpigotPrison.format("&cError: The prestige ladder has no prestiges"));
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
        isChatEventActive = true;
        
        sender.sendMessage(SpigotPrison.format(getPrisonConfig("Gui.Lore.PrestigeWarning") +
        		getPrisonConfig("Gui.Lore.PrestigeWarning2") + 
        		getPrisonConfig("Gui.Lore.PrestigeWarning3")));
        
        sender.sendMessage(SpigotPrison.format("&aConfirm&3: Type the word &aconfirm &3 to confirm"));
        sender.sendMessage(SpigotPrison.format("&cCancel&3: Type the word &ccancel &3to cancel, &cyou've 30 seconds."));
        
        final Player player = getSpigotPlayer( sender );

        mode = "prestige";
        id = Bukkit.getScheduler().scheduleSyncDelayedTask(SpigotPrison.getInstance(), () -> {
            if (isChatEventActive) {
                isChatEventActive = false;
                player.sendMessage(SpigotPrison.format("&cYou ran out of time, prestige cancelled."));
            }
        }, 20L * 30);
    }

    
    @Command( identifier = "gui prestiges", description = "GUI Prestiges", 
    		  aliases = {"prisonmanager prestiges"},
    		  onlyPlayers = true )
    private void prisonManagerPrestiges( CommandSender sender ) {
    	
        if ( !isPrisonConfig("prestiges") ) {
            sender.sendMessage(SpigotPrison.format("&cPrestiges are disabled. Check config.yml"));
            return;
        }
        
    	
        if ( !isPrisonConfig("prison-gui-enabled") || !isConfig("Options.Prestiges.GUI_Enabled") ){
            sender.sendMessage(SpigotPrison.format("&cGUI and/or GUI Prestiges is not enabled. Check GuiConfig.yml"));
            return;
        }
        
        if ( isConfig("Options.Prestiges.Permission_GUI_Enabled") ){
        	String perm = getConfig( "Options.Prestiges.Permission_GUI");
        	
            if ( !sender.hasPermission( perm ) ){
                sender.sendMessage(SpigotPrison.format("&cYou lack the permissions to use GUI prestiges [" + 
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
            sender.sendMessage(SpigotPrison.format("&cGUI and/or GUI Mines is not enabled. Check GuiConfig.yml"));
            return;
        }
        

        if ( isConfig("Options.Mines.Permission_GUI_Enabled") ){
        	String perm = getConfig( "Options.Mines.Permission_GUI");
        	
            if ( !sender.hasPermission( perm ) ){
                sender.sendMessage(SpigotPrison.format("&cYou lack the permissions to use GUI mines [" + 
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
    	
        if ( !isPrisonConfig("prison-gui-enabled") || !isConfig("Options.Ranks.GUI_Enabled") ) {
        	sender.sendMessage(SpigotPrison.format(
        			String.format( "&cGUI and/or GUI ranks is not enabled. Check GuiConfig.yml (%s %s)",
        					getPrisonConfig("prison-gui-enabled"), getConfig("Options.Ranks.GUI_Enabled") )));
        	return;
        }
        
        if ( isConfig("Options.Ranks.Permission_GUI_Enabled") ) {
        	String perm = getConfig( "Options.Ranks.Permission_GUI");
        	if (!sender.hasPermission( perm ) ) {
        		
        		sender.sendMessage(SpigotPrison.format("&cYou lack the permissions to use GUI ranks [" + 
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
    	
        Player player = getSpigotPlayer( sender );
        
        if ( player == null ) {
        	sender.sendMessage( SpigotPrison.format( "You cannot run the GUI from the console." ) );
        	return;
        }
        
    	SpigotPrisonGUI gui = new SpigotPrisonGUI( player );
        gui.open();
    }
}
