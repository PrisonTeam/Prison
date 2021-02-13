/**
 * 
 */
package tech.mcprison.prison.spigot.permissions;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.SortedSet;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import org.bukkit.plugin.RegisteredServiceProvider;

import net.luckperms.api.LuckPerms;
import net.luckperms.api.model.data.DataMutateResult;
import net.luckperms.api.model.user.User;
import net.luckperms.api.model.user.UserManager;
import net.luckperms.api.node.Node;
import net.luckperms.api.node.NodeType;
import net.luckperms.api.node.types.PermissionNode;
import tech.mcprison.prison.internal.Player;
import tech.mcprison.prison.placeholders.PlaceholdersUtil;

/**
 * <p>This wrapper provides support for LuckPerms v5.x.  Both v5.x and legacy 
 * use the same provider name, so additional checking must be performed to see
 * which is actually registered.
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
	
	public void addPermission( Player holder, String permission ) {
		 editPermission(holder.getUUID(), permission, true);
	}
	
	public void removePermission( Player holder, String permission ) {
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

    private void changePermission( User user, String permission, boolean add ) {
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
    
    
    public List<String> getPermissions(Player holder, boolean detailed) {
    	List<String> results = new ArrayList<>();
    	
    	UUID uuid = holder.getUUID();
    	
    	if ( api != null ) {

			UserManager um = api.getUserManager();
    	    User user = um.getUser(uuid);
    	    
    	    if (user != null) {
    	    	
    	    	SortedSet<Node> permNodes = user.getDistinctNodes();
    	    	
    	    	for ( Node node : permNodes ) {
    	    		
    	    		String perm = node.getKey();
    	    		
//    	    		String perm = node.getPermission();
    	    		StringBuilder details = new StringBuilder();
    	    		
    	    		if ( detailed ) {
    	    			
    	    			if ( node.hasExpiry() ) {
    	    				double seconds = (System.currentTimeMillis() - node.getExpiry().toEpochMilli()) / 1000.0;
    	    				String expiry = PlaceholdersUtil.formattedTime( seconds );
    	    				
    	    				details.append(expiry);
    	    			}
    	    			
    	    			if ( node.hasExpired() ) {
    	    				if ( details.length() > 0 ) {
    	    					details.append( ":" );
    	    				}
    	    				details.append( "expired" );
    	    			}

//    	    			if ( node.isGroup() ) {
//    	    				if ( details.length() > 0 ) {
//    	    					details.append( ":" );
//    	    				}
//    	    				details.append( "group=" ).append( node.getGroupName() );
//    	    			}
    	    			
//    	    			if ( node.getType() == NodeType.PERMISSION ) {
//    	    				node.get
//    	    			}
//    	    			
//    	    			if ( node.isWorldSpecific() ) {
//    	    				if ( details.length() > 0 ) {
//    	    					details.append( ":" );
//    	    				}
//    	    				details.append( "world=" ).append( node.getWorld() );
//    	    			}
//    	    			
//    	    			if ( node.isMeta() ) {
//    	    				if ( details.length() > 0 ) {
//    	    					details.append( ":" );
//    	    				}
//    	    				
//    	    				Entry<String, String> meta = node.getMeta();
//    	    				
//    	    				details.append( "meta={" ).append( meta.getKey() )
//    	    				.append( "=" ).append( meta.getValue() ).append( "}" );
//    	    			}
    	    			
    	    			if ( details.length() > 0 ) {
    	    				details.insert( 0, "::" );
    	    			}
    	    			
    	    		}
    	    		details.insert( 0, perm );
    	    		
    	    		results.add( details.toString() );
    	    	}
    	    }
    	}
    	
    	
    	

        
        return results;
    }
}

