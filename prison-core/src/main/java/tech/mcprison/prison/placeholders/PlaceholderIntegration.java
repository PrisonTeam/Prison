package tech.mcprison.prison.placeholders;

import java.util.function.Function;

import tech.mcprison.prison.integration.IntegrationCore;
import tech.mcprison.prison.integration.IntegrationType;
import tech.mcprison.prison.internal.Player;

/**
 * An integration into a placeholder plugin.
 */
public abstract class PlaceholderIntegration 
	extends IntegrationCore {

	public PlaceholderIntegration( String keyName, String providerName ) {
		super( keyName, providerName, IntegrationType.PLACEHOLDER );
		
	}
	
	public abstract void registerPlaceholder(String placeholder, Function<Player, String> action);

}
