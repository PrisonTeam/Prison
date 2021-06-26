package tech.mcprison.prison.ranks.commands;

import tech.mcprison.prison.commands.Arg;
import tech.mcprison.prison.commands.Command;
import tech.mcprison.prison.internal.CommandSender;
import tech.mcprison.prison.output.BulletedListComponent;
import tech.mcprison.prison.output.ChatDisplay;
import tech.mcprison.prison.ranks.PrisonRanks;
import tech.mcprison.prison.ranks.data.Rank;
import tech.mcprison.prison.ranks.data.RankLadder;

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
        
        if (ladder.getName().equalsIgnoreCase( "default" )) {
        	ladderDeleteCannotDeleteDefaultMsg( sender );
        	return;
        }

        if (ladder.getName().equalsIgnoreCase( "prestiges" )) {
        	ladderDeleteCannotDeletePrestigesMsg( sender );
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
        for (RankLadder ladder : PrisonRanks.getInstance().getLadderManager().getLadders()) {
            list.add(ladder.getName());
        }
        display.addComponent(list.build());

        display.send(sender);
    }

    @Command(identifier = "ranks ladder listranks", description = "Lists the ranks within a ladder.", 
    								onlyPlayers = false, permissions = "ranks.ladder")
    public void ladderInfo(CommandSender sender, @Arg(name = "ladderName") String ladderName) {
        RankLadder ladder = PrisonRanks.getInstance().getLadderManager().getLadder(ladderName);

        if ( ladder == null ) {
        	ladderDoesNotExistsMsg( sender, ladderName );
            return;
        }

        ChatDisplay display = new ChatDisplay(ladder.getName());
        display.addText( ladderHasRankMsg() );

        BulletedListComponent.BulletedListBuilder builder =
            new BulletedListComponent.BulletedListBuilder();
        
        boolean first = true;
        for (Rank rank : ladder.getRanks()) {
//        	Rank rank = PrisonRanks.getInstance().getRankManager().getRank(rankPos.getRankId());
        	if ( rank == null ) {
        		continue;
        	}
        	
//            Optional<Rank> rankOptional =
//                PrisonRanks.getInstance().getRankManager().getRankOptional(rankPos.getRankId());
//            if(!rankOptional.isPresent()) {
//                continue; // Skip it
//            }
            
            boolean defaultRank = ("default".equalsIgnoreCase( ladderName ) && first);
            
            String defaultRankValue = ladderDefaultRankMsg();

            builder.add("&3(#%s) &8- &3%s %s", 
            		Integer.toString( rank.getPosition() ),
            		rank.getName(), 
                (defaultRank ? defaultRankValue : "")
            	);
            first = false;
        }

        String seeRanksList = ladderSeeRanksListMsg();
        
        builder.add( seeRanksList );
        
        display.addComponent(builder.build());
        
        display.send(sender);
    }
    
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
    	
    	ladderRemoveRank( sender, ladderName, rankName );
    	ladderAddRank(sender, ladderName, rankName, position );
    }

    @Command(identifier = "ranks ladder addrank", description = "Adds a rank to a ladder, or move a rank.", 
    								onlyPlayers = false, permissions = "ranks.ladder")
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

        if (ladder.containsRank(rank.getId())) {
        	ladderAlreadyHasRankMsg( sender, ladderName, rankName );
            return;
        }

        if (position > 0) {
            ladder.addRank(position, rank);
        } else {
            ladder.addRank(rank);
        }

        if ( PrisonRanks.getInstance().getLadderManager().save(ladder) ) {
            
            ladderAddedRankMsg( sender, ladderName, rankName, position );
        } 
        else {
        	
        	ladderErrorAddingMsg( sender );
        	
        	ladderErrorSavingMsg( sender );
        }
    }

    @Command(identifier = "ranks ladder delrank", description = "Removes a rank from a ladder.", 
    											onlyPlayers = false, permissions = "ranks.ladder")
    public void ladderRemoveRank(CommandSender sender, @Arg(name = "ladderName") String ladderName,
        @Arg(name = "rankName") String rankName) {
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

        ladder.removeRank(ladder.getPositionOfRank(rank));

        if ( PrisonRanks.getInstance().getLadderManager().save(ladder) ) {

            ladderRemovedRankFromLadderMsg( sender, rankName, ladderName );
            
        } 
        else {
        	ladderErrorRemovingingMsg( sender );
        	ladderErrorSavingMsg( sender );
        }

    }


  
}
