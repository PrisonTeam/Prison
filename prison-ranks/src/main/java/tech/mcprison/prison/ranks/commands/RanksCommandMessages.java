package tech.mcprison.prison.ranks.commands;

import java.io.IOException;
import java.text.DecimalFormat;

import tech.mcprison.prison.commands.BaseCommands;
import tech.mcprison.prison.internal.CommandSender;
import tech.mcprison.prison.output.LogLevel;
import tech.mcprison.prison.output.Output;
import tech.mcprison.prison.placeholders.PlaceholdersUtil;
import tech.mcprison.prison.ranks.PrisonRanks;

public class RanksCommandMessages
	extends BaseCommands
{

	public RanksCommandMessages( String cmdGroup )
	{
		super( cmdGroup );
	}
	
	protected void rankAlreadyExists( CommandSender sender, String rankName ) {
		PrisonRanks.getInstance().getRanksMessages()
				.getLocalizable( "ranks_rankCommands__rank_already_exists" )
				.withReplacements( 
						rankName )
				.sendTo( sender );
	}
	
	protected void rankNameRequired( CommandSender sender ) {
		PrisonRanks.getInstance().getRanksMessages()
				.getLocalizable( "ranks_rankCommands__rank_name_required" )
				.sendTo( sender );
	}
	
	protected void ladderDoesNotExist( CommandSender sender, String ladderName ) {
		PrisonRanks.getInstance().getRanksMessages()
				.getLocalizable( "ranks_rankCommands__ladder_does_not_exist" )
				.withReplacements( 
						ladderName )
				.sendTo( sender );
	}
	
	protected void rankDoesNotExist( CommandSender sender, String rankName ) {
		PrisonRanks.getInstance().getRanksMessages()
				.getLocalizable( "ranks_rankCommands__rank_does_not_exist" )
				.withReplacements( 
						rankName )
				.sendTo( sender );
	}
	
	protected void rankCannotBeCreated( CommandSender sender ) {
		PrisonRanks.getInstance().getRanksMessages()
				.getLocalizable( "ranks_rankCommands__rank_cannot_be_created" )
				.sendTo( sender );
	}
	
	protected void rankCreatedSuccessfully( CommandSender sender, String rankName,
			String ladderName, String tag ) {
		PrisonRanks.getInstance().getRanksMessages()
				.getLocalizable( "ranks_rankCommands__rank_created_successfully" )
				.withReplacements( 
						rankName, ladderName, tag )
				.sendTo( sender );
	}
	
	protected void errorCouldNotSaveLadder( CommandSender sender, 
					String ladderName, IOException e ) {
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
		
		Output.get().logError( message, e );
	}
	
	
	protected void autoConfigPreexistingRankMineWarning( CommandSender sender, 
			int rankCount, int mineCount ) {
		PrisonRanks.getInstance().getRanksMessages()
				.getLocalizable( "ranks_rankCommands__auto_config_preexisting_warning" )
				.withReplacements( 
						Integer.toString( rankCount ), Integer.toString( mineCount ) )
				.sendTo( sender );
	}
	
	protected void autoConfigForceWarning( CommandSender sender ) {
		PrisonRanks.getInstance().getRanksMessages()
				.getLocalizable( "ranks_rankCommands__auto_config_force_warning" )
				.sendTo( sender );
	}
	
	
	
	protected void autoConfigInvalidOptions( CommandSender sender, 
			String optionHelp, String options ) {
		PrisonRanks.getInstance().getRanksMessages()
				.getLocalizable( "ranks_rankCommands__auto_config_invalid_options" )
				.withReplacements( 
						optionHelp, options )
				.sendTo( sender );
	}
	
	protected void autoConfigRankExistsSkip( CommandSender sender, 
			String cRank) {
		PrisonRanks.getInstance().getRanksMessages()
				.getLocalizable( "ranks_rankCommands__auto_config_invalid_options" )
				.withReplacements( 
						cRank )
				.sendTo( sender );
	}
	
	protected void autoConfigNoRanksCreated( CommandSender sender ) {
		PrisonRanks.getInstance().getRanksMessages()
				.getLocalizable( "ranks_rankCommands__auto_config_no_ranks_created" )
				.sendTo( sender );
	}
	
	protected void autoConfigRanksCreated( CommandSender sender, 
			String value) {
		PrisonRanks.getInstance().getRanksMessages()
				.getLocalizable( "ranks_rankCommands__auto_config_ranks_created" )
				.withReplacements( 
						value )
				.sendTo( sender );
	}
	
	
	protected void autoConfigNoRankCmdsCreated( CommandSender sender ) {
		PrisonRanks.getInstance().getRanksMessages()
				.getLocalizable( "ranks_rankCommands__auto_config_no_rank_cmds_created" )
				.sendTo( sender );
	}
	
	protected void autoConfigRankCmdsCreated( CommandSender sender, 
			String value) {
		PrisonRanks.getInstance().getRanksMessages()
				.getLocalizable( "ranks_rankCommands__auto_config_rank_cmds_created" )
				.withReplacements( 
						value )
				.sendTo( sender );
	}
	
	
	protected void autoConfigNoMinesCreated( CommandSender sender ) {
		PrisonRanks.getInstance().getRanksMessages()
				.getLocalizable( "ranks_rankCommands__auto_config_no_mines_created" )
				.sendTo( sender );
	}
	
	protected void autoConfigMinesCreated( CommandSender sender, 
			String value) {
		PrisonRanks.getInstance().getRanksMessages()
				.getLocalizable( "ranks_rankCommands__auto_config_mines_created" )
				.withReplacements( 
						value )
				.sendTo( sender );
	}
	
	protected void autoConfigNoLinkage( CommandSender sender ) {
		PrisonRanks.getInstance().getRanksMessages()
				.getLocalizable( "ranks_rankCommands__auto_config_no_linkage" )
				.sendTo( sender );
	}
	
	protected void autoConfigLinkageCount( CommandSender sender, 
			String value) {
		PrisonRanks.getInstance().getRanksMessages()
				.getLocalizable( "ranks_rankCommands__auto_config_linkage_count" )
				.withReplacements( 
						value )
				.sendTo( sender );
	}
	

	
	protected void rankCannotBeRemoved( CommandSender sender ) {
		PrisonRanks.getInstance().getRanksMessages()
				.getLocalizable( "ranks_rankCommands__rank_cannot_remove" )
				.sendTo( sender );
	}
	
	protected void rankWasRemoved( CommandSender sender, 
			String rankName) {
		PrisonRanks.getInstance().getRanksMessages()
				.getLocalizable( "ranks_rankCommands__rank_was_removed" )
				.withReplacements( 
						rankName )
				.sendTo( sender );
	}
	
	protected void rankDeleteError( CommandSender sender, 
			String rankName) {
		PrisonRanks.getInstance().getRanksMessages()
				.getLocalizable( "ranks_rankCommands__rank_delete_error" )
				.withReplacements( 
						rankName )
				.sendTo( sender );
	}
	
	protected String ranksListHeader( String ladderName) {
		return PrisonRanks.getInstance().getRanksMessages()
				.getLocalizable( "ranks_rankCommands__ranks_list_header" )
				.withReplacements( 
						ladderName )
				.localize();
	}
	
	protected String ranksListClickToEdit() {
		return PrisonRanks.getInstance().getRanksMessages()
				.getLocalizable( "ranks_rankCommands__ranks_list_click_to_edit" )
				.localize();
	}
	
	protected String ranksListCommandCount( int commandCount ) {
		return PrisonRanks.getInstance().getRanksMessages()
				.getLocalizable( "ranks_rankCommands__ranks_list_command_count" )
				.withReplacements( 
						Integer.toString(commandCount) )
				.localize();
	}
	
	protected String ranksListCurrency( String currency ) {
		return PrisonRanks.getInstance().getRanksMessages()
				.getLocalizable( "ranks_rankCommands__ranks_list_currency" )
				.withReplacements( 
						currency )
				.localize();
	}
	
	protected String ranksListClickToView() {
		return PrisonRanks.getInstance().getRanksMessages()
				.getLocalizable( "ranks_rankCommands__ranks_list_click_to_view" )
				.localize();
	}
	
	protected String ranksListClickToView2() {
		return PrisonRanks.getInstance().getRanksMessages()
				.getLocalizable( "ranks_rankCommands__ranks_list_click_to_view2" )
				.localize();
	}
	
	protected String ranksListCreateNewRank() {
		return PrisonRanks.getInstance().getRanksMessages()
				.getLocalizable( "ranks_rankCommands__ranks_list_create_new_rank" )
				.localize();
	}
	
	protected String ranksListYouMayTry() {
		return PrisonRanks.getInstance().getRanksMessages()
				.getLocalizable( "ranks_rankCommands__ranks_list_you_may_try" )
				.localize();
	}
	
	
	
	protected String ranksInfoHeader( String rankName ) {
		return PrisonRanks.getInstance().getRanksMessages()
				.getLocalizable( "ranks_rankCommands__ranks_info_header" )
				.withReplacements( 
						rankName )
				.localize();
	}
	
	protected String ranksInfoName( String rankName ) {
		return PrisonRanks.getInstance().getRanksMessages()
				.getLocalizable( "ranks_rankCommands__ranks_info_name" )
				.withReplacements( 
						rankName )
				.localize();
	}
	
	protected String ranksInfoTag( String tag ) {
		return PrisonRanks.getInstance().getRanksMessages()
				.getLocalizable( "ranks_rankCommands__ranks_info_tag" )
				.withReplacements( 
						tag, tag )
				.localize();
	}	
	
	protected String ranksInfoLadder( String ladderName ) {
		return PrisonRanks.getInstance().getRanksMessages()
				.getLocalizable( "ranks_rankCommands__ranks_info_ladder" )
				.withReplacements( 
						ladderName )
				.localize();
	}	
	
	protected String ranksInfoNotLinkedToMines() {
		return PrisonRanks.getInstance().getRanksMessages()
				.getLocalizable( "ranks_rankCommands__ranks_info_not_linked_to_mines" )
				.localize();
	}
	
	protected String ranksInfoLinkedMines( String mines ) {
		return PrisonRanks.getInstance().getRanksMessages()
				.getLocalizable( "ranks_rankCommands__ranks_info_linked_mines" )
				.withReplacements( 
						mines )
				.localize();
	}
	
	protected String ranksInfoCost( double cost ) {
		DecimalFormat dFmt = new DecimalFormat("#,##0.00");
		return PrisonRanks.getInstance().getRanksMessages()
				.getLocalizable( "ranks_rankCommands__ranks_info_cost" )
				.withReplacements( 
						PlaceholdersUtil.formattedKmbtSISize(cost, dFmt, " ") )
				.localize();
	}
	
	protected String ranksInfoCurrency( String currency ) {
		return PrisonRanks.getInstance().getRanksMessages()
				.getLocalizable( "ranks_rankCommands__ranks_info_currency" )
				.withReplacements( 
						currency )
				.localize();
	}
	
	protected String ranksInfoPlayersWithRank( double playerCount ) {
		DecimalFormat dFmt = new DecimalFormat("#,##0");
		return PrisonRanks.getInstance().getRanksMessages()
				.getLocalizable( "ranks_rankCommands__ranks_info_players_with_rank" )
				.withReplacements( 
						dFmt.format( playerCount ) )
				.localize();
	}
	
	protected String ranksInfoRankId( int rankId ) {
		return PrisonRanks.getInstance().getRanksMessages()
				.getLocalizable( "ranks_rankCommands__ranks_info_cost" )
				.withReplacements( 
						Integer.toString( rankId ) )
				.localize();
	}

	protected String ranksInfoRankDeleteMessage() {
		return PrisonRanks.getInstance().getRanksMessages()
				.getLocalizable( "ranks_rankCommands__ranks_info_rank_delete_message" )
				.localize();
	}
	
	protected String ranksInfoRankDeleteToolTip() {
		return PrisonRanks.getInstance().getRanksMessages()
				.getLocalizable( "ranks_rankCommands__ranks_info_rank_delete_tool_tip" )
				.localize();
	}
	
	protected void rankSetCostSuccessful( CommandSender sender, 
			String rankName, double cost ) {
		DecimalFormat dFmt = new DecimalFormat("#,##0.00");
		PrisonRanks.getInstance().getRanksMessages()
				.getLocalizable( "ranks_rankCommands__rank_set_cost_success" )
				.withReplacements( 
						rankName,
						PlaceholdersUtil.formattedKmbtSISize(cost, dFmt, " ") )
				.sendTo( sender );
	}

	protected void rankSetCurrencyNotSpecified( CommandSender sender, 
			String currency ) {
		PrisonRanks.getInstance().getRanksMessages()
				.getLocalizable( "ranks_rankCommands__set_currency_not_specified" )
				.withReplacements( 
						currency )
				.sendTo( sender );
	}

	protected void rankSetCurrencyNoCurrencyToClear( CommandSender sender, 
			String rankName ) {
		PrisonRanks.getInstance().getRanksMessages()
				.getLocalizable( "ranks_rankCommands__set_currency_no_currency_to_clear" )
				.withReplacements( 
						rankName )
				.sendTo( sender );
	}
	
	protected void rankSetCurrencyCleared( CommandSender sender, 
			String rankName ) {
		PrisonRanks.getInstance().getRanksMessages()
				.getLocalizable( "ranks_rankCommands__set_currency_cleared" )
				.withReplacements( 
						rankName )
				.sendTo( sender );
	}
	
	protected void rankSetCurrencyNoActiveSupport( CommandSender sender, 
			String currency ) {
		PrisonRanks.getInstance().getRanksMessages()
				.getLocalizable( "ranks_rankCommands__set_currency_no_active_support" )
				.withReplacements( 
						currency )
				.sendTo( sender );
	}
	
	protected void rankSetCurrencySuccessful( CommandSender sender, 
			String rankName, String currency ) {
		PrisonRanks.getInstance().getRanksMessages()
				.getLocalizable( "ranks_rankCommands__set_currency_successful" )
				.withReplacements( 
						rankName, currency )
				.sendTo( sender );
	}
	
	
	protected void rankSetTagInvalid( CommandSender sender ) {
		PrisonRanks.getInstance().getRanksMessages()
				.getLocalizable( "ranks_rankCommands__set_tag_invalid" )
				.sendTo( sender );
	}
	
	protected void rankSetTagNoChange( CommandSender sender ) {
		PrisonRanks.getInstance().getRanksMessages()
				.getLocalizable( "ranks_rankCommands__set_tag_no_change" )
				.sendTo( sender );
	}
	
	protected void rankSetTagCleared( CommandSender sender, 
			String rankName ) {
		PrisonRanks.getInstance().getRanksMessages()
				.getLocalizable( "ranks_rankCommands__set_tag_cleared" )
				.withReplacements( 
						rankName )
				.sendTo( sender );
	}
	
	protected void rankSetTagSucess( CommandSender sender, 
			String rankName ) {
		PrisonRanks.getInstance().getRanksMessages()
				.getLocalizable( "ranks_rankCommands__set_tag_success" )
				.withReplacements( 
						rankName )
				.sendTo( sender );
	}


	
	protected void ranksPlayerOnline( CommandSender sender ) {
		PrisonRanks.getInstance().getRanksMessages()
				.getLocalizable( "ranks_rankCommands__player_must_be_online" )
				.sendTo( sender );
	}

	protected String ranksPlayerLadderInfo( String playerName, 
					String ladderName, String rankName ) {
		return PrisonRanks.getInstance().getRanksMessages()
				.getLocalizable( "ranks_rankCommands__player_ladder_info" )
				.withReplacements( 
						playerName, ladderName, rankName )
				.localize();
	}

	protected String ranksPlayerLadderHighestRank() {
		return PrisonRanks.getInstance().getRanksMessages()
				.getLocalizable( "ranks_rankCommands__player_ladder_highest_rank" )
				.localize();
	}

	protected String ranksPlayerLadderNextRank( String nextRankName, 
					String nextRankCost ) {
		return PrisonRanks.getInstance().getRanksMessages()
				.getLocalizable( "ranks_rankCommands__player_ladder_next_rank" )
				.withReplacements( 
						nextRankName, nextRankCost )
				.localize();
	}

	protected String ranksPlayerLadderNextRankCurrency( String currency ) {
		return PrisonRanks.getInstance().getRanksMessages()
				.getLocalizable( "ranks_rankCommands__player_ladder_next_rank_currency" )
				.withReplacements( 
						currency )
				.localize();
	}
	
	protected String ranksPlayerBalanceDefault( String playerName, 
			String amount ) {
		return PrisonRanks.getInstance().getRanksMessages()
				.getLocalizable( "ranks_rankCommands__player_balance_default" )
				.withReplacements( 
						playerName, amount )
				.localize();
	}

	protected String ranksPlayerBalanceOthers( String playerName, String currency,
			String amount ) {
		return PrisonRanks.getInstance().getRanksMessages()
				.getLocalizable( "ranks_rankCommands__player_balance_others" )
				.withReplacements( 
						playerName, currency, amount )
				.localize();
	}

	protected String ranksPlayerPermsOffline() {
		return PrisonRanks.getInstance().getRanksMessages()
				.getLocalizable( "ranks_rankCommands__player_perms_offline" )
				.localize();
	}

	protected String ranksPlayerSellallMultiplier( String multiplier, 
			String notAccurate) {
		return PrisonRanks.getInstance().getRanksMessages()
				.getLocalizable( "ranks_rankCommands__player_sellall_multiplier" )
				.withReplacements( 
						multiplier, notAccurate )
				.localize();
	}

	protected String ranksPlayerNotAccurate() {
		return PrisonRanks.getInstance().getRanksMessages()
				.getLocalizable( "ranks_rankCommands__player_not_accurate" )
				.localize();
	}
	
	protected String ranksPlayerAdminOnly() {
		return PrisonRanks.getInstance().getRanksMessages()
				.getLocalizable( "ranks_rankCommands__player_admin_only" )
				.localize();
	}
	
	protected String ranksPlayerPastNames() {
		return PrisonRanks.getInstance().getRanksMessages()
				.getLocalizable( "ranks_rankCommands__player_past_names" )
				.localize();
	}

	protected String ranksPlayerPerms() {
		return PrisonRanks.getInstance().getRanksMessages()
				.getLocalizable( "ranks_rankCommands__player_perms" )
				.localize();
	}

	protected String ranksPlayerOp() {
		return PrisonRanks.getInstance().getRanksMessages()
				.getLocalizable( "ranks_rankCommands__player_op" )
				.localize();
	}

	protected String ranksPlayerPlayer() {
		return PrisonRanks.getInstance().getRanksMessages()
				.getLocalizable( "ranks_rankCommands__player_player" )
				.localize();
	}

	protected String ranksPlayerOnline() {
		return PrisonRanks.getInstance().getRanksMessages()
				.getLocalizable( "ranks_rankCommands__player_online" )
				.localize();
	}

	protected String ranksPlayerOffline() {
		return PrisonRanks.getInstance().getRanksMessages()
				.getLocalizable( "ranks_rankCommands__player_offline" )
				.localize();
	}

	protected String ranksPlayerPrisonOfflinePlayer() {
		return PrisonRanks.getInstance().getRanksMessages()
				.getLocalizable( "ranks_rankCommands__player_prison_offline_player" )
				.localize();
	}

	protected String ranksPlayerPrisonPlayer() {
		return PrisonRanks.getInstance().getRanksMessages()
				.getLocalizable( "ranks_rankCommands__player_prison_player" )
				.localize();
	}

	protected void ranksPlayerNoRanksFound( CommandSender sender, String playerName ) {
		PrisonRanks.getInstance().getRanksMessages()
				.getLocalizable( "ranks_rankCommands__player_no_ranks_found" )
				.withReplacements( 
						playerName )
				.sendTo( sender );
	}
	
	protected void ranksPlayersInvalidLadder( CommandSender sender, String ladderName ) {
		PrisonRanks.getInstance().getRanksMessages()
				.getLocalizable( "ranks_rankCommands__players_invalid_ladder" )
				.withReplacements( 
						ladderName )
				.sendTo( sender, LogLevel.ERROR );
	}
	
	protected void ranksPlayersInvalidAction( CommandSender sender, String action ) {
		PrisonRanks.getInstance().getRanksMessages()
				.getLocalizable( "ranks_rankCommands__players_invalid_action" )
				.withReplacements( 
						action )
				.sendTo( sender, LogLevel.ERROR );
	}

	
}
