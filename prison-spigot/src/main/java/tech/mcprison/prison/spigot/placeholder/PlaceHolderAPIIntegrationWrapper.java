package tech.mcprison.prison.spigot.placeholder;

import java.util.UUID;

import org.bukkit.OfflinePlayer;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import tech.mcprison.prison.Prison;
import tech.mcprison.prison.PrisonAPI;
import tech.mcprison.prison.output.Output;
import tech.mcprison.prison.placeholders.PlaceholderManager;
import tech.mcprison.prison.util.ChatColor;

public class PlaceHolderAPIIntegrationWrapper
	extends PlaceholderExpansion 
{
	public PlaceHolderAPIIntegrationWrapper() {
		super();
	}
	

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
    
    /**
     * Because this is an internal class,
     * you must override this method to let PlaceholderAPI know to not unregister your expansion class when
     * PlaceholderAPI is reloaded
     *
     * @return true to persist through reloads
     */
    @Override
    public boolean persist(){
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
		return PlaceholderManager.PRISON_PLACEHOLDER_PREFIX;
	}

	@Override
	public String getVersion(){
		return PrisonAPI.getPluginVersion();
	}

	/**
	 * Return a null if no hits.
	 */
	@Override
	public String onRequest(OfflinePlayer player, String identifier) {
		String results = null;
		
		if( player != null ) {
			
			UUID playerUuid = player.getUniqueId();
			results = Prison.get().getPlatform().getPlaceholders()
					.placeholderTranslate( playerUuid, player.getName(), identifier );
			
			results = ChatColor.translateAlternateColorCodes( '&', results);
		}
		else if ( Output.get().isDebug() ) {
			String msg = String.format(
					"PlacefolderAPIIntegrationWrapper.onRequest: OfflinePlayer is null. Prison is " +
					"receving a null value for the OfflinePlayer from PAI for the following " +
					"placeholder: [\\Q%s\\E]",
					identifier
					);
			Output.get().logInfo( msg );
		}

		return results;
	}
}
