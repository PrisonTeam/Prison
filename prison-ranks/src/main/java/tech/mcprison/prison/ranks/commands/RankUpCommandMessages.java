package tech.mcprison.prison.ranks.commands;

import java.text.DecimalFormat;

import tech.mcprison.prison.Prison;
import tech.mcprison.prison.commands.BaseCommands;
import tech.mcprison.prison.internal.CommandSender;
import tech.mcprison.prison.localization.LocaleManager;
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

	
	protected void rankupMaxNoPermissionMsg( CommandSender sender, String permission, 
			RankPlayer rPlayer ) {
		PrisonRanks.getInstance().getRanksMessages()
				.getLocalizable( "ranks_rankup__no_permission" )
				.withReplacements( permission.toLowerCase() )
				.sendTo( sender, rPlayer );
	}
	
	protected String rankupCannotRunFromConsoleMsg() {
		return PrisonRanks.getInstance().getRanksMessages()
    			.getLocalizable( "ranks_rankup__cannot_run_from_console" )
    			.localize();
	}
	
	protected void rankupInvalidPlayerNameMsg(CommandSender sender, String playerName) {
		PrisonRanks.getInstance().getRanksMessages()
				.getLocalizable( "ranks_rankup__invalid_player_name" )
				.withReplacements( playerName )
				.sendTo(sender);
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
	
	protected void rankupNotAtLastRankMsg( CommandSender sender, 
			RankPlayer rPlayer ) {
		PrisonRanks.getInstance().getRanksMessages()
				.getLocalizable( "ranks_rankup__not_at_last_rank" )
				.sendTo( sender, rPlayer );
	}
	
	protected void rankupAtLastRankMsg( CommandSender sender, 
			RankPlayer rPlayer  ) {
		PrisonRanks.getInstance().getRanksMessages()
		.getLocalizable( "ranks_rankup__at_last_rank" )
		.sendTo( sender, rPlayer );
	}
	
	protected void rankupNotAbleToPrestigeMsg( CommandSender sender, 
			RankPlayer rPlayer ) {
		PrisonRanks.getInstance().getRanksMessages()
				.getLocalizable( "ranks_rankup__not_able_to_prestige" )
				.sendTo( sender, rPlayer );
	}
	
	protected void rankupNotAbleToResetRankMsg( CommandSender sender, 
			RankPlayer rPlayer ) {
		PrisonRanks.getInstance().getRanksMessages()
				.getLocalizable( "ranks_rankup__not_able_to_reset_rank" )
				.sendTo( sender, rPlayer );
	}
	
	
	
	protected void prestigePlayerBalanceSetToZeroMsg( CommandSender sender, 
			RankPlayer rPlayer ) {
		PrisonRanks.getInstance().getRanksMessages()
				.getLocalizable( "ranks_rankup__balance_set_to_zero" )
				.sendTo( sender, rPlayer );
	}
	
	protected void prestigePlayerSucessfulMsg( CommandSender sender, String tag, 
			RankPlayer rPlayer ) {
		PrisonRanks.getInstance().getRanksMessages()
				.getLocalizable( "ranks_rankup__prestige_successful" )
				.withReplacements( tag )
				.sendTo( sender, rPlayer );
	}
	
	protected void prestigePlayerSucessfulBroadcastMsg( CommandSender sender, String tag, 
			RankPlayer rPlayer ) {
		PrisonRanks.getInstance().getRanksMessages()
		.getLocalizable( "ranks_rankup__prestige_successful_broadcast" )
		.withReplacements( tag )
		.sendTo( sender, rPlayer );
	}
	
	protected void prestigePlayerFailureMsg( CommandSender sender, String tag, 
			RankPlayer rPlayer ) {
		PrisonRanks.getInstance().getRanksMessages()
				.getLocalizable( "ranks_rankup__prestige_failure" )
				.withReplacements( tag )
				.sendTo( sender, rPlayer );
	}

	protected String prestigeConfirmationGUIMsg( CommandSender sender, 
					RankPlayer rPlayer, PlayerRank targetRank,
					boolean isResetDefaultLadder, boolean isConfirmationEnabled ) {
		StringBuilder sb = new StringBuilder();
		DecimalFormat dFmt = Prison.get().getDecimalFormat("#,##0.00");
		
		String currency = targetRank.getCurrency();
		double balance = rPlayer.getBalance( currency );
				
		LocaleManager rMsg = PrisonRanks.getInstance().getRanksMessages();
		
		String tag = targetRank.getRank().getTag();
		if ( tag == null ) {
			tag = targetRank.getRank().getName();
		}
		sb.append( 
				rPlayer.convertStringPlaceholders( 
					rMsg.getLocalizable( "ranks_rankup__confirm_prestige_line_1" )
					.withReplacements( tag )
						.localize().replace(" ", "_") )).append( " " );
		
		sb.append( 
				rPlayer.convertStringPlaceholders( 
						rMsg.getLocalizable( "ranks_rankup__confirm_prestige_line_2" )
					.withReplacements( 
							dFmt.format( targetRank.getRankCost()) )
					.localize().replace(" ", "_") )).append( " " );
		
		sb.append( 
				rPlayer.convertStringPlaceholders( 
						rMsg.getLocalizable( "ranks_rankup__confirm_prestige_line_3" )
					.withReplacements( 
							dFmt.format( balance),
							currency == null || currency.trim().length() == 0 ? 
									"" : " " + currency )
					.localize().replace(" ", "_") )).append( " " );

		if ( isResetDefaultLadder ) {
			sb.append( 
					rPlayer.convertStringPlaceholders( 
							rMsg.getLocalizable( "ranks_rankup__confirm_prestige_line_4" )
							.localize().replace(" ", "_") )).append( " " );
		}

		if ( isConfirmationEnabled ) {
			sb.append( 
					rPlayer.convertStringPlaceholders( 
							rMsg.getLocalizable( "ranks_rankup__confirm_prestige_line_5" )
							.localize().replace(" ", "_") )).append( " " );
		}

		sb.append( 
				rPlayer.convertStringPlaceholders( 
						rMsg.getLocalizable( "ranks_rankup__confirm_prestige_line_7" )
						.localize().replace(" ", "_") ));
		
		return sb.toString();
	}
	
	protected void prestigeConfirmationMsg( CommandSender sender, 
					RankPlayer rPlayer, PlayerRank targetRank,
						boolean isResetDefaultLadder, boolean isResetMoney,
						boolean isPlayer ) {
		DecimalFormat dFmt = Prison.get().getDecimalFormat("#,##0.00");
		
		String currency = targetRank.getCurrency();
		double balance = rPlayer.getBalance( currency );
				
		LocaleManager rMsg = PrisonRanks.getInstance().getRanksMessages();
		

		String tag = targetRank.getRank().getTag();
		if ( tag == null ) {
			tag = targetRank.getRank().getName();
		}
		rMsg.getLocalizable( "ranks_rankup__confirm_prestige_line_1" )
			.withReplacements( tag )
			.sendTo( sender, rPlayer );
		
		
		rMsg.getLocalizable( "ranks_rankup__confirm_prestige_line_2" )
			.withReplacements( 
				dFmt.format( targetRank.getRankCost()) )
			.sendTo( sender, rPlayer );
		
		rMsg.getLocalizable( "ranks_rankup__confirm_prestige_line_3" )
		.withReplacements( 
				dFmt.format( balance),
				currency == null || currency.trim().length() == 0 ? 
						"" : " " + currency )
			.sendTo( sender, rPlayer );

		if ( isResetDefaultLadder ) {
			rMsg.getLocalizable( "ranks_rankup__confirm_prestige_line_4" )
				.sendTo( sender, rPlayer );
		}

		if ( isResetMoney ) {
			rMsg.getLocalizable( "ranks_rankup__confirm_prestige_line_4" )
				.sendTo( sender, rPlayer );
		}
		
		rMsg.getLocalizable( "ranks_rankup__confirm_prestige_line_5" )
			.sendTo( sender, rPlayer );
		
		String playerName = !isPlayer ? rPlayer.getName() + " " : "";
		rMsg.getLocalizable( "ranks_rankup__confirm_prestige_line_6" )
			.withReplacements( playerName )
			.sendTo( sender, rPlayer );

		
//		ranks_rankup__confirm_prestige_line_1=&3Confirm Prestige: %s
//				ranks_rankup__confirm_prestige_line_2=&3  Cost: &7%1
//				ranks_rankup__confirm_prestige_line_3=&3  Default Rank will be reset.
//				ranks_rankup__confirm_prestige_line_4=&3  Balance will be reset.
//				ranks_rankup__confirm_prestige_line_5=&3Confirm with command: '&7/prestiges confirm&3'
//				ranks_rankup__confirm_prestige_line_6=&3Confirm by clicking on the green block

	}
	
	protected void ranksRankupPlayerBalanceMsg( CommandSender sender, 
			double balance, String currency, 
			RankPlayer rPlayer ) {
		DecimalFormat dFmt = Prison.get().getDecimalFormat("#,##0.00");

		LocaleManager rMsg = PrisonRanks.getInstance().getRanksMessages();
		
		rMsg.getLocalizable( "ranks_rankup__confirm_prestige_line_3" )
		.withReplacements( 
				dFmt.format( balance),
				currency == null || currency.trim().length() == 0 ? 
						"" : " " + currency )
			.sendTo( sender, rPlayer );
	}
	
	protected void ranksPromotePlayerMustBeOnlineMsg( CommandSender sender ) {
		PrisonRanks.getInstance().getRanksMessages()
				.getLocalizable( "ranks_rankup__rankup_failure_must_be_online_player" )
				.sendTo( sender );
	}
	
	protected void ranksPromotePlayerInvalidChargeValueMsg( CommandSender sender, 
			RankPlayer rPlayer ) {
		PrisonRanks.getInstance().getRanksMessages()
				.getLocalizable( "ranks_rankup__invalid_charge_value" )
				.withReplacements(
						PromoteForceCharge.no_charge.name(), 
						PromoteForceCharge.charge_player.name()
						)
				.sendTo( sender, rPlayer );
	}
	
	protected void ranksDemotePlayerInvalidRefundValueMsg( CommandSender sender, 
			RankPlayer rPlayer ) {
		PrisonRanks.getInstance().getRanksMessages()
				.getLocalizable( "ranks_rankup__invalid_refund_value" )
				.withReplacements(
						PromoteForceCharge.no_charge.name(), 
						PromoteForceCharge.refund_player.name()
						)
				.sendTo( sender, rPlayer );
	}
	
	protected void ranksConfirmLadderMsg( CommandSender sender, String ladderName, 
			RankPlayer rPlayer ) {
		PrisonRanks.getInstance().getRanksMessages()
				.getLocalizable( "ranks_rankup__rankup_failure_invalid_ladder" )
				.withReplacements( ladderName )
				.sendTo( sender, rPlayer );
	}
	
	
	protected void ranksRankupFailureToGetRankPlayerMsg( CommandSender sender ) {
       	PrisonRanks.getInstance().getRanksMessages()
	    		.getLocalizable( "ranks_rankup__rankup_failure_to_get_rankplayer" )
	    		.sendTo( sender );
	}
	
	
	
	protected void ranksRankupSuccessMsg( CommandSender sender, String playerName, 
			RankupResults results, 
			StringBuilder sbRanks
			 ) {
		
		RankPlayer rPlayer = results.getRankPlayer();
		
//		PlayerRank tpRank = results.getPlayerRankTarget();
		Rank tRank = results.getTargetRank();
		
		
		// If sbRanks is not null, then just log the rank tag and exit.  
		// Do not generate any other messages.
		if ( sbRanks != null && tRank != null ) {
			
			sbRanks.append( tRank.getTag() ).append( " " );
			return;
		}
		
       	String messageId = results.getStatus() == RankupStatus.DEMOTE_SUCCESS ? 
       					"ranks_rankup__demote_success" :
       					"ranks_rankup__rankup_success" ;

    	String messagNoPlayerName = PrisonRanks.getInstance().getRanksMessages()
    			.getLocalizable( "ranks_rankup__rankup_no_player_name" ).localize();
    	
    	Localizable localManager = PrisonRanks.getInstance().getRanksMessages()
    			.getLocalizable( messageId )
    			.withReplacements(
    					
    					(playerName == null ? messagNoPlayerName : playerName),
    					(tRank == null ? "" : tRank.getTag() ), 
    					(results.getMessage() != null ? results.getMessage() : "")
    				);
    	
    	Localizable localManagerLog = PrisonRanks.getInstance().getRanksMessages()
    			.getLocalizable( "ranks_rankup__log_rank_change" )
    			.withReplacements(
    					
    					sender.getName(), localManager.localize()
    				);           	
    	Output.get().logInfo( 
    			rPlayer.convertStringPlaceholders( 
    					localManagerLog.localize() ));

    	
    	if ( Prison.get().getPlatform().getConfigBooleanFalse( "broadcast-rankups" ) ) {
    		String messagNoPlayerNameBroadcast = PrisonRanks.getInstance().getRanksMessages()
    				.getLocalizable( "ranks_rankup__rankup_no_player_name_broadcast" ).localize();
    		
    		PrisonRanks.getInstance().getRanksMessages()
        		.getLocalizable( messageId )
        		.withReplacements(
    				
    				(playerName == null ? messagNoPlayerNameBroadcast : playerName),
    				(tRank == null ? "" : tRank.getTag() ), 
    				(results.getMessage() != null ? results.getMessage() : "")
    			)
        		.broadcast( rPlayer );
    	}
    	else {
    		localManager.sendTo( sender, rPlayer );
    	}
	}

	protected void ranksRankupMaxSuccessMsg( CommandSender sender, StringBuilder ranks, 
			RankPlayer rPlayer ) {
		
    	Localizable localManagerLog = PrisonRanks.getInstance().getRanksMessages()
    			.getLocalizable( "ranks_rankup__log_rank_change" )
    			.withReplacements(
    					
    					sender.getName(), ranks.toString()
    				);
    	
    	// Print to console for record:
    	Output.get().logInfo( localManagerLog.localize() );
    	
    	
    	if ( Prison.get().getPlatform().getConfigBooleanFalse( "broadcast-rankups" ) ) {
    		
    		localManagerLog.broadcast( rPlayer );
    		
    	}
    	else {
    		
    		localManagerLog.sendTo( sender, rPlayer );
    	}
	}
	
	protected void ranksRankupCannotAffordMsg( CommandSender sender, 
			RankupResults results ) {
		
		PlayerRank tpRank = results.getPlayerRankTarget();
		
		ranksRankupCannotAffordMsg( sender, tpRank, results.getRankPlayer() );
	}
	
	protected void ranksRankupCannotAffordMsg( CommandSender sender, 
			PlayerRank tpRank, 
			RankPlayer rPlayer ) {
			
		DecimalFormat dFmt = Prison.get().getDecimalFormat("#,##0.00");
    	
		Rank tRank = tpRank.getRank();
		
    	PrisonRanks.getInstance().getRanksMessages()
	    		.getLocalizable( "ranks_rankup__rankup_cant_afford" )
	    		.withReplacements(
				
    				dFmt.format( tpRank == null ? 0 : tpRank.getRankCost()), 
    				tRank == null || tRank.getCurrency() == null ? "" : 
    					" " +tRank.getCurrency()
				)
	    		.sendTo( sender, rPlayer );
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
				.sendTo( sender, results.getRankPlayer() );
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
				.sendTo( sender, results.getRankPlayer() );
	}
	
	protected void ranksRankupFailureMsg( CommandSender sender, 
			RankPlayer rPlayer ) {
		
		PrisonRanks.getInstance().getRanksMessages()
				.getLocalizable( "ranks_rankup__rankup_failure" )
				.sendTo( sender, rPlayer );
	}
	
	protected void ranksRankupFailureCouldNotLoadPlayerMsg( CommandSender sender, 
			RankPlayer rPlayer ) {
		
		PrisonRanks.getInstance().getRanksMessages()
		    	.getLocalizable( "ranks_rankup__rankup_failed_to_load_player" )
		    	.sendTo( sender, rPlayer );
	}
	
	protected void ranksRankupFailureCouldNotLoadLadderMsg( CommandSender sender, 
			RankPlayer rPlayer ) {
		
		PrisonRanks.getInstance().getRanksMessages()
				.getLocalizable( "ranks_rankup__rankup_failed_to_load_ladder" )
				.sendTo( sender, rPlayer );
	}
	
	protected void ranksRankupFailureUnableToAssignRankMsg( CommandSender sender, 
			RankPlayer rPlayer ) {
		
		PrisonRanks.getInstance().getRanksMessages()
				.getLocalizable( "ranks_rankup__rankup_failed_to_assign_rank" )
				.sendTo( sender, rPlayer );
	}
	
	protected void ranksRankupFailureUnableToAssignRankWithRefundMsg( CommandSender sender, 
			RankPlayer rPlayer ) {
		
		PrisonRanks.getInstance().getRanksMessages()
		.getLocalizable( "ranks_rankup__rankup_failed_to_assign_rank_with_refund" )
		.sendTo( sender, rPlayer );
	}
	
	protected void ranksRankupFailureCouldNotSavePlayerFileMsg( CommandSender sender, 
			RankPlayer rPlayer ) {
		
		PrisonRanks.getInstance().getRanksMessages()
				.getLocalizable( "ranks_rankup__rankup_failed_to_save_player_file" )
				.sendTo( sender, rPlayer );
	}
	
	protected void ranksRankupFailureNoRanksMsg( CommandSender sender, 
			RankPlayer rPlayer ) {
		
		PrisonRanks.getInstance().getRanksMessages()
				.getLocalizable( "ranks_rankup__rankup_no_ranks" )
				.sendTo( sender, rPlayer ); 
	}
	
	protected void ranksRankupFailureRankDoesNotExistMsg( CommandSender sender, String rank, 
			RankPlayer rPlayer ) {
		
		PrisonRanks.getInstance().getRanksMessages()
				.getLocalizable( "ranks_rankup__rankup_rank_does_not_exist" )
				.withReplacements( rank )
				.sendTo( sender, rPlayer ); 
	}
	
	protected void ranksRankupFailureRankIsNotInLadderMsg( CommandSender sender, 
			String rank, String ladder, 
			RankPlayer rPlayer ) {
		
		PrisonRanks.getInstance().getRanksMessages()
				.getLocalizable( "ranks_rankup__rankup_rank_is_not_in_ladder" )
				.withReplacements( rank, ladder )
				.sendTo( sender, rPlayer ); 
	}
	
	protected void ranksRankupFailureCurrencyIsNotSupportedMsg( CommandSender sender, 
			String currency, 
			RankPlayer rPlayer ) {
		
		PrisonRanks.getInstance().getRanksMessages()
				.getLocalizable( "ranks_rankup__rankup_currency_is_not_supported" )
				.withReplacements( currency )
				.sendTo( sender, rPlayer );
	}
	
	protected void ranksRankupFailureLadderRemovedMsg( CommandSender sender, 
			String ladder, 
			RankPlayer rPlayer ) {
		
		PrisonRanks.getInstance().getRanksMessages()
				.getLocalizable( "ranks_rankup__rankup_ladder_removed" )
				.withReplacements( ladder )
				.sendTo( sender, rPlayer );
	}
	
	protected void ranksRankupFailureRemovingLadderMsg( CommandSender sender, 
			String ladder, 
			RankPlayer rPlayer ) {
		
		PrisonRanks.getInstance().getRanksMessages()
				.getLocalizable( "ranks_rankup__rankup_failure_removing_ladder" )
				.withReplacements( ladder )
				.sendTo( sender, rPlayer );
	}
	
	protected void ranksRankupFailureInProgressMsg( CommandSender sender, 
			RankPlayer rPlayer ) {
		
		PrisonRanks.getInstance().getRanksMessages()
			.getLocalizable( "ranks_rankup__rankup_in_progress_failure" )
			.sendTo( sender, rPlayer );
	}
	
	
}
