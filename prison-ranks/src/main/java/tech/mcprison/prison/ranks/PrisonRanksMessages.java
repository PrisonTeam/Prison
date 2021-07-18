package tech.mcprison.prison.ranks;

import tech.mcprison.prison.modules.Module;

public abstract class PrisonRanksMessages
		extends Module
{

	public PrisonRanksMessages( String name, String version, int target )
	{
		super( name, version, target );
	}

	protected String prisonRanksFailureNoEconomyStatusMsg() {
		
		return PrisonRanks.getInstance().getRanksMessages()
    			.getLocalizable( "ranks_prisonRanks__failure_no_economy_status" )
    			.localize();
	}
	
	protected String prisonRanksFailureNoEconomyMsg( String integrationDebug ) {
		
		return PrisonRanks.getInstance().getRanksMessages()
				.getLocalizable( "ranks_prisonRanks__failure_no_economy" )
				.withReplacements( 
						integrationDebug )
				.localize();
	}
	
	protected String prisonRanksFailureLoadingRankStatusMsg( String errorMessage ) {
		
		return PrisonRanks.getInstance().getRanksMessages()
				.getLocalizable( "ranks_prisonRanks__failure_loading_ranks_status" )
				.withReplacements( 
						errorMessage )
				.localize();
	}

	protected String prisonRanksFailureLoadingRanksMsg( String errorMessage ) {
		
		return PrisonRanks.getInstance().getRanksMessages()
				.getLocalizable( "ranks_prisonRanks__failure_loading_ranks" )
				.withReplacements( 
						errorMessage )
				.localize();
	}
	
	protected String prisonRanksFailureLoadingLadderStatusMsg( String errorMessage ) {
		
		return PrisonRanks.getInstance().getRanksMessages()
				.getLocalizable( "ranks_prisonRanks__failure_loading_ladders_status" )
				.withReplacements( 
						errorMessage )
				.localize();
	}
	
	protected String prisonRanksFailureLoadingLadderMsg( String errorMessage ) {
		
		return PrisonRanks.getInstance().getRanksMessages()
				.getLocalizable( "ranks_prisonRanks__failure_loading_ladders" )
				.withReplacements( 
						errorMessage )
				.localize();
	}
	
	protected String prisonRanksFailureLoadingPlayersStatusMsg( String errorMessage ) {
		
		return PrisonRanks.getInstance().getRanksMessages()
				.getLocalizable( "ranks_prisonRanks__failure_loading_players_status" )
				.withReplacements( 
						errorMessage )
				.localize();
	}
	
	protected String prisonRanksFailureLoadingPlayersMsg( String errorMessage ) {
		
		return PrisonRanks.getInstance().getRanksMessages()
				.getLocalizable( "ranks_prisonRanks__failure_loading_players" )
				.withReplacements( 
						errorMessage )
				.localize();
	}
	
	protected String prisonRanksFailedLoadingPlayersMsg( String errorMessage ) {
		
		return PrisonRanks.getInstance().getRanksMessages()
				.getLocalizable( "ranks_prisonRanks__failed_loading_players" )
				.withReplacements( 
						errorMessage )
				.localize();
	}
	
	protected String prisonRanksFailedToLoadPlayFileMsg( String errorMessage ) {
		
		return PrisonRanks.getInstance().getRanksMessages()
				.getLocalizable( "ranks_prisonRanks__failed_to_load_player_file" )
				.withReplacements( 
						errorMessage )
				.localize();
	}
	
	protected String prisonRanksStatusLoadedRanksMsg( int rankCount ) {
		
		return PrisonRanks.getInstance().getRanksMessages()
				.getLocalizable( "ranks_prisonRanks__status_loaded_ranks" )
				.withReplacements( 
						Integer.toString( rankCount ) )
				.localize();
	}
	
	protected String prisonRanksStatusLoadedLaddersMsg( int ladderCount ) {
		
		return PrisonRanks.getInstance().getRanksMessages()
				.getLocalizable( "ranks_prisonRanks__status_loaded_ladders" )
				.withReplacements( 
						Integer.toString( ladderCount ) )
				.localize();
	}
	
	protected String prisonRanksStatusLoadedPlayersMsg( int playerCount ) {
		
		return PrisonRanks.getInstance().getRanksMessages()
				.getLocalizable( "ranks_prisonRanks__status_loaded_players" )
				.withReplacements( 
						Integer.toString( playerCount ) )
				.localize();
	}
	
	
	protected String prisonRanksFailureCreateDefaultLadderMsg() {
		
		String msgCreate = PrisonRanks.getInstance().getRanksMessages()
    			.getLocalizable( "ranks_prisonRanks__failure_with_ladder_create" )
    			.localize();
    	String msgDefault = PrisonRanks.getInstance().getRanksMessages()
    			.getLocalizable( "ranks_prisonRanks__failure_with_ladder_default" )
    			.localize();
    	
    	return PrisonRanks.getInstance().getRanksMessages()
    			.getLocalizable( "ranks_prisonRanks__failure_with_ladder" )
    			.withReplacements( 
    					msgCreate,
    					msgDefault )
    			.localize();
    	
	}
	
	
	protected String prisonRanksFailureSavingDefaultLadderMsg() {
		
		String msgSave = PrisonRanks.getInstance().getRanksMessages()
				.getLocalizable( "ranks_prisonRanks__failure_with_ladder_save" )
				.localize();
		String msgDefault = PrisonRanks.getInstance().getRanksMessages()
				.getLocalizable( "ranks_prisonRanks__failure_with_ladder_default" )
				.localize();
		
		return PrisonRanks.getInstance().getRanksMessages()
				.getLocalizable( "ranks_prisonRanks__failure_with_ladder" )
				.withReplacements( 
						msgSave,
						msgDefault )
				.localize();
		
	}
	
	
	
	protected String prisonRanksFailureCreatePrestigeLadderMsg() {
		
		String msgCreate = PrisonRanks.getInstance().getRanksMessages()
    			.getLocalizable( "ranks_prisonRanks__failure_with_ladder_create" )
    			.localize();
    	String msgDefault = PrisonRanks.getInstance().getRanksMessages()
    			.getLocalizable( "ranks_prisonRanks__failure_with_ladder_prestiges" )
    			.localize();
    	
    	return PrisonRanks.getInstance().getRanksMessages()
    			.getLocalizable( "ranks_prisonRanks__failure_with_ladder" )
    			.withReplacements( 
    					msgCreate,
    					msgDefault )
    			.localize();
    	
	}
	
	protected String prisonRanksFailureSavingPrestigeLadderMsg() {
		
		String msgSave = PrisonRanks.getInstance().getRanksMessages()
				.getLocalizable( "ranks_prisonRanks__failure_with_ladder_save" )
				.localize();
		String msgDefault = PrisonRanks.getInstance().getRanksMessages()
				.getLocalizable( "ranks_prisonRanks__failure_with_ladder_prestiges" )
				.localize();
		
		return PrisonRanks.getInstance().getRanksMessages()
				.getLocalizable( "ranks_prisonRanks__failure_with_ladder" )
				.withReplacements( 
						msgSave,
						msgDefault )
				.localize();
		
	}
	
	
	protected String prisonRankAddedNewPlayer( String playerName ) {
		return PrisonRanks.getInstance().getRanksMessages()
    			.getLocalizable( "ranks_prisonRanks__added_new_player" )
    			.withReplacements( 
    					playerName
    					)
    			.localize();
	}
	
	
	protected String prisonRankAddedAndFixedPlayers( int addedPlayers, int fixedPLayers ) {
		return PrisonRanks.getInstance().getRanksMessages()
				.getLocalizable( "ranks_prisonRanks__added_and_fixed_players" )
				.withReplacements( 
						Integer.toString( addedPlayers ),
						Integer.toString( fixedPLayers )
						)
				.localize();
	}
	
}
