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

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import tech.mcprison.prison.Prison;
import tech.mcprison.prison.commands.Arg;
import tech.mcprison.prison.commands.Command;
import tech.mcprison.prison.internal.CommandSender;
import tech.mcprison.prison.internal.Player;
import tech.mcprison.prison.internal.platform.Platform;
import tech.mcprison.prison.output.Output;
import tech.mcprison.prison.output.Output.DebugTarget;
import tech.mcprison.prison.ranks.PrisonRanks;
import tech.mcprison.prison.ranks.RankUtil;
import tech.mcprison.prison.ranks.RankUtil.PromoteForceCharge;
import tech.mcprison.prison.ranks.RankUtil.RankupModes;
import tech.mcprison.prison.ranks.RankUtil.RankupStatus;
import tech.mcprison.prison.ranks.RankupResults;
import tech.mcprison.prison.ranks.data.PlayerRank;
import tech.mcprison.prison.ranks.data.Rank;
import tech.mcprison.prison.ranks.data.RankLadder;
import tech.mcprison.prison.ranks.data.RankPlayer;
import tech.mcprison.prison.ranks.data.RankPlayerFactory;
import tech.mcprison.prison.ranks.managers.LadderManager;
import tech.mcprison.prison.ranks.managers.PlayerManager;
import tech.mcprison.prison.tasks.PrisonCommandTaskData;
import tech.mcprison.prison.tasks.PrisonCommandTasks;

/**
 * The commands for this module.
 *
 * @author Faizaan A. Datoo
 * @author GABRYCA
 * @author RoyalBlueRanger
 */
public class RankUpCommand 
				extends RankUpCommandMessages {

	public RankUpCommand() {
		super( "RankUpCommand" );
	}
	
    /*
     * /rankup command
     */
	
    @Command(identifier = "rankupMax", 
    			description = "Ranks up to the max rank that the player can afford. If the player has the " +
    					"perm ranks.rankupmax.prestige it will try to rankup prestige once it maxes out " +
    					"on the default ladder.", 
    			altPermissions = {"ranks.rankupmax.[ladderName]", "ranks.rankupmax.prestige"},
    			onlyPlayers = false) 
    public void rankUpMax(CommandSender sender,
    		@Arg(name = "ladder", description = "The ladder to rank up on.", def = "default")  String ladder 
    		) {

    	// Not supposed to check perms here... But it is a simple check, and it if works...
    	String perms = "ranks.rankupmax.";
    	if ( sender.hasPermission( perms + ladder) 
    			// || sender.hasPermission("ranks.rankupmax.prestiges")
    			) {
    		Output.get().logDebug( DebugTarget.rankup, 
    				"Rankup: cmd '/rankupmax %s'  Passed perm check: ranks.rankupmax.%s", 
    				ladder, ladder );
    		
    		boolean success = false;
    		
    		List<PrisonCommandTaskData> cmdTasks = new ArrayList<>();
    		StringBuilder sbRanks = new StringBuilder();
    		
    		
    		RankupModes mode = RankupModes.MAX_RANKS;
    		
    		if ( !LadderManager.LADDER_PRESTIGES.equalsIgnoreCase( ladder ) && 
    				!LadderManager.LADDER_DEFAULT.equalsIgnoreCase( ladder )) {
    			
    			success = rankUpPrivate(sender, "", ladder, mode, perms, cmdTasks, sbRanks );
    		}
    		else {
    			
    			// Run rankupmax on the default ladder only:
    			success = rankUpPrivate(sender, "", LadderManager.LADDER_DEFAULT, mode, perms, cmdTasks, sbRanks );
    			
    			// If they specified the prestiges ladder, then try to prestige that one rank:
    			if ( success && LadderManager.LADDER_PRESTIGES.equalsIgnoreCase( ladder ) ) {
    				
    				success = rankUpPrivate(sender, "", LadderManager.LADDER_PRESTIGES, RankupModes.ONE_RANK, perms, cmdTasks, sbRanks );
    			}
    		}
    		
			
			
			// submit cmdTasks
			if ( cmdTasks.size() > 0 ) {
				
				Player player = getPlayer( sender, null );
				submitCmdTasks( player, cmdTasks );
			}
			
			
			if ( sbRanks.length() > 0 ) {
				ranksRankupMaxSuccessMsg( sender, sbRanks );
			}
			
			// If the ran rankupmax for prestiges, and the last prestige was successful, then
			// try it all again!
			if ( success && LadderManager.LADDER_PRESTIGES.equalsIgnoreCase( ladder ) ) {
				rankUpMax( sender, ladder );
			}
		}
    	else {
    		Output.get().logDebug( DebugTarget.rankup, 
    				"Rankup: Failed: cmd '/rankupmax %s'  Does not have the permission ranks.rankupmax.%s", 
    				ladder, ladder );
    		rankupMaxNoPermissionMsg( sender, "ranks.rankupmax." + ladder );
    	}
    }


	@Command(identifier = "rankup", description = "Ranks up to the next rank.", 
			permissions = "ranks.user", altPermissions = "ranks.rankup.[ladderName]", onlyPlayers = false) 
    public void rankUp(CommandSender sender,
		@Arg(name = "ladder", description = "The ladder to rank up on.", def = "default")  String ladder,
		@Arg(name = "playerName", description = "Provides the player's name for the rankup, but" +
				"this can only be provided by a non-player such as console or ran from a script.", def = "")  String playerName
		) {
        
		if ( sender.isPlayer() ) {
			playerName = "";
		}
		
        if ( !sender.isPlayer() && playerName.length() == 0 ) {
        	Output.get().logInfo( rankupCannotRunFromConsoleMsg() );
        	return;
        }
        
		Output.get().logDebug( DebugTarget.rankup, 
				"Rankup: cmd '/rankup %s%s'  Processing ranks.rankup.%s", 
				ladder, 
				( playerName.length() == 0 ? "" : " " + playerName ),
				ladder
				);
        
		List<PrisonCommandTaskData> cmdTasks = new ArrayList<>();
		
    	rankUpPrivate(sender, playerName, ladder, RankupModes.ONE_RANK, "ranks.rankup.", cmdTasks, null );
    	
    	// submit cmdTasks
    	Player player = getPlayer( sender, playerName );
		submitCmdTasks( player, cmdTasks );
    	
    }

    private boolean rankUpPrivate(CommandSender sender, String playerName, String ladder, RankupModes mode, 
    		String permission, 
    		List<PrisonCommandTaskData> cmdTasks, 
    		StringBuilder sbRanks ) {

    	boolean rankupSuccess = false;
    	
        // RETRIEVE THE LADDER

    	boolean isPrestigesEnabled = Prison.get().getPlatform().getConfigBooleanFalse( "prestiges" ) || 
									 Prison.get().getPlatform().getConfigBooleanFalse( "prestige.enabled" );
    	boolean isLadderPrestiges = ladder.equalsIgnoreCase(LadderManager.LADDER_PRESTIGES);
    	boolean isLadderDefault = ladder.equalsIgnoreCase(LadderManager.LADDER_DEFAULT);
    	String permCheck = permission + ladder.toLowerCase();
    	
        // This player has to have permission to rank up on this ladder, but
    	// ignore if either the default or prestiges ladder.  This only is to check for
    	//  other ladders.
        if (!( isLadderPrestiges && isPrestigesEnabled ) && 
		        	!isLadderDefault && 
		        	!sender.hasPermission( permCheck )) {

        	Output.get().logDebug( DebugTarget.rankup, 
        			"Rankup: rankUpPrivate: failed rankup perm check. Missing perm: %s",
        				permCheck );
        	
        	rankupMaxNoPermissionMsg( sender, permCheck );
            return false;
        }

        
        // 
        if ( mode == null ) {
        	Output.get().logInfo( rankupInternalFailureMsg() );
        	return false;
        }
        
        
		if ( sender.isPlayer() ) {
			playerName = "";
		}
		
        if ( !sender.isPlayer() && playerName.length() == 0 ) {
        	Output.get().logInfo( rankupCannotRunFromConsoleMsg() );
        	return false;
        }
        

        // Player will always be the player since they have to be online and must be a player:
        Player player = getPlayer( sender, playerName );
        
        if ( player == null ) {
        	rankupInvalidPlayerNameMsg( sender, playerName );
        	return false;
        }

        
        //UUID playerUuid = player.getUUID();
        
		ladder = confirmLadder( sender, ladder );
		if ( ladder == null ) {
			// ladder cannot be null, 
			return false;
		}

		LadderManager lm = PrisonRanks.getInstance().getLadderManager();
		RankLadder targetLadder = lm.getLadder( ladder );
		
       	if ( targetLadder == null ){
    		rankupErrorNoLadderMsg( sender, ladder );
    		return false;
    	}
    	
    	if (!targetLadder.getLowestRank().isPresent()){
    		rankupErrorNoRankOnLadderMsg( sender, ladder );
    		return false;
    	}
		

    	
        RankPlayer rankPlayer = getRankPlayer( sender, player.getUUID(), player.getName() );
        
        
        
        // Get the player's next rank on default ladder, or if at end then it will return the next
        // prestiges rank.
        PlayerRank playerRankTarget = rankPlayer.getNextPlayerRank();
        
        
        // If the nextRank is null or the ladder does not match selected ladder, then exit:
        if ( playerRankTarget == null || playerRankTarget.getRank() == null  ||
        		!playerRankTarget.getRank().getLadder().getName().equalsIgnoreCase( ladder )	) {
        	
        	return false;
        }
        
        
        RankPlayerFactory rankPlayerFactory = new RankPlayerFactory();
        
        
        
        // If the target ladder is either default or prestiges, then use the getNextPlayerRank() value
        // that is provided.  Only use the following code if not these two ladders.
        if ( !ladder.equalsIgnoreCase( LadderManager.LADDER_DEFAULT ) && 
        		!ladder.equalsIgnoreCase( LadderManager.LADDER_PRESTIGES ) ) {
        	

        	PlayerRank playerRankCurrent = rankPlayerFactory.getRank( rankPlayer, ladder );
//        PlayerRank playerRankCurrent = rankPlayer.getPlayerRankDefault();

        	
        	// If the player does not have a rank on the current ladder, then assign the
        	// default rank for the ladder to be their next rank.
        	if ( playerRankCurrent == null ) {
        		
        		playerRankTarget = rankPlayer.calculateTargetPlayerRank( 
        				targetLadder.getLowestRank().get() );
        		
//        	playerRankTarget = rankPlayerFactory.createPlayerRank( 
//        			targetLadder.getLowestRank().get() );
        	}
        	else {
        		
        		playerRankTarget = rankPlayer.calculateTargetPlayerRank( playerRankCurrent.getRank() );
//        	playerRankTarget = playerRankCurrent.getTargetPlayerRankForPlayer( rankPlayer, 
//        					playerRankCurrent.getRank() );
        	}
        
        }
        

        
        
        
        		
        
        Output.get().logDebug( DebugTarget.rankup, 
    			"Rankup: rankUpPrivate: RankPlayer %s  playerRankTarget %s", 
    					(rankPlayer == null ? "null" : "true"), 
    					(playerRankTarget == null ? "null" : "true") );
        
        // If a player has a rank on the ladder get their rank, otherwise null:
        Rank pRankTarget = playerRankTarget.getRank();
        
//        Rank pRankAfter = null;
        boolean canPrestige = false;
        
        // If the player is trying to prestige, then the following must be ran to setup the prestige checks:
        if (ladder.equalsIgnoreCase(LadderManager.LADDER_PRESTIGES)) {

        	RankLadder rankLadder = lm.getLadder(LadderManager.LADDER_DEFAULT);
        	
        	if ( rankLadder == null ){
        		rankupErrorNoDefaultLadderMsg( sender );
        		return false;
        	}
        	
        	if (!rankLadder.getLowestRank().isPresent()){
        		rankupErrorNoLowerRankMsg( sender );
        		return false;
        	}
        	
        	// gets the rank on the default ladder. Used if ladder is not default.
        	PlayerRank pRankDefaultLadder = rankPlayerFactory.getRank( rankPlayer, LadderManager.LADDER_DEFAULT);
        	if ( pRankDefaultLadder == null ) {
        		rankupErrorPlayerNotOnDefaultLadder( sender, rankPlayer );
        	}
        	
        	Rank playersRankOnDefaultLadder = pRankDefaultLadder.getRank();
        	// On the default ladder, the player must be at the last rank:
        	// The last rank will never have a rankNext (it will be null):
        	if ( playersRankOnDefaultLadder.getRankNext() != null ) {
        		rankupNotAtLastRankMsg( sender );
        		return false;
        	}
        	
        	// If force sellall, then perform the sellall here:
        	if ( Prison.get().getPlatform().getConfigBooleanFalse( "prestige.force-sellall" ) ) {
        		
        		Prison.get().getPlatform().sellall( rankPlayer );
        	}
        	
        	
        	// IF everything's ready, this will be true if and only if pRank is not null,
        	// and the prestige method will start
        	canPrestige = true;
        }
        
        
        // Get currency if it exists, otherwise it will be null if the Rank has no currency:
        String currency = rankPlayer == null || pRankTarget == null ? null : pRankTarget.getCurrency();
        
        
        if (rankPlayer != null ) {
        	
        	// Performs the actual rankup here:
        	RankupResults results = new RankUtil().rankupPlayer(player, rankPlayer, ladder, 
        						sender.getName(), cmdTasks );
        	
        	
        	processResults( sender, player.getName(), results, null, ladder, currency, sbRanks );
        	
        	// If the last rankup attempt was successful and they are trying to rankup as many times as possible: 
        	// Note they used to restrict rankupmax from working on prestige ladder... 
        	if (results.getStatus() == RankupStatus.RANKUP_SUCCESS && mode == RankupModes.MAX_RANKS ) {
//    		if (results.getStatus() == RankupStatus.RANKUP_SUCCESS && mode == RankupModes.MAX_RANKS && 
//    				!ladder.equals(LadderManager.LADDER_PRESTIGES)) {
        		rankUpPrivate( sender, playerName, ladder, mode, permission, cmdTasks, sbRanks );
        	}
        	if (results.getStatus() == RankupStatus.RANKUP_SUCCESS){
        		rankupSuccess = true;
        	}
        	
        	
        	// Get the player rank after
//        	PlayerRank playerRankAfter = rankPlayer.getNextPlayerRank();
//        	PlayerRank playerRankAfter = rankPlayerFactory.getRank( rankPlayer, ladder );
        	
//        	if ( playerRankAfter != null ) {
//        		
//        		pRankAfter = playerRankAfter.getRank();
//        	}
        	
        	// Prestige method if canPrestige and a successful rankup. 
        	// pRankTarget now contains the target rank prior to processing the rankup.  SO it should be
        	// the same as pRankAfter, but if it is wrong, then rankupWithSuccess will not be true.  So ignore...
        	if ( canPrestige && rankupSuccess ) {
        		prestigePlayer( sender, rankPlayer, lm, cmdTasks, sbRanks );
//        		prestigePlayer( sender, player, rankPlayer, pRankAfter, lm );
        		
        	}
        	else if ( canPrestige ) {
        		rankupNotAbleToPrestigeMsg( sender );
        	}
        	
        }
        
        return rankupSuccess;
	}

    /**
     * <p>Perform the final prestige actions if prestige is requested (canPrestige) and if the 
     * rankup was successful.  It also assumes that pRankAfter is not null and not the same
     * as pRank, which would indicate something went wrong with the rankup.
     * </p>
     * 
     * <p>This function will reset the player's default ladder, if the configuration setting
     * 'prestige.resetDefaultLadder' has been enabled.  Otherwise the default ladder is not 
     * modified.
     * </p>
     * 
     * <p>This function will also reset the player's balance if the configuration 
     * 'prestige.resetMoney' is enabled.
     * </p>
     * 
     * 
     * @param sender
     * @param player
     * @param rankPlayer
     * @param pRankAfter
     * @param lm
     */
	private void prestigePlayer(CommandSender sender, RankPlayer rankPlayer, 
//						Rank pRankAfter, 
						LadderManager lm, 
						List<PrisonCommandTaskData> cmdTasks, StringBuilder sbRanks ) {
		
       	Output.get().logDebug( DebugTarget.rankup, "Rankup: prestigePlayer: ");

		Platform platform = Prison.get().getPlatform();
		boolean resetBalance = platform.getConfigBooleanTrue( "prestige.resetMoney" );
		boolean resetDefaultLadder = platform.getConfigBooleanTrue( "prestige.resetDefaultLadder" );
		
		boolean success = true;
		
		if ( resetDefaultLadder ) {
			
			// Get the player rank after, just to check if it has success Conditions
//			if (willPrestige && rankupWithSuccess && pRankAfter != null && pRank != pRankAfter) {
				// Set the player rank to the first one of the default ladder
				
			
			RankLadder ladder = lm.getLadder(LadderManager.LADDER_DEFAULT);
			Rank dRank = ladder.getLowestRank().get();
			setPlayerRank( rankPlayer, ladder, dRank, sender, cmdTasks, sbRanks );

			
//			String ladderName = LadderManager.LADDER_DEFAULT;
//			String defaultRank = lm.getLadder(LadderManager.LADDER_DEFAULT).getLowestRank().get().getName();
//			
//			setPlayerRank( rankPlayer, defaultRank, ladderName, sender );
			
				// Call the function directly and skip using dispatch commands:
//				setRank( sender, player.getName(), 
//						lm.getLadder(LadderManager.LADDER_DEFAULT).getLowestRank().get().getName(), 
//							LadderManager.LADDER_DEFAULT );
				
				
				PlayerRank playerRankSecond = rankPlayer.getPlayerRankDefault();
				

				
				if ( playerRankSecond != null ) {
					
					Rank pRankSecond = playerRankSecond.getRank();
					// Check if the ranks match
					
					if ( !pRankSecond.equals( lm.getLadder(LadderManager.LADDER_DEFAULT).getLowestRank().get()) ) {
						
						rankupNotAbleToResetRankMsg( sender );
						success = false;
					}
				}
				else {
					
					rankupNotAbleToResetRankMsg( sender );
					success = false;
				}
//			}
		}
		
		if ( success && resetBalance ) {
			
			// set the player's balance to zero:
			rankPlayer.setBalance( 0 );
			prestigePlayerBalanceSetToZeroMsg( sender );
		}
		
		
		
		PlayerRank newPrestigeRank = rankPlayer.getPlayerRankPrestiges();
		Rank newPRank = newPrestigeRank == null || newPrestigeRank.getRank() == null ? 
							null :
							newPrestigeRank.getRank();
		
		String title = newPRank == null || newPRank.getTag() == null ? 
							newPRank.getName() : 
							newPRank.getTag();
		if ( success ) {
			// Send a message to the player because he did prestige!

			prestigePlayerSucessfulMsg( sender, title );
		}
		else {
			
			prestigePlayerFailureMsg( sender, title );
		}

	}


	@Command(identifier = "ranks promote", description = "Promotes a player to the next rank.",
    			permissions = "ranks.promote", onlyPlayers = false) 
    public void promotePlayer(CommandSender sender,
    	@Arg(name = "playerName", def = "", description = "Player name") String playerName,
        @Arg(name = "ladder", description = "The ladder to promote on.", def = "default") String ladder,
        @Arg(name = "chargePlayers", description = "Force the player to pay for the rankup (no_charge, charge_player)", 
        					def = "no_charge") String chargePlayer
    		) {

    	Player player = getPlayer( sender, playerName );
    	
    	if (player == null) {
    		ranksPromotePlayerMustBeOnlineMsg( sender );
    		return;
    	}
    	
    	PromoteForceCharge pForceCharge = PromoteForceCharge.fromString( chargePlayer );
    	if ( pForceCharge == null|| pForceCharge == PromoteForceCharge.refund_player ) {
    		
    		ranksPromotePlayerInvalidChargeValueMsg( sender );
    		return;
    	}

        UUID playerUuid = player.getUUID();
        
		ladder = confirmLadder( sender, ladder );

		RankPlayerFactory rankPlayerFactory = new RankPlayerFactory();
		
        RankPlayer rankPlayer = getRankPlayer( sender, playerUuid, player.getName() );
        PlayerRank playerRank = rankPlayerFactory.getRank( rankPlayer, ladder );
        
        if ( playerRank != null ) {
        	
        	Rank pRank = playerRank.getRank();
        	
        	// Get currency if it exists, otherwise it will be null if the Rank has no currency:
        	String currency = rankPlayer == null || pRank == null ? null : pRank.getCurrency();
        	
        	if ( ladder != null && rankPlayer != null ) {
        		
        		List<PrisonCommandTaskData> cmdTasks = new ArrayList<>();
        		
        		RankupResults results = new RankUtil().promotePlayer(player, rankPlayer, ladder, 
        				player.getName(), sender.getName(), pForceCharge, cmdTasks );

        		// submit cmdTasks...
    			submitCmdTasks( player, cmdTasks );
        		
        		processResults( sender, player.getName(), results, null, ladder, currency, null );
        	}
        }
        else {
        	// Message: Player is not on the ladder
        }
    }


    @Command(identifier = "ranks demote", description = "Demotes a player to the next lower rank.", 
    			permissions = "ranks.demote", onlyPlayers = false) 
    public void demotePlayer(CommandSender sender,
    	@Arg(name = "playerName", def = "", description = "Player name") String playerName,
        @Arg(name = "ladder", description = "The ladder to demote on.", def = "default") String ladder,
        @Arg(name = "chargePlayers", description = "Refund the player for the demotion (no_charge, refund_player)", 
        				def = "no_charge") String refundPlayer
        ) {

    	Player player = getPlayer( sender, playerName );
    	
    	if (player == null) {
    		ranksPromotePlayerMustBeOnlineMsg( sender );
    		return;
    	}
    	
    	PromoteForceCharge pForceCharge = PromoteForceCharge.fromString( refundPlayer );
    	if ( pForceCharge == null || pForceCharge == PromoteForceCharge.charge_player ) {
    		ranksDemotePlayerInvalidRefundValueMsg( sender );
    		return;
    	}
    	
        UUID playerUuid = player.getUUID();
        
		ladder = confirmLadder( sender, ladder );
		if ( ladder == null ) {
			// Already displayed error message about ladder not existing:
			return;
		}

		RankPlayerFactory rankPlayerFactory = new RankPlayerFactory();
		
        RankPlayer rankPlayer = getRankPlayer( sender, playerUuid, player.getName() );
        PlayerRank playerRank = rankPlayerFactory.getRank( rankPlayer, ladder );
        
        if ( playerRank != null ) {
        	
        	Rank pRank = playerRank.getRank();
        	
        	// Get currency if it exists, otherwise it will be null if the Rank has no currency:
        	String currency = rankPlayer == null || pRank == null ? null : pRank.getCurrency();
        	
        	if ( ladder != null && rankPlayer != null ) {
        		
        		List<PrisonCommandTaskData> cmdTasks = new ArrayList<>();
        		
        		RankupResults results = new RankUtil().demotePlayer(player, rankPlayer, ladder, 
        				player.getName(), sender.getName(), pForceCharge, cmdTasks );
        		
        		// submit cmdTasks
    			submitCmdTasks( player, cmdTasks );
        		
        		processResults( sender, player.getName(), results, null, ladder, currency, null );
        	}
        }
        else {
        	// Message: Player is not on the ladder
        }
    }


    @Command(identifier = "ranks set rank", description = "Sets a player to a specified rank on a ladder, " +
    		"or remove a player from a ladder (delete player rank).  Or if you use '*all*' for player name, " +
    		"then it will run this command on all players registered with Prison. When *all* is combined with " +
    		"the rankName '*same*' it will reapply the same rank to each player which will rerun the rank " +
    		"commands for the players.  If rank 'A' is your starting rank, you can use '*all*' and 'A' to " +
    		"reset all players to the starting rank; next you will need to -remove- all prestige ladders " +
    		"from all players too.", 
    			permissions = "ranks.setrank", onlyPlayers = false) 
    public void setRank(CommandSender sender,
    	@Arg(name = "playerName", def = "", description = "Player name, or [*all*]") String playerName,
    	@Arg(name = "rankName", description = "The rank to assign to the player, or [-remove-, *same*] " +
    						"to deleete the player from the rank.") String rank,
        @Arg(name = "ladder", description = "The ladder to demote on.", def = "default") String ladder) {

    	if ( "*all*".equalsIgnoreCase( playerName )) {
    		PlayerManager pm = PrisonRanks.getInstance().getPlayerManager();
    		
    		for ( RankPlayer player : pm.getPlayers() ) {
    			
    			Player targetPlayer = getPlayer( null, player.getName() );
    			if ( targetPlayer != null ) {
    				
    				boolean isSameRank = rank.equalsIgnoreCase("*same*");

    				RankPlayerFactory rankPlayerFactory = new RankPlayerFactory();
    				
    				PlayerRank pRank = rankPlayerFactory.getRank( player, ladder );
    				String rankNameCurrent = isSameRank && 
    						pRank != null && 
    						pRank.getRank() != null ? 
    								pRank.getRank().getName() : "";
    				
    				String targetRank = isSameRank ? rankNameCurrent : rank;
    				setPlayerRank( targetPlayer, targetRank, ladder, sender );
    			}
    		}
    		
    	}
    	else {
    		
    		Player player = getPlayer( sender, playerName );
    		
    		if (player == null) {
    			ranksPromotePlayerMustBeOnlineMsg( sender );
    			return;
    		}
    		
    		setPlayerRank( player, rank, ladder, sender );
    	}
    }

    
    @Command(identifier = "ranks removeRank", description = "Removes a player from a specified ladder " +
    		"(delete player rank). This is an alias for /ranks set rank <playerName> -remove- <ladder>.", 
    		permissions = "ranks.setrank", onlyPlayers = false) 
    public void removeRank(CommandSender sender,
    		@Arg(name = "playerName", def = "", description = "Player name") String playerName,
    		@Arg(name = "ladder", description = "The ladder to demote on.", def = "default") String ladder) {
    	
    	setRank( sender, playerName, "-remove-", ladder );
    	
    }
    
    
    public void setPlayerRank( RankPlayer rankPlayer, Rank pRank ) {
        
        if ( rankPlayer != null ) {
        	
        	List<PrisonCommandTaskData> cmdTasks = new ArrayList<>();
        	
        	RankupResults results = 
        			new RankUtil().setRank(rankPlayer, rankPlayer, 
        						pRank.getLadder().getName(), pRank.getName(), 
        												rankPlayer.getName(), rankPlayer.getName(), 
        												cmdTasks );
        	
        	// submit cmdTasks
        	Player player = getPlayer( null, rankPlayer.getName() );
			submitCmdTasks( player, cmdTasks );
        	
        	processResults( rankPlayer, rankPlayer.getName(), results, 
        			pRank.getName(), pRank.getLadder().getName(), 
        			pRank.getCurrency(), null );
        }
    }
    
    /**
     * Added on 2022-07-04... called from: 
     * tech.mcprison.prison.ranks.data.RankPlayerFactory.firstJoin(RankPlayer)
     * 
     * @param rankPlayer
     * @param pRank
     */
    public void setPlayerRankFirstJoin( RankPlayer rankPlayer, Rank pRank ) {
    	
    	if ( rankPlayer != null ) {
    		
    		List<PrisonCommandTaskData> cmdTasks = new ArrayList<>();
    		
    		RankupResults results = 
    				new RankUtil().setRank(rankPlayer, rankPlayer, 
    						pRank.getLadder().getName(), pRank.getName(), 
    						rankPlayer.getName(), "FirstJoinEvent", 
    						cmdTasks );
    		
    		// submit cmdTasks
    		Player player = getPlayer( null, rankPlayer.getName() );
    		submitCmdTasks( player, cmdTasks );
    		
    		processResults( rankPlayer, rankPlayer.getName(), results, 
    				pRank.getName(), pRank.getLadder().getName(), 
    				pRank.getCurrency(), null );
    	}
    }
    
    
    private void setPlayerRank( RankPlayer rankPlayer, RankLadder ladder, Rank pRank,
    		CommandSender sender, 
    		List<PrisonCommandTaskData> cmdTasks, StringBuilder sbRanks ) {
    	
    	// Get currency if it exists, otherwise it will be null if the Rank has no currency:
    	String currency = rankPlayer == null || pRank == null ? null : pRank.getCurrency();
    	
    	
    	RankupResults results = new RankUtil().setRank( rankPlayer, rankPlayer, 
    			ladder.getName(), pRank.getName(), 
    			rankPlayer.getName(), sender.getName(), cmdTasks );
    	
    	
    	processResults( sender, rankPlayer.getName(), results, pRank.getName(), ladder.getName(), 
    			currency, sbRanks );
    	
    }
    
	private void setPlayerRank( Player player, String rank, String ladderName, CommandSender sender ) {
		UUID playerUuid = player.getUUID();
        
       	Output.get().logDebug( DebugTarget.rankup, "Rankup: setPlayerRank: ");
	
		ladderName = confirmLadder( sender, ladderName );

        RankPlayer rankPlayer = getRankPlayer( sender, playerUuid, player.getName() );
        
        if ( ladderName != null && rankPlayer != null ) {

//        	RankLadder rLadder = PrisonRanks.getInstance().getLadderManager().getLadder( ladderName );
        	
        	Rank pRank = PrisonRanks.getInstance().getRankManager().getRank( rank );
        	
        	// Get currency if it exists, otherwise it will be null if the Rank has no currency:
        	String currency = rankPlayer == null || pRank == null ? null : pRank.getCurrency();
        	
        	List<PrisonCommandTaskData> cmdTasks = new ArrayList<>();
        	
        	RankupResults results = new RankUtil().setRank(player, rankPlayer, ladderName, rank, 
        			player.getName(), sender.getName(), cmdTasks );
        	
        	// submit cmdTasks
			submitCmdTasks( player, cmdTasks );
        	
        	processResults( sender, player.getName(), results, rank, ladderName, currency, null );
        }
	}



	public String confirmLadder( CommandSender sender, String ladderName ) {
		String results = null;
		RankLadder ladder = PrisonRanks.getInstance().getLadderManager().getLadder(ladderName);

        // The ladder doesn't exist
        if ( ladder == null ) {
        	ranksConfirmLadderMsg( sender, ladderName );
        }
        else {
        	results = ladder.getName();
        }
        return results;
	}


	public RankPlayer getRankPlayer( CommandSender sender, UUID playerUuid, String playerName ) {
		
		RankPlayer player =
							PrisonRanks.getInstance().getPlayerManager().getPlayer(playerUuid, playerName);

        // Well, this isn't supposed to happen...
        if ( player == null ) {
        	ranksRankupFailureToGetRankPlayerMsg( sender );
        }

        return player;
	}


	public void processResults( CommandSender sender, String playerName, 
					RankupResults results, 
					String rank, String ladder, String currency, 
					StringBuilder sbRanks ) {
	
		switch (results.getStatus()) {
            case RANKUP_SUCCESS:
            	
            	ranksRankupSuccessMsg( sender, playerName, results, sbRanks );
            	
            	break;
            	
            case DEMOTE_SUCCESS:
            	ranksRankupSuccessMsg( sender, playerName, results, null );

                break;
            case RANKUP_CANT_AFFORD:
            	ranksRankupCannotAffordMsg( sender, results );
	            	
                break;
            case RANKUP_LOWEST:
            	ranksRankupLowestRankMsg( sender, playerName, results );
            	
            	break;
            case RANKUP_HIGHEST:
            	ranksRankupHighestRankMsg( sender, playerName, results );
            	
                break;
            case RANKUP_FAILURE:
            	ranksRankupFailureMsg( sender );

            	break;
            case RANKUP_FAILURE_COULD_NOT_LOAD_PLAYER:
            	ranksRankupFailureCouldNotLoadPlayerMsg( sender );
            	
            	break;
            case RANKUP_FAILURE_COULD_NOT_LOAD_LADDER:
            	ranksRankupFailureCouldNotLoadLadderMsg( sender );
            	
            	break;
            case RANKUP_FAILURE_UNABLE_TO_ASSIGN_RANK:
            	ranksRankupFailureUnableToAssignRankMsg( sender );
            	
            	break;
            case RANKUP_FAILURE_COULD_NOT_SAVE_PLAYER_FILE:
            	ranksRankupFailureCouldNotSavePlayerFileMsg( sender );
            	
            	break;
            case RANKUP_NO_RANKS:
            	ranksRankupFailureNoRanksMsg( sender );
            	
                break;
            case RANKUP_FAILURE_RANK_DOES_NOT_EXIST:
            	ranksRankupFailureRankDoesNotExistMsg( sender, rank );
            	
            	break;
			case RANKUP_FAILURE_RANK_IS_NOT_IN_LADDER:
				ranksRankupFailureRankIsNotInLadderMsg( sender, rank, ladder );
				
				break;
            
			case RANKUP_FAILURE_CURRENCY_IS_NOT_SUPPORTED:
				ranksRankupFailureCurrencyIsNotSupportedMsg( sender, results.getTargetRank().getCurrency() );
				
				break;
				
			case RANKUP_FAILURE_ECONOMY_FAILED:
				// TODO externalize message
				sender.sendMessage( "Failed to adjust player's balance. Could be an issue with vault or " +
						"a cache timing issue. Try again." );
				
				break;
				
			case RANKUP_LADDER_REMOVED:
				ranksRankupFailureLadderRemovedMsg( sender, ladder );
				
				break;
				
			case RANKUP_FAILURE_REMOVING_LADDER:
				ranksRankupFailureRemovingLadderMsg( sender, ladder );
				
				break;
				
			case IN_PROGRESS:
				ranksRankupFailureInProgressMsg( sender );
				
				break;
			default:
				break;
        }
	}

	
    private void submitCmdTasks( Player player, List<PrisonCommandTaskData> cmdTasks )
	{
    	
    	PrisonCommandTasks.submitTasks( player, cmdTasks );
		
	}
    
}
