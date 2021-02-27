/**
 * 
 */
package tech.mcprison.prison.spigot.permissions;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import org.bukkit.plugin.RegisteredServiceProvider;

import net.luckperms.api.LuckPerms;
import net.luckperms.api.context.DefaultContextKeys;
import net.luckperms.api.model.data.DataMutateResult;
import net.luckperms.api.model.user.User;
import net.luckperms.api.model.user.UserManager;
import net.luckperms.api.node.Node;
import net.luckperms.api.node.NodeType;
import net.luckperms.api.node.types.InheritanceNode;
import net.luckperms.api.node.types.PermissionNode;
import net.luckperms.api.node.types.WeightNode;
import net.luckperms.api.query.QueryOptions;
import tech.mcprison.prison.internal.Player;
import tech.mcprison.prison.placeholders.PlaceholdersUtil;

/**
 * <p>This wrapper provides support for LuckPerms v5.x.  Both v5.x and legacy 
 * use the same provider name, so additional checking must be performed to see
 * which is actually registered.
 * </p> 
 * 
 * <p>Some useful links to api docs:
 * </p>
 * 
 * https://luckperms.net/wiki/Developer-API-Usage#the-basics-of-node
 *
 */
public class LuckPerms5Wrapper
{
	private LuckPerms api = null;
	
	private enum LPPermissionType {
		PERM_ADD,
		PERM_REMOVE,
		PERM_GROUP_ADD,
		PERM_GROUP_REMOVE;
	}
	
	public LuckPerms5Wrapper(RegisteredServiceProvider<net.luckperms.api.LuckPerms> provider) {
		super();
		
//		LuckPerms api = provider.getProvider();
		api = provider.getProvider();
	}
	
    protected void addPermission(Player holder, String permission) {
        editPermission(holder.getUUID(), permission, LPPermissionType.PERM_ADD );
    }
    protected void removePermission(Player holder, String permission) {
        editPermission(holder.getUUID(), permission, LPPermissionType.PERM_REMOVE );
    }
    protected void addGroupPermission(Player holder, String permission) {
    	editPermission(holder.getUUID(), permission, LPPermissionType.PERM_GROUP_ADD );
    }
    protected void removeGroupPermission(Player holder, String permission) {
    	editPermission(holder.getUUID(), permission, LPPermissionType.PERM_GROUP_REMOVE );
    }
    
//	public void addPermission( Player holder, String permission ) {
//		 editPermission(holder.getUUID(), permission, true);
//	}
//	
//	public void removePermission( Player holder, String permission ) {
//		 editPermission(holder.getUUID(), permission, false);
//	}

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
    private void editPermission(UUID uuid, String permission, LPPermissionType permissionType) {
    	if ( api != null ) {

			UserManager um = api.getUserManager();
    	    User user = um.getUser(uuid);
    	    CompletableFuture<User> userFuture = (user == null ? 
    	    		um.loadUser(uuid) : CompletableFuture.completedFuture(user));
    	    
		    userFuture
		    	.thenAcceptAsync(lpUser -> {
		    		changePermission(lpUser, permission, permissionType);
		    });
    	}

    }

    private void changePermission( User user, String permission, LPPermissionType permissionType ) {
    	boolean dirty = false;
    	
    	// build a permission node now so we don't have to pass around
    	// the raw components, just this node.
//    	PermissionNode newNode = null; // PermissionNode.builder(permission).build();
    	
    	Node newNode = null;
        boolean enable = permissionType == LPPermissionType.PERM_ADD || 
        		permissionType == LPPermissionType.PERM_GROUP_ADD;
        
        switch ( permissionType )
		{
			case PERM_ADD:
			case PERM_REMOVE:
				newNode = PermissionNode.builder(permission).build();
				break;
				
			case PERM_GROUP_ADD:
			case PERM_GROUP_REMOVE:
				newNode = InheritanceNode.builder(permission).build();
				break;
				
			default:
				break;
		}
    	
    	
    	// Try to remove the perm if it exists.  Even if adding a perm, try to remove it
    	// to prevent possible duplicates.
    	if ( user.data().remove( newNode ) == DataMutateResult.SUCCESS ) {
    		dirty = true;
    	}
    	
    	// If the node is to be added/enabled, then try to add it:
    	if ( enable && user.data().add( newNode ) == DataMutateResult.SUCCESS ) {
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
    	    	
    	    	SortedSet<Node> permNodes = user.resolveDistinctInheritedNodes( QueryOptions.nonContextual() );
//    	    	SortedSet<Node> permNodes = user.getDistinctNodes();
    	    	
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

    	    			// Group node:
    	    			if ( node.getType() == NodeType.INHERITANCE ) {
    	    				if ( details.length() > 0 ) {
    	    					details.append( ":" );
    	    				}
    	    				details.append( "group=" ).append( node.getKey() );
    	    			}
    	    			
//    	    			// Permission node:
//    	    			if ( node.getType() == NodeType.PERMISSION ) {
//    	    				if ( details.length() > 0 ) {
//    	    					details.append( ":" );
//    	    				}
//    	    				details.append( "permission=" ).append( node.getKey() );
//    	    			}
    	    			
    	    			
    	    			// Weighted node:
    	    			if ( node.getType() == NodeType.WEIGHT ) {
    	    				if ( details.length() > 0 ) {
    	    					details.append( ":" );
    	    				}
    	    				WeightNode weighted = (WeightNode) node;
    	    				
    	    				details.append( "weighted=" ).append( weighted.getWeight() );
    	    			}
    	    			
    	    			// Weighted node:
    	    			if ( node.getContexts().containsKey( DefaultContextKeys.WORLD_KEY ) ) {
    	    				if ( details.length() > 0 ) {
    	    					details.append( ":" );
    	    				}
    	    				details.append( "Worlds=" ).append( 
    	    						String.join( ",", 
    	    								node.getContexts().getValues( DefaultContextKeys.WORLD_KEY ) ));
    	    			}
    	    			
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

