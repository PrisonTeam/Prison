package xyz.faizaan.prison.spigot.permissions;

import me.lucko.luckperms.LuckPerms;
import me.lucko.luckperms.api.DataMutateResult;
import me.lucko.luckperms.api.LuckPermsApi;
import me.lucko.luckperms.api.Node;
import me.lucko.luckperms.api.User;
import xyz.faizaan.prison.integration.PermissionIntegration;
import xyz.faizaan.prison.internal.Player;

import java.util.UUID;

/**
 * @author Faizaan A. Datoo
 */
public class LuckPermissions implements PermissionIntegration {

    private LuckPermsApi api;

    public LuckPermissions() {
        api = LuckPerms.getApi();
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
        return "LuckPerms";
    }

    @Override
    public boolean hasIntegrated() {
        return api != null;
    }

}
