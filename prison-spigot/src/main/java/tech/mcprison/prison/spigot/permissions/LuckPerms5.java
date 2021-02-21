package tech.mcprison.prison.spigot.permissions;

import java.util.ArrayList;
import java.util.List;

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
	extends PermissionIntegration {

	private LuckPerms5Wrapper permsWrapper;
	
	public LuckPerms5() {
		super( "LuckPermsV5", "LuckPerms" );
	}
	
	@Override
	public void integrate() {
		if ( isRegistered()) {
			try {
				RegisteredServiceProvider<LuckPerms> provider = Bukkit.getServicesManager().getRegistration(LuckPerms.class);
				if (provider != null) {
					this.permsWrapper = new LuckPerms5Wrapper(provider);
				}
			}
			catch ( java.lang.NoClassDefFoundError | IllegalStateException e ) {
				// ignore since this just means v4.x or lower is being used and not v5
			}
			catch ( Exception e ) {
				e.printStackTrace();
			}
		}
	}
	
	@Override
	public void addPermission( Player holder, String permission ) {
		 if ( permsWrapper != null ) {
			 permsWrapper.addPermission( holder, permission );
		 }
	}
	

	@Override
	public void removePermission( Player holder, String permission ) {
		if ( permsWrapper != null ) {
			permsWrapper.removePermission( holder, permission );
		}
	}
	
    @Override 
    public void addGroupPermission(Player holder, String groupPermission) {
    	if ( permsWrapper != null ) {
    		permsWrapper.addGroupPermission( holder, groupPermission );
    	}
    }
    
    @Override 
    public void removeGroupPermission(Player holder, String groupPermission) {
    	if ( permsWrapper != null ) {
    		permsWrapper.removeGroupPermission( holder, groupPermission );
    	}
    }
    
    @Override
    public List<String> getPermissions(Player holder, boolean detailed) {
    	List<String> results = new ArrayList<>();
    	
    	if ( permsWrapper != null ) {
    		results = permsWrapper.getPermissions(holder, detailed);
    	}
    	
    	return results;
    }
    
	@Override
	public boolean hasIntegrated() {
		return (permsWrapper != null);
	}

	@Override
	public String getPluginSourceURL() {
		return "https://luckperms.net/";
	}

}
