package tech.mcprison.prison.ranks.managers;

import tech.mcprison.prison.ranks.PrisonRanks;

public class LadderManagerMessages {

	protected String cannotSaveLadderFile( String ladderName, String errorMessage ) {
		
		return PrisonRanks.getInstance().getRanksMessages()
    			.getLocalizable( "ranks_ladderManager__cannot_save_ladder_file" )
    			.withReplacements( 
    					ladderName, 
    					errorMessage )
    			.localize();
	}
	
}
