package tech.mcprison.prison.ranks.commands;

import java.text.DecimalFormat;

import tech.mcprison.prison.Prison;
import tech.mcprison.prison.commands.BaseCommands;
import tech.mcprison.prison.internal.CommandSender;
import tech.mcprison.prison.localization.Localizable;
import tech.mcprison.prison.output.Output;
import tech.mcprison.prison.ranks.PrisonRanks;
import tech.mcprison.prison.ranks.RankUtil.PromoteForceCharge;
import tech.mcprison.prison.ranks.RankUtil.RankupStatus;
import tech.mcprison.prison.ranks.RankupResults;
import tech.mcprison.prison.ranks.data.PlayerRank;
import tech.mcprison.prison.ranks.data.Rank;
import tech.mcprison.prison.ranks.data.RankPlayer;

public class RankUpCommandMessages 
		extends BaseCommands {

	public RankUpCommandMessages( String cmdGroup )
	{
		super( cmdGroup );
	}

	
	protected void rankupMaxNoPermissionMsg( CommandSender sender, String permission ) {
		PrisonRanks.getInstance().getRanksMessages()
				.getLocalizable( "ranks_rankup__no_permission" )
				.withReplacements( permission.toLowerCase() )
				.sendTo( sender );
	}
	
	protected String rankupCannotRunFromConsoleMsg() {
		return PrisonRanks.getInstance().getRanksMessages()
    			.getLocalizable( "ranks_rankup__cannot_run_from_console" )
    			.localize();
	}
	
	protected String rankupInternalFailureMsg() {
		return PrisonRanks.getInstance().getRanksMessages()
    			.getLocalizable( "ranks_rankup__internal_failure" )
				.localize();
	}
	
	protected void rankupErrorNoDefaultLadderMsg( CommandSender sender ) {
		PrisonRanks.getInstance().getRanksMessages()
				.getLocalizable( "ranks_rankup__error_no_default_ladder" )
				.sendTo( sender );
	}

	protected void rankupErrorNoLowerRankMsg( CommandSender sender ) {
		PrisonRanks.getInstance().getRanksMessages()
				.getLocalizable( "ranks_rankup__error_no_lower_rank" )
				.sendTo( sender );
	}
	
	protected void rankupErrorNoLadderMsg( CommandSender sender, String ladderName ) {
		PrisonRanks.getInstance().getRanksMessages()
		.getLocalizable( "ranks_rankup__error_no_ladder" )
		.withReplacements( ladderName )
		.sendTo( sender );
	}
	
	protected void rankupErrorNoRankOnLadderMsg( CommandSender sender, String ladderName ) {
		PrisonRanks.getInstance().getRanksMessages()
		.getLocalizable( "ranks_rankup__error_no_lower_rank_on_ladder" )
		.withReplacements( ladderName )
		.sendTo( sender );
	}
	
	protected void rankupErrorPlayerNotOnDefaultLadder( CommandSender sender, 
							RankPlayer rankPlayer ) {
		PrisonRanks.getInstance().getRanksMessages()
		.getLocalizable( "ranks_rankup__error_player_not_on_default_ladder" )
		.withReplacements( rankPlayer.getName() )
		.sendTo( sender );
	}
	
	protected void rankupNotAtLastRankMsg( CommandSender sender ) {
		PrisonRanks.getInstance().getRanksMessages()
				.getLocalizable( "ranks_rankup__not_at_last_rank" )
				.sendTo( sender );
	}
	
	protected void rankupNotAbleToPrestigeMsg( CommandSender sender ) {
		PrisonRanks.getInstance().getRanksMessages()
				.getLocalizable( "ranks_rankup__not_able_to_prestige" )
				.sendTo( sender );
	}
	
	protected void rankupNotAbleToResetRankMsg( CommandSender sender ) {
		PrisonRanks.getInstance().getRanksMessages()
				.getLocalizable( "ranks_rankup__not_able_to_reset_rank" )
				.sendTo( sender );
	}
	
	
	
	protected void prestigePlayerBalanceSetToZeroMsg( CommandSender sender ) {
		PrisonRanks.getInstance().getRanksMessages()
				.getLocalizable( "ranks_rankup__balance_set_to_zero" )
				.sendTo( sender );
	}
	
	protected void prestigePlayerSucessfulMsg( CommandSender sender, String tag ) {
		PrisonRanks.getInstance().getRanksMessages()
				.getLocalizable( "ranks_rankup__prestige_successful" )
				.withReplacements( tag )
				.sendTo( sender );
	}
	
	protected void prestigePlayerFailureMsg( CommandSender sender, String tag ) {
		PrisonRanks.getInstance().getRanksMessages()
				.getLocalizable( "ranks_rankup__prestige_failure" )
				.withReplacements( tag )
				.sendTo( sender );
	}

	
	protected void ranksPromotePlayerMustBeOnlineMsg( CommandSender sender ) {
		PrisonRanks.getInstance().getRanksMessages()
				.getLocalizable( "ranks_rankup__rankup_failure_must_be_online_player" )
				.sendTo( sender );
	}
	
	protected void ranksPromotePlayerInvalidChargeValueMsg( CommandSender sender ) {
		PrisonRanks.getInstance().getRanksMessages()
				.getLocalizable( "ranks_rankup__invalid_charge_value" )
				.withReplacements(
						PromoteForceCharge.no_charge.name(), 
						PromoteForceCharge.charge_player.name()
						)
				.sendTo( sender );
	}
	
	protected void ranksDemotePlayerInvalidRefundValueMsg( CommandSender sender ) {
		PrisonRanks.getInstance().getRanksMessages()
				.getLocalizable( "ranks_rankup__invalid_refund_value" )
				.withReplacements(
						PromoteForceCharge.no_charge.name(), 
						PromoteForceCharge.refund_player.name()
						)
				.sendTo( sender );
	}
	
	protected void ranksConfirmLadderMsg( CommandSender sender, String ladderName ) {
		PrisonRanks.getInstance().getRanksMessages()
				.getLocalizable( "ranks_rankup__rankup_failure_invalid_ladder" )
				.withReplacements( ladderName )
				.sendTo( sender );
	}
	
	
	protected void ranksRankupFailureToGetRankPlayerMsg( CommandSender sender ) {
       	PrisonRanks.getInstance().getRanksMessages()
	    		.getLocalizable( "ranks_rankup__rankup_failure_to_get_rankplayer" )
	    		.sendTo( sender );
	}
	
	
	
	protected void ranksRankupSuccessMsg( CommandSender sender, String playerName, 
			RankupResults results ) {
		
		
       	String messageId = results.getStatus() == RankupStatus.DEMOTE_SUCCESS ? 
       					"ranks_rankup__demote_success" :
       					"ranks_rankup__rankup_success" ;

    	String messagNoPlayerName = PrisonRanks.getInstance().getRanksMessages()
    			.getLocalizable( "ranks_rankup__rankup_no_player_name" ).localize();
    	
//		PlayerRank tpRank = results.getPlayerRankTarget();
		Rank tRank = results.getTargetRank();
		
    	
    	Localizable localManager = PrisonRanks.getInstance().getRanksMessages()
    			.getLocalizable( messageId )
    			.withReplacements(
    					
    					(playerName == null ? messagNoPlayerName : playerName),
    					(tRank == null ? "" : tRank.getName()), 
    					(results.getMessage() != null ? results.getMessage() : "")
    				);
    	
    	Localizable localManagerLog = PrisonRanks.getInstance().getRanksMessages()
    			.getLocalizable( "ranks_rankup__log_rank_change" )
    			.withReplacements(
    					
    					sender.getName(), localManager.localize()
    				);           	
    	Output.get().logInfo( localManagerLog.localize() );

    	
    	if ( Prison.get().getPlatform().getConfigBooleanFalse( "broadcast-rankups" ) ) {
    		String messagNoPlayerNameBroadcast = PrisonRanks.getInstance().getRanksMessages()
    				.getLocalizable( "ranks_rankup__rankup_no_player_name_broadcast" ).localize();
    		
    		PrisonRanks.getInstance().getRanksMessages()
        		.getLocalizable( messageId )
        		.withReplacements(
    				
    				(playerName == null ? messagNoPlayerNameBroadcast : playerName),
    				(tRank == null ? "" : tRank.getName()), 
    				(results.getMessage() != null ? results.getMessage() : "")
    			)
        		.broadcast();
    	}
    	else {
    		localManager.sendTo( sender );
    	}
	}
	
	protected void ranksRankupCannotAffordMsg( CommandSender sender, 
			RankupResults results ) {
		
		DecimalFormat dFmt = new DecimalFormat("#,##0.00");
    	
		PlayerRank tpRank = results.getPlayerRankTarget();
		Rank tRank = results.getTargetRank();
		
    	PrisonRanks.getInstance().getRanksMessages()
	    		.getLocalizable( "ranks_rankup__rankup_cant_afford" )
	    		.withReplacements(
				
    				dFmt.format( tpRank == null ? 0 : tpRank.getRankCost()), 
    				tRank == null || tRank.getCurrency() == null ? "" : results.getTargetRank().getCurrency()
				)
	    		.sendTo( sender );
	}

	protected void ranksRankupLowestRankMsg( CommandSender sender, String playerName,
			RankupResults results ) {
		
		String messagYouAre = PrisonRanks.getInstance().getRanksMessages()
				.getLocalizable( "ranks_rankup__rankup_you_are" ).localize();
		
		PrisonRanks.getInstance().getRanksMessages()
				.getLocalizable( "ranks_rankup__rankup_lowest" )
				.withReplacements(
						(playerName == null ? messagYouAre : playerName)
					)
				.sendTo( sender );
	}
	
	protected void ranksRankupHighestRankMsg( CommandSender sender, String playerName,
			RankupResults results ) {
		
		String messagYouAre = PrisonRanks.getInstance().getRanksMessages()
				.getLocalizable( "ranks_rankup__rankup_you_are" ).localize();
		
		PrisonRanks.getInstance().getRanksMessages()
				.getLocalizable( "ranks_rankup__rankup_highest" )
				.withReplacements(
						(playerName == null ? messagYouAre : playerName)
					)
				.sendTo( sender );
	}
	
	protected void ranksRankupFailureMsg( CommandSender sender ) {
		
		PrisonRanks.getInstance().getRanksMessages()
				.getLocalizable( "ranks_rankup__rankup_failure" )
				.sendTo( sender );
	}
	
	protected void ranksRankupFailureCouldNotLoadPlayerMsg( CommandSender sender ) {
		
		PrisonRanks.getInstance().getRanksMessages()
		    	.getLocalizable( "ranks_rankup__rankup_failed_to_load_player" )
		    	.sendTo( sender );
	}
	
	protected void ranksRankupFailureCouldNotLoadLadderMsg( CommandSender sender ) {
		
		PrisonRanks.getInstance().getRanksMessages()
				.getLocalizable( "ranks_rankup__rankup_failed_to_load_ladder" )
				.sendTo( sender );
	}
	
	protected void ranksRankupFailureUnableToAssignRankMsg( CommandSender sender ) {
		
		PrisonRanks.getInstance().getRanksMessages()
				.getLocalizable( "ranks_rankup__rankup_failed_to_assign_rank" )
				.sendTo( sender );
	}
	
	protected void ranksRankupFailureCouldNotSavePlayerFileMsg( CommandSender sender ) {
		
		PrisonRanks.getInstance().getRanksMessages()
				.getLocalizable( "ranks_rankup__rankup_failed_to_save_player_file" )
				.sendTo( sender );
	}
	
	protected void ranksRankupFailureNoRanksMsg( CommandSender sender ) {
		
		PrisonRanks.getInstance().getRanksMessages()
				.getLocalizable( "ranks_rankup__rankup_no_ranks" )
				.sendTo( sender ); 
	}
	
	protected void ranksRankupFailureRankDoesNotExistMsg( CommandSender sender, String rank ) {
		
		PrisonRanks.getInstance().getRanksMessages()
				.getLocalizable( "ranks_rankup__rankup_rank_does_not_exist" )
				.withReplacements( rank )
				.sendTo( sender ); 
	}
	
	protected void ranksRankupFailureRankIsNotInLadderMsg( CommandSender sender, 
			String rank, String ladder ) {
		
		PrisonRanks.getInstance().getRanksMessages()
				.getLocalizable( "ranks_rankup__rankup_rank_is_not_in_ladder" )
				.withReplacements( rank, ladder )
				.sendTo( sender ); 
	}
	
	protected void ranksRankupFailureCurrencyIsNotSupportedMsg( CommandSender sender, 
			String currency ) {
		
		PrisonRanks.getInstance().getRanksMessages()
				.getLocalizable( "ranks_rankup__rankup_currency_is_not_supported" )
				.withReplacements( currency )
				.sendTo( sender );
	}
	
	protected void ranksRankupFailureLadderRemovedMsg( CommandSender sender, 
			String ladder ) {
		
		PrisonRanks.getInstance().getRanksMessages()
				.getLocalizable( "ranks_rankup__rankup_ladder_removed" )
				.withReplacements( ladder )
				.sendTo( sender );
	}
	
	protected void ranksRankupFailureRemovingLadderMsg( CommandSender sender, 
			String ladder ) {
		
		PrisonRanks.getInstance().getRanksMessages()
				.getLocalizable( "ranks_rankup__rankup_failure_removing_ladder" )
				.withReplacements( ladder )
				.sendTo( sender );
	}
	
	protected void ranksRankupFailureInProgressMsg( CommandSender sender ) {
		
		PrisonRanks.getInstance().getRanksMessages()
			.getLocalizable( "ranks_rankup__rankup_in_progress_failure" )
			.sendTo( sender );
	}
	
	
}
