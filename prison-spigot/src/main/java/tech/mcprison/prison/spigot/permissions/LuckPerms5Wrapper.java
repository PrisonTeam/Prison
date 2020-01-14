/**
 * 
 */
package tech.mcprison.prison.spigot.permissions;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import org.bukkit.plugin.RegisteredServiceProvider;

import net.luckperms.api.LuckPerms;
import net.luckperms.api.model.data.DataMutateResult;
import net.luckperms.api.model.user.User;
import net.luckperms.api.model.user.UserManager;
import net.luckperms.api.node.types.PermissionNode;
import tech.mcprison.prison.internal.Player;

/**
 * <p>This wrapper provides support for LuckPerms v5.x.
 * </p> 
 *
 */
public class LuckPerms5Wrapper
{
	private LuckPerms api = null;
	
	public LuckPerms5Wrapper(RegisteredServiceProvider<net.luckperms.api.LuckPerms> provider) {
		super();
		
//		LuckPerms api = provider.getProvider();
		api = provider.getProvider();
	}
	
	public void addPermission( Player holder, String permission )
	{
		 editPermission(holder.getUUID(), permission, true);
	}
	
	public void removePermission( Player holder, String permission )
	{
		 editPermission(holder.getUUID(), permission, false);
	}

	/**
	 * <p>This function does not return anything, nor is there anything waiting for its
	 * completion.  Therefore it can be ran async, which is the correct way to do this.
	 * </p>
	 * 
	 * <p>Instead of using lambda functions, with inline callbacks, methods are being
	 * used instead.  The biggest reason for this is that if things go bad, error messages 
	 * and/or stack traces are clearer and more to the point on what exactly when wrong and
	 * where. I've seen too many vague entries in the logs, and not knowing who may be trying
	 * to fix future issues, then it makes more sense to keep things understandable instead
	 * of cryptic.  Besides, the optimizers within the compliers will be able to work with
	 * either technique just as well, so performance is not an issue.
	 * </p>
	 * 
	 * @param uuid
	 * @param permission
	 * @param add
	 */
    private void editPermission(UUID uuid, String permission, boolean add) {
    	if ( api != null ) {

			UserManager um = api.getUserManager();
    	    User user = um.getUser(uuid);
    	    CompletableFuture<User> userFuture = (user == null ? 
    	    		um.loadUser(uuid) : CompletableFuture.completedFuture(user));
    	    
		    userFuture
		    	.thenAcceptAsync(lpUser -> {
		    		changePermission(lpUser, permission, add);
		    });
    	}

    }

    private void changePermission( User user, String permission, boolean add )
    {
    	boolean dirty = false;
    	
    	// build a permission node now so we don't have to pass around
    	// the raw components, just this node.
    	PermissionNode newNode = PermissionNode.builder(permission).build();
    	
    	// Try to remove the perm if it exists.  Even if adding a perm, try to remove it
    	// to prevent possible duplicates.
    	if ( user.data().remove( newNode ) == DataMutateResult.SUCCESS ) {
    		dirty = true;
    	}
    	
    	if ( add && user.data().add( newNode ) == DataMutateResult.SUCCESS ) {
    		dirty = true;
    	}
    	
		if ( dirty ) {
			api.getUserManager().saveUser( user );
		}
    }
    
}

