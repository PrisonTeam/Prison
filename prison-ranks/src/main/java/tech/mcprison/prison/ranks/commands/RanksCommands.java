package tech.mcprison.prison.ranks.commands;

import tech.mcprison.prison.Prison;
import tech.mcprison.prison.chat.FancyMessage;
import tech.mcprison.prison.commands.Arg;
import tech.mcprison.prison.commands.Command;
import tech.mcprison.prison.internal.CommandSender;
import tech.mcprison.prison.internal.Player;
import tech.mcprison.prison.output.BulletedListComponent;
import tech.mcprison.prison.output.ChatDisplay;
import tech.mcprison.prison.output.FancyMessageComponent;
import tech.mcprison.prison.output.Output;
import tech.mcprison.prison.ranks.PrisonRanks;
import tech.mcprison.prison.ranks.data.Rank;
import tech.mcprison.prison.ranks.data.RankLadder;
import tech.mcprison.prison.ranks.data.RankPlayer;
import tech.mcprison.prison.ranks.managers.PlayerManager;
import tech.mcprison.prison.util.Text;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author Faizaan A. Datoo
 */
public class RanksCommands {


    @Command(identifier = "ranks", onlyPlayers = false)
    public void baseCommand(CommandSender sender,
        @Arg(name = "ladder", def = "default") String ladderName) {
        if (!sender.hasPermission("ranks.admin")) {
            sender.dispatchCommand("ranks list " + ladderName);
        } else {
            sender.dispatchCommand("ranks help");
        }
    }

    @Command(identifier = "ranks create", description = "Creates a new rank", onlyPlayers = false, permissions = "ranks.create")
    public void createRank(CommandSender sender,
        @Arg(name = "name", description = "The name of this rank.") String name,
        @Arg(name = "cost", description = "The cost of this rank.") double cost,
        @Arg(name = "ladder", description = "The ladder to put this rank on.", def = "default")
            String ladder,
        @Arg(name = "tag", description = "The tag to use for this rank.", def = "none")
            String tag) {

        // Ensure a rank with the name doesn't already exist
        if (PrisonRanks.getInstance().getRankManager().getRank(name).isPresent()) {
            Output.get()
                .sendWarn(sender, "A rank by this name already exists. Try a different name.");
            return;
        }

        // Fetch the ladder first, so we can see if it exists

        Optional<RankLadder> rankLadderOptional =
            PrisonRanks.getInstance().getLadderManager().getLadder(ladder);
        if (!rankLadderOptional.isPresent()) {
            Output.get().sendWarn(sender, "A ladder by the name of '%s' does not exist.", ladder);
            return;
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
            Output.get().sendError(sender, "The rank could not be created.");
            return;
        }

        Rank newRank = newRankOptional.get();

        // Save the rank
        try {
            PrisonRanks.getInstance().getRankManager().saveRank(newRank);
        } catch (IOException e) {
            Output.get().sendError(sender,
                "The new rank could not be saved to disk. Check the console for details.");
            Output.get().logError("Rank could not be written to disk.", e);
        }

        // Add the ladder

        rankLadderOptional.get().addRank(newRank);
        try {
            PrisonRanks.getInstance().getLadderManager().saveLadder(rankLadderOptional.get());
            
            // Tell the player the good news!
            Output.get()
            	.sendInfo(sender, "Your new rank, '%s', was created in the ladder '%s'", name, ladder);
        } catch (IOException e) {
            Output.get().sendError(sender,
                "The '%s' ladder could not be saved to disk. Check the console for details.",
                rankLadderOptional.get().name);
            Output.get().logError("Ladder could not be written to disk.", e);
        }

    }

    @Command(identifier = "ranks delete", description = "Removes a rank, and deletes its files.", onlyPlayers = false, permissions = "ranks.delete")
    public void removeRank(CommandSender sender, @Arg(name = "name") String rankName) {
        // Check to ensure the rank exists
        Optional<Rank> rankOptional = PrisonRanks.getInstance().getRankManager().getRank(rankName);
        if (!rankOptional.isPresent()) {
            Output.get().sendError(sender, "The rank '%s' does not exist.", rankName);
            return;
        }

        Rank rank = rankOptional.get();

        if (PrisonRanks.getInstance().getDefaultLadder().containsRank(rank.id)
            && PrisonRanks.getInstance().getDefaultLadder().ranks.size() == 1) {
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

    @Command(identifier = "ranks list", description = "Lists all the ranks on the server.", onlyPlayers = false, permissions = "ranks.list")
    public void listRanks(CommandSender sender,
        @Arg(name = "ladderName", def = "default") String ladderName) {

        Optional<RankLadder> ladder =
            PrisonRanks.getInstance().getLadderManager().getLadder(ladderName);

        if (!ladder.isPresent()) {
            Output.get().sendError(sender, "The ladder '%s' doesn't exist.", ladderName);
            return;
        }

        List<RankLadder.PositionRank> ranks = ladder.get().ranks;

        ChatDisplay display = new ChatDisplay("Ranks in " + ladderName);
        display.text("&7Click on a rank's name to view more info.");

        BulletedListComponent.BulletedListBuilder builder =
            new BulletedListComponent.BulletedListBuilder();
        
        boolean first = true;
        for (RankLadder.PositionRank pos : ranks) {
            Optional<Rank> rankOptional = ladder.get().getByPosition(pos.getPosition());
            if (!rankOptional.isPresent()) {
                continue; // Skip it
            }
            Rank rank = rankOptional.get();

            boolean defaultRank = ("default".equalsIgnoreCase( ladderName ) && first);
            
            String text =
                String.format("&3%s &9[&3%s&9] &7- %s&7%s &7- Commands: &3%d", 
                			rank.name, rank.tag, 
                			(defaultRank ? "&b(&9Default&b) &7-" : ""),
                			Text.numberToDollars(rank.cost),
                			rank.rankUpCommands.size());
            FancyMessage msg = new FancyMessage(text).command("/ranks info " + rank.name)
                .tooltip("&7Click to view info.");
            builder.add(msg);
            first = false;
        }

        display.addComponent(builder.build());
        display.addComponent(new FancyMessageComponent(
            new FancyMessage("&7[&a+&7] Add").suggest("/ranks create ")
                .tooltip("&7Create a new rank.")));

        List<String> others = new ArrayList<>();
        for (RankLadder other : PrisonRanks.getInstance().getLadderManager().getLadders()) {
            if (!other.name.equals(ladderName) && (other.name.equals("default") || sender
                .hasPermission("ranks.rankup." + other.name.toLowerCase()))) {
                if (sender.hasPermission("ranks.admin")) {
                    others.add("/ranks list " + other.name);
                } else {
                    others.add("/ranks " + other.name);
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

        display.send(sender);

    }

    @Command(identifier = "ranks info", description = "Information about a rank.", onlyPlayers = false, permissions = "ranks.info")
    public void infoCmd(CommandSender sender, @Arg(name = "rankName") String rankName) {
        Optional<Rank> rank = PrisonRanks.getInstance().getRankManager().getRank(rankName);
        if (!rank.isPresent()) {
            Output.get().sendError(sender, "The rank '%s' doesn't exist.", rankName);
            return;
        }

        List<RankLadder> ladders =
            PrisonRanks.getInstance().getLadderManager().getLaddersWithRank(rank.get().id);

        ChatDisplay display = new ChatDisplay("Rank " + rank.get().tag);
        // (I know this is confusing) Ex. Ladder(s): default, test, and test2.
        display.text("&3%s: &7%s", Text.pluralize("Ladder", ladders.size()),
            Text.implodeCommaAndDot(
                ladders.stream().map(rankLadder -> rankLadder.name).collect(Collectors.toList())));

        display.text("&3Cost: &7%s", Text.numberToDollars(rank.get().cost));

        if (sender.hasPermission("ranks.admin")) {
            // This is admin-exclusive content

            display.text("&8[Admin Only]");
            display.text("&6Rank ID: &7%s", rank.get().id);
            display.text("&6Rank Name: &7%s", rank.get().name);

            List<RankPlayer> players =
                PrisonRanks.getInstance().getPlayerManager().getPlayers().stream()
                    .filter(rankPlayer -> rankPlayer.getRanks().values().contains(rank.get()))
                    .collect(Collectors.toList());
            display.text("&7There are &6%s &7with this rank.", players.size() + " players");

            FancyMessage del =
                new FancyMessage("&7[&c-&7] Delete").command("/ranks delete " + rank.get().name)
                    .tooltip("&7Click to delete this rank.\n&cYou may not reverse this action.");
            display.addComponent(new FancyMessageComponent(del));
        }

        display.send(sender);
    }

    // set commands
    @Command(identifier = "ranks set cost", description = "Modifies a ranks cost", onlyPlayers = false, permissions = "ranks.set")
    public void setCost(CommandSender sender, @Arg(name = "name") String rankName, @Arg(name = "cost", description = "The cost of this rank.") double cost){
        Optional<Rank> rankOptional = PrisonRanks.getInstance().getRankManager().getRank(rankName);
        if (!rankOptional.isPresent()) {
            Output.get().sendError(sender, "The rank '%s' doesn't exist.", rankName);
            return;
        }
        
        Rank rank = rankOptional.get();
        rank.cost = cost;
        
        // Save the rank
        try {
            PrisonRanks.getInstance().getRankManager().saveRank(rank);

            Output.get().sendInfo(sender,"Successfully set the cost of rank '%s' to "+cost,rankName);
        } catch (IOException e) {
            Output.get().sendError(sender,
                "The rank could not be saved to disk. The change in rank cost has not been saved. Check the console for details.");
            Output.get().logError("Rank could not be written to disk.", e);
        }
    }

    @Command(identifier = "ranks set tag", description = "Modifies a ranks tag", onlyPlayers = false, permissions = "ranks.set")
    public void setTag(CommandSender sender, @Arg(name = "name") String rankName, @Arg(name = "tag", description = "The desired tag.") String tag){
        Optional<Rank> rankOptional = PrisonRanks.getInstance().getRankManager().getRank(rankName);
        if (!rankOptional.isPresent()) {
            Output.get().sendError(sender, "The rank '%s' doesn't exist.", rankName);
            return;
        }
        
        Rank rank = rankOptional.get();
        rank.tag = tag;
        
        // Save the rank
        try {
            PrisonRanks.getInstance().getRankManager().saveRank(rank);

            Output.get().sendInfo(sender,"Successfully set the tag of rank '%s' to "+tag,rankName);
        } catch (IOException e) {
            Output.get().sendError(sender,
                "The rank could not be saved to disk. The tag change for the rank has not been saved. Check the console for details.");
            Output.get().logError("Rank could not be written to disk.", e);
        }

    }
    
    @Command(identifier = "ranks player", description = "Shows a player their rank", onlyPlayers = false)
    public void rankPlayer(CommandSender sender,
    			@Arg(name = "player", def = "", description = "Player name") String playerName){
    	
    	Player player = getPlayer( sender, playerName );
    	
    	if (player == null) {
    		sender.sendMessage( "&3You must be a player in the game to run this command, and/or the player must be online." );
    		return;
    	}

    	PlayerManager pm = PrisonRanks.getInstance().getPlayerManager();
		Optional<RankPlayer> oPlayer = pm.getPlayer(player.getUUID());
		
		if ( oPlayer.isPresent() ) {
			RankPlayer rankPlayer = oPlayer.get();
			
			String nextRank = pm.getPlayerNextRankName( rankPlayer );
			String nextRankCost = pm.getPlayerNextRankCost( rankPlayer );
			
			String message = String.format("&c%s&7:  Current Rank: &b%s&7", 
					player.getDisplayName(), pm.getPlayerRankName( rankPlayer ));
			
			if ( nextRank.trim().length() == 0 ) {
				message += "  You are already at the highest rank!";
			} else {
				message += String.format("  Next rank: &b%s&7 &c$&b%s", nextRank, nextRankCost );
			}
			
			sender.sendMessage( message );
		} else {
			sender.sendMessage( "&3No ranks found for &c" + player.getDisplayName() );
		}
    }
    
    /**
     * <p>Gets a player by name.  If the player is not online, then try to get them from 
     * the offline player list. If not one is found, then return a null.
     * </p>
     * 
     * @param sender
     * @param playerName is optional, if not supplied, then sender will be used
     * @return Player if found, or null.
     */
	private Player getPlayer( CommandSender sender, String playerName ) {
		Player result = null;
		
		playerName = playerName != null ? playerName : sender != null ? sender.getName() : null;
		
		//Output.get().logInfo("RanksCommands.getPlayer :: playerName = " + playerName );
		
		if ( playerName != null ) {
			Optional<Player> opt = Prison.get().getPlatform().getPlayer( playerName );
			if ( !opt.isPresent() ) {
				opt = Prison.get().getPlatform().getOfflinePlayer( playerName );
			}
			if ( opt.isPresent() ) {
				result = opt.get();
			}
		}
		return result;
	}
    
}
