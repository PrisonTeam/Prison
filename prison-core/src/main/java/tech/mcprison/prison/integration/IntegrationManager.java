package tech.mcprison.prison.integration;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import tech.mcprison.prison.chat.FancyMessage;
import tech.mcprison.prison.internal.block.PrisonBlock.PrisonBlockType;
import tech.mcprison.prison.output.DisplayComponent;
import tech.mcprison.prison.output.FancyMessageComponent;
import tech.mcprison.prison.output.Output;
import tech.mcprison.prison.output.TextComponent;
import tech.mcprison.prison.placeholders.PlaceholderManager.PrisonPlaceHolders;

/**
 * The IntegrationManager stores instances of each {@link Integration} and allows
 * them to be registered and retrieved.
 *
 * @author Faizaan A. Datoo
 * @since API 1.0
 */
public class IntegrationManager {

    private Map<IntegrationType, List<Integration>> integrations;
    
    private List<Integration> deferredIntegrations;

    public IntegrationManager() {
        this.integrations = new HashMap<>();
        this.deferredIntegrations = new ArrayList<>();
    }


	/**
     * Returns a list of all of the {@link Integration}s that are registered under a certain {@link IntegrationType}, if any.
     * This includes integrations that have not successfully integrated.
     * If there are none, an empty list will be returned.
     *
     * @param type The desired {@link IntegrationType}.
     * @return A list.
     */
    public List<Integration> getAllForType(IntegrationType type) {
        return integrations.getOrDefault(type, Collections.emptyList());
    }

    /**
     * Returns an optional containing the first working {@link Integration} for the specified {@link IntegrationType}.
     * If there are no working integrations, the optional will be empty.
     *
     * @param type The desired {@link IntegrationType}.
     * @return An optional containing the first working integration, or empty if none are found.
     */
    public Optional<Integration> getForType(IntegrationType type) {
        if(!integrations.containsKey(type)) {
            return Optional.empty();
        }
        return integrations.get(type).stream().filter(Integration::hasIntegrated).findFirst();
    }

    /**
     * Returns true if there are any working {@link Integration}s registered for a specific {@link IntegrationType}.
     *
     * @param type The desired {@link IntegrationType}.
     * @return true if there is any working {@link Integration} registered, false otherwise.
     */
    public boolean hasForType(IntegrationType type) {
        return getForType(type).isPresent();
    }

    /**
     * Registers an {@link Integration}.
     * @param i The {@link Integration}.
     */
    public void register(Integration i) {
    	IntegrationType iType = i.getType();
    	if ( !integrations.containsKey( iType ) ) {
    		integrations.put(iType, new ArrayList<>());
    	}
    	
    	// If integration is already stored, remove the older one.  Check on keyName:
    	List<Integration> regIntegrations = integrations.get(iType);
    	for ( int x = 0; x < regIntegrations.size(); x++ ) {
    		if ( i.getKeyName().equals( regIntegrations.get(x).getKeyName() ) ) {
    			regIntegrations.remove( x );
    			break;
    		}
    	}
    	integrations.get(iType).add(i);
    }
    
    public PermissionIntegration getPermission() {
    	return (PermissionIntegration) getForType(IntegrationType.PERMISSION)
							.orElse( null );
    }
    
    public EconomyIntegration getEconomy() {
    	return (EconomyIntegration) getForType(IntegrationType.ECONOMY)
    			.orElse( null );
    }

    public EconomyCurrencyIntegration getEconomyForCurrency(String currency) {
    	EconomyCurrencyIntegration results = null;
    	
        if(integrations.containsKey(IntegrationType.ECONOMY)) {
        	
        	List<Integration> econs = getAllForType(IntegrationType.ECONOMY);

        	for ( Integration econ : econs ) {
				if ( econ.hasIntegrated() && econ instanceof EconomyCurrencyIntegration ) {
					
					EconomyCurrencyIntegration currencyEcon = (EconomyCurrencyIntegration) econ;
					
					if ( currencyEcon.hasCurrency( currency )) {
						results = currencyEcon;
						break;
					}
				}
			}
        
        }
        return results;
    }
    
    

	public CustomBlockIntegration getCustomBlockIntegration( PrisonBlockType blockType )
	{
    	CustomBlockIntegration results = null;
    	
        if(integrations.containsKey(IntegrationType.CUSTOMBLOCK)) {
        	
        	List<Integration> cbIntegrations = getAllForType(IntegrationType.CUSTOMBLOCK);

        	for ( Integration cbIntegration : cbIntegrations ) {
				if ( cbIntegration.hasIntegrated() && cbIntegration instanceof CustomBlockIntegration ) {
					
					CustomBlockIntegration customBlock = (CustomBlockIntegration) cbIntegration;
					
					if ( customBlock.getBlockType() == blockType ) {
						results = customBlock;
						break;
					}
				}
			}
        
        }
        return results;
		
	}

	
    
    public String getIntegrationDetails( IntegrationType integrationType ) {
    	StringBuilder sb = new StringBuilder();
    	Set<IntegrationType> keys = integrations.keySet();
    	
    	for ( IntegrationType key : keys ) {
    		if ( key == integrationType ) {
    			sb.append( key.name() );
    			sb.append( ": [" );
    			
    			StringBuilder sb2 = new StringBuilder();
    			List<Integration> integrates = integrations.get( key );
    			for ( Integration i : integrates ) {
    				if ( sb2.length() > 0 ) {
    					sb2.append( ", " );
    				}
    				sb2.append( i.getDisplayName() );
    				sb2.append( " (registered=" );
    				sb2.append( i.isRegistered() );
    				sb2.append( ", integrated=" );
    				sb2.append( i.hasIntegrated() );
    				sb2.append( ")" );
    				if ( i.getDebugInfo() != null && i.getDebugInfo().trim().length() > 0 ) {
    					sb2.append( " Debug: {" );
    					sb2.append( i.getDebugInfo() );
    					sb2.append( "}" );
    				}
    			}
    			
    			sb.append( sb2 );
    			sb.append( "] " );
    		}
		}
    	return sb.toString();
    }
    
    /**
     * <p>This function formats all the Integrations, both active and inactive, to be displayed
     * to a user, or sent to the server logs.  This function returns a List to provide a 
     * degree of flexibility in where it is being used, without this container 
     * (The integration manager) having to know where the data will be used. This function 
     * keeps the business logic of relationship of integrations to Integration Types 
     * internal so as to no leak that knowledge out of this function. 
     * </p>
     * @param isBasic 
     * 
     * @return
     */
    public List<DisplayComponent> getIntegrationComponents(boolean isBasic) {
    	List<DisplayComponent> results = new ArrayList<>();
    	
        for ( IntegrationType integrationType : IntegrationType.values() )
		{
			if ( integrationType == IntegrationType.WORLDGUARD ) {
				// Skip this integration type:
				break;
			} 
			
			boolean activeIntegration = false;
			
        	results.add( new TextComponent( String.format( "&7Integration Type: &3%s", integrationType.name() ) ));

        	// Generates the placeholder list for the /prison version command, printing
        	// two placeholders per line.
			if ( integrationType ==  IntegrationType.PLACEHOLDER ) {
				results.add(  new TextComponent( ". . &7To list all or search for placeholders see: " +
						"&a/prison placeholders") );
//				getPlaceholderTemplateList( results );
			}
			
			List<Integration> plugins = getAllForType( integrationType );
			
			if ( integrationType == IntegrationType.WORLDGUARD && 
					(plugins == null || plugins.size() == 0 ) ) {
				results.add( new TextComponent( ". . &e&oWorldGuard integration has not been added " +
						"to Prison yet.&3 WorldGuard can still be used normally since this " +
						"is not an error." ));
			} 
			else if ( plugins == null || plugins.size() == 0 ) {
				results.add( new TextComponent( "&e. . none" ));
				activeIntegration = true;
			} 
			else {
				for ( Integration plugin : plugins ) {
					
					if ( isBasic && plugin.hasIntegrated() || !isBasic ) {
						activeIntegration = true;
						
						String pluginUrl = plugin.getPluginSourceURL();
						String msg = String.format( "&a. . %s <%s> %s", plugin.getDisplayName(),
								( plugin.hasIntegrated() ? "Active" : "Inactive"),
								( pluginUrl == null ? "" : "&7[URL]"));
						FancyMessage fancy = new FancyMessage( msg );
						if ( pluginUrl != null ) {
							fancy.command( pluginUrl ).tooltip( "Click to open URL for this plugin.", pluginUrl );
						}
						results.add( new FancyMessageComponent(fancy) );
						
						String altInfo = plugin.getAlternativeInformation();
						if ( altInfo != null ) {
							results.add( new TextComponent( ". . . . " + altInfo ));
						}
						
						if ( integrationType ==  IntegrationType.ECONOMY && 
								plugin instanceof EconomyCurrencyIntegration ) {
							EconomyCurrencyIntegration econ = (EconomyCurrencyIntegration) plugin;
							
							StringBuilder sb = new StringBuilder();
							
							for ( String currency : econ.getSupportedCurrencies().keySet() ) {
								Boolean supported = econ.getSupportedCurrencies().get( currency );
								if ( supported.booleanValue() ) {
									if ( sb.length() > 0 ) {
										sb.append( " " );
									}
									sb.append( currency );
								}
							}
							
							if ( sb.length() > 0 ) {
								results.add( new TextComponent( ". . . &3Currencies: " + sb.toString() ));
							}
						}
					}
				}
			}
			
			if ( isBasic && !activeIntegration ) {
				
				results.add( new TextComponent( String.format( "&7. . No active Integrations of this Type." ) ));
				
			}
		}
    	
    	return results;
    }

    /**
     * This adds the list of placeholders to the results List.
     * @param results
     */
	public void getPlaceholderTemplateList( List<DisplayComponent> results )
	{
		//results.add( new TextComponent( "  &7Available PlaceHolders: " ));

		List<String> placeholders = PrisonPlaceHolders.getAllChatList(true);
//		StringBuilder sb = new StringBuilder();
		for ( String placeholder : placeholders ) {
			results.add( new TextComponent( "  " + placeholder ));
			
//			if ( sb.length() == 0) {
//				sb.append( "      " );
//				sb.append( placeholder );
//			} 
//			else if ( (sb.length() + placeholder.length()) > 90) {
//				// will be too long combined so write existing sb then start over:
//				results.add( new TextComponent( sb.toString() ));
//				sb.setLength( 0 );
//				
//				sb.append( "      " );
//				sb.append( placeholder );
//			} else {
//				sb.append( placeholder );
//				results.add( new TextComponent( sb.toString() ));
//				sb.setLength( 0 );
//			}
		}
//		if ( sb.length() > 0 ) {
//			results.add( new TextComponent( sb.toString() ));
//		}
	}

	public List<Integration> getDeferredIntegrations() {
		return deferredIntegrations;
	}
	public void setDeferredIntegrations( List<Integration> deferredIntegrations ) {
		this.deferredIntegrations = deferredIntegrations;
	}

	public void addDeferredInitialization( Integration defferedIntegration ) {
		getDeferredIntegrations().add( defferedIntegration );
	}

	public void register( Integration integration, boolean isRegistered, String version )
	{
		try {
			
			integration.setRegistered( isRegistered );
			integration.setVersion( version );
			
			integration.integrate();
			
			if ( integration.hasIntegrated() ) {
				register(integration );
			}
//    	else {
//    		boolean deferredRemoved = getDeferredIntegrations().remove( integration );
//    		
//    		Output.get().logWarn( 
//    				String.format( "Warning: An integration that is registered with bukkit " +
//    				"failed to integrate: %s %s %s[%s]", 
//    				integration.getKeyName(), integration.getVersion(),
//    				( deferredRemoved ? "(Deferred Processing Removed) " : "" ),
//    				(integration.getDebugInfo() == null ? 
//    							"no debug info" : integration.getDebugInfo()) ));
//    	}
		}
		catch ( Exception e ) {
    		boolean deferredRemoved = getDeferredIntegrations().remove( integration );
    		
    		removeIntegration( integration );

    		Output.get().logWarn( 
    				String.format( "Warning: An integration caused an error while loading. " +
    				"Disabling the integration to protect Prison: %s %s %s[%s]", 
    				integration.getKeyName(), integration.getVersion(),
    				( deferredRemoved ? "(Deferred Processing Removed) " : "" ),
    				(integration.getDebugInfo() == null ? 
    							"no debug info" : integration.getDebugInfo()) ));
			
		}
    	
	}


	/**
	 * <p>This will perform a best effort to remove an integration.  It will not remove it from
	 * the deferred list, but it would not be able to run.
	 * </p>
	 * 
	 * @param integration
	 */
	public void removeIntegration( Integration integration )
	{
		// make sure it's disabled:
		integration.setRegistered( false );
		integration.disableIntegration();
		
		integrations.get( integration.getType() ).remove( integration );
	}


}
