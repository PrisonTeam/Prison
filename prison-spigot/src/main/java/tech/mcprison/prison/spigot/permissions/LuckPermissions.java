package tech.mcprison.prison.spigot.permissions;

import java.util.ArrayList;
import java.util.List;

import me.lucko.luckperms.LuckPerms;
import me.lucko.luckperms.api.LuckPermsApi;
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
	extends PermissionIntegration {

    private LuckPermissionsWrapper permsWrapper;

    public LuckPermissions() {
    	super( "LuckPerms-Legacy", "LuckPerms" );
    }
	
	@Override
	public void integrate() {
		if ( isRegistered()) {
			try {
				LuckPermsApi lp = LuckPerms.getApi();
				
				if (lp != null) {
					permsWrapper = new LuckPermissionsWrapper();
				}
			}
			catch ( NoClassDefFoundError | IllegalStateException e ) {
				// ignore this exception since it means the plugin was not loaded
			}
			catch ( Exception e ) {
				e.printStackTrace();
			}
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
		return "https://www.spigotmc.org/resources/luckperms-an-advanced-permissions-plugin.28140/history";
	}
}
