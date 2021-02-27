/*
 *  Prison is a Minecraft plugin for the prison game mode.
 *  Copyright (C) 2017 The Prison Team
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package tech.mcprison.prison.spigot.permissions;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;

import tech.mcprison.prison.integration.PermissionIntegration;
import tech.mcprison.prison.internal.Player;
import tech.mcprison.prison.spigot.game.SpigotPlayer;

/**
 * @author Faizaan A. Datoo
 */
public class VaultPermissions 
	extends PermissionIntegration {

    private net.milkbowl.vault.permission.Permission permissions = null;

    public VaultPermissions() {
    	super( "Vault", "Vault" );
    }
	
	@Override
	public void integrate() {
		if ( isRegistered()) {
			try {
				RegisteredServiceProvider<net.milkbowl.vault.permission.Permission> permissionProvider =
						Bukkit.getServer().getServicesManager()
						.getRegistration(net.milkbowl.vault.permission.Permission.class);
				if (permissionProvider != null) {
					permissions = permissionProvider.getProvider();
				}
			}
			catch ( NoClassDefFoundError | IllegalStateException e ) {
				// ignore this exception since it means the plugin was not loaded
			}
			catch ( Exception e ) {
				e.printStackTrace();
			}
		}
	}

	@Override 
	public void addPermission(Player holder, String permission) {
        SpigotPlayer player = (SpigotPlayer) holder;
        this.permissions.playerAdd(player.getWrapper(), permission);
    }

    @Override 
    public void removePermission(Player holder, String permission) {
        SpigotPlayer player = (SpigotPlayer) holder;
        this.permissions.playerRemove(player.getWrapper(), permission);
    }
    
    @Override 
    public void addGroupPermission(Player holder, String groupPermission) {
    	SpigotPlayer player = (SpigotPlayer) holder;
    	this.permissions.playerAddGroup(player.getWrapper(), groupPermission);
    }
    
    @Override 
    public void removeGroupPermission(Player holder, String groupPermission) {
    	SpigotPlayer player = (SpigotPlayer) holder;
    	this.permissions.playerRemoveGroup(player.getWrapper(), groupPermission);
    }

    @Override
    public String getDisplayName() {
    	return (permissions == null ? "Vault permissons" : permissions.getName()) + " (Vault)";
    }
    
    @Override public boolean hasIntegrated() {
        return permissions != null;
    }

    /**
     * <p>Vault is unable to return a list of permissions for the players when they
     * are offline.  Vault can only return lists of groups the player is in.  And
     * those group names do not include the prefix "group." which may be added
     * by some permission plugins.
     * </p>
     * 
     */
	@Override
	public List<String> getPermissions( Player holder, boolean detailed ) {
		List<String> results = new ArrayList<>();
		
		boolean hasGroupSupport = permissions.hasGroupSupport();
		
		if ( holder.isOnline() ) {
			results.add( String.format( "[vault: Group support is %senabled.]", 
					(hasGroupSupport ? "" : "NOT ")) );
			
			SpigotPlayer player = (SpigotPlayer) holder;
			String[] groups = permissions.getPlayerGroups( player.getWrapper() );
			for ( String group : groups ) {
				results.add( group );
			}
		}
//		else {
//			results.add( "[vault: Player is offline. Perms cannot be accessed.]" );
//		}
		
		return results;
	}

}
