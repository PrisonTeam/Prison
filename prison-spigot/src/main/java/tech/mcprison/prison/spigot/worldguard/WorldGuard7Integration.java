package tech.mcprison.prison.spigot.worldguard;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

import tech.mcprison.prison.integration.WorldGuardIntegration;
import tech.mcprison.prison.output.Output;

/**
 * <p>WorldGuard 7 uses com.sk89q.worldguard.bukkit.WorldGuardPlugin.
 * </p>
 *
 */
public class WorldGuard7Integration
	extends WorldGuardIntegration {

	public WorldGuard7Integration() {
		super( "WorldGuard7", "WorldGuard" );
		
	}

	@Override
	public void integrate() {
		if ( isRegistered()) {
			try {
				Plugin wgPlugin = Bukkit.getPluginManager().getPlugin(getProviderName());
				if ( wgPlugin != null ) {
					String version = wgPlugin.getDescription().getVersion();

					// If the version starts with 7.x.y then this is the correct
					// version of WorldGuard to use with the integration:
					setIntegrated( version.startsWith( "7." ) );
					
					String message = String.format( "WorldGuardIntegration: keyName=%s version=%s",
								getKeyName(), version);
					
					Output.get().logInfo( message );
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
