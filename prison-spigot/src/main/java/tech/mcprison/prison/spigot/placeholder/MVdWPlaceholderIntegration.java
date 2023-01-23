package tech.mcprison.prison.spigot.placeholder;

/**
 * <p>This hooks up the registration when the Prison plugin starts to run.
 * The MVdWPlaceholderIntegrationWrapper sets up the registrations. It should
 * be noted that the registrations used to occur within the ChatHandler, but
 * that did not make sense since these should be self-contained with the 
 * registration process if these plugins are active.
 * </p>
 * 
 * <p>LuckPerms v5 documentation notes on MVdWPlaceholderAPI:
 * 
 * To use the LuckPerms placeholders in plugins which support Maximvdw's MVdWPlaceholderAPI, 
 * you need to install the LuckPerms placeholder hook plugin.
 * https://github.com/lucko/LuckPerms/wiki/Placeholders
 * </p>
 *
 */
public class MVdWPlaceholderIntegration 
//	extends PlaceholderIntegration 
	{

//    private MVdWPlaceholderIntegrationWrapper placeholderWrapper;
//	
//    public MVdWPlaceholderIntegration() {
//    	super( "MVdWPlaceholderAPI", "MVdWPlaceholderAPI" );
//    }
	
//	@Override
//	public void integrate() {
//		if ( isRegistered()) {
//			try {
//				if ( Bukkit.getPluginManager().isPluginEnabled(getProviderName())) {
//					
//					
//					// The integration was written for MVdW v3.0.0, but if used with older versions
//					// it will fail.
//
//					// This will fail if the version of mvdw is v2.x.x, which is what we want:
//					Class.forName("be.maximvdw.placeholderapi.PlaceholderAPI", false, getClass().getClassLoader());
//					
//					MVdWPlaceholderIntegrationWrapper wrap = new MVdWPlaceholderIntegrationWrapper(getProviderName());
//					
//					placeholderWrapper = wrap;
//					
//					PrisonAPI.getIntegrationManager().addDeferredInitialization( this );
//				}
//			}
//			catch ( NoClassDefFoundError e ) {
//				// ignore this exception since it means the plugin was not loaded
//				Output.get().logWarn( "Attempted to enable the MVdWPlaceholderIntegration but it failed to find the " +
//						"class 'be.maximvdw.placeholderapi.PlaceholderAPI'. This could happen when using " +
//						"MVdWPlaceholderApi v2.x.x.  Prison ONLY support MVdW v3.x.x.  " +
//						"&c****&7  Try using PlaceholderAPI (papi) instead.  &c****" );
//			}
//			catch ( IllegalStateException e ) {
//				// ignore ... plugin is not loaded
//			}
//			catch ( Exception e ) {
//				e.printStackTrace();
//			}
//		}
//	}
	
	
//	/**
//	 * <p>Register both the player and mines placeholders with the MVdW plugin.
//	 * </p>
//	 */
//    @Override
//	public void deferredInitialization()
//	{
//    	boolean registered = false;
//    	if ( PrisonRanks.getInstance() != null && PrisonRanks.getInstance().isEnabled() ) {
//    		
//    		PlayerManager pm = PrisonRanks.getInstance().getPlayerManager();
//    		if ( pm != null ) {
//    			List<PlaceHolderKey> placeholderPlayerKeys = pm.getTranslatedPlaceHolderKeys();
//    			
//    			for ( PlaceHolderKey placeHolderKey : placeholderPlayerKeys ) {
//    				if ( !placeHolderKey.getPlaceholder().isSuppressed() ) {
//    					
//    					
//    					registerPlaceholder(placeHolderKey.getKey(), 
//    							player -> {
//    								
//    								PlaceholderIdentifier identifier = new PlaceholderIdentifier( placeHolderKey.getPlaceholder().name() );
//    								identifier.setPlayer( player.getUUID(), player.getName() );
//    								
//    								if ( identifier.checkPlaceholderKey(placeHolderKey) ) {
//    									
//    									pm.getTranslatePlayerPlaceHolder( identifier );
//    								}
//    								
//
//    								return Text.translateAmpColorCodes( identifier.getText() );
//    							});
//    					if ( !registered ) {
//    						registered = true;
//    					}
//    				}
//    			}
//    		}
//    		
//    		RankManager rm = PrisonRanks.getInstance().getRankManager();
//    		if ( rm != null ) {
//    			List<PlaceHolderKey> placeholderPlayerKeys = rm.getTranslatedPlaceHolderKeys();
//    			
//    			for ( PlaceHolderKey placeHolderKey : placeholderPlayerKeys ) {
//    				if ( !placeHolderKey.getPlaceholder().isSuppressed() ) {
//    					
//    					
//    					registerPlaceholder(placeHolderKey.getKey(), 
//    							player -> {
//    								
//    								PlaceholderIdentifier identifier = new PlaceholderIdentifier( placeHolderKey.getPlaceholder().name() );
//    								identifier.setPlayer( player.getUUID(), player.getName() );
//    								
//    								if ( identifier.checkPlaceholderKey(placeHolderKey) ) {
//    									
//    									rm.getTranslateRanksPlaceHolder( identifier );
//    								}
//    								
//
//    								return Text.translateAmpColorCodes( identifier.getText() );
//    							});
//    					if ( !registered ) {
//    						registered = true;
//    					}
//    				}
//    			}
//    		}
//    	}
//    	
//
//    	if ( PrisonMines.getInstance() != null && PrisonMines.getInstance().isEnabled() ) {
//    		MineManager mm = PrisonMines.getInstance().getMineManager();
//    		if ( mm != null ) {
//    			List<PlaceHolderKey> placeholderMinesKeys = mm.getTranslatedPlaceHolderKeys();
//    			
//    			for ( PlaceHolderKey placeHolderKey : placeholderMinesKeys ) {
//    				if ( !placeHolderKey.getPlaceholder().isSuppressed() ) {
//    					
//    					registerPlaceholder(placeHolderKey.getKey(),
//    							player -> {
//    								
//    								PlaceholderIdentifier identifier = new PlaceholderIdentifier( placeHolderKey.getPlaceholder().name() );
//    								identifier.setPlayer( player.getUUID(), player.getName() );
//    								
//    								if ( identifier.checkPlaceholderKey(placeHolderKey) ) {
//    									
//    									mm.getTranslateMinesPlaceholder( identifier );
//    								}
//    								
//    								return Text.translateAmpColorCodes( identifier.getText() );
//    							} );
//    					if ( !registered ) {
//    						registered = true;
//    					}
//    				}
//    			}
//    		}
//    	}
//    	
////    	if ( registered ) {
////    		Output.get().logWarn( "Prison registered all placeholders with MVdWPlaceholderAPI, " +
////    						"but unfortunately MVdWPlaceholderAPI does not support dynamic placeholders " +
////    						"that are available for customizations within prison.  Please try adding " +
////    						"Vault and PlaceholderAPI (papi) to your setup, if they do not already exist, " +
////    						"to enable these features.");
////    	}
//	}
//
//    
//	@Override
//    public void registerPlaceholder(String placeholder, Function<Player, String> action) {
//        if (placeholderWrapper != null) {
//        	placeholderWrapper.registerPlaceholder( placeholder, action );
//        }
//    }
//	
//    @Override
//    public boolean hasIntegrated() {
//        return (placeholderWrapper != null);
//    }
//    
//    @Override
//    public void disableIntegration() {
//    	placeholderWrapper = null;
//    }
//    
//    @Override
//    public String getAlternativeInformation() {
//    	return null;
//    }
//    
//
//	@Override
//	public String getPluginSourceURL() {
//		return "https://www.spigotmc.org/resources/mvdwplaceholderapi.11182/";
//	}
    
}
