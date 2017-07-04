package tech.mcprison.prison.spigot.permissions;

import me.lucko.luckperms.LuckPerms;
import me.lucko.luckperms.api.LuckPermsApi;
import me.lucko.luckperms.api.Node;
import me.lucko.luckperms.api.User;
import me.lucko.luckperms.exceptions.ObjectAlreadyHasException;
import tech.mcprison.prison.integration.PermissionIntegration;
import tech.mcprison.prison.internal.Player;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

/**
 * @author Faizaan A. Datoo
 */
public class LuckPermissions implements PermissionIntegration {

    private LuckPermsApi api;

    public LuckPermissions() {
        api = LuckPerms.getApi();
    }

    @Override public void addPermission(Player holder, String permission) {
        editPermission(holder.getUUID(), permission, true);
    }

    @Override public void removePermission(Player holder, String permission) {
        editPermission(holder.getUUID(), permission, false);
    }

    private void editPermission(UUID uuid, String permission, boolean value) {
        // load the user in from storage. we can specify "null" for their username,
        // since it's unknown to us.
        api.getStorage().loadUser(uuid, "null").thenComposeAsync(success -> {
            // loading the user failed, return straight away
            if (!success) {
                return CompletableFuture.completedFuture(false);
            }

            // get the user instance, they're now loaded in memory.
            User user = api.getUser(uuid);

            // Build the permission node we want to set
            Node node = api.getNodeFactory().newBuilder(permission).setValue(value).build();

            // Set the permission, and return true if the user didn't already have it set.
            try {
                user.setPermission(node);

                // now we've set the permission, but still need to save the user data
                // back to the storage.

                // first save the user
                return api.getStorage().saveUser(user)
                    .thenCompose(b -> {
                        // then cleanup their user instance so we don't create
                        // a memory leak.
                        api.cleanupUser(user);
                        return CompletableFuture.completedFuture(b);
                    });

            } catch (ObjectAlreadyHasException e) {
                return CompletableFuture.completedFuture(false);
            }

        }, api.getStorage().getAsyncExecutor());
    }

    @Override public String getProviderName() {
        return "LuckPerms";
    }

    @Override public boolean hasIntegrated() {
        return api != null;
    }

}
