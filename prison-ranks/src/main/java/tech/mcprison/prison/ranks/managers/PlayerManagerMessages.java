package tech.mcprison.prison.ranks.managers;

import tech.mcprison.prison.commands.BaseCommands;
import tech.mcprison.prison.ranks.PrisonRanks;

public class PlayerManagerMessages
				extends BaseCommands {
	
	public PlayerManagerMessages( String cmdGroup ) {
		super( cmdGroup );
	}

	protected String cannotSavePlayerFile( String playerFileName ) {
		
		return PrisonRanks.getInstance().getRanksMessages()
    			.getLocalizable( "ranks_playerManager__cannot_save_player_file" )
    			.withReplacements( 
    					playerFileName )
    			.localize();
	}
	
	protected String cannotAddNewPlayer( String playerName, String errorMessage ) {
		
		return PrisonRanks.getInstance().getRanksMessages()
    			.getLocalizable( "ranks_playerManager__cannot_add_new_player" )
    			.withReplacements( 
    					playerName, 
    					errorMessage )
				.localize();
	}
	
	
	protected String cannotSaveNewPlayerFile( String playerName, String newPlayerFilename ) {
		
		if ( playerName == null ) {
			playerName = PrisonRanks.getInstance().getRanksMessages()
					.getLocalizable( "ranks_playerManager__no_player_name_available" )
					.localize();
		}
		
		String cannotSaveNewPlayerFile = PrisonRanks.getInstance().getRanksMessages()
    			.getLocalizable( "ranks_playerManager__cannot_save_new_player_file" )
    			.withReplacements( 
    					playerName,
    					newPlayerFilename )
    			.localize();
		
		return cannotSaveNewPlayerFile;
	}
	
	
	protected String cannotLoadPlayerFile( String playerUUID ) {
		
		return PrisonRanks.getInstance().getRanksMessages()
    			.getLocalizable( "ranks_playerManager__cannot_load_player_file" )
    			.withReplacements( 
    					playerUUID )
				.localize();
	}
	
	protected String cannotLoadEconomyCurrency( String playerName, String rankCurrency ) {
		
		return PrisonRanks.getInstance().getRanksMessages()
    			.getLocalizable( "ranks_playerManager__failed_to_load_economy_currency" )
    			.withReplacements( 
    					playerName, 
    					rankCurrency )
				.localize();
	}
	
	
	protected String cannotLoadEconomy( String playerName ) {
		
		return PrisonRanks.getInstance().getRanksMessages()
				.getLocalizable( "ranks_playerManager__failed_to_load_economy" )
				.withReplacements( 
						playerName )
				.localize();
	}
	
	
	protected String lastRankMessageForDefaultLadder() {
		
		return PrisonRanks.getInstance().getRanksMessages()
				.getLocalizable( "ranks_playerManager__last_rank_message_for__" +
						"prison_rankup_rank_tag_default" )
				.localize();
	}
	


	
}
