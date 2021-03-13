package tech.mcprison.prison.ranks.commands;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.TreeMap;
import java.util.stream.Collectors;

import tech.mcprison.prison.Prison;
import tech.mcprison.prison.PrisonAPI;
import tech.mcprison.prison.PrisonCommand.RegisteredPluginsData;
import tech.mcprison.prison.chat.FancyMessage;
import tech.mcprison.prison.commands.Arg;
import tech.mcprison.prison.commands.BaseCommands;
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
import tech.mcprison.prison.output.Output;
import tech.mcprison.prison.output.RowComponent;
import tech.mcprison.prison.ranks.PrisonRanks;
import tech.mcprison.prison.ranks.data.Rank;
import tech.mcprison.prison.ranks.data.RankLadder;
import tech.mcprison.prison.ranks.data.RankPlayer;
import tech.mcprison.prison.ranks.data.RankPlayerName;
import tech.mcprison.prison.ranks.managers.LadderManager;
import tech.mcprison.prison.ranks.managers.PlayerManager;
import tech.mcprison.prison.util.Text;

/**
 * @author Faizaan A. Datoo
 */
public class RanksCommands
			extends BaseCommands {
	
	private CommandCommands rankCommandCommands = null;
	
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
    
    @Command(identifier = "ranks perms", 
    		onlyPlayers = false, permissions = "prison.commands")
    public void ranksPermsSubcommands(CommandSender sender) {
    	sender.dispatchCommand( "ranks perms help" );
    }
    
    @Command(identifier = "ranks remove", 
    		onlyPlayers = false, permissions = "prison.commands")
    public void ranksRemoveSubcommands(CommandSender sender) {
    	sender.dispatchCommand( "ranks remove help" );
    }
    
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
        @Wildcard(join=true)
        	@Arg(name = "tag", description = "The tag to use for this rank.", def = "none")
            		String tag) {
		
		boolean success = false;

        // Ensure a rank with the name doesn't already exist
        if (PrisonRanks.getInstance().getRankManager().getRank(name) != null) {
            Output.get()
                .sendWarn(sender, 
                		String.format( "&3The rank named &7%s &3already exists. Try a different name.", name) );
            return success;
        }
        
        // Ensure a rank with the name doesn't already exist
        if (name == null || name.trim().length() == 0 || name.contains( "&" )) {
        	Output.get().sendWarn(sender, "&3A rank name is required and cannot contain formatting codes.");
        	return success;
        }

        // Fetch the ladder first, so we can see if it exists

        RankLadder rankLadder = PrisonRanks.getInstance().getLadderManager().getLadder(ladder);
        
        if ( rankLadder == null ) {
            Output.get().sendWarn(sender, "&3A ladder by the name of '&7%s&3' does not exist.", ladder);
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
            Output.get().sendError(sender, "&3The rank could not be created.");
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
        try {
            PrisonRanks.getInstance().getLadderManager().saveLadder(rankLadder);
            
            success = true;
            
            // Tell the player the good news!
            Output.get()
            	.sendInfo(sender, "&3Your new rank, '&7%s&3', was created in the ladder '&7%s&3', " +
            			"using the tag value of '&7%s&3'", name, ladder, tag);
        } catch (IOException e) {
            Output.get().sendError(sender,
                "&3The '&7%s&3' ladder could not be saved to disk. Check the console for details.",
                rankLadder.getName());
            Output.get().logError("&3Ladder could not be written to disk.", e);
        }

        return success;
    }

	
	@Command(identifier = "ranks autoConfigure", description = "Auto configures Ranks and Mines using " +
			"single letters A through Z for both the rank and mine names. If both ranks and mines are " +
			"generated, they will also be linked together automatically. To set the starting price use " +
			"price=x. To set multiplier mult=x. Cannot autoConfigure if any ranks or mines are defined, " +
			"but 'force' will attempt it but at your risk. " +
			"Default values [full price=50000 mult=1.5]", 
			onlyPlayers = false, permissions = "ranks.set")
	public void autoConfigureRanks(CommandSender sender, 
			@Wildcard(join=true)
			@Arg(name = "options", 
				description = "Options: [full ranks mines price=x mult=x force]", 
				def = "full") String options
			) {
		
		boolean force = options != null && options.contains( "force" );
		if ( force ) {
			options = options.replace( "force", "" );
		}
		
		int rankCount = PrisonRanks.getInstance().getRankManager().getRanks().size();
		int mineCount = Prison.get().getPlatform().getModuleElementCount( ModuleElementType.MINE );
		
		if (!force && ( rankCount > 0 || mineCount > 0 ) ) {
			String message = String.format( "&3You should not run &7/ranks autoConfigure &3 with any " +
					"ranks or mines already setup. Rank count = &7%d&3. Mine count = &7%d " +
					"Add the option 'force' to force it to run.  If there is a conflict with a " +
					"preexisting rank or mine, then they will be skipped with no configuration.", 
					rankCount, mineCount );
			Output.get().logWarn( message );
			return;
		}
		
		if ( force ) {
			String message = String.format( "&aWarning! &3Running autoConfigure with &7force&3 enabled. " +
					"Not responsible if mines or ranks collide. ");
			Output.get().logWarn( message );
		}
		
		String optionHelp = "&b[&7full ranks mines price=&dx &7mult=&dx&b]";
		boolean ranks = false;
		boolean mines = false;
		double startingPrice = 50000;
		double percentMultipler = 1.5;
		
		options = (options == null ? "" : options.replaceAll( "/s*", " " ));
		if ( options.trim().length() == 0 ) {
			Output.get().sendError(sender,
					"&3Invalid options.  Use %s&3.  Was: &3%s",
					optionHelp, options );
			return;
		}

		if ( options.contains( "full" ) ) {
			ranks = true;
			mines = true;
			options = options.replace( "full", "" );
		}
		if ( options.contains( "ranks" ) ) {
			ranks = true;
			options = options.replace( "ranks", "" );
		}
		if ( options.contains( "mines" ) ) {
			mines = true;
			options = options.replace( "mines", "" );
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
			Output.get().sendError(sender,
	                "Invalid options..  Use either %s&3.  Was: [%s]",
	                optionHelp, 
	                options == null ? "null" : options );
			return;
		}
		
		TreeMap<String, RegisteredPluginsData> plugins = 
								Prison.get().getPrisonCommands().getRegisteredPluginData();
		
//
        String permCmdAdd = null;
        String permCmdDel = null;
        String perm1 = "mines.";
        String perm2 = "mines.tp.";
        
        if ( plugins.containsKey("LuckPerms") ){
        	permCmdAdd = "lp user {player} permission set ";
        	permCmdDel = "lp user {player} permission unset ";
        } 
        else if ( plugins.containsKey("PermissionsEx") ){
        	permCmdAdd = "pex user {player} add ";
        	permCmdDel = "pex user {player} add -";
        } 
        else if ( plugins.containsKey("UltraPermissions") ){
        	permCmdAdd = "upc addplayerpermission {player} ";
        	permCmdDel = "upc removeplayerpermission {player} ";
        } 
        else if ( plugins.containsKey("GroupManager") ){
        	permCmdAdd = "manuaddp {player} ";
        	permCmdDel = "manudelp {player} ";
        } 
        else if ( plugins.containsKey("zPermissions") ){
        	permCmdAdd = "permissions player {player} set ";
        	permCmdDel = "permissions player {player} unset ";
        } 
        else if ( plugins.containsKey("PowerfulPerms") ){
        	permCmdAdd = "pp user {player} add ";
        	permCmdAdd = "pp user {player} remove ";
        }


		
		int countRanks = 0;
		int countRankCmds = 0;
		int countMines = 0;
		int countLinked = 0;
		
		if ( ranks ) {
			
	        int colorID = 1;
	        double price = 0;

	        
	        for ( char cRank = 'A'; cRank <= 'Z'; cRank++) {
	        	String rankName = Character.toString( cRank );
	        	String tag = "&7[&" + Integer.toHexString((colorID++ % 15) + 1) + rankName + "&7]";
	        	
	        	char cRankNext = (char) (cRank + 1);
	        	String rankNameNext = Character.toString( cRankNext );
	        	
	        	if ( createRank(sender, rankName, price, "default", tag) ) {
	        		countRanks++;
	        		
	        		if ( permCmdAdd != null ) {
	        			getRankCommandCommands().commandAdd( sender, rankName, permCmdAdd + perm1 + rankName.toLowerCase());
	        			countRankCmds++;
	        			getRankCommandCommands().commandAdd( sender, rankName, permCmdAdd + perm2 + rankName.toLowerCase());
	        			countRankCmds++;
	        			
	        			if ( cRankNext <= 'Z' ) {
	        				getRankCommandCommands().commandAdd( sender, rankName, permCmdDel + perm1 + rankNameNext.toLowerCase());
	        				countRankCmds++;
	        				getRankCommandCommands().commandAdd( sender, rankName, permCmdDel + perm2 + rankNameNext.toLowerCase());
	        				countRankCmds++;
	        			}
	        			
	        		}
	        		
	        		if ( mines ) {

	        			// Creates a virtual mine:
	        			ModuleElement mine = Prison.get().getPlatform().createModuleElement( 
	        					sender, ModuleElementType.MINE, rankName, tag );
	        			
	        			if ( mine != null ) {
	        				countMines++;
	        				
	        				// Links the virtual mine to generated rank:
	        				if ( Prison.get().getPlatform().linkModuleElements( mine, ModuleElementType.RANK, rankName ) ) {
	        					countLinked++;
	        				}
	        			}
	        		}
	        	}
	        	else {
	        		String message = String.format( "&aWarning! &3Rank &7%s &3already exists and is being skipped " +
	        				"along with generating the mine if enabled, along with all of the other features. ", cRank );
	    			Output.get().logWarn( message );
	        	}

	            if (price == 0){
	                price += startingPrice;
	            } else {
	                price *= percentMultipler;
	            }

	        }
		}
		
		// If mines were created, go ahead and auto assign blocks to the mines:
		if ( countMines > 0 ) {
			Prison.get().getPlatform().autoCreateMineBlockAssignment();
		}
		
		if ( countMines > 0 ) {
			Prison.get().getPlatform().autoCreateMineLinerAssignment();
		}
		
		if ( countRanks == 0 ) {
			Output.get().logInfo( "Ranks autoConfigure: No ranks were created.");
		}
		else {
			Output.get().logInfo( "Ranks autoConfigure: %d ranks were created.", countRanks);
			
			if ( countRankCmds == 0 ) {
				Output.get().logInfo( "Ranks autoConfigure: No rank commandss were created.");
			}
			else {
				Output.get().logInfo( "Ranks autoConfigure: %d rank commands were created.", countRanks);
				Output.get().logInfo( "Ranks autoConfigure: The permission %s<rankName> and " +
						"%s<rankName> was " +
						"created for each rank. Make sure you add every permission to your " +
						"permission plugin or they may not work. ",
						 perm1, perm2 );
			}
		}
		
		if ( countMines == 0 ) {
			Output.get().logInfo( "Ranks autoConfigure: No mines were created.");
		}
		else {
			Output.get().logInfo( "Ranks autoConfigure: %d mines were created.", countMines);
			
			if ( countLinked == 0 ) {
				Output.get().logInfo( "Ranks autoConfigure: No mines and no ranks were linked.");
			}
			else {
				Output.get().logInfo( "Ranks autoConfigure: %d ranks and mines were linked.", countLinked);
			}
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
            Output.get().sendError(sender, "The rank '%s' does not exist.", rankName);
            return;
        }

        if (PrisonRanks.getInstance().getDefaultLadder().getRanks().contains( rank ) 
            && PrisonRanks.getInstance().getDefaultLadder().getRanks().size() == 1) {
            Output.get().sendError(sender,
                "You can't remove this rank because it's the only rank in the default ladder.");
            return;
        }

        if ( PrisonRanks.getInstance().getRankManager().removeRank(rank) ) {
        	
            Output.get().sendInfo(sender, "The rank '%s' has been removed successfully.", rankName);
        } else {
            Output.get()
                .sendError(sender, "The rank '%s' could not be deleted due to an error.", rankName);
        }
    }

    @Command(identifier = "ranks list", description = "Lists all the ranks on the server.", 
    							onlyPlayers = false, altPermissions = "ranks.list"
    							)
    public void listRanks(CommandSender sender,
        @Arg(name = "ladderName", def = "default") String ladderName) {

    	boolean hasPerm = sender.hasPermission("ranks.list") ||
    					sender.isOp();
    	
        RankLadder ladder =
        			PrisonRanks.getInstance().getLadderManager().getLadder(ladderName);

        if ( ladder == null ) {
            Output.get().sendError(sender, "The ladder '%s' doesn't exist.", ladderName);
            return;
        }

        Rank rank = ladder.getLowestRank().orElse( null );

//        Rank rank = null;
//        for (Rank pRank : ladder.getPositionRanks()) {
//            Optional<Rank> rankOptional = ladder.getByPosition(pRank.getPosition());
//            if (rankOptional.isPresent()) {
//            	rank = rankOptional.get();
//            	break;
//            }
//        }
        

        String rankHeader = "Ranks" + 
        				( hasPerm || !hasPerm && !ladderName.equalsIgnoreCase( "default" ) ?
        						" in " + ladderName : "");
        ChatDisplay display = new ChatDisplay( rankHeader );
        
        if ( hasPerm ) {
        	display.addText("&7Click on a rank's name to view more info.");
        }
        

        BulletedListComponent.BulletedListBuilder builder =
            new BulletedListComponent.BulletedListBuilder();
        
        boolean first = true;
        while ( rank != null ) {
        	
            boolean defaultRank = ("default".equalsIgnoreCase( ladderName ) && first);
            
            String textRankName = ( hasPerm ?
            							String.format( "&3%s " , rank.getName() )
            							: "");
            String textCmdCount = ( hasPerm ? 
            							String.format( " &7- Commands: &3%d", rank.getRankUpCommands().size())
            							: "" );
            
            String text =
                String.format("%s &9[&3%s&9] &7- %s&7%s%s%s", 
                			textRankName, rank.getTag(), 
                			(defaultRank ? "&b(&9Default&b) &7- " : ""),
                			Text.numberToDollars(rank.getCost()),
                			(rank.getCurrency() == null ? "" : " &3Currency: &2" + rank.getCurrency()),
                			textCmdCount );
            
            String rankName = rank.getName();
            if ( rankName.contains( "&" ) ) {
            	rankName = rankName.replace( "&", "-" );
            }
            FancyMessage msg = null;
            if ( hasPerm ) {
            	msg = new FancyMessage(text).command("/ranks info " + rankName)
            			.tooltip("&7Click to view info.");
            }
            else {
            	msg = new FancyMessage(text);
            }
            
            builder.add(msg);
        	
        	rank = rank.getRankNext();
        	first = false;
        }
        
//        for (RankLadder.PositionRank pos : ranks) {
//            Optional<Rank> rankOptional = ladder.get().getByPosition(pos.getPosition());
//            if (!rankOptional.isPresent()) {
//                continue; // Skip it
//            }
//            Rank rank = rankOptional.get();
//
//            boolean defaultRank = ("default".equalsIgnoreCase( ladderName ) && first);
//            
//            String text =
//                String.format("&3%s &9[&3%s&9] &7- %s&7%s &7- Commands: &3%d", 
//                			rank.name, rank.tag, 
//                			(defaultRank ? "&b(&9Default&b) &7-" : ""),
//                			Text.numberToDollars(rank.cost),
//                			rank.rankUpCommands.size());
//            FancyMessage msg = new FancyMessage(text).command("/ranks info " + rank.name)
//                .tooltip("&7Click to view info.");
//            builder.add(msg);
//            first = false;
//        }

        display.addComponent(builder.build());
        
        if ( hasPerm ) {
        	display.addComponent(new FancyMessageComponent(
        			new FancyMessage("&7[&a+&7] Add").suggest("/ranks create ")
        			.tooltip("&7Create a new rank.")));
        	
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
        		FancyMessage msg = new FancyMessage("&8You may also try ");
        		int i = 0;
        		for (String other : others) {
        			i++;
        			if (i == others.size() && others.size() > 1) {
        				msg.then(" &8and ");
        			}
        			msg.then("&7" + other).tooltip("&7Click to view.").command(other);
        			msg.then(i == others.size() ? "&8." : "&8,");
        		}
        		display.addComponent(new FancyMessageComponent(msg));
        	}
        	
        }

        display.send(sender);

    }

    @Command(identifier = "ranks info", description = "Information about a rank.", 
    							onlyPlayers = false, permissions = "ranks.info", 
    							altPermissions = "ranks.admin" )
    public void infoCmd(CommandSender sender, @Arg(name = "rankName") String rankName) {
    	
        Rank rank = PrisonRanks.getInstance().getRankManager().getRank(rankName);
        if ( rank == null ) {
//        	rankOpt = PrisonRanks.getInstance().getRankManager().getRankEscaped(rankName);
//        	if (!rankOpt.isPresent()) {
        		Output.get().sendError(sender, "The rank '%s' doesn't exist.", rankName);
        		return;
//        	}
        }

        List<RankLadder> ladders =
            PrisonRanks.getInstance().getLadderManager().getLaddersWithRank(rank.getId());

        ChatDisplay display = new ChatDisplay("Rank " + rank.getTag());

        display.addText("&3Rank Name: &7%s", rank.getName());
        display.addText("&3Rank Tag:  &7%s  &3Raw: &7\\Q%s\\E", rank.getTag(), rank.getTag());
        
        // (I know this is confusing) Ex. Ladder(s): default, test, and test2.
        display.addText("&3%s: &7%s", Text.pluralize("Ladder", ladders.size()),
            Text.implodeCommaAndDot(
                ladders.stream().map(rankLadder -> rankLadder.getName()).collect(Collectors.toList())));
        
        if ( rank.getMines().size() == 0 ) {
        	display.addText( "&3This rank is not linked to any mines" );
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
        	
        	display.addText( "&3Mines linked to this rank: %s", sb.toString() );
        }

        display.addText("&3Cost: &7%s", Text.numberToDollars(rank.getCost()));
        
        display.addText("&3Currency: &7<&a%s&7>", (rank.getCurrency() == null ? "&cdefault" : rank.getCurrency()) );
        
        List<RankPlayer> players =
        		PrisonRanks.getInstance().getPlayerManager().getPlayers().stream()
        		.filter(rankPlayer -> rankPlayer.getLadderRanks().values().contains(rank))
        		.collect(Collectors.toList());
        display.addText("&7There %s &3%s players &7with this rank.", 
        				(players.size() == 1 ? "is": "are"), 
        				players.size() + "");

        if (sender.hasPermission("ranks.admin")) {
            // This is admin-exclusive content

            display.addText("&8[Admin Only]");
            display.addText("&6Rank ID: &7%s", rank.getId());

            FancyMessage del =
                new FancyMessage("&7[&c-&7] Delete").command("/ranks delete " + rank.getName())
                    .tooltip("&7Click to delete this rank.\n&cYou may not reverse this action.");
            display.addComponent(new FancyMessageComponent(del));
        }

        display.send(sender);
    }

    // set commands
    @Command(identifier = "ranks set cost", description = "Modifies a ranks cost", 
    							onlyPlayers = false, permissions = "ranks.set")
    public void setCost(CommandSender sender, 
    		@Arg(name = "rankName") String rankName, 
    		@Arg(name = "cost", description = "The cost of this rank.") double cost){
        Rank rank = PrisonRanks.getInstance().getRankManager().getRank(rankName);
        if ( rank == null ) {
            Output.get().sendError(sender, "The rank '%s' doesn't exist.", rankName);
            return;
        }
        
        rank.setCost( cost );
        
        // Save the rank
//        try {
            PrisonRanks.getInstance().getRankManager().saveRank(rank);

            Output.get().sendInfo(sender,"Successfully set the cost of rank '%s' to "+cost,rankName);
//        } catch (IOException e) {
//            Output.get().sendError(sender,
//                "The rank could not be saved to disk. The change in rank cost has not been saved. Check the console for details.");
//            Output.get().logError("Rank could not be written to disk (setCost).", e);
//        }
    }
    
    // set commands
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
    		Output.get().sendError(sender, "The rank '%s' doesn't exist.", rankName);
    		return;
    	}
    	
    	
    	if ( currency == null || currency.trim().length() == 0 ) {
    		Output.get().sendError(sender, "A currency name must be specified, or must be " +
    				"'none'. '%s' is invalid.", currency);
    		return;
    	}
    	
    	if ( "none".equalsIgnoreCase( currency ) && rank.getCurrency() == null ) {

    		Output.get().sendInfo(sender,"The rank '%s' does not have a currency that " +
    								"can be cleared. ", rankName);
    		
    	}
    	else if ( "none".equalsIgnoreCase( currency ) ) {
    		rank.setCurrency( null );
    		
    		PrisonRanks.getInstance().getRankManager().saveRank(rank);
    		
    		Output.get().sendInfo(sender,"Successfully cleared the currency for the rank '%s'. " +
    				"This rank no longer has a custom currency. ", rankName);
    		
    	}
    	else {
    		
    		EconomyCurrencyIntegration currencyEcon = PrisonAPI.getIntegrationManager()
    				.getEconomyForCurrency( currency );
    		if ( currencyEcon == null ) {
    			Output.get().sendError(sender, "No active economy supports the currency named '%s'.", currency);
    			return;
    		}
    		
    		rank.setCurrency( currency );
    		
    		PrisonRanks.getInstance().getRankManager().saveRank(rank);
    		
    		Output.get().sendInfo(sender,"Successfully set the currency for the rank '%s' to %s", rankName, currency);
    	}
    	
    }

    @Command(identifier = "ranks set tag", description = "Modifies a ranks tag", 
    							onlyPlayers = false, permissions = "ranks.set")
    public void setTag(CommandSender sender, 
    				@Arg(name = "rankName") String rankName, 
    				@Wildcard(join=true)
    					@Arg(name = "tag", description = "Tag value for the Rank. Use [null] to remove.") String tag){
    	
        Rank rank = PrisonRanks.getInstance().getRankManager().getRank(rankName);
        if ( rank == null ) {
            Output.get().sendError(sender, "The rank '%s' doesn't exist.", rankName);
            return;
        }
        

        if ( tag == null || tag.trim().length() == 0 ) {
        	sender.sendMessage( "&cTag name must be a valid value. To remove use a value of &anull&c." );
        	return;
        }
        
        if ( tag.equalsIgnoreCase( "null" ) ) {
        	tag = null;
        }


        if ( tag == null && rank.getTag() == null || 
        		rank.getTag() != null &&
        		rank.getTag().equalsIgnoreCase( tag )) {
        
        	sender.sendMessage( "&cThe new tag name is the same as what it was. No change was made." );
        	return;
        }

        rank.setTag( tag );
        
        PrisonRanks.getInstance().getRankManager().saveRank(rank);

        if ( tag == null ) {
        	sender.sendMessage( 
        			String.format( "&cThe tag name was cleared for the rank %s.", 
        					rank.getName() ) );
        }
        else {
        	sender.sendMessage( 
        			String.format( "&cThe tag name was changed to %s for the rank %s.", 
        					tag, rank.getName() ) );
        }
    }
    
    

    @Command(identifier = "ranks perms list", description = "Lists rank permissions", 
    							onlyPlayers = false, permissions = "ranks.set")
    public void rankPermsList(CommandSender sender, 
    				@Arg(name = "rankName") String rankName
    			){
    	sender.sendMessage( "&cWarning: &3This feature is not yet functional." );
	  
        Rank rank = PrisonRanks.getInstance().getRankManager().getRank(rankName);
        if ( rank == null ) {
            Output.get().sendError(sender, "The rank '%s' doesn't exist.", rankName);
            return;
        }
        
        
        if ( rank.getPermissions() == null ||rank.getPermissions().size() == 0 && 
        		rank.getPermissionGroups() == null && rank.getPermissionGroups().size() == 0 ) {
        	
            Output.get().sendInfo(sender, "The Rank '%s' contains no permissions or " +
            		"permission groups.", rank.getName());
            return;
        }

        RankLadder ladder = rank.getLadder();

        ChatDisplay display = new ChatDisplay("Rank Permissions and Groups for " + rank.getName());
        display.addText("&8Click a Permission to remove it.");
        BulletedListComponent.BulletedListBuilder builder =
        									new BulletedListComponent.BulletedListBuilder();

        listLadderPerms( ladder, builder );
        
        int rowNumber = 1;
        
        if ( rank.getPermissions().size() > 0 ) {
        	builder.add( "&7Permissions:" );
        }
        for (String perm : rank.getPermissions() ) {
        	
        	RowComponent row = new RowComponent();
        	
        	row.addTextComponent( "  &3Row: &d%d  ", rowNumber++ );
        	
        	FancyMessage msgPermission = new FancyMessage( String.format( "&7%s ", perm ) )
        			.command( "/ranks perms edit " + rank.getName() + " " + rowNumber + " " )
        			.tooltip("Permission - Click to Edit");
        	row.addFancy( msgPermission );
        	
        	
        	FancyMessage msgRemove = new FancyMessage( String.format( "  &cRemove " ) )
        			.command( "/ranks perms remove " + rank.getName() + " " + rowNumber + " " )
        			.tooltip("Remove Permission - Click to Delete");
        	row.addFancy( msgRemove );
        	
            builder.add( row );
        }

        if ( rank.getPermissionGroups().size() > 0 ) {
        	builder.add( "&7Permission Groups:" );
        }
        for (String permGroup : rank.getPermissionGroups() ) {
        	
        	RowComponent row = new RowComponent();
        	
        	row.addTextComponent( "  &3Row: &d%d  ", rowNumber++ );
        	
        	FancyMessage msgPermission = new FancyMessage( String.format( "&7%s ", permGroup ) )
        			.command( "/ranks perms edit " + rank.getName() + " " + rowNumber + " " )
        			.tooltip("Permission Group - Click to Edit");
        	row.addFancy( msgPermission );
        	
        	
        	FancyMessage msgRemove = new FancyMessage( String.format( "  &cRemove " ) )
        			.command( "/ranks perms remove " + rank.getName() + " " + rowNumber + " " )
        			.tooltip("Remove Permission Group - Click to Delete");
        	row.addFancy( msgRemove );
        	
            builder.add( row );
        }

        
        display.addComponent(builder.build());
        display.addComponent(new FancyMessageComponent(
            new FancyMessage("&7[&a+&7] Add Permission")
            			.suggest("/ranks perms addPerm " + rank.getName() + " [perm] /")
                .tooltip("&7Add a new Permission.")));
        display.addComponent(new FancyMessageComponent(
        		new FancyMessage("&7[&a+&7] Add Permission Group")
        				.suggest("/ranks perms addPermGroup " + rank.getName() + " [permGroup] /")
        		.tooltip("&7Add a new Permission Group.")));

        display.send(sender);

    }
    
    private void listLadderPerms( RankLadder ladder, 
    					BulletedListComponent.BulletedListBuilder builder ) {
    	
        if ( ladder.getPermissions().size() > 0 ) {
          	builder.add( "&3Ladder &7%s &3Permissions:", ladder.getName() );
        }
        for (String perm : ladder.getPermissions() ) {
          	
          	RowComponent row = new RowComponent();
          	
          	row.addTextComponent( "    " );
          	
          	FancyMessage msgPermission = new FancyMessage( String.format( "&7%s ", perm ) )
          			.command( "/ranks ladder perms list " + ladder.getName() )
          			.tooltip("Ladder Permission - Click to List Ladder");
          	row.addFancy( msgPermission );
          	
            builder.add( row );
        }

        if ( ladder.getPermissionGroups().size() > 0 ) {
          	builder.add( "&3Ladder &7%s &3Permission Groups:", ladder.getName() );
        }
        for (String permGroup : ladder.getPermissionGroups() ) {
          	
          	RowComponent row = new RowComponent();
          	
          	row.addTextComponent( "    " );
          	
          	FancyMessage msgPermission = new FancyMessage( String.format( "&7%s ", permGroup ) )
          			.command( "/ranks ladder perms list " + ladder.getName() )
          			.tooltip("Ladder Permission Group - Click to List Ladder");
          	row.addFancy( msgPermission );
          	
          	builder.add( row );
        }

    }
    

    @Command(identifier = "ranks perms addPerm", 
  		  		description = "Add a ladder permission. Valid placeholder: {rank}.", 
  		  onlyPlayers = false, permissions = "ranks.set")
    public void rankPermsAddPerm(CommandSender sender, 
  		  @Arg(name = "rankName", 
  						description = "Rank name to add the permission to.") String rankName,
  		  @Arg(name = "permission", description = "Permission") String permission
  		  ){
    	
    	sender.sendMessage( "&cWarning: &3This feature is not yet functional." );
  	  
        Rank rank = PrisonRanks.getInstance().getRankManager().getRank(rankName);
        if ( rank == null ) {
            Output.get().sendError(sender, "The rank '%s' doesn't exist.", rankName);
            return;
        }
        

        if ( permission == null || permission.trim().isEmpty() ) {
        	
            Output.get().sendInfo(sender, "&3The &7permission &3parameter is required." );
            return;
        }
        
        
        if ( rank.hasPermission( permission ) ) {
      	  
      	  Output.get().sendInfo(sender, "&3The permission &7%s &3already exists.", permission );
      	  return;
        }
        
        rank.getPermissions().add( permission );
        
        
        PrisonRanks.getInstance().getRankManager().saveRank(rank);

        Output.get().sendInfo(sender, "&3The permission &7%s &3was successfully added " +
        		"to the rank &7%s&3.", permission, rank.getName() );
        
        rankPermsList( sender, rank.getName() );
    }
    
    
    @Command(identifier = "ranks perms addGroup", 
    		description = "Add a ladder permission. Valid placeholder: {rank}.", 
    		onlyPlayers = false, permissions = "ranks.set")
    public void rankPermsAddPermGroup(CommandSender sender, 
    		@Arg(name = "rankName", 
    					description = "Rank name to add the permission to.") String rankName,
    		@Arg(name = "permissionGroup", description = "Permission Group") String permissionGroup
    	){
    	
    	sender.sendMessage( "&cWarning: &3This feature is not yet functional." );
    	
    	Rank rank = PrisonRanks.getInstance().getRankManager().getRank(rankName);
    	if ( rank == null ) {
    		Output.get().sendError(sender, "The rank '%s' doesn't exist.", rankName);
    		return;
    	}
    	
    	
    	if ( permissionGroup == null || permissionGroup.trim().isEmpty() ) {
    		
    		Output.get().sendInfo(sender, "&3The &7permission group &3parameter is required." );
    		return;
    	}
    	
    	
    	if ( rank.hasPermissionGroup( permissionGroup ) ) {
    		
    		Output.get().sendInfo(sender, "&3The permission Group &7%s &3already exists.", permissionGroup );
    		return;
    	}
    	
    	rank.getPermissionGroups().add( permissionGroup );
    	
    	
    	PrisonRanks.getInstance().getRankManager().saveRank(rank);
    	
    	Output.get().sendInfo(sender, "&3The permission group &7%s &3was successfully added " +
    			"to the rank &7%s&3.", permissionGroup, rank.getName() );
    	
    	rankPermsList( sender, rank.getName() );
    }
    


    @Command(identifier = "ranks perms remove", description = "Remove rank permissions", 
  		  onlyPlayers = false, permissions = "ranks.set")
    public void rankPermsRemove(CommandSender sender, 
    		@Arg(name = "rankName", def = "default", 
  						description = "Rank name to list the permissions.") String rankName,
  			@Arg(name = "row") Integer row
  		  ){
    	sender.sendMessage( "&cWarning: &3This feature is not yet functional." );
  	  
       	
    	Rank rank = PrisonRanks.getInstance().getRankManager().getRank(rankName);
    	if ( rank == null ) {
    		Output.get().sendError(sender, "The rank '%s' doesn't exist.", rankName);
    		return;
    	}
    	
  	  
        boolean dirty = false;
        String removedPerm = "";
        boolean permGroup = false;
        
        if ( row == null || row <= 0 ) {
        	sender.sendMessage( 
        			String.format("&7Please provide a valid row number greater than zero. " +
        					"Was row=[&b%d&7]",
        					(row == null ? "null" : row) ));
        	return;        	
        }
        
        if ( row <= rank.getPermissions().size() ) {
      	  removedPerm = rank.getPermissions().remove( row - 1 );
      	  dirty = true;
        }
        else {
      	  // Remove from row the size of permissions so the row will align to the permissionGroups.
      	  row -= rank.getPermissions().size();
      	  
      	  if ( row <= rank.getPermissionGroups().size() ) {
      		  
      		  removedPerm = rank.getPermissions().remove( row - 1 );
      		  dirty = true;
      		  permGroup = true;
      	  }
        }

        if ( dirty ) {
        	PrisonRanks.getInstance().getRankManager().saveRank(rank);
        	
        	Output.get().sendInfo(sender, "&3The permission%s &7%s &3was successfully removed " +
        			"to the rank &7%s&3.",
        			( permGroup ? " group" : "" ),
        			removedPerm, rank.getName() );
           
        }
        else {
      	  Output.get().sendInfo(sender, "&3The permission on row &7%s &3was unable to be removed " +
      	  		"from the &7%s &3rank. " +
    	  			"Is that a valid row number?",
    	  			Integer.toString( row ), rank.getName() );
        }
    }
      
    
    
    
    @Command(identifier = "ranks player", description = "Shows a player their rank", 
    		onlyPlayers = false, altPermissions = "ranks.admin" )
    public void rankPlayer(CommandSender sender,
    			@Arg(name = "player", def = "", description = "Player name") String playerName
//    			@Arg(name = "options", def = "", description = "Options [allPerms]") String options
    			){
    	
    	Player player = getPlayer( sender, playerName );
    	
    	if (player == null) {
    		sender.sendMessage( "&3You must be a player in the game to run this command, and/or the player must be online." );
    		return;
    	}

    	PlayerManager pm = PrisonRanks.getInstance().getPlayerManager();
		RankPlayer rankPlayer = pm.getPlayer(player.getUUID(), player.getName());
		
		if ( rankPlayer != null ) {
			DecimalFormat dFmt = new DecimalFormat("#,##0.00");
			
			// Collect all currencies in the default ladder:
			Set<String> currencies = new LinkedHashSet<>();
			LadderManager lm = PrisonRanks.getInstance().getLadderManager();
			
			for ( Rank rank : lm.getLadder( "default" ).getRanks() ) {
				if ( rank.getCurrency() != null && !currencies.contains( rank.getCurrency() )) {
					currencies.add( rank.getCurrency() );
				}
			}
			
			
			Map<RankLadder, Rank> rankLadders = rankPlayer.getLadderRanks();
			
			for ( RankLadder rankLadder : rankLadders.keySet() )
			{
				Rank rank = rankLadders.get( rankLadder );
				Rank nextRank = rank.getRankNext();
				
				String messageRank = String.format("&c%s&7: Ladder: &b%s  &7Current Rank: &b%s", 
						player.getDisplayName(), 
						rankLadder.getName(),
						rank.getName() );
				
				if ( nextRank == null ) {
					messageRank += "  It's the highest rank!";
				} else {
					messageRank += String.format("  &7Next rank: &b%s&7 &c$&b%s", 
							nextRank.getName(), 
							dFmt.format( nextRank.getCost() ));

					if ( nextRank.getCurrency() != null ) {
						messageRank += String.format("  &7Currency: &2%s", 
								nextRank.getCurrency());
					}
				}
				
				sendToPlayerAndConsole( sender, messageRank );
			}
			
			// Print out the player's balances: 

			// The default currency first:
			double balance = rankPlayer.getBalance();
			String message = String.format( "&7The current balance for &b%s &7is &b%s", 
					player.getName(), dFmt.format( balance ) );
			sendToPlayerAndConsole( sender, message );
			
			for ( String currency : currencies ) {
				double balanceCurrency = rankPlayer.getBalance( currency );
				String messageCurrency = String.format( "&7The current balance for &b%s &7is &b%s &2%s", 
						player.getName(), dFmt.format( balanceCurrency ), currency );
				sendToPlayerAndConsole( sender, messageCurrency );

			}
			
			boolean isOp = player.isOp();
			boolean isPlayer = player.isPlayer();
			boolean isOnline = player.isOnline();
			
			boolean isPrisonPlayer = (player instanceof Player);
			boolean isPrisonOfflineMcPlayer = (player instanceof OfflineMcPlayer);

			if ( !isOnline ) {
				sendToPlayerAndConsole( sender, "  &7Notice: &3The player is offline so permissions are " +
														"not available nor accurate." );
			}
			
			double sellallMultiplier = player.getSellAllMultiplier();
			DecimalFormat pFmt = new DecimalFormat("#,##0.0000");
			String messageCurrency = String.format( "&7  Sellall multiplier: &b%s %s", 
					pFmt.format( sellallMultiplier ), 
					(!isOnline ? "  &5(&2Not Accurate&5)" : "") );
			sendToPlayerAndConsole( sender, messageCurrency );

			
			
			if ( sender.hasPermission("ranks.admin") ) {
	            // This is admin-exclusive content

				sendToPlayerAndConsole( sender, "&8[Admin Only]" );
				
				if ( rankPlayer.getNames().size() > 1 ) {
					
					sendToPlayerAndConsole( sender, "  &7Past Player Names and Date Changed:" );
					
					for ( RankPlayerName rpn : rankPlayer.getNames() ) {
						
						sendToPlayerAndConsole( sender, "    &b" + rpn.toString() );
					}
				}

				
//				if ( options != null && options.toLowerCase().contains( "allperms" ) ) 
				{
					
					
					sendToPlayerAndConsole( sender, String.format( "  &7Player Perms:  %s%s%s%s", 
								(isOp ? " &cOP&7" : ""),
								(isPlayer ? " &3Player&7" : ""),
								(isOnline ? " &3Online&7" : " &3Offline&7"),
								(isPrisonOfflineMcPlayer ? " &3PrisonOfflinePlayer&7" : 
									(isPrisonPlayer ? " &3PrisonPlayer&7" : ""))
							) );
					
					if ( !isOnline ) {
						sendToPlayerAndConsole( sender, "  &7Player is offline so perms may not be available." );
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
			
		} else {
			sender.sendMessage( "&3No ranks found for &c" + player.getDisplayName() );
		}
    }

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

	private void sendToPlayerAndConsole( CommandSender sender, String messageRank )
	{
		// If not a console user then send the message to the sender, other wise if a console
		// user then they will see duplicate messages:
		if ( sender.getName() != null && !sender.getName().equalsIgnoreCase( "console" ) ) {
			sender.sendMessage( messageRank );
		}
		
		// log the rank. There was one issue with the ranks suddenly being changed so this
		// will help document what ranks were.
		Output.get().logInfo( messageRank );
	}
 
    
    @Command(identifier = "ranks players", description = "Shows all ranks with player counts", onlyPlayers = false)
    public void rankPlayers(CommandSender sender,
    			@Arg(name = "ladderName", def = "all", description = "Ladder Name [all, none, LadderName]") String ladderName,
    			@Arg(name = "action", def = "players", description = "List type [players, all]") String action){

    	
    	if ( !ladderName.equalsIgnoreCase( "all" ) && 
    			PrisonRanks.getInstance().getLadderManager().getLadder( ladderName ) == null ) {
    		Output.get().sendError(sender, "The ladder '%s' doesn't exist, or was not ALL.", ladderName);
    		return;
    	}
    	
    	
    	if ( !action.equalsIgnoreCase( "players" ) && !action.equalsIgnoreCase( "all" ) ) {
    		Output.get().sendError(sender, "The action '%s' is invalid. [players, all]", action);
    		
    		return;
    	}
    	
    	boolean includeAll = action.equalsIgnoreCase( "all" );
    	PrisonRanks.getInstance().getRankManager().ranksByLadders( sender, ladderName, includeAll );
    	
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
