package tech.mcprison.prison.ranks.managers;

import java.util.UUID;

import tech.mcprison.prison.ranks.PrisonRanks;
import tech.mcprison.prison.tasks.PrisonRunnable;

public class NewRankPlayerSyncTask
	implements PrisonRunnable {
	
	private UUID uid;
	private String playerName;
	
	public NewRankPlayerSyncTask( UUID uid, String playerName ) {
		super();
		
		this.uid = uid;
		this.playerName = playerName;
	}
	
	@Override
	public void run() {
		
		PrisonRanks.getInstance().getPlayerManager().addPlayerSyncTask( uid, playerName );
		
	}

}
