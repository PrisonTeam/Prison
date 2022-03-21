package tech.mcprison.prison.ranks.commands;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import tech.mcprison.prison.Prison;
import tech.mcprison.prison.PrisonAPI;
import tech.mcprison.prison.PrisonCommand;
import tech.mcprison.prison.autofeatures.AutoFeaturesFileConfig.AutoFeatures;
import tech.mcprison.prison.autofeatures.AutoFeaturesWrapper;
import tech.mcprison.prison.cache.PlayerCache;
import tech.mcprison.prison.cache.PlayerCachePlayerData;
import tech.mcprison.prison.chat.FancyMessage;
import tech.mcprison.prison.commands.Arg;
import tech.mcprison.prison.commands.Command;
import tech.mcprison.prison.commands.Wildcard;
import tech.mcprison.prison.integration.EconomyCurrencyIntegration;
import tech.mcprison.prison.integration.Integration;
import tech.mcprison.prison.integration.IntegrationType;
import tech.mcprison.prison.integration.PermissionIntegration;
import tech.mcprison.prison.internal.CommandSender;
import tech.mcprison.prison.internal.OfflineMcPlayer;
import tech.mcprison.prison.internal.Player;
import tech.mcprison.prison.modules.ModuleElement;
import tech.mcprison.prison.modules.ModuleElementType;
import tech.mcprison.prison.output.BulletedListComponent;
import tech.mcprison.prison.output.ChatDisplay;
import tech.mcprison.prison.output.FancyMessageComponent;
import tech.mcprison.prison.output.LogLevel;
import tech.mcprison.prison.output.Output;
import tech.mcprison.prison.output.RowComponent;
import tech.mcprison.prison.placeholders.PlaceholdersUtil;
import tech.mcprison.prison.ranks.PrisonRanks;
import tech.mcprison.prison.ranks.data.PlayerRank;
import tech.mcprison.prison.ranks.data.PlayerRankRefreshTask;
import tech.mcprison.prison.ranks.data.Rank;
import tech.mcprison.prison.ranks.data.RankLadder;
import tech.mcprison.prison.ranks.data.RankPlayer;
import tech.mcprison.prison.ranks.data.RankPlayerFactory;
import tech.mcprison.prison.ranks.data.RankPlayerName;
import tech.mcprison.prison.ranks.managers.LadderManager;
import tech.mcprison.prison.ranks.managers.PlayerManager;
import tech.mcprison.prison.ranks.managers.RankManager;
import tech.mcprison.prison.ranks.managers.RankManager.RanksByLadderOptions;
import tech.mcprison.prison.util.JumboTextFont;
import tech.mcprison.prison.util.Text;

/**
 * @author Faizaan A. Datoo
 */
public class RanksCommands
			extends RanksCommandsMessages {
	
	private CommandCommands rankCommandCommands = null;
	
	public RanksCommands() {
		super( "RanksCommands" );
	}
	
	public RanksCommands( CommandCommands rankCommandCommands ) {
		super( "RanksCommands" );
		
		this.rankCommandCommands = rankCommandCommands;
	}
	
    public CommandCommands getRankCommandCommands() {
		return rankCommandCommands;
	}
	public void setRankCommandCommands( CommandCommands rankCommandCommands ) {
		this.rankCommandCommands = rankCommandCommands;
	}

    @Command(identifier = "ranks command", 
    		onlyPlayers = false, permissions = "prison.commands")
    public void ranksCommandSubcommands(CommandSender sender) {
    	sender.dispatchCommand( "ranks command help" );
    }
    
    @Command(identifier = "ranks ladder", 
    		onlyPlayers = false, permissions = "prison.commands")
    public void ranksLadderSubcommands(CommandSender sender) {
    	sender.dispatchCommand( "ranks ladder help" );
    }
    
//    @Command(identifier = "ranks perms", 
//    		onlyPlayers = false, permissions = "prison.commands")
    public void ranksPermsSubcommands(CommandSender sender) {
    	sender.dispatchCommand( "ranks perms help" );
    }
    
//    @Command(identifier = "ranks remove", 
//    		onlyPlayers = false, permissions = "prison.commands")
//    public void ranksRemoveSubcommands(CommandSender sender) {
//    	sender.dispatchCommand( "ranks remove help" );
//    }
    
    @Command(identifier = "ranks set", 
    		onlyPlayers = false, permissions = "prison.commands")
    public void ranksSetSubcommands(CommandSender sender) {
    	sender.dispatchCommand( "ranks set help" );
    }

	@Command(identifier = "ranks create", description = "Creates a new rank", 
    							onlyPlayers = false, permissions = "ranks.create")
    public boolean createRank(CommandSender sender,
        @Arg(name = "rankName", description = "The name of this rank.") String name,
        @Arg(name = "cost", description = "The cost of this rank.") double cost,
        @Arg(name = "ladder", description = "The ladder to put this rank on.", def = "default")
            String ladder,
            
        @Arg(name = "tag", description = "The tag to use for this rank.", def = "none")
            		String tag,
            		
        @Wildcard(join=true)
		@Arg(name = "options", def = " ", 
		description = "Options for rank creation. " +
				"Use 'noPlaceholderUpdate' to prevent reloading all placeholders when " +
				"creating this rank. This is useful if you have multiple ranks " +
				"you want to create. " +
				"[noPlaceholderUpdate]") String options
    		) {
		
		// The tag may actually bleed over in to options, so combine both together with one space:
		if ( options != null && !options.trim().isEmpty() ) {
			tag += " " + options;
		}
		
    	boolean updatePlaceholders = !tag.toLowerCase().contains( "noplaceholderupdate" );
    	if ( !updatePlaceholders ) {
    		tag = tag.replaceAll( "(?i)noPlaceholderUpdate", "" ).trim();
    	}

		boolean success = false;

        // Ensure a rank with the name doesn't already exist
        if (PrisonRanks.getInstance().getRankManager().getRank(name) != null) {
        	rankAlreadyExistsMsg( sender, name );
            return success;
        }
        
        // Ensure a rank with the name doesn't already exist
        if (name == null || name.trim().length() == 0 || name.contains( "&" )) {
        	rankNameRequiredMsg( sender );
        	return success;
        }

        // Fetch the ladder first, so we can see if it exists

        RankLadder rankLadder = PrisonRanks.getInstance().getLadderManager().getLadder(ladder);
        
        if ( rankLadder == null ) {
        	ladderDoesNotExistMsg( sender, ladder );
            return success;
        }

        // Set a default tag if necessary
        if (tag.equals("none")) {
            tag = "[" + name + "]";
        }

        // Create the rank
        Optional<Rank> newRankOptional =
            PrisonRanks.getInstance().getRankManager().createRank(name, tag, cost);

        // Ensure it was created
        if (!newRankOptional.isPresent()) {
        	rankCannotBeCreatedMsg( sender );
            return success;
        }

        Rank newRank = newRankOptional.get();

        // Save the rank
//        try {
            PrisonRanks.getInstance().getRankManager().saveRank(newRank);
//        } catch (IOException e) {
//            Output.get().sendError(sender,
//                "The new rank could not be saved to disk. Check the console for details.");
//            Output.get().logError("Rank could not be written to disk.", e);
//        }

        // Add the ladder

        rankLadder.addRank(newRank);
        if ( PrisonRanks.getInstance().getLadderManager().save(rankLadder) ) {
            
            success = true;
            
            if ( updatePlaceholders ) {
            	Prison.get().getPlatform().getPlaceholders().reloadPlaceholders();
            }
            
            
            // Recalculate the ladder's base rank cost multiplier:
            PlayerRankRefreshTask rankRefreshTask = new PlayerRankRefreshTask();
            rankRefreshTask.submitAsyncTPSTask();

            
            // Tell the player the good news!
            rankCreatedSuccessfullyMsg( sender, name, ladder, tag );
            
        } 
        else {
        	errorCouldNotSaveLadderMsg( sender, rankLadder.getName() );
        }

        return success;
    }

	
	@Command(identifier = "ranks autoConfigure", description = "Auto configures Ranks and Mines using " +
			"single letters A through Z for both the rank and mine names. Both ranks and mines are " +
			"generated, they will also be linked together automatically. To set the starting price use " +
			"price=x. To set multiplier mult=x. AutoConfigure will try to merge any preexsiting ranks " +
			"and mines, but you must use the 'force' keyword in 'options'. Force will replace all blocks " +
			"in preexisting " +
			"mines. To keep preexisting blocks, use 'forceKeepBlocks' with the 'force' option. " +
			"Default values [full price=50000 mult=1.5]", 
			onlyPlayers = false, permissions = "ranks.set", 
			aliases = {"prison autoConfigure"} )
	public void autoConfigureRanks(CommandSender sender, 
			@Wildcard(join=true)
			@Arg(name = "options", 
				description = "Options: [full ranks mines price=x mult=x force forceKeepBlocks dontForceLinerWalls dontForceLinerBottoms]", 
				def = "full") String options
			) {
		
		// force options to lower case to work better with mixed case usage:
		options = options == null ? "" : options.toLowerCase().trim();
		
		boolean force = false;
		boolean forceLinersWalls = true;
		boolean forceLinersBottom = true;
		boolean forceKeepBlocks = false;
		
		
		if ( options.contains( "forcekeepblocks" ) ) {
			forceKeepBlocks = true;
			options = options.replace( "forcekeepblocks", "" ).trim();
		}
		
		if ( options.contains( "dontforcelinerwalls" ) ) {
			forceLinersWalls = false;
			options = options.replace( "dontforcelinerwalls", "" ).trim();
		}
		if ( options.contains( "dontforcelinerbottoms" ) ) {
			forceLinersBottom = false;
			options = options.replace( "dontforcelinerbottoms", "" ).trim();
		}
		
		if ( options.contains( "force" ) ) {
			force = true;
			options = options.replace( "force", "" ).trim();
		}
		
		int rankCount = PrisonRanks.getInstance().getRankManager().getRanks().size();
		int mineCount = Prison.get().getPlatform().getModuleElementCount( ModuleElementType.MINE );
		
		if (!force && ( rankCount > 0 || mineCount > 0 ) ) {
			
			autoConfigPreexistingRankMineWarningMsg( sender, rankCount, mineCount );
			return;
		}
		
		if ( force ) {
			autoConfigForceWarningMsg( sender );
		}
		
		String optionHelp = "&b[&7full ranks mines price=&dx &7mult=&dx &7force forceKeepBlocks dontForceLinerWalls dontForceLinerBottoms&b]";
		boolean ranks = false;
		boolean mines = false;
		double startingPrice = 50000;
		double percentMultipler = 1.5;
		
		options = (options == null || options.trim().isEmpty() ? 
							"full" : options.trim().replaceAll( "/s*", " " ));
		if ( options.trim().length() == 0 ) {
			Output.get().sendError(sender,
					"&3Invalid options.  Use %s&3.  Was: &3%s",
					optionHelp, options );
			return;
		}

		if ( options.contains( "full" ) ) {
			ranks = true;
			mines = true;
			options = options.replace( "full", "" ).trim();
		}
		if ( options.contains( "ranks" ) ) {
			ranks = true;
			options = options.replace( "ranks", "" ).trim();
		}
		if ( options.contains( "mines" ) ) {
			mines = true;
			options = options.replace( "mines", "" ).trim();
		}
		
		String priceStr = extractParameter("price=", options);
		if ( priceStr != null ) {
			options = options.replace( priceStr, "" );
			priceStr = priceStr.replace( "price=", "" ).trim();
			
			try {
				startingPrice = Double.parseDouble( priceStr );
			}
			catch ( NumberFormatException e ) {
				// Not a valid double number, or price:
			}
		}
		
		
		String multStr = extractParameter("mult=", options);
		if ( multStr != null ) {
			options = options.replace( multStr, "" );
			multStr = multStr.replace( "mult=", "" ).trim();
			
			try {
				percentMultipler = Double.parseDouble( multStr );
			}
			catch ( NumberFormatException e ) {
				// Not a valid double number, or price:
			}
		}
		
		
		// What's left over, if not just a blank string, must be an error:
		options = (options == null ? "" : options.replaceAll( "/s*", " " ));
		if ( options.trim().length() != 0 ) {
			autoConfigInvalidOptionsMsg( sender, optionHelp, options );
			return;
		}
		
//		TreeMap<String, RegisteredPluginsData> plugins = 
//								Prison.get().getPrisonCommands().getRegisteredPluginData();
		

//		String permCmdAdd = null;
//        String permCmdDel = null;
//        String perm1 = "mines.";
//        String perm2 = "mines.tp.";
        
//        if ( plugins.containsKey("LuckPerms") ){
//        	permCmdAdd = "lp user {player} permission set ";
//        	permCmdDel = "lp user {player} permission unset ";
//        } 
//        else if ( plugins.containsKey("PermissionsEx") ){
//        	permCmdAdd = "pex user {player} add ";
//        	permCmdDel = "pex user {player} add -";
//        } 
//        else if ( plugins.containsKey("UltraPermissions") ){
//        	permCmdAdd = "upc addplayerpermission {player} ";
//        	permCmdDel = "upc removeplayerpermission {player} ";
//        } 
//        else if ( plugins.containsKey("GroupManager") ){
//        	permCmdAdd = "manuaddp {player} ";
//        	permCmdDel = "manudelp {player} ";
//        } 
//        else if ( plugins.containsKey("zPermissions") ){
//        	permCmdAdd = "permissions player {player} set ";
//        	permCmdDel = "permissions player {player} unset ";
//        } 
//        else if ( plugins.containsKey("PowerfulPerms") ){
//        	permCmdAdd = "pp user {player} add ";
//        	permCmdAdd = "pp user {player} remove ";
//        }


		
		int countRanks = 0;
		int countRanksForced = 0;
		int countRankCmds = 0;
		int countMines = 0;
		int countMinesForced = 0;
		int countLinked = 0;
		
		List<String> rankMineNames = new ArrayList<>();
		
		if ( ranks ) {
			
	        int colorID = 1;
	        double price = 0;

	        String firstRankName = null;
	        
	        for ( char cRank = 'A'; cRank <= 'Z'; cRank++) {
	        	String rankName = Character.toString( cRank );
	        	
	        	rankMineNames.add( rankName );
	        	
	        	String tag = "&7[&" + Integer.toHexString((colorID++ % 15) + 1) + rankName + "&7]";
	        	
	        	if ( firstRankName == null ) {
	        		firstRankName = rankName;
	        	}
	        	
//	        	char cRankNext = (char) (cRank + 1);
//	        	String rankNameNext = Character.toString( cRankNext );
	        	
	        	boolean forceRank = force && PrisonRanks.getInstance().getRankManager().getRank( rankName ) != null;
	        	if ( forceRank ||
	        			createRank(sender, rankName, price, "default", tag, "noPlaceholderUpdate") ) {
	        		
	        		if ( forceRank ) {
	        			countRanksForced++;
	        		}
	        		else {
	        			countRanks++;
	        		}
	        		
	        		
//	        		if ( permCmdAdd != null ) {
//	        			getRankCommandCommands().commandAdd( sender, rankName, permCmdAdd + perm1 + rankName.toLowerCase());
//	        			countRankCmds++;
////	        			getRankCommandCommands().commandAdd( sender, rankName, permCmdAdd + perm2 + rankName.toLowerCase());
////	        			countRankCmds++;
//	        			
//	        			// Add all the command removal statements to rank A's commands so if the command /ranks set rank A is 
//	        			// used then all perms are removed
//	        			if ( !firstRankName.equalsIgnoreCase( rankName ) ) {
//	        				getRankCommandCommands().commandAdd( sender, firstRankName, permCmdDel + perm1 + rankName.toLowerCase());
//	        				countRankCmds++;
////	        				getRankCommandCommands().commandAdd( sender, firstRankName, permCmdDel + perm2 + rankName.toLowerCase());
////	        				countRankCmds++;
//	        			}
//	        			
//	        			if ( cRankNext <= 'Z' ) {
//	        				getRankCommandCommands().commandAdd( sender, rankName, permCmdDel + perm1 + rankNameNext.toLowerCase());
//	        				countRankCmds++;
////	        				getRankCommandCommands().commandAdd( sender, rankName, permCmdDel + perm2 + rankNameNext.toLowerCase());
////	        				countRankCmds++;
//	        			}
//	        			
//	        		}
	        		
	        		if ( mines ) {

	        			// Creates a virtual mine:
	        			String perm = null;
//	        			String perm = perm1 + rankName;
	        			
	        			
	        			
	        			ModuleElement mine = Prison.get().getPlatform().getModuleElement( ModuleElementType.MINE, rankName );
	        			
	        			boolean forceMine = force && mine != null;
	        			
	        			if ( mine == null ) {
	        				mine = Prison.get().getPlatform().createModuleElement( 
	        								sender, ModuleElementType.MINE, rankName, tag, perm );
	        			}
	        			
	        			
	        			if ( mine != null ) {
	        				if ( forceMine ) {
	        					countMinesForced++;
	        				}
	        				else {
	        					countMines++;
	        				}
	        				
	        				// Links the virtual mine to generated rank and configure mines:
	        				if ( Prison.get().getPlatform().linkModuleElements( mine, ModuleElementType.RANK, rankName ) ) {
	        					countLinked++;
	        				}
	        				
	        			}
	        		}
	        	}
	        	else {
	        		autoConfigRankExistsSkipMsg( sender, Character.toString( cRank ) );
	        	}

	            if (price == 0){
	                price += startingPrice;
	            } else {
	                price *= percentMultipler;
	            }

	        }
		}
		
		int prestigesCount = 0;
		
		// add in 10 prestiges at 1 billion each:
		double prestigeCost = 1000000000;
		
		for ( int i = 0; i < 10; i++ ) {
			String name = "P" + (i + 1);
			String tag = "&5[&d+" + (i > 0 ? i + 1 : "" ) + "&5]";
			createRank(sender, name, (prestigeCost * (i + 1) ), "prestiges", tag, "noPlaceholderUpdate");
			prestigesCount++;
		}
		
		// If mines were created, go ahead and auto assign blocks to the mines:
		if ( countMines > 0 || countMinesForced > 0 ) {
			Prison.get().getPlatform().autoCreateMineBlockAssignment( rankMineNames, forceKeepBlocks );
			Prison.get().getPlatform().autoCreateMineLinerAssignment( rankMineNames, forceLinersBottom, forceLinersWalls );
			
			Prison.get().getPlatform().autoCreateConfigureMines();
		}
		
		
		if ( countRanks == 0 ) {
			autoConfigNoRanksCreatedMsg( sender );
		}
		else {
			
			// Set the prestiges ladder with a 10% base rank cost multiplier
			double rankCostMultiplier = 0.10;
			
			RankLadder prestiges = PrisonRanks.getInstance().getLadderManager().getLadder( "prestiges" );
			prestiges.setRankCostMultiplierPerRank( rankCostMultiplier );
			PrisonRanks.getInstance().getLadderManager().save( prestiges );
			
			// Log that the rank cost multiplier has been applied to the ladder
			// with information on how to change it.
			autoConfigLadderRankCostMultiplierInfoMsg( sender, rankCostMultiplier );
			autoConfigLadderRankCostMultiplierCmdMsg( sender );
			
			
			autoConfigRanksCreatedMsg( sender, Integer.toString( countRanks ) );
			
			if ( countRankCmds == 0 ) {
				autoConfigNoRankCmdsCreatedMsg( sender );
			}
			else {
				autoConfigRankCmdsCreatedMsg( sender, Integer.toString( countRanks ) );
			}
		}

		
		// Reload the placeholders and autoFeatures:
		Prison.get().getPlatform().getPlaceholders().reloadPlaceholders();
		
		AutoFeaturesWrapper.getInstance().getAutoFeaturesConfig().reloadConfig();
		
		
		// Reset all player to the first rank on the default ladder:
		PrisonRanks.getInstance().checkAllPlayersForJoin();
		
//		RankLadder defaultLadder = PrisonRanks.getInstance().getLadderManager().getLadder( "default" );
//		Rank defaultRank = defaultLadder.getLowestRank().get();
//		PrisonRanks.getInstance().getRankManager().getRankupCommands()
//				.setRank( sender, "*all*", "*join*", defaultLadder.getName() );
		
		
		if ( countRanksForced > 0 ) {
			// message about number of ranks that preexisting and were force:
		}
		
		if ( countMines == 0 ) {
			autoConfigNoMinesCreatedMsg( sender );
		}
		else {
			autoConfigMinesCreatedMsg( sender, Integer.toString( countMines ) );
			
			if ( countLinked == 0 ) {
				autoConfigNoLinkageMsg( sender );
			}
			else {
				autoConfigLinkageCountMsg( sender, Integer.toString( countLinked ) );
			}
		}
		
		if ( countMinesForced > 0 ) {
			// message about number of mines that preexisting and were force:
		}

		if ( prestigesCount > 0 ) {
			sender.sendMessage( "Created " + prestigesCount + " prestige ranks (temp message)." );
		}
		
		Output.get().logInfo( "");
		
		
		
	}
	
	private String extractParameter( String key, String options ) {
		String results = null;
		int idx = options.indexOf( key );
		if ( idx != -1 ) {
			int idxEnd = options.indexOf( " ", idx );
			if ( idxEnd == -1 ) {
				idxEnd = options.length();
			}
			results = options.substring( idx, idxEnd );
		}
		return results;
	}
	
	
    @Command(identifier = "ranks delete", description = "Removes a rank, and deletes its files.", 
    								onlyPlayers = false, permissions = "ranks.delete")
    public void removeRank(CommandSender sender, 
    			@Arg(name = "rankName") String rankName) {
        // Check to ensure the rank exists
        Rank rank = PrisonRanks.getInstance().getRankManager().getRank(rankName);
        if ( rank == null ) {
        	rankDoesNotExistMsg( sender, rankName );
            return;
        }

        if (PrisonRanks.getInstance().getDefaultLadder().getRanks().contains( rank ) 
            && PrisonRanks.getInstance().getDefaultLadder().getRanks().size() == 1) {
        	rankCannotBeRemovedMsg( sender );
            return;
        }

        if ( PrisonRanks.getInstance().getRankManager().removeRank(rank) ) {

        	Prison.get().getPlatform().getPlaceholders().reloadPlaceholders();
        	
            // Recalculate the ladder's base rank cost multiplier:
            PlayerRankRefreshTask rankRefreshTask = new PlayerRankRefreshTask();
            rankRefreshTask.submitAsyncTPSTask();
            
            rankWasRemovedMsg( sender, rankName );
        } else {
        	rankDeleteErrorMsg( sender, rankName );
        }
    }

    @Command(identifier = "ranks list", description = "Lists all the ranks on the server by" +
    		"ladder.  If 'all' is used instead of a ladder name, then it will print all " +
    		"ranks.", 
    			onlyPlayers = false, altPermissions = "ranks.list"
    							)
    public void listRanks(CommandSender sender,
        @Arg(name = "ladderName", def = "default", 
        	description = "A ladder name, or 'all' to list all ranks by ladder.") String ladderName) {

    	boolean hasPerm = sender.hasPermission("ranks.list") ||
    					sender.isOp() || !sender.isPlayer();
    	
        RankLadder ladder =
        			PrisonRanks.getInstance().getLadderManager().getLadder(ladderName);

        if ( ladder == null && !"all".equalsIgnoreCase( ladderName ) ) {
        	ladderDoesNotExistMsg( sender, ladderName );
            return;
        }

        
        if ( ladder != null && ladder.getRanks().size() == 0 ) {
        	ladderHasNoRanksMsg( sender, ladderName );
        }

//        Rank rank = null;
//        for (Rank pRank : ladder.getPositionRanks()) {
//            Optional<Rank> rankOptional = ladder.getByPosition(pRank.getPosition());
//            if (rankOptional.isPresent()) {
//            	rank = rankOptional.get();
//            	break;
//            }
//        }
        
        RankPlayer rPlayer = 
        		PrisonRanks.getInstance().getPlayerManager().getPlayer( getPlayer(sender) );
        
        ChatDisplay display = null;
        
        if ( ladder != null ) {
        	display = listRanksOnLadder( ladder, hasPerm, rPlayer );
        }
        else {
        	display = new ChatDisplay( "List ALL Ranks" );
        	
        	listAllRanksByLadders( display, hasPerm, rPlayer );
        }
        

        
        if ( hasPerm ) {
        	
        	
        	List<String> others = new ArrayList<>();
        	for (RankLadder other : PrisonRanks.getInstance().getLadderManager().getLadders()) {
        		if (!other.getName().equals(ladderName) && (other.getName().equals("default") || sender
        				.hasPermission("ranks.rankup." + other.getName().toLowerCase()))) {
        			if (sender.hasPermission("ranks.admin")) {
        				others.add("/ranks list " + other.getName());
        			} else {
        				others.add("/ranks " + other.getName());
        			}
        		}
        	}
        	
        	if (others.size() != 0) {
        		FancyMessage msg = new FancyMessage( ranksListYouMayTryMsg() );
        		msg.then( " " );
        		int i = 0;
        		for (String other : others) {
        			i++;
        			if (i == others.size() && others.size() > 1) {
        				msg.then(" &8and ");
        			}
        			msg.then("&7" + other).tooltip( ranksListClickToView2Msg() ).command(other);
        			msg.then(i == others.size() ? "&8." : "&8, ");
        		}
        		display.addComponent(new FancyMessageComponent(msg));
        	}
        	
        }

        display.send(sender);

    }

	public void listAllRanksByLadders( ChatDisplay display, boolean hasPerm, RankPlayer rPlayer )
	{
//		List<RankLadder> ladders = PrisonRanks.getInstance().getLadderManager().getLadders();
		
//		for ( RankLadder rLadder : ladders ) {
//			ChatDisplay cDisp = listRanksOnLadder( rLadder, hasPerm );
//			
//			if ( display == null ) {
//				display = cDisp;
//			}
//			else {
//				display.addEmptyLine();
//				
//				display.addChatDisplay( cDisp );
//			}
//			
//		}
		
    	// Track which ranks were included in the ladders listed:
    	List<Rank> ranksIncluded = new ArrayList<>();
    	
    	for ( RankLadder ladder : PrisonRanks.getInstance().getLadderManager().getLadders() ) {
    		List<Rank> ladderRanks = ladder.getRanks();
    		ranksIncluded.addAll( ladderRanks );
    		
    		ChatDisplay cDisp = listRanksOnLadder( ladder, hasPerm, rPlayer );
			
			if ( display == null ) {
				display = cDisp;
			}
			else {
				display.addEmptyLine();
				
				display.addChatDisplay( cDisp );
			}
			
    	}
    	
    	// Next we need to get a list of all ranks that were not included. Create a temp ladder so they
    	// can be printed out with them:
    	List<Rank> ranksExcluded = new ArrayList<>( PrisonRanks.getInstance().getRankManager().getRanks() );
    	ranksExcluded.removeAll( ranksIncluded );
    	
    	if ( ranksExcluded.size() > 0 ) {
    		RankLadder noLadder = new RankLadder( -1, "No Ladder" );
    		
    		for ( Rank rank : ranksExcluded ) {
    			noLadder.addRank( rank );
			}
    		
    		ChatDisplay cDisp = listRanksOnLadder( noLadder, hasPerm, rPlayer );
			
			if ( display == null ) {
				display = cDisp;
			}
			else {
				display.addEmptyLine();
				
				display.addChatDisplay( cDisp );
			}
    	}
    	
	}
	
	public void listAllRanksByInfo( StringBuilder sb )
	{
		
		// Track which ranks were included in the ladders listed:
		List<Rank> ranksIncluded = new ArrayList<>();
		
		for ( RankLadder ladder : PrisonRanks.getInstance().getLadderManager().getLadders() ) {
			List<Rank> ladderRanks = ladder.getRanks();
			ranksIncluded.addAll( ladderRanks );
			
			for ( Rank rank : ladderRanks )
			{
				
				PrisonCommand.printFooter( sb );
				
				JumboTextFont.makeJumboFontText( rank.getName(), sb );
				sb.append( "\n" );
				
				ChatDisplay chatDisplay = rankInfoDetails( null, rank, "all" );
				
				sb.append( chatDisplay.toStringBuilder() );
			}
		}
		
		
		
		// Next we need to get a list of all ranks that were not included. Create a temp ladder so they
		// can be printed out with them:
		List<Rank> ranksExcluded = new ArrayList<>( PrisonRanks.getInstance().getRankManager().getRanks() );
		ranksExcluded.removeAll( ranksIncluded );
		
		if ( ranksExcluded.size() > 0 ) {
			
			for ( Rank rank : ranksExcluded )
			{
				
				PrisonCommand.printFooter( sb );
				
				JumboTextFont.makeJumboFontText( rank.getName(), sb );
				sb.append( "\n" );
				
				ChatDisplay chatDisplay = rankInfoDetails( null, rank, "all" );
				
				sb.append( chatDisplay.toStringBuilder() );
			}
			
		}
		
	}

	private ChatDisplay listRanksOnLadder( RankLadder ladder, boolean hasPerm, RankPlayer rPlayer )
	{
		String rankHeader = ranksListHeaderMsg( ladder.getName() );
        ChatDisplay display = new ChatDisplay( rankHeader );
        
        display.addText( ranksListLadderCostMultiplierMsg( 
        							ladder.getRankCostMultiplierPerRank() ) );
        
        if ( hasPerm ) {
        	display.addText( ranksListClickToEditMsg() );
        }
        
        
        if ( ladder.getRanks().size() == 0 ) {
        	display.addText( ladderHasNoRanksTextMsg() );
        }
        
        BulletedListComponent.BulletedListBuilder builder =
        		new BulletedListComponent.BulletedListBuilder();
        
        DecimalFormat fFmt = new DecimalFormat("#,##0.0000");
        
        // Here's the deal... With color codes, Java's String.format() cannot detect the correct
        // length of a tag. So go through all tags, strip the colors, and see how long they are.
        // We need to know the max length so we can pad the others with periods to align all costs.
        int maxRankNameSize = 0;
        int maxRankTagNoColorSize = 0;
        for (Rank rank : ladder.getRanks()) {
        	if ( rank.getName().length() > maxRankNameSize ) {
        		maxRankNameSize = rank.getName().length();
        	}
        	String tag = rank.getTag() == null ? "" : rank.getTag();
        	String tagNoColor = Text.stripColor( tag );
        	if ( tagNoColor.length() > maxRankTagNoColorSize ) {
        		maxRankTagNoColorSize = tagNoColor.length();
        	}
        }
        

        RankPlayerFactory rankPlayerFactory = new RankPlayerFactory();
        
        boolean first = true;
        for (Rank rank : ladder.getRanks()) {
        	
        	boolean defaultRank = ("default".equalsIgnoreCase( ladder.getName() ) && first);
        	
        	
        	// Since the formatting gets confused with color formatting, we must 
        	// strip the color codes and then inject them back in.  So instead, this
        	// provides the formatting rules for both name and rank tag, thus 
        	// taking in to consideration the color codes and if the hasPerms is
        	// true. To prevent variable space issues, the difference is filled in with periods.
        	String textRankNameString = padRankName( rank, maxRankNameSize, maxRankTagNoColorSize, hasPerm );
        	
//        	// trick it to deal correctly with tags.  Tags can have many colors, but
//        	// it will render as if it had the colors stripped.  So first generate the
//        	// formatted text with tagNoColor, then replace the no color tag with the
//        	// normal tag.
//        	// If tag is null, show it as an empty String.  Normally rank name will
//        	// be used, but at least this show's it is not set.
//        	String tag = rank.getTag() == null ? "" : rank.getTag();
//        	String tagNoColor = Text.stripColor( tag );
        	
        	
        	
        	// If rank list is being generated for a console or op'd player, then show the ladder's rank multiplier,
        	// but if generating for a player, then show total multiplier accross all ladders.
        	PlayerRank pRank = null;
        	double rankCost = 0;
        	double rMulti = 0;
        	
        	if ( hasPerm || rPlayer == null ) {
        		
        		rankCost = rank.getRawRankCost();
        		
        		pRank = rankPlayerFactory.createPlayerRank( rank );
        		
        		rMulti = pRank.getLadderBasedRankMultiplier();

        	}
        	else {
        		pRank = rankPlayerFactory.createPlayerRank( rank );

        		pRank = pRank.getTargetPlayerRankForPlayer( pRank, rPlayer, rank );
        		rankCost = pRank.getRankCost();
        		
        		rMulti = pRank.getRankMultiplier();
        	}
        	
        	
        	
        	String textCmdCount = ( hasPerm ? 
        			ranksListCommandCountMsg(rank.getRankUpCommands().size())
        			: "" );
        	String textCurrency = (rank.getCurrency() == null ? "" : 
        		ranksListCurrencyMsg( rank.getCurrency() ));
        	
        	String rankMultiplier = rMulti == 0d ? "" : fFmt.format( rMulti );
        	
        	String players = rank.getPlayers().size() == 0 ? "" : 
        		" &dPlayers: &3" + rank.getPlayers().size();
        	
        	String rawRankId = ( hasPerm ?
        			String.format( "(rankId: %s%s%s)",
        					Integer.toString( rank.getId() ),
        					(rank.getRankPrior() == null ? "" : " -"),
        					(rank.getRankNext() == null ? "" : " +") )
        			: "");
        	
        	String text =
        			String.format("&3%s &7%-17s%s&7 &b%s &3%s %s&7 %s%s", 
        					textRankNameString, 
        					Text.numberToDollars( rankCost ),
        					(defaultRank ? "{def}" : ""),
        					
        					rankMultiplier,
        					
        					rawRankId,
        					
        					textCurrency,
        					textCmdCount,
        					players
        					);
        	
//        	// Swap the color tag back in:
//        	text = text.replace( tagNoColor, tag );
        	
        	if ( defaultRank ) {
        		// Swap out the default placeholder for the actual content:
        		text = text.replace( "{def}", "&c(&9Default&c)" );
        	}
        	
        	String rankName = rank.getName();
        	if ( rankName.contains( "&" ) ) {
        		rankName = rankName.replace( "&", "-" );
        	}
        	FancyMessage msg = null;
        	if ( hasPerm ) {
        		msg = new FancyMessage(text).command("/ranks info " + rankName)
        				.tooltip( ranksListClickToViewMsg() );
        	}
        	else {
        		msg = new FancyMessage(text);
        	}
        	
        	builder.add(msg);
        	
//        		rank = rank.getRankNext();
        	first = false;
        	
        }
        
        display.addComponent(builder.build());
        
        
        if ( hasPerm && !"No Ladder".equals( ladder.getName() ) ) {
        	
        	RowComponent row = new RowComponent();
        	
        	row.addFancy(
        			new FancyMessage("&7[&a+&7] Add a new Rank")
        			.suggest("/ranks create <rank> <cost> " + ladder.getName() + " <tag>")
        			.tooltip( ranksListCreateNewRankMsg() ));
        	
        	row.addTextComponent( "      " );
        	
        	row.addFancy(
        			new FancyMessage("&7[&a+&7] Edit Ladder Rank Cost Multiplier")
        			.suggest("/ranks ladder rankCostMultiplier " + 
        								ladder.getName() + " " + ladder.getRankCostMultiplierPerRank())
        			.tooltip( ranksListEditLadderCostMultiplierMsg() ));
        	
        	display.addComponent( row );

        	
        	// Include the listing of all ladder commands at the end of the list of ranks:
        	ChatDisplay cmdLadderDisplays = 
        			getRankCommandCommands().commandLadderListDetail( ladder, true );
        	
        	display.addChatDisplay( cmdLadderDisplays );
        	
        }

        if ( rPlayer != null && !"No Ladder".equals( ladder.getName() ) ) {
        	
//        	RankPlayerFactory rankPlayerFactory = new RankPlayerFactory();
        	
        	double ladderMultiplier = ladder.getRankCostMultiplierPerRank();
        	
        	PlayerRank pRank = rankPlayerFactory.getRank( rPlayer, ladder );
        	double playerMultiplier = pRank != null ? 
        			pRank.getRankMultiplier() : 0;
        	
        	if ( playerMultiplier == 0 ) {
        		display.addText( "&3You have no Ladder Rank Multipliers enabled. The rank costs are not adjusted." );
        	}
        	else {
        		display.addText( "&3Your current total Rank Multiplier: &7%s.", 
        				fFmt.format( playerMultiplier ) );
        		
        		if ( ladderMultiplier == 0 ) {
        			display.addText( "&3This ladder has no Rank Multiplier so all ranks on this ladder " +
        					"have the same multiplier." );
        		} 
        		else {
        			display.addText( "&3This ladder has a Rank Multiplier so each rank has " + 
        					"a differnt multiplier." );
        		}
        		
        		Set<RankLadder> ladders = rPlayer.getLadderRanks().keySet();
        		for ( RankLadder rLadder : ladders ) {
					if ( rLadder.getRankCostMultiplierPerRank() != 0d ) {
						
						Rank r = rPlayer.getLadderRanks().get( rLadder ).getRank();
						
						PlayerRank rpRank = rankPlayerFactory.createPlayerRank( r );
						
						display.addText( "&3  BaseMult: &7%7s  &3CurrMult: &7%7s  &7%s  &7%s  ", 
								fFmt.format( rLadder.getRankCostMultiplierPerRank() ),
								fFmt.format( rpRank.getLadderBasedRankMultiplier() ),
								rLadder.getName(), 
								(r.getTag() == null ? r.getName() : r.getTag())
								);
						
//						display.addText( "&3  Ladder: &7%-9s  &3Rank: &7%-8s  &3Base Mult: %7s", 
//								rLadder.getName(), 
//								rPlayer.getLadderRanks().get( rLadder ).getRank().getTag(),
//								fFmt.format( rLadder.getRankCostMultiplierPerRank() ) );
					}
				}
        	}
        	
        }
        
		return display;
	}

	private String padRankName( Rank rank, int maxRankNameSize, int maxRankTagNoColorSize, boolean hasPerm ) {
		return padRankName( rank.getName(), rank.getTag(), maxRankNameSize, maxRankTagNoColorSize, hasPerm );
	}
    protected String padRankName( String rankName, String rankTag, int maxRankNameSize, int maxRankTagNoColorSize, boolean hasPerm )
	{
    	StringBuilder sb = new StringBuilder();
    	
    	int tLen = (hasPerm ? maxRankNameSize + 1 : 0) + maxRankTagNoColorSize;
    	String name = hasPerm ? rankName + " " : "";
    	String tag = rankTag == null ? "" : rankTag;
    	String tagNoColor = Text.stripColor( tag );
    	
    	sb.append( name ).append( tag ).append( "&8" );
    	
    	int length = name.length() + tagNoColor.length();
    	while ( length++ < tLen ) {
    		sb.append( "." );
    	}
    	
		return sb.toString();
	}

	@Command(identifier = "ranks info", description = "Information about a rank.  Use of the option of 'ALL' then " +
    							"rank commands will be included too.", 
    							onlyPlayers = false, permissions = "ranks.info", 
    							altPermissions = "ranks.admin" )
    public void infoCmd(CommandSender sender, 
    		@Arg(name = "rankName") String rankName, 
    		@Arg(name = "options", def = "", description = "Optional: ['ALL'] will include rank commands.") String options) {
    	
        Rank rank = PrisonRanks.getInstance().getRankManager().getRank(rankName);
        if ( rank == null ) {
//        	rankOpt = PrisonRanks.getInstance().getRankManager().getRankEscaped(rankName);
//        	if (!rankOpt.isPresent()) {
        		rankDoesNotExistMsg( sender, rankName );
        		return;
//        	}
        }


        ChatDisplay display = rankInfoDetails( sender, rank, options );

        display.send(sender);
        
//        if ( options != null && "all".equalsIgnoreCase( options )) {
        	
        	//getRankCommandCommands().commandLadderList( sender, rank.getLadder().getName(), "noRemoves" );
        	
//        	getRankCommandCommands().commandList( sender, rankName, "noRemoves" );
//        }
    }


    public void allRanksInfoDetails( StringBuilder sb ) {
    	
    	PrisonRanks pRanks = PrisonRanks.getInstance();
    	RankManager rMan = pRanks.getRankManager();
    	
    	for ( Rank rank : rMan.getRanks() ) {

    		PrisonCommand.printFooter( sb );
    		
    		JumboTextFont.makeJumboFontText( rank.getName(), sb );
    		sb.append( "\n" );
    		
    		ChatDisplay chatDisplay = rankInfoDetails( null, rank, "all" );
    		
    		sb.append( chatDisplay.toStringBuilder() );
		}

    	PrisonCommand.printFooter( sb );
    }
    
    
	private ChatDisplay rankInfoDetails( CommandSender sender, Rank rank, String options )
	{
		String title = rank.getTag() == null ? rank.getName() : rank.getTag();
		
		ChatDisplay display = new ChatDisplay( ranksInfoHeaderMsg( title ));
		
		boolean isOp = sender != null && sender.isOp();
		boolean isConsole = sender == null || !sender.isPlayer();

        display.addText( ranksInfoNameMsg( rank.getName() ));
        display.addText( ranksInfoTagMsg( rank.getTag() == null ? "none" : rank.getTag() ));
        
        
        RowComponent row = new RowComponent();
        
        row.addTextComponent( ranksInfoLadderMsg( rank.getLadder() != null ? 
				rank.getLadder().getName() : "(not linked to any ladder)" ) );
        
        
        if ( rank.getLadder() != null ) {
        	row.addTextComponent( "      " );
        	
        	row.addTextComponent( "&3Ladder Position: &7%d", rank.getPosition() );
        }
        
        display.addComponent( row );
        
        
        if ( rank.getMines().size() == 0 ) {
        	display.addText( ranksInfoNotLinkedToMinesMsg() );
        }
        else {
        	StringBuilder sb = new StringBuilder();
        	
        	for ( ModuleElement mine : rank.getMines() ) {
				if ( sb.length() > 0 ) {
					sb.append( "&3, " );
				}
				sb.append( "&7" );
				sb.append( mine.getName() );
			}
        	
        	display.addText( ranksInfoLinkedMinesMsg( sb.toString() ));
        }

        
        
        
        
        // NOTE: Since rank info is NOT tied to a PlayerRank we cannot figure out the
        //       the actual cost, but we can calculate the ladder's multiplier.  This 
        //       will not be the player's total multiplier.
        display.addText( ranksInfoCostMsg( rank.getRawRankCost() ));
        
        
        
        // Add the raw ladder rank multiplier here:
        DecimalFormat fFmt = new DecimalFormat("#,##0.0000");
        
        // The following is the rank adjusted rank multiplier
        
        RankPlayerFactory rankPlayerFactory = new RankPlayerFactory();
        PlayerRank pRank = rankPlayerFactory.createPlayerRank( rank );
        
        double rankCostMultiplier = pRank.getLadderBasedRankMultiplier();
        double ladderBaseMultiplier = rank.getLadder() == null ? 0 : rank.getLadder().getRankCostMultiplierPerRank();
        
        String cmdLadderRankCostMult = "/ranks ladder rankMultiplier " + rank.getName() + " " + ladderBaseMultiplier;
        display.addComponent(new FancyMessageComponent(
    			new FancyMessage(
    					String.format( "&3Rank Cost Multiplier: &7%s  &3Ladder Base Multiplier: &7%s",
    							fFmt.format( rankCostMultiplier ),
    							fFmt.format( ladderBaseMultiplier )
    							)).suggest( cmdLadderRankCostMult )
    			.tooltip( "Ladder Rank Cost Multiplier" )));
    	

        
        display.addText( ranksInfoCurrencyMsg( (rank.getCurrency() == null ? "&cdefault" : rank.getCurrency()) ));
        
        int numberOfPlayersOnRank = rank.getPlayers().size();
        display.addText( ranksInfoPlayersWithRankMsg( numberOfPlayersOnRank ));

        if ( isOp || isConsole || sender.hasPermission("ranks.admin")) {
            // This is admin-exclusive content

//            display.addText("&8[Admin Only]");
            display.addText( ranksInfoRankIdMsg( rank.getId() ));

//            FancyMessage del =
//                new FancyMessage( ranksInfoRankDeleteMessageMsg() ).command("/ranks delete " + rank.getName())
//                    .tooltip( ranksInfoRankDeleteToolTipMsg() );
//            display.addComponent(new FancyMessageComponent(del));
        }
        
        if ( (isOp || isConsole) && options != null && "all".equalsIgnoreCase( options )) {
        	
        	if ( rank.getLadder() != null ) {
        		
        		ChatDisplay cmdLadderDisplays = getRankCommandCommands().commandLadderListDetail( rank.getLadder(), true );
        		display.addChatDisplay( cmdLadderDisplays );
        	}
        	
        	ChatDisplay cmdLadderCmdsDisplays = getRankCommandCommands().commandListDetails( rank, true );
        	display.addChatDisplay( cmdLadderCmdsDisplays );
        }
        
		return display;
	}


    @Command(identifier = "ranks set cost", description = "Sets a rank's raw cost. The ladder's rank cost " +
    		"multipliers are not applied to this value, and they will impact the actual price that a " +
    		"player will have to pay.  Every player may have to pay a different price for this rank, if " +
    		"ladder rank multipiers are used, and players have different ranks in the ladders that " +
    		"have them enabled.", 
    							onlyPlayers = false, permissions = "ranks.set")
    public void setCost(CommandSender sender, 
    		@Arg(name = "rankName") String rankName, 
    		@Arg(name = "rawCost", description = "The raw cost of this rank.") double rawCost) {
    	
        Rank rank = PrisonRanks.getInstance().getRankManager().getRank(rankName);
        if ( rank == null ) {
        	rankDoesNotExistMsg( sender, rankName );
            return;
        }
        
        
        rank.setRawRankCost( rawCost );
//        PlayerRank.setRawRankCost( rank, rawCost );
        
        PrisonRanks.getInstance().getRankManager().saveRank(rank);
        
        rankSetCostSuccessfulMsg( sender, rankName, rawCost );
    }
    

    @Command(identifier = "ranks set currency", description = "Modifies a rank's currency to use an alternative " +
    		"(custom) currency.  This is the currency that they player will have to pay with to rank up to this " +
    		"rank. This does not change how sellall, or any other aspect of prison will operate.  " +
    		"Few economy plugins support custom currencies; Gems Economy is one " +
    		"that is support by prison.  " +
    		"To use the default currency, this must not be set.", 
    													onlyPlayers = false, permissions = "ranks.set")
    public void setCurrency(CommandSender sender, 
    		@Arg(name = "rankName") String rankName, 
    		@Arg(name = "currency", 
    			description = "The custom currency to use with this rank, " +
    					"or 'none' to remove a custom currency.") String currency){
    	
    	Rank rank = PrisonRanks.getInstance().getRankManager().getRank(rankName);
    	if ( rank == null ) {
    		rankDoesNotExistMsg( sender, rankName );
    		return;
    	}
    	
    	
    	if ( currency == null || currency.trim().length() == 0 ) {
    		rankSetCurrencyNotSpecifiedMsg( sender, currency );
    		return;
    	}
    	
    	if ( "none".equalsIgnoreCase( currency ) && rank.getCurrency() == null ) {

    		rankSetCurrencyNoCurrencyToClearMsg( sender, rankName );
    		
    	}
    	else if ( "none".equalsIgnoreCase( currency ) ) {
    		rank.setCurrency( null );
    		
    		PrisonRanks.getInstance().getRankManager().saveRank(rank);
    		
    		rankSetCurrencyClearedMsg( sender, rankName );
    		
    	}
    	else {
    		
    		EconomyCurrencyIntegration currencyEcon = PrisonAPI.getIntegrationManager()
    				.getEconomyForCurrency( currency );
    		if ( currencyEcon == null ) {
    			
    			rankSetCurrencyNoActiveSupportMsg( sender, currency );
    			return;
    		}
    		
    		rank.setCurrency( currency );
    		
    		PrisonRanks.getInstance().getRankManager().saveRank(rank);
    		
    		rankSetCurrencySuccessfulMsg( sender, rankName, currency );
    	}
    	
    }

    @Command(identifier = "ranks set tag", description = "Modifies a ranks tag. If a rank does not have a " +
    		"tag set, then the rank name will be used. You can use color codes to stylize your rank tag.", 
    							onlyPlayers = false, permissions = "ranks.set")
    public void setTag(CommandSender sender, 
    				@Arg(name = "rankName") String rankName, 
    				@Wildcard(join=true)
    					@Arg(name = "tag", description = "Tag value for the Rank. Use [none] to remove.") String tag){
    	
        Rank rank = PrisonRanks.getInstance().getRankManager().getRank(rankName);
        if ( rank == null ) {
        	rankDoesNotExistMsg( sender, rankName );
            return;
        }
        

        if ( tag == null || tag.trim().length() == 0 ) {
        	rankSetTagInvalidMsg( sender );
        	return;
        }
        
        if ( tag.equalsIgnoreCase( "none" ) ) {
        	tag = null;
        }


        if ( tag == null && rank.getTag() == null || 
        		rank.getTag() != null &&
        		rank.getTag().equalsIgnoreCase( tag )) {
        
        	rankSetTagNoChangeMsg( sender );
        	return;
        }

        rank.setTag( tag );
        
        PrisonRanks.getInstance().getRankManager().saveRank(rank);

        if ( tag == null ) {
        	rankSetTagClearedMsg( sender, rank.getName() );
        }
        else {
        	rankSetTagSucessMsg( sender, tag, rank.getName() );
        }
    }
    
    
    
    
//    @Command(identifier = "ranks import byPerms", description = "This resets 'all' player's ranks based " +
//    		"upon a series of permissions.  An example of a permission may be 'permission.mine.A'. Basically " +
//    		"a standard prefix, followed by a rank name, or mine name.  This is a very dangerous command to " +
//    		"run because it will reset many player's ranks. All ranks can be reset back to a " +
//    		"spcific rank with the command '/ranks set rank *all* A default`. " +
//    		"It is highly recommended to backup your server, and Prison's plugin folder.  Plus you should" +
//    		"reset all your players to the default rank on the 'default' " +
//    		"ladder.  Usually the default rank is 'a', but please confirm before resetting all ranks.  "
//    		, 
//    		onlyPlayers = false, aliases="ranks stats")
    public void ranksImportByPermissions(CommandSender sender,
    			@Arg(name = "ladderName", def = "default", 
    						description = "Ladder Name. Required: [default]") String ladderName,
    			
    			@Arg(name = "permission", description = "This is the full permission name, but use " +
    					"a '{rank}' placeholder to indicate where the rank name should be. ") String permission,
    			@Arg(name = "options", def = "", 
    				description = "Work in progress.. options will be added in the future. " +
    						"[]") String options){

    	
    	if ( !ladderName.equalsIgnoreCase( "all" ) && 
    			PrisonRanks.getInstance().getLadderManager().getLadder( ladderName ) == null ) {
    		ranksPlayersInvalidLadderMsg( sender, ladderName );
    		return;
    	}
    	
    	
//    	RanksByLadderOptions option = RanksByLadderOptions.fromString( action );
//    	if ( option == null ) {
//    		ranksPlayersInvalidActionMsg( sender, action );
//    		return;
//    	}
    	
//    	boolean includeAll = action.equalsIgnoreCase( "all" );
//    	PrisonRanks.getInstance().getRankManager().ranksByLadders( sender, ladderName, option );
    	
//    	Output.get().logInfo( "Ranks by ladders:" );
//    	
//    	for ( RankLadder ladder : PrisonRanks.getInstance().getLadderManager().getLadders() ) {
//    		if ( ladderName.equalsIgnoreCase( "all" ) || ladderName.equalsIgnoreCase( ladder.name ) ) {
//    			
//    			boolean includeAll = action.equalsIgnoreCase( "all" );
//    			String ladderRanks = ladder.listAllRanks( includeAll );
//    			
//    			sender.sendMessage( ladderRanks );
//    		}
//			
//		}
    	
    }
    
    
    
    
    @Command(identifier = "ranks player", description = "Shows a player their rank", 
    		onlyPlayers = false, altPermissions = "ranks.admin" )
    public void rankPlayer(CommandSender sender,
    			@Arg(name = "player", def = "", description = "Player name") String playerName,
    			@Arg(name = "options", def = "", description = "Options [perms]") String options
    			){
    	
    	Player player = getPlayer( sender, playerName );
    	
    	if (player == null) {
    		ranksPlayerOnlineMsg( sender );
    		return;
    	}

    	List<String> msgs = new ArrayList<>();

    	DecimalFormat iFmt = new DecimalFormat("#,##0");
    	DecimalFormat dFmt = new DecimalFormat("#,##0.00");
    	DecimalFormat fFmt = new DecimalFormat("0.0000");
    	DecimalFormat pFmt = new DecimalFormat("#,##0.0000");
		SimpleDateFormat sdFmt = new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss" );
    	
    	
    	PlayerManager pm = PrisonRanks.getInstance().getPlayerManager();
		RankPlayer rankPlayer = pm.getPlayer(player.getUUID(), player.getName());

		// Get the cachedPlayer:
		PlayerCachePlayerData cPlayer = PlayerCache.getInstance().getOnlinePlayer( rankPlayer );
		
		
		
		String msg1 = String.format( "&7Player Stats for&8:    &c%s", 
				rankPlayer.getName() );
		msgs.add( msg1 );


		
		String lastSeen = cPlayer == null || cPlayer.getLastSeenDate() == 0 ? 
				"-- never -- " :
					sdFmt.format( new Date( cPlayer.getLastSeenDate() ) );
		long lastSeenElapsedMs = cPlayer == null || cPlayer.getLastSeenDate() == 0 ? 
						0 : System.currentTimeMillis() - cPlayer.getLastSeenDate();
		String lastSeenElapsed = lastSeenElapsedMs == 0 ? 
				"" : 
					// less than 5 mins just show recently since its not too accurate
					lastSeenElapsedMs < (5 * 60 * 1000) ? "-- recently --" :
					Text.formatTimeDaysHhMmSs( System.currentTimeMillis() - cPlayer.getLastSeenDate() ) + " ago";
		
		String msgLs = String.format( "  &7Last Seen: &3%s   %s", 
				lastSeen, lastSeenElapsed );
		msgs.add( msgLs );
		
		
		
		
		String msg2 = String.format( "  &7Rank Cost Multiplier: &f", 
						fFmt.format( rankPlayer.getSellAllMultiplier() ));
		msgs.add( msg2 );

		
		
		if ( rankPlayer != null ) {
//			DecimalFormat iFmt = new DecimalFormat("#,##0");
//			
//			SimpleDateFormat sdFmt = new SimpleDateFormat( "HH:mm:ss" );
			
			// Collect all currencies in the default ladder:
			Set<String> currencies = new LinkedHashSet<>();
			LadderManager lm = PrisonRanks.getInstance().getLadderManager();
			
			for ( Rank rank : lm.getLadder( "default" ).getRanks() ) {
				if ( rank.getCurrency() != null && !currencies.contains( rank.getCurrency() )) {
					currencies.add( rank.getCurrency() );
				}
			}
			
			
//			RankPlayerFactory rankPlayerFactory = new RankPlayerFactory();
			
			
			Map<RankLadder, PlayerRank> rankLadders = rankPlayer.getLadderRanks();
			
			for ( RankLadder rankLadder : rankLadders.keySet() )
			{
				PlayerRank pRank = rankLadders.get( rankLadder );
				Rank rank = pRank.getRank();
				Rank nextRank = rank.getRankNext();
				
		        // This calculates the target rank, and takes in to consideration the player's existing rank:
				PlayerRank nextPRank = pRank.getTargetPlayerRankForPlayer( rankPlayer, nextRank );
//				PlayerRank nextPRank = PlayerRank.getTargetPlayerRankForPlayer( rankPlayer, nextRank );

//				PlayerRank nextPRank = nextRank == null ? null :
//									new PlayerRank( nextRank, pRank.getRankMultiplier() );
				
				String messageRank = ranksPlayerLadderInfoMsg( 
						rankLadder.getName(),
						rank.getName() );
				
				if ( nextRank == null ) {
					messageRank += ranksPlayerLadderHighestRankMsg();
				} else {
					messageRank += ranksPlayerLadderNextRankMsg( 
							nextRank.getName(), 
							( nextRank == null ? "0" : dFmt.format( nextPRank.getRankCost()) ) );
//							dFmt.format( nextRank.getCost() ) );

					if ( nextRank.getCurrency() != null ) {
						messageRank += ranksPlayerLadderNextRankCurrencyMsg( nextRank.getCurrency() );
					}
				}
				
				msgs.add( messageRank );
//				sendToPlayerAndConsole( sender, messageRank );
			}
			
			// Print out the player's balances: 

			// The default currency first:
			double balance = rankPlayer.getBalance();
			String message = ranksPlayerBalanceDefaultMsg( player.getName(), dFmt.format( balance ) );
			msgs.add( message );
//			sendToPlayerAndConsole( sender, message );
			
			
			for ( String currency : currencies ) {
				double balanceCurrency = rankPlayer.getBalance( currency );
				String messageCurrency = ranksPlayerBalanceOthersMsg( 
						player.getName(), dFmt.format( balanceCurrency ), currency );
				msgs.add( messageCurrency );
//				sendToPlayerAndConsole( sender, messageCurrency );

			}
			
			boolean isOp = player.isOp();
			boolean isPlayer = player.isPlayer();
			boolean isOnline = player.isOnline();
			
			boolean isPrisonPlayer = (player instanceof Player);
			boolean isPrisonOfflineMcPlayer = (player instanceof OfflineMcPlayer);

			if ( !isOnline ) {
				String msgOffline = ranksPlayerPermsOfflineMsg();
				msgs.add( msgOffline );
//				sendToPlayerAndConsole( sender, msgOffline );
			}
			
			double sellallMultiplier = player.getSellAllMultiplier();
			String messageNotAccurrate = ranksPlayerNotAccurateMsg();
			String messageSellallMultiplier = ranksPlayerSellallMultiplierMsg( 
					pFmt.format( sellallMultiplier ), 
					(!isOnline ? "  " + messageNotAccurrate : "") );
			msgs.add( messageSellallMultiplier );
//			sendToPlayerAndConsole( sender, messageSellallMultiplier );

			
			
			msgs.add( "" );
			

			if ( cPlayer != null ) {
				
				msgs.add( "PlayerCache Information:" );
				
				// Print all earnings for all mines:
				msgs.add( String.format( 
						"  Earnings By Mine:    &2Avg Earnings per min: &3%s", 
								dFmt.format( cPlayer.getAverageEarningsPerMinute() )) );
				
				msgs.addAll( 
						Text.formatTreeMapStats(cPlayer.getEarningsByMine(), 5 ) );	
				
				
				if ( cPlayer.getTokens() > 0 || cPlayer.getTokensTotal() > 0 ||
						cPlayer.getTokensByMine().size() > 0 ) {
					
					msgs.add( String.format( 
							"  Tokens By Mine:   &2Tokens: &3%s   &2Avg/min: &3%s   &2Blocks/Token: &3%d",
									iFmt.format( cPlayer.getTokens() ),
									dFmt.format( cPlayer.getAverageTokensPerMinute() ),
									AutoFeaturesWrapper.getInstance().getInteger( AutoFeatures.tokensBlocksPerToken )
							));
					
					msgs.add( String.format( 
							"                    &2TotalEarned: &3%s   &2AdminAdded: &3%s   &2AdminRemvd: &3%s",
									iFmt.format( cPlayer.getTokensTotal() ),
									iFmt.format( cPlayer.getTokensTotalAdminAdded() ),
									iFmt.format( cPlayer.getTokensTotalAdminRemoved() )
							));
					
					msgs.addAll( 
							Text.formatTreeMapStats(cPlayer.getTokensByMine(), 5 ) );	
				}
				
				
				msgs.add( String.format( 
						"  &7Timings By Mine&8:    &2Online&8: &3%s    &2Mining&8: &3%s", 
						Text.formatTimeDaysHhMmSs( cPlayer.getOnlineTimeTotal() ),
						Text.formatTimeDaysHhMmSs( cPlayer.getOnlineMiningTimeTotal()) )
						);
				
				msgs.addAll( 
						Text.formatTreeMapStats(cPlayer.getTimeByMine(), 5, true ) );
				
				
				// Print all earnings for all mines:
				String totalBlocks = PlaceholdersUtil.formattedKmbtSISize( 
											cPlayer.getBlocksTotal(), dFmt, " &9" );
				msgs.add( String.format( 
						"  &7Blocks By Mine&8:    &2Blocks Total&8: &3%s", 
						totalBlocks) );
				msgs.addAll( 
						Text.formatTreeMapStats(cPlayer.getBlocksByMine(), 5 ) );
				
				
				msgs.add( "  &7Blocks By Type&8:" );
				msgs.addAll( 
						Text.formatTreeMapStats(cPlayer.getBlocksByType(), 3 ) );
				
				
//				Set<String> keysEarnings = cPlayer.getEarningsByMine().keySet();
//				
//				int count = 0;
//				StringBuilder sbErn = new StringBuilder();
//				for ( String earningKey : keysEarnings )
//				{
//					Double mineEarnings = cPlayer.getEarningsByMine().get( earningKey );
//					
//					String earnings = PlaceholdersUtil.formattedKmbtSISize( mineEarnings, dFmt, " " );
//					
//					sbErn.append( String.format( "%s %s    ", earningKey, earnings ) );
//					
//					if ( count++ % 5 == 0 ) {
//						msgs.add( String.format( 
//								"    " + sbErn.toString() ) );
//						sbErn.setLength( 0 );
//						
//					}
//				}
//				
//				if ( sbErn.length() > 0 ) {
//					
//					msgs.add( String.format( 
//							"    " + sbErn.toString() ) );
//				}
				
				
				
//				msgs.add( String.format( 
//						"    " ) );
//				
//				cPlayer.getEarningsByMine()
				
			}
			
			
			
			
			
			sendToPlayerAndConsole( sender, msgs );
			
			
			
			if ( sender.hasPermission("ranks.admin") ) {
	            // This is admin-exclusive content

				sendToPlayerAndConsole( sender, ranksPlayerAdminOnlyMsg() );
				
				if ( rankPlayer.getNames().size() > 1 ) {
					
					sendToPlayerAndConsole( sender, "  " + ranksPlayerPastNamesMsg() );
					
					for ( RankPlayerName rpn : rankPlayer.getNames() ) {
						
						sendToPlayerAndConsole( sender, "    &b" + rpn.toString() );
					}
				}

				
				if ( options != null && options.toLowerCase().contains( "perms" ) ) 
				{
					
					String msgPlayerPerms = ranksPlayerPermsMsg();
					
					sendToPlayerAndConsole( sender, String.format( "  %s:  %s%s%s%s", 
								msgPlayerPerms,
								(isOp ? " " + ranksPlayerOpMsg() : ""),
								(isPlayer ? " " + ranksPlayerPlayerMsg() : ""),
								(isOnline ? " " + ranksPlayerOnlineMsg() : " " + ranksPlayerOfflineMsg()),
								(isPrisonOfflineMcPlayer ? " " + ranksPlayerPrisonOfflinePlayerMsg() : 
									(isPrisonPlayer ? " " + ranksPlayerPrisonPlayerMsg() : ""))
							) );
					
					if ( !isOnline ) {
						sendToPlayerAndConsole( sender, ranksPlayerPermsOfflineMsg() );
					}
					
					player.recalculatePermissions();
					
					List<String> perms = player.getPermissions();
					
					listPermissions( sender, "bukkit", perms );

//					sendToPlayerAndConsole( sender, "### has perm prison.mines.a: " + 
//								player.hasPermission( "prison.mines.a" ) );
					
					
					List<Integration> permissionIntegrations = PrisonAPI.getIntegrationManager().getAllForType( IntegrationType.PERMISSION );

					for ( Integration pIntegration : permissionIntegrations ) {
						if ( pIntegration instanceof PermissionIntegration ) {
							
							PermissionIntegration integrationPerms = (PermissionIntegration) pIntegration;
							
							List<String> iPerms = integrationPerms.getPermissions( player, true );
							
							String permSource = integrationPerms.getDisplayName();
							listPermissions( sender, permSource, iPerms );
						}
					}
					
				}
	        }
			
//			String nextRank = pm.getPlayerNextRankName( rankPlayer );
//			String nextRankCost = pm.getPlayerNextRankCost( rankPlayer );
//			
//			String message = String.format("&c%s&7:  Current Rank: &b%s&7", 
//					player.getDisplayName(), pm.getPlayerRankName( rankPlayer ));
//			
//			if ( nextRank.trim().length() == 0 ) {
//				message += "  It's the highest rank!";
//			} else {
//				message += String.format("  Next rank: &b%s&7 &c$&b%s &7s%", 
//						nextRank, nextRankCost, currency );
//			}
//			sender.sendMessage( message );
			
		}
//		else {
//			ranksPlayerNoRanksFoundMsg( sender, player.getDisplayName() );
//		}
    }
    
//    private String formatTimeMs( long timeMs ) {
//    	
//    	DecimalFormat iFmt = new DecimalFormat("#,##0");
//    	DecimalFormat tFmt = new DecimalFormat("00");
////    	SimpleDateFormat sdFmt = new SimpleDateFormat( "HH:mm:ss" );
//    	
//    	long _sec = 1000;
//    	long _min = _sec * 60;
//    	long _hour = _min * 60;
//    	long _day = _hour * 24;
//
//    	long ms = timeMs;
//		long days = _day < ms ? ms / _day : 0;
//		
//		ms -= (days * _day);
//		long hours = _hour < ms ? ms / _hour : 0;
//		
//		ms -= (hours * _hour);
//		long mins = _min < ms ? ms / _min : 0;
//		
//		ms -= (mins * _min);
//		long secs = _sec < ms ? ms / _sec : 0;
//		
//		
//		String results = 
//				(days == 0 ? "" : iFmt.format( days ) + "d ") +
//				tFmt.format( hours ) + ":" +
//				tFmt.format( mins ) + ":" +
//				tFmt.format( secs )
//				;
//
//		return results;
//    }
    
//    private void formatTreeMapStats( TreeMap<String,?> statMap, List<String> msgs, 
//    		DecimalFormat dFmt, DecimalFormat iFmt, 
//    		int columns ) {
//    	
//		Set<String> keysEarnings = statMap.keySet();
//		
//		
//		List<String> values = new ArrayList<>();
//		List<Integer> valueMaxLen = new ArrayList<>();
//		
//		
//		int count = 0;
//		StringBuilder sb = new StringBuilder();
//		for ( String earningKey : keysEarnings )
//		{
//			String value = null;
//			Object valueObj = statMap.get( earningKey );
//			
//			if ( valueObj instanceof Double ) {
//				
//				value = PlaceholdersUtil.formattedKmbtSISize( (Double) valueObj, dFmt, " &9" );
//			}
//			else if ( valueObj instanceof Integer ) {
//				int intVal = (Integer) valueObj;
//				value = PlaceholdersUtil.formattedKmbtSISize( intVal, 
//						( intVal < 1000 ? iFmt : dFmt ), " &9" );
//			}
//			else if ( valueObj instanceof Long ) {
//				
//				value = Text.formatTimeDaysHhMmSs( (Long) valueObj );
//			}
//			
//			String msg = String.format( "&3%s&8: &b%s", earningKey, value ).trim();
//			
//			String msgNoColor = Text.stripColor( msg );
//			int lenMNC = msgNoColor.length();
//			
//		
//			int col = values.size() % columns;
//			values.add( msg );
//			
//			if ( col >= valueMaxLen.size() || lenMNC > valueMaxLen.get( col ) ) {
//				
//				if ( col > valueMaxLen.size() - 1 ) {
//					valueMaxLen.add( lenMNC );
//				}
//				else {
//					
//					valueMaxLen.set( col, lenMNC );
//				}
//			}
//		}
//		
//		
//		for ( int j = 0; j < values.size(); j++ )
//		{
//			String msg = values.get( j );
//			
//			int col = j % columns;
//			
//			int maxColumnWidth = col > valueMaxLen.size() - 1 ?
//							msg.length() :
//								valueMaxLen.get( col );
//		
//			sb.append( msg );
//			
//			// Pad the right of all content with spaces to align columns, up to a 
//			// given maxLength:
//			String msgNoColor = Text.stripColor( msg );
//			int lenMNC = msgNoColor.length();
//			for( int i = lenMNC; i < maxColumnWidth; i++ ) {
//				sb.append( " " );
//			}
//
//			// The spacer:
//			sb.append( "   " );
//			
//			if ( ++count % columns == 0 ) {
//				msgs.add( String.format( 
//						"      " + sb.toString() ) );
//				sb.setLength( 0 );
//				
//			}
//		}
//		
//		if ( sb.length() > 0 ) {
//			
//			msgs.add( String.format( 
//					"      " + sb.toString() ) );
//		}
//
//    	
//    }
    
    
////    @Command(identifier = "ranks playerInventory", permissions = "mines.set", 
////    		description = "For listing what's in a player's inventory by dumping it to console.", 
////    		onlyPlayers = false )
//    public void ranksPlayerInventoryCommand(CommandSender sender,
//					@Arg(name = "player", def = "", description = "Player name") String playerName
//			) {
//    	
//    	Player player = getPlayer( sender, playerName );
//    	
//    	if (player == null) {
//    		sender.sendMessage( "&3You must be a player in the game to run this command, and/or the player must be online." );
//    		return;
//    	}
//
////    	Player player = getPlayer( sender );
////    	
////    	if (player == null || !player.isOnline()) {
////    		sender.sendMessage( "&3You must be a player in the game to run this command." );
////    		return;
////    	}
//    	
//    	player.printDebugInventoryInformationToConsole();
//    }
//    
    
	private void listPermissions( CommandSender sender, String prefix, List<String> perms )
	{
		StringBuilder sb = new StringBuilder();
		
		Collections.sort( perms );
		
		for ( String perm : perms ) {
			sb.append( perm ).append( " " );
			
			if ( sb.length() > 70 ) {
				String message = String.format( "    &7* (%s) &3%s", 
						prefix, sb.toString().replace( "%", "%%%%" ));
				sendToPlayerAndConsole( sender, message );
				sb.setLength( 0 );
			}
		}
		if ( sb.length() > 0 ) {
			String message = String.format( "    &7* (%s) &3%s", 
					prefix, sb.toString().replace( "%", "%%%%" ));
			sendToPlayerAndConsole( sender, message );
			sb.setLength( 0 );
		}
	}

	private void sendToPlayerAndConsole( CommandSender sender, List<String> messages )
	{
		
		// If not a console user then send the message to the sender, other wise if a console
		// user then they will see duplicate messages:
		if ( sender.getName() != null && !sender.getName().equalsIgnoreCase( "console" ) ) {
			sender.sendMessage( messages.toArray( new String[0] ) );
		}
		
		for ( String message : messages )
		{
			// log the rank. There was one issue with the ranks suddenly being changed so this
			// will help document what ranks were.
			Output.get().log( message, LogLevel.PLAIN );
		}
	}
	
	private void sendToPlayerAndConsole( CommandSender sender, String messageRank )
	{
		// If not a console user then send the message to the sender, other wise if a console
		// user then they will see duplicate messages:
		if ( sender.getName() != null && !sender.getName().equalsIgnoreCase( "console" ) ) {
			sender.sendMessage( messageRank );
		}
		
		// log the rank. There was one issue with the ranks suddenly being changed so this
		// will help document what ranks were.
		Output.get().log( messageRank, LogLevel.PLAIN );
	}
 
    
    @Command(identifier = "ranks players", description = "Shows all ranks with player counts", 
    		onlyPlayers = false, aliases="ranks stats")
    public void rankPlayers(CommandSender sender,
    			@Arg(name = "ladderName", def = "all", description = "Ladder Name [all, none, LadderName]") String ladderName,
    			@Arg(name = "action", def = "all", 
    				description = "List type; default so 'all'. 'Players' only shows ranks that have player. " +
    						"'All' includes all ranks including ones without players. " +
    						"'Full includes player names if prison is tracking them. [players, all, full]") String action){

    	
    	if ( !ladderName.equalsIgnoreCase( "all" ) && 
    			PrisonRanks.getInstance().getLadderManager().getLadder( ladderName ) == null ) {
    		ranksPlayersInvalidLadderMsg( sender, ladderName );
    		return;
    	}
    	
    	
    	RanksByLadderOptions option = RanksByLadderOptions.fromString( action );
    	if ( option == null ) {
    		ranksPlayersInvalidActionMsg( sender, action );
    		return;
    	}
    	
//    	boolean includeAll = action.equalsIgnoreCase( "all" );
    	PrisonRanks.getInstance().getRankManager().ranksByLadders( sender, ladderName, option );
    	
//    	Output.get().logInfo( "Ranks by ladders:" );
//    	
//    	for ( RankLadder ladder : PrisonRanks.getInstance().getLadderManager().getLadders() ) {
//    		if ( ladderName.equalsIgnoreCase( "all" ) || ladderName.equalsIgnoreCase( ladder.name ) ) {
//    			
//    			boolean includeAll = action.equalsIgnoreCase( "all" );
//    			String ladderRanks = ladder.listAllRanks( includeAll );
//    			
//    			sender.sendMessage( ladderRanks );
//    		}
//			
//		}
    	
    }
    

    
    @Command(identifier = "ranks topn", description = "Shows the top-n ranked players on the server. "
    		+ "The rankings are calculated by prestiges ladder, default ladder, and then by the "
    		+ "player's balance which is used to generate a rank-score.  If enabled, players who "
    		+ "do not rankup when they can, will be penalized with their excessive balance. ", 
    		onlyPlayers = false, aliases="topn")
    public void rankTopN(CommandSender sender,
    			@Arg(name = "page", def = "1", 
    				description = "Page number [1]") String pageNumber,
    			@Arg(name = "pageSize", def = "10", 
    				description = "Page size [10]") String pageSizeNumber){

    	int page = 1;
    	int pageSize = 10;
    	
    	
    	try {
    		page = Integer.parseInt(pageNumber);
    	}
    	catch (NumberFormatException e ) {
    		// Ignore: will use defaults
    	}
    	try {
    		pageSize = Integer.parseInt(pageSizeNumber);
    	}
    	catch (NumberFormatException e ) {
    		// Ignore: will use defaults
    	}
    	
    	if ( page <= 0 ) {
    		page = 1;
    	}
    	if ( pageSize <= 0 ) {
    		pageSize = 10;
    	}
    	
    	int totalPlayers = PrisonRanks.getInstance().getPlayerManager().getPlayers().size();
    	int totalPages = (totalPlayers / pageSize) + (totalPlayers % pageSize == 0 ? 0 : 1);
    	
    	if ( page > totalPages ) {
    		page = totalPages;
    	}

    	int posStart = (page - 1) * pageSize;
    	int posEnd = posStart + pageSize;
    	
//    	DecimalFormat dFmt = new DecimalFormat("#,##0.00");
    	
    	List<RankPlayer> topN = PrisonRanks.getInstance().getPlayerManager().getPlayersByTop();

    	sender.sendMessage( RankPlayer.printRankScoreLineHeader() );
    	
    	for ( int i = posStart; i < posEnd && i < topN.size(); i++ ) {
    		RankPlayer rPlayer = topN.get(i);
    		
    		String message = rPlayer.printRankScoreLine( i + 1 );
    		
    		sender.sendMessage(message);
    	}
    	

    	
    }
    
    
    
//    /**
//     * This function is just an arbitrary test to access the various components.
//     * 
//     * @param sender
//     * @param playerName
//     */
//    @Command( identifier = "ranks test", onlyPlayers = false, permissions = "prison.admin" )
//    public void prisonModuleTest(CommandSender sender,
//			@Arg(name = "player", def = "", description = "Player name") String playerName){
//    	
// 		ModuleManager modMan = Prison.get().getModuleManager();
// 	    Module module = modMan == null ? null : modMan.getModule( PrisonRanks.MODULE_NAME ).orElse( null );
//
// 	    int moduleCount = (modMan == null ? 0 : modMan.getModules().size());
// 	    sender.sendMessage(String.format( "prisonModuleTest: prison=%s moduleManager=%s " +
// 	    		"registeredModules=%s PrisonRanks=%s", 
// 	    		(Prison.get() == null ? "null" : "active"),
// 	    		(Prison.get().getModuleManager() == null ? "null" : "active"),
// 	    		Integer.toString( moduleCount ),
// 	    		(modMan.getModule( PrisonRanks.MODULE_NAME ) == null ? "null" : "active")
// 	    		) );
//
// 	    if ( module == null || !(module instanceof PrisonRanks) ) {
// 	    	
// 	    	sender.sendMessage( "prisonModuleTest: Cannot get PrisonRanks. Terminating" );
// 	    	return;
// 	    }
// 	    
//
// 	    PrisonRanks rankPlugin = (PrisonRanks) module;
//
// 	    if ( rankPlugin == null || rankPlugin.getPlayerManager() == null ) {
// 	    	sender.sendMessage( "prisonModuleTest: PrisonRanks could not be created. Terminating" );
// 	    	return;
// 	    }
//
// 	    
// 	    PlayerManager playerManager = rankPlugin.getPlayerManager();
//    	Player player = getPlayer( sender, playerName );
//
//    	sender.sendMessage( String.format( "prisonModuleTest: PlayerManager=%s player=%s sender=%s playerName=%s",
//    				(playerManager == null ? "null" : "active"), (player == null ? "null" : player.getName()),
//    				(sender == null ? "null" : sender.getName()), (playerName == null ? "null" : playerName)
//    			));
//
//    	
//    	if ( player == null ) {
//    		sender.sendMessage( "prisonModuleTest: Cannot get a valid player. " +
//    				"If console, must supply a valid name. Terminating" );
//    		return;
//    	}
// 
//    	RankPlayer rPlayer = playerManager.getPlayer( player.getUUID() ).orElse( null );
//        LadderManager lm = rankPlugin.getLadderManager();
//        
//        for ( RankLadder ladderData : lm.getLadders() ) {
//        	Rank playerRank = rPlayer == null ? null : rPlayer.getRank( ladderData ).orElse( null );
//        	Rank rank = ladderData.getLowestRank().orElse( null );
//        	
//        	while ( rank != null ) {
//        		boolean playerHasThisRank = playerRank != null && playerRank.equals( rank );
//        		
//        		sender.sendMessage(String.format( "prisonModuleTest: ladder=%s rank=%s playerRank=%s hasRank=%s", 
//        				ladderData.name, rank.name, (playerRank == null ? "null" : playerRank.name ),
//        				Boolean.valueOf( playerHasThisRank ).toString()
//        				));
//        		
//        		rank = rank.rankNext;
//        	}
//        }
//    }
    

}
