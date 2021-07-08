package tech.mcprison.prison.ranks;

public class FirstJoinHandlerMessages {

	public String firstJoinWarningNoRanksOnServer() {
		return PrisonRanks.getInstance().getRanksMessages()
		    			.getLocalizable( "ranks_firstJoinHandler__no_ranks_on_server" )
		    			.localize();
	}
	
	protected String firstJoinErrorCouldNotSavePlayer() {
		return PrisonRanks.getInstance().getRanksMessages()
						.getLocalizable( "ranks_firstJoinHandler__could_not_save_player" )
						.localize();
	}
	
}
