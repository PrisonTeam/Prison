package tech.mcprison.prison.ranks.data;

import tech.mcprison.prison.output.Output;
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
	
	protected void rankFailureLoadingRankManagerMsg( String ladderName, int ladderId ) {
		String message = PrisonRanks.getInstance().getRanksMessages()
				.getLocalizable( "ranks_rankManager__failure_loading_rankManager" )
				.withReplacements( 
						ladderName,
						Integer.toString( ladderId ) )
				.localize();    
		Output.get().logError( message );
	}
	
	protected void rankFailureLoadingDuplicateRankMsg( String rankName, String ladderName,
				String badLadderName ) {
		String message = PrisonRanks.getInstance().getRanksMessages()
				.getLocalizable( "ranks_rankManager__failure_duplicate_rank" )
				.withReplacements( 
						rankName, ladderName, badLadderName, badLadderName )
				.localize();    
		Output.get().logError( message );
	}
	
}
