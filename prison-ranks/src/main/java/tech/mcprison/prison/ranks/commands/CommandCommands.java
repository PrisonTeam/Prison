package tech.mcprison.prison.ranks.commands;

import java.util.ArrayList;

import tech.mcprison.prison.chat.FancyMessage;
import tech.mcprison.prison.commands.Arg;
import tech.mcprison.prison.commands.Command;
import tech.mcprison.prison.commands.Wildcard;
import tech.mcprison.prison.internal.CommandSender;
import tech.mcprison.prison.output.BulletedListComponent;
import tech.mcprison.prison.output.ChatDisplay;
import tech.mcprison.prison.output.FancyMessageComponent;
import tech.mcprison.prison.output.LogLevel;
import tech.mcprison.prison.output.Output;
import tech.mcprison.prison.output.RowComponent;
import tech.mcprison.prison.ranks.PrisonRanks;
import tech.mcprison.prison.ranks.data.Rank;
import tech.mcprison.prison.ranks.data.RankLadder;
import tech.mcprison.prison.tasks.PrisonCommandTask;

/**
 * @author Faizaan A. Datoo
 */
public class CommandCommands
				extends CommandCommandsMessages {
	
	public CommandCommands() {
		super( "CommandCommands" );
		
		// Now this is slightly strange. Once in a while I've been seeing exceptions that the 
		// following class cannot be resolved.  So I don't know why it can't, but it was not
		// being used directly within this Ranks module, so it's being added here just to 
		// allow the compiler to add it, and so hopefully the class loaders at run time can
		// finally access it consistently.
		@SuppressWarnings( "unused" )
		LogLevel force = LogLevel.ERROR;
	}

    @Command(identifier = "ranks command add", 
    		description = "Adds a command to a rank using {player} {player_uid} {msg} {broadcast} as placeholders. " +
    				" Plus many custom placeholders!  Enter `placeholders` instead of rankName for a list. " +
    				"Use ; between multiple commands.", 
    		onlyPlayers = false, permissions = "ranks.command")
    public void commandAdd(CommandSender sender, 
    			@Arg(name = "rankName", description = "The Rank name that will recieve this command.") String rankName,
    			@Arg(name = "command", 
    				description = "The command to add without / prefix. Will be ran as a console command.") 
    					@Wildcard String command) {
        if (command.startsWith("/")) {
            command = command.replaceFirst("/", "");
        }

        if ( rankName != null && "placeholders".equalsIgnoreCase( rankName ) ) {
        	
        	String placeholders = 
        			
        			PrisonCommandTask.CustomPlaceholders.listPlaceholders(
        					PrisonCommandTask.CommandEnvironment.all_commands ) + " " +
        			
        			PrisonCommandTask.CustomPlaceholders.listPlaceholders(
									PrisonCommandTask.CommandEnvironment.rank_commands );
        	
        	String message = ranksCommandAddPlaceholdersMsg( placeholders );
        	
        	sender.sendMessage( message );
        	return;
        }
        
        Rank rank = PrisonRanks.getInstance().getRankManager().getRank(rankName);
        if ( rank == null ) {
        	rankDoesNotExistMsg( sender, rankName );
            return;
        }

        if (rank.getRankUpCommands() == null) {
            rank.setRankUpCommands( new ArrayList<>() );
        }
        
        
        // Make sure the command is not already added.  If so, then don't add it:
        for ( String rankCommand : rank.getRankUpCommands() ) {
			if ( rankCommand.equalsIgnoreCase( command ) ) {
				
				ranksCommandAddDuplicateMsg( sender, command, rankName );
				return;
			}
		}
        
        
        rank.getRankUpCommands().add(command);
    	
        PrisonRanks.getInstance().getRankManager().saveRank( rank );
        
        ranksCommandAddSuccessMsg( sender, command, rankName );

    }

    @Command(identifier = "ranks command remove", description = "Removes a command from a rank.", 
    		onlyPlayers = false, permissions = "ranks.command")
    public void commandRemove(CommandSender sender, 
    			@Arg(name = "rankName") String rankName,
    			@Arg(name = "row", 
    					description = "The row number of the command to remove.") 
    					Integer row) {
    	
        if ( row == null || row <= 0 ) {
        	rankRowNumberMustBeGreaterThanZero( sender, row );
        	return;        	
        }

        Rank rank = PrisonRanks.getInstance().getRankManager().getRank(rankName);
        if ( rank == null) {
        	rankDoesNotExistMsg( sender, rankName );
            return;
        }

        if (rank.getRankUpCommands() == null) {
            rank.setRankUpCommands( new ArrayList<>() );
        }

        if ( row > rank.getRankUpCommands().size() ) {
        	rankRowNumberTooHigh( sender, row );
        	return;        	
        }
        
        String oldCommand = rank.getRankUpCommands().remove( (int) row - 1 );
        
        if ( oldCommand != null ) {
        	
        	PrisonRanks.getInstance().getRankManager().saveRank( rank );
        	
        	ranksCommandRemoveSuccessMsg( sender, oldCommand, rank.getName() );

        } else {
        	ranksCommandRemoveFailedMsg( sender );
        }
        
        // Redisplay the the rank command list:
        commandList( sender, rankName, "" );
    }


	@Command(identifier = "ranks command list", description = "Lists the commands for a rank.", 
    		onlyPlayers = false, permissions = "ranks.command")
    public void commandList(CommandSender sender, 
    		@Arg(name = "rankName") String rankName, 
    		@Arg(name = "options", description = "Options [noRemoves]", def = "") String options) {
    	
        Rank rank = PrisonRanks.getInstance().getRankManager().getRank(rankName);
        if ( rank == null ) {
        	rankDoesNotExistMsg( sender, rankName );
            return;
        }

        if (rank.getRankUpCommands() == null || rank.getRankUpCommands().size() == 0) {
        	ranksCommandListContainsNoneMsg( sender, rank.getName() );
            return;
        }
        
        boolean noRemoves = options != null && "noRemoves".equalsIgnoreCase( options );

        ChatDisplay display = new ChatDisplay( ranksCommandListCmdHeaderMsg( rank.getTag() ));
        if ( !noRemoves ) {
        	
        	display.addText( ranksCommandListClickCmdToRemoveMsg() );
        }
        BulletedListComponent.BulletedListBuilder builder =
            new BulletedListComponent.BulletedListBuilder();

        int rowNumber = 1;
        for (String command : rank.getRankUpCommands()) {
        	
        	RowComponent row = new RowComponent();
        	
        	row.addTextComponent( " &3Row: &d%d  ", rowNumber++ );
        	
            FancyMessage msg = new FancyMessage("&3/" + command);
            row.addFancy( msg );
            
            if ( !noRemoves ) {
            	FancyMessage msgRemove = new FancyMessage( " &4Remove&3" )
            			.suggest("/ranks command remove " + rankName + " " + rowNumber )
            			.tooltip( ranksCommandListClickToRemoveMsg() );
            	row.addFancy( msgRemove );
            }
	
            builder.add( row );
            
        }

        display.addComponent(builder.build());
        display.addComponent(new FancyMessageComponent(
            new FancyMessage( ranksCommandListAddButtonMsg() )
            	.suggest("/ranks command add " + rankName + " /")
                .tooltip( ranksCommandListAddNewCommandToolTipMsg() )));
        display.send(sender);
    }

    

    @Command(identifier = "ranks ladder command add", 
    		description = "Ladder commands apply to all ranks. Adds a command to a ladder " +
    				"using {player} {player_uid} {msg} {broadcast} as placeholders. " +
    				" Plus many custom placeholders!  Enter `placeholders` instead of rankName for a list. " +
    				"Use ; between multiple commands.", 
    		onlyPlayers = false, permissions = "ranks.command")
    public void commandLadderAdd(CommandSender sender, 
    			@Arg(name = "ladderName", 
    				description = "The ladder name that will recieve this command.") String ladderName,
    			@Arg(name = "command", 
    				description = "The command to add without / prefix. Will be ran as a console command.") 
    					@Wildcard String command) {
        if (command.startsWith("/")) {
            command = command.replaceFirst("/", "");
        }

        if ( ladderName != null && "placeholders".equalsIgnoreCase( ladderName ) ) {
        	
        	String placeholders = PrisonCommandTask.CustomPlaceholders.listPlaceholders(
											PrisonCommandTask.CommandEnvironment.all_commands ) + " " +
        			
									PrisonCommandTask.CustomPlaceholders.listPlaceholders(
											PrisonCommandTask.CommandEnvironment.rank_commands );
        	
        	String message = ladderCommandAddPlaceholdersMsg( placeholders );
        	
        	sender.sendMessage( message );
        	return;
        }
        
        RankLadder ladder = PrisonRanks.getInstance().getLadderManager().getLadder( ladderName );
        if ( ladder == null ) {
        	ladderDoesNotExistMsg( sender, ladderName );
            return;
        }

        if (ladder.getRankUpCommands() == null) {
        	ladder.setRankUpCommands( new ArrayList<>() );
        }
        
        
        // Make sure the command is not already added.  If so, then don't add it:
        for ( String rankCommand : ladder.getRankUpCommands() ) {
			if ( rankCommand.equalsIgnoreCase( command ) ) {
				
				ladderCommandAddDuplicateMsg( sender, command, ladderName );
				return;
			}
		}
        
        
        ladder.getRankUpCommands().add(command);
    	
        PrisonRanks.getInstance().getLadderManager().save( ladder );
        
        ladderCommandAddSuccessMsg( sender, command, ladderName );

    }

    @Command(identifier = "ranks ladder command remove", 
    		description = "Removes a command from a rank.", 
    		onlyPlayers = false, permissions = "ranks.command")
    public void commandLadderRemove(CommandSender sender, 
    			@Arg(name = "ladderName") String ladderName,
    			@Arg(name = "row", 
    					description = "The row number of the command to remove.") 
    					Integer row) {
    	
        if ( row == null || row <= 0 ) {
        	rankRowNumberMustBeGreaterThanZero( sender, row );
        	return;        	
        }

        RankLadder ladder = PrisonRanks.getInstance().getLadderManager().getLadder(ladderName);
        if ( ladder == null) {
        	ladderDoesNotExistMsg( sender, ladderName );
            return;
        }

        if (ladder.getRankUpCommands() == null) {
            ladder.setRankUpCommands( new ArrayList<>() );
        }

        if ( row > ladder.getRankUpCommands().size() ) {
        	rankRowNumberTooHigh( sender, row );
        	return;        	
        }
        
        String oldCommand = ladder.getRankUpCommands().remove( (int) row - 1 );
        
        if ( oldCommand != null ) {
        	
        	PrisonRanks.getInstance().getLadderManager().save( ladder );
        	
        	ladderCommandRemoveSuccessMsg( sender, oldCommand, ladder.getName() );

        } else {
        	ladderCommandRemoveFailedMsg( sender );
        }
        
        // Redisplay the the rank command list:
        commandLadderList( sender, ladderName, "" );
    }

    @Command(identifier = "ranks ladder command list", description = "Lists the commands for a ladder.", 
    		onlyPlayers = false, permissions = "ranks.command")
    public void commandLadderList(CommandSender sender, 
    		@Arg(name = "ladderName", description = "Ladder Name") String ladderName, 
    		@Arg(name = "options", description = "Options [noRemoves]", def = "") String options ) {
    	
        RankLadder ladder = PrisonRanks.getInstance().getLadderManager().getLadder( ladderName );
        if ( ladder == null ) {
        	ladderDoesNotExistMsg( sender, ladderName );
            return;
        }

        if (ladder.getRankUpCommands() == null || ladder.getRankUpCommands().size() == 0) {
        	ladderCommandListContainsNoneMsg( sender, ladder.getName() );
            return;
        }
        
        boolean noRemoves = options != null && "noRemoves".equalsIgnoreCase( options );

        ChatDisplay display = new ChatDisplay( ladderCommandListCmdHeaderMsg( ladder.getName() ));
        if ( !noRemoves ) {
        	display.addText( ranksCommandListClickCmdToRemoveMsg() );
        }
        
        BulletedListComponent.BulletedListBuilder builder =
            new BulletedListComponent.BulletedListBuilder();

        int rowNumber = 1;
        for (String command : ladder.getRankUpCommands()) {
        	
        	RowComponent row = new RowComponent();
        	
        	row.addTextComponent( " &3Row: &d%d  ", rowNumber++ );
        	
            FancyMessage msg = new FancyMessage("&3/" + command);
            row.addFancy( msg );
            
            if ( !noRemoves ) {
            	
            	FancyMessage msgRemove = new FancyMessage( " &4Remove&3" )
            			.suggest("/ranks ladder command remove " + ladderName + " " + rowNumber )
            			.tooltip( ranksCommandListClickToRemoveMsg() );
            	row.addFancy( msgRemove );
            }
	
            builder.add( row );
            
        }

        display.addComponent(builder.build());
        display.addComponent(new FancyMessageComponent(
            new FancyMessage( ranksCommandListAddButtonMsg() )
            	.suggest("/ranks ladder command add " + ladderName + " /")
                .tooltip( ranksCommandListAddNewCommandToolTipMsg() )));
        display.send(sender);
    }

    
}
