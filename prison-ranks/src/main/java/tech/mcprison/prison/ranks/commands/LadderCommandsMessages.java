package tech.mcprison.prison.ranks.commands;

import java.text.DecimalFormat;

import tech.mcprison.prison.commands.BaseCommands;
import tech.mcprison.prison.internal.CommandSender;
import tech.mcprison.prison.ranks.PrisonRanks;

public class LadderCommandsMessages
		extends BaseCommands
{

	public LadderCommandsMessages( String cmdGroup )
	{
		super( cmdGroup );
	}
	
	protected void ladderAddAlreadyExistsMsg( CommandSender sender, String ladderName ) {
		PrisonRanks.getInstance().getRanksMessages()
				.getLocalizable( "ranks_LadderCommands__ladder_already_exists" )
				.withReplacements( 
						ladderName )
				.sendTo( sender );
	}
	
	protected void ladderAddCreationErrorMsg( CommandSender sender, String ladderName ) {
		PrisonRanks.getInstance().getRanksMessages()
				.getLocalizable( "ranks_LadderCommands__ladder_creation_error" )
				.withReplacements( 
						ladderName )
				.sendTo( sender );
	}
	
	protected void ladderAddCreatedMsg( CommandSender sender, String ladderName ) {
		PrisonRanks.getInstance().getRanksMessages()
				.getLocalizable( "ranks_LadderCommands__ladder_created" )
				.withReplacements( 
						ladderName )
				.sendTo( sender );
	}
	
	protected void ladderAddCouldNotSaveMsg( CommandSender sender ) {
		PrisonRanks.getInstance().getRanksMessages()
				.getLocalizable( "ranks_LadderCommands__ladder_could_not_save" )
				.sendTo( sender );
	}
	
	protected void ladderDoesNotExistsMsg( CommandSender sender, String ladderName ) {
		PrisonRanks.getInstance().getRanksMessages()
				.getLocalizable( "ranks_LadderCommands__ladder_does_not_exist" )
				.withReplacements( 
						ladderName )
				.sendTo( sender );
	}
	
	protected void ladderDeleteCannotDeleteDefaultMsg( CommandSender sender ) {
		PrisonRanks.getInstance().getRanksMessages()
		    	.getLocalizable( "ranks_LadderCommands__ladder_cannot_delete_default" )
		    	.sendTo( sender );
	}
	
	protected void ladderDeleteCannotDeletePrestigesMsg( CommandSender sender ) {
		PrisonRanks.getInstance().getRanksMessages()
				.getLocalizable( "ranks_LadderCommands__ladder_cannot_delete_prestiges" )
				.sendTo( sender );
	}
	
	protected void ladderDeleteCannotDeleteWithRanksMsg( CommandSender sender ) {
		PrisonRanks.getInstance().getRanksMessages()
		.getLocalizable( "ranks_LadderCommands__ladder_cannot_delete_with_ranks" )
		.sendTo( sender );
	}

	protected void ladderDeletedMsg( CommandSender sender, String ladderName ) {
		PrisonRanks.getInstance().getRanksMessages()
				.getLocalizable( "ranks_LadderCommands__ladder_deleted" )
				.withReplacements( 
						ladderName )
				.sendTo( sender );
	}

	protected void ladderErrorMsg( CommandSender sender ) {
		PrisonRanks.getInstance().getRanksMessages()
				.getLocalizable( "ranks_LadderCommands__ladder_error" )
				.sendTo( sender );
	}

	protected String ladderHasRankMsg() {
		
		return PrisonRanks.getInstance().getRanksMessages()
				.getLocalizable( "ranks_LadderCommands__ladder_has_ranks" )
				.localize();
	}
	
	protected String ladderDefaultRankMsg() {
		
		return PrisonRanks.getInstance().getRanksMessages()
				.getLocalizable( "ranks_LadderCommands__ladder_default_rank" )
				.localize();
	}
	
	protected String ladderSeeRanksListMsg() {
		
		return PrisonRanks.getInstance().getRanksMessages()
				.getLocalizable( "ranks_LadderCommands__ladder_see_ranks_list" )
				.localize();
	}
	
	
	protected void ladderMoveRankNoticeMsg( CommandSender sender ) {
		
		PrisonRanks.getInstance().getRanksMessages()
				.getLocalizable( "ranks_LadderCommands__move_rank_notice" )
				.sendTo( sender );
	}
	
	protected void ladderRankDoesNotExistMsg( CommandSender sender, String rankName ) {
		PrisonRanks.getInstance().getRanksMessages()
				.getLocalizable( "ranks_LadderCommands__rank_does_not_exist" )
				.withReplacements( 
						rankName )
				.sendTo( sender );
	}
	
	protected void ladderAlreadyHasRankMsg( CommandSender sender, 
			String ladderName, String rankName ) {
		PrisonRanks.getInstance().getRanksMessages()
				.getLocalizable( "ranks_LadderCommands__ladder_already_has_rank" )
				.withReplacements( 
						ladderName, rankName )
				.sendTo( sender );
	}
	
	protected void ladderAddedRankMsg( CommandSender sender, 
			String ladderName, String rankName, int position ) {
		PrisonRanks.getInstance().getRanksMessages()
		.getLocalizable( "ranks_LadderCommands__ladder_added_rank" )
		.withReplacements( 
				rankName, 
				ladderName, 
				Integer.toString( position ) )
		.sendTo( sender );
	}
	
	
	protected void ladderErrorAddingMsg( CommandSender sender ) {
		
		PrisonRanks.getInstance().getRanksMessages()
				.getLocalizable( "ranks_LadderCommands__ladder_error_adding" )
				.sendTo( sender );
	}
	
	protected void ladderErrorRemovingingMsg( CommandSender sender ) {
		
		PrisonRanks.getInstance().getRanksMessages()
		.getLocalizable( "ranks_LadderCommands__ladder_error_removing" )
		.sendTo( sender );
	}
	
	protected void ladderErrorSavingMsg( CommandSender sender ) {
		
		PrisonRanks.getInstance().getRanksMessages()
				.getLocalizable( "ranks_LadderCommands__ladder_error_saving" )
				.sendTo( sender );
	}
	
	protected void ladderRemovedRankFromLadderMsg( CommandSender sender, 
			String rankName, String ladderName ) {
		
		PrisonRanks.getInstance().getRanksMessages()
				.getLocalizable( "ranks_LadderCommands__ladder_removed_rank_from_ladder" )
				.withReplacements( 
						rankName, ladderName )
				.sendTo( sender );
	}
	
	protected void ladderSetRankCostMultiplierSavedMsg( CommandSender sender, String ladderName, 
			double rankCostMultiplier, double oldRankCostMultiplier )
	{

		DecimalFormat fFmt = new DecimalFormat("#,##0.00000");
		
		PrisonRanks.getInstance().getRanksMessages()
		.getLocalizable( "ranks_LadderCommands__ladder_set_rank_cost_multiplier" )
		.withReplacements( 
				ladderName, 
				fFmt.format( rankCostMultiplier ), 
				fFmt.format( oldRankCostMultiplier) )
		.sendTo( sender );
	}

	protected void ladderSetRankCostMultiplierNoChangeMsg( CommandSender sender, String ladderName, 
			double rankCostMultiplier )
	{

		DecimalFormat fFmt = new DecimalFormat("#,##0.00000");
		
		PrisonRanks.getInstance().getRanksMessages()
		.getLocalizable( "ranks_LadderCommands__ladder_rank_cost_multiplier_no_change" )
		.withReplacements( 
				ladderName, 
				fFmt.format( rankCostMultiplier ) )
		.sendTo( sender );
	}
	
	protected void ladderSetRankCostMultiplierOutOfRangeMsg( CommandSender sender,
			double rankCostMultiplier )
	{
		
		DecimalFormat fFmt = new DecimalFormat("#,##0.00000");
		
		PrisonRanks.getInstance().getRanksMessages()
		.getLocalizable( "ranks_LadderCommands__ladder_rank_cost_multiplier_out_of_range" )
		.withReplacements( 
				fFmt.format( rankCostMultiplier ) )
		.sendTo( sender );
	}
	
	protected void ladderApplyRankCostMultiplierNoChangeMsg( CommandSender sender, String ladderName, 
			boolean applyRCM ) {
		
		PrisonRanks.getInstance().getRanksMessages()
		.getLocalizable( "ranks_LadderCommands__ladder_apply_rank_cost_multiplier_no_change" )
		.withReplacements( 
				ladderName, 
				applyRCM ? "apply" : "disabled" )
		.sendTo( sender );
	}
	
	protected void ladderApplyRankCostMultiplierSavedMsg( CommandSender sender, String ladderName, 
			boolean applyRCM, boolean applyRCMOld ) {
		
		PrisonRanks.getInstance().getRanksMessages()
		.getLocalizable( "ranks_LadderCommands__ladder_apply_rank_cost_multiplier_saved" )
		.withReplacements( 
				ladderName, 
				applyRCM ? "apply" : "disabled",
				applyRCMOld ? "apply" : "disabled" )
		.sendTo( sender );
	}
	
}
