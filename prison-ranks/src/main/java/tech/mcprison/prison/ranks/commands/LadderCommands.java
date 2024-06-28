package tech.mcprison.prison.ranks.commands;

import tech.mcprison.prison.Prison;
import tech.mcprison.prison.backups.PrisonBackups;
import tech.mcprison.prison.backups.PrisonBackups.BackupTypes;
import tech.mcprison.prison.commands.Arg;
import tech.mcprison.prison.commands.Command;
import tech.mcprison.prison.internal.CommandSender;
import tech.mcprison.prison.output.BulletedListComponent;
import tech.mcprison.prison.output.ChatDisplay;
import tech.mcprison.prison.output.Output;
import tech.mcprison.prison.ranks.PrisonRanks;
import tech.mcprison.prison.ranks.data.PlayerRankRefreshTask;
import tech.mcprison.prison.ranks.data.Rank;
import tech.mcprison.prison.ranks.data.RankLadder;
import tech.mcprison.prison.ranks.managers.LadderManager;
import tech.mcprison.prison.ranks.managers.RankManager;

/**
 * @author Faizaan A. Datoo
 */
public class LadderCommands
				extends LadderCommandsMessages {
	
	public LadderCommands() {
		super( "LadderCommands" );
	}

    @Command(identifier = "ranks ladder create", description = "Creates a new rank ladder.", 
    								onlyPlayers = false, permissions = "ranks.ladder")
    public void ladderAdd(CommandSender sender, @Arg(name = "ladderName") String ladderName) {
        RankLadder ladder = PrisonRanks.getInstance().getLadderManager().getLadder(ladderName);
        if ( ladder != null ) {
        			
        	ladderAddAlreadyExistsMsg( sender, ladderName );
            return;
        }

        RankLadder rankLadder = PrisonRanks.getInstance().getLadderManager().createLadder(ladderName);

        if ( rankLadder == null ) {
        	
        	ladderAddCreationErrorMsg( sender, ladderName );
        	
            return;
        }

        if ( PrisonRanks.getInstance().getLadderManager().save(rankLadder) ) {

        	
        	Prison.get().getPlatform().getPlaceholders().reloadPlaceholders();
        	
        	
        	ladderAddCreatedMsg( sender, ladderName );
            
        }
        else {
        	
        	ladderAddCreationErrorMsg( sender, ladderName );

        	ladderAddCouldNotSaveMsg( sender );
        }
    }

    @Command(identifier = "ranks ladder delete", description = "Deletes a rank ladder.", 
    								onlyPlayers = false, permissions = "ranks.ladder")
    public void ladderRemove(CommandSender sender, @Arg(name = "ladderName") String ladderName) {
        RankLadder ladder = PrisonRanks.getInstance().getLadderManager().getLadder(ladderName);

        if ( ladder == null ) {
        	ladderDoesNotExistsMsg( sender, ladderName );
            return;
        }
        
        if (ladder.getName().equalsIgnoreCase( LadderManager.LADDER_DEFAULT )) {
        	ladderDeleteCannotDeleteDefaultMsg( sender );
        	return;
        }

        if (ladder.getName().equalsIgnoreCase( LadderManager.LADDER_PRESTIGES )) {
        	ladderDeleteCannotDeletePrestigesMsg( sender );
        	return;
        }
        
        if ( ladder.getRanks().size() > 0 ) {
        	ladderDeleteCannotDeleteWithRanksMsg( sender );
        	return;
        }
        
        if ( PrisonRanks.getInstance().getLadderManager().removeLadder(ladder) ) {
        	ladderDeletedMsg( sender, ladderName );

        } else {
        	ladderErrorMsg( sender );
        }
    }

    @Command(identifier = "ranks ladder list", description = "Lists all rank ladders.", 
    								onlyPlayers = false, permissions = "ranks.ladder")
    public void ladderList(CommandSender sender) {
        
    	ChatDisplay display = getLadderList();
    	
//    	ChatDisplay display = new ChatDisplay("Ladders");
//
//    	display.addSupportHyperLinkData( "Ladder List" );
//        
//        BulletedListComponent.BulletedListBuilder list =
//        					new BulletedListComponent.BulletedListBuilder();
//        
////        DecimalFormat dFmt = Prison.get().getDecimalFormat( "#,##0.0000" );
//        
////        String header = String.format( 
////        		"&d%-12s   %16s   %5s   %12s   %12s",
////        		"Ladder",
////        		"Rank Cost Mult",
////        		"Ranks",
////        		"First Rank",
////        		"Last Rank"
////        		);
//        
//        list.add( PrisonRanks.getInstance().getLadderManager().printRankLadderInfoHeader() );
//        
//        for (RankLadder ladder : PrisonRanks.getInstance().getLadderManager().getLadders()) {
//        	
////        	int rankCount = ladder.getRanks() == null ? 0 : ladder.getRanks().size();
////        	
////        	Rank firstRank = rankCount == 0 ? null : ladder.getRanks().get(0);
////        	Rank lastRank = rankCount == 0 ? null : ladder.getRanks().get( rankCount - 1 );
////        	
////        	String ladderInfo = String.format(
////        			"&7%-12s   %16s   %4d   %-12s   %-12s", 
////        			ladder.getName(),
////        			dFmt.format( ladder.getRankCostMultiplierPerRank() ),
////        			rankCount,
////        			firstRank.getName(),
////        			lastRank.getName()
////        			);
//        	
//            list.add( PrisonRanks.getInstance().getLadderManager().printRankLadderInfoDetail( ladder ) );
//        }
//        
//        display.addComponent(list.build());

        display.send(sender);
    }
    
    public ChatDisplay getLadderList() {
    	ChatDisplay display = new ChatDisplay("Ladders");
    	
        display.addSupportHyperLinkData( "Ladder List" );
        
        BulletedListComponent.BulletedListBuilder list =
        					new BulletedListComponent.BulletedListBuilder();
        
//        DecimalFormat dFmt = Prison.get().getDecimalFormat( "#,##0.0000" );
        
//        String header = String.format( 
//        		"&d%-12s   %16s   %5s   %12s   %12s",
//        		"Ladder",
//        		"Rank Cost Mult",
//        		"Ranks",
//        		"First Rank",
//        		"Last Rank"
//        		);
        
        list.add( PrisonRanks.getInstance().getLadderManager().printRankLadderInfoHeader() );
        
        for (RankLadder ladder : PrisonRanks.getInstance().getLadderManager().getLadders()) {
        	
//        	int rankCount = ladder.getRanks() == null ? 0 : ladder.getRanks().size();
//        	
//        	Rank firstRank = rankCount == 0 ? null : ladder.getRanks().get(0);
//        	Rank lastRank = rankCount == 0 ? null : ladder.getRanks().get( rankCount - 1 );
//        	
//        	String ladderInfo = String.format(
//        			"&7%-12s   %16s   %4d   %-12s   %-12s", 
//        			ladder.getName(),
//        			dFmt.format( ladder.getRankCostMultiplierPerRank() ),
//        			rankCount,
//        			firstRank.getName(),
//        			lastRank.getName()
//        			);
        	
            list.add( PrisonRanks.getInstance().getLadderManager().printRankLadderInfoDetail( ladder ) );
        }
        
        display.addComponent(list.build());
    
    	return display;
    }

//    @Command(identifier = "ranks ladder listranks", description = "Lists the ranks within a ladder.", 
//    								onlyPlayers = false, permissions = "ranks.ladder")
//    public void ladderInfo(
//    		CommandSender sender, 
//    		@Arg(name = "ladderName", def = "default", 
//    		description = "The ladder name to display the ranks on. " +
//    				"Defaults to the default ladder") String ladderName
//    		
//    		) {
//        RankLadder ladder = PrisonRanks.getInstance().getLadderManager().getLadder(ladderName);
//
//        if ( ladder == null ) {
//        	ladderDoesNotExistsMsg( sender, ladderName );
//            return;
//        }
//
//        ChatDisplay display = new ChatDisplay(ladder.getName());
//        display.addText( ladderHasRankMsg() );
//
//        BulletedListComponent.BulletedListBuilder builder =
//            new BulletedListComponent.BulletedListBuilder();
//        
//        boolean first = true;
//        for (Rank rank : ladder.getRanks()) {
////        	Rank rank = PrisonRanks.getInstance().getRankManager().getRank(rankPos.getRankId());
//        	if ( rank == null ) {
//        		continue;
//        	}
//        	
////            Optional<Rank> rankOptional =
////                PrisonRanks.getInstance().getRankManager().getRankOptional(rankPos.getRankId());
////            if(!rankOptional.isPresent()) {
////                continue; // Skip it
////            }
//            
//            boolean defaultRank = ("default".equalsIgnoreCase( ladderName ) && first);
//            
//            String defaultRankValue = ladderDefaultRankMsg();
//
//            builder.add("&3(#%s) &8- &3%s (rankId: %s%s%s) %s", 
//            		Integer.toString( rank.getPosition() ),
//            		rank.getName(),
//            		
//            		Integer.toString( rank.getId() ),
//            		(rank.getRankPrior() == null ? "" : " -"),
//            		(rank.getRankNext() == null ? "" : " +"),
//            		
//                (defaultRank ? defaultRankValue : "")
//            	);
//            first = false;
//        }
//
//        String seeRanksList = ladderSeeRanksListMsg();
//        
//        builder.add( seeRanksList );
//        
//        display.addComponent(builder.build());
//        
//        display.send(sender);
//    }
    
    @Command(identifier = "ranks ladder moveRank", description = "Moves a rank to a new " +
    		"ladder position or a new ladder.", 
			onlyPlayers = false, permissions = "ranks.ladder")
		public void ladderMoveRank(CommandSender sender, 
				@Arg(name = "ladderName", 
						description = "Use a valid ladder name, or '*no-ladder*' "
								+ "to remove the ladder.") String ladderName,
				@Arg(name = "rankName") String rankName,
				@Arg(name = "position", def = "0", verifiers = "min[0]", 
				description = "Position where you want the rank to be moved to. " +
						"0 is the first position in the ladder.") int position) {
    	
    	ladderMoveRankNoticeMsg( sender );
    	
    	ladderRemoveRank( sender, rankName );
    	
    	// If '*no-ladder*' then don't try to reassign it to another ladder:
    	if ( !ladderName.equalsIgnoreCase( "*no-ladder*" ) ) {
    		
    		ladderAddRank(sender, ladderName, rankName, position );
    	}
    	
    }

//    @Command(identifier = "ranks ladder addrank", description = "Adds a rank to a ladder, or move a rank.", 
//    								onlyPlayers = false, permissions = "ranks.ladder")
    public void ladderAddRank(CommandSender sender, 
    		@Arg(name = "ladderName") String ladderName,
	        @Arg(name = "rankName") String rankName,
	        @Arg(name = "position", def = "0", verifiers = "min[0]",
	        description = "Position where you want the rank to be added. " +
	        		"0 is the first position in the ladder.") int position) {
        RankLadder ladder = PrisonRanks.getInstance().getLadderManager().getLadder(ladderName);
        
        if ( ladder == null ) {
        	ladderDoesNotExistsMsg( sender, ladderName );
            return;
        }

        Rank rank = PrisonRanks.getInstance().getRankManager().getRank(rankName);
//        Optional<Rank> rank = PrisonRanks.getInstance().getRankManager().getRankOptional(rankName);
        if ( rank == null ) {
        	ladderRankDoesNotExistMsg( sender, rankName );
            return;
        }

        if (ladder.containsRank( rank )) {
        	ladderAlreadyHasRankMsg( sender, ladderName, rankName );
            return;
        }

        if (position > 0) {
            ladder.addRank(position, rank);
        } else {
            ladder.addRank(rank);
        }

        if ( PrisonRanks.getInstance().getLadderManager().save(ladder) ) {
            
        	Prison.get().getPlatform().getPlaceholders().reloadPlaceholders();
        	
        	
            // Recalculate the ladder's base rank cost multiplier:
            PlayerRankRefreshTask rankRefreshTask = new PlayerRankRefreshTask();
            rankRefreshTask.submitAsyncTPSTask();

            ladderAddedRankMsg( sender, ladderName, rankName, position );
        } 
        else {
        	
        	ladderErrorAddingMsg( sender );
        	
        	ladderErrorSavingMsg( sender );
        }
    }

//    @Command(identifier = "ranks ladder delrank", description = "Removes a rank from a ladder.", 
//    											onlyPlayers = false, permissions = "ranks.ladder")
    public void ladderRemoveRank(CommandSender sender, 
//    	@Arg(name = "ladderName", description = "Then intended ladder to remove the rank from. " +
//    			"But note, this is ignored and the real ladder used is the ladder tied to the rank.") String ladderName,
//        @Arg(name = "rankName") 
    		String rankName) {
//        RankLadder ladder = PrisonRanks.getInstance().getLadderManager().getLadder(ladderName);
        
//        if ( ladder == null ) {
//        	ladderDoesNotExistsMsg( sender, ladderName );
//            return;
//        }

        Rank rank = PrisonRanks.getInstance().getRankManager().getRank(rankName);
//        Optional<Rank> rank = PrisonRanks.getInstance().getRankManager().getRankOptional(rankName);
        if ( rank == null ) {
        	ladderRankDoesNotExistMsg( sender, rankName );
            return;
        }

        RankLadder ladder = rank.getLadder();
        
        if ( ladder == null ) {
        	Output.get().logInfo(
        			"The rank %s has no ladder, so ladder cannot be removed.",
        			rank.getName() );
        	return;
        }
        ladder.removeRank( rank );
        
//        ladder.removeRank(ladder.getPositionOfRank(rank));

        if ( PrisonRanks.getInstance().getLadderManager().save(ladder) ) {

            
            PrisonRanks.getInstance().getRankManager().saveRank( rank );
            
            
            Prison.get().getPlatform().getPlaceholders().reloadPlaceholders();
            
            
            // Recalculate the ladder's base rank cost multiplier:
            PlayerRankRefreshTask rankRefreshTask = new PlayerRankRefreshTask();
            rankRefreshTask.submitAsyncTPSTask();

            
            ladderRemovedRankFromLadderMsg( sender, rankName, ladder.getName() );
        } 
        else {
        	ladderErrorRemovingingMsg( sender );
        	ladderErrorSavingMsg( sender );
        }

    }


	@Command( identifier = "ranks ladder rankCostMultiplier", 
			description = "Sets a ladder's rank cost multiplier which is used to calculate " +
					"a rank's cost on ladders that have them enabled. " +
					"Setting the value to zero will remove that ladder's rank multiplier from " +
					"the calculations. " +
					"Rank Cost Multiplier represents a percentage, and can be either postive or " +
					"negative, and is multiplied by the rank's position (1's based). " +
					"This means that for ladders have have a rank cost multiplier, the " +
					"multiplier increases by the rank's position, such that if the prestige " +
					"ladder has a ranks cost multiplier of 0.1, the P1=0.1, P2=0.2, P3=0.3, etc... {br}" +
	
					"All rank cost multipliers for the player's rank on that ladder, are " +
					"combined from all ladders the player is on, " +
					"then this value is added to a value of 1.0 before being multiplied to the rank cost. " +
					"This will result in a progressively more expensive rank cost for the " + 
					"player as they advance on multiple ladders. " +
					"If a ladder has 'applyRankCostMultiplier' disabled, then rank costs on that " +
					"ladder will not use the player's combined rank cost multiplier. {br}" +
					
					"This calculates " +
					"what a player would have to pay when ranking up.  " +
					"It should be understood that a ladder can contribute to the total " +
					"ranks multiplier, but yet it could ignore the ranks multiplier when " +
					"calculating the rank costs for that ladder. {br}" +
					
					"Example of this kind of setup would be to have the " +
					"default ladder apply the rank cost multiplier to its rank's costs, " +
					"but yet have the default ladder set it's rank cost multiplier to a value of 0.0. " +
					"Then have the prestige ladder ignore " +
					"them, but have the prestige ladder contribute to the global rank " +
					"cost multipliers. This configuration will result in rank costs increasing " + 
					"for the default ladder's ranks, as the player increases their prestige ranks. " + 
					"But yet the prestiges ladder rank costs will not be impacted " +
					"by the rank cost multipliers. Using the example above for p1, p2, p3, etc, " +
					"the rank costs on the default ladder will increase by 10 percent each time " +
					"the player prestiges.  At a 10% increase, the default rank costs will be " +
					"twice as expensive when the are at P10, compared to when they were at " +
					"p0 (no prestige rank)." 
					, 
			onlyPlayers = false, permissions = "ranks.ladder" )
	public void ladderSetRankCostMultiplier( CommandSender sender, 
			@Arg( name = "ladderName" ) String ladderName,
			@Arg( name = "rankCostMultiplier", def = "0", 
			description = "Sets the Rank Cost Multiplier for the ladder, which will be " +
					"applied to each rank, multiplied by the position of the rank.  " +
					"A value of zero is disabled. The value is expressed in " +
					"percentages and can be positive or negative, with any " +
					"valid double value such as -3.141592653589793 or 475.19.") Double rankCostMultiplier )
	{
		RankLadder ladder = PrisonRanks.getInstance().getLadderManager().getLadder( ladderName );

		if ( ladder == null )
		{
			ladderDoesNotExistsMsg( sender, ladderName );
			return;
		}

		if ( rankCostMultiplier == null ) {
			// rankCostMultiplier may never be null?
		}
		
		if ( ladder.getRankCostMultiplierPerRank() == rankCostMultiplier ) {
			// No change:
			
			ladderSetRankCostMultiplierNoChangeMsg( sender, ladderName, rankCostMultiplier );
			
			return;
		}

//		if ( rankCostMultiplier < -100d || rankCostMultiplier > 100d ) {
//			
//			ladderSetRankCostMultiplierOutOfRangeMsg( sender, rankCostMultiplier );
//			return;
//		}
		
		double oldRankCostMultiplier = ladder.getRankCostMultiplierPerRank() * 100;
		
		ladder.setRankCostMultiplierPerRank( rankCostMultiplier / 100 );

		if ( PrisonRanks.getInstance().getLadderManager().save( ladder ) )
		{
			ladderSetRankCostMultiplierSavedMsg( sender, ladderName, 
										rankCostMultiplier, oldRankCostMultiplier );

            // Recalculate the ladder's base rank cost multiplier:
            PlayerRankRefreshTask rankRefreshTask = new PlayerRankRefreshTask();
            rankRefreshTask.submitAsyncTPSTask();
		}
		else
		{
			ladderErrorSavingMsg( sender );
		}
	}

	
	@Command( identifier = "ranks ladder applyRankCostMultiplier", 
			description = "Controls if the rank costs multiplier should apply to the " +
					"ranks on this ladder. This is an 'ON' or 'OFF' situation for the whole " +
					"ladder, where the Rank Cost Multiplier will be ignored for a ladder " +
					"if this is disabled.", 
					onlyPlayers = false, permissions = "ranks.ladder" )
	public void ladderApplyRankCostMultiplier( CommandSender sender, 
			@Arg( name = "ladderName" ) String ladderName,
			@Arg( name = "applyRankCostMultiplier", def = "apply", 
				description = "Applies or disables the ranks on this ladder "
					+ "from applying the rank multiplier to the rank cost for players. "
					+ "Use a value of 'apply' or 'true'; anything else is treated as 'false'."
					) 
			String applyRankCostMultiplier )
	{
		RankLadder ladder = PrisonRanks.getInstance().getLadderManager().getLadder( ladderName );
		
		if ( ladder == null )
		{
			ladderDoesNotExistsMsg( sender, ladderName );
			return;
		}
		
		boolean applyRCM = applyRankCostMultiplier != null && 
				(applyRankCostMultiplier.equalsIgnoreCase( "apply" ) || 
						applyRankCostMultiplier.equalsIgnoreCase( "true" ) );
		
		boolean applyRCMOld = ladder.isApplyRankCostMultiplierToLadder();
		
		
		if ( applyRCMOld == applyRCM ) {
			// No change:
			
			ladderApplyRankCostMultiplierNoChangeMsg( sender, ladderName, applyRCM );
			
			return;
		}
		
		ladder.setApplyRankCostMultiplierToLadder(applyRCM);
		
		
		if ( PrisonRanks.getInstance().getLadderManager().save( ladder ) )
		{
			ladderApplyRankCostMultiplierSavedMsg( sender, ladderName, 
					applyRCM, applyRCMOld );
			
			// Recalculate the ladder's base rank cost multiplier:
			PlayerRankRefreshTask rankRefreshTask = new PlayerRankRefreshTask();
			rankRefreshTask.submitAsyncTPSTask();
		}
		else
		{
			ladderErrorSavingMsg( sender );
		}
	}
	
  
	@Command( identifier = "ranks ladder resetRankCosts", 
			description = "For a given ladder, this command will reset all rank costs "
					+ "based upon a formula, where each rank is progressivly more "
					+ "expensive. "
					+ "This allow easier adjustments to many ranks at the same time. "
					+ "The ranks within this ladder will not be changed, and they will be "
					+ "processed in the same order in which they are listed with the "
					+ "command: '/ranks list <ladderName>'.", 
					onlyPlayers = false, permissions = "ranks.ladder" )
    public void ladderResetRankCosts(CommandSender sender, 
    		@Arg(name = "ladderName") String ladderName,
    		
	        @Arg(name = "initialCost",
	        	def = "1000000000", verifiers = "min[1]",
	        	description = "The 'initialCost' will set the cost of the first rank on "
	        			+ "this ladder.  All other rank costs will be based upon this "
	        			+ "value. The default value is 1_000_000_0000."
	        		) double initialCost,
	        @Arg(name = "addMult", def = "1", verifiers = "min[1]",
	        description = "This is an 'additional multiplier' for all ranks on the ladder, "
	        		+ "with the default value of 1. The cost for each rank is based upon "
	        		+ "the initial rank cost, times the rank's level. So the default ranks "
	        		+ "named 'C', or 'p3', since both are the third rank on their ladders, "
	        		+ "will be 3 times the cost of the first ranks, 'A' or 'p1', with this "
	        		+ "additional multiplier being multiplied against that value.  "
	        		+ "So for default values for a rankk in the third position, "
	        		+ "with a 1.75 multipler will have a "
	        		+ "cost = 1_000_000_000 * 3 * 1.75.") double addMult,
	        @Arg(name = "exponent", def = "1", verifiers = "min[0.00001]",
	        description = "See the cost formula for the 'addMult' parameter. "
	        		+ "This parameter adds an exponent to the formula.  For a linear " 
	        		+ "rank cost, use a value of 1.0 for this parameter. Use a value " 
	        		+ "greater than 1.0, such as 1.2 for slightly more agressive cost "
	        		+ "calculation. Since cost is a IEEE double, there is a risk of " 
	        		+ "lost precision with numbers greater than 16 digits. "
	        		+ "cost = (initialCost * rankPosition * addMult)^exponent.") 
    				double exponent ) {
    	
		
		LadderManager lm = PrisonRanks.getInstance().getLadderManager();
		RankManager rm = PrisonRanks.getInstance().getRankManager();
		
        RankLadder ladder = lm.getLadder(ladderName);
        
        if ( ladder == null ) {
        	ladderDoesNotExistsMsg( sender, ladderName );
            return;
        }

        
        if ( exponent <= 0 ) {
        	sender.sendMessage("Error: ranks ladder resetRankCosts: Exponent parameter must be greater than zero.");
        	return;
        }
        
        // Force a backup:
    	PrisonBackups prisonBackup = new PrisonBackups();
    	
    	String backupComment = String.format( 
    							"Resetting all rank costs on ladder %s.", 
    							ladder.getName() );
    	String message = prisonBackup.startBackup( BackupTypes.auto, backupComment );
    	
    	sender.sendMessage( message );
    	sender.sendMessage( "Forced a Backup of prison configs prior to changing rank costs." );

        
        int ranksChanged = 0;
        
        int i = 1;
        for (Rank rank : ladder.getRanks() ) {
			
        	double cost = Math.pow(initialCost * i++ * addMult, exponent );
        	
        	if ( rank.getRawRankCost() != cost ) {
        		rank.setRawRankCost( cost );
        		
        		rm.saveRank( rank );
        		
        		ranksChanged++;
        	}
        	
		}

        if ( ranksChanged > 0 ) {
        	// Reload the placeholders: 
        	Prison.get().getPlatform().getPlaceholders().reloadPlaceholders();
        	
        	String msg = String.format(
        			"Done resetting all rank costs on the '%s' ladder. "
        			+ "There were %d ranks that had cost changes.", 
        			ladder.getName(),
        			ranksChanged );
        	
        	Output.get().logInfo( msg );
        }
        
//        for ( int i = 0; i < prestigeRanks; i++ ) {
//			String name = "P" + (i + 1);
//			String tag = "&5[&d+" + (i > 0 ? i + 1 : "" ) + "&5]";
//			double cost = prestigeCost * (i + 1) * prestigeMult;
//			
//			// Only add prestige ranks if they do not already exist:
//			if ( PrisonRanks.getInstance().getRankManager().getRank( name ) == null ) {
//				
//				createRank(sender, name, cost, LadderManager.LADDER_PRESTIGES, tag, "noPlaceholderUpdate");
//				prestigesCount++;
//			}
//		}


    }

	
}
