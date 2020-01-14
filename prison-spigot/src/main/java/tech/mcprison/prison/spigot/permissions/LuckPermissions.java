package tech.mcprison.prison.spigot.permissions;

import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;

import me.lucko.luckperms.LuckPerms;
import tech.mcprison.prison.integration.PermissionIntegration;
import tech.mcprison.prison.internal.Player;

/**
 * <p>This class supports pre version 5 of LuckPerms only. But that said,
 * it can be used with v5 with their legacy jar file.  See URL below.
 * </p>
 * 
 * <p>Note that v5 uses different package names and class names, so it is 
 * not compatible directly with this
 * older version of LuckPerms.  They have an extension that will continue 
 * to work with this older api format but server owners must install it on
 * their end.  Over all it is not recommended since this plugin now does 
 * support v5, but still, some servers may need to use it for other plugins, 
 * so not sure what kind of conflict that will cause.
 * https://github.com/lucko/LuckPerms/wiki/Upgrading-from-v4-to-v5
 * </p>

 * <p>
 * It should be noted that v5.x of LuckPerm supports all versions of minecraft and
 * spigot from versions 1.8.8 to newest.  
 * </p>
 * 
 * @author Faizaan A. Datoo
 */
public class LuckPermissions 
	implements PermissionIntegration {

    public static final String PROVIDER_NAME = "LuckPerms";
    private LuckPermissionsWrapper permsWrapper;
//	private LuckPermsApi api;

    public LuckPermissions() {
    	super();
    }
	
	@Override
	public void integrate() {
		try {
			RegisteredServiceProvider<LuckPerms> provider = 
					Bukkit.getServicesManager().getRegistration(me.lucko.luckperms.LuckPerms.class);
			if (provider != null) {
				permsWrapper = new LuckPermissionsWrapper();
			}
		}
		catch ( Exception e ) {
			e.printStackTrace();
		}
	}

    @Override
    public void addPermission(Player holder, String permission) {
    	if ( permsWrapper != null ) {
    		permsWrapper.addPermission( holder, permission );
    	}
    }

    @Override
    public void removePermission(Player holder, String permission) {
    	if ( permsWrapper != null ) {
    		permsWrapper.removePermission( holder, permission );
    	}
    }

    @Override
    public String getProviderName() {
        return PROVIDER_NAME;
    }
    
    @Override
    public String getKeyName() {
    	return PROVIDER_NAME;
    }
    
    @Override
    public boolean hasIntegrated() {
        return (permsWrapper != null);
    }

}
