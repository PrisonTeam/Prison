package tech.mcprison.prison.spigot.permissions;

import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;

import net.luckperms.api.LuckPerms;
import tech.mcprison.prison.integration.PermissionIntegration;
import tech.mcprison.prison.internal.Player;

/**
 * <p>This provides support for the integration of LuckPerms v5.x.
 * </p>
 * 
 * <p>Additional information can be found on the following topics:
 * </p>
 * <ul>
 *   <li>LuckPerms PlaceHolders - 
 *   	- Note if using MVdWPlaceholderAPI - 
 *   	- https://github.com/lucko/LuckPerms/wiki/Placeholders</li>
 * </ul>
 *
 */
public class LuckPerms5
	implements PermissionIntegration {

	public static final String PROVIDER_NAME = "LuckPermsV5";
	private LuckPerms5Wrapper permsWrapper;
	
	public LuckPerms5() {
		super();
	}
	
	@Override
	public void integrate() {
		try {
			
			//net.luckperms.api.LuckPerms;
			
			RegisteredServiceProvider<LuckPerms> provider = Bukkit.getServicesManager().getRegistration(LuckPerms.class);
			if (provider != null) {
				this.permsWrapper = new LuckPerms5Wrapper(provider);
			    
			}
		}
		catch ( Exception e ) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void addPermission( Player holder, String permission )
	{
		 if ( permsWrapper != null ) {
			 permsWrapper.addPermission( holder, permission );
		 }
	}
	

	@Override
	public void removePermission( Player holder, String permission )
	{
		if ( permsWrapper != null ) {
			permsWrapper.removePermission( holder, permission );
		}
	}
	
	@Override
	public String getProviderName()
	{
		return PROVIDER_NAME;
	}
    
    @Override
    public String getKeyName() {
    	return PROVIDER_NAME;
    }
    
	@Override
	public boolean hasIntegrated()
	{
		return (permsWrapper != null);
	}
}
