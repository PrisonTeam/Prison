package tech.mcprison.prison.ranks.commands;

import tech.mcprison.prison.commands.Arg;
import tech.mcprison.prison.commands.BaseCommands;
import tech.mcprison.prison.commands.Command;
import tech.mcprison.prison.internal.CommandSender;
import tech.mcprison.prison.output.BulletedListComponent;
import tech.mcprison.prison.output.ChatDisplay;
import tech.mcprison.prison.output.Output;
import tech.mcprison.prison.ranks.PrisonRanks;
import tech.mcprison.prison.ranks.data.Rank;
import tech.mcprison.prison.ranks.data.RankLadder;

import java.io.IOException;
import java.util.Optional;

/**
 * @author Faizaan A. Datoo
 */
public class LadderCommands
				extends BaseCommands {
	
	public LadderCommands() {
		super( "LadderCommands" );
	}

    @Command(identifier = "ranks ladder create", description = "Creates a new rank ladder.", 
    								onlyPlayers = false, permissions = "ranks.ladder")
    public void ladderAdd(CommandSender sender, @Arg(name = "ladderName") String ladderName) {
        Optional<RankLadder> ladderOptional =
            PrisonRanks.getInstance().getLadderManager().getLadder(ladderName);
        if (ladderOptional.isPresent()) {
            Output.get()
                .sendError(sender, "A ladder with the name '%s' already exists.", ladderName);
            return;
        }

        ladderOptional = PrisonRanks.getInstance().getLadderManager().createLadder(ladderName);

        if (!ladderOptional.isPresent()) {
            Output.get().sendError(sender,
                "An error occurred while creating your ladder. &8Check the console for details.");
            return;
        }

        try {
            PrisonRanks.getInstance().getLadderManager().saveLadder(ladderOptional.get());
            
            Output.get().sendInfo(sender, "The ladder '%s' has been created.", ladderName);
        } catch (IOException e) {
            Output.get().sendError(sender,
                "An error occurred while creating your ladder. &8Check the console for details.");
            Output.get().logError("Could not save ladder.", e);
        }
    }

    @Command(identifier = "ranks ladder delete", description = "Deletes a rank ladder.", 
    								onlyPlayers = false, permissions = "ranks.ladder")
    public void ladderRemove(CommandSender sender, @Arg(name = "ladderName") String ladderName) {
        Optional<RankLadder> ladder =
            PrisonRanks.getInstance().getLadderManager().getLadder(ladderName);

        if (!ladder.isPresent()) {
            Output.get().sendError(sender, "The ladder '%s' doesn't exist.", ladderName);
            return;
        }
        
        if (ladder.get().name.equalsIgnoreCase( "default" )) {
        	Output.get().sendError(sender, "You cannot delete the default ladder. It's needed." );
        	return;
        }

        if (ladder.get().name.equalsIgnoreCase( "prestiges" )) {
        	Output.get().sendError(sender, "You cannot delete the prestiges ladder. It's needed." );
        	return;
        }
        
        if ( PrisonRanks.getInstance().getLadderManager().removeLadder(ladder.get()) ) {
            Output.get().sendInfo(sender, "The ladder '%s' has been deleted.", ladderName);

        } else {
            Output.get().sendError(sender,
                "An error occurred while removing your ladder. &8Check the console for details.");
        }
    }

    @Command(identifier = "ranks ladder list", description = "Lists all rank ladders.", 
    								onlyPlayers = false, permissions = "ranks.ladder")
    public void ladderList(CommandSender sender) {
        ChatDisplay display = new ChatDisplay("Ladders");
        BulletedListComponent.BulletedListBuilder list =
            new BulletedListComponent.BulletedListBuilder();
        for (RankLadder ladder : PrisonRanks.getInstance().getLadderManager().getLadders()) {
            list.add(ladder.name);
        }
        display.addComponent(list.build());

        display.send(sender);
    }

    @Command(identifier = "ranks ladder listranks", description = "Lists the ranks within a ladder.", 
    								onlyPlayers = false, permissions = "ranks.ladder")
    public void ladderInfo(CommandSender sender, @Arg(name = "ladderName") String ladderName) {
        Optional<RankLadder> ladder =
            PrisonRanks.getInstance().getLadderManager().getLadder(ladderName);

        if (!ladder.isPresent()) {
            Output.get().sendError(sender, "The ladder '%s' doesn't exist.", ladderName);
            return;
        }

        ChatDisplay display = new ChatDisplay(ladder.get().name);
        display.text("&7This ladder contains the following ranks:");

        BulletedListComponent.BulletedListBuilder builder =
            new BulletedListComponent.BulletedListBuilder();
        
        boolean first = true;
        for (RankLadder.PositionRank rankPos : ladder.get().ranks) {
        	Rank rank = PrisonRanks.getInstance().getRankManager().getRank(rankPos.getRankId());
        	if ( rank == null ) {
        		continue;
        	}
        	
//            Optional<Rank> rankOptional =
//                PrisonRanks.getInstance().getRankManager().getRankOptional(rankPos.getRankId());
//            if(!rankOptional.isPresent()) {
//                continue; // Skip it
//            }
            
            boolean defaultRank = ("default".equalsIgnoreCase( ladderName ) && first);

            builder.add("&3#%d &8- &3%s %s", rankPos.getPosition(),
                rank.name, 
                (defaultRank ? "&b(&9Default Rank&b) &7-" : "")
            	);
            first = false;
        }

        builder.add( "&3See &f/ranks list &b[ladderName] &3for more details on ranks." );
        
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
    	sender.sendMessage( "Attempting to remove the specified rank from it's original ladder, " +
    			"then it will be added back to the target ladder at the spcified location. The rank " +
    			"will not be lost." );
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
        Optional<RankLadder> ladder =
            PrisonRanks.getInstance().getLadderManager().getLadder(ladderName);
        if (!ladder.isPresent()) {
            Output.get().sendError(sender, "The ladder '%s' doesn't exist.", ladderName);
            return;
        }

        Rank rank = PrisonRanks.getInstance().getRankManager().getRank(rankName);
//        Optional<Rank> rank = PrisonRanks.getInstance().getRankManager().getRankOptional(rankName);
        if ( rank == null ) {
            Output.get().sendError(sender, "The rank '%s' doesn't exist.", rankName);
            return;
        }

        if (ladder.get().containsRank(rank.id)) {
            Output.get()
                .sendError(sender, "The ladder '%s' already contains the rank '%s'.", ladderName,
                    rankName);
            return;
        }

        if (position > 0) {
            ladder.get().addRank(position, rank);
        } else {
            ladder.get().addRank(rank);
        }

        try {
            PrisonRanks.getInstance().getLadderManager().saveLadder(ladder.get());
            
            Output.get().sendInfo(sender, "Added rank '%s' to ladder '%s' in position %s.", 
            		rank.name, ladder.get().name, Integer.toString( position ));
        } catch (IOException e) {
            Output.get().sendError(sender,
                "An error occurred while adding a rank to your ladder. &8Check the console for details.");
            Output.get().logError("Error while saving ladder.", e);
        }
    }

    @Command(identifier = "ranks ladder delrank", description = "Removes a rank from a ladder.", 
    											onlyPlayers = false, permissions = "ranks.ladder")
    public void ladderRemoveRank(CommandSender sender, @Arg(name = "ladderName") String ladderName,
        @Arg(name = "rankName") String rankName) {
        Optional<RankLadder> ladder =
            PrisonRanks.getInstance().getLadderManager().getLadder(ladderName);
        if (!ladder.isPresent()) {
            Output.get().sendError(sender, "The ladder '%s' doesn't exist.", ladderName);
            return;
        }

        Rank rank = PrisonRanks.getInstance().getRankManager().getRank(rankName);
//        Optional<Rank> rank = PrisonRanks.getInstance().getRankManager().getRankOptional(rankName);
        if ( rank == null ) {
            Output.get().sendError(sender, "The rank '%s' doesn't exist.", rankName);
            return;
        }

        ladder.get().removeRank(ladder.get().getPositionOfRank(rank));

        try {
            PrisonRanks.getInstance().getLadderManager().saveLadder(ladder.get());

            Output.get().sendInfo(sender, "Removed rank '%s' from ladder '%s'.", rank.name,
            		ladder.get().name);
        } catch (IOException e) {
            Output.get().sendError(sender,
                "An error occurred while removing a rank from your ladder. &8Check the console for details.");
            Output.get().logError("Error while saving ladder.", e);
        }

    }

}
