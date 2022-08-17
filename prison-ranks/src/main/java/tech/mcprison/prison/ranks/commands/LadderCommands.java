package tech.mcprison.prison.ranks.commands;

import tech.mcprison.prison.Prison;
import tech.mcprison.prison.commands.Arg;
import tech.mcprison.prison.commands.Command;
import tech.mcprison.prison.internal.CommandSender;
import tech.mcprison.prison.output.BulletedListComponent;
import tech.mcprison.prison.output.ChatDisplay;
import tech.mcprison.prison.ranks.PrisonRanks;
import tech.mcprison.prison.ranks.data.PlayerRankRefreshTask;
import tech.mcprison.prison.ranks.data.Rank;
import tech.mcprison.prison.ranks.data.RankLadder;
import tech.mcprison.prison.ranks.managers.LadderManager;

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
        ChatDisplay display = new ChatDisplay("Ladders");
        
        BulletedListComponent.BulletedListBuilder list =
        					new BulletedListComponent.BulletedListBuilder();
        
//        DecimalFormat dFmt = new DecimalFormat( "#,##0.0000" );
        
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

        display.send(sender);
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
				@Arg(name = "ladderName") String ladderName,
				@Arg(name = "rankName") String rankName,
				@Arg(name = "position", def = "0", verifiers = "min[0]", 
				description = "Position where you want the rank to be moved to. " +
						"0 is the first position in the ladder.") int position) {
    	
    	ladderMoveRankNoticeMsg( sender );
    	
    	ladderRemoveRank( sender, rankName );
    	ladderAddRank(sender, ladderName, rankName, position );
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
			description = "Sets or removes a ladder's Rank Cost Multiplier.  Setting the " +
					"value to zero will remove the multiplier from the calculations.  The " +
					"Rank Cost Multiplier from all ladders a player has active, will be " +
					"summed together and applied to the cost of all of their ranks. The Rank " +
					"Cost Multiplier represents a percentage, and can be either postive or " +
					"negative. ", 
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
			description = "Controls if the rank costs multiplier should apply to the" +
					"ranks on this ladder.  If the ladder has a rank cost multipiler " +
					"enabled, this setting will not effect its contribution to other " +
					"the multiplier.", 
					onlyPlayers = false, permissions = "ranks.ladder" )
	public void ladderApplyRankCostMultiplier( CommandSender sender, 
			@Arg( name = "ladderName" ) String ladderName,
			@Arg( name = "applyRankCostMultiplier", def = "apply", 
			description = "Applies or disables the ranks on this ladder "
					+ "from applying the rank multiplier to the rank cost for players."
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
				applyRankCostMultiplier.equalsIgnoreCase( "apply" );
		
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
	
  
}
