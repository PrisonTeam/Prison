package tech.mcprison.prison.integration;

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

}
