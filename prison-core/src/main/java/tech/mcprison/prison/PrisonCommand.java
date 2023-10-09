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

package tech.mcprison.prison;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.UUID;

import tech.mcprison.prison.autofeatures.AutoFeaturesFileConfig.AutoFeatures;
import tech.mcprison.prison.autofeatures.AutoFeaturesWrapper;
import tech.mcprison.prison.backpacks.BackpackConverterOldPrisonBackpacks;
import tech.mcprison.prison.backups.PrisonBackups;
import tech.mcprison.prison.backups.PrisonBackups.BackupTypes;
import tech.mcprison.prison.cache.PlayerCachePlayerData;
import tech.mcprison.prison.commands.Arg;
import tech.mcprison.prison.commands.Command;
import tech.mcprison.prison.commands.CommandPagedData;
import tech.mcprison.prison.commands.RegisteredCommand;
import tech.mcprison.prison.commands.Wildcard;
import tech.mcprison.prison.discord.PrisonPasteChat;
import tech.mcprison.prison.discord.PrisonSupportFiles;
import tech.mcprison.prison.internal.CommandSender;
import tech.mcprison.prison.internal.Player;
import tech.mcprison.prison.localization.LocaleManager;
import tech.mcprison.prison.modules.Module;
import tech.mcprison.prison.output.BulletedListComponent;
import tech.mcprison.prison.output.ChatDisplay;
import tech.mcprison.prison.output.DisplayComponent;
import tech.mcprison.prison.output.Output;
import tech.mcprison.prison.output.Output.DebugTarget;
import tech.mcprison.prison.placeholders.PlaceholdersStats;
import tech.mcprison.prison.troubleshoot.TroubleshootResult;
import tech.mcprison.prison.troubleshoot.Troubleshooter;
import tech.mcprison.prison.util.PrisonJarReporter;

/**
 * Root commands for managing the platform as a whole, in-game.
 *
 * @author Faizaan A. Datoo
 * @since API 1.0
 */
public class PrisonCommand 
		extends PrisonCommandMessages {

	private List<String> registeredPlugins = new ArrayList<>();
	
	private TreeMap<String, RegisteredPluginsData> registeredPluginData = new TreeMap<>();
	
	private List<String> prisonStartupDetails;
	
	private String supportName = null;
	private PrisonSupportFiles supportFile = null;
	private TreeMap<String, String> supportURLs;
	
	
	public PrisonCommand() {
		super();
		
		this.prisonStartupDetails = new ArrayList<>();
		
		this.supportURLs = new TreeMap<>();
		
	}
	
    @Command(identifier = "prison version", description = "Displays version information.", 
    		onlyPlayers = false, aliases = "prison info" )
    public void versionCommand(CommandSender sender,
    		@Wildcard(join=true)
    		@Arg(name = "options", description = "Options [basic, all]", def = "basic" ) String options) {
    	
    	if ( options != null && !"basic".equalsIgnoreCase( options ) && !"all".equalsIgnoreCase( options ) ) {
    		Output.get().logInfo( "&7Invalid option. [basic, all]" );
    		return;
 
    	}
    	else if ( options == null ) {
    		options = "basic";
    	}
    	
    	ChatDisplay display = Prison.get().getPrisonStatsUtil().displayVersion(options);
    	
        display.send(sender);
        
//        if ( options != null && "all".equalsIgnoreCase( options )) {
//        	// Display all Ranks in each ladder:
//        	boolean includeAll = true;
//        	PrisonRanks.getInstance().getRankManager().ranksByLadders( includeAll );
//        }
    }
    
    /**
     * <p>This class contains the data that is used to log the plugins, commands, and their aliases,
     * that may be setup on the server.  This is setting the ground work to store the command
     * data that can be used to trouble shoot complex problems, such as conflicts, that are
     * occuring with the prison plugin.
     * </p>
     * 
     * <p>This is just the data, and does not interact, or modify, any of the other commands.
     * </p>
     *
     */
    public class RegisteredPluginsData {
    	private String pluginName;
    	private String pluginVersion;
    	private List<RegisteredPluginCommandData> registeredCommands;
    	
    	private boolean registered = false;
    	private int aliasCount = 0;
    	
    	public RegisteredPluginsData( String pluginName, String pluginVersion, boolean registered ) {
    		super();
    		
    		this.pluginName = pluginName;
    		this.pluginVersion = pluginVersion;
    		this.registered = registered;
    		
    		this.registeredCommands = new ArrayList<>();
    	}
    	
    	public void addCommand( String commandName, List<String> commandAliases ) {
    		RegisteredPluginCommandData command =
    				new RegisteredPluginCommandData( commandName, commandAliases );
    		
    		getRegisteredCommands().add( command );
    		
    		setAliasCount( getAliasCount() + commandAliases.size() );
    	}


		public Object formatted()
		{
			String message = String.format( "&7%s &c%s&3(&a%s &7c:&a%s &7a:&a%s &3)", 
						getPluginName(), 
						(isRegistered() ? "" : "*"),
						getPluginVersion(), 
						Integer.toString(getRegisteredCommands().size()), 
						Integer.toString(getAliasCount()));
			return message;
		}
		

		public String getdetails() {
			StringBuilder sbCmd = new StringBuilder();
			StringBuilder sbAlias = new StringBuilder();
			for ( RegisteredPluginCommandData cmd : getRegisteredCommands() )
			{
				if ( sbCmd.length() > 0 ) {
					sbCmd.append( " " );
				}
				sbCmd.append( cmd.getCommand() );
				
				for ( String alias : cmd.getAliases() )
				{
					if ( sbAlias.length() > 0 ) {
						sbAlias.append( " " );
					}
					sbAlias.append( alias );
				}
				
			}
			
			return "Plugin: " + getPluginName() + " cmd: " + sbCmd.toString() + 
					(sbAlias.length() == 0 ? "" :
						" alias: " + sbAlias.toString());
		}
		
		public String getPluginName() {
			return pluginName;
		}
		public void setPluginName( String pluginName ) {
			this.pluginName = pluginName;
		}

		public String getPluginVersion() {
			return pluginVersion;
		}
		public void setPluginVersion( String pluginVersion ) {
			this.pluginVersion = pluginVersion;
		}

		public boolean isRegistered() {
			return registered;
		}
		public void setRegistered( boolean registered ) {
			this.registered = registered;
		}

		public List<RegisteredPluginCommandData> getRegisteredCommands() {
			return registeredCommands;
		}
		public void setRegisteredCommands( List<RegisteredPluginCommandData> registeredCommands ) {
			this.registeredCommands = registeredCommands;
		}

		public int getAliasCount() {
			return aliasCount;
		}
		public void setAliasCount( int aliasCount ) {
			this.aliasCount = aliasCount;
		}

    	
    }
    
    public class RegisteredPluginCommandData {
    	private String command;
    	private List<String> aliases;
    	
    	public RegisteredPluginCommandData( String command, List<String> aliases ) {
    		super();
    		
    		this.command = command;
    		this.aliases = aliases;
    	}

		public String getCommand() {
			return command;
		}
		public void setCommand( String command ) {
			this.command = command;
		}

		public List<String> getAliases() {
			return aliases;
		}
		public void setAliases( List<String> aliases ) {
			this.aliases = aliases;
		}
    }
    
    
//    	
//    public ChatDisplay displayVersion(String options) {
//    
//    	boolean isBasic = options == null || "basic".equalsIgnoreCase( options );
//    	
//        ChatDisplay display = new ChatDisplay("/prison version");
//        display.addText("&7Prison Version: %s", Prison.get().getPlatform().getPluginVersion());
//
//        display.addText("&7Running on Platform: %s", Prison.get().getPlatform().getClass().getName());
//        display.addText("&7Minecraft Version: %s", Prison.get().getMinecraftVersion());
//
//        
//        // System stats:
//        display.addText("");
//        
//        Prison.get().displaySystemSettings( display );
//        
//        Prison.get().displaySystemTPS( display );
//
//        
//        display.addText("");
//
//
//        // This generates the module listing, the autoFeatures overview, 
//        // the integrations listings, and the plugins listings.
//        boolean showLaddersAndRanks = true;
//        Prison.get().getPlatform().prisonVersionFeatures( display, isBasic, showLaddersAndRanks );
//
//        
//        
////        List<String> features = Prison.get().getPlatform().getActiveFeatures();
////        if ( features.size() > 0 ) {
////        	
////        	display.addText("");
////        	for ( String feature : features ) {
////        		
////        		if ( !feature.startsWith( "+" ) ) {
////        			
////        			display.addText( feature );
////        		}
////        		else if ( !isBasic ) {
////        			
////        			display.addText( feature.substring( 1 ) );
////        		}
////        	}
////        }
////        
////        
////        display.addText("");
////        
////        // Active Modules:
////        display.addText("&7Prison's root Command: &3/prison");
////        
////        for ( Module module : Prison.get().getModuleManager().getModules() ) {
////        	
////        	display.addText( "&7Module: %s : %s %s", module.getName(), 
////        			module.getStatus().getStatusText(),
////        			(module.getStatus().getStatus() == ModuleStatus.Status.FAILED ? 
////        					"[" + module.getStatus().getMessage() + "]" : "")
////        			);
////        	// display.addText( ".   &7Base Commands: %s", module.getBaseCommands() );
////        }
////        
////        List<String> disabledModules = Prison.get().getModuleManager().getDisabledModules();
////        if ( disabledModules.size() > 0 ) {
////        	display.addText( "&7Disabled Module%s:", (disabledModules.size() > 1 ? "s" : ""));
////        	for ( String disabledModule : Prison.get().getModuleManager().getDisabledModules() ) {
////        		display.addText( ".   &cDisabled Module:&7 %s. Related commands and placeholders are non-functional. ",
////        				disabledModule );
////        	}
////        }
////        
////        display.addText("");
////        display.addText("&7Integrations:");
////
////        IntegrationManager im = Prison.get().getIntegrationManager();
////        String permissions =
////        		(im.hasForType(IntegrationType.PERMISSION) ?
////                " " + im.getForType(IntegrationType.PERMISSION).get().getDisplayName() :
////                "None");
////
////        display.addText(". . &7Permissions: " + permissions);
////
////        String economy =
////        		(im.hasForType(IntegrationType.ECONOMY) ?
////                " " + im.getForType(IntegrationType.ECONOMY).get().getDisplayName() : 
////                "None");
////
////        display.addText(". . &7Economy: " + economy);
////        
////        
////        List<DisplayComponent> integrationRows = im.getIntegrationComponents( isBasic );
////        for ( DisplayComponent component : integrationRows )
////		{
////        	display.addComponent( component );
////		}
////        
////        
////        display.addText("");
////        display.addText("&7Locale Settings:");
////        
////        for ( String localeInfo : Prison.get().getLocaleLoadInfo() ) {
////			display.addText( ". . " + localeInfo );
////		}
//        
////        
////        Prison.get().getPlatform().identifyRegisteredPlugins();
////        
////        // NOTE: This list of plugins is good enough and the detailed does not have all the info.
////        // Display all loaded plugins:
////        if ( getRegisteredPlugins().size() > 0 ) {
////        	display.addText("");
////        	display.addText( "&7Registered Plugins: " );
////        	
////        	List<String> plugins = getRegisteredPlugins();
////        	Collections.sort( plugins );
////        	List<String> plugins2Cols = Text.formatColumnsFromList( plugins, 2 );
////        	
////        	for ( String rp : plugins2Cols ) {
////				
////        		display.addText( rp );
////			}
////        	
//////        	StringBuilder sb = new StringBuilder();
//////        	for ( String plugin : getRegisteredPlugins() ) {
//////        		if ( sb.length() == 0) {
//////        			sb.append( ". " );
//////        			sb.append( plugin );
//////        		} else {
//////        			sb.append( ",  " );
//////        			sb.append( plugin );
//////        			display.addText( sb.toString() );
//////        			sb.setLength( 0 );
//////        		}
//////        	}
//////        	if ( sb.length() > 0 ) {
//////        		display.addText( sb.toString());
//////        	}
////        }
////        
////        // This version of plugins does not have all the registered commands:
//////        // The new plugin listings:
//////        if ( getRegisteredPluginData().size() > 0 ) {
//////        	display.text( "&7Registered Plugins Detailed: " );
//////        	StringBuilder sb = new StringBuilder();
//////        	Set<String> keys = getRegisteredPluginData().keySet();
//////        	
//////        	for ( String key : keys ) {
//////        		RegisteredPluginsData plugin = getRegisteredPluginData().get(key);
//////        		
//////        		if ( sb.length() == 0) {
//////        			sb.append( "  " );
//////        			sb.append( plugin.formatted() );
//////        		} else {
//////        			sb.append( ",  " );
//////        			sb.append( plugin.formatted() );
//////        			display.text( sb.toString() );
//////        			sb.setLength( 0 );
//////        		}
//////        	}
//////        	if ( sb.length() > 0 ) {
//////        		display.text( sb.toString());
//////        	}
//////        }
////        
////        
//////        RegisteredPluginsData plugin = getRegisteredPluginData().get( "Prison" );
//////        String pluginDetails = plugin.getdetails();
//////        
//////        display.text( pluginDetails );
////        
////
//////        if ( !isBasic ) {
//////        	Prison.get().getPlatform().dumpEventListenersBlockBreakEvents();
//////        }
////        
////        
////        Prison.get().getPlatform().getWorldLoadErrors( display );
////
////        if ( !isBasic && getPrisonStartupDetails().size() > 0 ) {
////        	display.addText("");
////        	
////        	for ( String msg : getPrisonStartupDetails() ) {
////				display.addText( msg );
////			}
////        }
//        
//        return display;
//    }


	/**
     * A test to see if these dummy command placeholders could possibly lock out
     * players who don't have permission to view the subcommands?
     * 
     */

    @Command(identifier = "prison", 
    		onlyPlayers = false, permissions = "prison.commands")
    public void prisonSubcommands(CommandSender sender) {
    	sender.dispatchCommand( "prison help" );
    }
 
    @Command(identifier = "prison placeholders", 
    		onlyPlayers = false, permissions = "prison.commands")
    public void prisonPlaceholdersSubcommands(CommandSender sender) {
    	sender.dispatchCommand( "prison placeholders help" );
    }
    
    @Command(identifier = "prison reload", 
    		onlyPlayers = false, permissions = "prison.commands")
    public void prisonReloadSubcommands(CommandSender sender) {
    	sender.dispatchCommand( "prison reload help" );
    }
    
    @Command(identifier = "prison utils", 
    		onlyPlayers = false, permissions = "prison.commands")
    public void prisonUtilsSubcommands(CommandSender sender) {
    	sender.dispatchCommand( "prison utils help" );
    }
    
    
    @Command(identifier = "prison modules", onlyPlayers = false, permissions = "prison.modules", 
    				description = "Lists the modules that hook into Prison to give it functionality.")
    public void modulesCommand(CommandSender sender) {
        ChatDisplay display = new ChatDisplay("/prison modules");
        display.addEmptyLine();

        BulletedListComponent.BulletedListBuilder builder =
            new BulletedListComponent.BulletedListBuilder();
        for (Module module : Prison.get().getModuleManager().getModules()) {
            builder.add("&3%s &8(%s) &3v%s &8- %s", module.getName(), module.getPackageName(),
                module.getVersion(), module.getStatus().getMessage());
        }

        display.addComponent(builder.build());

        display.send(sender);
    }

//    @Command(identifier = "prison troubleshoot", description = "Runs a troubleshooter.", 
//    					onlyPlayers = false, permissions = "prison.troubleshoot")
    public void troubleshootCommand(CommandSender sender,
        @Arg(name = "name", def = "list", description = "The name of the troubleshooter.") String name) {
        // They just want to list stuff
        if (name.equals("list")) {
            sender.dispatchCommand("prison troubleshoot list");
            return;
        }

        TroubleshootResult result =
            PrisonAPI.getTroubleshootManager().invokeTroubleshooter(name, sender);
        if (result == null) {
            Output.get().sendError(sender, "The troubleshooter %s doesn't exist.", name);
            return;
        }

        ChatDisplay display = new ChatDisplay("Result Summary");
        display.addText("&7Troubleshooter name: &b%s", name.toLowerCase()) //
            .addText("&7Result type: &b%s", result.getResult().name()) //
            .addText("&7Result details: &b%s", result.getDescription()) //
            .send(sender);

    }

//    @Command(identifier = "prison troubleshoot list", description = "Lists the troubleshooters.", 
//    						onlyPlayers = false, permissions = "prison.troubleshoot")
    public void troubleshootListCommand(CommandSender sender) {
        ChatDisplay display = new ChatDisplay("Troubleshooters");
        display.addText("&8Type /prison troubleshoot <name> to run a troubleshooter.");

        BulletedListComponent.BulletedListBuilder builder =
            new BulletedListComponent.BulletedListBuilder();
        for (Troubleshooter troubleshooter : PrisonAPI.getTroubleshootManager()
            .getTroubleshooters()) {
            builder.add("&b%s &8- &7%s", troubleshooter.getName(), troubleshooter.getDescription());
        }
        display.addComponent(builder.build());

        display.send(sender);
    }

    
    @Command(identifier = "prison placeholders test", 
    		description = "Converts any Prison placeholders in the test string to their values. "
    				+ "Use '-s' keyword to reduce output text. "
    				+ "All placeholder attributes are supported.", 
    		onlyPlayers = false, permissions = "prison.placeholder")
    public void placeholdersTestCommand(CommandSender sender,
    		@Arg(name = "playerName", description = "Player name to use with player rank placeholders (optional)", 
    							def = "." ) String playerName,
    		@Wildcard(join=true)
    		@Arg(name = "text", 
    			description = "Placeholder text to test using { } as escape characters" ) String text ) {
    	
    	
    	if ( playerName != null && playerName.contains( "%" ) || 
    			text != null && text.contains( "%" ) ) {
    		Output.get().logInfo( "&3You cannot use &7 %%%% &3 as escape characters. Use &7{&3 &7}&3 instead." );
    		return;
    	}
    	
    	
    	// blank defaults do not work when there are more than one at a time.  So had to
    	// default to periods.  So convert periods to blanks initially:
    	playerName = (playerName.equals( "." ) ? "" : playerName );
    	
    	// Try to get player from the supplied playerName first:
    	Player player = getPlayer( null, playerName );
    	if ( player == null ) {
    		// No player found, or none specified. Need to shift parameters over by one:
    		if ( text != null && text.trim().length() > 0 ) {
    			
    			// playerName should be moved to the pageNumber, after pageNumber is moved to patterns:
    			text = (playerName.trim() + " " + text).trim();
    		} 
    		else {
    			text = playerName;
    		}
    		
    		// Try to get player from the sender:
    		player = getPlayer( sender );
    	}
    	
    	boolean isShort = text.startsWith( "-s " );
    	if ( isShort ) {
    		text = text.substring( 3 );
    	}

    	ChatDisplay display = new ChatDisplay("Placeholder Test");
    	
        BulletedListComponent.BulletedListBuilder builder =
                new BulletedListComponent.BulletedListBuilder();
        
        
    	UUID playerUuid = player == null ? null : player.getUUID();
    	playerName = player != null ? player.getName() :
    			(playerName.isEmpty() ? sender.getName() : playerName);
    	
    	String translated = Prison.get().getPlatform().getPlaceholders()
    					.placeholderTranslateText( playerUuid, playerName, text );
    	
    	if ( !isShort ) {
    		builder.add( String.format( "&a    Include one or more Prison placeholders with other text..."));
    		builder.add( String.format( "&a    Use { } to escape the placeholders."));
    		
    		// Show player info here like with the search:
    		if ( player != null ) {
    			builder.add( String.format( "&a    Player: &7%s  &aPlayerUuid: &7%s", player.getName(), 
    					(playerUuid == null ? "null" : playerUuid.toString())));
    		}
    		
    		builder.add( String.format( "&7  Original:   \\Q%s\\E", text));
    	}
    	
    	builder.add( String.format( "&7  Translated: %s", translated));
    	
    	display.addComponent(builder.build());
    	display.send(sender);
    }
    
	private Player getPlayer( CommandSender sender ) {
		Optional<Player> player = Prison.get().getPlatform().getPlayer( sender.getName() );
		return player.isPresent() ? player.get() : null;
	}
   

    /**
     * <p>Gets a player by name.  If the player is not online, then try to get them from 
     * the offline player list. If not one is found, then return a null.
     * </p>
     * 
     * @param sender
     * @param playerName is optional, if not supplied, then sender will be used
     * @return Player if found, or null.
     */
	private Player getPlayer( CommandSender sender, String playerName ) {
		Player result = null;
		
		playerName = playerName != null ? playerName : sender != null ? sender.getName() : null;
		
		if ( playerName != null ) {
			Optional<Player> opt = Prison.get().getPlatform().getPlayer( playerName );
			if ( !opt.isPresent() ) {
				opt = Prison.get().getPlatform().getOfflinePlayer( playerName );
			}
			if ( opt.isPresent() ) {
				result = opt.get();
			}
		}
		return result;
	}
	
    @Command(identifier = "prison placeholders search", 
    				description = "Search all placeholders that match all patterns", 
    		onlyPlayers = false, permissions = "prison.placeholder")
    public void placeholdersSearchCommand(CommandSender sender,
    		@Arg(name = "playerName", description = "Player name to use with player rank placeholders (optional)", 
    				def = "." ) String playerName,
    		@Arg(name = "pageNumber", description = "page number of results to display (optional)", def = "." ) String pageNumber,
    		@Wildcard(join=true)
    		@Arg(name = "patterns", description = "Patterns of placeholders to search for" ) String patterns ) {
    
    	
    	
    	// blank defaults do not work when there are more than one at a time.  So had to
    	// default to periods.  So convert periods to blanks initially:
    	playerName = (playerName.equals( "." ) ? "" : playerName );
    	pageNumber = (pageNumber.equals( "." ) ? "" : pageNumber );
    	patterns = (patterns.equals( "." ) ? "" : patterns );
    	
    	Player player = getPlayer( null, playerName );
    	if ( player == null ) {
    		// No player found, or none specified. Need to shift parameters over by one:
    		if ( pageNumber != null && pageNumber.trim().length() > 0 ) {
    			
    			// playerName should be moved to the pageNumber, after pageNumber is moved to patterns:
    			patterns = (pageNumber.trim() + " " + patterns).trim();
    		} 
    		pageNumber = playerName;
    	}
    	
    	
    	int page = 1;
    	
    	/**
    	 * Please note: Page is optional and defaults to a value of 1.  But when it is not
    	 * provided, it "grabs" the first pattern.  So basically, if pageNumber proves not
    	 * to be a number, then we must prefix whatever is in patterns with that value.
    	 */
    	if ( pageNumber != null ) {
    		
    		try {
				page = Integer.parseInt( pageNumber );
			}
    		catch ( NumberFormatException e ) {
    			// If exception, add pageNumber to the beginning patterns.
    			// So no page number was specified, it was part of the patterns
    			patterns = (pageNumber.trim() + " " + patterns).trim();
			}
    		
    	}
    	

    	// Cannot allow pages less than 1:
    	if ( page < 1 ) {
    		page = 1;
    	}
    	
    	ChatDisplay display = new ChatDisplay("Placeholders Search");
    
    	
    	if ( patterns == null || patterns.trim().length() == 0 ) {
    		sender.sendMessage( "&7Pattern required. Placeholder results must match all pattern terms." );
    		return;
    	}
    	
        BulletedListComponent.BulletedListBuilder builder =
                						new BulletedListComponent.BulletedListBuilder();
        
        if ( player == null ) {
        	// playerName was not provided, or was invalid. So use sender.
        	player = getPlayer( sender );
        }
        UUID playerUuid = (player == null ? null : player.getUUID());
        
        List<String> placeholders = Prison.get().getPlatform().getPlaceholders()
        					.placeholderSearch( playerUuid, (player == null ? null : player.getName()), 
        								patterns.trim().split( " " ) );
        
        builder.add( String.format( "&a    Include one or more patterns to search for placeholders. If more"));
        builder.add( String.format( "&a    than one is provided, the returned placeholder will hit on all."));
        builder.add( String.format( "&a    Player based placeholders will return nulls for values if ran from console,"));
        builder.add( String.format( "&a    unless player name is specified. Can view placeholders for any player."));
        
        if ( player != null ) {
        	builder.add( String.format( "&a    Player: &7%s  &aPlayerUuid: &7%s", player.getName(), 
        			(playerUuid == null ? "null" : playerUuid.toString())));
        	
        }
        
        
        DecimalFormat dFmt = Prison.get().getDecimalFormatInt();
        builder.add( String.format( "&7  Results: &c%s  &7Original patterns:  &3%s", 
        		dFmt.format(placeholders.size()), patterns ));
    	
        
        CommandPagedData cmdPageData = new CommandPagedData(
        		"/prison placeholders search", placeholders.size(),
        		0, Integer.toString( page ), 20 );
        // Need to provide more "parts" to the command that follows the page number:
        cmdPageData.setPageCommandSuffix( patterns );
    	
        int count = 0;
    	for ( String placeholder : placeholders ) {
    		if ( cmdPageData == null ||
            		count++ >= cmdPageData.getPageStart() && 
            		count <= cmdPageData.getPageEnd() ) {
    			
    			builder.add( String.format( placeholder ));
    		}
		}
    	
    	display.addComponent(builder.build());
    	
    	cmdPageData.generatePagedCommandFooter( display );
    	
    	display.send(sender);
    }
    
    
    @Command(identifier = "prison placeholders list", 
    		description = "List all placeholders templates", 
    		onlyPlayers = false, permissions = "prison.placeholder")
    public void placeholdersListCommand(CommandSender sender
    		) {
    	
    	ChatDisplay display = new ChatDisplay("Placeholders List");
    	
    	display.addText( "&a    Placeholders are case insensitive, but are registered in all lowercase.");
    	display.addText( "&a    Placeholder escape characters may be { } or % %. If one does not work, try the other.");
    	display.addText( "&a    Placeholders that include 'rankname', 'laddername', or 'minename' should be");
    	display.addText( "&a    replaced with the appropriate rank names, ladder names, or mine names.");
    	
    	for ( String disabledModule : Prison.get().getModuleManager().getDisabledModules() ) {
    		display.addText( "&a    &cDisabled Module: &7%s&a. Related placeholders maybe listed but are non-functional. ",
    				disabledModule );
    	}
    	
    	List<DisplayComponent> placeholders = new ArrayList<>();
        Prison.get().getIntegrationManager().getPlaceholderTemplateList( placeholders );


    	for ( DisplayComponent placeholder : placeholders ) {
    		display.addComponent( placeholder );
    	}
    	
    	display.send(sender);
    }
    
    
    @Command(identifier = "prison placeholders stats", 
    		description = "List all placeholders that have been requested since server startup.", 
    		onlyPlayers = false, permissions = "prison.placeholder")
    public void placeholdersStatsCommand(CommandSender sender,
    		@Arg(name = "options", description = "Options for the cache.  "
    				+ "[resetCache] this will clear the placeholder "
    				+ "cache of all placeholders and all placeholders will have to be reidentified. "
    				+ "[removeErrors] all cached placeholders that have been marked as invalid will "
    				+ "be removed from the cache and will have to be reevaluated if used again.", 
			def = "." ) String options
    		) {
    	
    	ChatDisplay display = new ChatDisplay("Placeholders List");
    	
    	if ( options != null && !".".equals(options) ) {
    		boolean resetCache = "resetCache".equalsIgnoreCase( options );
    		boolean removeErrors = "removeErrors".equalsIgnoreCase( options );

    		PlaceholdersStats.getInstance().clearCache( resetCache, removeErrors );
    	}
    	
    	ArrayList<String> stats = PlaceholdersStats.getInstance().generatePlaceholderReport();
    	
    	for (String stat : stats) {
			display.addText( stat.replace( "%", "%%") );
		}
    	
    	
    	display.send(sender);
    }
    
    @Command(identifier = "prison reload placeholders", 
    		description = "Placeholder reload: Regenerates all placeholders and reregisters them.", 
    		onlyPlayers = false, permissions = "prison.placeholder")
    public void placeholdersReloadCommandAlias(CommandSender sender ) {
    	placeholdersReloadCommand( sender );
    }
    
    @Command(identifier = "prison placeholders reload", 
    		description = "Placeholder reload: Regenerates all placeholders and reregisters them.", 
    		onlyPlayers = false, permissions = "prison.placeholder")
    public void placeholdersReloadCommand(CommandSender sender ) {
    	
    	Prison.get().getPlatform().getPlaceholders().reloadPlaceholders();
    	
    	String message = "Placeholder reload was attempted. " +
    			"No guarentees that it worked 100%. Restart server if any doubts.";

    	sender.sendMessage( message );
    }

    
    @Command(identifier = "prison reload locales", 
    		description = "Locales reload: This will reload all of the language files that are being used " +
    				"within prison. Based upon the configuration settings, this will load the proper locales.", 
    		onlyPlayers = false, permissions = "prison.reload")
    public void localesReloadCommand(CommandSender sender ) {
    	
    	for ( LocaleManager LocalManager : LocaleManager.getRegisteredInstances() ) {
    		LocalManager.reload();
    	}
    	
    	String message = "Locales reload was attempted. " +
    			"No guarentees that it worked 100%. Restart server if any doubts.";
    	
    	sender.sendMessage( message );
    }
    
    
    @Command(identifier = "prison reload autoFeatures", 
    		description = "AutoFeatures reload: Reloads the auto features settings. The current " +
    				"settings will be discarded before reloading the configuration file.", 
    		onlyPlayers = false, permissions = "prison.autofeatures")
    public void reloadAutoFeatures(CommandSender sender ) {
    	    	
    	AutoFeaturesWrapper.getInstance().reloadConfigs();
    	
    	String message = "&7AutoFeatures were reloaded. The new settings are now in effect. ";
    	sender.sendMessage( message );

    	try {
    		String filePath = AutoFeaturesWrapper.getInstance().getAutoFeaturesConfig()
    								.getConfigFile().getCanonicalPath();
    		sender.sendMessage( filePath );
    	}
    	catch ( IOException e ) {
    		// Ignore
    	}
//    	try {
//    		
//    		if ( AutoFeaturesWrapper.getInstance().getBlockConvertersConfig() != null ) {
//    			
//    			File bcFile = AutoFeaturesWrapper.getInstance().getBlockConvertersConfig().getConfigFile();
//    			if ( bcFile != null && bcFile.exists() ) {
//    				
//    				String filePath = bcFile.getCanonicalPath();
//    				sender.sendMessage( filePath );
//    			}
//    		}
//    		
//    	}
//    	catch ( IOException e ) {
//    		// Ignore
//    	}
    }
    
    
    @Command(identifier = "prison reload blockConverters", 
    		description = "BlockConverters reload: Reloads the block converter settings. The current " +
    				"settings will be discarded before reloading the configuration file.", 
    				onlyPlayers = false, permissions = "prison.autofeatures")
    public void reloadBlockConverters(CommandSender sender ) {
    	
    	if ( AutoFeaturesWrapper.getBlockConvertersInstance() != null ) {
    		
    		AutoFeaturesWrapper.getBlockConvertersInstance().reloadConfig();
    		
    		String message = "&7BlockConverters were reloaded. The new settings are now in effect. ";
    		sender.sendMessage( message );
    		
    		try {
    			File bcFile = AutoFeaturesWrapper.getBlockConvertersInstance().getConfigFile();
    			if ( bcFile != null && bcFile.exists() ) {
    				
    				String filePath = bcFile.getCanonicalPath();
    				sender.sendMessage( filePath );
    			}
    		}
    		catch ( IOException e ) {
    			// Ignore
    		}
    		
    	}
    	
    }

    
    /**
     * <p>This command does not do anything, except to provide a command placeholder to
     * make owners aware that there is auto features enabled within prison. 
     * Running this command will show the permissions needed to use these auto features.
     * </p>
     * 
     * <p>Cannot use the @Command altPermissions parameter since the permissions can be
     * dynamically altered to fit the needs of the owner's server.  Using the command 
     * altPermissions will also require a server restart to reflect any online changes, 
     * not to mention a recompile since the end users cannot make these changes.
     * </p>
     * 
     * @param sender
     */
    @Command(identifier = "prison autofeatures", 
    		description = "Autofeatures for prison: pickup, smelt, and block", 
    		onlyPlayers = false, permissions = "prison.autofeatures" )
//    		, altPermissions = { "prison.autofeatures.pickup", "prison.autofeatures.smelt" , 
//    				"prison.autofeatures.block" })
    public void autoFeaturesInformation(CommandSender sender) {
    	
    	ChatDisplay display = new ChatDisplay("Auto Features Information");
    	
    	display.addText( "&a Prison auto features provide the following options:");
    	display.addText( "&7   Auto pickup - &aUpon block break, items are placed directly in to player inventory.");
    	display.addText( "&a     - Features for enabling XP, Durability, and Fortune are within the config file.");
    	display.addText( "&7   Auto smelt - &aItems that can be smelted will be smelted automatically.");
    	display.addText( "&7   Auto block - &aConverts ores to blocks.");
    	display.addText( "&7   Tool lore starts with: Pickup, Smelt, or Block. Only one per line." );
    	display.addText( "&7   Tool lore names can be customize in config file, but color codes could be an issue." );
    	display.addText( "&7   Tool lore 100 percent with just name. Can have value 0.001 to 100.0 percent." );
    	display.addText( "&7   Tool lore examples: Pickup, Pickup 7.13, Smelt 55, Block 75.123" );
    	
    	display.addText( "&a To configure modify plugin/Prison/autoFeaturesConfig.yml");
    	display.addText( "&a Or better yet, you can use the &7/prison gui");
    	
    	display.addText( "&a");
    	display.addText( "&aPrison supports TokenEnchant's explosion based enchants.  Please see our online " +
    			"documentation related to WorldGuard and LuckPerms with protecting mines (its near the bottom). " +
    			"TE's configurations may not be obvious without reading the document.");
    	display.addText( "&a");
    	display.addText( "&aPrison also supports Crazy Enchant's explosion based enchantments too.  ");
    	
    	
    	List<AutoFeatures> afs = AutoFeatures.permissions.getChildren();
    	StringBuilder sb = new StringBuilder();
    	for ( AutoFeatures af : afs ) {
			if ( sb.length() > 0 ) {
				sb.append( "  " );
			}
			sb.append( af.getMessage() );
		}
    	display.addText( "&3Permissions:" );
    	display.addText( "&b   %s", sb.toString() );
    	display.addText( "&7 NOTE: Permissions enables that feature even if disabled for mines." );
    	display.addText( " " );

    	
    	
    	AutoFeaturesWrapper afw = AutoFeaturesWrapper.getInstance();
    	
    	display.addText( "&3Selected Settings from &bplugins/Prison/autoFeaturesConfigs.yml&3:" );
    	display.addText( "&b " );
    	display.addText( "&b   options.general.isAutoManagerEnabled %s", 
    			afw.isBoolean( AutoFeatures.isAutoManagerEnabled ));
    	
    	
    	if ( afw.isBoolean( AutoFeatures.isAutoManagerEnabled ) ) {
    		
    		
    		display.addText( "&b " );
    		display.addText( "&b    options.blockBreakEvents.applyBlockBreaksThroughSyncTask:  %s", 
    				afw.getMessage( AutoFeatures.applyBlockBreaksThroughSyncTask ) );
    		
    		display.addText( "&b    options.blockBreakEvents.cancelAllBlockBreakEvents:  %s", 
    				afw.getMessage( AutoFeatures.cancelAllBlockBreakEvents ) );
    		
    		display.addText( "&b    options.blockBreakEvents.cancelAllBlockEventBlockDrops:  %s", 
    				afw.getMessage( AutoFeatures.cancelAllBlockEventBlockDrops ) );
    		
    		
    		display.addText( "&b    options.blockBreakEvents.TokenEnchantBlockExplodeEventPriority:  %s", 
    				afw.getMessage( AutoFeatures.TokenEnchantBlockExplodeEventPriority ) );
    		
    		display.addText( "&b    options.blockBreakEvents.CrazyEnchantsBlastUseEventPriority:  %s", 
    				afw.getMessage( AutoFeatures.CrazyEnchantsBlastUseEventPriority ) );
    		
    		display.addText( "&b    options.blockBreakEvents.RevEnchantsExplosiveEventPriority:  %s", 
    				afw.getMessage( AutoFeatures.RevEnchantsExplosiveEventPriority ) );

    		display.addText( "&b    options.blockBreakEvents.RevEnchantsJackHammerEventPriority:  %s", 
    				afw.getMessage( AutoFeatures.RevEnchantsJackHammerEventPriority ) );
    		
    		display.addText( "&b    options.blockBreakEvents.ZenchantmentsBlockShredEventPriority:  %s", 
    				afw.getMessage( AutoFeatures.ZenchantmentsBlockShredEventPriority ) );
    		
    		display.addText( "&b    options.blockBreakEvents.PrisonEnchantsExplosiveEventPriority:  %s", 
    				afw.getMessage( AutoFeatures.PrisonEnchantsExplosiveEventPriority ) );
    		
    		display.addText( "&b    options.blockBreakEvents.ProcessPrisons_ExplosiveBlockBreakEventsPriority:  %s", 
    				afw.getMessage( AutoFeatures.ProcessPrisons_ExplosiveBlockBreakEventsPriority ) );
    		
    		
    		
    		display.addText( "&b " );
    		display.addText( "&b  Normal Drops (if auto pickup is off):" );
    		display.addText( "&b    options.normalDrop.isProcessNormalDropsEvents:  %s", 
    				afw.isBoolean( AutoFeatures.handleNormalDropsEvents ) );
    		
    		display.addText( "&b " );
    		display.addText( "&7  NOTE: If this is enabled, then lore and perms will override the settings for " );
    		display.addText( "&7        pickup, smelt, and block when they are turned off." );
    		
    		
    		display.addText( "&b " );
    		
    		
    		display.addText( "&b   options.autoPickup.autoPickupEnabled %s", 
    				afw.isBoolean( AutoFeatures.autoPickupEnabled ));
    		
    		display.addText( "&b   options.autoSmelt.autoSmeltEnabled %s", 
    				afw.isBoolean( AutoFeatures.autoSmeltEnabled ));
    		display.addText( "&b   options.autoBlock.autoBlockEnabled %s", 
    				afw.isBoolean( AutoFeatures.autoBlockEnabled ));
    		
    		
    		
    		display.addText( "&b " );
    		display.addText( "&b   options.general.isCalculateDurabilityEnabled %s", 
    				afw.isBoolean( AutoFeatures.isCalculateDurabilityEnabled ));
    		display.addText( "&b   options.general.isCalculateFortuneEnabled %s", 
    				afw.isBoolean( AutoFeatures.isCalculateFortuneEnabled ));
    		display.addText( "&b   options.general.isCalculateAltFortuneOnAllBlocksEnabled %s", 
    				afw.isBoolean( AutoFeatures.isCalculateAltFortuneOnAllBlocksEnabled ));
    		display.addText( "&b   options.general.isCalculateXPEnabled %s", 
    				afw.isBoolean( AutoFeatures.isCalculateXPEnabled ));
    		display.addText( "&b   options.general.givePlayerXPAsOrbDrops %s", 
    				afw.isBoolean( AutoFeatures.givePlayerXPAsOrbDrops ));
    		display.addText( "&b   options.general.fortuneMultiplierGlobal %s", 
    				afw.getMessage( AutoFeatures.fortuneMultiplierGlobal ));
    		display.addText( "&b   options.general.fortuneMultiplierMax %s", 
    				afw.getMessage( AutoFeatures.fortuneMultiplierMax ));
    		
    		display.addText( "&b " );
    		display.addText( "&b   options.isProcessMcMMOBlockBreakEvents %s", 
    				afw.isBoolean( AutoFeatures.isProcessMcMMOBlockBreakEvents ));
    		display.addText( "&b   options.isProcessEZBlocksBlockBreakEvents %s", 
    				afw.isBoolean( AutoFeatures.isProcessEZBlocksBlockBreakEvents ));
    		display.addText( "&b   options.isProcessQuestsBlockBreakEvents %s", 
    				afw.isBoolean( AutoFeatures.isProcessQuestsBlockBreakEvents ));
    		display.addText( "&b " );
    		
    		
    		display.addText( "&b " );
    		display.addText( "&b   options.lore.isLoreEnabled %s", 
    				afw.isBoolean( AutoFeatures.isLoreEnabled ));
    		display.addText( "&b   options.lore.loreTrackBlockBreakCount %s", 
    				afw.isBoolean( AutoFeatures.loreTrackBlockBreakCount ));
    		display.addText( "&b   options.lore.loreBlockBreakCountName %s", 
    				afw.getMessage( AutoFeatures.loreBlockBreakCountName ));
    		
    		display.addText( "&b   options.lore.loreBlockExplosionCountName %s", 
    				afw.getMessage( AutoFeatures.loreBlockExplosionCountName ));
    		display.addText( "&b   options.lore.loreDurabiltyResistance %s", 
    				afw.isBoolean( AutoFeatures.loreDurabiltyResistance ));
    		display.addText( "&b   options.lore.loreDurabiltyResistanceName %s", 
    				afw.getMessage( AutoFeatures.loreDurabiltyResistanceName ));
    		display.addText( "&b " );
    	}
    	
    	
    	
    	display.send(sender);
    	
    	// altPermissions are now a part of this command.
//    	// After displaying the help information above, rerun the same command for the player
//    	// with the help keyword to show the permissions.
//    	String formatted = "prison autofeatures help";
//		Prison.get().getPlatform().dispatchCommand(sender, formatted);
        
    }
    
    
    @Command(identifier = "prison debug", 
    		description = "Enables debugging and trouble shooting information. " +
    				"For internal use only. Do not use unless instructed. This will add a lot of "
    				+ "data to the console.", 
    		onlyPlayers = false, permissions = "prison.debug",
    		aliases = {"prison support debug"} )
    public void toggleDebug(CommandSender sender,
    		@Wildcard(join=true)
    		@Arg(name = "targets", def = " ",
    				description = "Optional. Enable or disable a debugging target, or set a count down timer. " +
    					"[on, off, targets, (count-down-timer), selective, jarScan, " +
    					"testPlayerUtil, testLocale, rankup, player=<playerName> ] " +
    				"Use 'targets' to list all available targets.  Use 'on' or 'off' to toggle " +
    				"on and off individual targets, or 'all' targets if no target is specified. " +
    				"If any targets are enabled, then debug in general will be enabled. Selective will only " +
    				"activate debug with the specified targets. " +
    				"A positive integer value will enable the count down timer mode to enable " +
    				"debug mode for a number of loggings, then debug mode will be turned off. " +
    				"jarScan will identify what Java version compiled the class files within the listed jars. " +
    				"If a player name is given, all debug messages that are tracked by player name will only be " +
    				"logged for that player.  Example: `/debug playerName=RoyalBlueRanger 5` will log only " +
    				"5 debug messages for that player, then debug mode will be disabled.  "
    						) String targets ) {
    	
    	String playerName = null;
    	
		String playerStr = extractParameter("player=", targets);
		if ( playerStr != null ) {
			targets = targets.replace( playerStr, "" );
			playerName = playerStr.replace( "player=", "" ).trim();
			
			if ( playerName != null ) {
				
				Output.get().setDebugPlayerName( playerName );
				
				sender.sendMessage( "Prison Debug Mode enabled only for player: " + playerName );
			}
			else {
				Output.get().setDebugPlayerName( null );
				
				sender.sendMessage( "Prison Debug Only-for-player Mode has been disabled. Debug mode will be logged for all players." );
			}
		}
    	
    	if ( targets != null && "jarScan".equalsIgnoreCase( targets ) ) {
    		
    		PrisonJarReporter pjr = new PrisonJarReporter();
    		pjr.scanForJars();
    		pjr.dumpJarDetails();
    		
    		return;
    	}
    	
    	
    	if ( targets != null && "testLocale".equalsIgnoreCase( targets ) ) {
    		
    		coreDebugTestLocaleseMsg( sender );
    		
    		return;
    	}
    	
    	if ( targets != null && "testPlayerUtil".equalsIgnoreCase( targets ) ) {
    		
    		Player player = getPlayer( sender, "RoyalBlueRanger" );
    		Prison.get().getPlatform().testPlayerUtil( player.getUUID() );
    		
    		return;
    	}
    	
    	// Applies normal and selective targets:
    	Output.get().applyDebugTargets( targets );
    	
    	String message = "&7Global Debug Logging is " + (Output.get().isDebug() ? "&3enabled" : "&cdisabled");
    	sender.sendMessage( message );
    	
    	Set<DebugTarget> activeDebugTargets = Output.get().getActiveDebugTargets();
    	
    	if ( activeDebugTargets.size() > 0 ) {
    		message = ". Note: Active Debug Targets:";
    		sender.sendMessage( message );
    		
    		for ( DebugTarget target : activeDebugTargets )
			{
    			message = String.format( ". . Target: %s", target.name() );
    			sender.sendMessage( message );
			}
    	}
    	
    	Set<DebugTarget> selectiveDebugTargets = Output.get().getSelectiveDebugTargets();
    	
    	if ( selectiveDebugTargets.size() > 0 ) {
    		message = ". Selective Debug Targets:";
    		sender.sendMessage( message );
    		
    		for ( DebugTarget target : selectiveDebugTargets )
    		{
    			message = String.format( ". . Target: %s", target.name() );
    			sender.sendMessage( message );
    		}
    	}
    	
    	String validTargets = Output.get().getDebugTargetsString();
    	message = String.format( ". Valid Targets: %s", validTargets );
    	sender.sendMessage( message );

    }
    
    
	
	private String extractParameter( String key, String options ) {
		return extractParameter( key, options, true );
	}
	private String extractParameter( String key, String options, boolean tryLowerCase ) {
		String results = null;
		int idx = options.indexOf( key );
		if ( idx != -1 ) {
			int idxEnd = options.indexOf( " ", idx );
			if ( idxEnd == -1 ) {
				idxEnd = options.length();
			}
			results = options.substring( idx, idxEnd );
		}
		else if ( tryLowerCase ) {
			// try again, but lowercase the key
			results = extractParameter( key.toLowerCase(), options, false );
		}
		return results;
	}
    

	@Command(identifier = "prison findCmd", 
    		description = "For internal use only. Do not use.  This command is used by internal code to look up " +
    				"a command to get the registered command.  Example would be when prison is registering  the " +
    				"command '/backpack' and it's already been registered, bukkit would then try to register prison's " +
    				"version as '/prison:backpack'.  This information is used to ensure that internal calls go to the " +
    				"correct prison commands instead of another plugin's.", 
    		onlyPlayers = false, permissions = "prison.debug" )
    public void findCommand(CommandSender sender,
    		@Arg(name = "command", description = "The command to search for" ) String command
    		) {
    	
    	String registered = Prison.get().getCommandHandler().findRegisteredCommand( command );
    	
    	Output.get().logInfo( "&7Prison Find Registered Command: original= &3%s  &7registered= &3%s", 
    			command, registered );
    }
    
	

	@Command(identifier = "prison support cmdStats", 
    		description = "Stats on Prison command stats. Only shows actual commands that are used.", 
    		onlyPlayers = false, permissions = "prison.debug" )
    public void statsCommand(CommandSender sender ) {
    	
    	List<String> cmds = getCommandStats();
    	for (String cmd : cmds) {
			
    		Output.get().logInfo( cmd );
		}
    }
	
	private List<String> getCommandStats() {
		List<String> results = new ArrayList<>();
		
		DecimalFormat iFmt = Prison.get().getDecimalFormatInt();
		DecimalFormat dFmt = Prison.get().getDecimalFormatDouble();
		
    	TreeSet<RegisteredCommand> allCmds = Prison.get().getCommandHandler().getAllRegisteredCommands();
    	
    	results.add( "Prison Command Stats:" );
    	results.add( 
    			Output.stringFormat( "    &a&n%-40s&r  &a&n%7s&r  &a&n%-11s&r", 
    					" Commands     ", " Usage ", "  Avg ms  ") );
    	
    	int count = 0;
    	int totals = 0;
    	double totalDuration = 0d;
    	for (RegisteredCommand cmd : allCmds) {
			
    		if ( cmd.getUsageCount() > 0 ) {
    			
    			double duration = cmd.getUsageRunTimeNanos() / (double) cmd.getUsageCount() / 1000000.0d;
    			
    			results.add( Output.stringFormat( "    &2%-40s  &2%7s  &2%11s",
    					cmd.getCompleteLabel(), 
    					iFmt.format( cmd.getUsageCount() ),
    					dFmt.format( duration )
    					) );
    			count++;
    			totals += cmd.getUsageCount();
    			totalDuration += cmd.getUsageRunTimeNanos();
    		}
		}
    	
    	results.add( Output.stringFormat("  &3Total Registered Prison Commands: &7%9s", iFmt.format( allCmds.size() )) );
    	results.add( Output.stringFormat("  &3Total Prison Commands Listed:     &7%9s", iFmt.format( count )) );
    	results.add( Output.stringFormat("  &3Total Prison Command Usage:       &7%9s", iFmt.format( totals )) );
    	
    	double avgDuration = totalDuration / (double) count / 1000000.0d;
    	results.add( Output.stringFormat("  &3Average Command Duration ms:      &7%9s", dFmt.format( avgDuration )) );
    	
    	results.add( "  &d&oNOTE: Async Commands like '/mines reset' will not show actual runtime values. " );

    	
		return results;
	}
    
	
	@Command(identifier = "prison support runCmd", 
			description = "For use in other plugins to force a player to run a prison command. " +
					"Its been seen that when enabling NPCs to run commands as players, that the " +
					"NPC is still the active entity associated with the command when it enters " +
					"the Prison command handler.  This command will ensure the actual player is " +
					"used.  The commands that are specified to run, do not have to prison's commands.", 
					onlyPlayers = false, permissions = "prison.runcmd",
					aliases = "prison utils runCmd")
	public void runCommand(CommandSender sender,
			@Arg(name = "player", 
    		description = "Player to run the command as.") String playerName,
    		
    		@Wildcard(join=true)
			@Arg(name = "command", description = "The command to run for the player." ) String command
			) {
		
		
    	if ( playerName == null || playerName.isEmpty() ) {
    		
    		coreRunCommandNameRequiredMsg(sender);

    		return;
    	}
    	
    	if ( command == null || command.trim().length() == 0 ) {
    		coreRunCommandCommandRequiredMsg(sender);
    	}

    	Player player = getPlayer( playerName );
    	
    	PrisonAPI.dispatchCommand( player, command );

	}
	
	
    @Command(identifier = "prison support setSupportName", 
    		description = "This sets the support name that is used with the submissions so its " +
    				"easier to track who the submissions belong to.  It is recommended that you " +
    				"use your discord name, or at least something similar. ", 
    				onlyPlayers = false, permissions = "prison.debug" )
    public void supportSetName(CommandSender sender,
    		@Wildcard(join=true)
    		@Arg(name = "supportName", 
    				description = "The support name to use with support submissions." ) 
    			String supportName
    		) {
    	
    	if ( supportName == null || supportName.trim().isEmpty() ) {
    		sender.sendMessage( "A value for supportName is required." );
    		return;
    	}

    	setSupportName( supportName );
    	
    	sender.sendMessage( String.format( "The support name has been set to: %s", getSupportName() ) );
    	sender.sendMessage( "You can now use the support submit options." );

    }
    
    
    
    @Command(identifier = "prison support saveToFile", 
    		description = "This sets the  target of the support data to a local file.", 
    				onlyPlayers = false, permissions = "prison.debug" )
    public void supportSaveToFile(CommandSender sender,
    		@Wildcard(join=true)
    @Arg(name = "options", 
    description = "Enables, or disables the support file. 'basic' will enable the support file " +
    				"and add 'version', 'ranks', 'mines, and 'configs to the file automatically. " +
    				"Defaults to 'enable'. [enable, basic, disable]" ) 
    String options
    		) {

    	
    	if ( getSupportName() == null || getSupportName().trim().isEmpty() ) {
    		sender.sendMessage( "The support name needs to be set prior to using this command." );
    		sender.sendMessage( "Use &7/prison support setSupportName help" );
    		
    		return;
    	}
    	
    	
    	if ( options != null && options.toLowerCase().startsWith( "disable" ) ) {

    		setSupportFile( null );
    	}
    	else {
    		
    		setSupportFile( new PrisonSupportFiles() );
    		getSupportFile().setupSupportFile( getSupportName() );
    	}
    	
    	
    	
    	sender.sendMessage( String.format( "Save the support data to file: %b",
    														getSupportFile() != null ) );
    	
    	if ( getSupportFile() != null ) {
    		sender.sendMessage( "You can now use the support submit options and they will be save to files." );
    		
    		sender.sendMessage( "  Your support save file location: " +
    				getSupportFile().getSupportFile().getAbsolutePath() );
    		
    		if ( options.toLowerCase().equals( "basic" ) ) {
    			
    			StringBuilder text = Prison.get().getPrisonStatsUtil().getSupportSubmitBasic();
    			
    			getSupportFile().saveToSupportFile( text, getSupportName() );

    			sender.sendMessage("  - Support 'basic' data was just added to the support output file." );
        		sender.sendMessage("  - Includes: version, listeners, command stats, ladders, Ranks, Mines, and all Config files." );
        		sender.sendMessage( getSupportFile().getFileStats( text.length() ) );
    			
//    			supportSubmitVersion(sender);
//    			supportSubmitRanks(sender);
//    			supportSubmitMines(sender);
//    			supportSubmitConfigs(sender);
    		}
    	}
    	else {
    		sender.sendMessage( "Support save file has been disabled. Support files have not been removed." );
    	}
    }
    
   
    @Command(identifier = "prison support colorTest", 
    		description = "Displays a test swatch of minecraft colors .", 
    				onlyPlayers = false, permissions = "prison.debug" )
    public void supportColorTest(CommandSender sender
    		) {
    	
    	StringBuilder sb = Prison.get().getPrisonStatsUtil().getColorTest();
    	
    	for (String line : sb.toString().split("\n")) {
			Output.get().logInfo(line);
		}
    }
    
    @Command(identifier = "prison support submit version", 
    		description = "For Prison support: This will copy the contents of '/prison version all' " +
    				"to paste.helpch.at so it can be easily shared with Prison's support staff .", 
    				onlyPlayers = false, permissions = "prison.debug" )
    public void supportSubmitVersion(CommandSender sender
    		) {
    	
    	
    	if ( getSupportName() == null || getSupportName().trim().isEmpty() ) {
    		sender.sendMessage( "The support name needs to be set prior to using this command." );
    		sender.sendMessage( "Use &7/prison support setSupportName help" );
    		
    		return;
    	}
    	
    	
    	StringBuilder text = new StringBuilder();
    	
    	text.append( "NOTE: Listeners and Configs information is provided below.\n\n" );
    			
    	text.append( Prison.get().getPrisonStatsUtil().getSupportSubmitVersionData() );
    	
    	int idx = text.indexOf("{br}");
    	while ( idx != -1 ) {
    		// Convert `{br}` to `\n`:
    		text.replace(idx, idx+4, "\n");
    		
    		idx = text.indexOf("{br}");
    	}
    	
		
		// Add all of the listeners details:
    	text.append( "\n\n" );
    	text.append(
    				Prison.get().getPrisonStatsUtil().getSupportSubmitListenersData( "all" )
					);
    	
    	// Include the command stats:
    	text.append( Prison.get().getPrisonStatsUtil().getCommandStatsDetailData() );
//    	text.append( "\n\n" );
//    	List<String> cmdStats = getCommandStats();
//    	for (String cmd : cmdStats) {
//			text.append( cmd ).append( "\n" );
//		}
		
    	
    	
    	// Include Prison backup logs:
    	text.append( Prison.get().getPrisonStatsUtil().getPrisonBackupLogsData() );
//    	text.append( "\n\n" );
//    	text.append( "Prison Backup Logs:" ).append( "\n" );
//    	List<String> backupLogs = getPrisonBackupLogs();
//    	
//    	for (String log : backupLogs) {
//    		text.append( Output.decodePercentEncoding(log) ).append( "\n" );
//		}

    	

    	text.append( Prison.get().getPrisonStatsUtil().getSupportSubmitConfigsData() );
    	
    	
    	
    	if ( getSupportFile() != null ) {
    		
    		getSupportFile().saveToSupportFile( text, getSupportName() );

    		sender.sendMessage("  - Support 'version' data was just added to the support output file." );
    		sender.sendMessage( getSupportFile().getFileStats( text.length() ) );
    	}
    	else {
    		
    		PrisonPasteChat pasteChat = new PrisonPasteChat( getSupportName(), getSupportURLs() );
    		
    		String helpURL = pasteChat.post( text.toString() );
    		
    		getSupportURLs().put( "Submit version:", helpURL );
    		
    		if ( helpURL != null ) {
    			
    			sender.sendMessage( "Prison's support information has been pasted. Copy and " +
    					"paste this URL in to Prison's Discord server." );
    			sender.sendMessage( String.format( "Paste this URL: %s", helpURL ));
    		}
    		else {
    			sender.sendMessage( "There was an error trying to generate the paste.helpch.at URL." );
    		}
    		
    	}
    	
    	
    }


    
//    @Command(identifier = "prison support submit configs", 
//    		description = "For Prison support: This will copy the contents of Prison's config " +
//    				"file to paste.helpch.at so it can be easily shared with Prison's " +
//    				"support staff.  This will include the following: config.yml plugin.yml " +
//    				"autoFeaturesConfig.yml modules.yml module_conf/mines/config.json " +
//    				"SellAllConfig.yml GuiConfig.yml backpacks/backpacksconfig.yml", 
//    				onlyPlayers = false, permissions = "prison.debug" )
//    public void supportSubmitConfigs(CommandSender sender
//    		) {
//    	
//    	
//    	if ( getSupportName() == null || getSupportName().trim().isEmpty() ) {
//    		sender.sendMessage( "The support name needs to be set prior to using this command." );
//    		sender.sendMessage( "Use &7/prison support setSupportName help" );
//    		
//    		return;
//    	}
//    	
//    	StringBuilder text = Prison.get().getPrisonStatsUtil().getSupportSubmitConfigsData();
//    	
//    	
//    	
//    	if ( getSupportFile() != null ) {
//    		
//    		getSupportFile().saveToSupportFile( text, getSupportName() );
//
//    		sender.sendMessage("  - Support 'configs' data was just added to the support output file." );
//    		sender.sendMessage( getSupportFile().getFileStats( text.length() ) );
//    	}
//    	else {
//    		
//    		PrisonPasteChat pasteChat = new PrisonPasteChat( getSupportName(), getSupportURLs() );
//    		
//    		String helpURL = pasteChat.postKeepColorCodes( text.toString() );
//    		
//    		getSupportURLs().put( "Submit configs:", helpURL );
//    		
//    		if ( helpURL != null ) {
//    			
//    			sender.sendMessage( "Prison's support information has been pasted. Copy and " +
//    					"paste this URL in to Prison's Discord server." );
//    			sender.sendMessage( String.format( "Paste this URL: %s", helpURL ));
//    		}
//    		else {
//    			sender.sendMessage( "There was an error trying to generate the paste.helpch.at URL." );
//    		}
//    		
//    	}
//    }


    @Command(identifier = "prison support submit ranks", 
    		description = "For Prison support: This will copy the contents of Prison's " +
    				"ladders and ranks configs to paste.helpch.at so it can be " +
    				"easily shared with Prison's support staff.", 
    				onlyPlayers = false, permissions = "prison.debug" )
    public void supportSubmitRanks(CommandSender sender
    		) {
    	
    	
    	if ( getSupportName() == null || getSupportName().trim().isEmpty() ) {
    		sender.sendMessage( "The support name needs to be set prior to using this command." );
    		sender.sendMessage( "Use &7/prison support setSupportName help" );
    		
    		return;
    	}
    	
    	
    	// List Ladder and rank lists:
    	StringBuilder text = Prison.get().getPrisonStatsUtil().getSupportSubmitRanksData();
    	
    	// List rank files:
    	text.append( Prison.get().getPrisonStatsUtil().getSupportSubmitRanksFileData() );
    	
    	
    	if ( getSupportFile() != null ) {
    		
    		getSupportFile().saveToSupportFile( text, getSupportName() );

    		sender.sendMessage("  - Support 'ranks' data was just added to the support output file." );
    		sender.sendMessage( getSupportFile().getFileStats( text.length() ) );
    	}
    	else {
    		
    		PrisonPasteChat pasteChat = new PrisonPasteChat( getSupportName(), getSupportURLs() );
    		
    		String helpURL = pasteChat.post( text.toString() );
    		
    		getSupportURLs().put( "Submit ranks:", helpURL );
    		
    		if ( helpURL != null ) {
    			
    			sender.sendMessage( "Prison's support information has been pasted. Copy and " +
    					"paste this URL in to Prison's Discord server." );
    			sender.sendMessage( String.format( "Paste this URL: %s", helpURL ));
    		}
    		else {
    			sender.sendMessage( "There was an error trying to generate the paste.helpch.at URL." );
    		}
    	}
    	
    }

    
    
    @Command(identifier = "prison support submit mines", 
    		description = "For Prison support: This will copy the contents of Prison's " +
    				"mines configs to paste.helpch.at so it can be " +
    				"easily shared with Prison's support staff.", 
    				onlyPlayers = false, permissions = "prison.debug" )
    public void supportSubmitMines(CommandSender sender
    		) {
    	
    	
    	if ( getSupportName() == null || getSupportName().trim().isEmpty() ) {
    		sender.sendMessage( "The support name needs to be set prior to using this command." );
    		sender.sendMessage( "Use &7/prison support setSupportName help" );
    		
    		return;
    	}
    	
 
    	StringBuilder text = Prison.get().getPrisonStatsUtil().getSupportSubmitMinesData();
    	
    	
    	if ( getSupportFile() != null ) {
    		
    		getSupportFile().saveToSupportFile( text, getSupportName() );

    		sender.sendMessage("  - Support 'mines' data was just added to the support output file." );
    		sender.sendMessage( getSupportFile().getFileStats( text.length() ) );
    	}
    	else {
    		
    		PrisonPasteChat pasteChat = new PrisonPasteChat( getSupportName(), getSupportURLs() );
    		
    		String helpURL = pasteChat.post( text.toString() );
    		
    		getSupportURLs().put( "Submit mines:", helpURL );
    		
    		if ( helpURL != null ) {
    			
    			sender.sendMessage( "Prison's support information has been pasted. Copy and " +
    					"paste this URL in to Prison's Discord server." );
    			sender.sendMessage( String.format( "Paste this URL: %s", helpURL ));
    		}
    		else {
    			sender.sendMessage( "There was an error trying to generate the paste.helpch.at URL." );
    		}
    	}
    	
    }

    
    
    
    @Command(identifier = "prison backpacks listOld", 
    		description = "Using the new prison backpack cache interface, this will list existing "
    				+ "backpacks under the old prison backpack system. ", 
    				onlyPlayers = false, permissions = "prison.debug" )
    public void backpacksListOldCmd(CommandSender sender
    		) {
    	
    	BackpackConverterOldPrisonBackpacks converter = new BackpackConverterOldPrisonBackpacks();
    	
    	converter.convertOldBackpacks();
    
    	
    }

    
    
    
//    
//	private StringBuilder getSupportSubmitVersionData() {
//		ChatDisplay display = displayVersion("ALL");
//		StringBuilder text = display.toStringBuilder();
//		return text;
//	}
//    
//	private StringBuilder getSupportSubmitConfigsData() {
//		Prison.get().getPlatform().saveResource( "plugin.yml", true );
//    	
//    	String fileNames = "config.yml plugin.yml backups/versions.txt " + 
//    			"autoFeaturesConfig.yml blockConvertersConfig.json " + 
//    			"modules.yml module_conf/mines/config.json module_conf/mines/mineBombsConfig.json " +
//    			"SellAllConfig.yml GuiConfig.yml backpacks/backpacksconfig.yml";
//    	List<File> files = convertNamesToFiles( fileNames );
//    	
//    	
//    	StringBuilder text = new StringBuilder();
//
//    	for ( File file : files ) {
//			
//    		addFileToText( file, text );
//    		
//    		if ( file.getName().equalsIgnoreCase( "plugin.yml" ) ) {
//    			file.delete();
//    		}
//		}
//		return text;
//	}
//    
//
//	private StringBuilder getSupportSubmitRanksData() {
//		List<File> files = listFiles( "data_storage/ranksDb/ladders/", ".json" );
//    	files.addAll( listFiles( "data_storage/ranksDb/ranks/", ".json" ) );
//    	
//    	
//    	StringBuilder text = new StringBuilder();
//    	
//    	
//    	text.append( Prison.get().getPlatform().getRanksListString() );
//    	printFooter( text );
// 
//    	
//    	for ( File file : files ) {
//    		
//    		addFileToText( file, text );
//    		
//    	}
//		return text;
//	}
//	
//	
//	private StringBuilder getSupportSubmitMinesData() {
//		List<File> files = listFiles( "data_storage/mines/mines/", ".json" );
//    	Collections.sort( files );
//    	
//    	StringBuilder text = new StringBuilder();
//    	
//    	text.append( "\n" );
//    	text.append( "Table of contents:\n" );
//    	text.append( "  1. Mine list - All mines including virtual mines: /mines list all\n" );
//    	text.append( "  2. Mine info - All mines: /mines info <mineName> all\n" );
//    	text.append( "  3. Mine files - Raw JSON dump of all mine configuration files.\n" );
//    	text.append( "\n" );
//    	
//    	// Display a list of all mines, then display the /mines info <mineName> all for each:
//    	text.append( Prison.get().getPlatform().getMinesListString() );
//    	printFooter( text );
//    	
//    	
//    	
//    	for ( File file : files ) {
//    		
//    		addFileToText( file, text );
//    		
//    	}
//		return text;
//	}
    
    
//    private List<File> listFiles( String path, String fileSuffix ) {
//    	List<File> files = new ArrayList<>();
//		
//		File dataFolder = Prison.get().getDataFolder();
//		File filePaths = new File( dataFolder, path );
//		
//		for ( File file : filePaths.listFiles() ) {
//			if ( file.getName().toLowerCase().endsWith( fileSuffix.toLowerCase() )) {
//				files.add( file );
//			}
//		}
//		
//		return files;
//	}
//
//	private void addFileToText( File file, StringBuilder sb )
//	{
//    	DecimalFormat dFmt = Prison.get().getDecimalFormatInt();
//		SimpleDateFormat sdFmt = new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss" );
//    	
//    	sb.append( "\n" );
//
//    	JumboTextFont.makeJumboFontText( file.getName(), sb );
//    	
//    	sb.append( "\n" );
//    	
//		sb.append( "File Name:   " ).append( file.getName() ).append( "\n" );
//		sb.append( "File Path:   " ).append( file.getAbsolutePath() ).append( "\n" );
//		sb.append( "File Size:   " ).append( dFmt.format( file.length() ) ).append( " bytes\n" );
//		sb.append( "File Date:   " ).append( sdFmt.format( new Date(file.lastModified()) ) ).append( " bytes\n" );
//		sb.append( "File Stats:  " )
//					.append( file.exists() ? "EXISTS " : "" )
//					.append( file.canRead() ? "READABLE " : "" )
//					.append( file.canWrite() ? "WRITEABLE " : "" )
//					.append( "\n" );
//		
//		sb.append( "\n" );
//		sb.append( "=== ---  ---   ---   ---   ---   ---   ---   ---  --- ===\n" );
//		sb.append( "\n" );
//
//		
//		if ( file.exists() && file.canRead() ) {
//			readFileToStringBulider( file, sb );
//		}
//		else {
//			sb.append( "Warning: The file is not readable so it cannot be included.\n" );
//		}
//		
//		
//		printFooter( sb );
//	}
	
//	public static void printFooter( StringBuilder sb ) {
//		
//		sb.append( "\n\n\n" );
//		sb.append( "===  ---  ===   ---   ===   ---   ===   ---   ===  ---  ===\n" );
//		sb.append( "=== # # ### # # # ### # # # ### # # # ### # # # ### # # ===\n" );
//		sb.append( "===  ---  ===   ---   ===   ---   ===   ---   ===  ---  ===\n" );
//		sb.append( "\n\n" );
//		
//	}
//
//	private List<File> convertNamesToFiles( String fileNames )
//	{
//		List<File> files = new ArrayList<>();
//		
//		File dataFolder = Prison.get().getDataFolder();
//		
//		for ( String fileName : fileNames.split( " " )) {
//			File file = new File( dataFolder, fileName );
//			files.add( file );
//		}
//		
//		return files;
//	}

	@Command(identifier = "prison support submit latestLog", 
    		description = "For Prison support: This will copy the contents of `logs/latest.log` " +
    				"to paste.helpch.at so it can be easily shared with Prison's support staff .", 
    				onlyPlayers = false, permissions = "prison.debug" )
    public void supportSubmitLatestLog(CommandSender sender
    		) {
    	
    	
    	if ( getSupportName() == null || getSupportName().trim().isEmpty() ) {
    		sender.sendMessage( "The support name needs to be set prior to using this command." );
    		sender.sendMessage( "Use &7/prison support setSupportName help" );
    		
    		return;
    	}
    	
    	
    	File latestLogFile = new File( Prison.get().getDataFolder().getParentFile().getParentFile(), 
    									"logs/latest.log");
    	
    	sender.sendMessage( "### log path: " + latestLogFile.getAbsolutePath() );
    	
    	
    	StringBuilder logText = new StringBuilder();
    	
    	if ( latestLogFile.exists() && latestLogFile.canRead() ) {
    		
    		readFileToStringBulider( latestLogFile, logText );
    		
    		if ( logText != null ) {
    			
    			PrisonPasteChat pasteChat = new PrisonPasteChat( getSupportName(), getSupportURLs() );
    			
    			String helpURL = pasteChat.post( logText.toString() );
    			
    			getSupportURLs().put( "Submit lastlog:", helpURL );
    			
    			if ( helpURL != null ) {
    				
    				sender.sendMessage( "Prison's support information has been pasted. Copy and " +
    						"paste this URL in to Prison's Discord server." );
    				sender.sendMessage( String.format( "Paste this URL: %s", helpURL ));
    			}
    			else {
    				// Do nothing since if helpURL is null, then it has probably
    				// already sent an error message.
    			}
    			return;
    		}
    	}
    	
    	sender.sendMessage( "Unable to send log file.  Unknown reason why." );
    }

	private void readFileToStringBulider( File textFile, StringBuilder text )
	{
		try (
				BufferedReader br = Files.newBufferedReader( textFile.toPath() );
			) {
			String line = br.readLine();
			while ( line != null && text.length() < PrisonPasteChat.HASTEBIN_MAX_LENGTH ) {
				
				text.append( line ).append( "\n" );
				
				line = br.readLine();
			}
			
			if ( text.length() > PrisonPasteChat.HASTEBIN_MAX_LENGTH ) {
				
				String trimMessage = "\n\n### Log has been trimmed to a max length of " +
						PrisonPasteChat.HASTEBIN_MAX_LENGTH + "\n";
				int pos = PrisonPasteChat.HASTEBIN_MAX_LENGTH - trimMessage.length();
				
				text.insert( pos, trimMessage );
				text.setLength( PrisonPasteChat.HASTEBIN_MAX_LENGTH );
			}
			
		}
		catch ( IOException e ) {
			Output.get().logInfo( "Failed to read log file: %s  [%s]", 
					textFile.getAbsolutePath(), e.getMessage() );
			return;
		}
	}
    
    
//    @Command(identifier = "prison support submit listeners", 
//    		description = "For Prison support: This will copy the server's active listeners " +
//    				"for blockBreak, chat, and playerInteracts to paste.helpch.at so it can be " +
//    				"easily shared with Prison's support staff.", 
//    				onlyPlayers = false, permissions = "prison.debug" )
//    public void supportSubmitListeners(CommandSender sender
//    		) {
//    	
//    	
//    	if ( getSupportName() == null || getSupportName().trim().isEmpty() ) {
//    		sender.sendMessage( "The support name needs to be set prior to using this command." );
//    		sender.sendMessage( "Use &7/prison support setSupportName help" );
//    		
//    		return;
//    	}
//    	
// 
//    	StringBuilder text = Prison.get().getPrisonStatsUtil().getSupportSubmitListenersData( "all" );
//    	
//    	PrisonPasteChat pasteChat = new PrisonPasteChat( getSupportName(), getSupportURLs() );
//    	
//    	String helpURL = pasteChat.post( text.toString() );
//    	
//    	getSupportURLs().put( "Submit Listeners:", helpURL );
//    	
//    	if ( helpURL != null ) {
//    		
//    		sender.sendMessage( "Prison's support information has been pasted. Copy and " +
//    				"paste this URL in to Prison's Discord server." );
//    		sender.sendMessage( String.format( "Paste this URL: %s", helpURL ));
//    	}
//    	else {
//    		sender.sendMessage( "There was an error trying to generate the paste.helpch.at URL." );
//    	}
//    	
//    	
//    }


    

	@Command(identifier = "prison support listeners", 
    		description = "For Prison support: Provide a 'dump' of all event listeners.", 
    				onlyPlayers = false, permissions = "prison.debug" )
    public void supportListenersDump(CommandSender sender,
    		@Arg(name = "listener", def = " ",
			description = "Provides a detailed list of all registered event listeners for" +
					"the various event types.  BlockBreak listeners will include all " +
					"listeners that are being monitored within auto features. " +
				"[all, blockBreak, chat, playerInteract]"
					) String listener
    		) {
		
		if ( listener == null ) {
			String message = "You must supply a listener in order to use this command.";
			sender.sendMessage( message );
			return;
		}
		
		String results = Prison.get().getPrisonStatsUtil().getSupportSubmitListenersData( listener ).toString();
		
//		String results = null;
//		
//    	if ( "blockBreak".equalsIgnoreCase( listener ) ) {
//    		
//    		results = Prison.get().getPlatform().dumpEventListenersBlockBreakEvents();
//    	}
//    	
//    	if ( "chat".equalsIgnoreCase( listener ) ) {
//    		
//    		results = Prison.get().getPlatform().dumpEventListenersPlayerChatEvents();
//    	}
//    	
//    	if ( "traceBlockBreak".equalsIgnoreCase( listener ) ) {
//    		
//    		Prison.get().getPlatform().traceEventListenersBlockBreakEvents( sender );
//    		
//    		return;
//    	}
//    	
//    	if ( "playerInteract".equalsIgnoreCase( listener ) ) {
//    		
//    		results = Prison.get().getPlatform().dumpEventListenersPlayerInteractEvents();
//    	}
    	
		if ( results != null ) {

			for ( String line : results.split( "\n" ) ) {
				
				Output.get().logInfo( line );
			}
		}

	} 
    
	
    @Command(identifier = "prison support backup save", 
    		description = "This will make a backup of all Prison settings by creating a new " +
    				"zip file which will be stored in the directory plugins/Prison/backups.  " +
    				"After creating the backup, this will delete all temp files, backup files, etc.. " +
    				"since they will be included in the backup.", 
    				onlyPlayers = false, permissions = "prison.debug" )
    public void supportBackupPrison( CommandSender sender, 

    		@Wildcard(join=true)
    		@Arg(name = "notes", description = "Optional short note to append to the file name. Only the "
    				+ "first 20 characters will be used.",
    				def = "") String notes ) {
    	
    	PrisonBackups prisonBackup = new PrisonBackups();
    	
    	String message = prisonBackup.startBackup( BackupTypes.manual, notes );
    	
    	sender.sendMessage( message );
    	sender.sendMessage( "Backup finished." );

    }
    
    @Command(identifier = "prison support backup logs", 
    		description = "This will list Prison backup  logs that are in the file "
    				+ "`plugins/Prison/backup/versions.log`", 
    				onlyPlayers = false, permissions = "prison.debug" )
    public void supportBackupList( CommandSender sender  ) {
    	
    	ChatDisplay display = new ChatDisplay("Prison Backup Logs:");
    	
    	List<String> backupLogs = Prison.get().getPrisonStatsUtil().getPrisonBackupLogs();
//    	List<String> backupLogs = getPrisonBackupLogs();
    	
    	for (String log : backupLogs) {
			display.addText(log);
		}
    	
    	display.send(sender);
    	
    }
    
//    private List<String> getPrisonBackupLogs() {
//    	PrisonBackups prisonBackup = new PrisonBackups();
//    	List<String> backupLogs = prisonBackup.backupReport02BackupLog();
//    	return backupLogs;
//    }
	
    
    @Command(identifier = "prison tokens balance", 
    		description = "Prison tokens: a player's current balance.", 
    		// aliases = "tokens bal",
    		permissions = "tokens.bal",
    		altPermissions = "tokens.bal.others" )
    public void tokensBalance(CommandSender sender,
 		@Arg(name = "player", def = "", description = "Player to get token balance for. " +
				"If player is online this is not required for their own balance. " +
				"This is needed if used from console or to get the balance of " +
				"another player.") String playerName
    		) {
    	
    	Player player = getPlayer( sender );
    	
    	// If player is null, then need to use the playerName, so if it's empty, we have a problem:
    	if ( ( player == null || !player.isOnline() ) && 
    			( playerName == null || playerName.isEmpty() ) ) {
    		
    		coreTokensNameRequiredMsg(sender);
//    		String message = "Prison Tokens: A player's name is required when used from console.";
//    		
//    		Output.get().logWarn( message );
    		return;
    	}
    	else 
    	if ( playerName != null && !playerName.isEmpty() ){
    		
    		if ( !sender.isOp() &&  
    				!sender.hasPermission( "tokens.bal.others" ) ) {
    			coreTokensBalanceCannotViewOthersMsg(sender);
//    			String message = "Prison Tokens: You do not have permission to view other " +
//    					"player's balances.";
//    			Output.get().logWarn( message );
    			return;
    		}
    		
    		Player tempPlayer = getPlayer( playerName );
    		
    		if ( tempPlayer != null ) {
    			player = tempPlayer;
    		}
    	}
    	
    	
//    	player.getPlayerCache()
    	
    	long tokens = player.getPlayerCachePlayerData().getTokens();
    	
    	coreTokensBalanceViewMsg( sender, player.getName(), tokens );
    	
//    	DecimalFormat dFmt = Prison.get().getDecimalFormatInt();
//    	String tokensMsg = dFmt.format( tokens );
//    	
//    	String message = String.format( "&3%s has %s tokens.", player.getName(), tokensMsg );
//    	
//    	sender.sendMessage( message );
    }
    
    @Command(identifier = "prison tokens add", 
    		description = "Prison tokens Admin: an admins tool to give more tokens to a player. The " +
    				"tokens added by admin will not count as earned tokens, so they cannot be used in " +
    				"situations where the player must earn them. This restriction can be forced to " +
    				"be attributed to being earned by the player.", 
    		permissions = "tokens.admin.add" )
    public void tokensAdd( CommandSender sender,
    		@Arg(name = "player", 
    		description = "Player to add the tokens to.") String playerName,
    		
    		@Arg(name = "amount", verifiers = "min[1]",
    		description = "The number of tokens to add to the player's account.") long amount,
    		
    		@Wildcard(join=true)
    		@Arg(name = "options", description = "Optional settings: [silent forcePlayer]  Silent suppresses " +
    				"all messages related to the transaction, including failures. The option 'forcePlayer' " +
    				"will not use the admin logging of this transaction, but instead will assign it to the " +
    				"player as if they actually earned the tokens through normal ways. It is not advised to " +
    				"use 'forcePlayer' since it could be used to cheat the system in some ways.",
    				def = "") String options
    		) {
    	
    	boolean silent = options != null && options.toLowerCase().contains( "silent" );
    	boolean forcePlayer = options != null && options.toLowerCase().contains( "forceplayer" );
    	
    	if ( playerName == null || playerName.isEmpty() ) {
    		
    		if ( !silent ) {
    			coreTokensNameRequiredMsg(sender);
//    			String message = "Prison Tokens: A player's name is required.";
//    			Output.get().logWarn( message );
    		}
    		
    		return;
    	}

//    	DecimalFormat dFmt = Prison.get().getDecimalFormatInt();
    	
    	if ( amount <= 0 ) {
    		
    		if ( !silent ) {
    			coreTokensAddInvalidAmountMsg( sender, amount );
//    			String message = 
//    					String.format( 
//    							"Prison Tokens: Invalid amount: '%s'. Must be greater than zero.",
//    							dFmt.format( amount ) );
//    			Output.get().logWarn( message );
    		}
    		
    		return;
    	}
    	
    	Player player = getPlayer( playerName );
    	
    	if ( player == null ) {
    		if ( !silent ) {
    			sender.sendMessage(
    					String.format(
    							"Prison Tokens add: Player name not found. [%s] (hardCodedMessag)", 
    							playerName ));
    		}
    		return;
    	}
    	
    	
    	PlayerCachePlayerData pCache = player.getPlayerCachePlayerData();
    	
    	long tokenBal = pCache.getTokens();
    	
    	if ( forcePlayer ) {
    		
    		pCache.addTokens( amount );
    	}
    	else {
    		
    		pCache.addTokensAdmin( amount );
    	}
    	
    	if ( pCache.getTokens() != tokenBal + amount && Output.get().isDebug() ) {
    		Output.get().logError( 
    				String.format( 
    						"AddTokens failure: player: %s  Tokens: %d  Should have been: %d",
    							player.getName(), pCache.getTokens(), tokenBal + amount ));
    	}
    	
    	if ( !silent ) {
    		
    		String message = coreTokensAddedAmountMsg( player.getName(), 
    				pCache.getTokens(), amount );
    		
//    		String tokens = dFmt.format( player.getPlayerCachePlayerData().getTokens() );
//    		
//    		String message = String.format( "&3%s now has &7%s &3tokens after adding &7%s&3.", 
//    				player.getName(),  tokens, dFmt.format( amount ) );
    		
    		// The person adding the tokens, or console:
    		sender.sendMessage( message );
    		
    		// The player getting the tokens, if they are online:
    		if ( player.isOnline() && !player.getName().equalsIgnoreCase( sender.getName() ) ) {
    			
    			player.sendMessage( message );
    		}
    	}
    	
    }
    
    @Command(identifier = "prison tokens remove", 
    		description = "Prison tokens Admin: an admins tool to remove tokens from a player. " +
    				"It is possible to remove more tokens than what the player has, which can " +
    				"be treated like a debt.  The " + 
    				"tokens added by admin will not count as earned tokens, so they cannot be used in " + 
    				"situations where the player must earn them. This restriction can be forced to " + 
    				"be attributed to being earned by the player.", 
    		permissions = "tokens.admin.add" )
    public void tokensRemove( CommandSender sender,
    		@Arg(name = "player", 
    		description = "Player to remove the tokens from.") String playerName,
    		
    		@Arg(name = "amount", verifiers = "min[1]",
    		description = "The number of tokens to remove from the player's account. " +
    				"This amount must be positive. ") long amount,
    		
    		@Wildcard(join=true)
			@Arg(name = "options", description = "Optional settings: [silent forcePlayer]  Silent suppresses " +
					"all messages related to the transaction, including failures. The option 'forcePlayer' " +
					"will not use the admin logging of this transaction, but instead will assign it to the " +
					"player as if they actually earned the tokens through normal ways. It is not advised to " +
					"use 'forcePlayer' since it could be used to cheat the system in some ways.",
					def = "") String options
    		) {
    	
    	boolean silent = options != null && options.toLowerCase().contains( "silent" );
    	boolean forcePlayer = options != null && options.toLowerCase().contains( "forceplayer" );
    	
    	if ( playerName == null || playerName.isEmpty() ) {
    		
    		if ( !silent ) {
    			coreTokensNameRequiredMsg(sender);
//    			String message = "Prison Tokens: A player's name is required.";
//    			Output.get().logWarn( message );
    		}
    		
    		return;
    	}
    	
//    	DecimalFormat dFmt = Prison.get().getDecimalFormatInt();
    	
    	if ( amount <= 0 ) {
    		
    		if ( !silent ) {
    			coreTokensAddInvalidAmountMsg( sender, amount );
//    			String message = 
//    					String.format( 
//    							"Prison Tokens: Invalid amount: '%s'. Must be greater than zero.",
//    							dFmt.format( amount ) );
//    			Output.get().logWarn( message );
    		}
    		
    		return;
    	}
    	
    	Player player = getPlayer( playerName );
    	
    	
    	if ( player == null ) {
    		if ( !silent ) {
    			sender.sendMessage(
    					String.format(
    							"Prison Tokens remove: Player name not found. [%s] (hardCodedMessag)", 
    							playerName ));
    		}
    		return;
    	}
    	
    	
    	if ( forcePlayer ) {
    		
    		player.getPlayerCachePlayerData().removeTokens( amount );
    	}
    	else {
    		
    		player.getPlayerCachePlayerData().removeTokensAdmin( amount );
    	}
    	
    	
    	if ( !silent ) {
    		
    		String message = coreTokensRemovedAmountMsg( player.getName(),
    								player.getPlayerCachePlayerData().getTokens(), amount );
//    		String tokens = dFmt.format( player.getPlayerCachePlayerData().getTokens() );
//    		
//    		String message = String.format( "&3%s now has &7%s &3tokens after removing &7%s&3.", 
//    				player.getName(),  tokens, dFmt.format( amount ) );
    		
    		// The person adding the tokens, or console:
    		sender.sendMessage( message );
    		
    		// The player getting the tokens, if they are online:
    		if ( player.isOnline() && !player.getName().equalsIgnoreCase( sender.getName() ) ) {
    			
    			player.sendMessage( message );
    		}
    	}
    }
    
    @Command(identifier = "prison tokens set", 
    		description = "Prison tokens Admin: an admins tool to set the number of tokens " +
    				"for a player to a specific amount. " +
    				"It is possible to set the tokens to a negative amount, which can " +
    				"be treated like a debt.  The " + 
    				"tokens added by admin will not count as earned tokens, so they cannot be used in " + 
    				"situations where the player must earn them. This restriction can be forced to " + 
    				"be attributed to being earned by the player.", 
    				permissions = "tokens.admin.add" )
    public void tokensSet( CommandSender sender,
    		@Arg(name = "player", 
    		description = "Player to adjust their tokens balance.") String playerName,
    		
    		@Arg(name = "amount", 
    		description = "The number of tokens to set the player's account to. " +
    				"This amount can be negative. ") long amount,

    		@Wildcard(join=true)
			@Arg(name = "options", description = "Optional settings: [silent forcePlayer]  Silent suppresses " +
					"all messages related to the transaction, including failures. The option 'forcePlayer' " +
					"will not use the admin logging of this transaction, but instead will assign it to the " +
					"player as if they actually earned the tokens through normal ways. It is not advised to " +
					"use 'forcePlayer' since it could be used to cheat the system in some ways.",
					def = "") String options 
			) {
    	
    	boolean silent = options != null && options.toLowerCase().contains( "silent" );
    	boolean forcePlayer = options != null && options.toLowerCase().contains( "forceplayer" );
    	
    	if ( playerName == null || playerName.isEmpty() ) {
    		
    		if ( !silent ) {
    			coreTokensNameRequiredMsg(sender);
//    			String message = "Prison Tokens: A player's name is required.";
//    			Output.get().logWarn( message );
    		}
    		
    		return;
    	}
    	
//    	DecimalFormat dFmt = Prison.get().getDecimalFormatInt();
    	
    	Player player = getPlayer( playerName );
    	
    	
    	if ( player == null ) {
    		if ( !silent ) {
    			sender.sendMessage(
    					String.format(
    							"Prison Tokens set: Player name not found. [%s] (hardCodedMessag)", 
    							playerName ));
    		}
    		return;
    	}

//    	// Set to zero:
//    	long totalTokens = player.getPlayerCachePlayerData().getTokens();
//    	player.getPlayerCachePlayerData().removeTokensAdmin( totalTokens );
    	
    	if ( forcePlayer ) {
    		
    		player.getPlayerCachePlayerData().setTokens( amount );
    	}
    	else {
    		
    		player.getPlayerCachePlayerData().setTokensAdmin( amount );
    	}
    	
    	if ( !silent ) {
    		
    		String message = coreTokensSetAmountMsg( player.getName(), 
    						player.getPlayerCachePlayerData().getTokens() );
    		
//    		String tokens = dFmt.format( player.getPlayerCachePlayerData().getTokens() );
//    		
//    		String message = String.format( "&3%s now has &7%s &3tokens.", 
//    				player.getName(), tokens );
    		
    		// The person adding the tokens, or console:
    		sender.sendMessage( message );
    		
    		// The player getting the tokens, if they are online:
    		if ( player.isOnline() && !player.getName().equalsIgnoreCase( sender.getName() ) ) {
    			
    			player.sendMessage( message );
    		}
    	}
    }
    
    

    /**
     * <p>This function tries to first get the online player, otherwise it 
     * gets an offline player.  Hopefully it always returns a player if the
     * have been on the server before.
     * </p>
     * 
     * @param playerName
     * @return
     */
	private Player getPlayer( String playerName ) {
		Player player = null;
		if ( playerName != null && !playerName.trim().isEmpty() ) {
			
			player = Prison.get().getPlatform().getPlayer( playerName ).orElse( null );
		}
		if ( player == null ) {
			
			player = Prison.get().getPlatform().getOfflinePlayer( playerName ).orElse( null );
		}
		return player;
	}
	
    
// This functionality should not be available in v3.2.1!  If someone is still running Prison 2.x.x 
//							    then they must first upgrade to
// prison v3.0.0 and perform the upgrade, at the most recent, then v3.1.1.
//    @Command(identifier = "prison convert", description = "Convert your Prison 2 data to Prison 3 data.", 
//						    onlyPlayers = false, permissions = "prison.convert")
//    public void convertCommand(CommandSender sender) {
//        sender.sendMessage(Prison.get().getPlatform().runConverter());
//    }

	public List<String> getRegisteredPlugins() {
		return registeredPlugins;
	}

	public TreeMap<String, RegisteredPluginsData> getRegisteredPluginData() {
		return registeredPluginData;
	}

	public RegisteredPluginsData addRegisteredPlugin( String pluginName, String pluginVersion ) {
		RegisteredPluginsData rpd = new RegisteredPluginsData( pluginName, pluginVersion, true );
		getRegisteredPluginData().put( pluginName, rpd);
		return rpd;
	}
	
	public RegisteredPluginsData addUnregisteredPlugin( String pluginName, String pluginVersion ) {
		RegisteredPluginsData rpd = new RegisteredPluginsData( pluginName, pluginVersion, false );
		if ( !getRegisteredPluginData().containsKey( pluginName ) ) {
			getRegisteredPluginData().put( pluginName, rpd);
		}
		return rpd;
	}
	
	public void addPluginDetails( String pluginName, String pluginVersion, 
											String command, List<String> commandAliases ) {
		
		// just in case this plugin was not registered before:
		addUnregisteredPlugin( pluginName, pluginVersion );
		
		RegisteredPluginsData plugin = getRegisteredPluginData().get( pluginName );
		
		plugin.addCommand( command, commandAliases );
	}

	public List<String> getPrisonStartupDetails() {
		return prisonStartupDetails;
	}
	public void setPrisonStartupDetails( List<String> prisonStartupDetails ) {
		this.prisonStartupDetails = prisonStartupDetails;
	}

	public String getSupportName() {
		return supportName;
	}
	public void setSupportName( String supportName ) {
		this.supportName = supportName;
	}

	public PrisonSupportFiles getSupportFile() {
		return supportFile;
	}
	public void setSupportFile(PrisonSupportFiles supportFile) {
		this.supportFile = supportFile;
	}

	public TreeMap<String, String> getSupportURLs() {
		return supportURLs;
	}
	public void setSupportURLs( TreeMap<String, String> supportURLs ) {
		this.supportURLs = supportURLs;
	}

}
