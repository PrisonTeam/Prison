package tech.mcprison.prison.ranks.commands;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import tech.mcprison.prison.Prison;
import tech.mcprison.prison.PrisonAPI;
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
import tech.mcprison.prison.output.Output;
import tech.mcprison.prison.ranks.PrisonRanks;
import tech.mcprison.prison.ranks.data.Rank;
import tech.mcprison.prison.ranks.data.RankLadder;
import tech.mcprison.prison.ranks.data.RankPlayer;
import tech.mcprison.prison.ranks.data.RankPlayerName;
import tech.mcprison.prison.ranks.managers.LadderManager;
import tech.mcprison.prison.ranks.managers.PlayerManager;
import tech.mcprison.prison.ranks.managers.RankManager.RanksByLadderOptions;
import tech.mcprison.prison.util.Text;

/**
 * @author Faizaan A. Datoo
 */
public class RanksCommands
			extends RanksCommandsMessages {
	
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
    
//    @Command(identifier = "ranks perms", 
//    		onlyPlayers = false, permissions = "prison.commands")
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
            
            // Tell the player the good news!
            rankCreatedSuccessfullyMsg( sender, name, ladder, tag );
        } 
        else {
        	errorCouldNotSaveLadderMsg( sender, rankLadder.getName() );
        }

        return success;
    }

	
	@Command(identifier = "ranks autoConfigure", description = "Auto configures Ranks and Mines using " +
			"single letters A through Z for both the rank and mine names. If both ranks and mines are " +
			"generated, they will also be linked together automatically. To set the starting price use " +
			"price=x. To set multiplier mult=x. Cannot autoConfigure if any ranks or mines are defined, " +
			"but 'force' will attempt it but at your risk and will replace all blocks in preexisting " +
			"mines. To keep preexisting blocks, use 'forceKeepBlocks' with the 'force' option. " +
			"Default values [full price=50000 mult=1.5]", 
			onlyPlayers = false, permissions = "ranks.set")
	public void autoConfigureRanks(CommandSender sender, 
			@Wildcard(join=true)
			@Arg(name = "options", 
				description = "Options: [full ranks mines price=x mult=x force forceKeepBlocks]", 
				def = "full") String options
			) {
		
		boolean forceKeepBlocks = options != null && options.contains( "forceKeepBlocks" );
		if ( forceKeepBlocks ) {
			options = options.replace( "forceKeepBlocks", "" ).trim();
		}
		
		boolean force = options != null && options.contains( "force" );
		if ( force ) {
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
		
		String optionHelp = "&b[&7full ranks mines price=&dx &7mult=&dx &7force forceKeepBlocks&b]";
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
		
		if ( ranks ) {
			
	        int colorID = 1;
	        double price = 0;

	        String firstRankName = null;
	        
	        for ( char cRank = 'A'; cRank <= 'Z'; cRank++) {
	        	String rankName = Character.toString( cRank );
	        	String tag = "&7[&" + Integer.toHexString((colorID++ % 15) + 1) + rankName + "&7]";
	        	
	        	if ( firstRankName == null ) {
	        		firstRankName = rankName;
	        	}
	        	
//	        	char cRankNext = (char) (cRank + 1);
//	        	String rankNameNext = Character.toString( cRankNext );
	        	
	        	boolean forceRank = force && PrisonRanks.getInstance().getRankManager().getRank( rankName ) != null;
	        	if ( forceRank ||
	        			createRank(sender, rankName, price, "default", tag) ) {
	        		
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
		
		// If mines were created, go ahead and auto assign blocks to the mines:
		if ( countMines > 0 || countMinesForced > 0 ) {
			Prison.get().getPlatform().autoCreateMineBlockAssignment( forceKeepBlocks );
			Prison.get().getPlatform().autoCreateMineLinerAssignment();
			
			Prison.get().getPlatform().autoCreateConfigureMines();
		}
		
		
		if ( countRanks == 0 ) {
			autoConfigNoRanksCreatedMsg( sender );
		}
		else {
			autoConfigRanksCreatedMsg( sender, Integer.toString( countRanks ) );
			
			if ( countRankCmds == 0 ) {
				autoConfigNoRankCmdsCreatedMsg( sender );
			}
			else {
				autoConfigRankCmdsCreatedMsg( sender, Integer.toString( countRanks ) );
			}
		}
		
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
        	rankWasRemovedMsg( sender, rankName );
        } else {
        	rankDeleteErrorMsg( sender, rankName );
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
        	ladderDoesNotExistMsg( sender, ladderName );
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
        

        String rankHeader = ranksListHeaderMsg( ladderName );
        ChatDisplay display = new ChatDisplay( rankHeader );
        
        if ( hasPerm ) {
        	display.addText( ranksListClickToEditMsg() );
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
            					ranksListCommandCountMsg(rank.getRankUpCommands().size())
            							: "" );
            String textCurrency = (rank.getCurrency() == null ? "" : 
            								ranksListCurrencyMsg( rank.getCurrency() ));
            
            String text =
                String.format("%s &9[&3%s&9] &7- %s&7%s%s%s", 
                			textRankName, rank.getTag(), 
                			(defaultRank ? "&b(&9Default&b) &7- " : ""),
                			Text.numberToDollars(rank.getCost()),
                			textCurrency,
                			textCmdCount );
            
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
        			.tooltip( ranksListCreateNewRankMsg() )));
        	
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
        			msg.then(i == others.size() ? "&8." : "&8,");
        		}
        		display.addComponent(new FancyMessageComponent(msg));
        	}
        	
        }

        display.send(sender);

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


        ChatDisplay display = new ChatDisplay( ranksInfoHeaderMsg( rank.getTag() ));

        display.addText( ranksInfoNameMsg( rank.getName() ));
        display.addText( ranksInfoTagMsg( rank.getTag() ));
        display.addText( ranksInfoLadderMsg( rank.getLadder().getName() ));
        
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

        display.addText( ranksInfoCostMsg( rank.getCost() ));
        
        display.addText( ranksInfoCurrencyMsg( (rank.getCurrency() == null ? "&cdefault" : rank.getCurrency()) ));
        
        List<RankPlayer> players =
        		PrisonRanks.getInstance().getPlayerManager().getPlayers().stream()
        		.filter(rankPlayer -> rankPlayer.getLadderRanks().values().contains(rank))
        		.collect(Collectors.toList());
        display.addText( ranksInfoPlayersWithRankMsg( players.size() ));

        if (sender.hasPermission("ranks.admin")) {
            // This is admin-exclusive content

//            display.addText("&8[Admin Only]");
            display.addText( ranksInfoRankIdMsg( rank.getId() ));

            FancyMessage del =
                new FancyMessage( ranksInfoRankDeleteMessageMsg() ).command("/ranks delete " + rank.getName())
                    .tooltip( ranksInfoRankDeleteToolTipMsg() );
            display.addComponent(new FancyMessageComponent(del));
        }

        display.send(sender);
        
        if ( options != null && "all".equalsIgnoreCase( options )) {
        	
        	getRankCommandCommands().commandLadderList( sender, rank.getLadder().getName(), "noRemoves" );
        	
        	getRankCommandCommands().commandList( sender, rankName, "noRemoves" );
        }
    }


    @Command(identifier = "ranks set cost", description = "Modifies a ranks cost", 
    							onlyPlayers = false, permissions = "ranks.set")
    public void setCost(CommandSender sender, 
    		@Arg(name = "rankName") String rankName, 
    		@Arg(name = "cost", description = "The cost of this rank.") double cost){
        Rank rank = PrisonRanks.getInstance().getRankManager().getRank(rankName);
        if ( rank == null ) {
        	rankDoesNotExistMsg( sender, rankName );
            return;
        }
        
        rank.setCost( cost );
        
        PrisonRanks.getInstance().getRankManager().saveRank(rank);
        
        rankSetCostSuccessfulMsg( sender, rankName, cost );
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

    @Command(identifier = "ranks set tag", description = "Modifies a ranks tag", 
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
				
				String messageRank = ranksPlayerLadderInfoMsg( 
						player.getDisplayName(), 
						rankLadder.getName(),
						rank.getName() );
				
				if ( nextRank == null ) {
					messageRank += ranksPlayerLadderHighestRankMsg();
				} else {
					messageRank += ranksPlayerLadderNextRankMsg( 
							nextRank.getName(), 
							dFmt.format( nextRank.getCost() ) );

					if ( nextRank.getCurrency() != null ) {
						messageRank += ranksPlayerLadderNextRankCurrencyMsg( nextRank.getCurrency() );
					}
				}
				
				sendToPlayerAndConsole( sender, messageRank );
			}
			
			// Print out the player's balances: 

			// The default currency first:
			double balance = rankPlayer.getBalance();
			String message = ranksPlayerBalanceDefaultMsg( player.getName(), dFmt.format( balance ) );
			sendToPlayerAndConsole( sender, message );
			
			for ( String currency : currencies ) {
				double balanceCurrency = rankPlayer.getBalance( currency );
				String messageCurrency = ranksPlayerBalanceOthersMsg( 
						player.getName(), dFmt.format( balanceCurrency ), currency );
				sendToPlayerAndConsole( sender, messageCurrency );

			}
			
			boolean isOp = player.isOp();
			boolean isPlayer = player.isPlayer();
			boolean isOnline = player.isOnline();
			
			boolean isPrisonPlayer = (player instanceof Player);
			boolean isPrisonOfflineMcPlayer = (player instanceof OfflineMcPlayer);

			if ( !isOnline ) {
				String msgOffline = ranksPlayerPermsOfflineMsg();
				sendToPlayerAndConsole( sender, msgOffline );
			}
			
			double sellallMultiplier = player.getSellAllMultiplier();
			DecimalFormat pFmt = new DecimalFormat("#,##0.0000");
			String messageNotAccurrate = ranksPlayerNotAccurateMsg();
			String messageSellallMultiplier = ranksPlayerSellallMultiplierMsg( 
					pFmt.format( sellallMultiplier ), 
					(!isOnline ? "  " + messageNotAccurrate : "") );
			sendToPlayerAndConsole( sender, messageSellallMultiplier );

			
			
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
			
		} else {
			ranksPlayerNoRanksFoundMsg( sender, player.getDisplayName() );
		}
    }

    
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
