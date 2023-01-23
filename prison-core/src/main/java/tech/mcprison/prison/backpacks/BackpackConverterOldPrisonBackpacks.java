package tech.mcprison.prison.backpacks;

import java.util.List;

import tech.mcprison.prison.Prison;
import tech.mcprison.prison.internal.Player;
import tech.mcprison.prison.output.Output;

public class BackpackConverterOldPrisonBackpacks {
	
	public void convertOldBackpacks() {
		
		List<Player> offlinePlayers = Prison.get().getPlatform().getOfflinePlayers();
		
		for (Player player : offlinePlayers) {
			
			List<PlayerBackpack> oldBackpacks = Prison.get().getPlatform().getPlayerOldBackpacks( player );
			
			if ( oldBackpacks.size() > 0 ) {
				
				BackpackCachePlayerData playerBackpacks = BackpackCache.getInstance().getPlayers().get( player.getName() );
				
				Output.get().logInfo( playerBackpacks.toString() );
				
				Output.get().logInfo( "  Old backpack size:", oldBackpacks.size() );
			}
			
			
		}
		
	}

}
