package tech.mcprison.prison.integration;

import java.util.function.Function;

import tech.mcprison.prison.internal.Player;

/**
 * An integration into a placeholder plugin.
 */
public abstract class PlaceholderIntegration 
	extends Integration {

	public PlaceholderIntegration( String keyName, String providerName ) {
		super( keyName, providerName, IntegrationType.PLACEHOLDER );
		
	}
	
	public abstract void registerPlaceholder(String placeholder, Function<Player, String> action);

}
