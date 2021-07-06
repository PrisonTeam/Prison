/*
 *  Prison is a Minecraft plugin for the prison game mode.
 *  Copyright (C) 2017-2020 The Prison Team
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package tech.mcprison.prison.spigot;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;
import java.util.stream.Collectors;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.Server;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.command.SimpleCommandMap;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredListener;

import com.cryptomorin.xseries.XBlock;
import com.cryptomorin.xseries.XMaterial;
import com.cryptomorin.xseries.messages.Titles;

import tech.mcprison.prison.Prison;
import tech.mcprison.prison.PrisonCommand;
import tech.mcprison.prison.autofeatures.AutoFeaturesFileConfig.AutoFeatures;
import tech.mcprison.prison.autofeatures.AutoFeaturesWrapper;
import tech.mcprison.prison.commands.PluginCommand;
import tech.mcprison.prison.convert.ConversionManager;
import tech.mcprison.prison.convert.ConversionResult;
import tech.mcprison.prison.file.FileStorage;
import tech.mcprison.prison.file.YamlFileIO;
import tech.mcprison.prison.internal.Player;
import tech.mcprison.prison.internal.Scheduler;
import tech.mcprison.prison.internal.World;
import tech.mcprison.prison.internal.block.PrisonBlock;
import tech.mcprison.prison.internal.block.PrisonBlockTypes;
import tech.mcprison.prison.internal.platform.Capability;
import tech.mcprison.prison.internal.platform.Platform;
import tech.mcprison.prison.internal.scoreboard.ScoreboardManager;
import tech.mcprison.prison.mines.PrisonMines;
import tech.mcprison.prison.mines.data.Mine;
import tech.mcprison.prison.mines.features.MineLinerBuilder.LinerPatterns;
import tech.mcprison.prison.mines.managers.MineManager;
import tech.mcprison.prison.modules.Module;
import tech.mcprison.prison.modules.ModuleElement;
import tech.mcprison.prison.modules.ModuleElementType;
import tech.mcprison.prison.output.BulletedListComponent;
import tech.mcprison.prison.output.ChatDisplay;
import tech.mcprison.prison.output.LogLevel;
import tech.mcprison.prison.output.Output;
import tech.mcprison.prison.ranks.PrisonRanks;
import tech.mcprison.prison.ranks.data.Rank;
import tech.mcprison.prison.ranks.data.RankPlayer;
import tech.mcprison.prison.ranks.managers.PlayerManager;
import tech.mcprison.prison.ranks.managers.RankManager;
import tech.mcprison.prison.spigot.block.OnBlockBreakEventListener.BlockBreakPriority;
import tech.mcprison.prison.spigot.commands.PrisonSpigotSellAllCommands;
import tech.mcprison.prison.spigot.game.SpigotCommandSender;
import tech.mcprison.prison.spigot.game.SpigotOfflinePlayer;
import tech.mcprison.prison.spigot.game.SpigotPlayer;
import tech.mcprison.prison.spigot.game.SpigotWorld;
import tech.mcprison.prison.spigot.placeholder.SpigotPlaceholders;
import tech.mcprison.prison.spigot.scoreboard.SpigotScoreboardManager;
import tech.mcprison.prison.spigot.sellall.SellAllBlockData;
import tech.mcprison.prison.spigot.spiget.BluesSpigetSemVerComparator;
import tech.mcprison.prison.spigot.util.ActionBarUtil;
import tech.mcprison.prison.spigot.util.SpigotYamlFileIO;
import tech.mcprison.prison.store.Storage;
import tech.mcprison.prison.util.BlockType;
import tech.mcprison.prison.util.Bounds.Edges;
import tech.mcprison.prison.util.Location;
import tech.mcprison.prison.util.PrisonJarReporter;
import tech.mcprison.prison.util.PrisonJarReporter.JarFileData;
import tech.mcprison.prison.util.Text;

/**
 * @author Faizaan A. Datoo
 */
@SuppressWarnings( "deprecation" )
public class SpigotPlatform 
	implements Platform {

    private SpigotPrison plugin;
    private List<PluginCommand> commands = new ArrayList<>();
    private Map<String, World> worlds = new HashMap<>();
    
//    @Deprecated
//    private List<Player> players = new ArrayList<>();

    private ScoreboardManager scoreboardManager;
    private Storage storage;
    
    private SpigotPlaceholders placeholders;
    
    

    /**
     * This is only for junit testing.
     */
    protected SpigotPlatform() {
    	super();
    	
    	this.plugin = null;
    	//this.scoreboardManager = new SpigotScoreboardManager();
    	//this.storage = initStorage();
    	
    	//this.placeholders = new SpigotPlaceholders();
    	
    	//ActionBarUtil.init(plugin);
    }
    
    public SpigotPlatform(SpigotPrison plugin) {
    	super();
    	
        this.plugin = plugin;
        this.scoreboardManager = new SpigotScoreboardManager();
        this.storage = initStorage();
        
        this.placeholders = new SpigotPlaceholders();
        
        ActionBarUtil.init(plugin);
    }

    private Storage initStorage() {
        String confStorage = plugin.getConfig().getString("storage", "file");
        Storage storage = new FileStorage(plugin.getDataDirectory());
        
        if (!confStorage.equalsIgnoreCase("file")) {
            Output.get().logError("Unknown file storage type in configuration \"" + confStorage
                + "\". Using file storage.");
            Output.get().logWarn(
                "Note: In this version of Prison 3, 'file' is the only supported type of storage. We're working to bring other storage types soon.");
        }
        
        return storage;
    }

    
    public org.bukkit.World getBukkitWorld(String name ) {
    	return Bukkit.getWorld(name);
    }
    
    @Override 
    public Optional<World> getWorld(String name) {
        if (name != null && worlds.containsKey(name)) {
            return Optional.of(worlds.get(name));
        }

        if (name == null || name.trim().length() == 0 || 
        		Bukkit.getWorld(name) == null) {
        	StringBuilder sb = new StringBuilder();
        	for ( org.bukkit.World bukkitWorld : Bukkit.getWorlds() ) {
        		if ( sb.length() > 0 ) {
        			sb.append( " " );
        		}
        		sb.append( bukkitWorld.getName() );
        	}
        	
        	Output.get().logWarn( "&cWorld does not exist: &a" + name + 
        			"  &7Available worlds: &a" + sb.toString() );
        	
            return Optional.empty(); // Avoid NPE
        }
        SpigotWorld newWorld = new SpigotWorld(Bukkit.getWorld(name));
        worlds.put(newWorld.getName(), newWorld);
        return Optional.of(newWorld);
    }

    @Override 
    public void getWorldLoadErrors( ChatDisplay display ) {
    
    	Optional<Module> prisonMinesOpt = Prison.get().getModuleManager().getModule( PrisonMines.MODULE_NAME );
    	
    	if ( prisonMinesOpt.isPresent() ) {
    		MineManager mineManager = ((PrisonMines) prisonMinesOpt.get()).getMineManager();
    		
    		// When finished loading the mines, then if there are any worlds that
    		// could not be loaded, dump the details:
    		List<String> unavailableWorlds = mineManager.getUnavailableWorldsListings();
    		for ( String uWorld : unavailableWorlds ) {
    			
    			display.addText( uWorld );
    		}
    		
    	}
        
    }
    
    @Override public Optional<Player> getPlayer(String name) {
    	
    	org.bukkit.entity.Player playerBukkit = Bukkit.getPlayer(name);
    	
    	if ( name != null && playerBukkit != null && !playerBukkit.getName().equalsIgnoreCase( name ) ) {
    		playerBukkit = null;
    	}

    	return Optional.ofNullable( playerBukkit == null ? null : new SpigotPlayer(playerBukkit) );
    	
//        return Optional.ofNullable(
//            players.stream().filter(player -> player.getName().equalsIgnoreCase( name)).findFirst()
//                .orElseGet(() -> {
//                	
//           // ### getting the bukkit player here!
//                	org.bukkit.entity.Player playerBukkit = Bukkit.getPlayer(name);
//                    if (playerBukkit == null) {
//                        return null;
//                    }
//                    SpigotPlayer player = new SpigotPlayer(playerBukkit);
//                    players.add(player);
//                    return player;
//                }));
    }

    @Override public Optional<Player> getPlayer(UUID uuid) {
    	org.bukkit.entity.Player playerBukkit = Bukkit.getPlayer(uuid);

    	return Optional.ofNullable( playerBukkit == null ? null : new SpigotPlayer(playerBukkit) );
    	
//        return Optional.ofNullable(
//            players.stream().filter(player -> player.getUUID().equals(uuid)).findFirst()
//                .orElseGet(() -> {
//                	
//                	
//    	// ### getting the bukkit player here!
//                	org.bukkit.entity.Player playerBukkit = Bukkit.getPlayer(uuid);
//                    if (playerBukkit == null) {
//                        return null;
//                    }
//                    SpigotPlayer player = new SpigotPlayer(playerBukkit);
//                    players.add(player);
//                    return player;
//                }));
    }

    @Override public List<Player> getOnlinePlayers() {
        return Bukkit.getOnlinePlayers().stream()
            .map(player -> getPlayer(player.getUniqueId()).get()).collect(Collectors.toList());
    }

    @Override
    public Optional<Player> getOfflinePlayer(String name) {
    	return getOfflinePlayer(name, null);
    }
    
    @Override
    public Optional<Player> getOfflinePlayer(UUID uuid) {
    	return getOfflinePlayer(null, uuid);
    }
    private Optional<Player> getOfflinePlayer(String name, UUID uuid) {
    	Player player = null;
    	
    	if ( uuid != null ) {
    		OfflinePlayer oPlayer = Bukkit.getOfflinePlayer( uuid );
    		player = (oPlayer == null ? null : new SpigotOfflinePlayer( oPlayer ) );
    		
    	}
    	
    	if ( player == null && name != null && name.trim().length() > 0 ) {
    		
    		// No hits on uuid so only compare names:
    		for ( OfflinePlayer oPlayer : Bukkit.getOfflinePlayers() ) {
    			if ( oPlayer != null && oPlayer.getName() != null && 
    					oPlayer.getName().equalsIgnoreCase( name.trim() ) ) {
    				
    				player = new SpigotOfflinePlayer( oPlayer );
    				break;
    			}
    			else if ( oPlayer == null || oPlayer.getName() == null ) {
    				Output.get().logWarn( "SpigotPlatform.getOfflinePlayer: Bukkit return a " +
    						"bad player: OfflinePlayer == null? " + (oPlayer == null) + 
    						( oPlayer == null ? "" : 
    							"  name= " + (oPlayer.getName() == null ? "null" : 
    								oPlayer.getName())));
    				
    			}
    		}
    	}
    	
    	// If player is not available, then try to get a RankPlayer instance of the player:
    	if ( player == null ) {
    		if ( PrisonRanks.getInstance() != null && PrisonRanks.getInstance().isEnabled() ) {
    			PlayerManager pm = PrisonRanks.getInstance().getPlayerManager();
    			
    			RankPlayer rankPlayer = pm.getPlayer( uuid, name );
    			if ( rankPlayer != null ) {
    				if ( uuid != null && rankPlayer.getUUID().equals( uuid ) || 
    					 uuid == null && name != null && rankPlayer.getName() != null && 
    						rankPlayer.getName().equalsIgnoreCase( name )) {
    					
    					player = rankPlayer;
    				}
    			}
    		}
    	}
    	
    	return Optional.ofNullable( player );
    	
    	
//    	for ( OfflinePlayer offP : Bukkit.getOfflinePlayers() ) {
//    		if ( name != null && offP.getName().equalsIgnoreCase( name) ||
//					  uuid != null && offP.getUniqueId().equals(uuid) ) {
//    			
//	// ### getting the offline bukkit player here!
//    			player = new SpigotOfflinePlayer( offP );
//	  			players.add(player);
//	              break;
//	  		}
//		}
//    	
//    	List<OfflinePlayer> olPlayers = Arrays.asList( Bukkit.getOfflinePlayers() );
//    	for ( OfflinePlayer offlinePlayer : olPlayers ) {
//    		if ( name != null && offlinePlayer.getName().equals(name) ||
//					  uuid != null && offlinePlayer.getUniqueId().equals(uuid) ) {
//    			player = new SpigotPlayer(offlinePlayer.getPlayer());
//    			players.add(player);
//                break;
//    		}
//		}
//    	return Optional.ofNullable( player );
    }
    
    @Override public String getPluginVersion() {
        return plugin.getDescription().getVersion();
    }

    @Override public File getPluginDirectory() {
        return plugin.getDataFolder();
    }

    @Override public void registerCommand(PluginCommand command) {
        try {
        	Command cmd = new Command(
    				command.getLabel(),
    				command.getDescription(), 
    				command.getUsage(),
    				Collections.emptyList() ) {

                @Override 
                public boolean execute(CommandSender sender, String commandLabel, String[] args) {
                    if (sender instanceof org.bukkit.entity.Player) {
                        return Prison.get().getCommandHandler()
                            .onCommand(new SpigotPlayer((org.bukkit.entity.Player) sender),
                                command, commandLabel, args);
                    }
                    
                    return Prison.get().getCommandHandler()
                    				.onCommand(new SpigotCommandSender(sender), command, commandLabel, args);
                }    
                
       			
    			@Override
				public List<String> tabComplete( CommandSender sender, String alias, String[] args )
						throws IllegalArgumentException
				{
    				
    				List<String> results = Prison.get().getCommandHandler().getTabCompleaterData().check( alias, args );
    				
    				
//    				StringBuilder sb = new StringBuilder();
//    				for ( String arg : args ) {
//    					sb.append( "[" ).append( arg ).append( "] " );
//    				}
//    				
//    				StringBuilder sbR = new StringBuilder();
//    				for ( String result : results ) {
//    					sbR.append( "[" ).append( result ).append( "] " );
//    				}
//
//    				plugin.logDebug( "### registerCommand: Command.tabComplete() : alias= %s  args= %s   results= %s", 
//    						alias, sb.toString(), sbR.toString() );

    				
    				return results;
				}


				//@Override
				public List<String> tabComplete( CommandSender sender, String alias, String[] args, 
										org.bukkit.Location location )
						throws IllegalArgumentException
				{
    				return tabComplete( sender, alias, args );
				}

            };
        	
            @SuppressWarnings( "unused" )
			boolean success = 
            			((SimpleCommandMap) plugin.commandMap.get(Bukkit.getServer()))
            				.register(command.getLabel(), "prison", cmd );
            
         // Always record the registered label:
 			if ( cmd != null ) {
 				command.setLabelRegistered( cmd.getLabel() );
 			}
        
 			getCommands().add(command);
            
//            if ( !success ) {
//            	Output.get().logInfo( "SpigotPlatform.registerCommand: %s  " +
//            			"Duplicate command. Fall back to Prison: [%s] ", command.getLabel(), 
//            			cmd.getLabel() );
//            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("unchecked") @Override 
    public void unregisterCommand(String command) {
        try {
            ((Map<String, Command>) plugin.knownCommands
                .get(plugin.commandMap.get(Bukkit.getServer()))).remove(command);
            getCommands().removeIf(pluginCommand -> pluginCommand.getLabel().equals(command));
        } catch (IllegalAccessException e) {
            e.printStackTrace(); // This should only happen if something's wrong up there.
        }
    }

    
    @Override
    public void unregisterAllCommands() {
    	List<String> cmds = new ArrayList<>();
    	for ( PluginCommand pluginCommand : getCommands() ) {
    		cmds.add( pluginCommand.getLabel() );
		}
    	
    	for ( String lable : cmds ) {
    		unregisterCommand( lable );
		}
    }
    
    public PluginCommand findCommand( String label ) {
    	PluginCommand results = null;
    	
    	for ( PluginCommand command : getCommands() ) {
    		if (command.getLabel().equalsIgnoreCase(label)) {
    			results = command;
    			break;
    		}
		}
    	return results;
    }
    
    @Override public List<PluginCommand> getCommands() {
        return commands;
    }

    @Override public void dispatchCommand(String cmd) {
        Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), cmd);
    }
    
    @Override public void dispatchCommand(tech.mcprison.prison.internal.CommandSender sender, String cmd) {
    	Bukkit.getServer().dispatchCommand( ((SpigotCommandSender) sender).getWrapper(), cmd);
    }

    @Override public Scheduler getScheduler() {
        return plugin.scheduler;
    }

    // Old method removed
    // @Override public GUI createGUI(String title, int numRows) {
    //    return new SpigotGUI(title, numRows);
    // }

//    @SuppressWarnings( "deprecation" )
	public void toggleDoor(Location doorLocation) {
        org.bukkit.Location bLoc =
            new org.bukkit.Location(Bukkit.getWorld(doorLocation.getWorld().getName()),
                doorLocation.getX(), doorLocation.getY(), doorLocation.getZ());
        Block block = bLoc.getWorld().getBlockAt(bLoc).getRelative(BlockFace.DOWN);
        if (!isDoor(block.getType())) {
            return;
        }

        boolean isOpen = XBlock.isOpen( block );
        XBlock.setOpened( block, !isOpen );
        
//        BlockState state = block.getState();
//        Openable openable = (Openable) state.getData();
//        openable.setOpen(!openable.isOpen());
//        state.setData((MaterialData) openable);
//        state.update();
        
        SpigotPrison.getInstance().getCompatibility()
        					.playIronDoorSound(block.getLocation());
    }

    @Override 
    public void log(String message, Object... format) {
        message = Text.translateAmpColorCodes(String.format(message, format));

        logCore( message );
    }

    @Override 
	public void logCore( String message )
	{
		ConsoleCommandSender sender = Bukkit.getConsoleSender();
        if (sender == null) {
            Bukkit.getLogger().info(ChatColor.stripColor(message));
        } else {
            sender.sendMessage(message);
        }
	}
    
    /**
     * This does not translate any color codes.
     */
    @Override 
    public void logPlain( String message )
    {
    	ConsoleCommandSender sender = Bukkit.getConsoleSender();
    	if (sender == null) {
    		Bukkit.getLogger().info(message);
    	} else {
    		sender.sendMessage(message);
    	}
    }

    @Override public void debug(String message, Object... format) {
        if (!plugin.debug) {
            return;
        }

        log( Output.get().format( message, LogLevel.DEBUG), format );
    }

    @Override public String runConverter() {
        File file = new File(plugin.getDataFolder().getParent(), "Prison.old");
        if (!file.exists()) {
            return Output.get().format(
                "Could not find a 'Prison.old' folder to convert. Prison 2 may not have been installed " +
                "before, so there is nothing that can be converted :)",
                LogLevel.WARNING);
        }

        List<ConversionResult> results = ConversionManager.getInstance().runConversion();

        if (results.size() == 0) {
            return Text
                .translateAmpColorCodes("&7There are no conversions to be run at this time.");
        }

        BulletedListComponent.BulletedListBuilder builder =
            new BulletedListComponent.BulletedListBuilder();
        for (ConversionResult result : results) {
            String status =
                result.getStatus() == ConversionResult.Status.Success ? "&aSuccess" : "&cFailure";
            builder.add(
                result.getAgentName() + " &8- " + status + " &7(" + result.getReason() + "&7)");
        }

        return builder.build().text();
    }

//    @SuppressWarnings( "deprecation" )
	@Override public void showTitle(Player player, String title, String subtitle, int fade) {
        org.bukkit.entity.Player play = Bukkit.getPlayer(player.getName());
//        play.sendTitle(title, subtitle);
        
        Titles.sendTitle( play, title, subtitle );
    }

    @Override public void showActionBar(Player player, String text, int duration) {
        org.bukkit.entity.Player play = Bukkit.getPlayer(player.getName());
        ActionBarUtil.sendActionBar(play, Text.translateAmpColorCodes(text), duration);
    }

    @Override public ScoreboardManager getScoreboardManager() {
        return scoreboardManager;
    }

    @Override public Storage getStorage() {
        return storage;
    }

    @Override
    public boolean shouldShowAlerts() {
        return plugin.getConfig().getBoolean("show-alerts", true);
    }

    private boolean isDoor(Material block) {
    	
    	Material acaciaDoor = Material.matchMaterial( "ACACIA_DOOR" );
    	Material birchDoor = Material.matchMaterial( "BIRCH_DOOR" );
    	Material darkOakDoor = Material.matchMaterial( "DARK_OAK_DOOR" );
    	Material ironDoor = Material.matchMaterial( "IRON_DOOR_BLOCK" );
    	Material jungleDoor = Material.matchMaterial( "JUNGLE_DOOR" );
    	Material woodenDoor = Material.matchMaterial( "WOODEN_DOOR" );
    	Material spruceDoor = Material.matchMaterial( "SPRUCE_DOOR" );    	
    	
//        return block == Material.ACACIA_DOOR || block == Material.BIRCH_DOOR
//            || block == Material.DARK_OAK_DOOR || block == Material.IRON_DOOR_BLOCK
//            || block == Material.JUNGLE_DOOR || block == Material.WOODEN_DOOR
//            || block == Material.SPRUCE_DOOR;
    	
    	return block == acaciaDoor || block == birchDoor || 
    		   block == darkOakDoor || block == ironDoor ||
    		   block == jungleDoor || block == woodenDoor ||
    		   block == spruceDoor;
    }

    @Override public Map<Capability, Boolean> getCapabilities() {
        Map<Capability, Boolean> capabilities = new HashMap<>();
        capabilities.put(Capability.ACTION_BARS, true);
        capabilities.put(Capability.GUI, true);
        return capabilities;
    }

    @Override
	public void identifyRegisteredPlugins() {
		 PrisonCommand cmdVersion = Prison.get().getPrisonCommands();
		 
		 // reset so it will reload cleanly:
		 cmdVersion.getRegisteredPlugins().clear();
//		 cmdVersion.getRegisteredPluginData().clear();
		 
		 Server server = SpigotPrison.getInstance().getServer();
		 
		 // Scan the existing jar files:
		 PrisonJarReporter jarReporter = new PrisonJarReporter();
		 jarReporter.scanForJars();
		 // jarReporter.dumpJarDetails(); // temp!
		 
        // Finally print the version after loading the prison plugin:
//        PrisonCommand cmdVersion = Prison.get().getPrisonCommands();
		 
		 boolean isPlugManPresent = false;
        
        // Store all loaded plugins within the PrisonCommand for later inclusion:
        for ( Plugin plugin : server.getPluginManager().getPlugins() ) {
        	String name = plugin.getName();
        	String version = plugin.getDescription().getVersion();
        	JarFileData pluginJarFile = jarReporter.getJarsByPluginName().get( name );
        	
        	String value = " " + name + " (" + version + 
        			( pluginJarFile == null ? "" : " " + pluginJarFile.getJavaVersion().name() ) +
        			")";
        	cmdVersion.getRegisteredPlugins().add( value );
        	
        	cmdVersion.addRegisteredPlugin( name, version );
        	
        	if ( "PlugMan".equalsIgnoreCase( name ) ) {
        		isPlugManPresent = true;
        	}
		}
        
        if ( isPlugManPresent ) {
        	ChatDisplay chatDisplay = new ChatDisplay("&d* *&5 WARNING: &d PlugMan &5 Detected! &d* *");
        	chatDisplay.addText( "&7The use of PlugMan on this Prison server will corrupt internals" );
        	chatDisplay.addText( "&7of Prison and may lead to a non-functional state, or even total" );
        	chatDisplay.addText( "&7corruption of the internal settings, the saved files, and maybe" );
        	chatDisplay.addText( "&7even the mines and surrounding areas too." );
        	chatDisplay.addText( "&7The only safe way to restart Prison is through a server restart." );
        	chatDisplay.addText( "&7Use of PlugMan at your own risk.  You have been warned. " );
        	chatDisplay.addText( "&7Prison support team has no obligation to help recover, or repair," );
        	chatDisplay.addText( "&7any troubles that may result of the use of PlugMan." );
        	chatDisplay.addText( "&bPlease Note: &3The &7/prison reload&3 commands are safe to use anytime." );
        	chatDisplay.addText( "&d* *&5 WARNING &d* *&5 WARNING &d* *&5 WARNING &d* *" );
        	
        	chatDisplay.sendtoOutputLogInfo();;
        }

        // NOTE: The following code does not actually get all of the commands that have been
        //       registered with the bukkit plugin registry.  So commenting this out and may revisit
        //       in the future.  Only tested with 1.8.8 so may work better with more recent version.
//        SimplePluginManager spm = (SimplePluginManager) Bukkit.getPluginManager();
//        
//        try {
//        	// The following code is based upon work provided by Technius:
//        	// https://bukkit.org/threads/get-all-the-available-commands.61941/
//			PluginManager manager = server.getPluginManager();
//			SimplePluginManager spm = (SimplePluginManager) manager;
//			//List<Plugin> plugins = null;
//			//Map<String, Plugin> lookupNames = null;
//			SimpleCommandMap commandMap = null;
//			Map<String, Command> knownCommands = null;
//			if (spm != null) {
//			    //Field pluginsField = spm.getClass().getDeclaredField("plugins");
//			    //Field lookupNamesField = spm.getClass().getDeclaredField("lookupNames");
//			    Field commandMapField = spm.getClass().getDeclaredField("commandMap");
//			    //pluginsField.setAccessible(true);
//			    //lookupNamesField.setAccessible(true);
//			    commandMapField.setAccessible(true);
//			    //plugins = (List<Plugin>) pluginsField.get(spm);
//			    //lookupNames = (Map<String, Plugin>) lookupNamesField.get(spm);
//			    commandMap = (SimpleCommandMap) commandMapField.get(spm);
//			    Field knownCommandsField = commandMap.getClass().getDeclaredField("knownCommands");
//			    knownCommandsField.setAccessible(true);
//			    knownCommands = (Map<String, Command>) knownCommandsField.get(commandMap);
//			}
//			 
//			if (commandMap != null) {
//			    for (Iterator<Map.Entry<String, Command>> it = knownCommands.entrySet().iterator(); it.hasNext(); ) {
//			        Map.Entry<String, Command> entry = it.next();
//			        if (entry.getValue() instanceof org.bukkit.command.PluginCommand) {
//			        	org.bukkit.command.PluginCommand c = (org.bukkit.command.PluginCommand) entry.getValue();
//			            //"c" is the command
//			            
//			            String pluginName = c.getPlugin().getName();
//			            String pluginVersion = c.getPlugin().getDescription().getVersion();
//			            String commandName = c.getName();
//			            List<String> commandAliases = c.getAliases();
//			            
//			            // Log the command and it's aliases:
//			            cmdVersion.addPluginDetails( pluginName, pluginVersion, commandName, commandAliases );
//			        }
//			    }
//			}
//		}
//		catch ( NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e ) {
//			e.printStackTrace();
//		}
        
        
	}
    
    
    
    public SpigotPlaceholders getPlaceholders() {
		return placeholders;
	}


	
	@Override
	public YamlFileIO getYamlFileIO( File yamlFile ) {
		return new SpigotYamlFileIO( yamlFile );
	}
	
	
	/**
	 * Forces the plugin config to reload.
	 * 
	 * @return
	 */
	@Override
	public void reloadConfig() {
		
		SpigotPrison.getInstance().reloadConfig();
	}
	
	@Override
	public String getConfigString( String key ) {
		return SpigotPrison.getInstance().getConfig().getString( key );
	}
	
	@Override
	public String getConfigString( String key, String defaultValue ) {
		return SpigotPrison.getInstance().getConfig().getString( key, defaultValue );
	}
	
	/**
	 * <p>This returns the boolean value that is associated with the key.
	 * It has to match on true to return a true value.  If the key does
	 * not exist, then it returns a value of false. Default value is false.
	 * </p>
	 * 
	 * @param key
	 * @return
	 */
	@Override
	public boolean getConfigBooleanFalse( String key ) {
		
		String val = SpigotPrison.getInstance().getConfig().getString( key );
		
		return ( val != null && val.trim().equalsIgnoreCase( "true" ) );
	}
	
	/**
	 * <p>Prison is now automatically enabling the new prison block model.
	 * The old block model still exists, but it has to be explicitly 
	 * enabled in config.yml.
	 * </p>
	 * 
	 * <p>No one should ever use the old block model. If there is an issue with
	 * the new model then it should be fixed and not avoided.  But if they 
	 * must, then the following must be added to the `plugins/Prison/config.yml`.
	 * </p>
	 * 
	 * <pre>
	 * # Warning: The use of the OLD prison block model will be removed
	 * #          from future releases in the near future.  This old
	 * #          model is to be used only on an emergency basis 
	 * #          until any issues with the new model have been resolved.
	 * use-old-prison-block-model: true
	 * </pre>
	 * 
	 * @return
	 */
	@Override
	public boolean isUseNewPrisonBlockModel() {
		
//		return getConfigBooleanFalse( "use-new-prison-block-model" );
		return !getConfigBooleanFalse( "use-old-prison-block-model" );
	}
	
	/**
	 * <p>This returns the boolean value that is associated with the key.
	 * It has to match on true to return a true value, but if the key does
	 * not exist, then it returns a value of true. Default value is true.
	 * </p>
	 * 
	 * @param key
	 * @return
	 */
	@Override
	public boolean getConfigBooleanTrue( String key ) {
		
		String val = SpigotPrison.getInstance().getConfig().getString( key );
		
		return ( val == null || val.trim().equalsIgnoreCase( "true" ) );
	}
	
	@Override
	public int getConfigInt( String key, int defaultValue ) {
		int results = defaultValue;
		
		String config = getConfigString(key);
		
		if ( config != null && config.trim().length() > 0) {

			try {
				results = Integer.parseInt( config );
			}
			catch ( NumberFormatException e ) {
				Output.get().logInfo( "Invalid config.yml value. The setting " +
						"%s should be an integer but had a value of [%s]", 
						key, config );
			}
			
		}
		
		return results;
	}
	
	@Override
	public long getConfigLong( String key, long defaultValue ) {
		long results = defaultValue;
		
		String config = getConfigString(key);
		
		if ( config != null && config.trim().length() > 0) {
			
			try {
				results = Long.parseLong( config );
			}
			catch ( NumberFormatException e ) {
				Output.get().logInfo( "Invalid config.yml value. The setting " +
						"%s should be an long but had a value of [%s]", 
						key, config );
			}
			
		}
		
		return results;
	}
	
	
	@Override
	public double getConfigDouble( String key, double defaultValue ) {
		double results = defaultValue;
		
		String config = getConfigString(key);
		
		if ( config != null && config.trim().length() > 0) {
			
			try {
				results = Double.parseDouble( config );
			}
			catch ( NumberFormatException e ) {
				Output.get().logInfo( "Invalid config.yml value. The setting " +
						"%s should be an double but had a value of [%s]", 
						key, config );
			}
			
		}
		
		return results;
	}

	
    /**
     * Setup hooks in to the valid prison block types.  This will be only the 
     * block types that have tested to be valid on the server that is running 
     * prison.  This provides full compatibility to the admins that if a block 
     * is listed, then it's usable.  No more guessing or finding out after the 
     * fact that a block that was used was invalid for their version of minecraft.
     */
	public PrisonBlockTypes getPrisonBlockTypes() {
		return SpigotPrison.getInstance().getPrisonBlockTypes();
	}
//	/**
//	 * This listing that is returned, should be the XMaterial enum name
//	 * for the blocks that are valid on the server.
//	 * 
//	 * @return
//	 */
//	@Override
//	public void getAllPlatformBlockTypes( List<PrisonBlock> blockTypes ) {
//		
//		SpigotUtil.getAllPlatformBlockTypes( blockTypes );
//		
//		SpigotUtil.getAllCustomBlockTypes( blockTypes );
//	}
	
	@Override
	public PrisonBlock getPrisonBlock( String blockName ) {
		
		return getPrisonBlockTypes().getBlockTypesByName( blockName );
//		return SpigotUtil.getPrisonBlock( blockName );
	}
	
	
	/**
	 * ModuleElements are Mines or Ranks, and sometimes maybe even ladders.
	 * 
	 * The purpose of this function is to link together Mines and rank (and maybe even
	 * ladders) when they cannot reference each other within their native modules. So
	 * this external linking is required.
	 * 
	 * Currently, the only linkage that is supported are:
	 * 
	 * Mine to one rank
	 * rank has many mines
	 * 
	 * 
	 */
	@Override
	public boolean linkModuleElements( ModuleElement sourceElement, 
					ModuleElementType targetElementType, String name ) {
		boolean results = false;
		
		if ( sourceElement != null) {
			
			if ( sourceElement.getModuleElementType() == ModuleElementType.MINE && 
					sourceElement instanceof Mine ) {
				// If we have an instance of a mine, then we know that module has been
				// enabled.
				
				// We need to confirm targetElementType is ranks, then we need to check to
				// ensure the rank module is active, then search for a rank with the given
				// name.  If found, then link.
				if ( targetElementType != null && targetElementType == ModuleElementType.RANK &&
						PrisonRanks.getInstance() != null && PrisonRanks.getInstance().isEnabled() ) {
					
					RankManager rm = PrisonRanks.getInstance().getRankManager();
					if ( rm != null ) {
						Rank rank = rm.getRank( name );
						
						if ( rank != null ) {
							Mine mine = (Mine) sourceElement;
							
							// Add the mine to the rank, and the rank to the mine:
							mine.setRank( rank );
							rank.getMines().add( mine );
							
							// save both the mine and the rank:
							MineManager mm = PrisonMines.getInstance().getMineManager();
							mm.saveMine( mine );
							rm.saveRank( rank );
							
							results = true;
						}
					}
				}
			}
			
			else if ( sourceElement.getModuleElementType() == ModuleElementType.RANK && 
					sourceElement instanceof Rank ) {
				// If we have an instance of a mine, then we know that module has been
				// enabled.
				
				// We need to confirm targetElementType is ranks, then we need to check to
				// ensure the rank module is active, then search for a rank with the given
				// name.  If found, then link.
				if ( targetElementType != null && targetElementType == ModuleElementType.MINE &&
						PrisonMines.getInstance() != null && PrisonMines.getInstance().isEnabled() ) {
					MineManager mm = PrisonMines.getInstance().getMineManager();
					if ( mm != null ) {
						Mine mine = mm.getMine( name );
						
						if ( mine != null ) {
							Rank rank = (Rank) sourceElement;
							
							mine.setRank( rank );
							rank.getMines().add( mine );

							// save both the mine and the rank:
							RankManager rm = PrisonRanks.getInstance().getRankManager();
							mm.saveMine( mine );
							rm.saveRank( rank );

							results = true;
						}
							
					}
				}
			}
		}
		
		return results;
	}
	

	@Override
	public boolean unlinkModuleElements( ModuleElement elementA, ModuleElement elementB ) {
		boolean results = false;
		
		unlinkModuleElement( elementA, elementB );
		
		return results;
	}
	
	
	private boolean unlinkModuleElement( ModuleElement elementA, ModuleElement elementB ) {
		boolean results = false;
		
		
		if ( elementA != null) {
			
			if ( elementA.getModuleElementType() == ModuleElementType.MINE && 
					elementA instanceof Mine ) {
				
				// We need to confirm targetElementType is ranks, then we need to check to
				// ensure the rank module is active, then search for a rank with the given
				// name.  If found, then link.
				if ( elementB != null && elementB.getModuleElementType() == ModuleElementType.RANK ) {
					
					RankManager rm = PrisonRanks.getInstance().getRankManager();
					if ( rm != null ) {
						// To remove the rank from the mine, just set the value to null:
						Mine mine = (Mine) elementA;
						mine.setRank( null );
						
						Rank rank = (Rank) elementB;
						rank.getMines().remove( mine );

						// save both the mine and the rank:
						MineManager mm = PrisonMines.getInstance().getMineManager();
						mm.saveMine( mine );
						rm.saveRank( rank );

					}
				}
			}
			
			else if ( elementA.getModuleElementType() == ModuleElementType.RANK && 
					elementA instanceof Rank ) {
				// If we have an instance of a mine, then we know that module has been
				// enabled.
				
				// We need to confirm targetElementType is ranks, then we need to check to
				// ensure the rank module is active, then search for a rank with the given
				// name.  If found, then link.
				if ( elementB != null && elementB.getModuleElementType() == ModuleElementType.MINE ) {
					MineManager mm = PrisonMines.getInstance().getMineManager();
					if ( mm != null ) {
						Mine mine = (Mine) elementB;
						
						if ( mine != null ) {
							Rank rank = (Rank) elementA;
							
							mine.setRank( rank );
							rank.getMines().remove( mine );

							// save both the mine and the rank:
							RankManager rm = PrisonRanks.getInstance().getRankManager();
							mm.saveMine( mine );
							rm.saveRank( rank );

							results = true;
						}
							
					}
				}
			}
		}

		
		return results;
	}
	
	/**
	 * <p>This function will create the specified ModuleElement.  It will create the minimal 
	 * possible element, of which, the settings can then be changed.  If the create was
	 * successful, then it will return the element, otherwise it will return a null.
	 * </p>
	 * 
	 * <p>Minimal mines will be a virtual mine, but with the tag set.
	 * </p>
	 * 
	 * <p>Minimal rank will be placed on the default ladder with a cost of zero.
	 * </p>
	 * 
	 */
	public ModuleElement createModuleElement( tech.mcprison.prison.internal.CommandSender sender, 
					ModuleElementType elementType, String name, String tag, String accessPermission ) {
		ModuleElement results = null;
		
		if ( elementType == ModuleElementType.MINE && 
								PrisonMines.getInstance() != null && PrisonMines.getInstance().isEnabled() ) {
			MineManager mm = PrisonMines.getInstance().getMineManager();
			Mine mine = mm.getMine( name );
			if ( mine == null ) {
				PrisonMines.getInstance().getMinesCommands().createCommand( sender, "virtual", name );
				mine = mm.getMine( name );
				mine.setTag( tag );
				
				if ( accessPermission != null && !accessPermission.trim().isEmpty() ) {
					mine.setAccessPermission( accessPermission );
				}
				
				results = mine;
			}
		}
		else if ( elementType == ModuleElementType.RANK &&
								PrisonRanks.getInstance() != null && PrisonRanks.getInstance().isEnabled() ) {
			RankManager rm = PrisonRanks.getInstance().getRankManager();
			rm.getRanksCommands().createRank( sender, name, 0, "default", tag );
			
			Rank rank = rm.getRank( name );
			
			results = rank;
		}
		
		return results;
	}
	
	@Override
	public int getModuleElementCount( ModuleElementType elementType ) {
		int results = 0;
		
		if ( elementType == ModuleElementType.MINE &&
							PrisonMines.getInstance() != null && PrisonMines.getInstance().isEnabled() ) {
			MineManager mm = PrisonMines.getInstance().getMineManager();
			results = mm.getMines().size();
		}
		else if ( elementType == ModuleElementType.RANK &&
							PrisonRanks.getInstance() != null && PrisonRanks.getInstance().isEnabled() ) {
			RankManager rm = PrisonRanks.getInstance().getRankManager();
			results = rm.getRanks().size();
		}
		
		return results;
	}
	
	@Override
	public ModuleElement getModuleElement( ModuleElementType elementType, String elementName ) {
		ModuleElement results = null;
		
		if ( elementType == ModuleElementType.MINE &&
							PrisonMines.getInstance() != null && PrisonMines.getInstance().isEnabled() ) {
			MineManager mm = PrisonMines.getInstance().getMineManager();
			results = mm.getMine( elementName );
		}
		else if ( elementType == ModuleElementType.RANK &&
							PrisonRanks.getInstance() != null && PrisonRanks.getInstance().isEnabled() ) {
			RankManager rm = PrisonRanks.getInstance().getRankManager();
			results = rm.getRank( elementName );
		}
		
		return results;
	}
	/**
	 * <p>This function takes a CommandSender for a player, and tries to find a mine
	 * that would be associated with that player.  This is a very complex process
	 * since mines don't have to be associated with mines, and you can have multiple 
	 * mines per rank.  This only processes ranks on the default ladder. Both 
	 * the Ranks and Mines modules must be enabled too.
	 * </p>
	 * 
	 * <p>First, the CommandSender has to be converted to a Player object, then
	 * mapped to a RankPlayer.  This process can only happen with a RankPlayer object
	 * since that is where a Player is associated with ranks. 
	 * </p>
	 * 
	 * <p>If the player has a rank on the default ladder, then that rank will be
	 * used to continue the search for the mine.  If there is more than one mine 
	 * associated with the rank, then it tries to find a mine with the same name.
	 * Otherwise it will take the first mine in the list.
	 * </p>
	 * 
	 * <p>The result, if not null, is the best mine that can be found. It is recognized
	 * that if multiple mines exist, then it may not always be the one intended.
	 * </p>
	 * 
	 * @param sender
	 * @return
	 */
	@Override
	public ModuleElement getPlayerDefaultMine( tech.mcprison.prison.internal.CommandSender sender ) {
		Mine results = null;
		
		if ( PrisonMines.getInstance() != null && PrisonMines.getInstance().isEnabled() &&
				PrisonRanks.getInstance() != null && PrisonRanks.getInstance().isEnabled() 
				) {
			
    		PlayerManager pm = PrisonRanks.getInstance().getPlayerManager();
    		Player player = pm.getPlayer( sender );
    		RankPlayer rankPlayer = pm.getPlayer( player );

    		if ( rankPlayer != null ) {
    			Rank rank = rankPlayer.getRank( "default" );
    			
    			if ( rank != null ) {
    				
    				// First check to see if there are any mines linked to a rank:
    				if ( rank.getMines() != null && rank.getMines().size() > 0 ) {
    					
    					for ( ModuleElement mineME : rank.getMines() ) {
							if ( mineME.getName().equalsIgnoreCase( rank.getName() )) {
								// Found a mine with the same name as the rank. Give high priority:
								
								results = (Mine) mineME;
								break;
							}
						}
    					
    					if ( results == null ) {
    						results = (Mine) rank.getMines().get(0);
    					}
    				}
    				
    				if ( results == null ) {
    					// Check to see if there are any mines with the same name:
    					
    					MineManager mm = PrisonMines.getInstance().getMineManager();
    					results = mm.getMine( rank.getName() );
    				}
    				
    			}
    			
    		}
			
		}
		
		return results;
	}
	
	
	/**
	 * <p>This function will take the player and check to see if they have access to the mine
	 * based upon ranks.  The mine must be linked to a rank, and it must be either the player's
	 * current rank, or a prior rank of the player.
	 * </p>
	 * 
	 * <p>If the ranks module or mines module are disabled, or if a mine is not linked to a rank, 
	 * then this function will return a value of false.
	 * </p>
	 * 
	 * <p>Please note that this function does not check to see if this feature should, or should
	 * not be enabled, or how this feature should be used, or in what situation.  That should be
	 * handled by the caller to this function.
	 * </p>
	 * 
	 * @param player
	 * @param mine
	 * @return
	 */
	@Override
	public boolean isMineAccessibleByRank( Player player, ModuleElement mine ) {
		boolean isAccessible = false;
		
		if ( PrisonMines.getInstance() != null && PrisonMines.getInstance().isEnabled() &&
				PrisonRanks.getInstance() != null && PrisonRanks.getInstance().isEnabled() &&
				player != null && 
				mine != null && mine instanceof Mine && ((Mine) mine).getRank() != null
				) {
			
			Rank targetRank = (Rank) ((Mine) mine).getRank();
			
    		PlayerManager pm = PrisonRanks.getInstance().getPlayerManager();
    		RankPlayer rankPlayer = pm.getPlayer( player );

    		if ( rankPlayer != null ) {
    			
    			isAccessible = rankPlayer.hasAccessToRank( targetRank );
    			
//    			Rank rank = rankPlayer.getRank( "default" );
//    			if ( rank != null ) {
//    				
//    				isAccessible = rank.equals( targetRank );
//    				Rank priorRank = rank.getRankPrior();
//    				
//    				while ( !isAccessible && priorRank != null ) {
//    					
//    					isAccessible = priorRank.equals( targetRank );
//    					priorRank = priorRank.getRankPrior();
//    				}
//    			}
    		}
		}
		
		return isAccessible;
	}

	/**
	 * This function will try to enable Access by Rank on all mines that are linked to ranks.
	 * This will be applied to mines and ranks that would not normally be generated by 
	 * the autoConfigure process.
	 */
	@Override
	public void autoCreateConfigureMines() {
		
		MineManager mm = PrisonMines.getInstance().getMineManager();
		List<Mine> mines = mm.getMines();
		
		for ( Mine mine : mines ) {
			
			// Setup access by rank:
			
			if ( mine.getRank() != null ) {
				
				mine.setTpAccessByRank( true );
				mine.setMineAccessByRank( true );
				
			}
		}
			

	}
	
	/**
	 * <p>This function assigns blocks to all of the generated mines.  It is intended that
	 * these mines were just created by the autoCreate function which will ensure that
	 * no blocks have yet been assigned to any mines.  Because it is assumed that no 
	 * blocks are in any of these mines, no check is performed to eliminate possible 
	 * duplicates or to prevent total chance from exceeding 100.0%.
	 * </p>
	 * 
	 * <p>This function uses a sliding window of X number of block types to assign.  
	 * The number of block types is defined by the percents List in both the number of
	 * blocks, but also the percentage for each block.  
	 * The current List has 6 types per mine, with the first few and last few having less.
	 * The percents are assigned to the most valuable to the least valuable blocks:
	 * 5%, 10%, 20%, 20%, 20%, and 25%.
	 * </p>
	 * 
	 * <p>This function works with the old and new prison block models, and uses the
	 * exact same blocks for consistency.
	 * </p>
	 * 
	 */
	@Override
	public void autoCreateMineBlockAssignment( List<String> rankMineNames, boolean forceKeepBlocks ) {
		List<String> blockList = new ArrayList<>(); 
		
		
		// Turn on sellall:
		SpigotPrison.getInstance().getConfig().set( "sellall", true );
		
		
		PrisonSpigotSellAllCommands sellall = PrisonSpigotSellAllCommands.get();
		if ( sellall != null && PrisonSpigotSellAllCommands.isEnabled()) {
			
			// Setup all the prices in sellall:
			for ( SellAllBlockData xMatCost : buildBlockListXMaterial() ) {
				
				// Add blocks to sellall:
				sellall.sellAllAddCommand( xMatCost.getBlock(), xMatCost.getPrice() );
			}
		}
		
		
		
        if ( Prison.get().getPlatform().isUseNewPrisonBlockModel() ) {
        	
        	for ( SellAllBlockData xMatCost : buildBlockListXMaterial() ) {
        		
        		// Add only the primary blocks to this blockList which will be used to generate the
        		// mine's block contents:
				if ( xMatCost.isPrimary() ) {
					blockList.add( xMatCost.getBlock().name() );
				}
			}
        	
//        	blockList = buildBlockListXMaterial();
        }
        else {
        	blockList = buildBlockListBlockType();
        }
		
		MineManager mm = PrisonMines.getInstance().getMineManager();
		//List<Mine> mines = mm.getMines();
		
		List<Double> percents = new ArrayList<>();
		percents.add(5d);
		percents.add(10d);
		percents.add(20d);
		percents.add(20d);
		percents.add(20d);
		percents.add(25d);
		int mineBlockSize = percents.size();
		
		int startPos = 1;
		for ( String mineName : rankMineNames ) {
			
			Mine mine = mm.getMine( mineName );
			
			boolean hasBlocks = mine.getPrisonBlocks().size() > 0 || mine.getBlocks().size() > 0;
			
			// If the mines already has blocks, log them, then clear them since this will replace them:
			if ( hasBlocks && !forceKeepBlocks ) {
				String oldBlockList = mine.getBlockListString();
				
				String message = String.format( "Mine: %s  Removed old blocks: %s", 
						mine.getName(), oldBlockList );
				
				Output.get().logInfo( message );
				
				mine.getPrisonBlocks().clear();
				mine.getBlocks().clear();
			}
			
			else if ( hasBlocks && forceKeepBlocks ) {
				String oldBlockList = mine.getBlockListString();

				String message = String.format( "Mine: %s  Keeping existing blocks: %s", 
						mine.getName(), oldBlockList );
				
				Output.get().logInfo( message );
				
				continue;
			}
			
			 List<String> mBlocks = mineBlockList( blockList, startPos++, mineBlockSize );
			
			 // If startPos > percents.size(), which means we are past the initial 
			 // ramp up to the full variety of blocks per mine.  At that point, if 
			 // percents is grater than mBlocks, then we must trim the first entry
			 // from percents so that the most valuable block is able to have more
			 // than just 5% allocation. 
			 // This should only happen at the tail end of processing and will only
			 // have a decrease by one per mine so there should never be a need to
			 // to check more than once, or remove more than one.
			 if ( startPos > percents.size() && percents.size() > mBlocks.size() ) {
				 percents.remove( 0 );
			 }
			 
			double total = 0;
			for ( int i = 0; i < mBlocks.size(); i++ )
			{
				
				if ( Prison.get().getPlatform().isUseNewPrisonBlockModel() ) {
					
					PrisonBlock prisonBlock = Prison.get().getPlatform().getPrisonBlock( mBlocks.get( i ) );
	            	if ( prisonBlock != null ) {
	            	
	            		prisonBlock.setChance( percents.get( i ) );
	            		prisonBlock.setBlockCountTotal( 0 );
	            		
	            		mine.getPrisonBlocks().add( prisonBlock );
	            		
	            		total += prisonBlock.getChance();
	            		
	            		// If this is the last block and the totals are not 100%, then
	            		// add the balance to the last block.
	            		if ( i == (mBlocks.size() - 1) && total < 100.0d ) {
	            			double remaining = 100.0d - total;
	            			prisonBlock.setChance( remaining + prisonBlock.getChance() );
	            		}
	            	}
	            	else {
	            		Output.get().logInfo(
	            				String.format( "AutoConfigure block assignment failure: New Block Model: " +
	            						"Unable to map to a valid PrisonBlock for this version of mc. [%s]", 
	            						mBlocks.get( i ) ) );
	            	}
				}
				else {
					
					tech.mcprison.prison.mines.data.BlockOld block = 
							new tech.mcprison.prison.mines.data.BlockOld( 
									mBlocks.get( i ), percents.get( i ), 0 );
					
					mine.getBlocks().add( block );
					
					total += block.getChance();
					
					// If this is the last block and the totals are not 100%, then
					// add the balance to the last block.
					if ( i == (mBlocks.size() - 1) && total < 100.0d ) {
						double remaining = 100.0d - total;
						block.setChance( remaining + block.getChance() );
					}
				}
				
			}
			
			mm.saveMine( mine );
			
			String mineBlockListing = mine.getBlockListString();
			Output.get().logInfo( mineBlockListing );
		}
	}
	
	@Override
	public void autoCreateMineLinerAssignment( List<String> rankMineNames, 
			boolean forceLinersBottom, boolean forceLinersWalls ) {
		
		MineManager mm = PrisonMines.getInstance().getMineManager();
//		List<Mine> mines = mm.getMines();
		
		for ( String mineName : rankMineNames ) {
			
			Mine mine = mm.getMine( mineName );

			String mineLinerData = mine.getLinerData().toSaveString();
			boolean createLiner = mineLinerData.trim().isEmpty();
			
			if ( createLiner ) {
				mine.getLinerData().setLiner( Edges.walls, getRandomLinerType(), forceLinersWalls );
				mine.getLinerData().setLiner( Edges.bottom, getRandomLinerType(), forceLinersBottom );

				mineLinerData = mine.getLinerData().toSaveString();
				
				mm.saveMine( mine );
			}

			String message = String.format( "Mine Liner status: %s %s : %s", 
					mine.getName(),
					(createLiner ? "(Created)" : "(AlreadyExists)"),
					mineLinerData );
			Output.get().logInfo( message );
		}	
	}
	
	private LinerPatterns getRandomLinerType() {
		LinerPatterns[] liners = LinerPatterns.values();
		
		// Exclude the last LinerPattern since it is "repair".
		int pos = new Random().nextInt( liners.length - 1 );
		return liners[pos];
	}
	
	/**
	 * This function grabs a rolling sub set of blocks from the startPos and working backwards 
	 * up to the specified length. The result set will be less than the specified length if at
	 * the beginning of the list, or at the end.
	 *  
	 * @param blockList
	 * @param startPos
	 * @param length
	 * @return
	 */
	protected List<String> mineBlockList( List<String> blockList, int startPos, int length ) {

		List<String> results = new ArrayList<>();
		for (int i = (startPos >= blockList.size() ? blockList.size() - 1 : startPos); i >= 0 && i >= startPos - length + 1; i--) {
			results.add( blockList.get( i ) );
		}
		
		return results;
	}
	
	
	/**
	 * This listing of blocks is based strictly upon XMaterial. 
	 * This is the preferred list to use with the new block model.
	 * 
	 * @return
	 */
	public List<SellAllBlockData> buildBlockListXMaterial() {
		List<SellAllBlockData> blockList = new ArrayList<>();
		
		blockList.add( new SellAllBlockData( XMaterial.COBBLESTONE, 4, true) );
		blockList.add( new SellAllBlockData( XMaterial.ANDESITE, 5, true) );
		blockList.add( new SellAllBlockData( XMaterial.DIORITE, 6, true) );
		blockList.add( new SellAllBlockData( XMaterial.COAL_ORE, 13, true) );
		blockList.add( new SellAllBlockData( XMaterial.GRANITE, 8, true) );

		blockList.add( new SellAllBlockData( XMaterial.STONE, 9, true) );
		blockList.add( new SellAllBlockData( XMaterial.IRON_ORE, 18, true) );
		blockList.add( new SellAllBlockData( XMaterial.POLISHED_ANDESITE, 7, true) );
		blockList.add( new SellAllBlockData( XMaterial.GOLD_ORE, 45, true) );
		blockList.add( new SellAllBlockData( XMaterial.MOSSY_COBBLESTONE, 29, true) );

		
		blockList.add( new SellAllBlockData( XMaterial.COAL_BLOCK, 135, true) );
		blockList.add( new SellAllBlockData( XMaterial.IRON_BLOCK, 190, true) );
		blockList.add( new SellAllBlockData( XMaterial.LAPIS_ORE, 100, true) );
		blockList.add( new SellAllBlockData( XMaterial.REDSTONE_ORE, 45, true) );
		blockList.add( new SellAllBlockData( XMaterial.DIAMOND_ORE, 200, true) );

		blockList.add( new SellAllBlockData( XMaterial.EMERALD_ORE, 250, true) );
		blockList.add( new SellAllBlockData( XMaterial.GOLD_BLOCK, 450, true) );
		blockList.add( new SellAllBlockData( XMaterial.LAPIS_BLOCK, 950, true) );
		blockList.add( new SellAllBlockData( XMaterial.REDSTONE_BLOCK, 405, true) );
		blockList.add( new SellAllBlockData( XMaterial.DIAMOND_BLOCK, 2000, true) );
		
		blockList.add( new SellAllBlockData( XMaterial.EMERALD_BLOCK, 2250, true) );
		
		
		
//		blockList.add( XMaterial.SLIME_BLOCK.name() );
		blockList.add( new SellAllBlockData( XMaterial.OBSIDIAN, 450 ) );
		
		
		
		// these are not used to generate the mine blocks:
		blockList.add( new SellAllBlockData( XMaterial.CLAY, 12 ) );
		blockList.add( new SellAllBlockData( XMaterial.GRAVEL, 3 ) );
		blockList.add( new SellAllBlockData( XMaterial.SAND, 6 ) );
		blockList.add( new SellAllBlockData( XMaterial.DIRT, 4 ) );
		blockList.add( new SellAllBlockData( XMaterial.COARSE_DIRT, 7 ) );
		blockList.add( new SellAllBlockData( XMaterial.PODZOL, 6 ) );
		blockList.add( new SellAllBlockData( XMaterial.RED_SAND, 9 ) );
		blockList.add( new SellAllBlockData( XMaterial.BEDROCK, 500 ) );
		blockList.add( new SellAllBlockData( XMaterial.SANDSTONE, 3 ) );
		
		blockList.add( new SellAllBlockData( XMaterial.POLISHED_ANDESITE, 7 ) );
		blockList.add( new SellAllBlockData( XMaterial.POLISHED_DIORITE, 8 ) );
		blockList.add( new SellAllBlockData( XMaterial.POLISHED_GRANITE, 9 ) );
		blockList.add( new SellAllBlockData( XMaterial.CHISELED_NETHER_BRICKS, 39 ) );
		blockList.add( new SellAllBlockData( XMaterial.CHISELED_RED_SANDSTONE, 11 ) );
		blockList.add( new SellAllBlockData( XMaterial.CHISELED_STONE_BRICKS, 11 ) );
		blockList.add( new SellAllBlockData( XMaterial.CUT_RED_SANDSTONE, 13 ) );
		blockList.add( new SellAllBlockData( XMaterial.CUT_SANDSTONE, 10 ) );

		
		
		blockList.add( new SellAllBlockData( XMaterial.NETHER_QUARTZ_ORE, 34, true) );
		blockList.add( new SellAllBlockData( XMaterial.QUARTZ, 34 ) );
		blockList.add( new SellAllBlockData( XMaterial.QUARTZ_BLOCK, 136, true) );
		blockList.add( new SellAllBlockData( XMaterial.QUARTZ_SLAB, 68, true) );

		blockList.add( new SellAllBlockData( XMaterial.CHISELED_QUARTZ_BLOCK, 136 ) );
		blockList.add( new SellAllBlockData( XMaterial.QUARTZ_BRICKS, 136 ) );
		blockList.add( new SellAllBlockData( XMaterial.QUARTZ_PILLAR, 136 ) );
		blockList.add( new SellAllBlockData( XMaterial.SMOOTH_QUARTZ, 136 ) );


		blockList.add( new SellAllBlockData( XMaterial.SMOOTH_RED_SANDSTONE, 14 ) );
		blockList.add( new SellAllBlockData( XMaterial.SMOOTH_SANDSTONE, 14 ) );
		blockList.add( new SellAllBlockData( XMaterial.SMOOTH_STONE, 14 ) );
		
		
		blockList.add( new SellAllBlockData( XMaterial.CHARCOAL, 16 ) );
		blockList.add( new SellAllBlockData( XMaterial.CRACKED_NETHER_BRICKS, 16 ) );
		blockList.add( new SellAllBlockData( XMaterial.CRACKED_STONE_BRICKS, 14 ) );
		
		blockList.add( new SellAllBlockData( XMaterial.EMERALD, 14 ) );
		blockList.add( new SellAllBlockData( XMaterial.END_STONE, 14 ) );
		blockList.add( new SellAllBlockData( XMaterial.END_STONE_BRICKS, 14 ) );

		
		blockList.add( new SellAllBlockData( XMaterial.FLINT, 9 ) );
		
		
		blockList.add( new SellAllBlockData( XMaterial.LAPIS_LAZULI, 14 ) );
		blockList.add( new SellAllBlockData( XMaterial.MOSSY_STONE_BRICKS, 14 ) );
		
		
		blockList.add( new SellAllBlockData( XMaterial.PRISMARINE_SHARD, 13 ) );
		blockList.add( new SellAllBlockData( XMaterial.PRISMARINE, 52 ) );
		
		blockList.add( new SellAllBlockData( XMaterial.PRISMARINE_BRICKS, 52 ) );
		blockList.add( new SellAllBlockData( XMaterial.PRISMARINE_BRICK_SLAB, 52 ) );
		blockList.add( new SellAllBlockData( XMaterial.PRISMARINE_CRYSTALS, 37 ) );
		blockList.add( new SellAllBlockData( XMaterial.DARK_PRISMARINE, 52 ) );
		blockList.add( new SellAllBlockData( XMaterial.DARK_PRISMARINE_SLAB, 52 ) );
		
		blockList.add( new SellAllBlockData( XMaterial.PURPUR_BLOCK, 14 ) );
		blockList.add( new SellAllBlockData( XMaterial.PURPUR_PILLAR, 14 ) );

		
		
//		blockList.add( new SellAllBlockData( XMaterial.SEA_LANTERN, 98 ) );

		blockList.add( new SellAllBlockData( XMaterial.TERRACOTTA, 10 ) );

		
		blockList.add( new SellAllBlockData( XMaterial.ACACIA_LOG, 7 ) );
		blockList.add( new SellAllBlockData( XMaterial.BIRCH_LOG, 7 ) );
		blockList.add( new SellAllBlockData( XMaterial.DARK_OAK_LOG, 7 ) );
		blockList.add( new SellAllBlockData( XMaterial.JUNGLE_LOG, 7 ) );
		blockList.add( new SellAllBlockData( XMaterial.OAK_LOG, 7 ) );
		blockList.add( new SellAllBlockData( XMaterial.SPRUCE_LOG, 7 ) );
		blockList.add( new SellAllBlockData( XMaterial.ACACIA_PLANKS, 28 ) );
		blockList.add( new SellAllBlockData( XMaterial.BIRCH_PLANKS, 28 ) );
		blockList.add( new SellAllBlockData( XMaterial.DARK_OAK_PLANKS, 28 ) );
		blockList.add( new SellAllBlockData( XMaterial.JUNGLE_PLANKS, 28 ) );
		blockList.add( new SellAllBlockData( XMaterial.OAK_PLANKS, 28 ) );
		blockList.add( new SellAllBlockData( XMaterial.SPRUCE_PLANKS, 28 ) );
		
		blockList.add( new SellAllBlockData( XMaterial.ACACIA_WOOD, 7 ) );
		blockList.add( new SellAllBlockData( XMaterial.BIRCH_WOOD, 7 ) );
		blockList.add( new SellAllBlockData( XMaterial.DARK_OAK_WOOD, 7 ) );
		blockList.add( new SellAllBlockData( XMaterial.JUNGLE_WOOD, 7 ) );
		blockList.add( new SellAllBlockData( XMaterial.OAK_WOOD, 7 ) );
		blockList.add( new SellAllBlockData( XMaterial.SPRUCE_WOOD, 7 ) );
		

		
		blockList.add( new SellAllBlockData( XMaterial.IRON_NUGGET, 3 ) );
		blockList.add( new SellAllBlockData( XMaterial.IRON_INGOT, 27 ) );
		
		blockList.add( new SellAllBlockData( XMaterial.GOLD_NUGGET, 12 ) );
		blockList.add( new SellAllBlockData( XMaterial.GOLD_INGOT, 108 ) );
		
		blockList.add( new SellAllBlockData( XMaterial.REDSTONE, 45 ) );
		

		blockList.add( new SellAllBlockData( XMaterial.GLOWSTONE, 52 ) );
		blockList.add( new SellAllBlockData( XMaterial.GLOWSTONE_DUST, 14 ) );

		
		
		blockList.add( new SellAllBlockData( XMaterial.COAL, 15 ) );
		blockList.add( new SellAllBlockData( XMaterial.DIAMOND, 200 ) );
		
		blockList.add( new SellAllBlockData( XMaterial.SUGAR_CANE, 13 ) );
		blockList.add( new SellAllBlockData( XMaterial.SUGAR, 13 ) );
		blockList.add( new SellAllBlockData( XMaterial.PAPER, 13 ) );
		
		return blockList;
	}
	
	/**
	 * This listing of blocks is based strictly upon the old prison's block
	 * model.
	 * 
	 * Please note, that right now these names match exactly with XMaterial only
	 * because I renamed a few of them to make them match.  But if more are added
	 * in the future, then there may be mismatches.
	 * 
	 * @return
	 */
	protected List<String> buildBlockListBlockType() {
		List<String> blockList = new ArrayList<>();
		
		blockList.add( BlockType.COBBLESTONE.name() );
		blockList.add( BlockType.ANDESITE.name() );
		blockList.add( BlockType.DIORITE.name() );
		blockList.add( BlockType.COAL_ORE.name() );
		
		blockList.add( BlockType.GRANITE.name() );
		blockList.add( BlockType.STONE.name() );
		blockList.add( BlockType.IRON_ORE.name() );
		blockList.add( BlockType.POLISHED_ANDESITE.name() );
		
//		blockList.add( BlockType.POLISHED_DIORITE.name() );
//		blockList.add( BlockType.POLISHED_GRANITE.name() );
		blockList.add( BlockType.GOLD_ORE.name() );
		
		
		blockList.add( BlockType.MOSSY_COBBLESTONE.name() );
		blockList.add( BlockType.COAL_BLOCK.name() );
		blockList.add( BlockType.NETHER_QUARTZ_ORE.name() );
		blockList.add( BlockType.IRON_BLOCK.name() );
		
		blockList.add( BlockType.LAPIS_ORE.name() );
		blockList.add( BlockType.REDSTONE_ORE.name() );
		blockList.add( BlockType.DIAMOND_ORE.name() );
		
		blockList.add( BlockType.QUARTZ_BLOCK.name() );
		blockList.add( BlockType.EMERALD_ORE.name() );
		
		blockList.add( BlockType.GOLD_BLOCK.name() );
		blockList.add( BlockType.LAPIS_BLOCK.name() );
		blockList.add( BlockType.REDSTONE_BLOCK.name() );
		
//		blockList.add( BlockType.SLIME_BLOCK.name() );
		blockList.add( BlockType.DIAMOND_BLOCK.name() );
		blockList.add( BlockType.EMERALD_BLOCK.name() );
		
		return blockList;
	}
	
	@Override
	public List<String> getActiveFeatures() {
		List<String> results = new ArrayList<>();
		
		
    	
    	AutoFeaturesWrapper afw = AutoFeaturesWrapper.getInstance();
    	
    	boolean isAutoManagerEnabled = afw.isBoolean( AutoFeatures.isAutoManagerEnabled );
    	results.add( String.format("AutoManager Enabled:&b %s", isAutoManagerEnabled) );
    	
    	if ( isAutoManagerEnabled ) {
    		
    		String bbePriority = afw.getMessage( AutoFeatures.blockBreakEventPriority );
    		BlockBreakPriority blockBreakPriority = BlockBreakPriority.fromString( bbePriority );
    		results.add( String.format(".   Block Break Event Priority:&b %s", 
    				blockBreakPriority.name() ) );
    		
    		String tebePriority = afw.getMessage( AutoFeatures.TokenEnchantBlockExplodeEventPriority );
    		BlockBreakPriority tebEventPriority = BlockBreakPriority.fromString( tebePriority );
    		results.add( String.format(".   Token Enchant BlockExplodeEvent Priority:&b %s", 
    				tebEventPriority.name() ) );
    		
    		String cebuePriority = afw.getMessage( AutoFeatures.CrazyEnchantsBlastUseEventPriority );
    		BlockBreakPriority cebuEventPriority = BlockBreakPriority.fromString( cebuePriority );
    		results.add( String.format(".   Crazy Enchant BlastUseEvent Priority:&b %s", 
    				cebuEventPriority.name() ) );
    		
    		String zbsePriority = afw.getMessage( AutoFeatures.ZenchantmentsBlockShredEventPriority );
    		BlockBreakPriority zbsEventPriority = BlockBreakPriority.fromString( zbsePriority );
    		results.add( String.format(".   Zenchantments BlockShredEvent Priority:&b %s", 
    				zbsEventPriority.name() ) );
    		
    		results.add( " " );
    		
    		boolean isAutoPickup = afw.isBoolean( AutoFeatures.autoPickupEnabled );
    		results.add( String.format(".   Auto Pickup:&b %s", isAutoPickup ) );
    		
    		results.add( String.format(".   Auto Smelt:&b %s", (!isAutoPickup ? "disabled" :
    			afw.isBoolean( AutoFeatures.autoSmeltEnabled ))) );
    		results.add( String.format(".   Auto Block:&b %s", (!isAutoPickup ? "disabled" :
    			afw.isBoolean( AutoFeatures.autoBlockEnabled ))) );
    		
    		
    		results.add( String.format(".   Handle Normal Drops:&b %s", (isAutoPickup ? "disabled by AutoPickup" :
    			afw.isBoolean( AutoFeatures.handleNormalDropsEvents ))) );
    		results.add( String.format(".   Normal Drop Smelt:&b %s", (isAutoPickup ? "disabled" :
    			afw.isBoolean( AutoFeatures.normalDropSmelt ))) );
    		results.add( String.format(".   Normal Drop Block:&b %s", (isAutoPickup ? "disabled" :
    			afw.isBoolean( AutoFeatures.normalDropBlock ))) );
    		
    		
    		
    		results.add( " " );
    		
    		results.add( String.format("+.   Calculate Durability:&b %s", 
    				afw.isBoolean( AutoFeatures.isCalculateDurabilityEnabled )) );
    		
    		
    		boolean isCalcFortune = afw.isBoolean( AutoFeatures.isCalculateFortuneEnabled );
    		results.add( String.format(".   Calculate Fortune:&b %s", isCalcFortune) );
    		results.add( String.format("+.  .  Max Fortune Multiplier:&b %s", 
    				afw.getInteger( AutoFeatures.fortuneMultiplierMax )) );
    		
    		boolean isExtendedBukkitFortune = afw.isBoolean( AutoFeatures.isExtendBukkitFortuneCalculationsEnabled );
    		results.add( String.format(".  .  Extended Bukkit Fortune Enabled:&b %s", 
    				isExtendedBukkitFortune) );
    		results.add( String.format("+.  .  Extended Bukkit Fortune Factor Percent Range Low:&b %s", 
    				afw.getInteger( AutoFeatures.extendBukkitFortuneFactorPercentRangeLow )) );
    		results.add( String.format("+.  .  Extended Bukkit Fortune Factor Percent Range High:&b %s", 
    				afw.getInteger( AutoFeatures.extendBukkitFortuneFactorPercentRangeHigh )) );
    		
    		
    		results.add( String.format(".  .  Calculate Alt Fortune Enabled:&b %s", ( isExtendedBukkitFortune ? "disabled" :
    			afw.isBoolean( AutoFeatures.isCalculateAltFortuneEnabled ))) );
    		results.add( String.format("+.  .  Calculate Alt Fortune on all Blocks:&b %s", 
    				afw.isBoolean( AutoFeatures.isCalculateAltFortuneOnAllBlocksEnabled )) );
    		
    		
    		results.add( " " );
    		
    		
    		results.add( String.format("+.   Calculate XP:&b %s", 
    				afw.isBoolean( AutoFeatures.isCalculateXPEnabled )) );
    		results.add( String.format("+.   Drop XP as Orbs:&b %s", 
    				afw.isBoolean( AutoFeatures.givePlayerXPAsOrbDrops )) );
    		
    		results.add( String.format("+.   Process TokensEnchant Explosive Events:&b %s", 
    				afw.isBoolean( AutoFeatures.isProcessTokensEnchantExplosiveEvents )) );
    		results.add( String.format("+.   Process Crazy Enchants Block Explode Events:&b %s", 
    				afw.isBoolean( AutoFeatures.isProcessCrazyEnchantsBlockExplodeEvents )) );
    		results.add( String.format("+.   Process McMMO BlockBreakEvents:&b %s", 
    				afw.isBoolean( AutoFeatures.isProcessMcMMOBlockBreakEvents )) );
    		
    		
    		
    	}
    	
    	
    	results.add( String.format("Prestiges Enabled:&b %s", 
    										getConfigBooleanFalse( "prestige.enabled" )) );
    	results.add( String.format(".   Reset Money:&b %s", 
    										getConfigBooleanFalse( "prestige.resetMoney" )) );
    	results.add( String.format(".   Reset Default Ladder:&b %s", 
    										getConfigBooleanFalse( "prestige.resetDefaultLadder" )) );


    	results.add( "" );
    	
    	
    	boolean delayedPrisonStartup = getConfigBooleanFalse( "delayedCMIStartup" );
    	if ( delayedPrisonStartup ) {
    		
    		results.add( String.format("Prison Delayed Start:&b %s", delayedPrisonStartup) );
    	}
    	
    	
    	results.add( String.format("GUI Enabled:&b %s", 
    			getConfigBooleanFalse( "prison-gui-enabled" )) );
    	
    	
    	results.add( String.format("Sellall Enabled:&b %s", 
    										getConfigBooleanFalse( "sellall" )) );
    		  
    	
    	results.add( String.format("Backpacks Enabled:&b %s", 
    										getConfigBooleanFalse( "backpacks" )) );
    	

		
		return results;
	}
	
	
	
	@Override
	public void dumpEventListenersBlockBreakEvents() {
		
		dumpEventListeners( "BlockBreakEvent", BlockBreakEvent.getHandlerList() );
		
		Output.get().logInfo( "&2NOTE: Prison Block Event Listeners:" );
		
		Output.get().logInfo( "&2. . Prison Internal BlockBreakEvents: " +
								"tech.mcprison.prison.spigot.SpigotListener" );
		Output.get().logInfo( "&2. . Auto Feature Core: Non-AutoManager: " +
								"OnBlockBreakEventListeners$OnBlockBreakEventListener*" );
		Output.get().logInfo( "&2. . Prison MONITOR Events manages block counts, " +
								"Mine Sweeper, and zero block conditions." );
		Output.get().logInfo( "&2. . AutoManager and enchantment event listeners are " +
								"identified by their class names." );
	}
	
	@Override
	public void dumpEventListenersPlayerChatEvents() {
		
		dumpEventListeners( "AsyncPlayerChatEvent", AsyncPlayerChatEvent.getHandlerList() );
			
		if ( new BluesSpigetSemVerComparator().compareMCVersionTo("1.17.0") < 0 ) {
			
			dumpEventListeners( "PlayerChatEvent", PlayerChatEvent.getHandlerList() );
		}
	}
	
	/**
	 * <p>Some information on events...
	 * </p>
	 * 
	 * https://bukkit.fandom.com/wiki/Event_API_Reference
	 * 
	 * <p>When changing values of an event the changes of one with the higher priority will 
	 * override any changes done before by a listener with a lower priority so that in the 
	 * end the one with the highest priority can have the final say in the actually outcome. 
	 * <b>To achieve this priority order listeners are called from the ones with the 
	 * lowest to the ones with the highest priority. Any listener with the MONITOR 
	 * priority is called last.</b> 
	 * 
	 * </p>
	 * 
	 * @param eventType
	 * @param handlerList
	 */
	private void dumpEventListeners( String eventType, HandlerList handlerList ) {
		
		RegisteredListener[] listeners = handlerList.getRegisteredListeners();
		
		ChatDisplay display = new ChatDisplay("Event Dump: " + eventType );
		display.addText("&8All registered EventListeners (%d):", listeners.length );
		
		for ( RegisteredListener eventListner : listeners ) {
			String plugin = eventListner.getPlugin().getName();
			EventPriority priority = eventListner.getPriority();
			String listener = eventListner.getListener().getClass().getName();
			
			String message = String.format( "&3  Plugin: &7%s   %s  &3(%s)", 
					plugin, priority.name(), listener);
			
			display.addText( message );
		}
		
		display.toLog( LogLevel.DEBUG );
	}

	/**
	 * <p>This feature will be similar to the WorldGuard's command 
	 * 	<pre>/wg debug testbreak -ts &lt;player&gt;</pre>, but this will show far 
	 * more details on each and every step.
	 * </p>
	 * 
	 */
	@Override
	public void traceEventListenersBlockBreakEvents( tech.mcprison.prison.internal.CommandSender sender )
	{
		// TODO Auto-generated method stub
		
	}
	
}
