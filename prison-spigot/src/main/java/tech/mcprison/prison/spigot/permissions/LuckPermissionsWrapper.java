package tech.mcprison.prison.spigot.permissions;

import java.util.UUID;

import me.lucko.luckperms.LuckPerms;
import me.lucko.luckperms.api.DataMutateResult;
import me.lucko.luckperms.api.LuckPermsApi;
import me.lucko.luckperms.api.Node;
import me.lucko.luckperms.api.User;
import tech.mcprison.prison.internal.Player;

public class LuckPermissionsWrapper
{
	private LuckPermsApi api;
	
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
        editPermission(holder.getUUID(), permission, true);
    }
    protected void removePermission(Player holder, String permission) {
        editPermission(holder.getUUID(), permission, false);
    }

    protected void editPermission(UUID uuid, String permission, boolean add) {
        // get the user
        User user = api.getUser(uuid);
        if (user == null) {
            return; // user not loaded
        }

        // build the permission node
        Node node = api.getNodeFactory().newBuilder(permission).build();

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
        api.getStorage().saveUser(user)
            .thenAcceptAsync(wasSuccessful -> {
                if (!wasSuccessful) {
                    return;
                }

                // refresh the user's permissions, so the change is "live"
                user.refreshCachedData();

            }, api.getStorage().getAsyncExecutor());
    }
	
}
