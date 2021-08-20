package tech.mcprison.prison.ranks.commands;

import java.text.DecimalFormat;

import tech.mcprison.prison.commands.BaseCommands;
import tech.mcprison.prison.internal.CommandSender;
import tech.mcprison.prison.output.LogLevel;
import tech.mcprison.prison.output.Output;
import tech.mcprison.prison.placeholders.PlaceholdersUtil;
import tech.mcprison.prison.ranks.PrisonRanks;

public class RanksCommandsMessages
	extends BaseCommands
{

	public RanksCommandsMessages( String cmdGroup )
	{
		super( cmdGroup );
	}
	
	protected void rankAlreadyExistsMsg( CommandSender sender, String rankName ) {
		PrisonRanks.getInstance().getRanksMessages()
				.getLocalizable( "ranks_rankCommands__rank_already_exists" )
				.withReplacements( 
						rankName )
				.sendTo( sender );
	}
	
	protected void rankNameRequiredMsg( CommandSender sender ) {
		PrisonRanks.getInstance().getRanksMessages()
				.getLocalizable( "ranks_rankCommands__rank_name_required" )
				.sendTo( sender );
	}
	
	protected void ladderDoesNotExistMsg( CommandSender sender, String ladderName ) {
		PrisonRanks.getInstance().getRanksMessages()
				.getLocalizable( "ranks_rankCommands__ladder_does_not_exist" )
				.withReplacements( 
						ladderName )
				.sendTo( sender );
	}
	
	protected void ladderHasNoRanksMsg( CommandSender sender, String ladderName ) {
		PrisonRanks.getInstance().getRanksMessages()
		.getLocalizable( "ranks_rankCommands__ladder_has_no_ranks" )
		.withReplacements( 
				ladderName )
		.sendTo( sender );
	}
	
	protected String ladderHasNoRanksTextMsg() {
		return PrisonRanks.getInstance().getRanksMessages()
		.getLocalizable( "ranks_rankCommands__ladder_has_no_ranks_text" )
		.localize();
	}
	
	protected void rankDoesNotExistMsg( CommandSender sender, String rankName ) {
		PrisonRanks.getInstance().getRanksMessages()
				.getLocalizable( "ranks_rankCommands__rank_does_not_exist" )
				.withReplacements( 
						rankName )
				.sendTo( sender );
	}
	
	protected void rankCannotBeCreatedMsg( CommandSender sender ) {
		PrisonRanks.getInstance().getRanksMessages()
				.getLocalizable( "ranks_rankCommands__rank_cannot_be_created" )
				.sendTo( sender );
	}
	
	protected void rankCreatedSuccessfullyMsg( CommandSender sender, String rankName,
			String ladderName, String tag ) {
		PrisonRanks.getInstance().getRanksMessages()
				.getLocalizable( "ranks_rankCommands__rank_created_successfully" )
				.withReplacements( 
						rankName, ladderName, tag )
				.sendTo( sender );
	}
	
	protected void errorCouldNotSaveLadderMsg( CommandSender sender, 
					String ladderName ) {
		PrisonRanks.getInstance().getRanksMessages()
				.getLocalizable( "ranks_rankCommands__error_saving_ladder" )
				.withReplacements( 
						ladderName )
				.sendTo( sender );

		String message = PrisonRanks.getInstance().getRanksMessages()
				.getLocalizable( "ranks_rankCommands__error_writting_ladder" )
				.withReplacements( 
						ladderName )
				.localize();
		
		Output.get().logError( message );
	}
	
	
	protected void autoConfigPreexistingRankMineWarningMsg( CommandSender sender, 
			int rankCount, int mineCount ) {
		PrisonRanks.getInstance().getRanksMessages()
				.getLocalizable( "ranks_rankCommands__auto_config_preexisting_warning" )
				.withReplacements( 
						Integer.toString( rankCount ), Integer.toString( mineCount ) )
				.sendTo( sender );
	}
	
	protected void autoConfigForceWarningMsg( CommandSender sender ) {
		PrisonRanks.getInstance().getRanksMessages()
				.getLocalizable( "ranks_rankCommands__auto_config_force_warning" )
				.sendTo( sender );
	}
	
	
	
	protected void autoConfigInvalidOptionsMsg( CommandSender sender, 
			String optionHelp, String options ) {
		PrisonRanks.getInstance().getRanksMessages()
				.getLocalizable( "ranks_rankCommands__auto_config_invalid_options" )
				.withReplacements( 
						optionHelp, options )
				.sendTo( sender );
	}
	
	protected void autoConfigRankExistsSkipMsg( CommandSender sender, 
			String cRank) {
		PrisonRanks.getInstance().getRanksMessages()
				.getLocalizable( "ranks_rankCommands__auto_config_invalid_options" )
				.withReplacements( 
						cRank )
				.sendTo( sender );
	}
	
	protected void autoConfigNoRanksCreatedMsg( CommandSender sender ) {
		PrisonRanks.getInstance().getRanksMessages()
				.getLocalizable( "ranks_rankCommands__auto_config_no_ranks_created" )
				.sendTo( sender );
	}
	
	protected void autoConfigLadderRankCostMultiplierInfoMsg( CommandSender sender,
			double rankCostMultiplier ) {
		DecimalFormat dFmt = new DecimalFormat("0.0000");
		
		PrisonRanks.getInstance().getRanksMessages()
		.getLocalizable( "ranks_rankCommands__auto_config_ladder_rank_cost_multiplier_info" )
		.withReplacements(
				dFmt.format( rankCostMultiplier ) )
		.sendTo( sender );
	}
	
	protected void autoConfigLadderRankCostMultiplierCmdMsg( CommandSender sender ) {
		PrisonRanks.getInstance().getRanksMessages()
		.getLocalizable( "ranks_rankCommands__auto_config_ladder_rank_cost_multiplier_command_example" )
		.sendTo( sender );
	}
	
	protected void autoConfigRanksCreatedMsg( CommandSender sender, 
			String value) {
		PrisonRanks.getInstance().getRanksMessages()
				.getLocalizable( "ranks_rankCommands__auto_config_ranks_created" )
				.withReplacements( 
						value )
				.sendTo( sender );
	}
	
	
	protected void autoConfigNoRankCmdsCreatedMsg( CommandSender sender ) {
		PrisonRanks.getInstance().getRanksMessages()
				.getLocalizable( "ranks_rankCommands__auto_config_no_rank_cmds_created" )
				.sendTo( sender );
	}
	
	protected void autoConfigRankCmdsCreatedMsg( CommandSender sender, 
			String value) {
		PrisonRanks.getInstance().getRanksMessages()
				.getLocalizable( "ranks_rankCommands__auto_config_rank_cmds_created" )
				.withReplacements( 
						value )
				.sendTo( sender );
	}
	
	
	protected void autoConfigNoMinesCreatedMsg( CommandSender sender ) {
		PrisonRanks.getInstance().getRanksMessages()
				.getLocalizable( "ranks_rankCommands__auto_config_no_mines_created" )
				.sendTo( sender );
	}
	
	protected void autoConfigMinesCreatedMsg( CommandSender sender, 
			String value) {
		PrisonRanks.getInstance().getRanksMessages()
				.getLocalizable( "ranks_rankCommands__auto_config_mines_created" )
				.withReplacements( 
						value )
				.sendTo( sender );
	}
	
	protected void autoConfigNoLinkageMsg( CommandSender sender ) {
		PrisonRanks.getInstance().getRanksMessages()
				.getLocalizable( "ranks_rankCommands__auto_config_no_linkage" )
				.sendTo( sender );
	}
	
	protected void autoConfigLinkageCountMsg( CommandSender sender, 
			String value) {
		PrisonRanks.getInstance().getRanksMessages()
				.getLocalizable( "ranks_rankCommands__auto_config_linkage_count" )
				.withReplacements( 
						value )
				.sendTo( sender );
	}
	

	
	protected void rankCannotBeRemovedMsg( CommandSender sender ) {
		PrisonRanks.getInstance().getRanksMessages()
				.getLocalizable( "ranks_rankCommands__rank_cannot_remove" )
				.sendTo( sender );
	}
	
	protected void rankWasRemovedMsg( CommandSender sender, 
			String rankName) {
		PrisonRanks.getInstance().getRanksMessages()
				.getLocalizable( "ranks_rankCommands__rank_was_removed" )
				.withReplacements( 
						rankName )
				.sendTo( sender );
	}
	
	protected void rankDeleteErrorMsg( CommandSender sender, 
			String rankName) {
		PrisonRanks.getInstance().getRanksMessages()
				.getLocalizable( "ranks_rankCommands__rank_delete_error" )
				.withReplacements( 
						rankName )
				.sendTo( sender );
	}
	
	protected String ranksListHeaderMsg( String ladderName) {
		return PrisonRanks.getInstance().getRanksMessages()
				.getLocalizable( "ranks_rankCommands__ranks_list_header" )
				.withReplacements( 
						ladderName )
				.localize();
	}
	
	protected String ranksListLadderCostMultiplierMsg( double multiplier ) {
		
		DecimalFormat fFmt = new DecimalFormat("#,##0.0000");
		
		return PrisonRanks.getInstance().getRanksMessages()
				.getLocalizable( "ranks_rankCommands__ranks_list_ladder_cost_multplier" )
				.withReplacements(
					fFmt.format( multiplier )	)
				.localize();
	}
	
	protected String ranksListEditLadderCostMultiplierMsg() {
		
		return PrisonRanks.getInstance().getRanksMessages()
				.getLocalizable( "ranks_rankCommands__ranks_list_ladder_edit_cost_multplier" )
				.localize();
	}
	
	protected String ranksListClickToEditMsg() {
		return PrisonRanks.getInstance().getRanksMessages()
				.getLocalizable( "ranks_rankCommands__ranks_list_click_to_edit" )
				.localize();
	}
	
	protected String ranksListCommandCountMsg( int commandCount ) {
		return PrisonRanks.getInstance().getRanksMessages()
				.getLocalizable( "ranks_rankCommands__ranks_list_command_count" )
				.withReplacements( 
						Integer.toString(commandCount) )
				.localize();
	}
	
	protected String ranksListCurrencyMsg( String currency ) {
		return PrisonRanks.getInstance().getRanksMessages()
				.getLocalizable( "ranks_rankCommands__ranks_list_currency" )
				.withReplacements( 
						currency )
				.localize();
	}
	
	protected String ranksListClickToViewMsg() {
		return PrisonRanks.getInstance().getRanksMessages()
				.getLocalizable( "ranks_rankCommands__ranks_list_click_to_view" )
				.localize();
	}
	
	protected String ranksListClickToView2Msg() {
		return PrisonRanks.getInstance().getRanksMessages()
				.getLocalizable( "ranks_rankCommands__ranks_list_click_to_view2" )
				.localize();
	}
	
	protected String ranksListCreateNewRankMsg() {
		return PrisonRanks.getInstance().getRanksMessages()
				.getLocalizable( "ranks_rankCommands__ranks_list_create_new_rank" )
				.localize();
	}
	
	protected String ranksListYouMayTryMsg() {
		return PrisonRanks.getInstance().getRanksMessages()
				.getLocalizable( "ranks_rankCommands__ranks_list_you_may_try" )
				.localize();
	}
	
	
	
	protected String ranksInfoHeaderMsg( String rankName ) {
		return PrisonRanks.getInstance().getRanksMessages()
				.getLocalizable( "ranks_rankCommands__ranks_info_header" )
				.withReplacements( 
						rankName )
				.localize();
	}
	
	protected String ranksInfoNameMsg( String rankName ) {
		return PrisonRanks.getInstance().getRanksMessages()
				.getLocalizable( "ranks_rankCommands__ranks_info_name" )
				.withReplacements( 
						rankName )
				.localize();
	}
	
	protected String ranksInfoTagMsg( String tag ) {
		return PrisonRanks.getInstance().getRanksMessages()
				.getLocalizable( "ranks_rankCommands__ranks_info_tag" )
				.withReplacements( 
						tag, tag )
				.localize();
	}	
	
	protected String ranksInfoLadderMsg( String ladderName ) {
		return PrisonRanks.getInstance().getRanksMessages()
				.getLocalizable( "ranks_rankCommands__ranks_info_ladder" )
				.withReplacements( 
						ladderName )
				.localize();
	}	
	
	protected String ranksInfoNotLinkedToMinesMsg() {
		return PrisonRanks.getInstance().getRanksMessages()
				.getLocalizable( "ranks_rankCommands__ranks_info_not_linked_to_mines" )
				.localize();
	}
	
	protected String ranksInfoLinkedMinesMsg( String mines ) {
		return PrisonRanks.getInstance().getRanksMessages()
				.getLocalizable( "ranks_rankCommands__ranks_info_linked_mines" )
				.withReplacements( 
						mines )
				.localize();
	}
	
	protected String ranksInfoCostMsg( double cost ) {
		DecimalFormat dFmt = new DecimalFormat("#,##0.00");
		return PrisonRanks.getInstance().getRanksMessages()
				.getLocalizable( "ranks_rankCommands__ranks_info_cost" )
				.withReplacements( 
						PlaceholdersUtil.formattedKmbtSISize(cost, dFmt, " ") )
				.localize();
	}
	
	protected String ranksInfoCurrencyMsg( String currency ) {
		return PrisonRanks.getInstance().getRanksMessages()
				.getLocalizable( "ranks_rankCommands__ranks_info_currency" )
				.withReplacements( 
						currency )
				.localize();
	}
	
	protected String ranksInfoPlayersWithRankMsg( double playerCount ) {
		DecimalFormat dFmt = new DecimalFormat("#,##0");
		return PrisonRanks.getInstance().getRanksMessages()
				.getLocalizable( "ranks_rankCommands__ranks_info_players_with_rank" )
				.withReplacements( 
						dFmt.format( playerCount ) )
				.localize();
	}
	
	protected String ranksInfoRankIdMsg( int rankId ) {
		return PrisonRanks.getInstance().getRanksMessages()
				.getLocalizable( "ranks_rankCommands__ranks_info_rank_id" )
				.withReplacements( 
						Integer.toString( rankId ) )
				.localize();
	}

	protected String ranksInfoRankDeleteMessageMsg() {
		return PrisonRanks.getInstance().getRanksMessages()
				.getLocalizable( "ranks_rankCommands__ranks_info_rank_delete_message" )
				.localize();
	}
	
	protected String ranksInfoRankDeleteToolTipMsg() {
		return PrisonRanks.getInstance().getRanksMessages()
				.getLocalizable( "ranks_rankCommands__ranks_info_rank_delete_tool_tip" )
				.localize();
	}
	
	protected void rankSetCostSuccessfulMsg( CommandSender sender, 
			String rankName, double cost ) {
		DecimalFormat dFmt = new DecimalFormat("#,##0.00");
		PrisonRanks.getInstance().getRanksMessages()
				.getLocalizable( "ranks_rankCommands__rank_set_cost_success" )
				.withReplacements( 
						rankName,
						PlaceholdersUtil.formattedKmbtSISize(cost, dFmt, " ") )
				.sendTo( sender );
	}

	protected void rankSetCurrencyNotSpecifiedMsg( CommandSender sender, 
			String currency ) {
		PrisonRanks.getInstance().getRanksMessages()
				.getLocalizable( "ranks_rankCommands__set_currency_not_specified" )
				.withReplacements( 
						currency )
				.sendTo( sender );
	}

	protected void rankSetCurrencyNoCurrencyToClearMsg( CommandSender sender, 
			String rankName ) {
		PrisonRanks.getInstance().getRanksMessages()
				.getLocalizable( "ranks_rankCommands__set_currency_no_currency_to_clear" )
				.withReplacements( 
						rankName )
				.sendTo( sender );
	}
	
	protected void rankSetCurrencyClearedMsg( CommandSender sender, 
			String rankName ) {
		PrisonRanks.getInstance().getRanksMessages()
				.getLocalizable( "ranks_rankCommands__set_currency_cleared" )
				.withReplacements( 
						rankName )
				.sendTo( sender );
	}
	
	protected void rankSetCurrencyNoActiveSupportMsg( CommandSender sender, 
			String currency ) {
		PrisonRanks.getInstance().getRanksMessages()
				.getLocalizable( "ranks_rankCommands__set_currency_no_active_support" )
				.withReplacements( 
						currency )
				.sendTo( sender );
	}
	
	protected void rankSetCurrencySuccessfulMsg( CommandSender sender, 
			String rankName, String currency ) {
		PrisonRanks.getInstance().getRanksMessages()
				.getLocalizable( "ranks_rankCommands__set_currency_successful" )
				.withReplacements( 
						rankName, currency )
				.sendTo( sender );
	}
	
	
	protected void rankSetTagInvalidMsg( CommandSender sender ) {
		PrisonRanks.getInstance().getRanksMessages()
				.getLocalizable( "ranks_rankCommands__set_tag_invalid" )
				.sendTo( sender );
	}
	
	protected void rankSetTagNoChangeMsg( CommandSender sender ) {
		PrisonRanks.getInstance().getRanksMessages()
				.getLocalizable( "ranks_rankCommands__set_tag_no_change" )
				.sendTo( sender );
	}
	
	protected void rankSetTagClearedMsg( CommandSender sender, 
			String rankName ) {
		PrisonRanks.getInstance().getRanksMessages()
				.getLocalizable( "ranks_rankCommands__set_tag_cleared" )
				.withReplacements( 
						rankName )
				.sendTo( sender );
	}
	
	protected void rankSetTagSucessMsg( CommandSender sender, 
			String tag, String rankName ) {
		PrisonRanks.getInstance().getRanksMessages()
				.getLocalizable( "ranks_rankCommands__set_tag_success" )
				.withReplacements( 
						tag,
						rankName )
				.sendTo( sender );
	}


	
	protected void ranksPlayerOnlineMsg( CommandSender sender ) {
		PrisonRanks.getInstance().getRanksMessages()
				.getLocalizable( "ranks_rankCommands__player_must_be_online" )
				.sendTo( sender );
	}

	protected String ranksPlayerLadderInfoMsg(
					String ladderName, String rankName ) {
		return PrisonRanks.getInstance().getRanksMessages()
				.getLocalizable( "ranks_rankCommands__player_ladder_info" )
				.withReplacements( 
						ladderName, rankName )
				.localize();
	}

	protected String ranksPlayerLadderHighestRankMsg() {
		return PrisonRanks.getInstance().getRanksMessages()
				.getLocalizable( "ranks_rankCommands__player_ladder_highest_rank" )
				.localize();
	}

	protected String ranksPlayerLadderNextRankMsg( String nextRankName, 
					String nextRankCost ) {
		return PrisonRanks.getInstance().getRanksMessages()
				.getLocalizable( "ranks_rankCommands__player_ladder_next_rank" )
				.withReplacements( 
						nextRankName, nextRankCost )
				.localize();
	}

	protected String ranksPlayerLadderNextRankCurrencyMsg( String currency ) {
		return PrisonRanks.getInstance().getRanksMessages()
				.getLocalizable( "ranks_rankCommands__player_ladder_next_rank_currency" )
				.withReplacements( 
						currency )
				.localize();
	}
	
	protected String ranksPlayerBalanceDefaultMsg( String playerName, 
			String amount ) {
		return PrisonRanks.getInstance().getRanksMessages()
				.getLocalizable( "ranks_rankCommands__player_balance_default" )
				.withReplacements( 
						playerName, amount )
				.localize();
	}

	protected String ranksPlayerBalanceOthersMsg( String playerName, String currency,
			String amount ) {
		return PrisonRanks.getInstance().getRanksMessages()
				.getLocalizable( "ranks_rankCommands__player_balance_others" )
				.withReplacements( 
						playerName, currency, amount )
				.localize();
	}

	protected String ranksPlayerPermsOfflineMsg() {
		return PrisonRanks.getInstance().getRanksMessages()
				.getLocalizable( "ranks_rankCommands__player_perms_offline" )
				.localize();
	}

	protected String ranksPlayerSellallMultiplierMsg( String multiplier, 
			String notAccurate) {
		return PrisonRanks.getInstance().getRanksMessages()
				.getLocalizable( "ranks_rankCommands__player_sellall_multiplier" )
				.withReplacements( 
						multiplier, notAccurate )
				.localize();
	}

	protected String ranksPlayerNotAccurateMsg() {
		return PrisonRanks.getInstance().getRanksMessages()
				.getLocalizable( "ranks_rankCommands__player_not_accurate" )
				.localize();
	}
	
	protected String ranksPlayerAdminOnlyMsg() {
		return PrisonRanks.getInstance().getRanksMessages()
				.getLocalizable( "ranks_rankCommands__player_admin_only" )
				.localize();
	}
	
	protected String ranksPlayerPastNamesMsg() {
		return PrisonRanks.getInstance().getRanksMessages()
				.getLocalizable( "ranks_rankCommands__player_past_names" )
				.localize();
	}

	protected String ranksPlayerPermsMsg() {
		return PrisonRanks.getInstance().getRanksMessages()
				.getLocalizable( "ranks_rankCommands__player_perms" )
				.localize();
	}

	protected String ranksPlayerOpMsg() {
		return PrisonRanks.getInstance().getRanksMessages()
				.getLocalizable( "ranks_rankCommands__player_op" )
				.localize();
	}

	protected String ranksPlayerPlayerMsg() {
		return PrisonRanks.getInstance().getRanksMessages()
				.getLocalizable( "ranks_rankCommands__player_player" )
				.localize();
	}

	protected String ranksPlayerOnlineMsg() {
		return PrisonRanks.getInstance().getRanksMessages()
				.getLocalizable( "ranks_rankCommands__player_online" )
				.localize();
	}

	protected String ranksPlayerOfflineMsg() {
		return PrisonRanks.getInstance().getRanksMessages()
				.getLocalizable( "ranks_rankCommands__player_offline" )
				.localize();
	}

	protected String ranksPlayerPrisonOfflinePlayerMsg() {
		return PrisonRanks.getInstance().getRanksMessages()
				.getLocalizable( "ranks_rankCommands__player_prison_offline_player" )
				.localize();
	}

	protected String ranksPlayerPrisonPlayerMsg() {
		return PrisonRanks.getInstance().getRanksMessages()
				.getLocalizable( "ranks_rankCommands__player_prison_player" )
				.localize();
	}

	protected void ranksPlayerNoRanksFoundMsg( CommandSender sender, String playerName ) {
		PrisonRanks.getInstance().getRanksMessages()
				.getLocalizable( "ranks_rankCommands__player_no_ranks_found" )
				.withReplacements( 
						playerName )
				.sendTo( sender );
	}
	
	protected void ranksPlayersInvalidLadderMsg( CommandSender sender, String ladderName ) {
		PrisonRanks.getInstance().getRanksMessages()
				.getLocalizable( "ranks_rankCommands__players_invalid_ladder" )
				.withReplacements( 
						ladderName )
				.sendTo( sender, LogLevel.ERROR );
	}
	
	protected void ranksPlayersInvalidActionMsg( CommandSender sender, String action ) {
		PrisonRanks.getInstance().getRanksMessages()
				.getLocalizable( "ranks_rankCommands__players_invalid_action" )
				.withReplacements( 
						action )
				.sendTo( sender, LogLevel.ERROR );
	}

	
}
