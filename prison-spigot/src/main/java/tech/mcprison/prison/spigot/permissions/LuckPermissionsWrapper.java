package tech.mcprison.prison.spigot.permissions;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.SortedSet;
import java.util.UUID;

import me.lucko.luckperms.LuckPerms;
import me.lucko.luckperms.api.DataMutateResult;
import me.lucko.luckperms.api.LuckPermsApi;
import me.lucko.luckperms.api.Node;
import me.lucko.luckperms.api.User;
import tech.mcprison.prison.internal.Player;
import tech.mcprison.prison.placeholders.PlaceholdersUtil;

public class LuckPermissionsWrapper
{
	private LuckPermsApi api;
	
	private enum LPPermissionType {
		PERM_ADD,
		PERM_REMOVE,
		PERM_GROUP_ADD,
		PERM_GROUP_REMOVE;
	}
	
	public LuckPermissionsWrapper() {
		try {
			api = LuckPerms.getApi();
		}
		catch (  NoClassDefFoundError | IllegalStateException  e ) {
			// If a NoClassDefFoundError happens, then that basically means the LuckPerms
			// plugin has not been loaded.  Basically since this is the legacy support, 
			// the v5 may be the one that was registered with the server api.
			
			// Ignore for now... maybe log the exception if in debug mode?
		}
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

    protected void editPermission(UUID uuid, String permission, LPPermissionType permissionType) {
        // get the user
        User user = api.getUser(uuid);
        if (user == null) {
            return; // user not loaded
        }

        // build the permission node
        Node node = null;
        
        // set the permission
        DataMutateResult result = null;
        
        switch ( permissionType )
		{
			case PERM_ADD:
				node = api.getNodeFactory().newBuilder(permission).build();
				result = user.setPermission(node);
				break;

			case PERM_REMOVE:
				node = api.getNodeFactory().newBuilder(permission).build();
				result = user.unsetPermission(node);
				break;

			case PERM_GROUP_ADD:
				node = api.getNodeFactory().makeGroupNode( permission ).build();
				result = user.setPermission( node );
				break;
				
			case PERM_GROUP_REMOVE:
				node = api.getNodeFactory().makeGroupNode( permission ).build();
				result = user.unsetPermission( node );
				break;
				
			default:
				break;
		}
        
//        if(add) {
//            result = user.setPermission(node);
//        } else {
//            result = user.unsetPermission(node);
//        }

        // wasn't successful.
        // they most likely already have (or didn't have if add = false) the permission
        if (result != DataMutateResult.SUCCESS) {
            return;
        }

        // now, before we return, we need to have the user to storage.
        // this method will save the user, then run the callback once complete.
        api.getStorage().saveUser(user)
            .thenAcceptAsync(wasSuccessful -> {
                if (!wasSuccessful) {
                    return;
                }

                // refresh the user's permissions, so the change is "live"
                user.refreshCachedData();

            }, api.getStorage().getAsyncExecutor());
    }
    
    
    public List<String> getPermissions(Player holder, boolean detailed) {
    	List<String> results = new ArrayList<>();
    	
    	UUID uuid = holder.getUUID();
    	
        // get the user
        User user = api.getUser(uuid);
        if (user != null) {
        	// user loaded
        	
        	SortedSet<? extends Node> permNodes = user.getPermissions();
        	
        	for ( Node node : permNodes ) {
        		String perm = node.getPermission();
        		StringBuilder details = new StringBuilder();
        		
        		if ( detailed ) {
        			
        			if ( node.isTemporary() ) {
        				long seconds = node.getSecondsTilExpiry();
        				String expiry = PlaceholdersUtil.formattedTime( seconds );
        				
        				details.append(expiry);
        			}
        			
        			if ( node.isGroupNode() ) {
        				if ( details.length() > 0 ) {
        					details.append( ":" );
        				}
        				details.append( "group=" ).append( node.getGroupName() );
        			}
        			
        			if ( node.isWorldSpecific() ) {
        				if ( details.length() > 0 ) {
        					details.append( ":" );
        				}
        				details.append( "world=" ).append( node.getWorld() );
        			}
        			
        			if ( node.isMeta() ) {
        				if ( details.length() > 0 ) {
        					details.append( ":" );
        				}
        				
        				Entry<String, String> meta = node.getMeta();
        				
        				details.append( "meta={" ).append( meta.getKey() )
        				.append( "=" ).append( meta.getValue() ).append( "}" );
        			}
        			
        			if ( details.length() > 0 ) {
        				details.insert( 0, "::" );
        			}
        			
        		}
        		details.insert( 0, perm );
        		
        		results.add( details.toString() );
        	}
        }

        
        return results;
    }
	
}
