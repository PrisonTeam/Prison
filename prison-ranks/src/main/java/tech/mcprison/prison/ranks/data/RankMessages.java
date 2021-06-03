package tech.mcprison.prison.ranks.data;

import tech.mcprison.prison.ranks.PrisonRanks;

public class RankMessages {

	protected String rankFailureLoadingRanksMsg( String id, String name, String errorMessage ) {
		return PrisonRanks.getInstance().getRanksMessages()
				.getLocalizable( "ranks_rank__failure_loading_ranks" )
				.withReplacements( 
						id, name,
						errorMessage )
				.localize();           	
	}
	
}
