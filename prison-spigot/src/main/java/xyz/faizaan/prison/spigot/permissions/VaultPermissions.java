/*
 * Prison is a Minecraft plugin for the prison game mode.
 * Copyright (C) 2018 The Prison Team
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package xyz.faizaan.prison.spigot.permissions;

import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;
import xyz.faizaan.prison.integration.PermissionIntegration;
import xyz.faizaan.prison.internal.Player;
import xyz.faizaan.prison.spigot.game.SpigotPlayer;

/**
 * @author Faizaan A. Datoo
 */
public class VaultPermissions implements PermissionIntegration {

    private net.milkbowl.vault.permission.Permission permissions = null;

    public VaultPermissions() {
        RegisteredServiceProvider<net.milkbowl.vault.permission.Permission> permissionProvider =
            Bukkit.getServer().getServicesManager()
                .getRegistration(net.milkbowl.vault.permission.Permission.class);
        if (permissionProvider != null) {
            permissions = permissionProvider.getProvider();
        }
    }

    @Override public void addPermission(Player holder, String permission) {
        SpigotPlayer player = (SpigotPlayer) holder;
        this.permissions.playerAdd(player.getWrapper(), permission);
    }

    @Override public void removePermission(Player holder, String permission) {
        SpigotPlayer player = (SpigotPlayer) holder;
        this.permissions.playerRemove(player.getWrapper(), permission);
    }

    @Override public String getProviderName() {
        return permissions.getName() + " (Vault)";
    }

    @Override public boolean hasIntegrated() {
        return permissions != null;
    }

}
