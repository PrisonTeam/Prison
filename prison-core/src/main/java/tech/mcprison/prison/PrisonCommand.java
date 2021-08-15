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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.TreeMap;
import java.util.UUID;

import tech.mcprison.prison.autofeatures.AutoFeaturesFileConfig.AutoFeatures;
import tech.mcprison.prison.autofeatures.AutoFeaturesWrapper;
import tech.mcprison.prison.commands.Arg;
import tech.mcprison.prison.commands.Command;
import tech.mcprison.prison.commands.CommandPagedData;
import tech.mcprison.prison.commands.Wildcard;
import tech.mcprison.prison.discord.PrisonPasteChat;
import tech.mcprison.prison.integration.IntegrationManager;
import tech.mcprison.prison.integration.IntegrationType;
import tech.mcprison.prison.internal.CommandSender;
import tech.mcprison.prison.internal.Player;
import tech.mcprison.prison.localization.LocaleManager;
import tech.mcprison.prison.modules.Module;
import tech.mcprison.prison.modules.ModuleStatus;
import tech.mcprison.prison.output.BulletedListComponent;
import tech.mcprison.prison.output.ChatDisplay;
import tech.mcprison.prison.output.DisplayComponent;
import tech.mcprison.prison.output.Output;
import tech.mcprison.prison.output.Output.DebugTarget;
import tech.mcprison.prison.troubleshoot.TroubleshootResult;
import tech.mcprison.prison.troubleshoot.Troubleshooter;
import tech.mcprison.prison.util.JumboTextFont;
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
    	
    	ChatDisplay display = displayVersion(options);
    	
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
    
    
    	
    public ChatDisplay displayVersion(String options) {
    
    	boolean isBasic = options == null || "basic".equalsIgnoreCase( options );
    	
        ChatDisplay display = new ChatDisplay("/prison version");
        display.addText("&7Prison Version: %s", Prison.get().getPlatform().getPluginVersion());

        display.addText("&7Running on Platform: %s", Prison.get().getPlatform().getClass().getName());
        display.addText("&7Minecraft Version: %s", Prison.get().getMinecraftVersion());

        
        // System stats:
        display.addText("");
        
        Prison.get().displaySystemSettings( display );
        
        Prison.get().displaySystemTPS( display );

        
        display.addText("");
        
        display.addText("&7Prison's root Command: /prison");
        
        for ( Module module : Prison.get().getModuleManager().getModules() ) {
        	
        	display.addText( "&7Module: %s : %s %s", module.getName(), 
        			module.getStatus().getStatusText(),
        			(module.getStatus().getStatus() == ModuleStatus.Status.FAILED ? 
        						"[" + module.getStatus().getMessage() + "]" : "")
        			);
        	// display.addText( ".   &7Base Commands: %s", module.getBaseCommands() );
        }
        
        List<String> disabledModules = Prison.get().getModuleManager().getDisabledModules();
        if ( disabledModules.size() > 0 ) {
        	display.addText( "&7Disabled Module%s:", (disabledModules.size() > 1 ? "s" : ""));
        	for ( String disabledModule : Prison.get().getModuleManager().getDisabledModules() ) {
        		display.addText( ".   &cDisabled Module:&7 %s. Related commands and placeholders are non-functional. ",
        				disabledModule );
        	}
        }

        
        List<String> features = Prison.get().getPlatform().getActiveFeatures();
        if ( features.size() > 0 ) {
        	
        	display.addText("");
        	for ( String feature : features ) {
        		
        		if ( !feature.startsWith( "+" ) ) {
        			
        			display.addText( feature );
        		}
        		else if ( !isBasic ) {
        			
        			display.addText( feature.substring( 1 ) );
        		}
        	}
        }
        
        
        display.addText("");
        display.addText("&7Integrations:");

        IntegrationManager im = Prison.get().getIntegrationManager();
        String permissions =
        		(im.hasForType(IntegrationType.PERMISSION) ?
                " " + im.getForType(IntegrationType.PERMISSION).get().getDisplayName() :
                "None");

        display.addText(". . &7Permissions: " + permissions);

        String economy =
        		(im.hasForType(IntegrationType.ECONOMY) ?
                " " + im.getForType(IntegrationType.ECONOMY).get().getDisplayName() : 
                "None");

        display.addText(". . &7Economy: " + economy);
        
        
        List<DisplayComponent> integrationRows = im.getIntegrationComponents( isBasic );
        for ( DisplayComponent component : integrationRows )
		{
        	display.addComponent( component );
		}
        
        Prison.get().getPlatform().identifyRegisteredPlugins();
        
        // NOTE: This list of plugins is good enough and the detailed does not have all the info.
        // Display all loaded plugins:
        if ( getRegisteredPlugins().size() > 0 ) {
        	display.addText("");
        	display.addText( "&7Registered Plugins: " );
        	StringBuilder sb = new StringBuilder();
        	for ( String plugin : getRegisteredPlugins() ) {
        		if ( sb.length() == 0) {
        			sb.append( ". " );
        			sb.append( plugin );
        		} else {
        			sb.append( ",  " );
        			sb.append( plugin );
        			display.addText( sb.toString() );
        			sb.setLength( 0 );
        		}
        	}
        	if ( sb.length() > 0 ) {
        		display.addText( sb.toString());
        	}
        }
        
        // This version of plugins does not have all the registered commands:
//        // The new plugin listings:
//        if ( getRegisteredPluginData().size() > 0 ) {
//        	display.text( "&7Registered Plugins Detailed: " );
//        	StringBuilder sb = new StringBuilder();
//        	Set<String> keys = getRegisteredPluginData().keySet();
//        	
//        	for ( String key : keys ) {
//        		RegisteredPluginsData plugin = getRegisteredPluginData().get(key);
//        		
//        		if ( sb.length() == 0) {
//        			sb.append( "  " );
//        			sb.append( plugin.formatted() );
//        		} else {
//        			sb.append( ",  " );
//        			sb.append( plugin.formatted() );
//        			display.text( sb.toString() );
//        			sb.setLength( 0 );
//        		}
//        	}
//        	if ( sb.length() > 0 ) {
//        		display.text( sb.toString());
//        	}
//        }
        
        
//        RegisteredPluginsData plugin = getRegisteredPluginData().get( "Prison" );
//        String pluginDetails = plugin.getdetails();
//        
//        display.text( pluginDetails );
        

//        if ( !isBasic ) {
//        	Prison.get().getPlatform().dumpEventListenersBlockBreakEvents();
//        }
        
        
        Prison.get().getPlatform().getWorldLoadErrors( display );

        if ( !isBasic && getPrisonStartupDetails().size() > 0 ) {
        	display.addText("");
        	
        	for ( String msg : getPrisonStartupDetails() ) {
				display.addText( msg );
			}
        }
        
        return display;
    }


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
    		description = "Converts any Prison placeholders in the test string to their values", 
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
    	

    	ChatDisplay display = new ChatDisplay("Placeholder Test");
    	
        BulletedListComponent.BulletedListBuilder builder =
                new BulletedListComponent.BulletedListBuilder();
        
        
    	UUID playerUuid = player == null ? null : player.getUUID();
    	playerName = player != null ? player.getName() :
    			(playerName.isEmpty() ? sender.getName() : playerName);
    	
    	String translated = Prison.get().getPlatform().getPlaceholders()
    					.placeholderTranslateText( playerUuid, playerName, text );
    	
    	builder.add( String.format( "&a    Include one or more Prison placeholders with other text..."));
    	builder.add( String.format( "&a    Use { } to escape the placeholders."));
    	
    	// Show player info here like with the search:
        if ( player != null ) {
        	builder.add( String.format( "&a    Player: &7%s  &aPlayerUuid: &7%s", player.getName(), 
        			(playerUuid == null ? "null" : playerUuid.toString())));
        	
        }
        
    	
    	builder.add( String.format( "&7  Original:   \\Q%s\\E", text));
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
        
        
        DecimalFormat dFmt = new DecimalFormat("#,##0");
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
    	display.addText( "&a    Chat based placeholders use { }, but others may use other escape codes like %% %%.");
    	display.addText( "&a    Mine based placeholders uses the mine name to replace 'minename'.");
    	
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
    	    	
    	AutoFeaturesWrapper.getInstance().getAutoFeaturesConfig().reloadConfig();
    	
    	String message = "&7AutoFeatures were reloaded. The new settings are now in effect. ";
    	sender.sendMessage( message );

    	try {
    		String filePath = AutoFeaturesWrapper.getInstance().getAutoFeaturesConfig()
    								.getConfigFile().getCanonicalPath();
    		sender.sendMessage( filePath );
    	}
    	catch ( IOException e ) {
    		// Ingore
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
    	
    	
    	
    	
    	display.addText( "&b " );
    	display.addText( "&b    options.blockBreakEvents.isProcessTokensEnchantExplosiveEvents:  %s", 
    			afw.isBoolean( AutoFeatures.isProcessTokensEnchantExplosiveEvents ) );
    	display.addText( "&b    options.blockBreakEvents.TokenEnchantBlockExplodeEventPriority:  %s", 
    			afw.getMessage( AutoFeatures.isProcessTokensEnchantExplosiveEvents ) );
    	
    	display.addText( "&b    options.blockBreakEvents.isProcessCrazyEnchantsBlockExplodeEvents:  %s", 
    			afw.isBoolean( AutoFeatures.isProcessCrazyEnchantsBlockExplodeEvents ) );
    	display.addText( "&b    options.blockBreakEvents.CrazyEnchantsBlastUseEventPriority:  %s", 
    			afw.getMessage( AutoFeatures.CrazyEnchantsBlastUseEventPriority ) );
    	
    	display.addText( "&b    options.blockBreakEvents.isProcessZenchantsBlockExplodeEvents:  %s", 
    			afw.isBoolean( AutoFeatures.isProcessZenchantsBlockExplodeEvents ) );
    	display.addText( "&b    options.blockBreakEvents.ZenchantmentsBlockShredEventPriority:  %s", 
    			afw.getMessage( AutoFeatures.ZenchantmentsBlockShredEventPriority ) );
    	
    	display.addText( "&b    options.blockBreakEvents.isProcessPrisonEnchantsExplosiveEvents:  %s", 
    			afw.isBoolean( AutoFeatures.isProcessPrisonEnchantsExplosiveEvents ) );
    	display.addText( "&b    options.blockBreakEvents.PrisonEnchantsExplosiveEventPriority:  %s", 
    			afw.getMessage( AutoFeatures.PrisonEnchantsExplosiveEventPriority ) );
    	
    	
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
    	display.addText( "&b   options.general.fortuneMultiplierMax %s", 
    									afw.getMessage( AutoFeatures.fortuneMultiplierMax ));

    	display.addText( "&b " );
    	display.addText( "&b   options.isProcessTokensEnchantExplosiveEvents %s", 
    									afw.isBoolean( AutoFeatures.isProcessTokensEnchantExplosiveEvents ));
    	display.addText( "&b   options.isProcessCrazyEnchantsBlockExplodeEvents %s", 
    									afw.isBoolean( AutoFeatures.isProcessCrazyEnchantsBlockExplodeEvents ));
    	display.addText( "&b   options.isProcessMcMMOBlockBreakEvents %s", 
    									afw.isBoolean( AutoFeatures.isProcessMcMMOBlockBreakEvents ));
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
    	
    	
    	
    	display.send(sender);
    	
    	// altPermissions are now a part of this command.
//    	// After displaying the help information above, rerun the same command for the player
//    	// with the help keyword to show the permissions.
//    	String formatted = "prison autofeatures help";
//		Prison.get().getPlatform().dispatchCommand(sender, formatted);
        
    }
    
    
    @Command(identifier = "prison debug", 
    		description = "Enables debugging and trouble shooting information. " +
    				"For internal use only. Do not use unless instructed.", 
    		onlyPlayers = false, permissions = "prison.debug" )
    public void toggleDebug(CommandSender sender,
    		@Wildcard(join=true)
    		@Arg(name = "targets", def = " ",
    				description = "Optional. Enable or disable a debugging target. " +
    					"[on, off, targets, jarScan, " +
    					"testPlayerUtil, testLocale, rankup] " +
    				"Use 'targets' to list all available targets.  Use 'on' or 'off' to toggle " +
    				"on and off individual targets, or all targets if no target is specified.  " +
    				"jarScan will identify what Java version compiled the class files within the listed jars"
    						) String targets ) {
    	
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
    	
    	
    	Output.get().applyDebugTargets( targets );
    	
    	String message = "Global Debug Logging is " + (Output.get().isDebug() ? "enabled" : "disabled");
    	sender.sendMessage( message );
    	
    	Set<DebugTarget> activeDebugTargets = Output.get().getActiveDebugTargets();
    	
    	if ( activeDebugTargets.size() > 0 ) {
    		message = ". Active Debug Targets:";
    		sender.sendMessage( message );
    		
    		for ( DebugTarget target : activeDebugTargets )
			{
    			message = String.format( ". . Target: %s", target.name() );
    			sender.sendMessage( message );
			}
    	}
    	
    	String validTargets = Output.get().getDebugTargetsString();
    	message = String.format( ". Valid Targets: %s", validTargets );
    	sender.sendMessage( message );

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
    		Output.get().logInfo( "A value for supportName is required." );
    		return;
    	}

    	setSupportName( supportName );
    	
    	Output.get().logInfo( "The support name has been set to: %s", getSupportName() );
    	Output.get().logInfo( "You can now use the support submit options." );

    }
    
    
    
    @Command(identifier = "prison support submit version", 
    		description = "For Prison support: This will copy the contents of '/prison version all' " +
    				"to paste.helpch.at so it can be easily shared with Prison's support staff .", 
    				onlyPlayers = false, permissions = "prison.debug" )
    public void supportSubmitVersion(CommandSender sender
    		) {
    	
    	
    	if ( getSupportName() == null || getSupportName().trim().isEmpty() ) {
    		Output.get().logInfo( "The support name needs to be set prior to using this command." );
    		Output.get().logInfo( "Use &7/prison support setSupportName help" );
    		
    		return;
    	}
    	
    	
    	ChatDisplay display = displayVersion("ALL");
		StringBuilder text = display.toStringBuilder();
		
		PrisonPasteChat pasteChat = new PrisonPasteChat( getSupportName(), getSupportURLs() );
		
		String helpURL = pasteChat.post( text.toString() );
		
		getSupportURLs().put( "Submit version:", helpURL );
		
		if ( helpURL != null ) {
			
			Output.get().logInfo( "Prison's support information has been pasted. Copy and " +
					"paste this URL in to Prison's Discord server." );
			Output.get().logInfo( "Paste this URL: %s", helpURL );
		}
		else {
    		Output.get().logInfo( "There was an error trying to generate the paste.helpch.at URL." );
		}
		
    	
    }
    
    @Command(identifier = "prison support submit configs", 
    		description = "For Prison support: This will copy the contents of Prison's config " +
    				"file to paste.helpch.at so it can be easily shared with Prison's " +
    				"support staff.  This will include the following: config.yml plugin.yml " +
    				"autoFeaturesConfig.yml modules.yml module_conf/mines/config.json " +
    				"SellAllConfig.yml GuiConfig.yml backpacks/backpacksconfig.yml", 
    				onlyPlayers = false, permissions = "prison.debug" )
    public void supportSubmitConfigs(CommandSender sender
    		) {
    	
    	
    	if ( getSupportName() == null || getSupportName().trim().isEmpty() ) {
    		Output.get().logInfo( "The support name needs to be set prior to using this command." );
    		Output.get().logInfo( "Use &7/prison support setSupportName help" );
    		
    		return;
    	}
    	
    	Prison.get().getPlatform().saveResource( "plugin.yml", true );
    	
    	String fileNames = "config.yml plugin.yml autoFeaturesConfig.yml modules.yml module_conf/mines/config.json " +
    			"SellAllConfig.yml GuiConfig.yml backpacks/backpacksconfig.yml";
    	List<File> files = convertNamesToFiles( fileNames );
    	
    	
    	StringBuilder text = new StringBuilder();

    	for ( File file : files ) {
			
    		addFileToText( file, text );
    		
    		if ( file.getName().equalsIgnoreCase( "plugin.yml" ) ) {
    			file.delete();
    		}
		}
    	

    	PrisonPasteChat pasteChat = new PrisonPasteChat( getSupportName(), getSupportURLs() );
    	
    	String helpURL = pasteChat.postKeepColorCodes( text.toString() );
    	
    	getSupportURLs().put( "Submit configs:", helpURL );
    	
    	if ( helpURL != null ) {
    		
    		Output.get().logInfo( "Prison's support information has been pasted. Copy and " +
    				"paste this URL in to Prison's Discord server." );
    		Output.get().logInfo( "Paste this URL: %s", helpURL );
    	}
    	else {
    		Output.get().logInfo( "There was an error trying to generate the paste.helpch.at URL." );
    	}
    	
    	
    }
    
    @Command(identifier = "prison support submit ranks", 
    		description = "For Prison support: This will copy the contents of Prison's " +
    				"ladders and ranks configs to paste.helpch.at so it can be " +
    				"easily shared with Prison's support staff.", 
    				onlyPlayers = false, permissions = "prison.debug" )
    public void supportSubmitRanks(CommandSender sender
    		) {
    	
    	
    	if ( getSupportName() == null || getSupportName().trim().isEmpty() ) {
    		Output.get().logInfo( "The support name needs to be set prior to using this command." );
    		Output.get().logInfo( "Use &7/prison support setSupportName help" );
    		
    		return;
    	}
    	
    	
    	List<File> files = listFiles( "data_storage/ranksDb/ladders/", ".json" );
    	files.addAll( listFiles( "data_storage/ranksDb/ranks/", ".json" ) );
    	
    	
    	StringBuilder text = new StringBuilder();
    	
    	
    	text.append( Prison.get().getPlatform().getRanksListString() );
    	printFooter( text );
 
    	
    	for ( File file : files ) {
    		
    		addFileToText( file, text );
    		
    	}
    	
    	
    	PrisonPasteChat pasteChat = new PrisonPasteChat( getSupportName(), getSupportURLs() );
    	
    	String helpURL = pasteChat.post( text.toString() );
    	
    	getSupportURLs().put( "Submit ranks:", helpURL );
    	
    	if ( helpURL != null ) {
    		
    		Output.get().logInfo( "Prison's support information has been pasted. Copy and " +
    				"paste this URL in to Prison's Discord server." );
    		Output.get().logInfo( "Paste this URL: %s", helpURL );
    	}
    	else {
    		Output.get().logInfo( "There was an error trying to generate the paste.helpch.at URL." );
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
    		Output.get().logInfo( "The support name needs to be set prior to using this command." );
    		Output.get().logInfo( "Use &7/prison support setSupportName help" );
    		
    		return;
    	}
    	
    	
    	List<File> files = listFiles( "data_storage/mines/mines/", ".json" );
    	
    	
    	StringBuilder text = new StringBuilder();
    	
    	text.append( "\n" );
    	text.append( "Table of contents:\n" );
    	text.append( "  1. Mine list - All mines including virtual mines: /mines list all\n" );
    	text.append( "  2. Mine info - All mines: /mines info <mineName> all\n" );
    	text.append( "  3. Mine files - Raw JSON dump of all mine configuration files.\n" );
    	text.append( "\n" );
    	
    	// Display a list of all mines, then display the /mines info <mineName> all for each:
    	text.append( Prison.get().getPlatform().getMinesListString() );
    	printFooter( text );
    	
    	
    	
    	for ( File file : files ) {
    		
    		addFileToText( file, text );
    		
    	}
    	
    	
    	PrisonPasteChat pasteChat = new PrisonPasteChat( getSupportName(), getSupportURLs() );
    	
    	String helpURL = pasteChat.post( text.toString() );
    	
    	getSupportURLs().put( "Submit mines:", helpURL );
    	
    	if ( helpURL != null ) {
    		
    		Output.get().logInfo( "Prison's support information has been pasted. Copy and " +
    				"paste this URL in to Prison's Discord server." );
    		Output.get().logInfo( "Paste this URL: %s", helpURL );
    	}
    	else {
    		Output.get().logInfo( "There was an error trying to generate the paste.helpch.at URL." );
    	}
    	
    	
    }
    
    
    private List<File> listFiles( String path, String fileSuffix ) {
    	List<File> files = new ArrayList<>();
		
		File dataFolder = Prison.get().getDataFolder();
		File filePaths = new File( dataFolder, path );
		
		for ( File file : filePaths.listFiles() ) {
			if ( file.getName().toLowerCase().endsWith( fileSuffix.toLowerCase() )) {
				files.add( file );
			}
		}
		
		return files;
	}

	private void addFileToText( File file, StringBuilder sb )
	{
    	DecimalFormat dFmt = new DecimalFormat("#,##0");
		SimpleDateFormat sdFmt = new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss" );
    	
    	sb.append( "\n" );

    	JumboTextFont.makeJumboFontText( file.getName(), sb );
    	
    	sb.append( "\n" );
    	
		sb.append( "File Name:   " ).append( file.getName() ).append( "\n" );
		sb.append( "File Path:   " ).append( file.getAbsolutePath() ).append( "\n" );
		sb.append( "File Size:   " ).append( dFmt.format( file.length() ) ).append( " bytes\n" );
		sb.append( "File Date:   " ).append( sdFmt.format( new Date(file.lastModified()) ) ).append( " bytes\n" );
		sb.append( "File Stats:  " )
					.append( file.exists() ? "EXISTS " : "" )
					.append( file.canRead() ? "READABLE " : "" )
					.append( file.canWrite() ? "WRITEABLE " : "" )
					.append( "\n" );
		
		sb.append( "\n" );
		sb.append( "=== ---  ---   ---   ---   ---   ---   ---   ---  --- ===\n" );
		sb.append( "\n" );

		
		if ( file.exists() && file.canRead() ) {
			readFileToStringBulider( file, sb );
		}
		else {
			sb.append( "Warning: The file is not readable so it cannot be included.\n" );
		}
		
		
		printFooter( sb );
	}
	
	public static void printFooter( StringBuilder sb ) {
		
		sb.append( "\n\n\n" );
		sb.append( "===  ---  ===   ---   ===   ---   ===   ---   ===  ---  ===\n" );
		sb.append( "=== # # ### # # # ### # # # ### # # # ### # # # ### # # ===\n" );
		sb.append( "===  ---  ===   ---   ===   ---   ===   ---   ===  ---  ===\n" );
		sb.append( "\n\n" );
		
	}

	private List<File> convertNamesToFiles( String fileNames )
	{
		List<File> files = new ArrayList<>();
		
		File dataFolder = Prison.get().getDataFolder();
		
		for ( String fileName : fileNames.split( " " )) {
			File file = new File( dataFolder, fileName );
			files.add( file );
		}
		
		return files;
	}

	@Command(identifier = "prison support submit latestLog", 
    		description = "For Prison support: This will copy the contents of `logs/latest.log` " +
    				"to paste.helpch.at so it can be easily shared with Prison's support staff .", 
    				onlyPlayers = false, permissions = "prison.debug" )
    public void supportSubmitLatestLog(CommandSender sender
    		) {
    	
    	
    	if ( getSupportName() == null || getSupportName().trim().isEmpty() ) {
    		Output.get().logInfo( "The support name needs to be set prior to using this command." );
    		Output.get().logInfo( "Use &7/prison support setSupportName help" );
    		
    		return;
    	}
    	
    	
    	File latestLogFile = new File( Prison.get().getDataFolder().getParentFile().getParentFile(), 
    									"logs/latest.log");
    	
    	Output.get().logInfo( "### log path: " + latestLogFile.getAbsolutePath() );
    	
    	
    	StringBuilder logText = new StringBuilder();
    	
    	if ( latestLogFile.exists() && latestLogFile.canRead() ) {
    		
    		readFileToStringBulider( latestLogFile, logText );
    		
    		if ( logText != null ) {
    			
    			PrisonPasteChat pasteChat = new PrisonPasteChat( getSupportName(), getSupportURLs() );
    			
    			String helpURL = pasteChat.post( logText.toString() );
    			
    			getSupportURLs().put( "Submit lastlog:", helpURL );
    			
    			if ( helpURL != null ) {
    				
    				Output.get().logInfo( "Prison's support information has been pasted. Copy and " +
    						"paste this URL in to Prison's Discord server." );
    				Output.get().logInfo( "Paste this URL: %s", helpURL );
    			}
    			else {
    				// Do nothing since if helpURL is null, then it has probably
    				// already sent an error message.
    			}
    			return;
    		}
    	}
    	
    	Output.get().logInfo( "Unable to send log file.  Unknown reason why." );
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
    

	@Command(identifier = "prison support listeners", 
    		description = "For Prison support: Provide a 'dump' of all event listeners.", 
    				onlyPlayers = false, permissions = "prison.debug" )
    public void supportListenersDump(CommandSender sender,
    		@Arg(name = "listener", def = " ",
			description = "Provides a detailed list of all registered event listeners for" +
					"the various event types.  BlockBreak listeners will include all " +
					"listeners that are being monitored within auto features. " +
				"[blockBreak, traceBlockBreak, chat]"
					) String listener
    		) {
		
		if ( listener == null ) {
			String message = "You must supply a listener in order to use this command.";
			sender.sendMessage( message );
			return;
		}
		
		String results = null;
		
    	if ( "blockBreak".equalsIgnoreCase( listener ) ) {
    		
    		results = Prison.get().getPlatform().dumpEventListenersBlockBreakEvents();
    	}
    	
    	if ( "chat".equalsIgnoreCase( listener ) ) {
    		
    		results = Prison.get().getPlatform().dumpEventListenersPlayerChatEvents();
    	}
    	
    	if ( "traceBlockBreak".equalsIgnoreCase( listener ) ) {
    		
    		Prison.get().getPlatform().traceEventListenersBlockBreakEvents( sender );
    		
    		return;
    	}
    	
		if ( results != null ) {

			for ( String line : results.split( "\n" ) ) {
				
				Output.get().logInfo( line );
			}
		}

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

	public TreeMap<String, String> getSupportURLs() {
		return supportURLs;
	}
	public void setSupportURLs( TreeMap<String, String> supportURLs ) {
		this.supportURLs = supportURLs;
	}

}
