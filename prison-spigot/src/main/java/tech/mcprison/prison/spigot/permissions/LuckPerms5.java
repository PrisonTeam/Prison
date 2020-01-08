package tech.mcprison.prison.spigot.permissions;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;

import me.lucko.luckperms.LuckPerms;
import me.lucko.luckperms.api.DataMutateResult;
import me.lucko.luckperms.api.Node;
import me.lucko.luckperms.api.User;
import tech.mcprison.prison.integration.PermissionIntegration;
import tech.mcprison.prison.internal.Player;

public class LuckPerms5
	implements PermissionIntegration {

	public static final String PROVIDER_NAME = "LuckPermsV5";
	private LuckPerms api = null;
	
	public LuckPerms5() {
		RegisteredServiceProvider<LuckPerms> provider = 
					Bukkit.getServicesManager().getRegistration(LuckPerms.class);
		if (provider != null) {
		    this.api = provider.getProvider();
		    
		}
	}
	
	@Override
	public void addPermission( Player holder, String permission )
	{
		 editPermission(holder.getUUID(), permission, true);
	}
	

	@Override
	public void removePermission( Player holder, String permission )
	{
		 editPermission(holder.getUUID(), permission, false);
	}

    private void editPermission(UUID uuid, String permission, boolean add) {
    	if ( api != null ) {
    		// get the user
    		User user = LuckPerms.getApi().getUser(uuid);
    		if (user == null) {
    			return; // user not loaded
    		}
    		
    		// build the permission node
    		Node node = LuckPerms.getApi().getNodeFactory().newBuilder(permission).build();
    		
    		// set the permission
    		DataMutateResult result;
    		if(add) {
    			result = user.setPermission(node);
    		} else {
    			result = user.unsetPermission(node);
    		}
    		
    		// wasn't successful.
    		// they most likely already have (or didn't have if add = false) the permission
    		if (result != DataMutateResult.SUCCESS) {
    			return;
    		}
    		
    		
    		// now, before we return, we need to have the user to storage.
    		// this method will save the user, then run the callback once complete.
    		LuckPerms.getApi().getStorage().saveUser(user)
    		.thenAcceptAsync(wasSuccessful -> {
    			if (!wasSuccessful) {
    				return;
    			}
    			
    			// refresh the user's permissions, so the change is "live"
    			user.refreshCachedData();
    			
    		}, LuckPerms.getApi().getStorage().getAsyncExecutor());
    	}
    }

	
	@Override
	public String getProviderName()
	{
		return PROVIDER_NAME;
	}
	
	@Override
	public boolean hasIntegrated()
	{
		return api != null;
	}
}
