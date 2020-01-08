package tech.mcprison.prison.spigot.permissions;

import java.util.UUID;
import me.lucko.luckperms.LuckPerms;
import me.lucko.luckperms.api.DataMutateResult;
import me.lucko.luckperms.api.LuckPermsApi;
import me.lucko.luckperms.api.Node;
import me.lucko.luckperms.api.User;
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
public class LuckPermissions implements PermissionIntegration {

    public static final String PROVIDER_NAME = "LuckPerms";
	private LuckPermsApi api;

    public LuckPermissions() {
    	try
		{
			api = LuckPerms.getApi();
		}
		catch (  NoClassDefFoundError | IllegalStateException  e )
		{
			// If a NoClassDefFoundError happens, then that basically means the LuckPerms
			// plugin has not been loaded.
			
			// Ignore for now... maybe log the exception if in debug mode?
		}
    }

    @Override
    public void addPermission(Player holder, String permission) {
        editPermission(holder.getUUID(), permission, true);
    }

    @Override
    public void removePermission(Player holder, String permission) {
        editPermission(holder.getUUID(), permission, false);
    }

    private void editPermission(UUID uuid, String permission, boolean add) {
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

    @Override
    public String getProviderName() {
        return PROVIDER_NAME;
    }

    @Override
    public boolean hasIntegrated() {
        return api != null;
    }

}
