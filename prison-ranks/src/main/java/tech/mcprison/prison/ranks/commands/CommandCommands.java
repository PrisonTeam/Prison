package tech.mcprison.prison.ranks.commands;

import java.util.ArrayList;

import tech.mcprison.prison.chat.FancyMessage;
import tech.mcprison.prison.commands.Arg;
import tech.mcprison.prison.commands.BaseCommands;
import tech.mcprison.prison.commands.Command;
import tech.mcprison.prison.commands.Wildcard;
import tech.mcprison.prison.internal.CommandSender;
import tech.mcprison.prison.output.BulletedListComponent;
import tech.mcprison.prison.output.ChatDisplay;
import tech.mcprison.prison.output.FancyMessageComponent;
import tech.mcprison.prison.output.LogLevel;
import tech.mcprison.prison.output.Output;
import tech.mcprison.prison.ranks.PrisonRanks;
import tech.mcprison.prison.ranks.data.Rank;
import tech.mcprison.prison.tasks.PrisonCommandTask;

/**
 * @author Faizaan A. Datoo
 */
public class CommandCommands
				extends BaseCommands {
	
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
        	String message = "&7Custom placeholders for rank commands are: &3" +
        			PrisonCommandTask.CustomPlaceholders.listPlaceholders(
							PrisonCommandTask.CommandEnvironment.rank_commands );
        	Output.get().logInfo( message );
        	return;
        }
        
        Rank rank = PrisonRanks.getInstance().getRankManager().getRank(rankName);
        if ( rank == null ) {
            Output.get().sendError(sender, "The rank '%s' does not exist.", rankName);
            return;
        }

        if (rank.getRankUpCommands() == null) {
            rank.setRankUpCommands( new ArrayList<>() );
        }
        
        
        // Make sure the command is not already added.  If so, then don't add it:
        for ( String rankCommand : rank.getRankUpCommands() ) {
			if ( rankCommand.equalsIgnoreCase( command ) ) {
				
				Output.get().sendInfo(sender, "Duplicate command '%s' was not added to the rank '%s'.", command, rank.getName());
				return;
			}
		}
        
        
        rank.getRankUpCommands().add(command);
    	
        PrisonRanks.getInstance().getRankManager().saveRank( rank );
        
        Output.get().sendInfo(sender, "Added command '%s' to the rank '%s'.", command, rank.getName());

    }

    @Command(identifier = "ranks command remove", description = "Removes a command from a rank.", 
    		onlyPlayers = false, permissions = "ranks.command")
    public void commandRemove(CommandSender sender, 
    			@Arg(name = "rankName") String rankName,
    			@Arg(name = "command", 
    					description = "The command must be exactly the same as stored in the rank.") 
    					@Wildcard String command) {
        if (command.startsWith("/")) {
            command = command.replaceFirst("/", "");
        }

        Rank rank = PrisonRanks.getInstance().getRankManager().getRank(rankName);
        if ( rank == null) {
            Output.get().sendError(sender, "The rank '%s' does not exist.", rankName);
            return;
        }

        if (rank.getRankUpCommands() == null) {
            rank.setRankUpCommands( new ArrayList<>() );
        }
        
        if ( rank.getRankUpCommands().remove(command) ) {
        	
        	PrisonRanks.getInstance().getRankManager().saveRank( rank );
        	
        	Output.get()
		        	.sendInfo(sender, "Removed command '%s' from the rank '%s'.", command, rank.getName());

        } else {
        	Output.get()
        		.sendWarn(sender, "The rank doesn't contain that command. Nothing was changed.");
        }
    }

    @Command(identifier = "ranks command list", description = "Lists the commands for a rank.", 
    		onlyPlayers = false, permissions = "ranks.command")
    public void commandList(CommandSender sender, 
    		@Arg(name = "rankName") String rankName) {
    	
        Rank rank = PrisonRanks.getInstance().getRankManager().getRank(rankName);
        if ( rank == null ) {
            Output.get().sendError(sender, "The rank '%s' does not exist.", rankName);
            return;
        }

        if (rank.getRankUpCommands() == null || rank.getRankUpCommands().size() == 0) {
            Output.get().sendInfo(sender, "The rank '%s' contains no commands.", rank.getName());
            return;
        }

        ChatDisplay display = new ChatDisplay("RankUpCommand for " + rank.getTag());
        display.addText("&8Click a command to remove it.");
        BulletedListComponent.BulletedListBuilder builder =
            new BulletedListComponent.BulletedListBuilder();

        for (String command : rank.getRankUpCommands()) {
            FancyMessage msg = new FancyMessage("&3/" + command)
                .command("/ranks command remove " + rankName + " " + command)
                .tooltip("Click to remove.");
            builder.add(msg);
        }

        display.addComponent(builder.build());
        display.addComponent(new FancyMessageComponent(
            new FancyMessage("&7[&a+&7] Add").suggest("/ranks command add " + rankName + " /")
                .tooltip("&7Add a new command.")));
        display.send(sender);
    }

}
