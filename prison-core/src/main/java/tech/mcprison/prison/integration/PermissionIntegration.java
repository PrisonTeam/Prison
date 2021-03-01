package tech.mcprison.prison.integration;

import java.util.List;

import tech.mcprison.prison.internal.Player;

/**
 * An {@link Integration} for a permissions plugin.
 *
 * @author Faizaan A. Datoo
 * @since API 1.0
 */
public abstract class PermissionIntegration 
	extends IntegrationCore {

	public PermissionIntegration( String keyName, String providerName ) {
		super( keyName, providerName, IntegrationType.PERMISSION );
		
	}
	
    /**
     * Adds a permission to this player.
     *
     * @param holder     The player that will receive this permission.
     * @param permission The permission to add.
     */
	public abstract void addPermission(Player holder, String permission);

    /**
     * Removes a permission from this player.
     *
     * @param holder     The player that will have this permission revoked.
     * @param permission The permission to remove.
     */
	public abstract void removePermission(Player holder, String permission);

	
	
    public abstract void addGroupPermission(Player holder, String groupPermission);
    
    
    
    public abstract void removeGroupPermission(Player holder, String groupPermission);
	
    
    
	/**
	 * Lists all permissions that a player has.
	 * 
	 * @param holder	The player that will be listed.
	 * @param detailed	If true, the additonal information about the permission 
	 * 					will be "added" after the atual permission following :: and 
	 * 					each item separated with a :.  A detailed listing should
	 * 					never be used for checking permissions, but could be useful
	 * 					for displaying and debugging permissions for a player.
	 * @return
	 */
	public abstract List<String> getPermissions(Player holder, boolean detailed);
}
