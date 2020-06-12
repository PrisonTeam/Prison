/*
 * Copyright (C) 2017-2020 The MC-Prison Team
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
import tech.mcprison.prison.PrisonAPI;
import tech.mcprison.prison.commands.Arg;
import tech.mcprison.prison.commands.Command;
import tech.mcprison.prison.integration.EconomyIntegration;
import tech.mcprison.prison.integration.IntegrationType;
import tech.mcprison.prison.internal.CommandSender;
import tech.mcprison.prison.internal.Player;
import tech.mcprison.prison.modules.Module;
import tech.mcprison.prison.modules.ModuleManager;
import tech.mcprison.prison.output.Output;
import tech.mcprison.prison.ranks.PrisonRanks;
import tech.mcprison.prison.ranks.RankUtil;
import tech.mcprison.prison.ranks.RankUtil.PromoteForceCharge;
import tech.mcprison.prison.ranks.RankUtil.RankupModes;
import tech.mcprison.prison.ranks.RankUtil.RankupStatus;
import tech.mcprison.prison.ranks.RankupResults;
import tech.mcprison.prison.ranks.data.Rank;
import tech.mcprison.prison.ranks.data.RankLadder;
import tech.mcprison.prison.ranks.data.RankPlayer;
import tech.mcprison.prison.ranks.managers.LadderManager;
import tech.mcprison.prison.util.ChatColor;

/**
 * The commands for this module.
 *
 * @author Faizaan A. Datoo
 * @author GABRYCA
 * @author RoyalBlueRanger
 */
public class RankUpCommand {

    /*
     * /rankup command
     */
	
    @Command(identifier = "rankupMax", description = "Ranks up to the max rank that the player can afford.", 
    			permissions = "ranks.user", altPermissions = "ranks.rankup.[ladderName]", onlyPlayers = true) 
    public void rankUpMax(Player sender,
    		@Arg(name = "ladder", description = "The ladder to rank up on.", def = "default")  String ladder 
    		) {
    	rankUpPrivate(sender, ladder, RankupModes.MAX_RANKS );
    }
	
    @Command(identifier = "rankup", description = "Ranks up to the next rank.", 
			permissions = "ranks.user", altPermissions = "ranks.rankup.[ladderName]", onlyPlayers = true) 
    public void rankUp(Player sender,
		@Arg(name = "ladder", description = "The ladder to rank up on.", def = "default")  String ladder
		) {
    	rankUpPrivate(sender, ladder, RankupModes.ONE_RANK );
    }

    private void rankUpPrivate(Player sender, String ladder, RankupModes mode ) {

        // RETRIEVE THE LADDER

        // This player has to have permission to rank up on this ladder.
        if (!ladder.equalsIgnoreCase("default") && !sender
            .hasPermission("ranks.rankup." + ladder.toLowerCase())) {
            Output.get()
                .sendError(sender, "You need the permission '%s' to rank up on this ladder.",
                    "ranks.rankup." + ladder.toLowerCase());
            return;
        }

        
        // 
        if ( mode == null ) {
        	
        	Output.get()
        		.sendError(sender, "&7Invalid rankup mode. Internal failure. Please report." );
        	return;
        }
        
        UUID playerUuid = sender.getUUID();
        
		ladder = confirmLadder( sender, ladder );

        RankPlayer rankPlayer = getPlayer( sender, playerUuid );
        Rank pRank = rankPlayer.getRank( ladder );
		Rank pRankSecond = rankPlayer.getRank("default");
		Rank pRankAfter = null;
		LadderManager lm = PrisonRanks.getInstance().getLadderManager();
		boolean WillPrestige = false;

		// If the ladder's the prestige one, it'll execute all of this
		if (ladder.equalsIgnoreCase("prestiges")) {

			if (!(lm.getLadder("default").isPresent())){
				sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&c[ERROR] There isn't a default ladder! Please report this to an admin!"));
				return;
			}
			if (!(lm.getLadder("default").get().getLowestRank().isPresent())){
				sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&c[ERROR] Can't get the lowest rank! Please report this to an admin!"));
				return;
			}

			Rank rank = lm.getLadder("default").get().getLowestRank().get();

			while (rank.rankNext != null) {
				rank = rank.rankNext;
			}

			if (!(rank == pRankSecond)) {
				sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cYou aren't at the last rank!"));
				return;
			}
			// IF everything's ready, this will be true and the prestige method will start
			WillPrestige = true;
		}
        
        // Get currency if it exists, otherwise it will be null if the Rank has no currency:
        String currency = rankPlayer == null || pRank == null ? null : pRank.currency;

		boolean rankupWithSuccess = false;

        if ( ladder != null && rankPlayer != null ) {
        	RankupResults results = new RankUtil().rankupPlayer(rankPlayer, ladder, sender.getName());
        	
        	processResults( sender, null, results, true, null, ladder, currency );

        	if (results.getStatus() == RankupStatus.RANKUP_SUCCESS && mode == RankupModes.MAX_RANKS && !ladder.equals("prestiges")) {
        		rankUpPrivate( sender, ladder, mode );
        	}
        	if (results.getStatus() == RankupStatus.RANKUP_SUCCESS){
        		rankupWithSuccess = true;
			}

        	// Get the player rank after
        	pRankAfter = rankPlayer.getRank(ladder);

        }

        // Prestige method
		prestigePlayer(sender, rankPlayer, pRank, pRankAfter, lm, WillPrestige, rankupWithSuccess);
	}

	private void prestigePlayer(Player sender, RankPlayer rankPlayer, Rank pRank, Rank pRankAfter, LadderManager lm, boolean willPrestige, boolean rankupWithSuccess) {
		// Get the player rank after, just to check if it has success
    	Rank pRankSecond;
    	// Conditions
		if (willPrestige && rankupWithSuccess && pRankAfter != null && pRank != pRankAfter) {
			// Set the player rank to the first one of the default ladder
			PrisonAPI.dispatchCommand("ranks set rank " + sender.getName() + " " + lm.getLadder("default").get().getLowestRank().get().name + " default");
			// Get that rank
			pRankSecond = rankPlayer.getRank("default");
			// Check if the ranks match
			if (pRankSecond == lm.getLadder("default").get().getLowestRank().get()) {
				// Get economy
				EconomyIntegration economy = (EconomyIntegration) PrisonAPI.getIntegrationManager().getForType(IntegrationType.ECONOMY).orElseThrow(IllegalStateException::new);
				// Set the player balance to 0 (reset)
				economy.setBalance(sender, 0);
				// Send a message to the player because he did prestige!
				sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7[&3Congratulations&7] &3You've &6Prestige&3 to " + pRankAfter.tag + "&c!"));
			}
		}
	}


	@Command(identifier = "ranks promote", description = "Promotes a player to the next rank.",
    			permissions = "ranks.promote", onlyPlayers = true) 
    public void promotePlayer(CommandSender sender,
    	@Arg(name = "playerName", def = "", description = "Player name") String playerName,
        @Arg(name = "ladder", description = "The ladder to promote on.", def = "default") String ladder,
        @Arg(name = "chargePlayers", description = "Force the player to pay for the rankup", 
        					def = "no_charge") String chargePlayer
    		) {

    	Player player = getPlayer( sender, playerName );
    	
    	if (player == null) {
    		sender.sendMessage( "&3You must be a player in the game to run this command, " +
    															"and/or the player must be online." );
    		return;
    	}
    	
    	PromoteForceCharge pForceCharge = PromoteForceCharge.fromString( chargePlayer );
    	if ( pForceCharge == null|| pForceCharge == PromoteForceCharge.refund_player ) {
    		sender.sendMessage( 
    				String.format( "&3Invalid value for chargePlayer. Valid values are: %s %s", 
    						PromoteForceCharge.no_charge.name(), PromoteForceCharge.charge_player.name()) );
    		return;
    	}

        UUID playerUuid = player.getUUID();
        
		ladder = confirmLadder( sender, ladder );

        RankPlayer rankPlayer = getPlayer( sender, playerUuid );
        Rank pRank = rankPlayer.getRank( ladder );
        
        // Get currency if it exists, otherwise it will be null if the Rank has no currency:
        String currency = rankPlayer == null || pRank == null ? null : pRank.currency;
        
        

        if ( ladder != null && rankPlayer != null ) {
        	RankupResults results = new RankUtil().promotePlayer(rankPlayer, ladder, 
        												player.getName(), sender.getName(), pForceCharge);
        	
        	processResults( sender, player, results, true, null, ladder, currency );
        }
    }


    @Command(identifier = "ranks demote", description = "Demotes a player to the next lower rank.", 
    			permissions = "ranks.demote", onlyPlayers = true) 
    public void demotePlayer(CommandSender sender,
    	@Arg(name = "playerName", def = "", description = "Player name") String playerName,
        @Arg(name = "ladder", description = "The ladder to demote on.", def = "default") String ladder,
        @Arg(name = "chargePlayers", description = "Force the player to pay for the rankup", 
        				def = "no_charge") String refundPlayer
        ) {

    	Player player = getPlayer( sender, playerName );
    	
    	if (player == null) {
    		sender.sendMessage( "&3You must be a player in the game to run this command, " +
    															"and/or the player must be online." );
    		return;
    	}
    	
    	PromoteForceCharge pForceCharge = PromoteForceCharge.fromString( refundPlayer );
    	if ( pForceCharge == null || pForceCharge == PromoteForceCharge.charge_player ) {
    		sender.sendMessage( 
    				String.format( "&3Invalid value for refundPlayer. Valid values are: %s %s", 
    						PromoteForceCharge.no_charge.name(), PromoteForceCharge.refund_player.name()) );
    		return;
    	}
    	
        UUID playerUuid = player.getUUID();
        
		ladder = confirmLadder( sender, ladder );

        RankPlayer rankPlayer = getPlayer( sender, playerUuid );
        Rank pRank = rankPlayer.getRank( ladder );
        
        // Get currency if it exists, otherwise it will be null if the Rank has no currency:
        String currency = rankPlayer == null || pRank == null ? null : pRank.currency;

        if ( ladder != null && rankPlayer != null ) {
        	RankupResults results = new RankUtil().demotePlayer(rankPlayer, ladder, 
        												player.getName(), sender.getName(), pForceCharge);
        	
        	processResults( sender, player, results, false, null, ladder, currency );
        }
    }


    @Command(identifier = "ranks set rank", description = "Sets a play to a specified rank.", 
    			permissions = "ranks.setrank", onlyPlayers = true) 
    public void setRank(CommandSender sender,
    	@Arg(name = "playerName", def = "", description = "Player name") String playerName,
    	@Arg(name = "rankName", description = "The rank to assign to the player") String rank,
        @Arg(name = "ladder", description = "The ladder to demote on.", def = "default") String ladder) {

    	Player player = getPlayer( sender, playerName );
    	
    	if (player == null) {
    		sender.sendMessage( "&3You must be a player in the game to run this command, " +
    										"and/or the player must be online." );
    		return;
    	}

        setPlayerRank( player, rank, ladder, sender );
    }


	private void setPlayerRank( Player player, String rank, String ladder, CommandSender sender )
	{
		UUID playerUuid = player.getUUID();
        
		ladder = confirmLadder( sender, ladder );

        RankPlayer rankPlayer = getPlayer( sender, playerUuid );
        Rank pRank = rankPlayer.getRank( ladder );
        
        // Get currency if it exists, otherwise it will be null if the Rank has no currency:
        String currency = rankPlayer == null || pRank == null ? null : pRank.currency;

        if ( ladder != null && rankPlayer != null ) {
        	RankupResults results = new RankUtil().setRank(rankPlayer, ladder, rank, 
        												player.getName(), sender.getName());
        	
        	processResults( sender, player, results, true, rank, ladder, currency );
        }
	}



	public String confirmLadder( CommandSender sender, String ladderName ) {
		Optional<RankLadder> ladderOptional =
            PrisonRanks.getInstance().getLadderManager().getLadder(ladderName);

        // The ladder doesn't exist
        if (!ladderOptional.isPresent()) {
            Output.get().sendError(sender, "The ladder '%s' does not exist.", ladderName);
            ladderName = null;
        }
        return ladderName;
	}


	public RankPlayer getPlayer( CommandSender sender, UUID playerUuid ) {
		Optional<RankPlayer> playerOptional =
            PrisonRanks.getInstance().getPlayerManager().getPlayer(playerUuid);

        // Well, this isn't supposed to happen...
        if (!playerOptional.isPresent()) {
            Output.get().sendError(sender,
                "You don't exist! The server has no records of you. Try rejoining, " +
            									"or contact a server administrator for help.");
        }

        return playerOptional.isPresent() ? playerOptional.get() : null;
	}


	public void processResults( CommandSender sender, Player player, 
					RankupResults results, 
					boolean rankup, String rank, String ladder, String currency ) {
	
		switch (results.getStatus()) {
            case RANKUP_SUCCESS:
            	if ( rankup ) {
            		String message = String.format( "Congratulations! %s ranked up to rank '%s'. %s",
            				(player == null ? "You have" : player.getName()),
            				(results.getTargetRank() == null ? "" : results.getTargetRank().name), 
            				(results.getMessage() != null ? results.getMessage() : "") );
            		Output.get().sendInfo(sender, message);
            		Output.get().logInfo( "%s initiated rank change: %s", sender.getName(), message );
            	} else {
	            	String message = String.format( "Unfortunately, %s has been demoted to rank '%s'. %s",
            				(player == null ? "You have" : player.getName()),
            				(results.getTargetRank() == null ? "" : results.getTargetRank().name), 
            				(results.getMessage() != null ? results.getMessage() : ""));
            		Output.get().sendInfo(sender, message);
            		Output.get().logInfo( "%s initiated rank change: %s", sender.getName(), message );
				}
                break;
            case RANKUP_CANT_AFFORD:
                Output.get().sendError(sender,
                    "You don't have enough money to rank up! The next rank costs %s.",
                    RankUtil.doubleToDollarString(
                    				results.getTargetRank() == null ? 0 : results.getTargetRank().cost));
                break;
            case RANKUP_LOWEST:
            	Output.get().sendInfo(sender, "%s already at the lowest rank!",
            				(player == null ? "You are" : player.getName()));
            	break;
            case RANKUP_HIGHEST:
                Output.get().sendInfo(sender, "%s already at the highest rank!",
            				(player == null ? "You are" : player.getName()));
                break;
            case RANKUP_FAILURE:
                Output.get().sendError(sender,
                    "Failed to retrieve or write data. Your files may be corrupted. " +
                														"Alert a server administrator.");
                break;
            case RANKUP_NO_RANKS:
                Output.get().sendError(sender, "There are no ranks in this ladder.");
                break;
            case RANKUP_FAILURE_RANK_DOES_NOT_EXIST:
            	Output.get().sendError(sender, "The rank %s does not exist on this server.", rank);
            	break;
			case RANKUP_FAILURE_RANK_IS_NOT_IN_LADDER:            
				Output.get().sendError(sender, "The rank %s does not exist in the ladder %s.", rank, ladder);
				break;
            
			case RANKUP_FAILURE_CURRENCY_IS_NOT_SUPPORTED:
				Output.get().sendError(sender, "The currency, %s, is not supported by any " +
													"loaded economies.", results.getTargetRank().currency);
				break;
				
			case IN_PROGRESS:
				Output.get().sendError(sender, "Rankup failed to complete normally. No status was set.");
				break;
			default:
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
