package tech.mcprison.prison.ranks.commands;

import tech.mcprison.prison.chat.FancyMessage;
import tech.mcprison.prison.commands.Arg;
import tech.mcprison.prison.commands.Command;
import tech.mcprison.prison.commands.Wildcard;
import tech.mcprison.prison.internal.CommandSender;
import tech.mcprison.prison.localization.Localizable;
import tech.mcprison.prison.localization.Localizable.Level;
import tech.mcprison.prison.output.BulletedListComponent;
import tech.mcprison.prison.output.ChatDisplay;
import tech.mcprison.prison.output.FancyMessageComponent;
import tech.mcprison.prison.output.Output;
import tech.mcprison.prison.ranks.PrisonRanks;
import tech.mcprison.prison.ranks.data.Rank;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Optional;

/**
 * @author Faizaan A. Datoo
 */
public class CommandCommands {
	
	public CommandCommands() {
		super();
		
		// Now this is slight strange. Once in a while I've been seeing exceptions that the 
		// following class cannot be resolved.  So I don't know why it can't, but it was not
		// being used directly within this Ranks module, so it's being added here just to 
		// allow the compiler to add it, and so hopefully the class loaders at run time can
		// finally access it consistently.
		@SuppressWarnings( "unused" )
		Level force = Localizable.Level.ERROR;
	}

    @Command(identifier = "ranks command add", 
    		description = "Adds a command to a rank using {player} and {player_uid} as placeholders.", 
    		onlyPlayers = false, permissions = "ranks.command")
    public void commandAdd(CommandSender sender, 
    			@Arg(name = "rankName", description = "The Rank name that will recieve this command.") String rankName,
    			@Arg(name = "command", 
    				description = "The command to add without / prefix. Will be ran as a console command.") 
    					@Wildcard String command) {
        if (command.startsWith("/")) {
            command = command.replaceFirst("/", "");
        }

        Optional<Rank> rankOptional = PrisonRanks.getInstance().getRankManager().getRank(rankName);
        if (!rankOptional.isPresent()) {
            Output.get().sendError(sender, "The rank '%s' does not exist.", rankName);
            return;
        }
        Rank rank = rankOptional.get();

        if (rank.rankUpCommands == null) {
            rank.rankUpCommands = new ArrayList<>();
        }
        rank.rankUpCommands.add(command);
    	
        try {
        	PrisonRanks.getInstance().getRankManager().saveRank( rank );
        	
        	Output.get().sendInfo(sender, "Added command '%s' to the rank '%s'.", command, rank.name);
        } catch (IOException e) {
            Output.get().sendError(sender,
                "The new command for the rank could not be saved to disk. Check the console for details.");
            Output.get().logError("Rank could not be written to disk.", e);
        }


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

        Optional<Rank> rankOptional = PrisonRanks.getInstance().getRankManager().getRank(rankName);
        if (!rankOptional.isPresent()) {
            Output.get().sendError(sender, "The rank '%s' does not exist.", rankName);
            return;
        }
        Rank rank = rankOptional.get();

        if (rank.rankUpCommands == null) {
            rank.rankUpCommands = new ArrayList<>();
        }
        
        if ( rank.rankUpCommands.remove(command) ) {
        	
            try {
            	PrisonRanks.getInstance().getRankManager().saveRank( rank );
            	
            	Output.get()
            		.sendInfo(sender, "Removed command '%s' from the rank '%s'.", command, rank.name);
            } catch (IOException e) {
                Output.get().sendError(sender,
                    "The updated rank could not be saved to disk. Check the console for details.");
                Output.get().logError("Rank could not be written to disk.", e);
            }
        } else {
        	Output.get()
        		.sendWarn(sender, "The rank doesn't contain that command. Nothing was changed.");
        }
    }

    @Command(identifier = "ranks command list", description = "Lists the commands for a rank.", 
    		onlyPlayers = false, permissions = "ranks.command")
    public void commandList(CommandSender sender, 
    		@Arg(name = "rankName") String rankName) {
    	
        Optional<Rank> rankOptional = PrisonRanks.getInstance().getRankManager().getRank(rankName);
        if (!rankOptional.isPresent()) {
            Output.get().sendError(sender, "The rank '%s' does not exist.", rankName);
            return;
        }
        Rank rank = rankOptional.get();

        if (rank.rankUpCommands == null || rank.rankUpCommands.size() == 0) {
            Output.get().sendInfo(sender, "The rank '%s' contains no commands.", rank.name);
            return;
        }

        ChatDisplay display = new ChatDisplay("RankUpCommand for " + rank.tag);
        display.text("&8Click a command to remove it.");
        BulletedListComponent.BulletedListBuilder builder =
            new BulletedListComponent.BulletedListBuilder();

        for (String command : rank.rankUpCommands) {
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
