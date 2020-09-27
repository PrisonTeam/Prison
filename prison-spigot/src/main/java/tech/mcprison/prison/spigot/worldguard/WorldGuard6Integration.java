package tech.mcprison.prison.spigot.worldguard;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

import tech.mcprison.prison.integration.WorldGuardIntegration;
import tech.mcprison.prison.output.Output;

/**
 * <p>WorldGuard 6 uses com.sk89q.worldguard.bukkit.WorldGuardPlugin.
 * </p>
 * 
 * <ul>
 *   <li>WorldGuard v6.1.0 - mc 1.7 to 1.8</li>
 *   <li>WorldGuard v6.1.2 - mc 1.9 to 1.10</li>
 *   <li>WorldGuard v6.2.0 - mc 1.9 to 1.11</li>
 *   <li>WorldGuard v6.2.2 - mc 1.12</li>
 *   <li>WorldGuard v7.0.0 - mc 1.13 to 1.14</li>
 *   <li>WorldGuard v7.0.1 - mc 1.14</li>
 *   <li>WorldGuard v7.0.2 - mc 1.15</li>
 * </ul>
 *
 */
public class WorldGuard6Integration
	extends WorldGuardIntegration {

	@SuppressWarnings( "unused" )
	private WorldGuard6IntegrationWrapper wrapper = null; 
	
	public WorldGuard6Integration() {
		super( "WorldGuard6", "WorldGuard" );
		
	}

	@Override
	public void integrate() {
		if ( isRegistered()) {
			try {
				Plugin wgPlugin = Bukkit.getPluginManager().getPlugin(getProviderName());
				if ( wgPlugin != null ) {
					String version = wgPlugin.getDescription().getVersion();
					
					// If the version starts with 6.x.y then this is the correct
					// version of WorldGuard to use with the integration:
					setIntegrated( version.startsWith( "6." ) );

					String message = String.format( "WorldGuardIntegration: keyName=%s version=%s",
								getKeyName(), version);
				
					Output.get().logInfo( message );
					
					if ( hasIntegrated() ) {
						this.wrapper = new WorldGuard6IntegrationWrapper( wgPlugin );
					}
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



	
}
