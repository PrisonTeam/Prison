package tech.mcprison.prison.spigot.placeholder;

import java.util.Optional;
import java.util.function.Function;

import org.bukkit.OfflinePlayer;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import tech.mcprison.prison.PrisonAPI;
import tech.mcprison.prison.internal.Player;
import tech.mcprison.prison.ranks.PrisonRanks;
import tech.mcprison.prison.ranks.data.RankPlayer;
import tech.mcprison.prison.ranks.managers.PlayerManager;

public class PlaceHolderAPIIntegrationWrapper
	extends PlaceholderExpansion 
{
	public PlaceHolderAPIIntegrationWrapper() {
		super();
	}
	
	public void registerPlaceholder(String placeholder, Function<Player, String> action) {
		this.register();
	}

	@Override
	public String getAuthor(){
		return "PrisonTeam";
	}

	@Override
	public String getIdentifier(){
		return "Prison";
	}

	@Override
	public String getVersion(){
		return PrisonAPI.getPluginVersion();
	}

	@Override
	public String onRequest(OfflinePlayer player, String identifier) {
		String results = null;
		
		PlayerManager pm = PrisonRanks.getInstance().getPlayerManager();
		Optional<RankPlayer> oPlayer = pm.getPlayer(player.getUniqueId());
		
		if ( oPlayer.isPresent() ) {
			RankPlayer rankPlayer = oPlayer.get();
			
			if (identifier.equalsIgnoreCase("rank")) {
				results = pm.getPlayerNames( rankPlayer );
			} else if (identifier.equalsIgnoreCase("rankup_cost")) {
				results = pm.getPlayerNextCost( rankPlayer );
			} else if (identifier.equalsIgnoreCase("rankup_rank")) {
				results = pm.getPlayerNextName( rankPlayer );
			}
		}

		return results;
	}
}
