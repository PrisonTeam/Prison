package tech.mcprison.prison.spigot.placeholder;

import java.util.UUID;

import org.bukkit.OfflinePlayer;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import tech.mcprison.prison.PrisonAPI;
import tech.mcprison.prison.integration.IntegrationManager;
import tech.mcprison.prison.ranks.PrisonRanks;
import tech.mcprison.prison.ranks.managers.PlayerManager;

public class PlaceHolderAPIIntegrationWrapper
	extends PlaceholderExpansion 
{
	public PlaceHolderAPIIntegrationWrapper() {
		super();
	}
	

//	public void registerPlaceholder(String placeholder, Function<Player, String> action) {
//		this.register();
//	}

    /**
     * This method should always return true unless we
     * have a dependency we need to make sure is on the server
     * for our placeholders to work!
     *
     * @return always true since we do not have any dependencies.
     */
    @Override
    public boolean canRegister(){
        return true;
    }
    
	@Override
	public String getAuthor(){
		return "PrisonTeam";
	}

	/**
	 * <p>It should be noted that this tells PlaceholderAPI that all identifiers, or 
	 * place holders, that begin with "prison" should use this API call under this
	 * onRequest(). This is very important to understand, since all prison related
	 * place holders MUST start with prison_ and cannot be something like "rankup_rank" or 
	 * "rank" since those will never work.
	 * </p>
	 * 
	 */
	@Override
	public String getIdentifier(){
		return IntegrationManager.PRISON_PLACEHOLDER_PREFIX;
	}

	@Override
	public String getVersion(){
		return PrisonAPI.getPluginVersion();
	}

	@Override
	public String onRequest(OfflinePlayer player, String identifier) {
		String results = null;
		
		UUID playerUuid = player.getUniqueId();
		
		PlayerManager pm = PrisonRanks.getInstance().getPlayerManager();
		results = pm.getTranslatePlayerPlaceHolder( playerUuid, identifier );
		
//		if (identifier.equalsIgnoreCase("rank")) {
//		results = pm.getPlayerNames( rankPlayer );
//	} else if (identifier.equalsIgnoreCase("rankup_cost")) {
//		results = pm.getPlayerNextCost( rankPlayer );
//	} else if (identifier.equalsIgnoreCase("rankup_rank")) {
//		results = pm.getPlayerNextName( rankPlayer );
//	}
		
		return results;
	}
}
