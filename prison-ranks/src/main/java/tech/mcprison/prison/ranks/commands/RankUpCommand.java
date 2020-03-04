/*
 * Copyright (C) 2017 The MC-Prison Team
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package tech.mcprison.prison.ranks.commands;

import java.util.Optional;
import java.util.UUID;

import tech.mcprison.prison.Prison;
import tech.mcprison.prison.commands.Arg;
import tech.mcprison.prison.commands.Command;
import tech.mcprison.prison.internal.CommandSender;
import tech.mcprison.prison.internal.Player;
import tech.mcprison.prison.output.Output;
import tech.mcprison.prison.ranks.PrisonRanks;
import tech.mcprison.prison.ranks.RankUtil;
import tech.mcprison.prison.ranks.data.RankLadder;
import tech.mcprison.prison.ranks.data.RankPlayer;

/**
 * The commands for this module.
 *
 * @author Faizaan A. Datoo
 */
public class RankUpCommand {

    /*
     * /rankup command
     */

    @Command(identifier = "rankup", description = "Ranks up to the next rank.", permissions = {
        "ranks.user"}) 
    public void rankUp(Player sender,
        @Arg(name = "ladder", description = "The ladder to rank up on.", def = "default")
            String ladderName) {

        // RETRIEVE THE LADDER

        // This player has to have permission to rank up on this ladder.
        if (!ladderName.equalsIgnoreCase("default") && !sender
            .hasPermission("ranks.rankup." + ladderName.toLowerCase())) {
            Output.get()
                .sendError(sender, "You need the permission '%s' to rank up on this ladder.",
                    "ranks.rankup." + ladderName.toLowerCase());
            return;
        }

        UUID playerUuid = sender.getUUID();
        
		ladderName = confirmLadder( sender, ladderName );

        RankPlayer rankPlayer = getPlayer( sender, playerUuid );

        if ( ladderName != null && rankPlayer != null ) {
        	RankUtil.RankUpResult result = RankUtil.rankUpPlayer(rankPlayer, ladderName);
        	
        	processResults( sender, result );
        }

    }


    @Command(identifier = "ranks promote", description = "Promotes a player to the next rank.", 
    		permissions = {"ranks.promote"}) 
    public void promotePlayer(Player sender,
    	@Arg(name = "player", def = "", description = "Player name") String playerName,
        @Arg(name = "ladder", description = "The ladder to promote on.", def = "default")
            String ladderName) {

    	Player player = getPlayer( sender, playerName );
    	
    	if (player == null) {
    		sender.sendMessage( "&3You must be a player in the game to run this command, and/or the player must be online." );
    		return;
    	}

        UUID playerUuid = player.getUUID();
        
		ladderName = confirmLadder( sender, ladderName );

        RankPlayer rankPlayer = getPlayer( sender, playerUuid );

        if ( ladderName != null && rankPlayer != null ) {
        	RankUtil.RankUpResult result = RankUtil.promotePlayer(rankPlayer, ladderName);
        	
        	processResults( sender, result );
        }
    }


    @Command(identifier = "ranks demote", description = "Demotes a player to the next lower rank.", 
    		permissions = {"ranks.demote"}) 
    public void demotePlayer(Player sender,
    	@Arg(name = "player", def = "", description = "Player name") String playerName,
        @Arg(name = "ladder", description = "The ladder to demote on.", def = "default")
            String ladderName) {

    	Player player = getPlayer( sender, playerName );
    	
    	if (player == null) {
    		sender.sendMessage( "&3You must be a player in the game to run this command, and/or the player must be online." );
    		return;
    	}

        UUID playerUuid = player.getUUID();
        
		ladderName = confirmLadder( sender, ladderName );

        RankPlayer rankPlayer = getPlayer( sender, playerUuid );

        if ( ladderName != null && rankPlayer != null ) {
        	RankUtil.RankUpResult result = RankUtil.demotePlayer(rankPlayer, ladderName);
        	
        	processResults( sender, result );
        }
    }

	private String confirmLadder( Player sender, String ladderName )
	{
		Optional<RankLadder> ladderOptional =
            PrisonRanks.getInstance().getLadderManager().getLadder(ladderName);

        // The ladder doesn't exist
        if (!ladderOptional.isPresent()) {
            Output.get().sendError(sender, "The ladder '%s' does not exist.", ladderName);
            ladderName = null;
        }
        return ladderName;
	}


	private RankPlayer getPlayer( Player sender, UUID playerUuid )
	{
		Optional<RankPlayer> playerOptional =
            PrisonRanks.getInstance().getPlayerManager().getPlayer(playerUuid);

        // Well, this isn't supposed to happen...
        if (!playerOptional.isPresent()) {
            Output.get().sendError(sender,
                "You don't exist! The server has no records of you. Try rejoining, or contact a server administrator for help.");
        }

        return playerOptional.isPresent() ? playerOptional.get() : null;
	}



	private void processResults( Player sender, RankUtil.RankUpResult result )
	{
		switch (result.getStatus()) {
            case RANKUP_SUCCESS:
                Output.get().sendInfo(sender, "Congratulations! You have ranked up to rank '%s'. %s",
                    result.getRank().name, (result.getMessage() != null ? result.getMessage() : ""));
                break;
            case RANKUP_CANT_AFFORD:
                Output.get().sendError(sender,
                    "You don't have enough money to rank up! The next rank costs %s.",
                    RankUtil.doubleToDollarString(result.getRank().cost));
                break;
            case RANKUP_LOWEST:
            	Output.get().sendInfo(sender, "You are already at the lowest rank!");
            	break;
            case RANKUP_HIGHEST:
                Output.get().sendInfo(sender, "You are already at the highest rank!");
                break;
            case RANKUP_FAILURE:
                Output.get().sendError(sender,
                    "Failed to retrieve or write data. Your files may be corrupted. Alert a server administrator.");
                break;
            case RANKUP_NO_RANKS:
                Output.get().sendError(sender, "There are no ranks in this ladder.");
                break;
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
//			if ( !opt.isPresent() ) {
//				opt = Prison.get().getPlatform().getOfflinePlayer( playerName );
//			}
			if ( opt.isPresent() ) {
				result = opt.get();
			}
		}
		return result;
	}
}
