package tech.mcprison.prison.ranks;

public class RankUtilMessages {

	protected String rankUtilFailureInternalMsg( String errorMessage ) {
		return PrisonRanks.getInstance().getRanksMessages()
    			.getLocalizable( "ranks_rankutil__failure_internal" )
    			.withReplacements( 
    					errorMessage == null ? "--" : errorMessage )
    			.localize();
	}
	
	protected String rankUtilFailureSavingPlayerMsg( String errorMessage ) {
		return PrisonRanks.getInstance().getRanksMessages()
				.getLocalizable( "ranks_rankutil__failure_saving_player_data" )
				.withReplacements( 
						errorMessage )
				.localize();
	}
	
	
}
